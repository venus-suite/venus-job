package io.suite.venus.job.admin.service;


import io.suite.venus.job.admin.core.model.XxlJobInfo;
import io.suite.venus.job.admin.domain.dto.ChartInfoRes;
import io.suite.venus.job.admin.domain.dto.DashInfoRes;
import io.suite.venus.job.admin.common.inout.BaseMngPageResp;
import com.xxl.job.core.biz.model.ReturnT;

import java.util.Date;

/**
 * core job action for xxl-job
 *
 * @author xuxueli 2016-5-28 15:30:33
 */
public interface XxlJobService {

    /**
     * page list
     *
     * @param start
     * @param length
     * @param jobGroup
     * @param jobDesc
     * @param executorHandler
     * @param filterTime
     * @return
     */
    BaseMngPageResp<XxlJobInfo> pageList(int start, int length, int jobGroup, String jobDesc, String email,String executorHandler,
                                         String filterTime,String jobStatus,
                                         String author,String jobCron,int jobId
                                         );

    /**
     * add job, default quartz stop
     *
     * @param jobInfo
     * @return
     */
    ReturnT<String> add(XxlJobInfo jobInfo);

    /**
     * update job, update quartz-cron if started
     *
     * @param jobInfo
     * @return
     */
    ReturnT<String> update(XxlJobInfo jobInfo);

    /**
     * remove job, unbind quartz
     *
     * @param jobInfo
     * @return
     */
    ReturnT<String> remove(XxlJobInfo jobInfo);

    /**
     * start job, bind quartz
     *
     * @param jobInfo
     * @return
     */
    ReturnT<String> start(XxlJobInfo jobInfo);

    /**
     * stop job, unbind quartz
     *
     * @param jobInfo
     * @return
     */
    ReturnT<String> stop(XxlJobInfo jobInfo);

    /**
     * stop job, unbind quartz
     *
     * @param jobInfo
     * @return
     */
    ReturnT<String> pause(XxlJobInfo jobInfo);

    /**
     * dashboard info
     *
     * @return
     */
    DashInfoRes dashboardInfo();

    /**
     * chart info
     *
     * @param startDate
     * @param endDate
     * @return
     */
    ChartInfoRes chartInfo(Date startDate, Date endDate);


    ReturnT<String> triggerJob(XxlJobInfo jobInfo);




    /**
     * 清空7天后的日志
     * @param expireCalendar
     * @return
     */
    Integer clearLogByDate(Date expireCalendar);


    Integer clearServerLogByDay(Date expireCalendar);


}
