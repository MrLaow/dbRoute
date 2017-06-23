package org.janson.common;

/**
 * Created by WANG.TAO on 2017/5/27.
 */
public class DynamicDataSourceHolder {
  
    public static final ThreadLocal<String> tenantDataSource  = new ThreadLocal<String>();
      
    /** 
     * 清空当前数据源 
     */  
    public static void clear(){
        tenantDataSource.remove();
    }  
      
    /** 
     * 获取当前数据源 
     */  
    public static String getDataSourceType(){
        return tenantDataSource.get();
    }  
      
    /** 
     * 设置当前数据源 
     */  
    public static void setDataSourceType(String dataSourceType){
        tenantDataSource.set(dataSourceType);
    }  
      
}  