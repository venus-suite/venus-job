package io.suite.venus.job.admin.core.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by xuxueli on 16/9/30.
 */
@ApiModel("执行器")
public class XxlJobGroup {
    @ApiModelProperty("主键id")
    private int id;
    @ApiModelProperty("appName")
    private String appName;
    @ApiModelProperty("命名空间")
    private String namespace;
    @ApiModelProperty("命名空间")
    private Long namespaceId;
    @ApiModelProperty("标题")
    private String title;
    @ApiModelProperty("排序")
    private int order;
    @ApiModelProperty("执行器地址类型：0=自动注册、1=手动录入")
    private int addressType;        // 执行器地址类型：0=自动注册、1=手动录入
    @ApiModelProperty("执行器地址列表，多地址逗号分隔(手动录入)")
    private String addressList;     // 执行器地址列表，多地址逗号分隔(手动录入)

    // registry list
    @ApiModelProperty("执行器地址列表(系统注册)")
    private List<String> registryList;  // 执行器地址列表(系统注册)
    public List<String> getRegistryList() {
        if (addressList!=null && addressList.trim().length()>0) {
            registryList = new ArrayList<String>(Arrays.asList(addressList.split(",")));
        }
        return registryList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getAddressType() {
        return addressType;
    }

    public void setAddressType(int addressType) {
        this.addressType = addressType;
    }

    public String getAddressList() {
        return addressList;
    }

    public void setAddressList(String addressList) {
        this.addressList = addressList;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public Long getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Long namespaceId) {
        this.namespaceId = namespaceId;
    }

    public List<XxlJobServerInfo> getServer() {
        return server;
    }

    public void setServer(List<XxlJobServerInfo> server) {
        this.server = server;
    }

    private List<XxlJobServerInfo> server;
}
