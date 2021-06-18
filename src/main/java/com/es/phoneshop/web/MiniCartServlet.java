package com.es.phoneshop.web;

import com.es.phoneshop.service.cart_service.CartService;
import com.es.phoneshop.service.cart_service.DefaultCartService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MiniCartServlet extends HttpServlet {

    private static final String MINICART_JSP = "/WEB-INF/pages/minicart.jsp";
    private CartService cartService;

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
        cartService = DefaultCartService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("cart", cartService.getCart(request));
        request.getRequestDispatcher(MINICART_JSP).include(request, response);
    }
}
