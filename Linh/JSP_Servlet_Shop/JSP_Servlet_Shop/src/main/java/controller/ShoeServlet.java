package controller;

import dao.ShoeDAO;
import model.Shoe;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

// Remove @WebServlet annotation since we're using web.xml mapping for /shoes/*
public class ShoeServlet extends BaseServlet {

    private ShoeDAO shoeDAO = new ShoeDAO();
    private CategoryDAO categoryDAO = new CategoryDAO();
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        String action = request.getParameter("action");
        if (pathInfo != null && pathInfo.length() > 1) {
            String path = pathInfo.substring(1); // Remove leading slash
            if (path.equals("all")) {
                action = "list";
            } else if (path.equals("search")) {
                action = "search";
            } else if (path.equals("category")) {
                action = "category";
            } else if (path.startsWith("category/")) {
                // Handle /shoes/category/123 format
                String categoryIdStr = path.substring("category/".length());
                if (categoryIdStr.matches("\\d+")) {
                    action = "category";
                    request.setAttribute("categoryId", categoryIdStr);
                }            } else if (path.equals("details")) {
                action = "detail";
            } else if (path.equals("sizes")) {
                action = "sizes";
            } else if (path.matches("\\d+")) { // If path is just a number, it's a shoe ID
                action = "detail";
                request.setAttribute("shoeId", path);
            }
        }
        
        if (action == null) {
            action = "list";
        }
          switch (action) {
            case "list":
                listShoes(request, response);
                break;
            case "detail":
                showShoeDetail(request, response);
                break;
            case "category":
                getShoesByCategory(request, response);
                break;
            default:
                listShoes(request, response);
                break;
        }
    }    private void listShoes(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Shoe> shoes = shoeDAO.getAllShoes();
        List<Category> categories = categoryDAO.getAllCategories();
        
        request.setAttribute("shoes", shoes);
        request.setAttribute("categories", categories);
        request.getRequestDispatcher("/shoe-list.jsp").forward(request, response);

    }
    private void showShoeDetail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idStr = request.getParameter("id");
        
        // Check if shoeId was set from path parsing
        if (idStr == null || idStr.trim().isEmpty()) {
            Object shoeIdFromPath = request.getAttribute("shoeId");
            if (shoeIdFromPath != null) {
                idStr = shoeIdFromPath.toString();
            }
        }
        
        if (idStr == null || idStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/shoes");
            return;
        }
        
        try {
            int shoeID = Integer.parseInt(idStr);
            Shoe shoe = shoeDAO.getShoeById(shoeID);
            
            if (shoe == null) {
                response.sendRedirect(request.getContextPath() + "/shoes");
                return;
            }
          request.getRequestDispatcher("/shoe-detail.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/shoes");
        } catch (Exception e) {
            getServletContext().log("Error loading shoe detail", e);
            response.sendRedirect(request.getContextPath() + "/shoes");
        }
    }

    private void searchShoes(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String keyword = request.getParameter("keyword");
        List<Category> categories = categoryDAO.getAllCategories();
        List<Shoe> shoes;
        
        if (keyword != null && !keyword.trim().isEmpty()) {
            shoes = shoeDAO.searchShoes(keyword.trim());
            request.setAttribute("keyword", keyword);
        } else {
            shoes = shoeDAO.getAllShoes();
        }        request.setAttribute("shoes", shoes);        
                 request.setAttribute("categories", categories);
                 request.getRequestDispatcher("/shoe-list.jsp").forward(request, response);
    }    private void filterShoes(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String keyword = request.getParameter("keyword");
        String categoryIdStr = request.getParameter("categoryId");
        String minPriceStr = request.getParameter("minPrice");
        String maxPriceStr = request.getParameter("maxPrice");
        String sort = request.getParameter("sort");
        
        Integer categoryId = null;
        Double minPrice = null;
        Double maxPrice = null;
        
        try {
            if (categoryIdStr != null && !categoryIdStr.trim().isEmpty()) {
                categoryId = Integer.parseInt(categoryIdStr);
            }
            if (minPriceStr != null && !minPriceStr.trim().isEmpty()) {
                minPrice = Double.parseDouble(minPriceStr);
            }
            if (maxPriceStr != null && !maxPriceStr.trim().isEmpty()) {
                maxPrice = Double.parseDouble(maxPriceStr);
            }
        } catch (NumberFormatException e) {
        }
        
        List<Shoe> shoes = shoeDAO.filterShoes(keyword, categoryId, minPrice, maxPrice, sort);
        List<Category> categories = categoryDAO.getAllCategories();
        
        request.setAttribute("shoes", shoes);
        request.setAttribute("categories", categories);
        request.setAttribute("keyword", keyword);
        request.setAttribute("selectedCategoryId", categoryId);
        request.setAttribute("minPrice", minPrice);        request.setAttribute("maxPrice", maxPrice);
        request.setAttribute("sort", sort);
        
        request.getRequestDispatcher("/shoe-list.jsp").forward(request, response);
    }

   