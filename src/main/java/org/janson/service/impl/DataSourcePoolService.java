package org.janson.service.impl;

import org.janson.dao.IDataSourcePoolDao;
import org.janson.service.IDataSourcePoolService;
import org.janson.vo.DataSourcePool;

import java.util.List;
import java.util.Map;

/**
 * Created by WANG.TAO on 2017/5/27.
 */
public class DataSourcePoolService implements IDataSourcePoolService {

    private IDataSourcePoolDao dataSourcePoolDao;

    @Override
    public List<DataSourcePool> listDataSources() {
        DataSourcePool sourcePool = new DataSourcePool();
        sourcePool.setIseffective(1);
        return dataSourcePoolDao.queryAll(sourcePool);
    }

    @Override
    public int addDataSourcePool(DataSourcePool dataSource) {
        return dataSourcePoolDao.insert(dataSource);
    }

    @Override
    public void removeDataSourcePool(DataSourcePool dataSource) {
        dataSourcePoolDao.deleteByPrimaryKey(dataSource.getId());
    }

    @Override
    public Map<String, Object> queryByPage(DataSourcePool dataSourcePool, Integer page, Integer pageSize) {
        return dataSourcePoolDao.queryByPage(dataSourcePool,page,pageSize);
    }

    @Override
    public String getNextId() {
        return dataSourcePoolDao.getNextId();
    }

    @Override
    public void updateById(DataSourcePool sourcePool) {
        this.dataSourcePoolDao.updateByPrimaryKey(sourcePool);
    }

    public void setDataSourcePoolDao(IDataSourcePoolDao dataSourcePoolDao) {
        this.dataSourcePoolDao = dataSourcePoolDao;
    }


}
