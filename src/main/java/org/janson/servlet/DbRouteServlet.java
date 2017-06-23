package org.janson.servlet;

import com.alibaba.fastjson.JSONObject;
import org.janson.service.IDataSourcePoolService;
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
import java.util.List;
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
        poolService = (IDataSourcePoolService) context.getBean("dataSourcePoolService");
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String url = request.getRequestURL().toString();
        if (url.endsWith("queryPage")) {
            Integer page = Integer.valueOf(request.getParameter("page"));
            Integer pageSize = Integer.valueOf(request.getParameter("rows"));
            Map<String, Object> resultMap = queryByPage(page,pageSize);
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
     * @param page
     * @param pageSize
     * @return
     */
    private Map<String, Object> queryByPage(Integer page, Integer pageSize) {
//        PageHelper.startPage(page, pageSize);

        List<DataSourcePool> sourcePools = poolService.listDataSources();
//        Page pageRst = (Page) sourcePools;
        Map<String, Object> resultMap = new HashMap<String, Object>();
        /*resultMap.put("total", pageRst.getTotal());
        resultMap.put("rows", pageRst);*/

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
                String type = method.getParameterTypes()[0].getName();
                method.invoke(sourcePool, type.endsWith("Integer") ? Integer.valueOf(keyValue.getValue()) : keyValue.getValue());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println(sourcePool);
        return SUCCESS;
    }
}
