package org.janson.service.impl;

import org.janson.dao.IDataSourcePoolDao;
import org.janson.service.IDataSourcePoolService;
import org.janson.vo.DataSourcePool;

import java.util.List;

/**
 * Created by WANG.TAO on 2017/5/27.
 */
public class DataSourcePoolService implements IDataSourcePoolService {

    private IDataSourcePoolDao dataSourcePoolDao;

    @Override
    public List<DataSourcePool> listDataSources() {
        return dataSourcePoolDao.queryAll();
    }

    @Override
    public int addDataSourcePool(DataSourcePool dataSource) {
        return dataSourcePoolDao.insert(dataSource);
    }

    @Override
    public void removeDataSourcePool(DataSourcePool dataSource) {
        dataSourcePoolDao.deleteByPrimaryKey(dataSource.getId());
    }

    public void setDataSourcePoolDao(IDataSourcePoolDao dataSourcePoolDao) {
        this.dataSourcePoolDao = dataSourcePoolDao;
    }


}
