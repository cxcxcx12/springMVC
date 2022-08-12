package Mybatis;

public class sqlMapping {
    String nsMethod;

    String sql;
    String resultType;

    public sqlMapping(String nsMethod, String sql, String resultType) {
        this.nsMethod = nsMethod;
        this.sql = sql;
        this.resultType = resultType;
    }

    public String getNsMethod() {
        return nsMethod;
    }

    public void setNsMethod(String nsMethod) {
        this.nsMethod = nsMethod;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }
}
