package controller;

import dao.ShoeDAO;
import dao.CategoryDAO;
import dao.ShoeSizeDAO;
import model.ShoeSize;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

// Remove @WebServlet annotation since we're using web.xml mapping for /shoes/*
public class ShoeServlet extends BaseServlet {

    private ShoeDAO shoeDAO = new ShoeDAO();
    private CategoryDAO categoryDAO = new CategoryDAO();
    private ShoeSizeDAO shoeSizeDAO = new ShoeSizeDAO();




    private void getShoeSizes(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String shoeIdStr = request.getParameter("shoeId");
        
        if (shoeIdStr == null || shoeIdStr.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("[]");
            return;
        }
        
        try {
            int shoeId = Integer.parseInt(shoeIdStr);
            List<ShoeSize> shoeSizes = shoeSizeDAO.getSizesByShoe(shoeId);
            
            // Convert to JSON format
            response.setContentType("application/json");
            StringBuilder json = new StringBuilder("[");
            for (int i = 0; i < shoeSizes.size(); i++) {
                ShoeSize shoeSize = shoeSizes.get(i);
                if (i > 0) json.append(",");                json.append("{")
                    .append("\"sizeId\":").append(shoeSize.getSizeID()).append(",")
                    .append("\"sizeValue\":\"").append(shoeSize.getSizeNumber()).append("\",")
                    .append("\"stock\":").append(shoeSize.getStockQuantity())
                    .append("}");
            }
            json.append("]");
            response.getWriter().write(json.toString());
            
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("[]");
        }
    }
}
