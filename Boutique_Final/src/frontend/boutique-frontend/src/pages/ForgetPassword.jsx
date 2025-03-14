import React, { useState } from "react";
import "../styles/Login.css";
import { useNavigate } from "react-router-dom";

const ForgotPassword = () => {
    const [email, setEmail] = useState("");
    const [token, setToken] = useState("");
    const [newPassword, setNewPassword] = useState("");
    const [message, setMessage] = useState("");
    const [tokenSent, setTokenSent] = useState(false);
    const navigate = useNavigate();

    // Step 1: Handle sending the token
    const handleSendToken = async (e) => {
        e.preventDefault();
        setMessage("");
        try {
            const response = await fetch(
                "http://localhost:8081/api/auth/forgot-password",
                {
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify({ email }),
                }
            );

            if (!response.ok) {
                const errorText = await response.text();
                throw new Error(`Server Error: ${response.status} - ${errorText}`);
            }

            setMessage("A reset token has been sent to your email.");
            setTokenSent(true); // ✅ Update state to show token input fields
        } catch (error) {
            console.error("❌ Error:", error.message);
            setMessage("Failed to send reset token. Please try again.");
        }
    };

    // Step 2: Handle resetting the password
    const handleResetPassword = async (e) => {
        e.preventDefault();
        setMessage("");
        try {
            const response = await fetch(
                "http://localhost:8081/api/auth/reset-password",
                {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json",
                    },
                    body: JSON.stringify({
                        email: email.trim(),
                        resetToken: token.trim(), // ✅ Ensure key matches backend
                        newPassword: newPassword.trim(),
                    }),
                }
            );

            if (response.ok) {
                setMessage("Password reset successfully!");
                navigate("/login");
            } else {
                const error = await response.text();
                setMessage(error || "Failed to reset password.");
            }
        } catch (error) {
            console.error("Error occurred:", error.message);
            setMessage("An unexpected error occurred. Please try again later.");
        }
    };

    return (
        <div className="login-container">
            <div className="login-form">
                <h1>Forgot Password</h1>
                <form onSubmit={tokenSent ? handleResetPassword : handleSendToken}>
                    <label htmlFor="email">Email:</label>
                    <input
                        type="email"
                        id="email"
                        placeholder="Enter your email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        required
                        disabled={tokenSent}
                    />

                    {tokenSent && (
                        <>
                            <label htmlFor="token">Token:</label>
                            <input
                                type="text"
                                id="token"
                                placeholder="Enter the token you received"
                                value={token}
                                onChange={(e) => setToken(e.target.value)}
                                required
                            />

                            <label htmlFor="newPassword">New Password:</label>
                            <input
                                type="password"
                                id="newPassword"
                                placeholder="Enter your new password"
                                value={newPassword}
                                onChange={(e) => setNewPassword(e.target.value)}
                                required
                            />
                        </>
                    )}
                    <button type="submit" className="sign-in-button">
                        {tokenSent ? "Reset Password" : "Send Token"}
                    </button>
                </form>
                {message && <p>{message}</p>}
            </div>
        </div>
    );
};

export default ForgotPassword;
