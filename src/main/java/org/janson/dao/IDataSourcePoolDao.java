package org.janson.dao;


import org.janson.vo.DataSourcePool;

import java.util.List;
import java.util.Map;

public interface IDataSourcePoolDao {

    /**
     * 删除
     * @param id
     * @return
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * 增加
     * @param dataSourcePool
     * @return
     */
    int insert(DataSourcePool dataSourcePool);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    DataSourcePool selectByPrimaryKey(Integer id);

    /**
     * 根据条件查询
     * @return
     */
    DataSourcePool selectByCondition(DataSourcePool dataSourcePool);

    /**
     * 更新
     * @param dataSourcePool
     * @return
     */
    int updateByPrimaryKey(DataSourcePool dataSourcePool);

    /**
     * 查询全部
     * @return
     */
    List<DataSourcePool> queryAll(DataSourcePool dataSourcePool);


    /**
     * 分页查询
     */
    Map<String, Object> queryByPage(DataSourcePool dataSourcePool, Integer page, Integer pageSize);

    String getNextId();
}