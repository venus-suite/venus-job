package io.suite.venus.job.admin.domain.dto;

import lombok.Data;

import java.util.List;

@Data
public class DashboardInfo {
    DashInfoRes dashInfo;
    List<ChartItemInfo> dayChartInfoList;
    int triggerCountRunningTotal = 0;
    int triggerCountSucTotal = 0;
    int triggerCountFailTotal = 0;
}
