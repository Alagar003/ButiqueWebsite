import React from 'react';
import ReactDOM from 'react-dom/client'; // Correct import for React 18
import App from './App';
import './styles/main.css';

const root = ReactDOM.createRoot(document.getElementById('root')); // Updated API
root.render(
    <React.StrictMode>
        <App />
    </React.StrictMode>
);
