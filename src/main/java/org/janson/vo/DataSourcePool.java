package org.janson.vo;

public class DataSourcePool {
    private Integer id;

    private String tenant;

    private String username;

    private String password;

    private String dbtype;

    private String url;

    private Integer iseffective;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTenant() {
        return tenant;
    }

    public void setTenant(String tenant) {
        this.tenant = tenant == null ? null : tenant.trim();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getDbtype() {
        return dbtype;
    }

    public void setDbtype(String dbtype) {
        this.dbtype = dbtype == null ? null : dbtype.trim();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    public Integer getIseffective() {
        return iseffective;
    }

    public void setIseffective(Integer iseffective) {
        this.iseffective = iseffective;
    }

    @Override
    public String toString() {
        return "DataSourcePool{" +
                "id=" + id +
                ", tenant='" + tenant + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", dbtype='" + dbtype + '\'' +
                ", url='" + url + '\'' +
                ", iseffective=" + iseffective +
                '}';
    }
}