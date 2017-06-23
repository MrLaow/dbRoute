package org.janson.dao.impl;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.commons.dbcp.BasicDataSource;
import org.janson.DynamicRoutingDataSource;
import org.janson.common.Constant;
import org.janson.common.DynamicDataSourceHolder;
import org.janson.dao.IDataSourcePoolDao;
import org.janson.util.StringUtils;
import org.janson.vo.DataSourcePool;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Windows User on 2017/6/22.
 */
public abstract class AbstractDataSourcePool implements IDataSourcePoolDao {

    protected JdbcTemplate jdbcTemplate;

    /**
     * 起始行数
     */
    private int startIndex;
    /**
     * 结束行数
     */
    private int lastIndex;

    /**
     * 记录行数
     */
    private int totalRows;

    /**
     * 总页数
     */
    private int totalPages;

    /**
     * 数据库类型
     * @param jdbcTemplate
     */
    protected String dbType;

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * 分页查询前确定 startIndex 和 lastIndex
     * @param totalRows
     * @param page
     * @param pageSize
     */
    protected final void pageInit(int totalRows, int page,int pageSize) {
        this.totalRows = totalRows;
        setTotalPages(pageSize);
        setStartIndex(page,pageSize);
        setLastIndex(page,pageSize);
    }


    /**
     * 获取条件查询时的sql,和条件
     * @param dataSourcePool
     * @return
     */
    protected final Map<String, Object> queryByConditionSql(final DataSourcePool dataSourcePool) {
        final Map<String, Object> resultMap = new HashMap<String, Object>();
        String sql = "select * from datasourcepool where 1=1 ";
        final List<Object> params = new ArrayList<Object>();

        if (dataSourcePool != null) {
            if (dataSourcePool.getId() != null) {
                sql += " and id = ? ";
                params.add(dataSourcePool.getId());
            }
            if (StringUtils.isNotEmptyOrNull(dataSourcePool.getTenant())) {
                sql += " and tenant like ? ";
                params.add("%" + dataSourcePool.getTenant() + "%");
            }
            if (StringUtils.isNotEmptyOrNull(dataSourcePool.getUsername())) {
                sql += " and username = ? ";
                params.add(dataSourcePool.getUsername());
            }
            if (StringUtils.isNotEmptyOrNull(dataSourcePool.getPassword())) {
                sql += " and password = ? ";
                params.add(dataSourcePool.getPassword());
            }
            if (StringUtils.isNotEmptyOrNull(dataSourcePool.getUrl())) {
                sql += " and url like ? ";
                params.add("%" + dataSourcePool.getUrl() + "%");
            }
            if (StringUtils.isNotEmptyOrNull(dataSourcePool.getDbtype())) {
                sql += " and dbtype = ? ";
                params.add(dataSourcePool.getDbtype());
            }
            if (dataSourcePool.getIseffective() != null) {
                sql += " and iseffective = ? ";
                params.add(dataSourcePool.getIseffective());
            }
        }
        resultMap.put("sql", sql);
        resultMap.put("params", params);
        return resultMap;
    }

    /**
     *  结果集转化为VO
     * @param rst
     * @return
     * @throws SQLException
     */
    protected final DataSourcePool objectMapper(ResultSet rst) throws SQLException {
        DataSourcePool dataSourcePool = new DataSourcePool();
        dataSourcePool.setId(rst.getInt("id"));
        dataSourcePool.setTenant(rst.getString("tenant"));
        dataSourcePool.setUsername(rst.getString("username"));
        dataSourcePool.setPassword(rst.getString("password"));
        dataSourcePool.setDbtype(rst.getString("dbtype"));
        dataSourcePool.setUrl(rst.getString("url"));
        dataSourcePool.setIseffective(rst.getInt("iseffective"));
        return dataSourcePool;
    }

    /**
     * 条件查询
     * @param dataSourcePool
     * @return
     */
    protected final List<DataSourcePool> conditionQuery(DataSourcePool dataSourcePool) {
        Map<String, Object> queryMap = queryByConditionSql(dataSourcePool);
        String sql = queryMap.get("sql").toString();
        Object[] params = ((List<Object>) queryMap.get("params")).toArray();

        final List<DataSourcePool> result = new ArrayList<DataSourcePool>();
        jdbcTemplate.query(sql, params, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet resultSet) throws SQLException {
                DataSourcePool sourcePool = objectMapper(resultSet);
                result.add(sourcePool);
            }
        });
        return result;
    }

    /**
     * 计算开始索引
     * @param page
     * @param pageSize
     */
    private void setStartIndex(int page,int pageSize) {
        this.startIndex = (page - 1) * pageSize;
    }

    /**
     * 计算结束索引
     */
    private void setLastIndex(int page,int pageSize) {
        if( totalRows < pageSize){
            this.lastIndex = totalRows;
        }else if((totalRows % pageSize == 0) || (totalRows % pageSize != 0 && page < totalPages)){
            this.lastIndex = page * pageSize;
        }else if(totalRows % pageSize != 0 && page == totalPages){//最后一页
            this.lastIndex = totalRows ;
        }
    }

    /**
     * 计算总页数
     * @param pageSize
     */
    private void setTotalPages(int pageSize) {
        if(totalRows % pageSize == 0){
            this.totalPages = totalRows / pageSize;
        }else{
            this.totalPages = (totalRows / pageSize) + 1;
        }
    }


    /**
     * 获取数据库的类型
     * @return
     */
    protected String getDbType() {
        DynamicRoutingDataSource dataSource = (DynamicRoutingDataSource) jdbcTemplate.getDataSource();
        Map<Object,Object> targetDataSource = dataSource.getTargetDataSource();
        DataSource  realDataSource= (DataSource) targetDataSource.get(DynamicDataSourceHolder.getDataSourceType());
        String url = null;
        if (realDataSource instanceof BasicDataSource) {
            BasicDataSource basicDataSource = (BasicDataSource) realDataSource;
            url = basicDataSource.getUrl();
        } else if (realDataSource instanceof DruidDataSource) {
            DruidDataSource druidDataSource = (DruidDataSource) realDataSource;
            url = druidDataSource.getUrl();

        }
        if (url == null) return null;
        if (url.startsWith("jdbc:mysql")) {
            dbType = Constant.DataBaseType.MYSQL;
        } else if (url.startsWith("jdbc:oracle")) {
            dbType = Constant.DataBaseType.ORACLE;
        }
        return dbType;
    }

    /**
     * 生产分页查询语句,和参数
     * @param dataSourcePool
     * @return
     */
    protected Map<String,Object> createPageQuerySql(DataSourcePool dataSourcePool,Integer pageSize) {
        getDbType();
        final Map<String, Object> queryMap = queryByConditionSql(dataSourcePool);
        List<Object> objects = (List<Object>) queryMap.get("params");
        String sql = queryMap.get("sql").toString();
        if (Constant.DataBaseType.MYSQL.equals(dbType)) {
            objects.add(getStartIndex());
            objects.add(pageSize);
            sql += " limit ? , ? ";
        } else if (Constant.DataBaseType.ORACLE.equals(dbType)) {
            objects.add(getLastIndex());
            objects.add(getStartIndex());
            sql = "SELECT * FROM (SELECT A.*, ROWNUM RN FROM ( "+sql+" ) A WHERE ROWNUM <= ? ) WHERE RN > ?";
        }
        queryMap.put("sql", sql);
        System.out.println(queryMap);
        return queryMap;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public int getLastIndex() {
        return lastIndex;
    }
}
