<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Nutrition Blog</title>
    <style>
        /* Reset default styles for main content only */
        .main-content * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        /* Main content container */
        .main-content {
            color: #333;
            background-color: #f9fafb;
        }

        /* Container */
        .container {
            max-width: 1200px;
            margin: 0 auto;
            padding: 2rem 1rem;
        }

        /* Header section */
        .head {
            text-align: center;
            margin-bottom: 3rem;
        }

        .head h1 {
            font-size: 2.5rem;
            color: #1a3c34;
            margin-bottom: 1rem;
            font-family: 'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
        }

        .head p {
            font-size: 1.1rem;
            color: #4b5e5a;
            max-width: 600px;
            margin: 0 auto;
            font-family: 'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
        }

        /* Blog grid */
        .blog-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
            gap: 2rem;
        }

        /* Blog card */
        .blog-card {
            background: #fff;
            border-radius: 12px;
            overflow: hidden;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            transition: transform 0.3s ease, box-shadow 0.3s ease;
            display: flex;
            flex-direction: column;
        }

        .blog-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 6px 12px rgba(0, 0, 0, 0.15);
        }

        .blog-card img {
            width: 100%;
            height: 200px;
            object-fit: cover;
        }

        .blog-card-content {
            padding: 1.5rem;
            flex-grow: 1;
            display: flex;
            flex-direction: column;
            gap: 0.5rem;
            font-family: 'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
        }

        .blog-card-content h2 {
            font-size: 1.25rem;
            color: #1a3c34;
            font-weight: 600;
        }

        .blog-card-content .description {
            display: -webkit-box;
            -webkit-line-clamp: 3;
            -webkit-box-orient: vertical;
            overflow: hidden;
            text-overflow: ellipsis;
            color: #6b7280;
            font-size: 0.95rem;
            flex-grow: 1;
        }

        .blog-card-content .date {
            font-size: 0.85rem;
            color: #9ca3af;
            font-style: italic;
        }

        .blog-card a {
            text-decoration: none;
            color: inherit;
        }

        /* Responsive design */
        @media (max-width: 768px) {
            .header h1 {
                font-size: 2rem;
            }

            .blog-grid {
                grid-template-columns: 1fr;
            }

            .blog-card img {
                height: 180px;
            }
        }

        @media (max-width: 480px) {
            .container {
                padding: 1rem;
            }

            .blog-card-content {
                padding: 1rem;
            }
        }
    </style>
</head>
<body>
    <jsp:include page="header.jsp" />
    <div class="main-content">
        <div class="container">
            <div class="head">
                <h1>Welcome to Our Nutrition Blog</h1>
                <p>Discover healthy eating tips and nutritious recipes to improve your lifestyle. Stay tuned for our latest posts!</p>
            </div>
            <section class="blog-grid">
                <c:forEach items="${requestScope.blogList}" var="o">
                    <article class="blog-card">
                        <a href="${pageContext.request.contextPath}/blogDetail?blogId=${o.id}">
                            <img src="${pageContext.request.contextPath}/images/${o.image}" alt="${o.title}">
                            <div class="blog-card-content">
                                <h2>${o.title}</h2>
                                <p class="description">${o.description}</p>
                                <p class="date">${o.created_at}</p>
                            </div>
                        </a>
                    </article>
                </c:forEach>
            </section>
        </div>
    </div>
    <jsp:include page="footer.jsp" />
</body>
</html>