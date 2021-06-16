package com.es.phoneshop.model.product;

import com.es.phoneshop.model.enumsort.SortField;
import com.es.phoneshop.model.enumsort.SortOrder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

public class ArrayListProductDao implements ProductDao {
    private static ProductDao instance;

    public static synchronized ProductDao getInstance() {
        if (instance == null) {
            instance = new ArrayListProductDao();
        }
        return instance;
    }

    private final List<Product> products;
    private long maxId;
    ReadWriteLock readWriteLock;

    private ArrayListProductDao() {
        products = new ArrayList<>();
        readWriteLock = new ReentrantReadWriteLock();

    }

    @Override
    public Product getProduct(Long id) throws ProductNotFoundException {
        try {
            readWriteLock.readLock().lock();
            return products.stream()
                    .filter(product -> id.equals(product.getId()))
                    .findAny()
                    .orElseThrow(() -> new ProductNotFoundException(id));
        } finally {
            readWriteLock.readLock().unlock();
        }
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

    @Override
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
        List<Product> productList = products.stream()
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

    @Override
    public void save(Product product) {
        readWriteLock.writeLock().lock();
        try {
            if (!products.isEmpty() && product.getId() != null) {
                for (Product prod :
                        products) {
                    if (product.getId().equals(prod.getId())) {
                        products.set(products.indexOf(prod), product);
                        return;
                    }
                }
            }
            product.setId(maxId++);
            this.products.add(product);
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    @Override
    public void delete(Long id) {
        readWriteLock.writeLock().lock();
        try {
            products.removeIf(product -> id.equals(product.getId()));
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }
}
