package org.janson.common;

import com.alibaba.druid.pool.DruidDataSource;
import org.janson.DataSourceInter;
import org.janson.dataBase.MysqlDataSource;
import org.janson.dataBase.OracleDataSource;
import org.janson.vo.DataSourcePool;

/**
 * Created by WANG.TAO on 2017/5/27.
 */
public class DataSourcePoolUtil {

    private static DataSourceInter sourceCreator;

    public static synchronized DruidDataSource createDataSource(DataSourcePool sourcePool) {
        String dataBaseType = sourcePool.getDbtype();
        DruidDataSource dataSource = null;
        if (dataBaseType != null) {
            if ( Constant.DataBaseType.MYSQL.equals(dataBaseType)) {
                sourceCreator = new MysqlDataSource();
            } else if (Constant.DataBaseType.ORACLE.equals(dataBaseType)) {
                sourceCreator = new OracleDataSource();
            } else {
                throw new RuntimeException("Do not support dataBase-Type with " +dataBaseType +" !");
            }
            dataSource = sourceCreator.getInstance(sourcePool);
        }
        return dataSource;
    }
}
