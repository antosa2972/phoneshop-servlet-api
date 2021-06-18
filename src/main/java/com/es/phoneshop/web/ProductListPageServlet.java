package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.service.cart_service.CartService;
import com.es.phoneshop.service.cart_service.DefaultCartService;
import com.es.phoneshop.model.cart.exception.OutOfStockException;
import com.es.phoneshop.model.enumsort.SortField;
import com.es.phoneshop.model.enumsort.SortOrder;
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
import java.util.Optional;

public class ProductListPageServlet extends HttpServlet {

    public static final String ERROR = "error";
    private ProductDao productDao;
    private CartService cartService;

    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
        productDao = ArrayListProductDao.getInstance();
        cartService = DefaultCartService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String query = request.getParameter("query");
        String sortField = request.getParameter("sort");
        String sortOrder = request.getParameter("order");
        request.setAttribute("products", productDao.findProducts(query,
                Optional.ofNullable(sortField).map(SortField::valueOf).orElse(null),
                Optional.ofNullable(sortOrder).map(SortOrder::valueOf).orElse(null)
        ));
        request.getRequestDispatcher("/WEB-INF/pages/productList.jsp").forward(request, response);
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String quantityStr = request.getParameter("quantity");
        String productIdStr = request.getParameter("productId");
        Cart cart = cartService.getCart(request);
        Long productId = 0L;
        try {
            NumberFormat format = NumberFormat.getInstance(request.getLocale());
            int quantity = format.parse(quantityStr).intValue();
            productId = Long.valueOf(productIdStr);
            if (quantity < 0) {
                throw new ParseException("Number is less than zero ", 0);
            }
            cartService.add(cart,productId,quantity);
        } catch (NumberFormatException | ParseException | OutOfStockException e) {
            if (e.getClass().equals(ParseException.class)) {
                request.setAttribute(ERROR,"Not correct number " +  e.getMessage());
                request.setAttribute("errorId",productId);
            } else {
                request.setAttribute(ERROR,"Out of stock, available " + e.getMessage());
                request.setAttribute("errorId",productId);
            }
            doGet(request, response);

        }
        response.sendRedirect(request.getContextPath() + "/products?message=Success! Product added to cart");
    }


}
