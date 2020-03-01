package smartx.publics.clustercompute.vo;

import java.io.Serializable;
import java.util.Map;

/**
 * 集群任务执行结果
 * @author teddyxu
 *
 */
public class ClusterComputeTaskResult implements Serializable{
	
	private static final long serialVersionUID = 4499700875446102815L;

	private int returnCode;
	
	private String exceptionDetail;//異常信息
	
	private Map<String,?> resultMap;
	public int getReturnCode() {
		return returnCode;
	}
	public void setReturnCode(int returnCode) {
		this.returnCode = returnCode;
	}
	public Map<String, ?> getResultMap() {
		return resultMap;
	}
	public void setResultMap(Map<String, ?> resultMap) {
		this.resultMap = resultMap;
	}
	public String getExceptionDetail() {
		return exceptionDetail;
	}
	public void setExceptionDetail(String exceptionDetail) {
		this.exceptionDetail = exceptionDetail;
	}
}
