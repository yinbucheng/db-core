package cn.intellif.db.core.base;

import cn.intellif.db.core.base.core.SqlCreator;
import cn.intellif.db.core.base.core.SqlDefination;
import cn.intellif.db.core.base.core.SqlParams;
import cn.intellif.db.core.base.core.SqlTableCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.sql.*;
import java.util.List;

public class BaseDaoImpl<T> implements BaseDao<T> {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private Class<T> clazz;

    public BaseDaoImpl(){
        ParameterizedType t = (ParameterizedType) this.getClass().getGenericSuperclass();
        //获取泛型参数的实际类型
        this.clazz = (Class<T>) t.getActualTypeArguments()[0];
    }

    @Override
    public int save(T bean) {
        SqlParams sqlParams = SqlCreator.createInsert(bean);
        KeyHolder keyHolder = new GeneratedKeyHolder();
      int result =  jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = (PreparedStatement) con.prepareStatement(sqlParams.getSql(), Statement.RETURN_GENERATED_KEYS);
                Object[] param = (Object[]) sqlParams.getParam();
                int len = param.length;
                for(int i=0;i<len;i++){
                    ps.setObject(i+1,param[i]);
                }
                return ps;
            }
        },keyHolder);
        Class clazz = bean.getClass();
        SqlDefination sqlDefination = SqlTableCache.getSqlDefination(clazz);
        Field idField = sqlDefination.getIdField();
        try {
            Object value = idField.get(bean);
            if(value==null){
               long idValue =  keyHolder.getKey().longValue();
               idField.set(bean,idValue);
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
        return result;

    }


    @Override
    public int[] batchSave(List<T> beans) {
        SqlParams sqlParams = SqlCreator.createBatchInsert(beans);
        return jdbcTemplate.batchUpdate(sqlParams.getSql(), (List<Object[]>) sqlParams.getParam());
    }

    @Override
    public int update(T bean) {
        SqlParams sqlParams = SqlCreator.createUpdateAll(bean);
        return jdbcTemplate.update(sqlParams.getSql(),(Object[])sqlParams.getParam());
    }

    @Override
    public int updateNotNull(T bean) {
        SqlParams sqlParams = SqlCreator.createUpdateNotNull(bean);
        return jdbcTemplate.update(sqlParams.getSql(),(Object[])sqlParams.getParam());
    }

    @Override
    public T findOne(Serializable id) {
        SqlParams sqlParams = SqlCreator.createSelect(clazz,id);
      List<T> result = jdbcTemplate.query(sqlParams.getSql(), new Object[]{sqlParams.getParam()}, new RowMapper<T>() {
            @Nullable
            @Override
            public T mapRow(ResultSet rs, int rowNum) throws SQLException {
                SqlDefination sqlDefination = SqlTableCache.getSqlDefination(clazz);
                try {
                    T bean = clazz.newInstance();
                    Field idField = sqlDefination.getIdField();
                   Object value =  rs.getObject(sqlDefination.getIdName());
                   idField.set(bean,value);
                   List<Field> fields = sqlDefination.getFields();
                   for(Field field:fields){
                       String columName = sqlDefination.getSqlColumn(field.getName());
                       value = rs.getObject(columName);
                       field.set(bean,value);
                   }
                   return bean;
                }catch (Exception e){
                    throw new RuntimeException(e);
                }
            }
        });
      if(result==null||result.size()==0)
          return null;
      return result.get(0);
    }

    @Override
    public int delete(Serializable id) {
        SqlParams sqlParams = SqlCreator.createDelete(clazz,id);
        return jdbcTemplate.update(sqlParams.getSql(),sqlParams.getParam());
    }
}
