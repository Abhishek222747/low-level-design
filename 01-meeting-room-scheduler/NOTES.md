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
