package org.janson;

import com.alibaba.druid.pool.DruidDataSource;
import org.janson.vo.DataSourcePool;

/**
 * Created by WANG.TAO on 2017/5/27.
 */
public abstract class DataSourceInter {


    protected DruidDataSource druidDataSource = new DruidDataSource();

    {
        druidDataSource.setInitialSize(5);//初始化连接数
        druidDataSource.setMinIdle(2);//最小连接池数量
        druidDataSource.setMaxActive(100);//最大连接池数量
        /*druidDataSource.setRemoveAbandoned(true);//连接超时是否回收
        druidDataSource.setRemoveAbandonedTimeoutMillis(1l);//超时时间；单位为秒
        druidDataSource.setLogAbandoned(true);//关闭超时连接时输出错误日志*/
    }

    public abstract DruidDataSource getInstance(DataSourcePool sourcePool);

    protected void prepareDruidDataSource(DataSourcePool sourcePool){
        druidDataSource.setUsername(sourcePool.getUsername());
        druidDataSource.setPassword(sourcePool.getPassword());
        druidDataSource.setUrl(sourcePool.getUrl());
    }

}
