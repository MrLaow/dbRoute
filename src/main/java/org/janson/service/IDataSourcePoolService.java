package org.janson.service;


import org.janson.vo.DataSourcePool;

import java.util.List;

/**
 * Created by WANG.TAO on 2017/5/27.
 */
public interface IDataSourcePoolService {

    List<DataSourcePool> listDataSources();

    int addDataSourcePool(DataSourcePool dataSource);

    void removeDataSourcePool(DataSourcePool dataSource);
}
