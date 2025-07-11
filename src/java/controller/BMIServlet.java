/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller;

import dal.DAOTag;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import model.Tag;

/**
 *
 * @author Hoa
 */
public class BMIServlet extends HttpServlet {
   
    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
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
            out.println("<title>Servlet BMIServlet</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet BMIServlet at " + request.getContextPath () + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    } 

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        //processRequest(request, response);
        DAOTag dao = new DAOTag();
        List<Tag> goalTags = dao.listAllGoalTags();
        request.setAttribute("goal", goalTags);
        request.getRequestDispatcher("/view/bmi.jsp").forward(request, response);
    } 

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        //processRequest(request, response);
        try {
            // Lấy dữ liệu từ form
            String heightStr = request.getParameter("height");
            String weightStr = request.getParameter("weight");
            String selectedGoalSlug = request.getParameter("goal");

            // Kiểm tra và chuyển đổi sang số
            if (heightStr == null || weightStr == null || heightStr.isEmpty() || weightStr.isEmpty()) {
                request.setAttribute("error", "Height and weight are required.");
                doGet(request, response);
                return;
            }

            double heightCm = Double.parseDouble(heightStr);
            double weightKg = Double.parseDouble(weightStr);

            // Tính BMI
            double bmi = utils.BMICalculator.calculate(weightKg, heightCm);
            String bmiCategory = utils.BMICalculator.getBMICategory(bmi);
            String bmiTag = utils.BMICalculator.getBMISlugTag(bmi);

            // Lấy lại danh sách goal tags
            DAOTag dao = new DAOTag();
            List<Tag> goalTags = dao.listAllGoalTags();

            // Tìm tên của goal đã chọn
            String goalName = selectedGoalSlug; // Mặc định là slug nếu không tìm thấy
            for (Tag tag : goalTags) {
                if (tag.getSlug().equals(selectedGoalSlug)) {
                    goalName = tag.getName();
                    break;
                }
            }

            // Đặt các thuộc tính để hiển thị
            request.setAttribute("goal", goalTags); // Truyền lại danh sách goal
            request.setAttribute("bmi", String.format("%.2f", bmi));
            request.setAttribute("category", bmiCategory);
            request.setAttribute("goalName", goalName); // Sử dụng goalName thay vì slug
            request.setAttribute("suggestion", "Consult a professional for personalized advice.");

            // Chuyển hướng đến JSP
            request.getRequestDispatcher("/view/bmi.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid height or weight format.");
            doGet(request, response);
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
            doGet(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "An error occurred. Please try again.");
            doGet(request, response);
        }
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
