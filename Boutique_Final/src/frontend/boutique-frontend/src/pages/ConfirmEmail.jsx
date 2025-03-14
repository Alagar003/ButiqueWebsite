import React, { useState } from 'react';
import '../styles/Login.css';
import {useNavigate} from "react-router-dom";

const ConfirmEmail = () => {
    const [emailAddress, setEmailAddress] = useState(''); // State for email input
    const [token, setToken] = useState('');                // State for token input
    const [message, setMessage] = useState('');
    const navigate = useNavigate();

    const handleConfirmEmail = async (e) => {
        e.preventDefault();
        setMessage('');

        try {
            const response = await fetch('http://localhost:8081/api/auth/confirm-email', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    email: emailAddress.trim(),            // Use the email state value
                    confirmationCode: token.trim(),          // Use the token state value
                }),
            });

            if (response.ok) {
                const data = await response.text();
                setMessage(data || 'Email confirmed successfully! You can now log in.');
                navigate("/login");
            } else {
                const error = await response.text();
                setMessage(error || 'Invalid confirmation code.');
            }
        } catch (error) {
            console.error('Error occurred:', error.message);
            setMessage('An unexpected error occurred. Please try again later.');
        }
    };

    return (
        <center>
            <div className="login-form">
                <h2>Confirm Your Email</h2>
                <p>
                    Enter the token you received in your email below to confirm your email address.
                </p>
                <p>
                    If you didn’t receive the email, check your spam folder.
                </p>
                <form onSubmit={handleConfirmEmail}>
                    <label htmlFor="email">Email</label>
                    <input
                        type="email"
                        id="email"
                        placeholder="Enter your email"
                        value={emailAddress}
                        onChange={(e) => setEmailAddress(e.target.value)}
                        required
                    />
                    <label htmlFor="token">Confirmation Token</label>
                    <input
                        type="text"
                        id="token"
                        placeholder="Enter your token here"
                        value={token}
                        onChange={(e) => setToken(e.target.value)}
                        required
                    />
                    <button type="submit" className= "sign-in-button">Confirm Email</button>
                </form>
                {message && <p>{message}</p>}
            </div>
        </center>
            );
            };

            export default ConfirmEmail;
