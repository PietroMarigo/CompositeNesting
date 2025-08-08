
# 🤖 AGENT.md – Development Instructions for the Nesting Software Agent

Welcome, Agent! This document outlines your role and responsibilities in developing the nesting desktop software. You’ll be working based on the roadmap in `ROADMAP.md` and code-level comments throughout the project.

---

## 🧭 Mission Overview

You are responsible for building a 2D nesting application similar to Deepnest using the following stack:

- **Backend**: Java (Spring Boot) – geometry engine, nesting logic, file handling
- **Frontend**: Electron + React – user interface and visual layout preview

The overall goal is to develop a Minimum Viable Product (MVP) that loads part files, nests them onto a material sheet, and exports the final layout. The nesting engine currently relies on Minkowski-sum based no-fit polygons and a boundary search with hill climbing for placement refinement; future contributions should build upon this approach.

---

## 📂 Project Structure

```
nesting-app/
├── AGENT.md               ← You are here
├── ROADMAP.md             ← Overall dev roadmap
├── backend/               ← Java Spring Boot nesting engine
│   └── src/main/java/com/nestingapp/
├── frontend/              ← Electron + React user interface
│   └── src/
└── README.md              ← User-facing instructions
```

---

## 🧑‍💻 How to Work

### 🟩 1. Follow `ROADMAP.md`
- The roadmap gives a step-by-step high-level direction.
- Always work on one phase at a time unless instructed otherwise.

### 🟩 2. Read Code Comments Carefully
- Code files contain TODO and HINT comments to guide your implementation.
- Do not remove these comments until the feature is complete and tested.

### 🟩 3. Write Clean, Maintainable Code
- Use meaningful variable names and structure logic clearly.
- Comment your code where decisions or algorithms are non-obvious.
- Prefer readability over cleverness.

### 🟩 4. Keep Backend and Frontend Decoupled
- All interaction happens via REST APIs (hosted by Spring Boot backend).
- Frontend should not access backend code directly — only via HTTP.

### 🟩 5. Test Your Features
- Write unit tests for backend logic (geometry, nesting, file parsing).
- Manually test UI features during MVP; automated tests optional for now.

### 🟩 6. Avoid Redundant Files
- Only create new files when they are genuinely needed.
- When adding a file, choose a name clearly distinct from existing ones to prevent confusion.

---

## ⚠️ Constraints & Guidelines

- Stick to the libraries and stack defined in `ROADMAP.md`.
- Use version control frequently. Commit often with descriptive messages.
- Communicate clearly in comments if assumptions are made or blockers exist.

---

## ✅ Completion Criteria

You are done when the MVP has the following working:
- Importing SVG/DXF parts
- Displaying the shapes in the UI
- Configurable nesting (spacing, rotation)
- Generating a nested layout
- Exporting the result (SVG or DXF)
- Works on Windows, macOS, and Linux

---

## 📬 Final Notes

- If a task is unclear, look at both `ROADMAP.md` and code comments together.
- Write modular code so other agents (or humans) can extend it later.
- Prioritize working features over polishing during MVP stage.

Good luck, Agent. Your mission starts now.
