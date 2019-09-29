package com.moji.server.slack.filter;

import com.moji.server.slack.util.MultiReadableHttpServletRequest;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created By ds on 29/09/2019.
 */

public class MultiReadableHttpServletRequestFilter implements Filter {

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        MultiReadableHttpServletRequest multiReadRequest = new MultiReadableHttpServletRequest((HttpServletRequest) req);
        chain.doFilter(multiReadRequest, res);
    }

    public void init(FilterConfig filterConfig) {
    }

    public void destroy() {
    }
}