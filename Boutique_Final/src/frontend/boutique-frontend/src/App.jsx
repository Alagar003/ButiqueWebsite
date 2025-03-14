import React from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import { CartProvider } from "./pages/CartContext"; // ✅ Correct import
import Layout from "./Components/Layout";
import Home from "./pages/Home";
import Login from "./pages/Login";
import CreateAccount from "./pages/CreateAccount";
import ConfirmEmail from "./pages/ConfirmEmail";
import ForgetPassword from "./pages/ForgetPassword";
import ProductList from "./pages/ProductList";
import NotFound from "./pages/NotFound";
import Cart from "./pages/Cart";
import Dashboard from "./pages/Profile";

const App = () => {
    return (
        <CartProvider> {/* ✅ Wrap everything in CartProvider */}
            <Router>
                <Routes>
                    <Route path="/" element={<Layout />}>
                        <Route index element={<Home />} />
                        <Route path="login" element={<Login />} />
                        <Route path="signup" element={<CreateAccount />} />
                        <Route path="confirm_email" element={<ConfirmEmail />} />
                        <Route path="forgot_password" element={<ForgetPassword />} />
                        <Route path="products/:category" element={<ProductList />} />
                        <Route path="/search" element={<ProductList />} />
                        <Route path="cart" element={<Cart />} />
                        <Route path="*" element={<NotFound />} />
                        <Route path="/dashboard" element={<Dashboard />} />
                    </Route>
                </Routes>
            </Router>
        </CartProvider>
    );
};

export default App;
