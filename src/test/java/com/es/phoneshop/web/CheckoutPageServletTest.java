package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.exception.OutOfStockException;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.service.cart_service.CartService;
import com.es.phoneshop.service.cart_service.DefaultCartService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Currency;
import java.util.GregorianCalendar;
import java.util.NoSuchElementException;

import static com.es.phoneshop.web.DeleteCartItemServletTest.setPriceHistory;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CheckoutPageServletTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Mock
    private ServletConfig config;
    @Mock
    private HttpSession session;

    private CheckoutPageServlet servlet = new CheckoutPageServlet();
    private CartService cartService;
    private Cart cart;
    private int quantity = 2;
    Currency usd = Currency.getInstance("USD");
    private Product product = new Product("iphone6", "Apple iPhone 6", new BigDecimal(1000), usd, 30,
            "https://raw.githubusercontent.com/andrewosipenko" +
                    "/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone%206.jpg",
            Arrays.asList(setPriceHistory(new GregorianCalendar(2015, 4, 4), new BigDecimal(1000)),
                    setPriceHistory(new GregorianCalendar(2016, 4, 4), new BigDecimal(1100)),
                    setPriceHistory(new GregorianCalendar(2017, 4, 4), new BigDecimal(1200))
            ));
    @Before
    public void setup() throws ServletException {
        servlet.init(config);
        cartService = DefaultCartService.getInstance();
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        when(request.getSession()).thenReturn(session);
        cart = cartService.getCart(request);
    }

    @Test(expected = NullPointerException.class)
    public void testEmptyCart() throws ServletException, IOException {
        servlet.doGet(request, response);
        verify(response).sendRedirect(anyString());
    }


    @Test(expected = NoSuchElementException.class)
    public void testDoGetWithNotEmptyCart() throws ServletException, IOException, OutOfStockException {
        cartService.add(cart, product.getId(), quantity);
        when(request.getSession().getAttribute(anyString())).thenReturn(cart);
        servlet.doGet(request, response);
        verify(requestDispatcher).forward(request, response);
    }
    @Test
    public void testMistakes() throws ServletException, IOException {
        cart.setTotalCost(product.getPrice());
        when(request.getSession().getAttribute(anyString())).thenReturn(cart);
        when(request.getParameter(anyString())).thenReturn("");
        servlet.doPost(request, response);
        verify(requestDispatcher).forward(request, response);
    }
    @Test
    public void testDoPostOK() throws ServletException, IOException {
        when(request.getParameter(eq("firstName"))).thenReturn("Anton");
        when(request.getParameter(eq("lastName"))).thenReturn("Pusnenkov");
        when(request.getParameter(eq("phone"))).thenReturn("+375291211053");
        when(request.getParameter(eq("deliveryDate"))).thenReturn(String.valueOf(LocalDate.of(2020, 1,4)));
        when(request.getParameter(eq("deliveryAddress"))).thenReturn("Voronovo");
        when(request.getParameter(eq("paymentMethod"))).thenReturn("CACHE");
        cart.setTotalCost(product.getPrice());
        when(request.getSession().getAttribute(anyString())).thenReturn(cart);
        servlet.doPost(request, response);
        verify(response).sendRedirect(anyString());
    }
}