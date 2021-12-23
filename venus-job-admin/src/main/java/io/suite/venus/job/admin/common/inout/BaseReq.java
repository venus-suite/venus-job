package io.suite.venus.job.admin.common.inout;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;

@Data
public class BaseReq {
    // 当前页
    @Min(value=1,message="currentPage最小1")
    @ApiModelProperty(notes="第几页",example="1")
    protected int currentPage = 1;

    // 每页显示记录数
    @Range(min=1, max=200,message = "pageSize最小1,最大200")
    @ApiModelProperty(notes="每页大小",example="10")
    protected int pageSize = 10;

    @ApiModelProperty(notes="总页数(导出的时候分页使用)",example="0")
    protected int totalPage = 0;

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
    public int getStartIndex() {
        int startIndex = (currentPage - 1) * pageSize;
        if (startIndex < 0) {
            startIndex = 0;
        }
        return startIndex;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
}
