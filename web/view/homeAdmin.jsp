<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Admin - Role Management</title>
        <style>
            :root {
                --primary-color: #4A90E2; /* Soft blue for primary elements */
                --secondary-color: #50C878; /* Vibrant green for gradients */
                --accent-color: #F7C948; /* Warm yellow for highlights */
                --text-color: #2D3748; /* Dark gray for text */
                --card-bg: #FFFFFF; /* Clean white for cards */
                --background: #F3F7FA; /* Light blue-gray background */
                --shadow: 0 6px 12px rgba(0, 0, 0, 0.1);
                --transition: all 0.3s ease;
            }

            * {
                margin: 0;
                padding: 0;
                box-sizing: border-box;
            }

            body {
                font-family: 'Inter', Arial, sans-serif;
                background-color: var(--background);
                color: var(--text-color);
                line-height: 1.6;
            }

            .header {
                background: linear-gradient(135deg, var(--primary-color), var(--secondary-color));
                padding: 15px 30px;
                display: flex;
                justify-content: space-between;
                align-items: center;
                box-shadow: var(--shadow);
                position: sticky;
                top: 0;
                z-index: 1000;
            }

            .header a {
                color: #FFFFFF;
                text-decoration: none;
                font-size: 1.5rem;
                font-weight: 600;
                transition: var(--transition);
            }

            .header a:hover {
                color: var(--accent-color);
            }

            .header .user-info {
                display: flex;
                align-items: center;
                gap: 15px;
            }

            .header .user-info span {
                color: #FFFFFF;
                font-size: 1.1rem;
                font-weight: 400;
                margin-right: 10px;
            }

            .welcome {
                text-align: center;
                margin: 40px 0;
                padding: 20px;
            }

            .welcome img {
                border-radius: 50%;
                width: 130px;
                height: 130px;
                object-fit: cover;
                border: 4px solid var(--primary-color);
                cursor: pointer;
                transition: var(--transition);
            }

            .welcome img:hover {
                transform: scale(1.08);
                box-shadow: var(--shadow);
                border-color: var(--accent-color);
            }

            .welcome h2 {
                margin-top: 15px;
                font-size: 2.2rem;
                color: var(--text-color);
                font-weight: 600;
                text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.05);
            }

            .dashboard {
                display: grid;
                grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
                gap: 30px;
                max-width: 1000px;
                margin: 0 auto;
                padding: 30px 20px;
            }

            .dashboard .card {
                background: var(--card-bg);
                border-radius: 12px;
                padding: 25px;
                text-align: center;
                cursor: pointer;
                transition: var(--transition);
                box-shadow: var(--shadow);
                border: 1px solid rgba(74, 144, 226, 0.1);
            }

            .dashboard .card:hover {
                transform: translateY(-8px);
                box-shadow: 0 8px 16px rgba(0, 0, 0, 0.15);
                border-color: var(--primary-color);
            }

            .dashboard .card img {
                width: 50px;
                height: 50px;
                object-fit: contain;
                margin-bottom: 15px;
                filter: brightness(1.1);
            }

            .dashboard .card h3 {
                font-size: 1.3rem;
                color: var(--text-color);
                font-weight: 500;
                transition: var(--transition);
            }

            .dashboard .card:hover h3 {
                color: var(--primary-color);
            }

            @media (max-width: 768px) {
                .header {
                    flex-direction: column;
                    gap: 12px;
                    padding: 15px;
                }

                .header .user-info {
                    flex-direction: column;
                    gap: 8px;
                }

                .welcome img {
                    width: 110px;
                    height: 110px;
                }

                .welcome h2 {
                    font-size: 1.8rem;
                }

                .dashboard {
                    grid-template-columns: 1fr;
                    padding: 20px;
                }

                .dashboard .card {
                    padding: 20px;
                }
            }
        </style>
    </head>
    <body>
        <div class="header">
            <a href="#">Admin Dashboard</a>
            <div class="user-info">
                <span>Welcome, <c:out value="${sessionScope.user.name}" /></span>
                <a href="Logout">Logout</a>
            </div>
        </div>

        <div class="welcome">
            <img src="images/admin.jpg" alt="Admin Avatar" onclick="location.href='DisplayAccount?idRole=1'">
            <h2>Welcome <c:out value="${sessionScope.user.name}" /></h2>
        </div>
        <div class="dashboard">
            <div class="card" onclick="location.href='DisplayAccount?idRole=1'">
                <img src="images/systemadmin.jpg" alt="System Admin">
                <h3>System Admin</h3>
            </div>
            <div class="card" onclick="location.href='DisplayAccount?idRole=2'">
                <img src="images/managershop.jpg" alt="Manager">
                <h3>Manager</h3>
            </div>
            <div class="card" onclick="location.href='DisplayAccount?idRole=3'">
                <img src="images/customer.jpg" alt="Customer">
                <h3>Customer</h3>
            </div>
            <div class="card" onclick="location.href='DisplayAccount?idRole=4'">
                <img src="images/nutritionist.jpg" alt="Nutritionist">
                <h3>Nutritionist</h3>
            </div>
            <div class="card" onclick="location.href='DisplayAccount?idRole=5'">
                <img src="images/seller.jpg" alt="Seller">
                <h3>Seller</h3>
            </div>
            <div class="card" onclick="location.href='DisplayAccount?idRole=6'">
                <img src="images/shipper.jpg" alt="Shipper">
                <h3>Shipper</h3>
            </div>
        </div>
    </body>
</html>