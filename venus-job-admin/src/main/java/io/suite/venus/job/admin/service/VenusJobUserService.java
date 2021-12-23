package io.suite.venus.job.admin.service;

import io.suite.venus.job.admin.domain.dto.BasePageResp;
import io.suite.venus.job.admin.domain.dto.VenusJobUserQueryReq;
import io.suite.venus.job.admin.domain.dto.VenusJobUserReq;
import io.suite.venus.job.admin.domain.dto.VenusJobUserResp;

public interface VenusJobUserService {

    void addVenusJobUser(VenusJobUserReq reqData);
    void updateVenusJobUser(VenusJobUserReq reqData);
    void deleteVenusJobUser(VenusJobUserReq reqData);
    BasePageResp<VenusJobUserResp> queryVenusJobUserPage(VenusJobUserQueryReq reqData);
}
