package com.epam.alextuleninov.taxiservice.controller.filter;

import com.epam.alextuleninov.taxiservice.Constants;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * LocaleFilter for intercepts the request.
 * The filter pre-processes the request before it reaches the servlet.
 * LocaleFilter sets the locale.
 */
@WebFilter(filterName = "LocaleFilter", urlPatterns = "/*")
public class LocaleFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(LocaleFilter.class);

    @Override
    public void init(FilterConfig config) {
        log.info("Locale filter initialized");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String locale = httpRequest.getParameter(Constants.LOCALE);
        if (isNotBlank(locale)) {
            httpRequest.getSession().setAttribute(Constants.LOCALE, locale);
        }
        if (httpRequest.getSession().getAttribute("locale") == null) {
            locale = "en";
            httpRequest.getSession().setAttribute("locale", locale);
        }
        log.info("The locale is: " + locale);
        chain.doFilter(request, response);
    }

    private boolean isBlank(String locale) {
        return locale == null || locale.isEmpty();
    }

    private boolean isNotBlank(String locale) {
        return !isBlank(locale);
    }
}