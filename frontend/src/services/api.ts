import axios from 'axios';

/**
 * Axios instance preconfigured to talk to the backend API.
 */
const api = axios.create({
  baseURL: 'http://localhost:8080/api',
});

export default api;
