package io.suite.venus.job.admin.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import java.util.Date;

@Data
public class OperaLogRes {

    @ApiModelProperty(value = "id")
    private Integer id;
    @Column(name = "user_email")
    @ApiModelProperty(value = "用户邮箱")
    private String userEmail;
    @ApiModelProperty(value = "用户名称")
    @Column(name = "username")
    private String userName;

    @ApiModelProperty(value = "访问ip")
    @Column(name = "ip")
    private String ip;

    @ApiModelProperty(value = "访问地址URL")

    @Column(name = "url")
    private String url;

    @ApiModelProperty(value = "访问事件")
    @Column(name = "event_name")
    private String eventName;

    //@ApiModelProperty(value = "访问参数")
   // @Column(name = "params")
   // private String params;

    @ApiModelProperty(value = "访问时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(name = "create_time")
    private Date createTime;
}
