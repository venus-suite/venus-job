package io.suite.venus.job.admin.domain.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ChartInfoRes {

    List<String> triggerDayList = new ArrayList<String>();
    List<Integer> triggerDayCountRunningList = new ArrayList<Integer>();
    List<Integer> triggerDayCountSucList = new ArrayList<Integer>();
    List<Integer> triggerDayCountFailList = new ArrayList<Integer>();
    int triggerCountRunningTotal = 0;
    int triggerCountSucTotal = 0;
    int triggerCountFailTotal = 0;

}
