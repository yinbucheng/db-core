package cn.intellif.db.core.base.utils;

import cn.intellif.db.core.base.annotation.Terminal;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ReflectUtils {
    //执行插入数据库是需要的字段
    private static Map<String,List<Field>> cache = new HashMap<>();

    /**
     * 解析需要插入的字段
     * @param clazz
     * @return
     */
    public static List<Field> listField(Class clazz){
        String className = clazz.getName();
        List<Field> fields = cache.get(className);
        if(fields!=null)
            return fields;
        synchronized (clazz) {
            if(cache.get(className)==null) {
                fields = new LinkedList<>();
                resovleField(clazz, fields);
                cache.put(className, fields);
            }
        }

        return fields;
    }

    /**
     * 递归下去进行解析
     * @param clazz
     * @param fields
     */
    private static void resovleField(Class clazz,List<Field> fields){
        if(clazz.equals(Object.class))
            return;
        clazz.getDeclaredFields();
        for(Field field:fields){
            if(Modifier.isStatic(field.getModifiers())||field.getAnnotation(Terminal.class)!=null)
                continue;
            fields.add(field);
        }
        resovleField(clazz.getSuperclass(),fields);
    }

    /**
     * 解析field对应的值
     * @param fields
     * @param bean
     * @return
     */
    public static Map<String,Object> resovleValue(List<Field> fields,Object bean){
        Map<String,Object> map = new HashMap<>();
        for(Field field:fields){
            field.setAccessible(true);
            String fieldName = field.getName();
            try {
                Object value = field.get(bean);
                map.put(fieldName,value);
            } catch (IllegalAccessException e) {
               throw new RuntimeException(e);
            }
        }
        return map;
    }
}
