import React, { useState, useEffect } from "react";
import { Outlet, useNavigate, Link } from "react-router-dom";
import "../styles/Home.css";
import logo from "../images/logo.jpg";

const Layout = () => {
    const [user, setUser] = useState(null);
    const [showUserDetails, setShowUserDetails] = useState(false);
    const [cart, setCart] = useState([]);
    const [searchQuery, setSearchQuery] = useState("");
    const navigate = useNavigate();

    useEffect(() => {
        const token = localStorage.getItem("token");
        if (token) {
            fetchUserDetails();
            fetchCartDetails();
        }
    }, []);

    const fetchUserDetails = async () => {
        const token = localStorage.getItem("token");
        if (!token) return;

        try {
            const response = await fetch("http://localhost:8081/api/users/user", {
                method: "GET",
                headers: {
                    Authorization: `Bearer ${token}`,
                    "Content-Type": "application/json",
                },
            });

            if (!response.ok) throw new Error("Failed to fetch user details");

            const data = await response.json();
            console.log("🔍 API Response:", data); // Check API response

            setUser(data); // Set user state
            console.log("✅ Updated User State:", data); // Check if user.id is correct
        } catch (error) {
            console.error(error.message);
        }
    };


    const fetchCartDetails = async () => {
        try {
            const token = localStorage.getItem("token");
            if (!token || !user?.id) return; // Ensure user.id exists

            const response = await fetch(`http://localhost:8081/api/cart/${user.id}`, {
                headers: { Authorization: `Bearer ${token}` },
            });

            if (!response.ok) throw new Error("Failed to fetch cart");

            const cartData = await response.json();
            setCart(cartData.cartItems || []);
        } catch (error) {
            console.error("❌ Error fetching cart:", error);
        }

    };

    return (
        <div>
            {/* Header */}
            <header className="header">
                <div>
                    <h1>Exclusive For Women!!!</h1>
                </div>
                <div className="h-logo">
                    <img className="img-style" src={logo} alt="Lotus Boutique Logo" />
                    <h1>LOTUS BOUTIQUE</h1>
                </div>
                <div className="container3">
                    <h1>Exclusive for Silks!!!</h1>
                </div>
            </header>

            {/* Navbar */}
            <nav className="navbar">
                <ul className="menu">
                    <li><Link to="/">Home</Link></li>
                    <li><Link to="/support">Support</Link></li>
                    <li><Link to="/about">About Us</Link></li>

                    <div className="search-bar">
                        <input
                            type="text"
                            value={searchQuery}
                            onChange={(e) => setSearchQuery(e.target.value)}
                            placeholder="Search for products..."
                        />
                        <button onClick={() => navigate(`/search?query=${searchQuery}`)}>Search</button>
                    </div>

                    {/* User */}
                    <li>
                        {user ? (
                            <span onClick={() => setShowUserDetails(!showUserDetails)}>👤 {user.name}</span>
                        ) : (
                            <Link to="/login">Login</Link>
                        )}
                    </li>

                    {/* Cart */}
                    <li>
                        <Link to="/cart">🛒 Cart ({cart.length})</Link>
                    </li>

                    {/* Logout */}
                    {user && (
                        <li>
                            <Link
                                to="/login"
                                onClick={() => {
                                    localStorage.removeItem("token");
                                    navigate("/login");
                                    window.location.reload();
                                }}
                            >
                                🚪 Sign Out
                            </Link>
                        </li>
                    )}
                </ul>
            </nav>

            {/* Dynamic Content */}
            <Outlet />
        </div>
    );
};

export default Layout;
