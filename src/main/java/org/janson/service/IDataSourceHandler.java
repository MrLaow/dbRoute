package org.janson.service;


import org.janson.vo.DataSourcePool;

/**
 * Created by WANG.TAO on 2017/5/31.
 */
public interface IDataSourceHandler {

    int addTerent(String tenant, DataSourcePool dataSource);

    void removeTerent(DataSourcePool dataSource);
}
