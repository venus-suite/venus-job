package io.suite.venus.job.admin.core.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * Created by xuxueli on 16/9/30.
 */
@ApiModel("job注册")
public class XxlJobRegistry {

    private int id;
    @ApiModelProperty("注册执行器")
    private String registryGroup;
    @ApiModelProperty("注册执行器Key")
    private String registryKey;
    @ApiModelProperty("注册namespace")
    private String nameSpace;
    @ApiModelProperty("注册执行器value")
    private String registryValue;
    @ApiModelProperty("更新时间")
    private Date updateTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRegistryGroup() {
        return registryGroup;
    }

    public void setRegistryGroup(String registryGroup) {
        this.registryGroup = registryGroup;
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

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getNameSpace() {
        return nameSpace;
    }

    public void setNameSpace(String nameSpace) {
        this.nameSpace = nameSpace;
    }
}
