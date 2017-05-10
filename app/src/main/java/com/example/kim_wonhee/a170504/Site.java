package com.example.kim_wonhee.a170504;

/**
 * Created by kim_wonhee on 2017. 5. 9..
 */

public class Site {

    String name;
    String url;

    public Site(String name, String url) {
        this.name = name;
        this.url = url;
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
}
