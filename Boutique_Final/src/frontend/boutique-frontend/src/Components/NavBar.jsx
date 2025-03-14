import React from 'react';
import '../styles/NavBar.css'

const NavBar = () => {
    return (
        <header className="navbar">
            <div className="top-banner">
                <p>Exclusive For Women!!!</p>
                <p>Exclusive For Silks!!!</p>
            </div>
            <div className="main-nav">
                <div className="logo">
                    <img src="../pages/logo.png" alt="Lotus Boutique Logo" />
                    <h1>LOTUS BOUTIQUE</h1>
                </div>
                <ul className="menu">
                    <li><a href="#home">Home</a></li>
                    <li><a href="#women">Women</a></li>
                    <li><a href="#men">Men</a></li>
                    <li><a href="#kids">Kids</a></li>
                    <li><a href="#support">Support</a></li>
                    <li><a href="#about-us">About Us</a></li>
                    <li><a href="#lookbooks">Look Books</a></li>
                </ul>
                <div className="icons">
                    <input type="text" placeholder="Search..." />
                    <i className="icon">👤</i> {/* Replace with appropriate icons */}
                    <i className="icon">❤️</i>
                    <i className="icon">🛒</i>
                </div>
            </div>
        </header>
    );
};

export default NavBar;
