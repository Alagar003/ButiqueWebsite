import axios from 'axios';

const API = axios.create({
    baseURL: 'http://localhost:8081', // Adjust according to your backend
});

export default API;
