package io.suite.venus.job.admin.controller;

import io.suite.venus.job.admin.common.inout.BaseMngPageResp;
import io.suite.venus.job.admin.common.inout.BaseResponse;
import io.suite.venus.job.admin.common.log.ActionOperateLog;
import io.suite.venus.job.admin.common.util.AssertUtil;
import io.suite.venus.job.admin.controller.annotation.AuditLimit;
import io.suite.venus.job.admin.core.model.Namespace;
import io.suite.venus.job.admin.core.model.XxlJobGroup;
import io.suite.venus.job.admin.core.model.XxlJobServerInfo;
import io.suite.venus.job.admin.domain.dto.GroupInfoReq;
import io.suite.venus.job.admin.domain.dto.NamespaceResp;
import io.suite.venus.job.admin.domain.dto.UserInfo;
import io.suite.venus.job.admin.domain.dto.UserInfoContext;
import io.suite.venus.job.admin.enums.OperaCodeEnum;
import io.suite.venus.job.admin.service.JobGroupService;
import io.suite.venus.job.admin.service.UserService;
import io.suite.venus.job.admin.service.VmService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * job group controller
 */
@RestController
@RequestMapping("/job/jobgroup")
@Api(tags = "执行器管理")
public class JobGroupController {

	@Resource
	public JobGroupService jobGroupService;

	@Resource
	public UserService userService;

	@Autowired
	private VmService vmService;

	@RequestMapping(value = "/",method = RequestMethod.POST)
	@ApiOperation(value = "job执行器列表", notes = "job执行器")
	public BaseResponse<BaseMngPageResp<XxlJobGroup>> jobGroupPageList(@RequestBody GroupInfoReq groupInfoReq) {
		// job group (executor)
		UserInfo  userInfo=UserInfoContext.getUserInfo();
		if(!StringUtils.isEmpty(groupInfoReq.getNamespace())) {
			try {
				groupInfoReq.setNamespaceId(Integer.parseInt(groupInfoReq.getNamespace()));
			} catch (Exception ex) {
			}
		}
		BaseMngPageResp<XxlJobGroup> list = jobGroupService.getAllJobGroup(groupInfoReq ,userInfo);
		if(list!=null && !CollectionUtils.isEmpty(list.getContentList())) {
			List<XxlJobGroup> jobGroupList = list.getContentList();
			List<String> allIpList = new ArrayList<>();
			for (XxlJobGroup group : jobGroupList) {
				List<String> ipList = group.getRegistryList();
				if (!CollectionUtils.isEmpty(ipList)) {
					allIpList.addAll(ipList);
				}
			}
			if(!CollectionUtils.isEmpty(allIpList)) {
				List<XxlJobServerInfo> allServerList = vmService.getServerListByAddressList(allIpList);
				Map<String, XxlJobServerInfo> jobServerInfoMap = new HashMap<>();
				if (!CollectionUtils.isEmpty(allServerList)) {
					for (XxlJobServerInfo xxlJobServerInfo : allServerList) {
						jobServerInfoMap.put(xxlJobServerInfo.getServerName(), xxlJobServerInfo);
					}
				}

				for (XxlJobGroup group : jobGroupList) {
					List<String> ipList = group.getRegistryList();
					if (!CollectionUtils.isEmpty(ipList)) {
						group.setServer(new ArrayList<>());
						for (String ip : ipList) {
							XxlJobServerInfo serverInfo = jobServerInfoMap.get(ip);
							if (serverInfo != null) {
								group.getServer().add(serverInfo);
							}
						}
					}
				}
			}
		}
		return BaseResponse.valueOfSuccess(list);
	}

	@RequestMapping( value = "/save",method = RequestMethod.POST)
	@ApiOperation(value = "job执行器保存", notes = "job执行器保存")
	@ActionOperateLog(eventName = "job执行器保存[save]",type = 0)
	@AuditLimit(code = OperaCodeEnum.GROUP_ADD)
	public BaseResponse<String> save(@RequestBody  XxlJobGroup xxlJobGroup){
		AssertUtil.isTrue(xxlJobGroup.getNamespaceId()>0,"命名空间不能为空！");
		return  jobGroupService.save(xxlJobGroup);
	}

	@RequestMapping(value = "/update",method = RequestMethod.POST)
	@ApiOperation(value = "job执行器编辑", notes = "job执行器")
	@ActionOperateLog(eventName = "job执行器编辑[update]",type = 0)
	@AuditLimit(code = OperaCodeEnum.GROUP_EDIT)
	public BaseResponse<String> update(@RequestBody  XxlJobGroup xxlJobGroup) {
		return jobGroupService.update(xxlJobGroup);
	}

	@RequestMapping(value = "/remove",method = RequestMethod.POST)
	@ApiOperation(value = "job执行器删除", notes = "job执行器")
	@ActionOperateLog(eventName = "job执行器删除[remove]",type = 0)
	@AuditLimit(code = OperaCodeEnum.GROUP_DELETE)
	public BaseResponse<String> remove( @RequestBody  XxlJobGroup xxlJobGroup){
		   return jobGroupService.remove(xxlJobGroup);
	}

	@RequestMapping(value = "/userNamespaceList",method = RequestMethod.POST)
	@ApiOperation(value = "执行器对应的命名空间", notes = "job执行器")
	@ActionOperateLog(eventName = "执行器对应的命名空间[userNamespaceList]",type = 0)
	public BaseResponse<List<NamespaceResp>> userNamespaceList(){
		UserInfo userInfo=UserInfoContext.getUserInfo();
		boolean isAdmin=userService.isSuperAdmin(userInfo.getEmail());
		List<Namespace>  namespaceList=userService.getUserNamespaceList(userInfo);
		List<NamespaceResp> namespaceRespList=new ArrayList<>();
		if(!CollectionUtils.isEmpty(namespaceList)){
			for(Namespace namespace : namespaceList) {
				NamespaceResp resp=new NamespaceResp();
				BeanUtils.copyProperties(namespace,resp);
				resp.setNamespace(namespace.getNamespace()+":"+namespace.getService());
				namespaceRespList.add(resp);
			}
		}
		return BaseResponse.valueOfSuccess(namespaceRespList);
	}

}
