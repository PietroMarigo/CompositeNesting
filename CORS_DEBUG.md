# Debugging CORS Errors

## Problem
When attempting to call the backend from the frontend, the browser blocks the request with a CORS policy error:

```
Access to XMLHttpRequest at 'http://localhost:8080/api/nest' from origin 'http://localhost:5173' has been blocked by CORS policy: No 'Access-Control-Allow-Origin' header is present on the requested resource.
```

The backend response lacks the `Access-Control-Allow-Origin` header, so the browser refuses to expose the response to the frontend.

## Possible Causes
- Backend server does not send the required CORS headers.
- Spring Boot CORS configuration is missing or misconfigured.
- Development proxy or server setup bypasses expected CORS handling.

## Debugging Steps
1. **Inspect Response Headers** – Check the Network tab in browser devtools or run `curl -i http://localhost:8080/api/nest` to see which CORS headers are returned.
2. **Configure CORS in Spring Boot** – Add `@CrossOrigin` annotations or a global `CorsConfigurationSource` bean to specify allowed origins, methods and headers.
3. **Use a Dev Proxy** – Configure the Vite dev server to proxy `/api` requests to the backend to avoid cross‑origin requests during development.
4. **Test Outside the Browser** – Use `curl` or Postman to send the same request. If it succeeds, the issue is restricted to browser CORS enforcement.
5. **Check Server Logs** – Ensure the backend receives the request and that any CORS filters execute before the request is rejected.

These steps should help identify and resolve the missing CORS headers so the frontend can communicate with the backend.

