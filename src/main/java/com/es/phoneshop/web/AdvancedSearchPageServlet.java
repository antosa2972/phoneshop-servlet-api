package com.es.phoneshop.web;

import com.es.phoneshop.dao.ArrayListProductDao;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.model.SearchTypes;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;

public class AdvancedSearchPageServlet extends HttpServlet {
    public static final String SEARCH_PAGE_JSP = "/WEB-INF/pages/advancedSearchPage.jsp";
    private ProductDao productDao;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HashMap<String, String> errors = new HashMap<>();
        String description = req.getParameter("description");
        String typeOfSearch = req.getParameter("typeOfSearch");
        String minPrice = req.getParameter("minimalPrice");
        String maxPrice = req.getParameter("maximalPrice");
        if(maxPrice!=null && !maxPrice.isEmpty()){
            checkPriceErrors(errors,maxPrice,"Maximal price");
        }
        if(minPrice!=null && !minPrice.isEmpty()){
            checkPriceErrors(errors,minPrice,"Minimal price");
        }
        if(!errors.isEmpty()){
            req.setAttribute("products",productDao.findProducts(null,null,null));
            req.setAttribute("typeOfSearches", Arrays.asList(SearchTypes.values()));
            req.setAttribute("errors",errors);
            req.getRequestDispatcher(SEARCH_PAGE_JSP).forward(req, resp);
        }
        if(description == null || description.isEmpty()){
            req.setAttribute("products",productDao.findProducts(null,null,null));
        }else {
            BigDecimal minimalPrice = parsePrice(minPrice,"Minimal price");
            BigDecimal maximalPrice = parsePrice(maxPrice,"Maximal price");
            req.setAttribute("products",productDao.advancedSearch(description,typeOfSearch,minimalPrice,maximalPrice));
            req.setAttribute("success","search success");
        }
        req.setAttribute("typeOfSearches", Arrays.asList(SearchTypes.values()));
        req.getRequestDispatcher(SEARCH_PAGE_JSP).forward(req, resp);
    }

    private void checkPriceErrors(HashMap<String, String> errors, String priceToCheck, String nameOfParam) {
        try {
            BigDecimal price = BigDecimal.valueOf(Long.parseLong(priceToCheck));
            if(price.compareTo(new BigDecimal(0L)) < 0){
                errors.put("Error " + nameOfParam,"Number is less than zero");
            }
        } catch (NumberFormatException e) {
            errors.put("Error " + nameOfParam,"Not correct number");
        }
    }
    private BigDecimal parsePrice(String price, String parameter) {
        if (price == null || price.isEmpty()) {
            if (parameter.equals("Minimal price")) {
                return new BigDecimal(0L);
            } else {
                return new BigDecimal(Long.MAX_VALUE);
            }
        } else {
            return BigDecimal.valueOf(Long.parseLong(price));
        }
    }
}
