package cn.intellif.db.core.base.core;

import cn.intellif.db.core.base.annotation.Id;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

/**
 * sql语句及参数创建器
 */
public abstract class SqlCreator {

    /**
     * 构建插入语句及参数封装
     * @param bean
     * @return
     */
   public static SqlParams createInsert(Object bean){
       try {
           Class clazz = bean.getClass();
           SqlDefination sqlDefination = SqlTableCache.getSqlDefination(clazz);
           List<Field> fields = sqlDefination.getFields();
           Field idField = sqlDefination.getIdField();
           StringBuilder sql = new StringBuilder();
           List<Object> param = new LinkedList<>();
           sql.append("insert into ").append(sqlDefination.getScheName()).append(".").append(sqlDefination.getTableName()).append("(");
           //如果id的值不为空插入进去
           Object value = idField.get(bean);
           boolean first = true;
           boolean flag = true;
           if(value!=null){
               sql.append(sqlDefination.getIdName());
               param.add(value);
               first =false;
           }
           int size =fields.size();
           for(int i=0;i<size;i++){
               if(!first&&flag){
                   sql.append(",");
                   flag = false;
               }
               Field tempField = fields.get(i);
               sql.append(sqlDefination.getSqlColumn(tempField.getName()));
               if(i!=size-1){
                   sql.append(",");
               }
               param.add(tempField.get(bean));
           }
           sql.append(")values(");
           size = param.size();
           for(int i=0;i<size;i++){
               sql.append("?");
               if(i!=size-1)
                   sql.append(",");
           }
           sql.append(")");
           SqlParams sqlParams = new SqlParams(sql.toString(),param.toArray());
           return sqlParams;
       }catch (Exception e){
           throw new RuntimeException(e);
       }
   }

    /**
     * 构建批量插入语句及参数封装
     * @param beans
     * @param <T>
     * @return
     */
   public static <T> SqlParams createBatchInsert(List<T> beans){
       try {
           Class clazz = beans.get(0).getClass();
           SqlDefination sqlDefination = SqlTableCache.getSqlDefination(clazz);
           List<Field> fields = sqlDefination.getFields();
           Field idField = sqlDefination.getIdField();
           StringBuilder sql = new StringBuilder();
           List<Object[]> params = new LinkedList<>();
           sql.append("insert into ").append(sqlDefination.getScheName()).append(".").append(sqlDefination.getTableName()).append("(");
           Id id = idField.getAnnotation(Id.class);
           boolean first = true;
           boolean flag = true;
           if (id.strategy().getCode() == 1) {
               sql.append(sqlDefination.getIdName());
               first = false;
           }
           int size = fields.size();
           for (int i = 0; i < size; i++) {
               if (!first&&flag) {
                   sql.append(",");
                   flag = false;
               }
               Field tempField = fields.get(i);
               sql.append(sqlDefination.getSqlColumn(tempField.getName()));
               if (i != size - 1)
                   sql.append(",");
           }
           sql.append(")values(");
           int len = first ? size : size + 1;
           for (int i = 0; i < len; i++) {
               sql.append("?");
               if (i != len - 1)
                   sql.append(",");
           }
           sql.append(")");
           for (T bean : beans) {
               Object[] data = new Object[len];
               int i = 0;
               if (!first) {
                   data[i++] = idField.get(bean);
               }
               for(Field field:fields){
                   data[i++]=field.get(bean);
               }
               params.add(data);
           }
           SqlParams sqlParams = new SqlParams(sql.toString(),params);
           return sqlParams;
       }catch (Exception e){
           throw new RuntimeException(e);
       }
    }


   public static SqlParams createDelete(Class clazz,Serializable id){
       SqlDefination sqlDefination = SqlTableCache.getSqlDefination(clazz);
       Field idField = sqlDefination.getIdField();
       StringBuilder sql = new StringBuilder();
       sql.append("delete from ").append(sqlDefination.getScheName()).append(".").append(sqlDefination.getTableName()).append(" ").append("where ").append(sqlDefination.getIdName()).append("=").append("?");
       SqlParams sqlParams = new SqlParams(sql.toString(),id);
       return sqlParams;
   }


   public static SqlParams createSelect(Class clazz,Serializable id){
       SqlDefination sqlDefination = SqlTableCache.getSqlDefination(clazz);
       Field idField = sqlDefination.getIdField();
       StringBuilder sql = new StringBuilder();
       sql.append("select * from ").append(sqlDefination.getScheName()).append(".").append(sqlDefination.getTableName()).append(" ").append("where ").append("id = ").append("?");
       SqlParams sqlParams = new SqlParams(sql.toString(),id);
       return sqlParams;
   }

    /**
     * 构建根据id更新所有的
     * @param bean
     * @return
     * */
   public static SqlParams createUpdateAll(Object bean){
       try {
           Class clazz = bean.getClass();
           SqlDefination sqlDefination = SqlTableCache.getSqlDefination(clazz);
           Field idField = sqlDefination.getIdField();
           List<Field> fields = sqlDefination.getFields();
           StringBuilder sql = new StringBuilder();
           List<Object> param = new LinkedList<>();
           sql.append("update ").append(sqlDefination.getScheName()).append(".").append(sqlDefination.getTableName()).append(" set ");
           int size = fields.size();
           for (int i = 0; i < size; i++) {
               Field field = fields.get(0);
               sql.append(sqlDefination.getSqlColumn(field.getName())).append("=?");
               if (i != size - 1) {
                   sql.append(",");
               }
               param.add(field.get(bean));
           }
           sql.append(" where ").append(sqlDefination.getIdName()).append("=").append("?");
           param.add(idField.get(bean));
           SqlParams sqlParams = new SqlParams(sql.toString(),param.toArray());
           return sqlParams;
       }catch (Exception e){
           throw new RuntimeException(e);
       }
   }

    /**
     * 更新非空
     * @param bean
     * @return
     */
   public static SqlParams createUpdateNotNull(Object bean){
       try {
           Class clazz = bean.getClass();
           SqlDefination sqlDefination = SqlTableCache.getSqlDefination(clazz);
           Field idField = sqlDefination.getIdField();
           List<Field> fields = sqlDefination.getFields();
           StringBuilder sql = new StringBuilder();
           List<Object> param = new LinkedList<>();
           sql.append("update ").append(sqlDefination.getScheName()).append(".").append(sqlDefination.getTableName()).append(" set ");
           int size = fields.size();
           for (int i = 0; i < size; i++) {
               Field field = fields.get(i);
               Object value =field.get(bean);
               if(value==null)
                   continue;
               sql.append(sqlDefination.getSqlColumn(field.getName())).append("=?");
               if (i != size - 1) {
                   sql.append(",");
               }
               param.add(value);
           }
           sql.append(" where ").append(sqlDefination.getIdName()).append("=").append("?");
           param.add(idField.get(bean));
           SqlParams sqlParams = new SqlParams(sql.toString(),param.toArray());
           return sqlParams;
       }catch (Exception e){
           throw new RuntimeException(e);
       }
   }
}
