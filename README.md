# CompositeNesting

This repository contains a minimal scaffold for a nesting application.
The project is split into two modules:

- **backend/** – Spring Boot service providing REST endpoints and
  placeholder geometry utilities.
- **frontend/** – Electron + React desktop client generated with Vite.

## Development

### Backend

```
cd backend
./mvnw spring-boot:run
```

### Frontend

```
cd frontend
npm install
npm run dev
```

The frontend expects the backend to be running on `http://localhost:8080`.
