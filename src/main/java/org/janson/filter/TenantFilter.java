package org.janson.filter;

import org.janson.common.Constant;
import org.janson.common.DynamicDataSourceHolder;
import org.janson.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by wang.tao on 2017/6/6.
 */
public class TenantFilter implements Filter{

    private static final String DEFAULT_DATABASE = "defaultDatabase";

    private String defaultTenant = null;

    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {
        defaultTenant = filterConfig.getInitParameter(DEFAULT_DATABASE);
        if (StringUtils.isEmptyOrNull(defaultTenant)) {
            throw new RuntimeException("TenantFilter must set the property name is " + DEFAULT_DATABASE);
        }
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpSession session = request.getSession();
        String tenant = (String) session.getAttribute(Constant.TENAT);
        tenant = tenant == null ? "default" : tenant;
        System.out.println("tenant_datasource:" + tenant);
        DynamicDataSourceHolder.setDataSourceType(tenant);
        filterChain.doFilter(request,response);
    }

    @Override
    public void destroy() {

    }
}
