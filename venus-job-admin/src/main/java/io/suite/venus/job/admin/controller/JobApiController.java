package io.suite.venus.job.admin.controller;

import io.suite.venus.job.admin.controller.annotation.PermessionLimit;
import io.suite.venus.job.admin.core.schedule.XxlJobDynamicScheduler;
import com.xxl.job.core.biz.AdminBiz;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**

 */
@RestController
@Api(tags = "jobAPI远程调用接口",hidden = true)
public class JobApiController implements InitializingBean {


    @Override
    public void afterPropertiesSet() throws Exception {

    }

    @RequestMapping(AdminBiz.MAPPING)
    @ApiOperation(value = "远程api调用接口", notes = "",httpMethod ="POST")
    @PermessionLimit(limit=false)
    public void api(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        XxlJobDynamicScheduler.invokeAdminService(request, response);
    }


}
