/**
 * 
 */
package smartx.bam.vo;

import smartx.framework.common.vo.DMOConst;
import smartx.publics.cep.bs.StreamModuleDeploymentManager;
import smartx.publics.cep.vo.CEPConst;

/**
 * @author caohenghui
 *
 */
public class DatabaseConst extends DMOConst{
	
	public static final String DATASOURCE_DEFAULT = "datasource_default";
	public static final String DATASOURCE_USERMGMT = "datasource_usermgmt";
	public static final String DATASOURCE_SPDD = "datasource_spdd";
	public static final String DATASOURCE_AVE = "datasource_ave";
	public static final String DATASOURCE_DQC = "datasource_dqc";
	public static final String DATASOURCE_AMS = "datasource_ams";
	public static final String DATASOURCE_REPORT = "datasource_report";
	public static final String DS_SMARTDQ = "datasource_smartdq";//DQ数据源名称 
	public static final String DATASOURCE_CEP = StreamModuleDeploymentManager.JDBC_DATASOURCE_PREFIX+CEPConst.DEFAULTPROVIDERNAME_CEP;

}
