package org.janson.service;


import org.janson.vo.DataSourcePool;

import java.util.List;
import java.util.Map;

/**
 * Created by WANG.TAO on 2017/5/27.
 */
public interface IDataSourcePoolService {

    List<DataSourcePool> listDataSources();

    int addDataSourcePool(DataSourcePool dataSource);

    void removeDataSourcePool(DataSourcePool dataSource);

    Map<String, Object> queryByPage(DataSourcePool dataSourcePool , Integer page, Integer pageSize);

    String getNextId();

    void updateById(DataSourcePool sourcePool);
}
