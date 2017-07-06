package org.janson.service.impl;

import org.janson.DynamicRoutingDataSource;
import org.janson.common.DataSourcePoolUtil;
import org.janson.service.IDataSourceHandler;
import org.janson.service.IDataSourcePoolService;
import org.janson.util.StringUtils;
import org.janson.vo.DataSourcePool;

import java.util.List;

/**
 * Created by WANG.TAO on 2017/5/31.
 */
public class DataSourceHandler implements IDataSourceHandler {

    private DynamicRoutingDataSource dataSource;
    /**
     * 读取多数据源配置信息的服务
     */
    private IDataSourcePoolService poolService;

    public DataSourceHandler() {
    }

    private void initailized() {
        List<DataSourcePool> poolList = poolService.listDataSources();
        if (poolList != null && poolList.size() > 0) {
            for (DataSourcePool sourcePool : poolList) {
                if (sourcePool != null && StringUtils.isNotEmptyOrNull(sourcePool.getTenant())) {
                    dataSource.addTargetDataSource(sourcePool.getTenant(), DataSourcePoolUtil.createDataSource(sourcePool));
                }
            }
        }
        System.out.println(dataSource.getTargetDataSource());
    }

    @Override
    public int addTerent(String tenant ,DataSourcePool dataSourcePool) {
        int id = poolService.addDataSourcePool(dataSourcePool);
        dataSource.addTargetDataSource(tenant,DataSourcePoolUtil.createDataSource(dataSourcePool));
        return id;
    }

    @Override
    public void removeTerent(DataSourcePool dataSource) {
        poolService.removeDataSourcePool(dataSource);
    }

    public void setDataSource(DynamicRoutingDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setPoolService(IDataSourcePoolService poolService) {
        this.poolService = poolService;
    }
}
