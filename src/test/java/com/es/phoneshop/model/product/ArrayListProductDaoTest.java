package com.es.phoneshop.model.product;

import com.es.phoneshop.web.DemoDataServletContextListener;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import javax.servlet.ServletContextEvent;
import java.math.BigDecimal;
import java.util.*;

import static org.junit.Assert.*;

public class ArrayListProductDaoTest {
    private ProductDao productDao;

    @Before
    public void setup() {
        productDao = ArrayListProductDao.getInstance();
    }
    @Test
    public void testFindProductsNoResults() {
        assertTrue(productDao.findProducts("fjuruih",SortField.description,SortOrder.asc).isEmpty());
    }
    @Test
    public void testSaveNewProduct() throws ProductNotFoundException {
        String product_code = "product-test";
        Product product = new Product(product_code, "Siemens C56", new BigDecimal(70), Currency.getInstance("USD"), 20, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20C56.jpg",
                Arrays.asList(new PriceHistory(new GregorianCalendar(2005,10,10),new BigDecimal(100))));
        productDao.save(product);
        assertFalse(product.getId() < 0);
        Product result = productDao.getProduct(product.getId());
        assertNotNull(result);
        assertEquals(product_code, result.getCode());
    }

    @Test
    public void findProductWithZeroStock() {
        List<Product> productList = productDao.findProducts("samsung s",SortField.description,SortOrder.asc);
        for (Product prod :
                productList) {
            assertFalse(prod.getStock() < 0);
        }
    }
    @Test
    public void findNullPriceProduct() {
        List<Product> productList = productDao.findProducts("samsung s",SortField.description,SortOrder.asc);
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
    public void wordCoincidenceSearchTest1(){
        String[] words = {"test1","test2","test3"};
        ArrayListProductDao arrayListProductDao = (ArrayListProductDao) ArrayListProductDao.getInstance();
        assertFalse(arrayListProductDao.wordCoincidenceSearch(words, "")<0);
    }
    @Test
    public void wordCoincidenceSearchTest2(){
        String[] words = {"test1","test2","test"};
        ArrayListProductDao arrayListProductDao = (ArrayListProductDao) ArrayListProductDao.getInstance();
        assertTrue(arrayListProductDao.wordCoincidenceSearch(words, "test should pass")>1);
    }
    @Test(expected = NullPointerException.class)
    public void wordCoincidenceSearchTest3(){
        String[] words = {"test1",null,"test"};
        ArrayListProductDao arrayListProductDao = (ArrayListProductDao) ArrayListProductDao.getInstance();
        assertTrue(arrayListProductDao.wordCoincidenceSearch(words, "test should pass")>1);
    }
    @Test(expected = NullPointerException.class)
    public void wordCoincidenceSearchTest4(){
        String[] words = {"test1","test2","test"};
        ArrayListProductDao arrayListProductDao = (ArrayListProductDao) ArrayListProductDao.getInstance();
        assertTrue(arrayListProductDao.wordCoincidenceSearch(words, null)==0);
    }
    @Test
    public void wordCoincidenceSearchTest5(){
        String[] words = {"test","test","test"};
        ArrayListProductDao arrayListProductDao = (ArrayListProductDao) ArrayListProductDao.getInstance();
        double value = arrayListProductDao.wordCoincidenceSearch(words,"testtesttesttesttesttesttesttesttesttesttesttestesttesttesttesttest");
        assertFalse(value>0);
    }
}

