package filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;
import java.io.IOException;
import java.net.URLEncoder;

@WebFilter("/admin/*")
public class AdminFilter implements Filter {
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization code if needed
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);        // Check if user is logged in
        if (session == null || session.getAttribute("user") == null) {
            // Capture the current URL and append as returnUrl parameter
            String currentUrl = getFullRequestURL(httpRequest);
            String encodedReturnUrl = java.net.URLEncoder.encode(currentUrl, "UTF-8");
            
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/auth/login?returnUrl=" + encodedReturnUrl + "&error=Please login to access admin area");
            return;
        }
        
        // Check if user has admin role
        User user = (User) session.getAttribute("user");
        if (!"admin".equals(user.getRole())) {
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/home?error=Access denied");
            return;
        }
        
        // User is authenticated and has admin role, continue with request
        chain.doFilter(request, response);    }
    
    private String getFullRequestURL(HttpServletRequest request) {
        StringBuffer requestURL = request.getRequestURL();
        String queryString = request.getQueryString();
        
        if (queryString == null) {
            return requestURL.toString();
        } else {
            return requestURL.append('?').append(queryString).toString();
        }
    }
    
    @Override
    public void destroy() {
        // Cleanup code if needed
    }
}
