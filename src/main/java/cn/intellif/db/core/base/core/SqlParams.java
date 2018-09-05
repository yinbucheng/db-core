package cn.intellif.db.core.base.core;

public class SqlParams {
    private String sql;
    private Object param;

    public SqlParams(String sql, Object param) {
        this.sql = sql;
        this.param = param;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public Object getParam() {
        return param;
    }

    public void setParam(Object param) {
        this.param = param;
    }
}
