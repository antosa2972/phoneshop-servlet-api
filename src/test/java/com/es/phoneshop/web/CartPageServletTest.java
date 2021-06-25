package com.es.phoneshop.web;

import com.es.phoneshop.dao.ArrayListProductDao;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.exception.OutOfStockException;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.service.cartservice.CartService;
import com.es.phoneshop.service.cartservice.DefaultCartService;
import org.junit.After;
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
import java.util.Arrays;
import java.util.Currency;
import java.util.GregorianCalendar;
import java.util.Locale;

import static com.es.phoneshop.web.DeleteCartItemServletTest.setPriceHistory;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CartPageServletTest {
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


    private CartPageServlet servlet = new CartPageServlet();
    private ProductDao productDao;
    private CartService cartService;
    private Cart cart;
    private Locale locale;
    Currency usd = Currency.getInstance("USD");
    private Product product = new Product("iphone6", "Apple iPhone 6", new BigDecimal(1000), usd, 30,
            "https://raw.githubusercontent.com/andrewosipenko" +
                    "/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone%206.jpg",
            Arrays.asList(setPriceHistory(new GregorianCalendar(2015, 4, 4), new BigDecimal(1000)),
                    setPriceHistory(new GregorianCalendar(2016, 4, 4), new BigDecimal(1100)),
                    setPriceHistory(new GregorianCalendar(2017, 4, 4), new BigDecimal(1200))
            ));

    public CartPageServletTest() {
    }

    @Before
    public void setup() throws ServletException {
        servlet.init(config);
        productDao = ArrayListProductDao.getInstance();
        cartService = DefaultCartService.getInstance();
        locale = new Locale("russian");
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        when(request.getSession()).thenReturn(session);
        cart = cartService.getCart(request);
    }

    @After
    public void clearProductDao() {

    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        servlet.doGet(request, response);
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDoGetCheckSetAttribute() throws ServletException, IOException {
        servlet.doGet(request, response);
        verify(request).setAttribute(eq("cart"), any());
    }

    @Test
    public void testDoPost() throws IOException, ServletException, OutOfStockException {
        productDao.save(product);
        when(request.getSession().getAttribute(anyString())).thenReturn(cart);
        cartService.add(cartService.getCart(request), product.getId(), 1);
        when(request.getParameterValues("productId")).thenReturn(new String[]{product.getId().toString()});
        when(request.getParameterValues("quantity")).thenReturn(new String[]{"5"});
        when(request.getLocale()).thenReturn(locale);
        servlet.doPost(request, response);
        verify(response).sendRedirect(anyString());
    }

    @Test(expected = NumberFormatException.class)
    public void testDoPostIncorrectId() throws ServletException, IOException, OutOfStockException {
        productDao.save(product);
        when(request.getSession().getAttribute(anyString())).thenReturn(cart);
        cartService.add(cartService.getCart(request), product.getId(), 1);
        when(request.getParameterValues("productId")).thenReturn(new String[]{"e"});
        when(request.getParameterValues("quantity")).thenReturn(new String[]{"1"});
        servlet.doPost(request, response);
    }
}