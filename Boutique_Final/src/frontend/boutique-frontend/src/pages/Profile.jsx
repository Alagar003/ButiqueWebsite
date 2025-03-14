import { useEffect, useState } from "react";

const Dashboard = () => {
    const [user, setUser] = useState(null);

    useEffect(() => {
        const fetchUser = async () => {
            const token = localStorage.getItem("token"); // Retrieve stored token
            if (!token) {
                console.log("No token found!");
                return;
            }

            try {
                const response = await fetch("http://localhost:8080/api/users/user", {
                    method: "GET",
                    headers: {
                        Authorization: `Bearer ${token}`, // Send token in headers
                        "Content-Type": "application/json",
                    },
                });

                if (!response.ok) {
                    throw new Error("Failed to fetch user details");
                }

                const data = await response.json();
                setUser(data); // Update state with user data
            } catch (error) {
                console.error(error.message);
            }
        };

        fetchUser();
    }, []);

    return (
        <div>
            <h2>Welcome, {user ? user.name : "Guest"}!</h2>
            {user && (
                <div>
                    <p>Email: {user.email}</p>
                    <p>Role: {user.role}</p>
                </div>
            )}
        </div>
    );
};

export default Dashboard;
