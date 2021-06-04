package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.model.cart.DefaultCartService;
import com.es.phoneshop.model.cart.OutOfStockException;
import com.es.phoneshop.model.product.*;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class ProductDetailsPageServlet extends HttpServlet {
    private ProductDao productDao;
    private CartService cartService;


    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
        productDao = ArrayListProductDao.getInstance();
        cartService = DefaultCartService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long id = parseProductId(request);
        request.setAttribute("product", productDao.getProduct(id));
        request.setAttribute("cart", cartService.getCart(request));
        request.getRequestDispatcher("/WEB-INF/pages/product.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String quantityString = req.getParameter("quantity");
        Long id = parseProductId(req);
        int quantity;
        try {
            NumberFormat format = NumberFormat.getInstance(req.getLocale());
            quantity = format.parse(quantityString).intValue();
        } catch (ParseException e) {
            req.setAttribute("error", "Not a number");
            doGet(req, resp);
            return;
        }
        Cart cart = cartService.getCart(req);
        try {
            cartService.add(cart, id, (Integer.valueOf(quantity)));
        } catch (OutOfStockException e) {
            req.setAttribute("error", "Out of stock, available " + e.getStockAvailable());
            doGet(req, resp);
            return;
        }
        resp.sendRedirect(req.getContextPath() + "/products/" + id + "?message=Success! Product added to cart");
    }

    private Long parseProductId(HttpServletRequest request) {
        return Long.valueOf(request.getPathInfo().split("/")[1]);
    }
}
