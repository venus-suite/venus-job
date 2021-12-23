package io.suite.venus.job.demo.task;


import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import org.springframework.stereotype.Component;

@Component

@JobHandler(value="echoService")
public class EchoService extends IJobHandler {

    @Override
    public ReturnT<String> execute(String param) throws Exception {
        System.out.println("echo");
        return new ReturnT<String>(200, "echo ok");
    }

}
