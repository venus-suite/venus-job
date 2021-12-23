package io.suite.venus.job.admin.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class TextMsgReq {

//    {
//        "msgtype": "text",
//            "text": {
//                   "content": ""
//                      "mentioned_mobile_list":[]
//          }
//    }

    @JsonProperty(value = "msgtype")
    private String msgType = "text";

    @JsonProperty(value = "text")
    private Map<String, Object> text = new HashMap<>();

    @JsonIgnore
    public List<String> userList;

    /**
     * 设置用户
     *
     * @param content
     */
    @JsonIgnore
    public TextMsgReq withContent(String content) {
        text.put("content", content);
        return this;
    }

    /**
     * 设置@人
     *
     * @param userList
     */
    @JsonIgnore
    public TextMsgReq withUserList(List<String> userList) {
        text.put("mentioned_mobile_list", userList);
        return this;
    }


}
