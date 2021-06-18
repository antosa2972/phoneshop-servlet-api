package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.model.order.PaymentMethods;
import com.es.phoneshop.model.order.exception.OrderNotFoundException;
import com.es.phoneshop.service.cart_service.CartService;
import com.es.phoneshop.service.cart_service.DefaultCartService;
import com.es.phoneshop.service.order_service.DefaultOrderService;
import com.es.phoneshop.service.order_service.OrderService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class CheckoutPageServlet extends HttpServlet {
    public static final String ERRORS = "errors";
    public static final String WEB_INF_PAGES_CHECKOUT_JSP = "/WEB-INF/pages/checkout.jsp";
    public static final String ORDER = "order";
    public static final String FIRST_NAME_PARAM = "firstName";
    public static final String LAST_NAME_PARAM = "lastName";
    public static final String PHONE_PARAM = "phone";
    public static final String DELIVERY_ADDRESS_PARAM = "deliveryAddress";
    public static final String DELIVERY_DATE_PARAM = "deliveryDate";
    public static final String PAYMENT_METHOD_PARAM = "paymentMethod";
    private CartService cartService;
    private OrderService orderService;

    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
        cartService = DefaultCartService.getInstance();
        orderService = DefaultOrderService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cart cart = cartService.getCart(request);
        request.setAttribute(ORDER, orderService.getOrder(cart));
        request.getRequestDispatcher(WEB_INF_PAGES_CHECKOUT_JSP).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cart cart = cartService.getCart(request);
        Order order = orderService.getOrder(cart);
        Map<String, String> errors = new HashMap<>();
        setRequiredParameter(request, FIRST_NAME_PARAM, errors, order::setFirstName);
        setRequiredParameter(request, LAST_NAME_PARAM, errors, order::setLastName);
        setRequiredParameter(request, PHONE_PARAM, errors, order::setPhone);
        setRequiredParameter(request, DELIVERY_ADDRESS_PARAM, errors, order::setDeliveryAddress);
        setDeliveryDateParam(request, errors, order);
        setPaymentMethodParam(request, errors, order);
        try {
            orderService.placeOrder(order);
            cartService.clearCart(cart);
        } catch (OrderNotFoundException e) {
            errors.put("error", "Order not found");
        }
        if (errors.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/order/overview/" + order.getSecureId());
        } else {
            request.setAttribute(ERRORS, errors);
            request.setAttribute(ORDER, order);
            request.getRequestDispatcher(WEB_INF_PAGES_CHECKOUT_JSP).forward(request, response);
        }
    }

    private void setRequiredParameter(HttpServletRequest request, String parameter, Map<String, String> errors,
                                      Consumer<String> consumer) {
        String object = request.getParameter(parameter);
        if (object == null || object.isEmpty()) {
            errors.put(parameter, parameter + " is required");
        } else {
            consumer.accept(object);
        }
    }

    private void setPaymentMethodParam(HttpServletRequest request, Map<String, String> errors, Order order) {
        String parameter = request.getParameter(PAYMENT_METHOD_PARAM);
        if (parameter == null || parameter.isEmpty()) {
            errors.put(PAYMENT_METHOD_PARAM, PAYMENT_METHOD_PARAM + " is required");
        } else {
            order.setPaymentMethod(PaymentMethods.valueOf(parameter));
        }
    }

    private void setDeliveryDateParam(HttpServletRequest request, Map<String, String> errors, Order order) {
        String parameter = request.getParameter(DELIVERY_DATE_PARAM);
        if (parameter == null || parameter.isEmpty()) {
            errors.put(DELIVERY_DATE_PARAM, DELIVERY_DATE_PARAM + " is required");
        } else {
            String[] dateParsed = parameter.split("-");
            LocalDate localDate = LocalDate.of(Integer.parseInt(dateParsed[0]), Integer.parseInt(dateParsed[1]), Integer.parseInt(dateParsed[2]));
            order.setDeliveryDate(localDate);
        }
    }
}
