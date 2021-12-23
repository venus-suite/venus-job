package io.suite.venus.job.admin.service;

import io.suite.venus.job.admin.core.util.LocalCacheUtil;
import io.suite.venus.job.admin.domain.dto.ChartInfoRes;
import io.suite.venus.job.admin.domain.dto.ChartItemInfo;
import io.suite.venus.job.admin.domain.dto.DashInfoRes;
import io.suite.venus.job.admin.domain.dto.DashboardInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class StatLogService {

    @Autowired
    private XxlJobService xxlJobService;

    public DashboardInfo getDashInfo() {
        Date startDate = new Date();
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, -7);
        startDate = c.getTime();
        Date endDate = new Date();
        // get cache
        String cacheKey = "p_job_dashboard";
        long t1 = System.currentTimeMillis();

        DashInfoRes res = xxlJobService.dashboardInfo();
        ChartInfoRes chartInfo = xxlJobService.chartInfo(startDate, endDate);
        DashboardInfo ds = new DashboardInfo();
        ds.setDashInfo(res);
        ds.setTriggerCountFailTotal(chartInfo.getTriggerCountFailTotal());
        ds.setTriggerCountRunningTotal(chartInfo.getTriggerCountRunningTotal());
        ds.setTriggerCountSucTotal(chartInfo.getTriggerCountSucTotal());
        List<String> dayList = chartInfo.getTriggerDayList();
        ds.setDayChartInfoList(new ArrayList<>());
        for (int i = 0; i < dayList.size(); i++) {
            ChartItemInfo item = new ChartItemInfo();
            item.setCategory("成功数");
            item.setLabel(dayList.get(i));
            item.setValue(chartInfo.getTriggerDayCountSucList().get(i));
            ds.getDayChartInfoList().add(item);
        }
        for (int i = 0; i < dayList.size(); i++) {
            ChartItemInfo item = new ChartItemInfo();
            item.setCategory("失败数");
            item.setLabel(dayList.get(i));
            item.setValue(chartInfo.getTriggerDayCountFailList().get(i));
            ds.getDayChartInfoList().add(item);
        }

        for (int i = 0; i < dayList.size(); i++) {
            ChartItemInfo item = new ChartItemInfo();
            item.setCategory("运行中");
            item.setLabel(dayList.get(i));
            item.setValue(chartInfo.getTriggerDayCountRunningList().get(i));
            ds.getDayChartInfoList().add(item);
        }
       // LocalCacheUtil.set(cacheKey, ds, 3600);
        return ds;
    }
}
