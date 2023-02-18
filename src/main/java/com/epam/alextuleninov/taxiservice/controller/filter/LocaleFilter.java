package com.epam.alextuleninov.taxiservice.controller.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static com.epam.alextuleninov.taxiservice.Constants.SCOPE_LOCALE;

/**
 * LocaleFilter for intercepts the request.
 * The filter pre-processes the request before it reaches the servlet.
 * LocaleFilter sets the locale.
 */
//@WebFilter(filterName = "LocaleFilter", urlPatterns = "/*")
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
        String localeFromRequest = httpRequest.getParameter(SCOPE_LOCALE);
        if (isNotBlank(localeFromRequest)) {
            httpRequest.getSession().setAttribute(SCOPE_LOCALE, localeFromRequest);
        } else if (!isNotBlank(localeFromRequest) && !isNotBlank((String) httpRequest.getSession().getAttribute("locale"))) {
            localeFromRequest = "en";
            httpRequest.getSession().setAttribute(SCOPE_LOCALE, localeFromRequest);
        }

        log.info("The locale is: " + localeFromRequest);
        chain.doFilter(request, response);
    }

    private boolean isNotBlank(String locale) {
        return !isBlank(locale);
    }

    private boolean isBlank(String locale) {
        return locale == null || locale.isEmpty();
    }
}