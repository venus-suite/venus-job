package io.suite.venus.job.admin.service.impl;

import io.suite.venus.job.admin.domain.dto.TextMsgReq;
import io.suite.venus.job.admin.service.RobotService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@Slf4j
public class RobotServiceImpl   implements RobotService {
    @Autowired
    private RestTemplate restTemplate;


    /**
     * 发送消息
     *
     * @param msg
     * @return
     */
    @Override
    public boolean sendRobot(String url,String msg, List<String> usersList) {
        if(StringUtils.isEmpty(url)){
            log.error("sendRobot url 格式错误！");
            return false;
        }
        if(!url.startsWith("http")) {
            log.error("sendRobot url 格式错误！");
            return  false;
        }
        //创建请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        TextMsgReq textMsgReq = new TextMsgReq();
        textMsgReq.withContent(msg);
        if (!CollectionUtils.isEmpty(usersList)) {
            textMsgReq.withUserList(usersList);
        }
        HttpEntity<TextMsgReq> entity = new HttpEntity<>(textMsgReq, headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity( url,entity, String.class);
        String body = responseEntity.getBody();
        //{"msg":"调用成功！","code":1}
        log.info("sendRobot={}",body);
        return responseEntity.getStatusCode() == HttpStatus.OK;
    }
}
