package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.model.cart.DefaultCartService;
import com.es.phoneshop.model.product.ProductNotFoundException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.concurrent.atomic.AtomicBoolean;

public class DeleteCartItemServlet extends HttpServlet {
    public static final String WEB_INF_PAGES_CART_JSP = "/WEB-INF/pages/cart.jsp";
    public static final String ERROR = "error";
    CartService cartService = DefaultCartService.getInstance();


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String productIdString = req.getPathInfo().substring(1);
        Cart cart = cartService.getCart(req);

        try {
            Long productId = Long.valueOf(productIdString);
            if (productId < 0) {
                throw new ParseException("Number is less than zero", 0);
            }
            if(isProductInCart(cart,productId)){
            cartService.delete(cart, productId);
            }else throw  new ProductNotFoundException(productId);
        }catch (ParseException e){
            req.setAttribute(ERROR,"Not a number!");
            req.getRequestDispatcher(WEB_INF_PAGES_CART_JSP).forward(req,resp);
        }
        catch (ProductNotFoundException e){
            req.setAttribute(ERROR,"No such product in the cart");
            req.getRequestDispatcher(WEB_INF_PAGES_CART_JSP).forward(req,resp);
        }

        resp.sendRedirect(req.getContextPath() + "/cart?message=Cart item removed successfully");
    }
    private boolean isProductInCart(Cart cart,Long id){
        AtomicBoolean check = new AtomicBoolean(false);
        cart.getItems().forEach(cartItem -> {
            if(cartItem.getProduct().getId().equals(id)){
                check.set(true);
            }
        });
        return check.get();
    }
}
