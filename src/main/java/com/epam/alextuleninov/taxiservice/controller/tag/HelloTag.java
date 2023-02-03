package com.epam.alextuleninov.taxiservice.controller.tag;

import jakarta.servlet.jsp.tagext.TagSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * HelloTag class is a custom tag for greeting.
 */
public class HelloTag extends TagSupport {

    private static final Logger log = LoggerFactory.getLogger(HelloTag.class);

    private String userLogin;

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    @Override
    public int doStartTag() {
        try {
            String to = " " + userLogin;
            pageContext.getOut().write("<hr/>" + to + "<hr/>");
        } catch (IOException e) {
            log.error("An I/O error occurs in HelloTag.class", e);
        }
        return SKIP_BODY;
    }

    @Override
    public int doEndTag() {
        return EVAL_PAGE;
    }
}
