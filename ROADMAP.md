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

---

## 🧭 Development Roadmap

### ✅ Phase 1: Project Setup
1. Scaffold Spring Boot backend (`spring init` or Spring Initializr).
2. Create Electron + React frontend using Vite or Create React App.
3. Configure CORS and API routing between Electron and Java backend.

### Phase 2: Geometry Core
- [x] Implement polygon and NFP support using JTS in `GeometryUtils`.
- [x] Parse SVG files into geometry structures with `SvgParser`.
- [x] Implement DXF parsing via JDxf or custom parser in `DxfParser`.
- [x] Add spacing, rotation, and material sheet helpers.

### Phase 3: Nesting Engine
- [ ] Replace `NestingService` placeholder with a basic nesting algorithm.
- [ ] Score layouts based on material usage or bounding box.
- [ ] Iterate until no improvement for N iterations.

### Phase 4: Backend API
- [ ] Expand `/api/nest` to accept uploaded shapes and sheet config.
- [ ] `/api/status` — Optional async task monitoring.
- [ ] `/api/export` — Returns nested layout as SVG or DXF.

### Phase 5: Frontend Features
- [ ] Upload/import parts (SVG/DXF) and configure nesting params.
- [ ] Display preview of nested layout.
- [ ] Export nested layout.

### Phase 6: Integration & Packaging
- [ ] Bundle backend with Electron using Node `child_process` or REST.
- [ ] Use Electron Forge/Builder to create platform installers.
- [ ] Finalize MVP with basic validation, error handling, and docs.

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
