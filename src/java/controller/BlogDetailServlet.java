/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dal.DAOBlog;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

import model.Blog;
import model.Tag;
import model.User;

/**
 *
 * @author HP
 */
@MultipartConfig
public class BlogDetailServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet BlogDetailServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet BlogDetailServlet at " + request.getContextPath() + "</h1>");
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

        String blogIdStr = request.getParameter("blogId");
        int blogId;
        try {
            blogId = Integer.parseInt(blogIdStr);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid Blog ID.");
            request.getRequestDispatcher("view/blogDetail.jsp").forward(request, response);
            return;
        }
        DAOBlog dao = new DAOBlog();
        Blog blog = dao.getBlogById(blogId);
        List<Blog> b = dao.getAllBlog();
        int prevId = 0;
        int nextId = 0;
        for (int i = 0; i < b.size(); i++) {
            if (b.get(i).getId() == blogId) {
                if (i > 0) {
                    prevId = b.get(i - 1).getId();
                }
                if (i < b.size() - 1) {
                    nextId = b.get(i + 1).getId();
                }
                break;
            }
        }
        List<Tag> tag = dao.getTagByBlogId(blogId);
        request.setAttribute("blogId", blog.getId());
        request.setAttribute("title", blog.getTitle());
        request.setAttribute("description", blog.getDescription());
        request.setAttribute("created_at", blog.getCreated_at());
        request.setAttribute("image", blog.getImage());
        request.setAttribute("createBy", blog.getUser().getName());
        request.setAttribute("prevId", prevId != 0 ? prevId : b.get(b.size() - 1).getId());
        request.setAttribute("nextId", nextId != 0 ? nextId : b.get(0).getId());
        request.setAttribute("blog", blog);
        request.setAttribute("tag", tag);
        request.getRequestDispatcher("view/blogDetail.jsp").forward(request, response);
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
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        String action = request.getParameter("action");
        User user = (User) request.getSession().getAttribute("user");
        DAOBlog dao = new DAOBlog();
        int blogId = Integer.parseInt(request.getParameter("blogId"));
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String image = request.getParameter("image");

        final String SAVE_DIR = "images";

        if ("editBlog".equals(action)) {
            Part filePart = request.getPart("file");
            String fileName = getFileName(filePart);

            String appPath = request.getServletContext().getRealPath("");
            File projectRoot = new File(appPath).getParentFile().getParentFile();
            String savePath = projectRoot.getAbsolutePath() + File.separator + "build" + File.separator + "web" + File.separator + SAVE_DIR;

            System.out.println("" + savePath);

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
            Blog blog = new Blog();
            blog.setId(blogId);
            blog.setTitle(title);
            blog.setDescription(description);
            blog.setImage(image);
            blog.setUser(user);
            blog.setCreated_at(new java.sql.Timestamp(System.currentTimeMillis()));

            dao.updateBlog(blog);

            response.sendRedirect(request.getContextPath() + "/blogDetail?blogId=" + blogId);
        }

        if ("deleteBlog".equals(action)) {
            dao.deleteBlogById(blogId);
            response.sendRedirect(request.getContextPath() + "/nutritionBlog");
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

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
