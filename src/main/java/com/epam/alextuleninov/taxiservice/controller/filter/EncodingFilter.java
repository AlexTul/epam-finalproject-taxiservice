package com.epam.alextuleninov.taxiservice.controller.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.annotation.WebInitParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static com.epam.alextuleninov.taxiservice.Routes.URL_EMPTY;

/**
 * EncodingFilter for intercepts the request.
 * The filter pre-processes the request before it reaches the servlet.
 * EncodingFilter sets the encoding.
 */
@WebFilter(filterName = "EncodingFilter", urlPatterns = URL_EMPTY,
        initParams = @WebInitParam(name = "encoding", value = "UTF-8"))
public class EncodingFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(EncodingFilter.class);

    private String encoding;

    @Override
    public void init(FilterConfig config) {
        encoding = config.getInitParameter("encoding");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        request.setCharacterEncoding(encoding);
        log.info("The encoding is: " + encoding);

        chain.doFilter(request, response);
    }
}
