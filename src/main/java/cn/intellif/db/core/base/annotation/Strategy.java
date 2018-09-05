package cn.intellif.db.core.base.annotation;

public enum  Strategy {
    NORM("一般主键",1),AUTO_INCREMENT("自动增加",2);
    private String name;
    private int code;

    Strategy(String name, int code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
