package org.haycco.tanlan.common.config.properties;

/**
 * Swagger Contacter
 *
 * @Author LiuQing.Qin
 * @Date 2018/10/19 15:16:20
 */
public class Contact {

    private String name;
    private String url;
    private String email;
    public Contact(){}

    public Contact(String name, String url, String email) {
        this.name = name;
        this.url = url;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
