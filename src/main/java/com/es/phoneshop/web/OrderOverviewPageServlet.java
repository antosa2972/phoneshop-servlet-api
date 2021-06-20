package com.es.phoneshop.web;

import com.es.phoneshop.dao.ArrayListOrderDao;
import com.es.phoneshop.dao.OrderDao;
import com.es.phoneshop.service.cartservice.CartService;
import com.es.phoneshop.service.orderservice.DefaultOrderService;
import com.es.phoneshop.service.orderservice.OrderService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class OrderOverviewPageServlet extends HttpServlet {
    public static final String ERRORS = "errors";
    public static final String WEB_INF_PAGES_CHECKOUT_JSP = "/WEB-INF/pages/orderOverview.jsp";
    public static final String ORDER = "order";
    public static final String FIRST_NAME_PARAM = "firstName";
    public static final String LAST_NAME_PARAM = "lastName";
    public static final String PHONE_PARAM = "phone";
    public static final String DELIVERY_ADDRESS_PARAM = "deliveryAddress";
    public static final String DELIVERY_DATE_PARAM = "deliveryDate";
    public static final String PAYMENT_METHOD_PARAM = "paymentMethod";
    private CartService cartService;
    private OrderService orderService;
    private OrderDao orderDao;

    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
        orderDao = ArrayListOrderDao.getInstance();
        orderService = DefaultOrderService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String prodSecureId = request.getPathInfo().substring(1);
        request.setAttribute(ORDER, orderDao.getItemBySecureId(prodSecureId));
        request.getRequestDispatcher(WEB_INF_PAGES_CHECKOUT_JSP).forward(request, response);
    }
}
