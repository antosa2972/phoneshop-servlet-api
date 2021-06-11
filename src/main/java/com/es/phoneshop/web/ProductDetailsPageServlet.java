package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.model.cart.DefaultCartService;
import com.es.phoneshop.model.cart.OutOfStockException;
import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductDao;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

public class ProductDetailsPageServlet extends HttpServlet {
    public static final String ERROR_ATTR = "error";
    public static final String RECENTLY_VIEWED_PRODUCTS = "recentlyViewedProducts";
    private ProductDao productDao;
    private CartService cartService;


    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
        productDao = ArrayListProductDao.getInstance();
        cartService = DefaultCartService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Set<Product> recentlyViewedProducts = (Set<Product>) request.getSession().getAttribute(RECENTLY_VIEWED_PRODUCTS);
        if (recentlyViewedProducts == null) {
            recentlyViewedProducts = new TreeSet<>();
        }
        Long id = parseProductId(request);
        if (recentlyViewedProducts.size() >= 3) {
            deleteElement(recentlyViewedProducts);
        }
        recentlyViewedProducts.add(productDao.getProduct(id));
        request.getSession().setAttribute(RECENTLY_VIEWED_PRODUCTS, recentlyViewedProducts);
        request.setAttribute("product", productDao.getProduct(id));
        request.setAttribute("cart", cartService.getCart(request));
        request.getRequestDispatcher("/WEB-INF/pages/product.jsp").forward(request, response);
    }

    private void deleteElement(Set<Product> recentlyViewedProducts) {
        Iterator<Product> iterator = recentlyViewedProducts.iterator();
        iterator.next();
        iterator.remove();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String quantityString = req.getParameter("quantity");
        Long id = parseProductId(req);
        int quantity;
        try {
            NumberFormat format = NumberFormat.getInstance(req.getLocale());
            quantity = format.parse(quantityString).intValue();
            if (quantity <= 0) {
                throw new ParseException("Number is less than zero", 0);
            }
        } catch (ParseException e) {
            req.setAttribute(ERROR_ATTR, "Not correct number");
            doGet(req, resp);
            return;
        }
        Cart cart = cartService.getCart(req);
        try {
            cartService.add(cart, id, quantity);
        } catch (OutOfStockException e) {
            req.setAttribute(ERROR_ATTR, "Out of stock, available " + e.getStockAvailable());
            doGet(req, resp);
            return;
        }
        resp.sendRedirect(req.getContextPath() + "/products/" + id + "?message=Success! Product added to cart");
    }

    private Long parseProductId(HttpServletRequest request) {
        return Long.valueOf(request.getPathInfo().split("/")[1]);
    }
}
