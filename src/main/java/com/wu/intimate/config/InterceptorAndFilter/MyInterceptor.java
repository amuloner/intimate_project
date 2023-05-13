package com.wu.intimate.config.InterceptorAndFilter;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//@Component
public class MyInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        long startTime = System.currentTimeMillis();
        System.out.println("\n-------- MyInterceptor.preHandle --- ");
        System.out.println("Request URL: " + request.getRequestURL());
//        System.out.println("Start Time: " + System.currentTimeMillis());
//        request.setAttribute("startTime", startTime);
//        String header = ("Content-Type");
//        System.out.println(header);
//        System.out.println(request.get);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        System.out.println("\n-------- MyInterceptor.postHandle --- ");
        System.out.println("Request URL: " + request.getRequestURL());
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        System.out.println("\n-------- MyInterceptor.afterCompletion --- ");

//        long startTime = (Long) request.getAttribute("startTime");
        long endTime = System.currentTimeMillis();
        System.out.println("Request URL: " + request.getRequestURL());
//        System.out.println("End Time: " + endTime);

//        System.out.println("Time Taken: " + (endTime - startTime));
    }
}
