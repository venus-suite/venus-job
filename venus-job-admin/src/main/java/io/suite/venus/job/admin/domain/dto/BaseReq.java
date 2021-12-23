package io.suite.venus.job.admin.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;

@Data
public class BaseReq {

    //current,pageSize,total
    // 当前页
    @Min(value = 1, message = "current最小1")
    @ApiModelProperty(notes = "第几页", example = "1")
    protected int current = 1;

    // 每页显示记录数
    @Range(min = 1, max = 200, message = "pageSize最小1,最大200")
    @ApiModelProperty(notes = "每页大小", example = "10")
    protected int pageSize = 10;

    @ApiModelProperty(notes = "总页数(导出的时候分页使用)", example = "0")
    protected int total = 0;

}
