package com.epam.alextuleninov.taxiservice.controller.tag;

import jakarta.servlet.jsp.JspWriter;
import jakarta.servlet.jsp.PageContext;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class HelloTagTest {

    @Test
    void testDoStartTag() {
        JspWriter jspWriter = new MyJspWriter();
        PageContext pageContext = mock(PageContext.class);
        when(pageContext.getOut()).thenReturn(jspWriter);

        HelloTag helloTag = new HelloTag();
        helloTag.setPageContext(pageContext);

        assertDoesNotThrow(helloTag::doStartTag);
    }

    static class MyJspWriter extends JspWriter {

        private final StringWriter stringWriter;

        public MyJspWriter() {
            super(4096, true);
            stringWriter = new StringWriter();
        }

        @Override
        public void print(Object o) {
            stringWriter.write(o.toString());
        }

        @Override
        public String toString() {
            return stringWriter.toString();
        }

        @Override
        public void write(@NotNull char[] cbuf, int off, int len) throws IOException {

        }

        @Override
        public void newLine() throws IOException {

        }

        @Override
        public void print(boolean b) throws IOException {

        }

        @Override
        public void print(char c) throws IOException {

        }

        @Override
        public void print(int i) throws IOException {

        }

        @Override
        public void print(long l) throws IOException {

        }

        @Override
        public void print(float f) throws IOException {

        }

        @Override
        public void print(double d) throws IOException {

        }

        @Override
        public void print(char[] s) throws IOException {

        }

        @Override
        public void print(String s) throws IOException {

        }

        @Override
        public void println() throws IOException {

        }

        @Override
        public void println(boolean x) throws IOException {

        }

        @Override
        public void println(char x) throws IOException {

        }

        @Override
        public void println(int x) throws IOException {

        }

        @Override
        public void println(long x) throws IOException {

        }

        @Override
        public void println(float x) throws IOException {

        }

        @Override
        public void println(double x) throws IOException {

        }

        @Override
        public void println(char[] x) throws IOException {

        }

        @Override
        public void println(String x) throws IOException {

        }

        @Override
        public void println(Object x) throws IOException {

        }

        @Override
        public void clear() throws IOException {

        }

        @Override
        public void clearBuffer() throws IOException {

        }

        @Override
        public void flush() throws IOException {

        }

        @Override
        public void close() throws IOException {

        }

        @Override
        public int getRemaining() {
            return 0;
        }
    }
}
