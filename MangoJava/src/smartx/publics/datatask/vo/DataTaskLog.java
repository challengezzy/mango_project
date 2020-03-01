package smartx.publics.datatask.vo;

import java.util.Date;


/**
 *@author zzy
 *@date Sep 9, 2011
 **/
public class DataTaskLog {
	
	private String dataTaskId;
	
	private Date logTime;
	
	private String message;
	
	private String taskDetail;
	
	private String taskName;

	public String getDataTaskId() {
		return dataTaskId;
	}

	public void setDataTaskId(String dataTaskId) {
		this.dataTaskId = dataTaskId;
	}

	public Date getLogTime() {
		return logTime;
	}

	public void setLogTime(Date logTime) {
		this.logTime = logTime;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getTaskDetail() {
		return taskDetail;
	}

	public void setTaskDetail(String taskDetail) {
		this.taskDetail = taskDetail;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

}
