import React, { createContext, useState, useContext, useEffect } from "react";
import axios from "axios";

const CartContext = createContext();

export const CartProvider = ({ children }) => {
    const [cart, setCart] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const [token, setToken] = useState(null);
    const [userId, setUserId] = useState(null);

    // Load token and userId when component mounts
    useEffect(() => {
        const storedToken = localStorage.getItem("token");
        const storedUser = JSON.parse(localStorage.getItem("user")) || {};

        console.log("🔍 Stored Token:", storedToken);
        console.log("🔍 Stored User:", storedUser);

        if (storedToken) {
            setToken(storedToken);
        }

        if (storedUser && storedUser.id) {
            setUserId(storedUser.id); // Extract correct user ID
        } else {
            console.error("⚠️ Invalid user data in localStorage:", storedUser);
        }
    }, []);

    // Fetch Cart Data once token and userId are set
    useEffect(() => {
        if (token && userId) {
            console.log("📦 Fetching cart for user:", userId);
            fetchCart();
        }
    }, [token, userId]);

    // Fetch cart function
    const fetchCart = async () => {
        setLoading(true);
        try {
            const response = await axios.get(`http://localhost:8081/api/cart/${userId}`, {

                headers: {
                    "Authorization": `Bearer ${token}`,
                    "Content-Type": "application/json"
                }
            });

            if (response.data && response.data.cartItems && Array.isArray(response.data.cartItems)) {
                setCart(response.data.cartItems);
            } else {
                console.log("Cart is empty or has no items", response.data);
                setCart([]);
            }
        } catch (error) {
            console.error("❌ Error fetching cart:", error.response?.data || error.message);
            setError("Failed to fetch cart data.");
        } finally {
            setLoading(false);
        }
    };


    // Add to Cart function
    const addToCart = async (productId, quantity = 1) => {
        if (!token || !userId) {
            setError("Please log in to add items to the cart.");
            console.error("⚠️ Token or userId missing:", { token, userId });
            return;
        }

        if (!productId || quantity <= 0) {
            setError("Invalid product ID or quantity.");
            console.error("⚠️ Invalid product ID or quantity:", { productId, quantity });
            return;
        }

        setLoading(true);
        try {
            const response = await axios.post(
                "http://localhost:8081/api/cart/add",
                { userId, productId, quantity },
                {
                    headers: {
                        Authorization: `Bearer ${token}`,
                        "Content-Type": "application/json",
                    },
                }
            );
            console.log("✅ Item added to cart:", response.data);
            setCart(response.data?.cartItems || []);
        } catch (err) {
            console.error("❌ Error adding to cart:", err.response?.data || err);
            setError("Failed to add item to the cart.");
        } finally {
            setLoading(false);
        }
    };

    return (
        <CartContext.Provider value={{ cart, addToCart, loading, error }}>
            {children}
        </CartContext.Provider>
    );
};

// Hook for consuming the cart context
export const useCart = () => {
    const context = useContext(CartContext);
    if (!context) {
        throw new Error("useCart must be used within a CartProvider");
    }
    return context;
};
