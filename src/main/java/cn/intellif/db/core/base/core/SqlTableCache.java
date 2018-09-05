package cn.intellif.db.core.base.core;

import cn.intellif.db.core.base.annotation.Table;
import cn.intellif.db.core.base.utils.BeanUtils;

import java.util.HashMap;
import java.util.Map;

public abstract class SqlTableCache {
    private static Map<String,SqlDefination> cache = new HashMap<>();

    public static SqlDefination getSqlDefination(String className){
        return cache.get(className);
    }

    public static  void save(String className,SqlDefination sqlDefination){
        cache.put(className,sqlDefination);
    }

    public static SqlDefination getSqlDefination(Class clazz){
        String className = clazz.getName();
        SqlDefination sqlDefination = getSqlDefination(className);
        if(sqlDefination==null){
            synchronized (clazz){
                if(sqlDefination==null){
                    sqlDefination = new SqlDefination();
                    sqlDefination.setClassName(className);
                    Map<String,String> data = BeanUtils.resovleSqlType(clazz);
                    String idName = data.remove("id");
                    sqlDefination.setIdName(idName);
                    sqlDefination.setFields(BeanUtils.resovleField(clazz));
                    sqlDefination.setIdField(BeanUtils.getIdField(clazz));
                    sqlDefination.setField(data);
                    Table table = (Table) clazz.getAnnotation(Table.class);
                    sqlDefination.setTableName(table.tableName());
                    sqlDefination.setScheName(table.schemaName());
                    save(className,sqlDefination);
                }
            }
        }
        return getSqlDefination(className);
    }

}
