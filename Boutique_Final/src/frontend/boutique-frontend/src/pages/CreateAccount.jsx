import React, { useState } from 'react';
import '../styles/Login.css';
import accImage from '../images/saree-pictures-2d8qt1hau3xlfjdp-removebg-preview.png';
import { useNavigate } from 'react-router-dom';

const CreateAccount = () => {
    const [name, setName] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [errorMessage, setErrorMessage] = useState('');
    const navigate = useNavigate();

    const handleCreateAccount = async (e) => {
        e.preventDefault();

        try {
            const response = await fetch('http://localhost:8081/api/auth/register', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ name, email, password }),
            });

            if (response.ok) {
                const data = await response.text(); // Direct message on success
                alert(data || 'Account created successfully!');
                navigate("/confirm_email");
            } else {
                const errorResponse = await response.json(); // Parse JSON error response
                alert(errorResponse.message || 'An unexpected error occurred.');
            }
        } catch (error) {
            console.error('Error occurred:', error.message);
            alert('An unexpected error occurred. Please try again later.');
        }
    };


    return (
        <div className="login-body">
            <div className="login-container">
                <img className="image" src={accImage} alt="Login" />
                <div className="login-form">
                    <h2>CREATE ACCOUNT</h2>
                    <form onSubmit={handleCreateAccount}>
                        <label>Name</label>
                        <input
                            type="text"
                            placeholder="Enter your name"
                            value={name}
                            onChange={(e) => setName(e.target.value)}
                            required
                        />
                        <label>Email</label>
                        <input
                            type="email"
                            placeholder="Enter your email"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            required
                        />
                        <label>Password</label>
                        <input
                            type="password"
                            placeholder="Enter your password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            required
                        />
                        <button className="sign-in-button" type="submit">CREATE</button>
                        <a
                            href="#"
                            className="create-account"
                            onClick={(e) => {
                                e.preventDefault();
                                navigate('/home');
                            }}
                        >
                            Cancel
                        </a>
                    </form>
                </div>
            </div>
        </div>
    );
};

export default CreateAccount;
