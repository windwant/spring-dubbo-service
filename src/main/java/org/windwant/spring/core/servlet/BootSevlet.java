package org.windwant.spring.core.servlet;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;

/**
 * BootSevlet
 */
@WebServlet("/web")
public class BootSevlet implements Servlet {

    @Override
    public void init(ServletConfig config) throws ServletException {
        System.out.println("servlet init...");
    }

    @Override
    public ServletConfig getServletConfig() {
        return null;
    }

    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        System.out.println(req.getServerName());
        res.getWriter().write("got your message!");
    }

    @Override
    public String getServletInfo() {
        return null;
    }

    @Override
    public void destroy() {
        System.out.println("servlet destroy...");
    }
}
