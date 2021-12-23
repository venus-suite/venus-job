package com.xxl.job.core.biz.model;

import java.io.Serializable;

/**
 * Created by xuxueli on 2017-05-10 20:22:42
 */
public class RegistryParam implements Serializable {
    private static final long serialVersionUID = 42L;

    private String registGroup;
    private String namespace;
    private String registryKey;
    private String registryValue;

    public RegistryParam(){}
    public RegistryParam(String registerGroup,String namespace, String registryKey, String registryValue) {
        this.registGroup = registerGroup;
        this.registryKey = registryKey;
        this.registryValue = registryValue;
        this.namespace=namespace;
    }

    public String getRegistGroup() {
        return registGroup;
    }

    public void setRegistGroup(String registGroup) {
        this.registGroup = registGroup;
    }

    public String getRegistryKey() {
        return registryKey;
    }

    public void setRegistryKey(String registryKey) {
        this.registryKey = registryKey;
    }

    public String getRegistryValue() {
        return registryValue;
    }

    public void setRegistryValue(String registryValue) {
        this.registryValue = registryValue;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    @Override
    public String toString() {
        return "RegistryParam{" +
                "registGroup='" + registGroup + '\'' +
                "namespace='" + namespace + '\'' +
                ", registryKey='" + registryKey + '\'' +
                ", registryValue='" + registryValue + '\'' +
                '}';
    }
}
