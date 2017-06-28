package org.janson.servlet;

import com.alibaba.fastjson.JSONObject;
import org.janson.service.IDataSourcePoolService;
import org.janson.util.StringUtils;
import org.janson.vo.DataSourcePool;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Wang.tao on 2017/6/21.
 */
public class DbRouteServlet extends HttpServlet {

    /**
     * 全局上下文
     */
    private ApplicationContext context;

    /**
     * 多租户数据源服务
     */
    private IDataSourcePoolService poolService;

    /**
     * 常量success
     */
    private static final String SUCCESS = "success";

    /**
     * 常量error
     */
    private static final String ERROR = "error";

    @Override
    public void init() throws ServletException {
        super.init();
        context = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
        poolService = (IDataSourcePoolService) context.getBean("poolService");
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String url = request.getRequestURL().toString();
        if (url.endsWith("queryPage")) {
            final Integer page = Integer.valueOf(request.getParameter("page"));
            final Integer pageSize = Integer.valueOf(request.getParameter("rows"));
            final String tenant = request.getParameter("tenant");
            final String dbtype = request.getParameter("dbtype");
            final String iseffective = request.getParameter("iseffective");

            DataSourcePool sourcePool = new DataSourcePool();
            sourcePool.setTenant(StringUtils.isEmptyOrNull(tenant) ? null : tenant);
            sourcePool.setDbtype(StringUtils.isEmptyOrNull(dbtype) ? null : dbtype);
            sourcePool.setIseffective(StringUtils.isEmptyOrNull(iseffective) ? null : Integer.valueOf(iseffective));
            System.out.println(sourcePool);
            Map<String, Object> resultMap = queryByPage(sourcePool,page,pageSize);
            output(resultMap, response);
        } else if (url.endsWith("save")) {
            Enumeration<String> enumeration = request.getParameterNames();
            Map<String, String> paramMap = new HashMap<String, String>();
            while (enumeration.hasMoreElements()) {
                String key = enumeration.nextElement();
                paramMap.put(key, request.getParameter(key));
            }
            String rstStr = save(paramMap);
            output(rstStr, response);
        } else if (url.endsWith("getId")) {
            String nextId = getNextId();
            output(nextId, response);
        } else {
            request.getRequestDispatcher("/dbRoute/setDataBase.jsp").forward(request, response);
        }
    }

    /**
     * 将请求以json的形式返回给前台
     * @param data
     * @param response
     */
    private void output(Object data, HttpServletResponse response) {
        response.setContentType("text/json; charset=utf-8");
        response.setHeader("Cache-Control", "no-cache");
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.print(JSONObject.toJSONString(data));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.flush();
                out.close();
            }
        }
    }


    /**
     * 分页查询
     *
     * @param sourcePool
     * @param page
     * @param pageSize
     * @return
     */
    private Map<String, Object> queryByPage(DataSourcePool sourcePool, Integer page, Integer pageSize) {
        Map<String, Object> resultMap = poolService.queryByPage(sourcePool,page,pageSize);
        return resultMap;
    }

    private String save(Map<String, String> paramMap) {
        DataSourcePool sourcePool = new DataSourcePool();
        Method[] methods = DataSourcePool.class.getMethods();
        Map<String, Method> methodMap = new HashMap<String, Method>();
        for (Method method : methods) {
            String methodName = method.getName();
            if (methodName.startsWith("set")) {
                String key = methodName.substring(3);
                key = key.substring(0, 1).toLowerCase() + key.substring(1);
                methodMap.put(key, method);
            }
        }
        for (Map.Entry<String, String> keyValue : paramMap.entrySet()) {
            try {
                Method method = methodMap.get(keyValue.getKey());
                if (method == null) continue;
                String type = method.getParameterTypes()[0].getName();
                method.invoke(sourcePool, type.endsWith("Integer") ? Integer.valueOf(keyValue.getValue()) : keyValue.getValue());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if ("true".equals(paramMap.get("needAdd"))) {
            poolService.addDataSourcePool(sourcePool);
        } else if ("false".equals(paramMap.get("needAdd"))){
            poolService.updateById(sourcePool);
        }
        System.out.println("sourcePool");
        return SUCCESS;
    }

    private String getNextId() {
        return poolService.getNextId();
    }
}
