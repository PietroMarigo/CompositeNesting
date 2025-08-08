# 🛠️ Nesting App – MVP Development Plan

This document outlines the steps required to create a Minimal Viable Product (MVP) for a nesting software inspired by Deepnest, using **Java** for the backend and **Electron + React** for the frontend.

---

## 📦 Tech Stack

### 🔧 Backend (Java)
- **Language**: Java 17+
- **Geometry**: [JTS Topology Suite](https://locationtech.github.io/jts/)
- **SVG Handling**: [Apache Batik](https://xmlgraphics.apache.org/batik/)
- **DXF Handling**: [JDxf](https://github.com/burkmarr/JDxf) or custom parser
- **Web Server**: [Spring Boot](https://spring.io/projects/spring-boot)
- **Build Tool**: Maven or Gradle

### 💻 Frontend (Electron + React)
- **Electron**: Desktop shell for Node + Chromium
- **React**: UI framework
- **TypeScript**: Static typing for JS
- **Axios**: HTTP client for API calls
- **SVG.js** or **React Konva**: For visualizing and manipulating shapes

---

## 📁 File Structure

nesting-app/
├── backend/
│ ├── src/main/java/
│ │ └── com/nestingapp/
│ │ ├── NestingService.java
│ │ ├── GeometryUtils.java
│ │ ├── SvgParser.java
│ │ ├── DxfParser.java
│ │ └── NestingController.java
│ └── pom.xml
│
├── frontend/
│ ├── public/
│ ├── src/
│ │ ├── components/
│ │ ├── views/
│ │ ├── services/
│ │ │ └── api.ts
│ │ └── App.tsx
│ ├── package.json
│ └── electron/
│ ├── main.js
│ └── preload.js
│
└── README.md

yaml
Copy
Edit

---

## 🧭 Development Roadmap

### ✅ Phase 1: Project Setup
1. Scaffold Spring Boot backend (`spring init` or Spring Initializr).
2. Create Electron + React frontend using Vite or Create React App.
3. Configure CORS and API routing between Electron and Java backend.

### ✅ Phase 2: Geometry Core
1. Implement polygon and NFP support using JTS.
2. Handle shape importing via Apache Batik (SVG) and JDxf (DXF).
3. Implement spacing, rotation, and material sheet logic.

### ✅ Phase 3: Nesting Engine
1. Basic brute-force or greedy nesting algorithm.
2. Score layouts based on material usage or bounding box.
3. Run loop until no improvement for N iterations.

### ✅ Phase 4: Backend API
1. `/api/nest` — Accepts uploaded shapes and sheet config.
2. `/api/status` — Optional async task monitoring.
3. `/api/export` — Returns nested layout as SVG or DXF.

### ✅ Phase 5: Frontend Features
1. Upload/import parts (SVG/DXF).
2. Configure nesting params (spacing, rotation step, quantity).
3. Display preview of nested layout.
4. Export nested layout.

### ✅ Phase 6: Integration & Packaging
1. Bundle backend with Electron using Node `child_process` or REST.
2. Use Electron Forge/Builder to create platform installers.
3. Finalize MVP with basic validation, error handling, and docs.

---

## ✅ MVP Completion Criteria
- [ ] Load SVG/DXF parts and display them.
- [ ] Accept nesting configuration.
- [ ] Generate a basic nested layout.
- [ ] Export nested layout to SVG.
- [ ] Cross-platform installer for Windows, macOS, Linux.

---

## 📚 Future Improvements
- Common-line cutting
- Multiple-sheet optimization
- Simulated annealing or genetic algorithm engine
- DXF preview in UI

---
