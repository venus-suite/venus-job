package io.suite.venus.job.admin.core.model;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;


@Data
@Entity
@Table(name = "xxl_job_qrtz_operate_log")
public class OperateLog implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6084978420776410994L;

	@KeySql(useGeneratedKeys = true)
	@Id
	@Column(name = "id")
	private Integer id;

	@Column(name = "user_email")
	private String userEmail;
	
	@Column(name = "username")
	private String username;
	

	@Column(name = "ip")
	private String ip;
	
	@Column(name = "use_time")
	private Integer useTime;

	@Column(name = "url")
	private String url;
	
	
	@Column(name = "event_name")
	private String eventName;


	@Column(name = "params")
	private String params;
	
	
	@Column(name = "headers")
	private String headers;

	@Column(name = "response_body")
	private String responseBody;
	

	@Column(name = "create_time")
	private Date createTime;

}
