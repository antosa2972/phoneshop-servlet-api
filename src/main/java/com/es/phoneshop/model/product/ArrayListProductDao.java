package com.es.phoneshop.model.product;

import java.math.BigDecimal;
import java.util.*;
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

    private List<Product> products;
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

    //todo sort num of equal words
    private boolean wordSearch(String[] words, String description) {

        return Arrays.stream(words)
                .allMatch(word -> description.toLowerCase(Locale.ROOT).contains(word.toLowerCase(Locale.ROOT)));
    }

    @Override
    public synchronized List<Product> findProducts(String query, SortField sortField, SortOrder sortOrder) {
        Comparator<Product> comparator = Comparator.comparing(product -> {
            if (sortField != null && SortField.description == sortField) {
                return (Comparable) product.getDescription();
            } else {
                return (Comparable) product.getPrice();
            }
        });
        if(sortOrder==SortOrder.desc){
            comparator=comparator.reversed();
        }
        //todo sort order
        return products.stream()
                .filter(product -> query == null || query.isEmpty() || wordSearch(query.split("\\s"), product.getDescription()))
                .filter(product -> product.getPrice() != null)
                .filter(this::productIsInStock)
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    private boolean productIsInStock(Product product) {
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
