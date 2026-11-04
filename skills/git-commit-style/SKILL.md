---
name: git-commit-style
description: Guide for formatting Git commit messages in this project. Use this skill when asked to create, amend, or format a Git commit.
---

# Git Commit Message Style

When creating or amending Git commits for this project, you MUST adhere to the following rules:

1. **Language:** Write the commit message in **English**.
2. **Human Style:** Write naturally and clearly. Do NOT use conventional commit prefixes (e.g., `feat:`, `fix:`, `docs:`, `chore:`).
3. **Imperative Mood:** The subject line (first line) MUST start with a capitalized imperative verb (e.g., `Add`, `Create`, `Update`, `Fix`, `Remove`, `Refactor`). 
   - *Correct:* `Add navigation dependency`
   - *Incorrect:* `Added navigation dependency`
   - *Incorrect:* `Adds navigation dependency`
4. **Structure:**
   - **Subject Line:** A short summary of the changes (preferably 50 characters or less).
   - **Blank Line:** Leave one blank line between the subject and the body.
   - **Body (optional):** Provide details on *what* was changed and *why*, if the subject line is not self-explanatory.

## Examples

**Good:**
```
Add UserViewModel to handle profile data

Create the initial implementation of the user profile screen and 
connect it with the local database.
```

**Good:**
```
Update navigation research in MASTER_PLAN.md
```

**Bad (Uses prefix):**
```
feat: add UserViewModel
```

**Bad (Wrong tense):**
```
Added UserViewModel to handle profile data
```