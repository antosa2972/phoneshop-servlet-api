package com.es.phoneshop.web;

import com.es.phoneshop.service.cart_service.CartService;
import com.es.phoneshop.service.cart_service.DefaultCartService;
import com.es.phoneshop.model.cart.exception.OutOfStockException;
import com.es.phoneshop.dao.ArrayListProductDao;
import com.es.phoneshop.dao.ProductDao;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

public class CartPageServlet extends HttpServlet {
    public static final String ERROR = "error";
    public static final String WEB_INF_PAGES_CART_JSP = "/WEB-INF/pages/cart.jsp";
    private ProductDao productDao;
    private CartService cartService;

    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
        productDao = ArrayListProductDao.getInstance();
        cartService = DefaultCartService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("cart", cartService.getCart(request));
        request.getRequestDispatcher(WEB_INF_PAGES_CART_JSP).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String[] productIds = request.getParameterValues("productId");
        String[] quantities = request.getParameterValues("quantity");
        Map<Long, String> errors = new HashMap<>();
        for (int i = 0; i < productIds.length; i++) {
            Long productId = Long.valueOf(productIds[i]);
            int quantity;

            try {
                quantity = getQuantity(quantities[i], request);
                if (quantity < 0) {
                    throw new ParseException("Number is less than zero", 0);
                }
                cartService.update(cartService.getCart(request), productId, quantity);
            } catch (ParseException | OutOfStockException e) {
                if (e.getClass().equals(ParseException.class)) {
                    errors.put(productId, "Not correct number, " + e.getMessage());
                } else {
                    errors.put(productId, "Out of stock. " + e.getMessage());
                }

            }


        }
        if (errors.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/cart?message=Cart updated successfully");
        } else {
            request.setAttribute("errors", errors);
            doGet(request, response);
        }
    }

    private int getQuantity(String quantityString, HttpServletRequest request) throws ParseException {
        NumberFormat numberFormat = NumberFormat.getInstance(request.getLocale());
        return numberFormat.parse(quantityString).intValue();
    }
}
