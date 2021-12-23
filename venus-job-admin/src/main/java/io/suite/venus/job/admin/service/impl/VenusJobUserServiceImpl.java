package io.suite.venus.job.admin.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import io.suite.venus.job.admin.common.constant.CommonContant;
import io.suite.venus.job.admin.common.constant.StatusConstant;
import io.suite.venus.job.admin.common.exception.BizBaseException;
import io.suite.venus.job.admin.common.exception.ExceptionType;
import io.suite.venus.job.admin.common.exception.ServiceException;
import io.suite.venus.job.admin.common.util.AESUtil;
import io.suite.venus.job.admin.core.model.UserAdmin;
import io.suite.venus.job.admin.core.model.VenusJobQrtzUser;
import io.suite.venus.job.admin.domain.dto.BasePageResp;
import io.suite.venus.job.admin.domain.dto.VenusJobUserQueryReq;
import io.suite.venus.job.admin.domain.dto.VenusJobUserReq;
import io.suite.venus.job.admin.domain.dto.VenusJobUserResp;
import io.suite.venus.job.admin.mapper.AdminMapper;
import io.suite.venus.job.admin.mapper.VenusJobQrtzUserMapper;
import io.suite.venus.job.admin.service.UserService;
import io.suite.venus.job.admin.service.VenusJobUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class VenusJobUserServiceImpl implements VenusJobUserService {
    @Autowired
    private VenusJobQrtzUserMapper userMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private AdminMapper adminMapper;

    /**
     * 新增用户表
     * @param reqData
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addVenusJobUser(VenusJobUserReq reqData) {
        VenusJobQrtzUser entity = new VenusJobQrtzUser();
        BeanUtils.copyProperties(reqData, entity,"id");
        String encryptPass = AESUtil.encrypt(reqData.getPassword(), CommonContant.PASSWORD_SALT);
        entity.setPassword(encryptPass);

        entity.setOperatorId(userService.getCurrentUserId());
        entity.setOperator(userService.getCurrentUsername());
        userMapper.insertSelective(entity);

        //是超管，要在admin表中插入一条记录
        if (reqData.getIsSuper() == 1){
            UserAdmin userAdmin = new UserAdmin();
            userAdmin.setEmail(entity.getEmail());
            userAdmin.setIsDeleted(StatusConstant.STATUS_INVALID);
            userAdmin.setCreateTime(LocalDateTime.now());
            adminMapper.insertSelective(userAdmin);
        }
    }

    /**
     * 更新用户表
     * @param reqData
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateVenusJobUser(VenusJobUserReq reqData) {
        if (reqData.getId() == null){
            throw new ServiceException(ExceptionType.UPDATE_USER_ID_NULL);
        }

        VenusJobQrtzUser entity = userMapper.selectByPrimaryKey(reqData.getId());

        if (entity == null){
            log.error("修改的用户中ID为{}数据不存在", reqData.getId());
            throw new ServiceException(ExceptionType.USER_DATA_NULL);
        }
        VenusJobQrtzUser updateEntity = new VenusJobQrtzUser();
        BeanUtils.copyProperties(reqData, updateEntity);
        if (reqData.getPassword() != null){
            String encryptPass = AESUtil.encrypt(reqData.getPassword(), CommonContant.PASSWORD_SALT);
            updateEntity.setPassword(encryptPass);
        }
        updateEntity.setOperatorId(userService.getCurrentUserId());
        updateEntity.setOperator(userService.getCurrentUsername());

        userMapper.updateByPrimaryKeySelective(updateEntity);

        //管理员字段有修改
        if (!entity.getIsSuper().equals(reqData.getIsSuper())) {
            //如果原来是管理员，修改成非管理员，则需要从admin表中删除记录
            if (entity.getIsSuper() == 1) {
                UserAdmin userAdmin = new UserAdmin();
                userAdmin.setEmail(entity.getEmail());
                adminMapper.delete(userAdmin);
            }
            //原本非管理员，改成管理员，在admin中插入记录
            if (entity.getIsSuper() == 0){
                UserAdmin userAdmin = new UserAdmin();
                userAdmin.setEmail(entity.getEmail());
                userAdmin.setIsDeleted(StatusConstant.STATUS_INVALID);
                userAdmin.setCreateTime(LocalDateTime.now());
                adminMapper.insertSelective(userAdmin);
            }
        }
    }

    /**
     * 删除用户表
     * @param reqData
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteVenusJobUser(VenusJobUserReq reqData) {
        if (reqData.getId() == null){
            throw new ServiceException(ExceptionType.UPDATE_USER_ID_NULL);
        }

        VenusJobQrtzUser entity = userMapper.selectByPrimaryKey(reqData.getId());

        if (entity == null){
            log.error("删除的用户中ID为{}数据不存在", reqData.getId());
            return;
        }
        VenusJobQrtzUser updateEntity = new VenusJobQrtzUser();
        updateEntity.setId(reqData.getId());
        userMapper.delete(updateEntity);
        //如果是管理员，删除关联的admin的记录
        UserAdmin userAdmin = new UserAdmin();
        userAdmin.setEmail(entity.getEmail());
        adminMapper.delete(userAdmin);
    }

    /**
     * 分页查询用户表
     */
    @Override
    public BasePageResp<VenusJobUserResp> queryVenusJobUserPage(VenusJobUserQueryReq reqData) {

        PageHelper.startPage(reqData.getCurrent(),reqData.getPageSize());
        List<VenusJobQrtzUser> entityList = userMapper.getUserInfoPageList(reqData);

        List<VenusJobUserResp> respList = Lists.newArrayList();
        entityList.forEach(entity -> {
            VenusJobUserResp resp = new VenusJobUserResp();
            BeanUtils.copyProperties(entity, resp);
            respList.add(resp);
        });

        PageInfo<VenusJobQrtzUser> pageInfo=new PageInfo<>(entityList);
        BasePageResp<VenusJobUserResp> basePageInfo=new BasePageResp<>();
        BeanUtils.copyProperties(pageInfo, basePageInfo);
        basePageInfo.setContentList(respList);

        return basePageInfo;
    }
}
