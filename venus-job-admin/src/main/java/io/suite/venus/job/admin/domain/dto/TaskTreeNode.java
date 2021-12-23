package io.suite.venus.job.admin.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TaskTreeNode {
    @JsonProperty(value = "id")
    private String id;
    @JsonProperty(value = "isroot")
    private boolean isroot;
    @JsonProperty(value = "topic")
    private String topic;
    @JsonProperty(value = "direction")
    private String direction;
    @JsonProperty(value = "parentid")
    private  String parentId;
    @JsonProperty(value = "expanded")
    private  boolean expanded=false;
}
