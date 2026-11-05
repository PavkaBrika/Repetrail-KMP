---
name: git-commit-style
description: Guide for formatting Git commit messages in this project. Use this skill when asked to create, amend, or format a Git commit.
---

# Git Commit Message Style

When creating or amending Git commits for this project, you MUST adhere to the following rules:

1. **Language:** Write the commit message in **English**.
2. **Human Style:** Write naturally and clearly. Do NOT use conventional commit prefixes (e.g., `feat:`, `fix:`, `docs:`, `chore:`).
3. **Imperative Mood:** The commit message MUST start with a capitalized imperative verb (e.g., `Add`, `Create`, `Update`, `Fix`, `Remove`, `Refactor`). 
   - *Correct:* `Add navigation dependency`
   - *Incorrect:* `Added navigation dependency`
   - *Incorrect:* `Adds navigation dependency`
4. **Single Line Only:** Write concise, single-line commit messages without any description or body (preferably 50 characters or less).

## Examples

**Good:**
```
Add UserViewModel to handle profile data
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
