import React from "react";
import { useCart } from "./CartContext";
import axios from "axios";

const Cart = () => {
    const { cart, setCart, clearCart, loading, error } = useCart();

    const handleIncreaseQuantity = async (productId) => {
        try {
            const token = localStorage.getItem("token");
            const response = await axios.post(
                `http://localhost:8081/api/cart/updateQuantity`,
                { productId, action: "increase" },
                { headers: { Authorization: `Bearer ${token}` } }
            );
            console.log("Updated cart from backend:", response.data);
            setCart(response.data.items); // Update cart with new state
        } catch (err) {
            console.error("Failed to increase quantity:", err.response?.data || err.message);
        }
    };

    const handleDecreaseQuantity = async (productId) => {
        try {
            const token = localStorage.getItem("token");
            const response = await axios.post(
                `http://localhost:8081/api/cart/updateQuantity`,
                { productId, action: "decrease" },
                { headers: { Authorization: `Bearer ${token}` } }
            );
            console.log("Updated cart from backend:", response.data);
            setCart(response.data.items); // Update cart with new state
        } catch (err) {
            console.error("Failed to decrease quantity:", err.response?.data || err.message);
        }
    };

    // Calculate total price dynamically
    const calculateTotalPrice = () => {
        return cart.reduce((total, item) => total + item.price * item.quantity, 0).toFixed(2);
    };

    if (loading) return <p>Loading your cart...</p>;
    if (error) return <p>{error}</p>;
    if (!cart || cart.length === 0) return <p>Your cart is empty!</p>;

    return (
        <div>
            <h1>Your Cart</h1>
            {cart.map((item) => (
                <div key={item.productId}>
                    <img src={item.image} alt={item.name} />
                    <h2>{item.name}</h2>
                    <p>Price per Item: ₹{item.price.toFixed(2)}</p>
                    <div>
                        <button onClick={() => handleDecreaseQuantity(item.productId)} disabled={item.quantity <= 1}>-</button>
                        <span> Quantity: {item.quantity} </span>
                        <button onClick={() => handleIncreaseQuantity(item.productId)}>+</button>
                    </div>
                    <p>Total for this Item: ₹{(item.price * item.quantity).toFixed(2)}</p>
                </div>
            ))}
            <h2>Total Price: ₹{calculateTotalPrice()}</h2>
            <button onClick={clearCart}>Clear Cart</button>
            <button onClick={() => alert("Order placed!")}>Order Now</button>
        </div>
    );
};

export default Cart;
