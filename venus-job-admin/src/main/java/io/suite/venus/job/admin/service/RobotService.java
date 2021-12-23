package io.suite.venus.job.admin.service;

import java.util.List;

public interface RobotService {
    boolean sendRobot(String url,String msg, List<String> usersList);
}
