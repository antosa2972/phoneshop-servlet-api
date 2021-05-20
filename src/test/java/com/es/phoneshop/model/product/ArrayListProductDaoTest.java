package com.es.phoneshop.model.product;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

import static org.junit.Assert.*;

public class ArrayListProductDaoTest {
    private ProductDao productDao;

    @Before
    public void setup() {
        productDao = new ArrayListProductDao();
    }

    @Test
    public void testFindProductsNoResults() {
        assertFalse(productDao.findProducts().isEmpty());
    }

    @Test
    public void testSaveNewProduct() throws ProductNotFoundException {
        String product_code = "product-test";
        Product product = new Product(product_code, "Siemens C56", new BigDecimal(70), Currency.getInstance("USD"), 20, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20C56.jpg");
        productDao.save(product);
        assertTrue(product.getId() > 0);
        Product result = productDao.getProduct(Long.valueOf(product.getId()));
        assertNotNull(result);
        assertEquals(product_code, result.getCode());
    }

    @Test
    public void findProductWithZeroStock() {
        List<Product> productList = productDao.findProducts();
        for (Product prod :
                productList) {
            assertFalse(prod.getStock() < 0);
        }
    }

    @Test
    public void findNullPriceProduct() {
        List<Product> productList = productDao.findProducts();
        for (Product prod :
                productList) {
            assertNotNull(prod.getPrice());
        }
    }

    @Test(expected = ProductNotFoundException.class)
    public void deleteProductTest() throws ProductNotFoundException {
        Long id = 2L;
        Product product = productDao.getProduct(id);
        assertEquals(product.getId(), id);
        productDao.delete(id);
        product = productDao.getProduct(id);
    }

    @Test
    public void inStockTest() throws ProductNotFoundException {
        Product product = productDao.getProduct(3L);
        assertTrue(product.getStock() > 0);
    }
}
