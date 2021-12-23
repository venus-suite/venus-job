package io.suite.venus.job.admin.controller;

import io.suite.venus.job.admin.common.inout.BaseResponse;
import io.suite.venus.job.admin.common.log.ActionOperateLog;
import io.suite.venus.job.admin.common.util.AssertUtil;
import io.suite.venus.job.admin.core.model.XxlJobGroup;

import io.suite.venus.job.admin.domain.dto.*;
import io.suite.venus.job.admin.common.inout.BaseMngPageResp;
import io.suite.venus.job.admin.service.JobGroupService;
import io.suite.venus.job.admin.service.NamespaceService;
import io.suite.venus.job.admin.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/job/namespace")
@Api(tags = "命名空间管理")
public class NamespaceController {

    @Autowired
    private NamespaceService namespaceService;

    @Autowired
    private UserService userService;
    @Autowired
    private JobGroupService jobGroupService;

    @RequestMapping(value = "/getNamespaceList", method = RequestMethod.POST)
    @ApiOperation(value = "命名空间列表", notes = "命名空间管理")
    @ActionOperateLog(eventName = "命名空间列表[getNamespaceList]", type = 0)
    public BaseResponse<BasePageResp<NamespaceAndUser>> getNamespaceList(@RequestBody NamespaceReq req) {
        UserInfo userInfo = UserInfoContext.getUserInfo();
        AssertUtil.isTrue(req.getCurrent() > 0, "页面必须大于0");
        AssertUtil.isTrue(req.getPageSize() > 0, "pageSize必须大于0");

        return BaseResponse.valueOfSuccess(namespaceService.getNamespaceList(req, userInfo));
    }

    @RequestMapping(value = "/addNamespace", method = RequestMethod.POST)
    @ApiOperation(value = "添加命名空间", notes = "命名空间管理")
    @ActionOperateLog(eventName = "添加命名空间[addNamespace]", type = 0)
    public BaseResponse<Boolean> addNamespace(@RequestBody NamespaceReq req) {
        UserInfo userInfo = UserInfoContext.getUserInfo();
        checkNamespace(req);
        AssertUtil.isTrue(!namespaceService.isExits(req), "命名空间已经存在！");
        return BaseResponse.valueOfSuccess(namespaceService.addNamespace(req));
    }

    @RequestMapping(value = "/delNamespace", method = RequestMethod.POST)
    @ApiOperation(value = "删除命名空间", notes = "命名空间管理")
    @ActionOperateLog(eventName = "删除命名空间[delNamespace]", type = 0)
    public BaseResponse<Boolean> delNamespace(@RequestBody NamespaceReq req) {
        UserInfo userInfo = UserInfoContext.getUserInfo();
        AssertUtil.isTrue(req.getNamespaceId() > 0, "namespaceId不能为空");
        return BaseResponse.valueOfSuccess(namespaceService.delNamespace(req));
    }


    @RequestMapping(value = "/updateNamespace", method = RequestMethod.POST)
    @ApiOperation(value = "编辑命名空间", notes = "命名空间管理")
    @ActionOperateLog(eventName = "编辑命名空间[updateNamespace]", type = 0)
    public BaseResponse<Boolean> updateNamespace(@RequestBody NamespaceReq req) {
        UserInfo userInfo = UserInfoContext.getUserInfo();
        checkNamespace(req);
        AssertUtil.isTrue(req.getNamespaceId() > 0, "namespaceId不能为空");
        AssertUtil.isTrue(!namespaceService.isExitsNotMe(req, req.getNamespaceId()), "命名空间已经存在！");
        return BaseResponse.valueOfSuccess(namespaceService.updateNamespace(req));
    }

    @RequestMapping(value = "/addNamespaceToUser", method = RequestMethod.POST)
    @ApiOperation(value = "授权用户命名空间", notes = "命名空间管理")
    @ActionOperateLog(eventName = "授权用户命名空间[addNamespaceToUser]", type = 0)
    public BaseResponse<Boolean> addNamespaceToUser(@RequestBody UserNamespaceReq req) {
        UserInfo userInfo = UserInfoContext.getUserInfo();
        AssertUtil.isTrue(!StringUtils.isEmpty(req.getNamespaceIdList()), "命名空间不能为空");
        AssertUtil.isTrue(!CollectionUtils.isEmpty(req.getEmailList()), "用户邮箱不能为空");
        boolean isAdmin = userService.isSuperAdmin(userInfo.getEmail());
        if (!isAdmin) {
            req.setNeedAudit(1);
        }
        return BaseResponse.valueOfSuccess(namespaceService.addNamespaceToUser(req));
    }


