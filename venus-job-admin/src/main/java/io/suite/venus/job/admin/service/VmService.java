package io.suite.venus.job.admin.service;

import io.suite.venus.job.admin.core.model.XxlJobServerInfo;
import io.suite.venus.job.admin.core.model.XxlJobServerInfoHis;
import io.suite.venus.job.admin.domain.dto.BasePageResp;
import io.suite.venus.job.admin.domain.dto.BaseReq;
import io.suite.venus.job.admin.domain.dto.UserInfo;
import io.suite.venus.job.admin.mapper.XxlJobServerInfoHisMapper;
import io.suite.venus.job.admin.mapper.XxlJobServerInfoMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xxl.job.core.vm.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class VmService {

    @Autowired
    private XxlJobServerInfoMapper xxlJobServerInfoMapper;

    @Autowired
    private XxlJobServerInfoHisMapper xxlJobServerInfoHisMapper;

    public boolean add(SystemInfo systemHardwareInfo) {
        XxlJobServerInfo info = new XxlJobServerInfo();
        info.setAppName(systemHardwareInfo.getAppName());
        info.setServerName(systemHardwareInfo.getServer());
        //cpu
        Cpu cpu = systemHardwareInfo.getCpu();
        info.setCpuNum(cpu.getCpuNum());
        info.setCpuFree(cpu.getFree());
        info.setCpuTotal(cpu.getTotal());
        info.setCpuSys(cpu.getSys());
        info.setCpuWait(cpu.getWait());
        info.setCpuUsed(cpu.getUsed());

        //Jvm
        Jvm jvm = systemHardwareInfo.getJvm();
        info.setJdkVersion(jvm.getVersion());
        info.setJvmMax(jvm.getMax());
        info.setJvmFree(jvm.getFree());
        info.setJvmTotal(jvm.getTotal());
        info.setJvmRunTime(jvm.getRunTime());
        info.setJvmUsage(jvm.getUsage());

        //mem
        Mem mem = systemHardwareInfo.getMem();
        info.setMemTotal(mem.getTotal());
        info.setMemFree(mem.getFree());
        info.setMemUsed(mem.getUsed());
        info.setMemUsage(mem.getUsage());
        //sys
        Sys sys = systemHardwareInfo.getSys();
        info.setSysComputerip(sys.getComputerIp());
        info.setSysOsarch(sys.getOsArch());
        info.setSysComputername(sys.getComputerName());
        info.setSysUserdir(sys.getUserDir());
        info.setSysOsname(sys.getOsName());
        //sys file
        TotalFile sysFile = systemHardwareInfo.getFile();
        info.setFileTotal(sysFile.getTotal());
        info.setFileFree(sysFile.getFree());
        info.setFileUsed(sysFile.getUsed());
        info.setFileUsage(sysFile.getUsage());
        info.setFileDirname(sysFile.getDirName());
        info.setFileSystypename(sysFile.getSysTypeName());
        info.setIsDeleted(0);
        info.setCreateTime(new Date());
        xxlJobServerInfoMapper.deleteByServerName(info.getServerName());
        XxlJobServerInfoHis his=new XxlJobServerInfoHis();
        BeanUtils.copyProperties(info,his);
        xxlJobServerInfoHisMapper.insertSelective(his);
        return xxlJobServerInfoMapper.insertSelective(info) > 0;
    }

    public BasePageResp<XxlJobServerInfo> getServerList(BaseReq req, UserInfo userInfo) {
        BasePageResp<XxlJobServerInfo> basePageResp = new BasePageResp();
        try {
            List<XxlJobServerInfo> resultList = new ArrayList<>();
            PageHelper.startPage(req.getCurrent(), req.getPageSize());
            List<XxlJobServerInfo> result = xxlJobServerInfoMapper.getServerList(req);
            PageInfo<XxlJobServerInfo> pageInfo = new PageInfo<>(result);
            basePageResp.setTotal(pageInfo.getTotal());
            basePageResp.setPages(pageInfo.getPages());
            basePageResp.setContentList(resultList);
        } finally {
            PageHelper.clearPage();
        }
        return basePageResp;
    }


    /**
     *
     */
    public List<XxlJobServerInfo> getServerListByAddressList(List<String> ipList) {
        List<XxlJobServerInfo> result = xxlJobServerInfoMapper.getServerListByAddressList(ipList);
        return result;
    }

}
