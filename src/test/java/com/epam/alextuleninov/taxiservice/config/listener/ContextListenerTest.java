package com.epam.alextuleninov.taxiservice.config.listener;

import com.epam.alextuleninov.taxiservice.config.context.AppContext;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ContextListenerTest {

    private ServletContextEvent sce;
    private ServletContext servletContext;

    @BeforeEach
    void setUp() {
        sce = mock(ServletContextEvent.class);
        servletContext = mock(ServletContext.class);
    }

    @Test
    void testContextInitialized() {
        when(sce.getServletContext()).thenReturn(servletContext);

        new ContextListener().contextInitialized(sce);

        assertNotNull(AppContext.getAppContext());
    }
}
