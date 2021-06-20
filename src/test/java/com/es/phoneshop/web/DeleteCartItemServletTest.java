package com.es.phoneshop.web;

import com.es.phoneshop.dao.ArrayListProductDao;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.model.cart.exception.OutOfStockException;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.price_history.PriceHistory;
import com.es.phoneshop.service.cart_service.CartService;
import com.es.phoneshop.service.cart_service.DefaultCartService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Currency;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DeleteCartItemServletTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private ServletConfig config;
    @Mock
    private HttpSession session;

    private DeleteCartItemServlet servlet = new DeleteCartItemServlet();
    Currency usd = Currency.getInstance("USD");
    private Product testProduct = new Product("iphone6", "Apple iPhone 6", new BigDecimal(1000), usd, 30,
            "https://raw.githubusercontent.com/andrewosipenko" +
                    "/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone%206.jpg",
            Arrays.asList(setPriceHistory(new GregorianCalendar(2015, 4, 4), new BigDecimal(1000)),
                    setPriceHistory(new GregorianCalendar(2016, 4, 4), new BigDecimal(1100)),
                    setPriceHistory(new GregorianCalendar(2017, 4, 4), new BigDecimal(1200))
            ));

    private ProductDao productDao;
    private CartService cartService;

    @Before
    public void setup() throws ServletException {
        servlet.init(config);
        productDao = ArrayListProductDao.getInstance();
        cartService = DefaultCartService.getInstance();
        when(request.getPathInfo()).thenReturn("/0");
        when(request.getSession()).thenReturn(session);
    }

    @Test
    public void testDoPostDelete() throws OutOfStockException, IOException, ServletException {
        productDao.save(testProduct);
        cartService.add(cartService.getCart(request), productDao.getItem(0L).getId(), 1);
        servlet.doPost(request, response);
        assertTrue(cartService.getCart(request).getItems().isEmpty());
    }

    public static PriceHistory setPriceHistory(Calendar calendar, BigDecimal price) {
        PriceHistory priceHistory = new PriceHistory(calendar, price);
        return priceHistory;
    }
}
