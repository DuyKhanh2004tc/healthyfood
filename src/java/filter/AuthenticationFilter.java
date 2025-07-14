/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Filter.java to edit this template
 */
package filter;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;

/**
 *
 * @author ASUS
 */
public class AuthenticationFilter implements Filter {

    private static final boolean debug = true;

    // The filter configuration object we are associated with.  If
    // this value is null, this filter instance is not currently
    // configured. 
    private FilterConfig filterConfig = null;

    public AuthenticationFilter() {
    }

    private void doBeforeProcessing(ServletRequest request, ServletResponse response)
            throws IOException, ServletException {
        if (debug) {
            log("AuthenticationFilter:DoBeforeProcessing");
        }

        // Write code here to process the request and/or response before
        // the rest of the filter chain is invoked.
        // For example, a logging filter might log items on the request object,
        // such as the parameters.
        /*
	for (Enumeration en = request.getParameterNames(); en.hasMoreElements(); ) {
	    String name = (String)en.nextElement();
	    String values[] = request.getParameterValues(name);
	    int n = values.length;
	    StringBuffer buf = new StringBuffer();
	    buf.append(name);
	    buf.append("=");
	    for(int i=0; i < n; i++) {
	        buf.append(values[i]);
	        if (i < n-1)
	            buf.append(",");
	    }
	    log(buf.toString());
	}
         */
    }

    private void doAfterProcessing(ServletRequest request, ServletResponse response)
            throws IOException, ServletException {
        if (debug) {
            log("AuthenticationFilter:DoAfterProcessing");
        }

        // Write code here to process the request and/or response after
        // the rest of the filter chain is invoked.
        // For example, a logging filter might log the attributes on the
        // request object after the request has been processed. 
        /*
	for (Enumeration en = request.getAttributeNames(); en.hasMoreElements(); ) {
	    String name = (String)en.nextElement();
	    Object value = request.getAttribute(name);
	    log("attribute: " + name + "=" + value.toString());

	}
         */
        // For example, a filter might append something to the response.
        /*
	PrintWriter respOut = new PrintWriter(response.getWriter());
	respOut.println("<P><B>This has been appended by an intrusive filter.</B>");
         */
    }

