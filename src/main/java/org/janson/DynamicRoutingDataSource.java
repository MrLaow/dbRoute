package org.janson;

import com.alibaba.druid.pool.DruidDataSource;
import org.janson.common.DynamicDataSourceHolder;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by WANG.TAO on 2017/5/26.
 * @author wang.tao
 */
public class DynamicRoutingDataSource extends AbstractRoutingDataSource{


    private Object defaultDataSource;

    /**
     * 全局的数据源的属性
     */
    private Map<Object, Object> targetDataSource = new HashMap<Object, Object>();

    @Override
    protected Object determineCurrentLookupKey() {
        final String dataSource ;
        if ((dataSource = DynamicDataSourceHolder.getDataSourceType()) == null) {
            return defaultDataSource.toString();
        }
        return dataSource;
    }

    public void setTargetDataSource(Map targetDataSource) {
        this.targetDataSource = targetDataSource;
        setTargetDataSources(this.targetDataSource);
    }

    public void addTargetDataSource(String key, DruidDataSource dataSource) {
        this.targetDataSource.put(key, dataSource);
        this.setTargetDataSources(targetDataSource);
    }

    @Override
    public void setTargetDataSources(Map targetDataSources) {
        super.setTargetDataSources(targetDataSources);
        super.afterPropertiesSet();
    }

    public Map<Object, Object> getTargetDataSource() {
        return targetDataSource;
    }

    public void setDefaultDataSource(String defaultDataSource) {
        this.defaultDataSource = defaultDataSource;
        setDefaultTargetDataSource(targetDataSource.get(defaultDataSource));
        DynamicDataSourceHolder.setDataSourceType(defaultDataSource);
    }
}
