package com.es.phoneshop.web;

import com.es.phoneshop.security.DOSProtectionService;
import com.es.phoneshop.security.DefaultDOSProtectionService;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DOSFilter implements Filter {
    private DOSProtectionService dosProtectionService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        dosProtectionService = DefaultDOSProtectionService.getInstance();
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (dosProtectionService.isAllowed(servletRequest.getRemoteAddr())) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            ((HttpServletResponse) servletResponse).setStatus(429);
        }
    }

    @Override
    public void destroy() {

    }
}
