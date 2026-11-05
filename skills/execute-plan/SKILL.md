---
name: execute-plan
description: Workflow for executing implementation plans step-by-step with strict plan adherence, code review stops after each step, user approval, and Git commits following git-commit-style. Use this skill when executing an approved task list or implementation plan.
---

# Plan Execution Workflow

This skill defines the step-by-step workflow for executing implementation plans in this project.

## Core Rules

1. **Strict Adherence to Plan:**
   - Execute tasks strictly in the order defined in `task.artifact.md` and `implementation_plan.artifact.md`.
   - Do NOT deviate from the agreed scope or perform unplanned changes without prior approval.
   - Focus on **exactly one task** at a time.
   - **Do NOT rewrite or edit plan text:** When updating `task.artifact.md`, ONLY update the task checkbox states (`[ ]` -> `[/]` -> `[x]`). Never modify, rewrite, reformat, or delete task descriptions or plan text.

2. **Step-by-Step Execution:**
   - Before starting a task:
     - Mark it as in-progress (`[/]`) in `task.artifact.md`.
     - Call `set_plan_state(state="EXECUTING", currentTask="<short task description>")`.
   - Implement only the code and modifications required for the current task.
   - Run tests or verification relevant to that specific task.

3. **Mandatory Stop for Code Review:**
   - After completing a task:
     - Mark it as completed (`[x]`) in `task.artifact.md`.
     - **STOP execution immediately.** Do NOT proceed to the next task automatically. Do NOT commit code yet.
     - Present a clear, structured summary of the changes to the user for code review (list modified files, explain key logic, show test results).
     - Ask the user to review and approve the changes.

4. **Approval & Git Commit:**
   - If the user requests fixes or modifications:
     - Make the requested changes and present the updated work for review.
   - Once the user **approves** the completed step:
     - Create a Git commit for the changes using the project's **`git-commit-style`** skill (`skills/git-commit-style/SKILL.md`).
     - Strictly adhere to `git-commit-style` rules (English, single-line, capitalized imperative subject line, no `feat:`/`fix:` prefixes, no body description).
     - Proceed to the next task in the plan.

5. **Plan Completion:**
   - Once all tasks in `task.artifact.md` are completed and committed:
     - Call `set_plan_state(state="COMPLETED", verified=true)`.
     - Create or update `walkthrough.artifact.md` with a summary of accomplished work and test results.

## Execution Workflow Diagram

```mermaid
flowchart TD
    Start[Select Next Task from Plan] --> MarkInProgress[Mark Task in-progress [/] in task.artifact.md]
    MarkInProgress --> UpdateState[Call set_plan_state EXECUTING]
    UpdateState --> Implement[Implement Task & Verify]
    Implement --> MarkDone[Mark Task completed [x] in task.artifact.md]
    MarkDone --> ReviewStop[STOP: Present Code Review to User]
    ReviewStop --> UserFeedback{User Approved?}
    UserFeedback -- Fixes Requested --> Implement
    UserFeedback -- Approved --> Commit[Commit using git-commit-style skill]
    Commit --> CheckMore{More Tasks?}
    CheckMore -- Yes --> Start
    CheckMore -- No --> Complete[Call set_plan_state COMPLETED & update walkthrough]
```
