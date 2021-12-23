package io.suite.venus.job.admin.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class MsgMarkDownReq {

//    {
//        "msgtype": "markdown",
//            "markdown": {
//                   "content": ""
//          }
//    }

    @JsonProperty(value = "msgtype")
    private String msgType = "markdown";

    @JsonProperty(value = "markdown")
    private Map<String, Object> markdown =new HashMap<>();

    @JsonIgnore
    public  MsgMarkDownReq withMarkdownText(String msg) {
        markdown.put("content",msg);
        return  this;
    }

}
