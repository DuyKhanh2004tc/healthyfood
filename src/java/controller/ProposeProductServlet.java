/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dal.DAOProposedProduct;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import model.ProposedProduct;
import model.User;

/**
 *
 * @author HP
 */
@MultipartConfig
public class ProposeProductServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private final String SAVE_DIR = "images";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ProposeProductServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ProposeProductServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) request.getSession().getAttribute("user");
        DAOProposedProduct dao = new DAOProposedProduct();
        List proposedProductList = dao.listProductByNutritionistId(user.getId());

        request.setAttribute("proposedProductList", proposedProductList);
        request.getRequestDispatcher("view/proposedProduct.jsp").forward(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) request.getSession().getAttribute("user");
        String action = request.getParameter("action");
        String proposeIdStr = request.getParameter("proposedId");
        String name = request.getParameter("name");
        String categoryName = request.getParameter("categoryName");
        String description = request.getParameter("description");
        String reason = request.getParameter("reason");
        String image = request.getParameter("image");
        DAOProposedProduct dao = new DAOProposedProduct();

        if ("edit".equals(action)) {
            Part filePart = request.getPart("file");
            String fileName = getFileName(filePart);

            String appPath = request.getServletContext().getRealPath("");
            File projectRoot = new File(appPath).getParentFile().getParentFile();
            String savePath = projectRoot.getAbsolutePath() + File.separator + "build" + File.separator + "web" + File.separator + SAVE_DIR;

            if (fileName != null && !fileName.isEmpty()) {
                File fileSaveDir = new File(savePath);
                if (!fileSaveDir.exists()) {
                    fileSaveDir.mkdirs();
                }

                File saveFile = new File(savePath, fileName);
                File parentDir = saveFile.getParentFile();
                if (!parentDir.exists()) {
                    parentDir.mkdirs();
                }

                try (InputStream fileContent = filePart.getInputStream()) {
                    Files.copy(fileContent, saveFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                }

                image = fileName;
            }
            ProposedProduct p = new ProposedProduct();
            p.setId(Integer.parseInt(proposeIdStr));
            p.setName(name);
            p.setCategoryName(categoryName);
            p.setDescription(description);
            p.setImage(image);
            p.setNutritionist(user);
            p.setReason(reason);
            p.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
            p.setStatus("pending");
            dao.updateProposedProduct(p);
            response.sendRedirect(request.getContextPath() + "/proposeProduct");
        }
        if ("delete".equals(action)) {
            dao.deleteProposedProductById(Integer.parseInt(proposeIdStr));
            response.sendRedirect(request.getContextPath() + "/proposeProduct");
        }
        if ("add".equals(action)) {
             Part filePart = request.getPart("file");
            String fileName = getFileName(filePart);

            String appPath = request.getServletContext().getRealPath("");
            File projectRoot = new File(appPath).getParentFile().getParentFile();
            String savePath = projectRoot.getAbsolutePath() + File.separator + "build" + File.separator + "web" + File.separator + SAVE_DIR;

            if (fileName != null && !fileName.isEmpty()) {
                File fileSaveDir = new File(savePath);
                if (!fileSaveDir.exists()) {
                    fileSaveDir.mkdirs();
                }

                File saveFile = new File(savePath, fileName);
                File parentDir = saveFile.getParentFile();
                if (!parentDir.exists()) {
                    parentDir.mkdirs();
                }

                try (InputStream fileContent = filePart.getInputStream()) {
                    Files.copy(fileContent, saveFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                }

                image = fileName;
            }
            ProposedProduct p = new ProposedProduct();
            p.setName(name);
            p.setCategoryName(categoryName);
            p.setDescription(description);
            p.setImage(image);
            p.setNutritionist(user);
            p.setReason(reason);
            p.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
            p.setStatus("pending");
            dao.insertProposedProduct(p);
            response.sendRedirect(request.getContextPath() + "/proposeProduct");
        }
    }

    private String getFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        for (String token : contentDisp.split(";")) {
            if (token.trim().startsWith("filename")) {
                return token.substring(token.indexOf("=") + 2, token.length() - 1);
            }
        }
        return "";
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