    /**
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     * @param chain The filter chain we are processing
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet error occurs
     */
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false); // Không tạo session mới nếu chưa có

        String path = req.getServletPath();

        boolean isGuest = path.equals("/home")
                || path.equals("/login")
                || path.equals("/register")
                || path.equals("/verifyRegister")
                || path.equals("/resendOTP")                
                || path.equals("/productDetail")
                || path.equals("/nutritionBlog")
                || path.equals("/blogDetail")
                || path.equals("/sortproduct")
                || path.equals("/pricefilter")
                || path.equals("/search")
                || path.equals("/category")
                || path.equals("/cart")
                || path.equals("/placeOrder")
                || path.equals("/orderCheckout");

        if (!isGuest && (session == null || session.getAttribute("user") == null)) {
            res.sendRedirect("home");
            return;
        }

        User u = null;
        int roleId = -1;

        if (!isGuest && (session == null || session.getAttribute("user") == null)) {
            res.sendRedirect("home");
            return;
        } else if (session != null && session.getAttribute("user") != null) {
            u = (User) session.getAttribute("user");
            roleId = u.getRole().getId();
        }

        boolean isAdminPath = path.equals("/HomeAdmin")
                || path.equals("/AddAccount")
                || path.equals("/DeleteUser")
                || path.equals("/UpdateAccount")
                || path.equals("/DisplayAccount");

        boolean isShipperPath = path.equals("/HomeShipper")
                || path.equals("/forgotPassword")
                || path.equals("/WaitingOrders")
                || path.equals("/ConfirmedOrders")
                || path.equals("/DeliveringOrders")
                || path.equals("/DeliveredOrders")
                || path.equals("/ShipperUpdateStatus");

        boolean isSellerPath = path.equals("/productmanagement")
                || path.equals("/home")
                || path.equals("/ConfirmedOrders")
                || path.equals("/productDetail")
                || path.equals("/nutritionBlog")
                || path.equals("/blogDetail")
                || path.equals("/sortproduct")
                || path.equals("/pricefilter")
                || path.equals("/search")
                || path.equals("/category")
                || path.equals("/UpdateProfile")
                || path.equals("/resetPassword")
                || path.equals("/SellerUpdateStatus")
                || path.equals("/SellerWaitingOrders")
                || path.equals("/SellerDeliveringOrders")
                || path.equals("/SellerCanceledOrders")
                || path.equals("/CanceledOrders");

        boolean isManagerPath = path.equals("/DeliveredOrders")
                || path.equals("/home")
                || path.equals("/productDetail")
                || path.equals("/nutritionBlog")
                || path.equals("/blogDetail")
                || path.equals("/sortproduct")
                || path.equals("/pricefilter")
                || path.equals("/search")
                || path.equals("/category")
                || path.equals("/UpdateProfile")
                || path.equals("/resetPassword");

        boolean isCustomerPath = path.equals("/cart")
                || path.equals("/home")
                || path.equals("/productDetail")
                || path.equals("/nutritionBlog")
                || path.equals("/blogDetail")
                || path.equals("/sortproduct")
                || path.equals("/pricefilter")
                || path.equals("/search")
                || path.equals("/category")
                || path.equals("/placeOrder")
                || path.equals("/orderCheckout")
                || path.equals("/CustomerAccount")
                || path.equals("/resetPassword")
                || path.equals("/UpdateProfile")
                || path.equals("/changePassword");

        boolean isNutritionistPath = path.equals("/nutritionistHome")
                || path.equals("/forgotPassword")
                || path.equals("/nutritionBlog")
                || path.equals("/sortproduct")
                || path.equals("/search")
                || path.equals("/category")
                || path.equals("/blogDetail");

        if (!isAdminPath && roleId == 1) {
            res.sendRedirect("HomeAdmin");
            return;
        } else if (!isManagerPath && roleId == 2) {
            res.sendRedirect("home");
            return;
        } else if (!isCustomerPath && roleId == 3) {
            res.sendRedirect("home");
            return;
        } else if (!isNutritionistPath && roleId == 4) {
            res.sendRedirect("nutritionistHome");
            return;
        } else if (!isSellerPath && roleId == 5) {
            res.sendRedirect("productmanagement");
            return;
        } else if (!isShipperPath && roleId == 6) {
            res.sendRedirect("HomeShipper");
            return;
        }

        chain.doFilter(request, response);
    }

    /**
     * Return the filter configuration object for this filter.
     */
    public FilterConfig getFilterConfig() {
        return (this.filterConfig);
    }

    /**
     * Set the filter configuration object for this filter.
     *
     * @param filterConfig The filter configuration object
     */
    public void setFilterConfig(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

    /**
     * Destroy method for this filter
     */
    public void destroy() {
    }

    /**
     * Init method for this filter
     */
    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
        if (filterConfig != null) {
            if (debug) {
                log("AuthenticationFilter:Initializing filter");
            }
        }
    }

    /**
     * Return a String representation of this object.
     */
    @Override
    public String toString() {
        if (filterConfig == null) {
            return ("AuthenticationFilter()");
        }
        StringBuffer sb = new StringBuffer("AuthenticationFilter(");
        sb.append(filterConfig);
        sb.append(")");
        return (sb.toString());
    }

    private void sendProcessingError(Throwable t, ServletResponse response) {
        String stackTrace = getStackTrace(t);

        if (stackTrace != null && !stackTrace.equals("")) {
            try {
                response.setContentType("text/html");
                PrintStream ps = new PrintStream(response.getOutputStream());
                PrintWriter pw = new PrintWriter(ps);
                pw.print("<html>\n<head>\n<title>Error</title>\n</head>\n<body>\n"); //NOI18N

                // PENDING! Localize this for next official release
                pw.print("<h1>The resource did not process correctly</h1>\n<pre>\n");
                pw.print(stackTrace);
                pw.print("</pre></body>\n</html>"); //NOI18N
                pw.close();
                ps.close();
                response.getOutputStream().close();
            } catch (Exception ex) {
            }
        } else {
            try {
                PrintStream ps = new PrintStream(response.getOutputStream());
                t.printStackTrace(ps);
                ps.close();
                response.getOutputStream().close();
            } catch (Exception ex) {
            }
        }
    }

    public static String getStackTrace(Throwable t) {
        String stackTrace = null;
        try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            t.printStackTrace(pw);
            pw.close();
            sw.close();
            stackTrace = sw.getBuffer().toString();
        } catch (Exception ex) {
        }
        return stackTrace;
    }

    public void log(String msg) {
        filterConfig.getServletContext().log(msg);
    }

}
