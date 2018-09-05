package cn.intellif.db.core.base.utils;

import cn.intellif.db.core.base.annotation.Column;
import cn.intellif.db.core.base.annotation.Id;
import cn.intellif.db.core.base.annotation.Terminal;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public abstract class BeanUtils {

    /**
     * 解析这个类对应库中java字段和库中对应关系
     * @param clazz
     * @return
     */
    public static Map<String,String> resovleSqlType(Class clazz){
        Map<String,String> reuslt = new HashMap<>();
         resovleAllSqlType(clazz,reuslt);
         return reuslt;
    }


    public static List<Field> resovleField(Class clazz){
        List<Field> fields = new LinkedList<>();
        resovleAllField(clazz,fields);
        return fields;
    }

    /**
     * 获取所有非static且没有被@Terminal修饰的字段
     * @param clazz
     * @param fields
     */
    private static void resovleAllField(Class clazz,List<Field> fields){
        if(clazz.equals(Object.class))
            return;
        Field[] temps = clazz.getDeclaredFields();
        if(temps==null||temps.length==0)
            return;
        for(Field field:temps){
            if(Modifier.isStatic(field.getModifiers())||field.getAnnotation(Terminal.class)!=null||field.getAnnotation(Id.class)!=null)
                continue;
           field.setAccessible(true);
           fields.add(field);
        }
        resovleAllField(clazz.getSuperclass(),fields);
    }

    /**
     * 获取ID主键字段
     * @param clazz
     * @return
     */
    public static Field  getIdField(Class clazz){
        if(clazz.equals(Object.class))
           throw new RuntimeException(clazz.getName()+" 中为未发现@Id表示的主键");
        Field[] temps = clazz.getDeclaredFields();
        for(Field field:temps){
           if(field.getAnnotation(Id.class)!=null) {
               field.setAccessible(true);
               return field;
           }
        }
        return getIdField(clazz.getSuperclass());
    }




    private static void resovleAllSqlType(Class clazz,Map<String,String> result){
        if(clazz.equals(Object.class))
            return;
       Field[] fields = clazz.getDeclaredFields();
       if(fields==null||fields.length==0)
           return;
       for(Field field:fields){
          if(Modifier.isStatic(field.getModifiers())||field.getAnnotation(Terminal.class)!=null)
              continue;
          String javaName = field.getName();
          saveId(result, field);
          saveColumn(result, field, javaName);
       }
        resovleAllSqlType(clazz.getSuperclass(),result);
    }

    private static void saveColumn(Map<String, String> result, Field field, String javaName) {
        String sqlName = null;
        Column column = field.getAnnotation(Column.class);
        if(column!=null){
            sqlName = column.columnName();
        }else{
            sqlName = changeJavatoSql(javaName);
        }
        result.put(javaName,sqlName);
    }

    private static String changeJavatoSql(String javaName){
        StringBuilder sb = new StringBuilder();
        char[] datas = javaName.toCharArray();
        for(char data:datas){
            if (data >= 'A' && data<= 'Z') {
                sb.append("_").append((data+"").toLowerCase());
            }else{
                sb.append(data);
            }
        }
        return sb.toString();
    }

    private static void saveId(Map<String, String> result, Field field) {
        Id id = field.getAnnotation(Id.class);
        if(id!=null){
            String idName = id.columnName();
            if(idName==null){
                idName = field.getName();
            }
            result.put("id",idName);
        }
    }

    public static Map<String,Object> resovleValueNotNull(Object bean){
        Class clazz = bean.getClass();
        clazz.getDeclaredFields();
        return null;
    }
}
