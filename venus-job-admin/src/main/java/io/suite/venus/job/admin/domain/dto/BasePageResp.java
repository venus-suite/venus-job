

package io.suite.venus.job.admin.domain.dto;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 *
 */
public class BasePageResp<T> {

    @ApiModelProperty("列表")
    List<T> contentList;
    @ApiModelProperty("总数")
    private long total;
    @ApiModelProperty("总页数")
    private int pages;
    public List<T> getContentList() {
        return contentList;
    }
    public void setContentList(List<T> contentList) {
        this.contentList = contentList;
    }
    public long getTotal() {
        return total;
    }
    public void setTotal(long total) {
        this.total = total;
    }
    public int getPages() {
        return pages;
    }
    public void setPages(int pages) {
        this.pages = pages;
    }


}