<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<div class="header-shipper">
    <style>
        .header-shipper {
            background: linear-gradient(135deg, #1e3a8a 0%, #4f46e5 100%); /* Gradient từ xanh navy sang tím indigo */
            color: #ffffff;
            padding: 0.5rem 2rem;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.15);
            position: sticky;
            top: 0;
            z-index: 1000;
            font-family: 'Poppins', 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }

        .header-shipper .container {
            max-width: 1400px;
            margin-left: auto;
            margin-right: auto;
            display: flex;
            justify-content: space-between;
            align-items: center;
            height: 70px;
        }

        .header-shipper .logo {
            font-size: 1.8rem;
            font-weight: 600;
            padding: 0.4rem 1.5rem;
            background: rgba(255, 255, 255, 0.15);
            border-radius: 0.5rem;
            display: flex;
            align-items: center;
            gap: 0.5rem;
            transition: transform 0.3s ease, background 0.3s ease;
            text-decoration: none;
            color: inherit;
        }

        .header-shipper .logo:hover {
            transform: scale(1.05);
            background: rgba(255, 255, 255, 0.25);
        }

        .header-shipper .logo i {
            font-size: 1.5rem;
        }

        .header-shipper nav {
            display: flex;
            gap: 2rem;
            align-items: center;
        }

        .header-shipper nav a {
            color: #ffffff;
            text-decoration: none;
            font-size: 1.1rem;
            padding: 0.5rem 1.2rem;
            border-radius: 0.375rem;
            transition: all 0.3s ease;
            position: relative;
        }

        .header-shipper nav a:hover {
            background: rgba(255, 255, 255, 0.2);
            transform: translateY(-3px);
        }

        .header-shipper nav a::after {
            content: '';
            position: absolute;
            width: 0;
            height: 2px;
            bottom: 0;
            left: 0;
            background: #34d399;
            transition: width 0.3s ease;
        }

        .header-shipper nav a:hover::after {
            width: 100%;
        }

        @media (max-width: 768px) {
            .header-shipper .container {
                flex-direction: column;
                height: auto;
                padding: 0.5rem;
            }

            .header-shipper .logo {
                margin-bottom: 0.5rem;
            }

            .header-shipper nav {
                flex-direction: column;
                gap: 1rem;
                margin-bottom: 0.5rem;
            }
        }
    </style>
    <div class="container">
        <!-- Logo hoặc tiêu đề -->
        <a href="${pageContext.request.contextPath}/HomeShipper" class="logo">
            <i class="fas fa-truck"></i> Shipper Home
        </a>

        <!-- Thanh điều hướng -->
        <nav>
            <a href="${pageContext.request.contextPath}/logout">Logout</a>
        </nav>
    </div>
</div>