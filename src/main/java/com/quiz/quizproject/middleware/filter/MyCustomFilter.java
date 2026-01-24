package com.quiz.quizproject.middleware.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class MyCustomFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse res = (HttpServletResponse) servletResponse;
        System.out.println("Filter: Bắt đầu xử lý request tới " + req.getRequestURI());

        filterChain.doFilter(servletRequest, servletResponse);

        System.out.println("Filter: Đã xử lý xong response"+res.getStatus());
    }
}
