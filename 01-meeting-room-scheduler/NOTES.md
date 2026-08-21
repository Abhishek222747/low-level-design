# Teacher notes — follow these while coding

## Why we do not start with `MeetingRoomScheduler` class

Beginners often sketch:

```
Scheduler
  book(room, start, end)
  cancel(...)
  findAvailable(...)
```

That API is fine **later**. If you start there, you hide the real bug:  
**what does “overlap” mean, and who owns that rule?**

If overlap logic is copied into `book`, `findAvailable`, and `cancel`, you will get inconsistencies in the interview when they ask a follow-up.

So we extract one tiny type: **a time range that knows if it collides with another**.

---

## Interval convention (say this out loud in the interview)

We use **half-open intervals**: `[start, end)`.

- Meeting 10:00–11:00 occupies 10:00 inclusive, 11:00 exclusive.
- Next meeting can start at 11:00. That is **not** a conflict.

Why? Back-to-back meetings are normal. Closed intervals `[start, end]` would block 11:00 twice.

Times are stored as **epoch milliseconds** (`long`) so we never parse strings in core logic.

---

## Overlap rule (draw this)

Two ranges A and B overlap iff:

```
A.start < B.end  AND  B.start < A.end
```

They do **not** overlap if one ends exactly when the other starts.

Memorize this. Interviewers poke here first.

---

## Step 2 — Room vs Meeting (entities, not the scheduler)

Draw this on the board:

```
Room  1 -------- *  Meeting
                     |
                     has-a
                     |
                  TimeRange
```

**Room** = a thing that exists in the building (id, name, floor, capacity).  
**Meeting** = one booking (title, organizer, attendee count, roomId, time).

### Why meetings are not one giant global list

Conflict is **per room**. Alpha at 10:00 and Beta at 10:00 is valid.

If every meeting lives in one `List<Meeting>`:

- every check is “scan the world, then filter by room”
- you will forget the room filter under pressure and mark two different rooms as a clash

So the *idea* of a meeting includes `roomId`. The *storage* of meetings-by-room comes in Step 3.

### Why Room does not hold `List<Meeting>` yet

Beginners write `Room.book()`. That mixes two jobs:

1. describing the physical room
2. running a calendar

In an interview that looks clever for 30 seconds, then `findAvailableRooms` has nowhere clean to live (that question is about *many* rooms). Calendar logic belongs on a service that *uses* rooms.

### Why Meeting stores `roomId`, not a `Room` object

- The meeting does not own the room.
- `Meeting → Room` and `Room → List<Meeting>` is a cycle.
- Persistence and APIs work with ids anyway.

### Capacity

`Room.canFit(n)` is a **room fact**. We still do not reject bookings here — there is no `book()` yet. Step 3 will check capacity *and* overlap before inserting.

---

## Step 3 — The scheduler owns calendars

`book / cancel / findAvailable` live here, not on `Room`.

### Data layout (draw this)

```
MeetingScheduler
  rooms:      roomId -> Room
  calendars:  roomId -> TreeMap<start, Meeting>
  byId:       meetingId -> Meeting
```

Each room has its **own** ordered calendar. Alpha’s 10:00 never looks at Beta.

### Why TreeMap, not `List<Meeting>`

A list per room is honest and OK for a tiny office: scan that room, call `overlaps`. Complexity O(n) per book.

The interview upgrade: **the calendar is always clash-free**. A new `[start, end)` can only hit:

1. the meeting with the greatest start **≤** our start (`floorEntry`)
2. the meeting with the least start **≥** our start (`ceilingEntry`)

You do **not** walk every meeting on that room. `TreeMap` finds those two neighbors in O(log n).

Example: Alpha already has `[9, 10)` and `[12, 13)`. You want `[10, 12)`.

- floor of 10 → `[9, 10)` → ends when you start → no overlap
- ceiling of 10 → `[12, 13)` → starts when you end → no overlap  
  → book it. Back-to-back on both sides.

### `book()` order (say it as a checklist)

1. Room exists?
2. `room.canFit(attendeeCount)`?
3. Neighbors overlap?
4. Only then insert.

Capacity before calendar is cheaper; either order is correct.

### `cancel` and `byId`

Cancel is by meeting id. `byId` is an index so we do not search every room. Remove from both maps.

### What we still skip (say this at minute 28)

- Recurring meetings
- Concurrent `book()` (need a lock per room — describe it, do not have to code it)
- Persistence, notifications, waiting lists

