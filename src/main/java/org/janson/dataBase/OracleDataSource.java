package org.janson.dataBase;

import com.alibaba.druid.pool.DruidDataSource;
import org.janson.DataSourceInter;
import org.janson.vo.DataSourcePool;

/**
 * Created by WANG.TAO User on 2017/5/27.
 */
public class OracleDataSource extends DataSourceInter {

    @Override
    public DruidDataSource getInstance(DataSourcePool sourcePool) {
        super.prepareDruidDataSource(sourcePool);
        return druidDataSource;
    }
}
