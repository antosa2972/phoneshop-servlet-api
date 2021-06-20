package com.es.phoneshop.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public abstract class GenericDao <T extends IdentifiableItem> {
    protected ReadWriteLock readWriteLock;
    protected final List<T> items;
    protected long orderId;

    protected GenericDao(){
        items = new ArrayList<>();
        readWriteLock = new ReentrantReadWriteLock();
        orderId = 0L;
    }

    public T getItem(Long id) {
        readWriteLock.readLock().lock();
        try {
            readWriteLock.readLock().lock();
            return items.stream()
                    .filter(item -> id.equals(item.getId()))
                    .findAny()
                    .orElseThrow(NoSuchElementException::new);
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    public void save(T item){
        readWriteLock.writeLock().lock();
        try {
            if (!items.isEmpty() && item.getId() != null) {
                for (T itm :
                        items) {
                    if (item.getId().equals(itm.getId())) {
                        items.set(items.indexOf(itm), item);
                        return;
                    }
                }
            }
            item.setId(orderId++);
            this.items.add(item);
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }
}
