import dal.DAOProduct;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Product;

@WebServlet("/productmanagement")
public class ProductManagementServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(ProductManagementServlet.class.getName());
    private DAOProduct productDAO;

    @Override
    public void init() throws ServletException {
        productDAO = new DAOProduct();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Product> products = productDAO.getAllProduct();
        if (products == null || products.isEmpty()) {
            request.setAttribute("errorMessage", "No products found.");
        } else {
            request.setAttribute("allProducts", products);
        }
        request.getRequestDispatcher("view/sProductManagement.jsp").forward(request, response);
    }
}