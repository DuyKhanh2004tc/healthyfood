/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dal.DAOBlog;
import dal.DAOTag;
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
import model.Blog;
import model.Tag;
import model.User;

/**
 *
 * @author Hoa
 */
@MultipartConfig
public class ManageBlogServlet extends HttpServlet {

    private DAOBlog daoBlog = new DAOBlog();
    private DAOTag daoTag = new DAOTag();

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
            out.println("<title>Servlet ManageBlogServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ManageBlogServlet at " + request.getContextPath() + "</h1>");
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
        User user = (User) session.getAttribute("user");

        // Check if user is logged in and has nutritionist role (ID = 4)
        if (user == null || user.getRole().getId() != 4) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String action = request.getParameter("action");
        if (action == null) {
            action = "";
        }

        // Set attributes for ManageBlog.jsp
        request.removeAttribute("error");
        request.setAttribute("action", action);
        request.setAttribute("tagList", daoTag.listAllTag());
        request.setAttribute("blogList", daoBlog.getAllBlogsByNewest());
        request.setAttribute("showManageBlog", true);

        // Forward to nutritionBlog.jsp, which includes ManageBlog.jsp when showManageBlog is true
        request.getRequestDispatcher("/nutritionBlog").forward(request, response);
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
        //processRequest(request, response);
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null || user.getRole().getId() != 4) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        request.removeAttribute("error");
        String action = request.getParameter("action");
        if (action == null) {
            action = "";
        }

        switch (action) {
            case "addBlog":
                handleAddBlog(request, response, user);
                break;
            case "deleteBlog":
                handleDeleteBlog(request, response);
                break;
            case "addTag":
                handleAddTag(request, response);
                break;
            case "editTag":
                handleEditTag(request, response);
                break;
            case "deleteTag":
                handleDeleteTag(request, response);
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/nutritionBlog");
                break;
        }
    }

    private void handleAddBlog(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String[] tagIds = request.getParameterValues("tags");

        // Handle file upload
        Part filePart = request.getPart("image");
        String fileName = getFileName(filePart);
        if (fileName == null || fileName.isEmpty()) {
            request.setAttribute("error", "Image is required");
            request.setAttribute("action", "addBlog");
            request.setAttribute("tagList", daoTag.listAllTag());
            request.setAttribute("showManageBlog", true);
            request.getRequestDispatcher("/nutritionBlog").forward(request, response);
            return;
        }

        String savePath = getServletContext().getRealPath("") + File.separator + "images";
        File fileSaveDir = new File(savePath);
        if (!fileSaveDir.exists()) {
            fileSaveDir.mkdir();
        }
        String filePath = "images" + File.separator + fileName;
        filePart.write(savePath + File.separator + fileName);

        // Create new blog
        Blog blog = new Blog();
        blog.setTitle(title);
        blog.setDescription(description);
        blog.setImage(filePath);
        blog.setUser(user);
        blog.setCreated_at(new java.sql.Timestamp(System.currentTimeMillis()));

        // Insert blog and get generated ID
        int blogId = daoBlog.insertBlog(blog);
        if (blogId != -1) {
            // Insert blog tags
            if (tagIds != null) {
                for (String tagId : tagIds) {
                    daoTag.insertBlogTag(blogId, Integer.parseInt(tagId));
                }
            }
            response.sendRedirect(request.getContextPath() + "/nutritionBlog");
        } else {
            request.setAttribute("error", "Failed to add blog");
            request.setAttribute("action", "addBlog");
            request.setAttribute("tagList", daoTag.listAllTag());
            request.setAttribute("showManageBlog", true);
            request.getRequestDispatcher("/nutritionBlog").forward(request, response);
        }
    }

    private void handleDeleteBlog(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String[] blogIds = request.getParameterValues("blogIds");
        boolean success = false;
        if (blogIds != null && blogIds.length > 0) {
            for (String blogId : blogIds) {
                try {
                    success = daoBlog.deleteBlogById(Integer.parseInt(blogId));
                    if (!success) {
                        request.setAttribute("error", "Failed to delete blog with ID: " + blogId);
                    }
                } catch (NumberFormatException e) {
                    request.setAttribute("error", "Invalid blog ID format: " + blogId);
                }
            }
            if (success) {
                request.setAttribute("success", "Blogs deleted successfully");
            }
        } else {
            request.setAttribute("error", "No blogs selected for deletion");
        }
        request.setAttribute("action", "deleteBlog");
        request.setAttribute("blogList", daoBlog.getAllBlogsByNewest());
        request.setAttribute("tagList", daoTag.listAllTag());
        request.setAttribute("showManageBlog", true);
        request.getRequestDispatcher("/nutritionBlog").forward(request, response);
    }

    private void handleAddTag(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String tagName = request.getParameter("tagName");
        String description = request.getParameter("description");

        if (tagName == null || tagName.trim().isEmpty()) {
            request.setAttribute("error", "Tag name is required");
            request.setAttribute("action", "addTag");
            request.setAttribute("tagList", daoTag.listAllTag());
            request.setAttribute("showManageBlog", true);
            request.getRequestDispatcher("/nutritionBlog").forward(request, response);
            return;
        }

        String slug = tagName.toLowerCase().replaceAll("\\s+", "-");
        Tag tag = new Tag();
        tag.setName(tagName);
        tag.setSlug(slug);
        tag.setDescription(description != null ? description : "");

        if (daoTag.insertTag(tag)) {
            response.sendRedirect(request.getContextPath() + "/nutritionBlog");
        } else {
            request.setAttribute("error", "Failed to add tag");
            request.setAttribute("action", "addTag");
            request.setAttribute("tagList", daoTag.listAllTag());
            request.setAttribute("showManageBlog", true);
            request.getRequestDispatcher("/nutritionBlog").forward(request, response);
        }
    }

    private void handleEditTag(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String tagIdStr = request.getParameter("tagId");
        String newTagName = request.getParameter("newTagName");
        String description = request.getParameter("description");

        request.setAttribute("action", "editTag");
        request.setAttribute("tagList", daoTag.listAllTag());
        request.setAttribute("showManageBlog", true);

        if (tagIdStr == null) {
            request.getRequestDispatcher("/nutritionBlog").forward(request, response);
            return;
        }

        int tagId;
        try {
            tagId = Integer.parseInt(tagIdStr);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid tag ID");
            request.getRequestDispatcher("/nutritionBlog").forward(request, response);
            return;
        }

        Tag tag = daoTag.getTagById(tagId);
        if (tag == null) {
            request.setAttribute("error", "Tag not found");
            request.getRequestDispatcher("/nutritionBlog").forward(request, response);
            return;
        }

        // Điền sẵn form với thông tin tag
        request.setAttribute("selectedTagId", tagId);
        request.setAttribute("selectedTagName", tag.getName());
        request.setAttribute("selectedTagDescription", tag.getDescription() != null ? tag.getDescription() : "");

        // Nếu có newTagName, thực hiện cập nhật tag
        if (newTagName != null && !newTagName.trim().isEmpty()) {
            String slug = newTagName.toLowerCase().replaceAll("\\s+", "-");
            tag.setName(newTagName);
            tag.setSlug(slug);
            tag.setDescription(description != null ? description : "");
            if (daoTag.updateTag(tag)) {
                request.setAttribute("success", "Tag updated successfully");
                request.setAttribute("selectedTagName", newTagName);
                request.setAttribute("selectedTagDescription", description != null ? description : "");
            } else {
                request.setAttribute("error", "Failed to update tag");
            }
        }

        request.getRequestDispatcher("/nutritionBlog").forward(request, response);
    }

    private void handleDeleteTag(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String[] tagIds = request.getParameterValues("tagIds");
        if (tagIds != null && tagIds.length > 0) {
            for (String tagId : tagIds) {
                daoTag.deleteTag(Integer.parseInt(tagId));
            }
            request.setAttribute("success", "Delete successfully.");
            request.setAttribute("action", "deleteTag");
            request.setAttribute("tagList", daoTag.listAllTag());
            request.setAttribute("showManageBlog", true);
            request.getRequestDispatcher("/nutritionBlog").forward(request, response);
        } else {
            request.setAttribute("error", "No tags selected");
            request.setAttribute("action", "deleteTag");
            request.setAttribute("tagList", daoTag.listAllTag());
            request.setAttribute("showManageBlog", true);
            request.getRequestDispatcher("/nutritionBlog").forward(request, response);
        }
    }

    private String getFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        for (String s : contentDisp.split(";")) {
            if (s.trim().startsWith("filename")) {
                return s.substring(s.indexOf("=") + 2, s.length() - 1);
            }
        }
        return null;
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
