package com.es.phoneshop.dao;

import com.es.phoneshop.model.enumsort.SortField;
import com.es.phoneshop.model.enumsort.SortOrder;
import com.es.phoneshop.model.product.Product;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ArrayListProductDao extends GenericDao<Product> implements ProductDao {
    private static ProductDao instance;

    public static synchronized ProductDao getInstance() {
        if (instance == null) {
            instance = new ArrayListProductDao();
        }
        return instance;
    }

    public double wordCoincidenceSearch(String[] words, String description) {
        double counter = 0;
        List<String> wordsList = Arrays.asList(words);
        List<String> descriptionWords = Arrays.asList(description.split("\\s"));
        counter = wordsList.stream()
                .filter(word -> descriptionWords.stream().anyMatch(descriptionWord -> descriptionWord.contains(word)))
                .count();

        if (counter != 0) {
            return 100000 - (counter * (double) 100 / (double) (descriptionWords.size() - 1));
        } else return 0;
    }

    public synchronized List<Product> findProducts(String query, SortField sortField, SortOrder sortOrder) {
        Comparator<Product> comparator = Comparator.comparing(product -> {
            if (sortField == null || sortOrder == null) {
                return (Comparable) 0;
            }
            if (sortField != null && SortField.DESCRIPTION == sortField) {
                return (Comparable) product.getDescription();
            } else {
                return (Comparable) product.getPrice();
            }
        });
        if (sortOrder == SortOrder.DESC) {
            comparator = comparator.reversed();
        }
        List<Product> productList = items.stream()
                .filter(product -> query == null || query.isEmpty() ||
                        wordCoincidenceSearch(query.split("\\s"), product.getDescription()) > 0)
                .filter(product -> product.getPrice() != null)
                .filter(this::isProductInStock)
                .sorted(Comparator.comparingDouble(product -> {
                    if (query != null) {
                        return wordCoincidenceSearch(query.split("\\s"), product.getDescription());
                    } else {
                        return 0;
                    }
                }))
                .sorted(comparator)
                .collect(Collectors.toList());
        return productList;
    }


    private boolean isProductInStock(Product product) {
        return product.getStock() > 0;
    }

    public void delete(Long id) {
        readWriteLock.writeLock().lock();
        try {
            items.removeIf(product -> id.equals(product.getId()));
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }
}
