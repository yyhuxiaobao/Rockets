package com.pauper.straw.mq.rabbit;

import java.io.Serializable;

public class AdderO implements Serializable {

    private String city;
    private String prov;
    private String local;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProv() {
        return prov;
    }

    public void setProv(String prov) {
        this.prov = prov;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }
}
