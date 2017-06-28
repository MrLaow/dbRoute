package org.janson.dao.impl;

import org.janson.vo.DataSourcePool;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowCallbackHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wang.tao on 2017/6/22.
 */
public class DataSourcePoolDaoJdbc extends AbstractDataSourcePool {


    @Override
    public int deleteByPrimaryKey(final Integer id) {
        String sql = "DELETE FROM datasourcepool WHERE id = ?";
        return jdbcTemplate.update(sql, new Object[]{id});
    }

    @Override
    public int insert(final DataSourcePool dataSourcePool) {
        String sql = "INSERT INTO datasourcepool (id , tenant , username , password , url ,dbtype ,iseffective) VALUES (?,?,?,?,?,?,?) ";
        return jdbcTemplate.update(sql, new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement pstmt) throws SQLException {
                pstmt.setObject(1, dataSourcePool.getId());
                pstmt.setObject(2, dataSourcePool.getTenant());
                pstmt.setObject(3, dataSourcePool.getUsername());
                pstmt.setObject(4, dataSourcePool.getPassword());
                pstmt.setObject(5, dataSourcePool.getUrl());
                pstmt.setObject(6, dataSourcePool.getDbtype());
                pstmt.setObject(7, dataSourcePool.getIseffective());
            }
        });
    }

    @Override
    public DataSourcePool selectByPrimaryKey(Integer id) {
        DataSourcePool sourcePool = new DataSourcePool();
        sourcePool.setId(id);
        sourcePool = selectByCondition(sourcePool);
        return sourcePool;
    }

    @Override
    public DataSourcePool selectByCondition(DataSourcePool dataSourcePool) {
        final List<DataSourcePool> result = conditionQuery(dataSourcePool);
        if (result.size() == 1) {
            return result.get(0);
        } else if (result.size() == 0) {
            return null;
        } else {
            throw new RuntimeException("Too many results ,expected 1 real " + result.size());
        }
    }

    @Override
    public int updateByPrimaryKey(final DataSourcePool dataSourcePool) {
        String sql = "update datasourcepool set tenant = ? , username = ? , password = ? , url = ? , dbtype = ? ," +
                    "iseffective = ? where id = ?";
        return jdbcTemplate.update(sql, new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement pstmt) throws SQLException {
                pstmt.setObject(1, dataSourcePool.getTenant());
                pstmt.setObject(2, dataSourcePool.getUsername());
                pstmt.setObject(3, dataSourcePool.getPassword());
                pstmt.setObject(4, dataSourcePool.getUrl());
                pstmt.setObject(5, dataSourcePool.getDbtype());
                pstmt.setObject(6, dataSourcePool.getIseffective());
                pstmt.setObject(7, dataSourcePool.getId());
            }
        });
    }

    @Override
    public List<DataSourcePool> queryAll(DataSourcePool sourcePool) {
        System.out.println(getDbType());
        final List<DataSourcePool> resultList = conditionQuery(sourcePool);
        return resultList;
    }

    @Override
    public Map<String, Object> queryByPage(DataSourcePool dataSourcePool, Integer page, Integer pageSize) {

        final Map<String, Object> resultMap = new HashMap<String, Object>();
        final List<DataSourcePool> sourcePoolList = conditionQuery(dataSourcePool);
        resultMap.put("total",sourcePoolList.size());

        pageInit(sourcePoolList.size(), page, pageSize);

        final Map<String, Object> queryMap = createPageQuerySql(dataSourcePool,pageSize);
        final String sql = queryMap.get("sql").toString();
        final List<Object> objects = (List<Object>) queryMap.get("params");
        final List<DataSourcePool> sourcePools = jdbcTemplate.query(sql, objects.toArray(),
                new BeanPropertyRowMapper(DataSourcePool.class));
        resultMap.put("rows", sourcePools);

        return resultMap;
    }

    @Override
    public String getNextId() {
        String sql = "SELECT MAX(id) FROM datasourcepool ";
        final String[] resultMsg = {null};
        jdbcTemplate.query(sql, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet resultSet) throws SQLException {
                resultMsg[0] = String.valueOf(resultSet.getInt(1) + 1);
            }
        });
        return resultMsg[0];
    }
}
