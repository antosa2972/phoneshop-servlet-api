package com.es.phoneshop.web;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DOSFilterTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private FilterChain filterChain;
    @Mock
    private FilterConfig config;

    private final DOSFilter dosFilter = new DOSFilter();

    @Before
    public void setup() throws ServletException {
        dosFilter.init(config);
        String testIp = "192.168.1.0";
        when(request.getRemoteAddr()).thenReturn(testIp);
    }

    @Test
    public void test1() throws IOException, ServletException {
        dosFilter.doFilter(request, response, filterChain);
        verify(filterChain).doFilter(request, response);
    }
}