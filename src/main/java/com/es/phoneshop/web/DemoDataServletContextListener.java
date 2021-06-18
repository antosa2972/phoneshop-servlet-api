package com.es.phoneshop.web;

import com.es.phoneshop.dao.ArrayListProductDao;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.price_history.PriceHistory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.math.BigDecimal;
import java.util.*;

public class DemoDataServletContextListener implements ServletContextListener {
    private ProductDao productDao;

    public DemoDataServletContextListener() {
        productDao = ArrayListProductDao.getInstance();
    }

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        boolean insertDemoData = Boolean.valueOf(servletContextEvent.getServletContext().getInitParameter("insertDemoData"));
        if (insertDemoData) {
            getSampleProducts().stream()
                    .forEach(product -> productDao
                            .save(product));
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }

    private PriceHistory setPriceHistory(Calendar calendar, BigDecimal price) {
        PriceHistory priceHistory = new PriceHistory(calendar, price);
        return priceHistory;
    }

    private List<Product> getSampleProducts() {
        List<Product> result = new ArrayList<>();
        Currency usd = Currency.getInstance("USD");
        result.add(new Product("sgs2", "Samsung Galaxy S II", new BigDecimal(200), usd, 50,
                "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master" +
                        "/manufacturer/Samsung/Samsung%20Galaxy%20S%20II.jpg",
                Arrays.asList(setPriceHistory(new GregorianCalendar(2015, 4, 4), new BigDecimal(200)),
                        setPriceHistory(new GregorianCalendar(2016, 4, 4), new BigDecimal(150)),
                        setPriceHistory(new GregorianCalendar(2017, 4, 4), new BigDecimal(100))
                )));
        result.add(new Product("sgs", "Samsung Galaxy S", new BigDecimal(100), usd, 100,
                "https://raw.githubusercontent.com/andrewosipenko/" +
                        "phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg",
                Arrays.asList(setPriceHistory(new GregorianCalendar(2013, 4, 4), new BigDecimal(100)),
                        setPriceHistory(new GregorianCalendar(2014, 4, 4), new BigDecimal(90)),
                        setPriceHistory(new GregorianCalendar(2015, 4, 4), new BigDecimal(60))
                )));
        result.add(new Product("sgs3", "Samsung Galaxy S III", new BigDecimal(300), usd, 5,
                "https://raw.githubusercontent.com/andrewosipenko" +
                        "/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20III.jpg",
                Arrays.asList(setPriceHistory(new GregorianCalendar(2015, 4, 4), new BigDecimal(300)),
                        setPriceHistory(new GregorianCalendar(2016, 4, 4), new BigDecimal(150)),
                        setPriceHistory(new GregorianCalendar(2017, 4, 4), new BigDecimal(100))
                )));
        result.add(new Product("iphone", "Apple iPhone", new BigDecimal(200), usd, 10,
                "https://raw.githubusercontent.com/andrewosipenko" +
                        "/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone.jpg",
                Arrays.asList(setPriceHistory(new GregorianCalendar(2015, 4, 4), new BigDecimal(200)),
                        setPriceHistory(new GregorianCalendar(2016, 4, 4), new BigDecimal(150)),
                        setPriceHistory(new GregorianCalendar(2017, 4, 4), new BigDecimal(100))
                )));
        result.add(new Product("iphone6", "Apple iPhone 6", new BigDecimal(1000), usd, 30,
                "https://raw.githubusercontent.com/andrewosipenko" +
                        "/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone%206.jpg",
                Arrays.asList(setPriceHistory(new GregorianCalendar(2015, 4, 4), new BigDecimal(1000)),
                        setPriceHistory(new GregorianCalendar(2016, 4, 4), new BigDecimal(1100)),
                        setPriceHistory(new GregorianCalendar(2017, 4, 4), new BigDecimal(1200))
                )));
        result.add(new Product("htces4g", "HTC EVO Shift 4G", new BigDecimal(320), usd, 3,
                "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images" +
                        "/master/manufacturer/HTC/HTC%20EVO%20Shift%204G.jpg",
                Arrays.asList(setPriceHistory(new GregorianCalendar(2015, 4, 4), new BigDecimal(320)),
                        setPriceHistory(new GregorianCalendar(2016, 4, 4), new BigDecimal(400)),
                        setPriceHistory(new GregorianCalendar(2017, 4, 4), new BigDecimal(500))
                )));
        result.add(new Product("sec901", "Sony Ericsson C901", new BigDecimal(420), usd, 30,
                "https://raw.githubusercontent.com/andrewosipenko/" +
                        "phoneshop-ext-images/master/manufacturer/Sony/Sony%20Ericsson%20C901.jpg",
                Arrays.asList(setPriceHistory(new GregorianCalendar(2015, 4, 4), new BigDecimal(420)),
                        setPriceHistory(new GregorianCalendar(2016, 4, 4), new BigDecimal(150)),
                        setPriceHistory(new GregorianCalendar(2017, 4, 4), new BigDecimal(100))
                )));
        result.add(new Product("xperiaxz", "Sony Xperia XZ", new BigDecimal(120), usd, 100,
                "https://raw.githubusercontent.com/andrewosipenko" +
                        "/phoneshop-ext-images/master/manufacturer/Sony/Sony%20Xperia%20XZ.jpg",
                Arrays.asList(setPriceHistory(new GregorianCalendar(2015, 4, 4), new BigDecimal(120)),
                        setPriceHistory(new GregorianCalendar(2016, 4, 4), new BigDecimal(150)),
                        setPriceHistory(new GregorianCalendar(2017, 4, 4), new BigDecimal(100))
                )));
        result.add(new Product("nokia3310", "Nokia 3310", new BigDecimal(70), usd, 100,
                "https://raw.githubusercontent.com/andrewosipenko" +
                        "/phoneshop-ext-images/master/manufacturer/Nokia/Nokia%203310.jpg",
                Arrays.asList(setPriceHistory(new GregorianCalendar(2015, 4, 4), new BigDecimal(70)),
                        setPriceHistory(new GregorianCalendar(2016, 4, 4), new BigDecimal(150)),
                        setPriceHistory(new GregorianCalendar(2017, 4, 4), new BigDecimal(100))
                )));
        result.add(new Product("palmp", "Palm Pixi", new BigDecimal(170), usd, 30,
                "https://raw.githubusercontent.com/andrewosipenko" +
                        "/phoneshop-ext-images/master/manufacturer/Palm/Palm%20Pixi.jpg",
                Arrays.asList(setPriceHistory(new GregorianCalendar(2015, 4, 4), new BigDecimal(170)),
                        setPriceHistory(new GregorianCalendar(2016, 4, 4), new BigDecimal(150)),
                        setPriceHistory(new GregorianCalendar(2017, 4, 4), new BigDecimal(100))
                )));
        result.add(new Product("simc56", "Siemens C56", new BigDecimal(70), usd, 20,
                "https://raw.githubusercontent.com/andrewosipenko" +
                        "/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20C56.jpg",
                Arrays.asList(setPriceHistory(new GregorianCalendar(2015, 4, 4), new BigDecimal(70)),
                        setPriceHistory(new GregorianCalendar(2016, 4, 4), new BigDecimal(150)),
                        setPriceHistory(new GregorianCalendar(2017, 4, 4), new BigDecimal(100))
                )));
        result.add(new Product("simc61", "Siemens C61", new BigDecimal(80), usd, 0,
                "https://raw.githubusercontent.com/andrewosipenko" +
                        "/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20C61.jpg",
                Arrays.asList(setPriceHistory(new GregorianCalendar(2015, 4, 4), new BigDecimal(80)),
                        setPriceHistory(new GregorianCalendar(2016, 4, 4), new BigDecimal(150)),
                        setPriceHistory(new GregorianCalendar(2017, 4, 4), new BigDecimal(100))
                )));
        result.add(new Product("simsxg75", "Siemens SXG75", new BigDecimal(150), usd, 40,
                "https://raw.githubusercontent.com/andrewosipenko" +
                        "/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20SXG75.jpg",
                Arrays.asList(setPriceHistory(new GregorianCalendar(2015, 4, 4), new BigDecimal(150)),
                        setPriceHistory(new GregorianCalendar(2016, 4, 4), new BigDecimal(130)),
                        setPriceHistory(new GregorianCalendar(2017, 4, 4), new BigDecimal(100))
                )));

        return result;
    }
}
