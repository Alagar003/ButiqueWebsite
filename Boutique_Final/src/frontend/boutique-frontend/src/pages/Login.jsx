import React, { useState } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import "../styles/Login.css";
import loginImage from "../images/login.jpg";
import {Link} from "@mui/material";

const Login = () => {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const navigate = useNavigate();
    const location = useLocation();

    const handleSubmit = async (e) => {
        e.preventDefault();

        try {
            const response = await fetch("http://localhost:8081/api/auth/login", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({ email, password }),
            });

            const data = await response.json();
            console.log("🔍 Login Response:", data); // Debugging

            if (response.ok) {
                if (data.token && data.user) {  // ✅ Ensure user object exists
                    localStorage.setItem("token", data.token);
                    localStorage.setItem("user", JSON.stringify(data.user)); // ✅ Store full user data

                    console.log("✅ Token & User saved:", data.token, data.user);
                    alert("Login successful!");
                    navigate("/");  // Redirect to home
                } else {
                    alert("Login successful but missing token or user data.");
                }
            } else {
                alert(data.message || "Login failed. Please check your credentials.");
            }
        } catch (error) {
            console.error("❌ Error during login:", error.message);
            alert("An error occurred. Please try again.");
        }
    };



    return (
        <div className="login-body">
            <div className="login-container">
                <img className="image" src={loginImage} alt="Login" />
                <div className="login-form">
                    <h2>LOG IN</h2>
                    <form onSubmit={handleSubmit}>
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
                        <Link className="forgot-password" onClick={() => window.location.href = '/forgot_password'}>Forgot Password?</Link>
                        <button className="sign-in-button" type="submit">
                            SIGN IN
                        </button>
                        <a className="create-account" onClick={() => window.location.href = '/signup'}>Create
                            Account</a>
                    </form>
                </div>
            </div>
        </div>
    );
};

export default Login;
