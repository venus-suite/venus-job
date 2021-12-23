package io.suite.venus.job.admin.domain.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class AuthToken implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -4338504908038320520L;
	/**
     *
     */
    private Long userId;
    private Long appId;
    private String name;
    private List<String> permissionList;
    private String email;
    private Long timestamp;
    private  boolean isAdmin;
}