<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Manager Menu</title>
        <meta name="viewport" content="width=device-width, initial-scale=1">

        <!-- Bootstrap CSS & Icons -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet">

        <style>
            body {
                background-color: #f8f9fa;
            }
            .offcanvas-header {
                background-color: #4CAF50;
                color: white;
            }
            .offcanvas-body {
                background-color: #4CAF50;
                padding: 0;
            }
            .offcanvas-body a {
                color: #ffffff;
                padding: 12px 20px;
                display: block;
                text-decoration: none;
            }
            .offcanvas-body a:hover {
                background-color: #495057;
                color: #ffc107;
            }
        </style>
    </head>
    <body>

        <!-- Navbar chứa nút mở sidebar -->
        <nav class="navbar navbar-light bg-light">
            <div class="container-fluid">
                <button class="btn btn-success" type="button" data-bs-toggle="offcanvas" data-bs-target="#sidebarMenu">
                    <i class="bi bi-list"></i> Menu
                </button>

            </div>
        </nav>

        <!-- Sidebar Offcanvas -->
        <div class="offcanvas offcanvas-start" tabindex="-1" id="sidebarMenu">
            <div class="offcanvas-header">
                <h5 class="offcanvas-title">Dashboard Menu</h5>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="offcanvas"></button>
            </div>
            <div class="offcanvas-body">
                <a href="productmanagement"><i class="bi bi-box-seam me-2"></i>Product Management</a>
                <a href="SellerUpdateSatus"><i class="bi bi-truck me-2"></i>Order Status</a>
                <a href="OrderHistory.jsp"><i class="bi bi-clock-history me-2"></i>Order History</a>
            </div>
        </div>





        <!-- Bootstrap JS -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
