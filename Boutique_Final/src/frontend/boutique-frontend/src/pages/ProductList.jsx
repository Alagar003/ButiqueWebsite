import React, { useEffect, useState } from "react";
import axios from "axios";
import { useNavigate, useParams, useLocation } from "react-router-dom";
import { useCart } from "./CartContext";

const ProductList = () => {
    const { category } = useParams();
    const location = useLocation();
    const navigate = useNavigate();
    const { addToCart } = useCart();

    const [products, setProducts] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    const query = new URLSearchParams(location.search).get("query");

    useEffect(() => {
        const fetchProducts = async () => {
            const token = localStorage.getItem("token");

            if (!token) {
                alert("No token found. Redirecting to login...");
                navigate("/login");
                return;
            }

            try {
                let response;
                if (query) {
                    response = await axios.get(
                        `http://localhost:8081/api/products/search?query=${query}`,
                        { headers: { Authorization: `Bearer ${token}` } }
                    );
                } else {
                    response = await axios.get(
                        `http://localhost:8081/api/products/category/${category}`,
                        { headers: { Authorization: `Bearer ${token}` } }
                    );
                }
                setProducts(response.data);
            } catch (err) {
                if (err.response?.status === 401) {
                    alert("Session expired. Please log in again.");
                    localStorage.removeItem("token");
                    navigate("/login");
                } else {
                    setError("Failed to load products. Please try again later.");
                }
            } finally {
                setLoading(false);
            }
        };

        fetchProducts();
    }, [category, query, navigate]);

    if (loading) return <div>Loading products...</div>;
    if (error) return <div>{error}</div>;

    return (
        <div>
            <h1 className= "heading">
                {query ? `Search Results for "${query}"` : `${category} Collection`}
            </h1>
            <div className="category-section">
                {products.map((product) => (
                    <div key={product.id}>
                        <div className="category">
                            <img src={product.image} alt={product.name} />
                            <h2>{product.name}</h2>
                            <p>{product.description}</p>
                            <p>₹{product.price}</p>
                            <button className= "sign-in-button" onClick={() => addToCart(product.id, 1)}>Add to Cart</button> {/* Fix */}
                        </div></div>
                ))}
            </div>
        </div>
    );
};

export default ProductList;
