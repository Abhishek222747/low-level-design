# Day 1 — Meeting Room Scheduler 

This folder is a **study walkthrough**, not a finished product dump.  
Read the notes in order. Code appears only after the idea is explained.

---

## How the 30 minutes are actually used

Interviewers are not grading “did you write Spring Boot.” They grade:

1. Did you **clarify the problem** instead of assuming?
2. Did you pick **clear entities and APIs**?
3. Did you get the **hard part** right (here: no double-booking)?
4. Can you talk about **what you’d add next** (concurrency, recurrence) without boiling the ocean?

A usable split:

| Time | What you do |
|------|-------------|
| 0–5 min | Requirements + constraints. Write them on the board. |
| 5–10 min | Entities, relationships, public APIs |
| 10–22 min | Core design: booking + conflict detection |
| 22–28 min | One deep dive (data structure / concurrency / cancellation) |
| 28–30 min | Extensions you would add if you had more time |

If you skip clarification and jump to classes, you look junior. If you design Kafka + microservices for an in-memory calendar, you look unfocused.

---

## Step 0 — What is the problem, in one sentence?

> Users book **rooms** for **time intervals**. A room cannot have two meetings whose times overlap.

Everything else (capacity, floor, projector, recurring meetings) is a **layer on top** of that sentence.

In the next files we implement that sentence first, then grow.
