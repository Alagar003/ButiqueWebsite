import React from 'react';
import '../styles/Home.css';
import banner from '../images/infashion-1690967784.png';
import women from '../images/Indian-Traditional-Clothing-min.jpg';
import men from '../images/GiiwWVAe_0c44b8c6cd354c10af411a0692f8c599.jpg';
import girl from '../images/cfe064dada1df3c62194fba860f86b2d.jpg';
import boy from '../images/istockphoto-524161848-612x612.jpg';
import { useNavigate } from "react-router-dom";

const Home = () => {
    const navigate = useNavigate();

    const handleNavigation = (category) => {
        navigate(`/products/${category}`);
    };

    return (
        <div>
            {/* Banner Section */}
            <div className="banner">
                <div>
                    <h1>Fashion is What You Buy,<br />
                        Style is What<br /> You Do with It.</h1>
                </div>
                <div>
                    <img className="banner-img" src={banner} alt="Family in traditional attire" />
                </div>
            </div>

            {/* Shop by Category Section */}
            <h1 className= "heading">Shop by Category</h1>
            <div className="category-section">
                <div className="category" onClick={() => handleNavigation('Women')}>
                    <img src={women} alt="Women Collections" />
                    <h3>Women Collections</h3>
                </div>
                <div className="category" onClick={() => handleNavigation('Men')}>
                    <img src={men} alt="Men Collections" />
                    <h3>Men Collections</h3>
                </div>
                <div className="category" onClick={() => handleNavigation('Girls')}>
                    <img src={girl} alt="Girls Collections" />
                    <h3>Girls Collections</h3>
                </div>
                <div className="category" onClick={() => handleNavigation('Boys')}>
                    <img src={boy} alt="Boys Collections" />
                    <h3>Boys Collections</h3>
                </div>
            </div>
        </div>
    );
};

export default Home;
