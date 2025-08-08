# CompositeNesting

This repository contains a minimal scaffold for a nesting application.
The project is split into two modules:

- **backend/** – Spring Boot service providing REST endpoints and
  placeholder geometry utilities.
- **frontend/** – Electron + React desktop client generated with Vite.

## Dependencies

The project requires the following tools to be installed:

- [Java Development Kit (JDK) 17](https://adoptium.net/)
- [Node.js 20+](https://nodejs.org/) and npm
- Git

The backend uses Maven with the included wrapper (`mvnw`) and pulls
its libraries automatically, including Spring Boot, JTS, and Batik.
The frontend's dependencies (React, Vite, Electron, etc.) are managed
through `npm`.

## Installation

Clone the repository and install the backend and frontend dependencies:

```bash
git clone <repository-url>
cd CompositeNesting

# Backend dependencies
cd backend
./mvnw install

# Frontend dependencies
cd ../frontend
npm install
```

## Running

### Backend

```
cd backend
./mvnw spring-boot:run
```

### Frontend

```
cd frontend
npm run dev
```

The frontend expects the backend to be running on `http://localhost:8080`.

## Testing

### Backend

```bash
cd backend
./mvnw test
```

### Frontend

```bash
cd frontend
npm test
```

The frontend currently has no automated tests; run the development
server (`npm run dev`) and test UI behavior manually.
