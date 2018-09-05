package cn.intellif.db.core.base.core;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SqlDefination {
    //类的全名称
    String className;
    //java类型和sql类型对应
    Map<String,String> field = new HashMap<>();
    //表名称
    String tableName;
    //数据库名称
    String scheName;
    //主键id名称
    String idName;
    //主键field
    Field idField;
    List<Field> fields;

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    public Field getIdField() {
        return idField;
    }

    public void setIdField(Field idField) {
        this.idField = idField;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Map<String, String> getField() {
        return field;
    }

    public void setField(Map<String, String> field) {
        this.field = field;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getScheName() {
        return scheName;
    }

    public void setScheName(String scheName) {
        this.scheName = scheName;
    }

    public String getIdName() {
        return idName;
    }

    public void setIdName(String idName) {
        this.idName = idName;
    }

    public String getSqlColumn(String fieldName){
        return field.get(fieldName);
    }
}
