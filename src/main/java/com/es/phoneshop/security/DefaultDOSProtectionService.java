package com.es.phoneshop.security;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultDOSProtectionService implements DOSProtectionService {

    private static final long THERESHOLD = 2000;
    public static final int MINUTE = 60000;
    Date beginOfMinute;
    private Map<String, Long> countMap = new ConcurrentHashMap<>();

    private static class SingletonHelper {
        private static final DefaultDOSProtectionService INSTANCE = new DefaultDOSProtectionService();
    }

    public static DefaultDOSProtectionService getInstance() {
        return DefaultDOSProtectionService.SingletonHelper.INSTANCE;
    }

    @Override
    public boolean isAllowed(String ip) {
        Long count = countMap.get(ip);
        if (count == null || count == 0L) {
            count = 1L;
            beginOfMinute = new Date();
        } else {
            if (isTimeAvailable(countMap, ip)) {
                return true;
            } else {
                if (count > THERESHOLD) {
                    return false;
                }
            }
            count++;
        }
        countMap.put(ip, count);
        return true;
    }

    private boolean isTimeAvailable(Map<String, Long> countMap, String ip) {
        if (System.currentTimeMillis() - beginOfMinute.getTime() > MINUTE) {
            countMap.put(ip, 0L);
            return true;
        }
        return false;
    }
}
