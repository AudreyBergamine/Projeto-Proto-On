import axios from 'axios';

const axiosInstance = axios.create({
  baseURL: 'http://localhost:8080/protoon/',
  // Outras configurações...
});

export default axiosInstance;