    @RequestMapping(value = "/removeUserNamespace", method = RequestMethod.POST)
    @ApiOperation(value = "删除用户命名空间权限", notes = "命名空间管理")
    @ActionOperateLog(eventName = "删除用户命名空间权限[removeUserNamespace]", type = 0)
    public BaseResponse<Boolean> removeUserNamespace(@RequestBody UserNamespaceRightReq req) {
        UserInfo userInfo = UserInfoContext.getUserInfo();
        AssertUtil.isTrue(req.getNamespaceId() > 0, "命名空间不能为空");
        AssertUtil.isTrue(!StringUtils.isEmpty(req.getEmail()), "用户邮箱不能为空");
        return BaseResponse.valueOfSuccess(namespaceService.removeUserNamespace(req));
    }

    private void checkNamespace(NamespaceReq req) {
        AssertUtil.isTrue(req != null, "NamespaceReq不能为空");
        AssertUtil.isTrue(!StringUtils.isEmpty(req.getNamespace()), "命名空间不能为空");
        AssertUtil.isTrue(!StringUtils.isEmpty(req.getService()), "服务名不能为空");
    }


    @RequestMapping(value = "/getTaskTreeNode", method = RequestMethod.POST)
    @ApiOperation(value = "获取图谱图", notes = "命名空间管理")
    public BaseResponse<List<TaskTreeNode>> getTaskTreeNode() {

        List<TaskTreeNode> nodeList = new ArrayList<>();

        UserInfo userInfo = UserInfoContext.getUserInfo();
        NamespaceReq req = new NamespaceReq();
        req.setCurrent(1);
        req.setPageSize(5000);
        BasePageResp<NamespaceAndUser> namespace = namespaceService.getNamespaceList(req, userInfo);

        //add root
        TaskTreeNode root = new TaskTreeNode();
        root.setIsroot(true);
        root.setTopic("任务图谱");
        root.setId("root");
        nodeList.add(root);

        if (!CollectionUtils.isEmpty(namespace.getContentList())) {
            //group info
            GroupInfoReq groupInfoReq = new GroupInfoReq();
            groupInfoReq.setPageSize(Integer.MAX_VALUE - 100);
            groupInfoReq.setCurrent(1);
            List<XxlJobGroup> groupList = null;
            BaseMngPageResp<XxlJobGroup> jobGroupList = jobGroupService.getAllJobGroup(groupInfoReq, userInfo);
            if (jobGroupList != null && !CollectionUtils.isEmpty(jobGroupList.getContentList())) {
                groupList = jobGroupList.getContentList();
            }

            List<NamespaceAndUser> namespaceAndUserList = namespace.getContentList();
            List<JobGroupStat> jobGroupStats = namespaceService.getStatByJobGroup();
            for (NamespaceAndUser andUser : namespaceAndUserList) {
                TaskTreeNode e = new TaskTreeNode();
                e.setDirection("right");
                e.setId("ns_" + andUser.getNamespaceId());
                e.setTopic(andUser.getRemark()+"("+andUser.getNamespace()+":"+andUser.getService()+")");
                e.setParentId("root");
                Long namespaceId = andUser.getNamespaceId();
                if (groupList != null) {
                    for (XxlJobGroup xxlJobGroup : groupList) {
                        if (Objects.equals(namespaceId, xxlJobGroup.getNamespaceId())) {
                            TaskTreeNode je = new TaskTreeNode();
                            je.setDirection("right");
                            je.setId("group_" + xxlJobGroup.getId());
                            je.setTopic(xxlJobGroup.getTitle());
                            je.setParentId("ns_" + andUser.getNamespaceId());
                            je.setExpanded(false);
                            nodeList.add(je);
                        }
                    }
                }
                e.setIsroot(false);
                nodeList.add(e);
            }
            bindData(nodeList,groupList,jobGroupStats);
        }
        return BaseResponse.valueOfSuccess(nodeList);
    }


    private void bindData(List<TaskTreeNode> nodeList,
                          List<XxlJobGroup> groupList, List<JobGroupStat> jobGroupStats) {
        if(jobGroupStats==null) return;
        for (XxlJobGroup xxlJobGroup : groupList) {
            TaskTreeNode je = new TaskTreeNode();
            je.setDirection("right");
            je.setId("stat_" + xxlJobGroup.getId());

            je.setParentId("group_" + xxlJobGroup.getId());
            int totals=0;
            int running=0;
            int stop=0;
            for(JobGroupStat jobGroupStat:jobGroupStats ) {
                if(jobGroupStat.getJobGroup()==xxlJobGroup.getId()) {
                   totals+=jobGroupStat.getGroupCount();
                   if(jobGroupStat.getJobStatus().equals("NORMAL")){
                       running+=jobGroupStat.getGroupCount();
                   }else{
                       stop+=jobGroupStat.getGroupCount();
                   }
                }
            }
            je.setExpanded(false);
            je.setTopic("总任务数："+totals + " 启动："+running +"  暂停：" +stop + "");
            nodeList.add(je);
        }
    }
}
