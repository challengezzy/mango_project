/**
 * 
 */

package smartx.bam.bs.bvmanager;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import smartx.framework.common.bs.CommDMO;
import smartx.framework.common.utils.StringUtil;
import smartx.framework.common.vo.HashVO;
import smartx.framework.metadata.vo.ComBoxItemVO;
import smartx.framework.metadata.vo.RefItemVO;
import smartx.publics.cep.bs.StreamModuleDeploymentManager;
import smartx.publics.cep.vo.CEPConst;

import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;

/**
 * @author sky Description 业务视图状态控制
 */
public class BusinessViewManager {
	private Logger logger = Logger.getLogger(this.getClass());

	public final static String MODULENAME_PREFIX = "module_";

	private StreamModuleDeploymentManager streamModuleDeploymentManager = new StreamModuleDeploymentManager();

	public static final String INSERT_TYPE = "insert";

	public static final String UPDATE_TYPE = "update";

	public static final String BVTABLE_PREFIX = "bv_";

	// 键为事件window名称 值为视图编码
	public static Map<String, String> bvTableMap;

	static {
		bvTableMap = new HashMap<String, String>();
	}

	/**
	 * 启动业务视图
	 * 
	 * @param code
	 *            视图编码
	 */
	public void startBusinessView(String code) throws Exception {
		logger.debug("启动业务视图[" + code + "]");
		CommDMO dmo = new CommDMO();
		StringBuilder sql = new StringBuilder();
		sql.append("select p.name,p.providername from pub_cep_streammodule p,bam_businessview b where b.streammodulename = p.name and p.name='");
		sql.append(code).append("'");
		HashVO[] vos = null;
		try {
			logger.debug("通过业务视图编码查询流模块...." + sql.toString());
			vos = dmo.getHashVoArrayByDS(null, sql.toString());
			if (vos.length > 0) {
				for (HashVO vo : vos) {
					// 启动模块
					streamModuleDeploymentManager.startStreamModule(
							vo.getStringValue("providername"),
							vo.getStringValue("name"));
				}
			} else
				throw new Exception("没有找到相关流模块");
		} catch (Exception e) {
			throw e;
		} finally {
			dmo.releaseContext(null);
		}
	}

	/**
	 * 停止业务视图
	 * 
	 * @param code
	 *            视图编码
	 */
	public void stopBusinessView(String code) throws Exception {
		logger.debug("停止业务视图[" + code + "]");
		CommDMO dmo = new CommDMO();
		StringBuilder sql = new StringBuilder();
		sql.append("select p.name,p.providername from pub_cep_streammodule p,bam_businessview b where b.streammodulename = p.name and b.code='");
		sql.append(code).append("'");
		HashVO[] vos = null;
		try {
			logger.debug("通过业务视图编码查询流模块...." + sql.toString());
			vos = dmo.getHashVoArrayByDS(null, sql.toString());
			if (vos.length > 0) {
				for (HashVO vo : vos) {
					// 停止模块
					streamModuleDeploymentManager.stopStreamModule(
							vo.getStringValue("providername"),
							vo.getStringValue("name"));
				}
			} else
				throw new Exception("没有找到相关流模块");
		} catch (Exception e) {
			throw e;
		} finally {
			dmo.releaseContext(null);
		}
	}

	/**
	 * 保存并发布模块
	 * 
	 * @param providerName
	 * @param viewName
	 * @param source
	 * @param operator
	 * @throws Exception
	 */
	public void deployStreamModule(String providerName, String viewName,
			String source, String operator, String type) throws Exception {
		String moduleName;
		if (BusinessViewManager.INSERT_TYPE.equals(type))
			moduleName = MODULENAME_PREFIX.concat(viewName);
		else
			moduleName = viewName;
		streamModuleDeploymentManager.deployStreamModule(providerName,
				moduleName, source, operator);

	}

	/**
	 * 重新发布流模块
	 * 
	 * @param providerName
	 * @param moduleName
	 * @throws Exception
	 */
	public void redeployStreamModule(String providerName, String moduleName)
			throws Exception {
		EPServiceProvider epService = EPServiceProviderManager
				.getProvider(providerName);
		Map<String, Object> variables = epService.getEPRuntime()
				.getVariableValueAll();
		String code = StringUtils.replace(moduleName, MODULENAME_PREFIX, "");
		for (Entry<String, Object> entry : variables.entrySet()) {
			if (entry.getKey().indexOf(code) >= 0)
				epService.getEPAdministrator().getConfiguration()
						.removeVariable(entry.getKey(), true);
		}

		streamModuleDeploymentManager.redeployStreamModule(providerName,
				moduleName);
	}

	/**
	 * 判断业务视图是否存在
	 * 
	 * @param code
	 * @throws Exception
	 */
	public boolean isExistBv(String code) throws Exception {
		logger.debug("是否存在业务视图[" + code + "]");
		CommDMO dmo = new CommDMO();
		String sql = "select b.id from bam_businessview b where b.code =?";
		try {
			HashVO[] vos = dmo.getHashVoArrayByDS(null, sql, code);
			if (vos.length > 0)
				return true;
		} catch (Exception e) {
			throw e;
		} finally {
			dmo.releaseContext(null);
		}
		return false;
	}

	/**
	 * 复制业务视图
	 * 
	 * @param dataValue
	 * @throws Exception
	 */
	public void copyBusinessView(Map<String, Object> dataValue)
			throws Exception {
		String code = dataValue.get("CODE") + "";
		if (isExistBv(code)) {
			throw new Exception("该业务视图已存在！");
		} else {
			String templeteCode = (String) dataValue
					.get("FORMSERVICE_TEMPLETCODE");
			if (!StringUtil.isEmpty(templeteCode)
					&& templeteCode.trim().equalsIgnoreCase(
							"T_BAM_BUSINESSVIEW")) {
				CommDMO dmo = new CommDMO();
				try {
					dataValue.put("STREAMMODULENAME",
							BusinessViewManager.MODULENAME_PREFIX.concat(code));
					deployStreamModule(
							CEPConst.DEFAULTPROVIDERNAME_CEP,
							code,
							dataValue.get("SOURCE") == null ? "" : dataValue
									.get("SOURCE") + "",
							dataValue.get("CREATOR") + "",
							BusinessViewManager.INSERT_TYPE);
					ComBoxItemVO isPersistViewdataVo = (ComBoxItemVO) dataValue
							.get("ISPERSISTVIEWDATA");
					if (Integer.parseInt(isPersistViewdataVo.getId()) == 1)
						BusinessViewManager.bvTableMap.put(
								dataValue.get("STREAMWINDOWNAME") + "",
								dataValue.get("CODE") + "");
					RefItemVO folderVo = (RefItemVO) dataValue.get("FOLDERID");
					ComBoxItemVO typeVo = (ComBoxItemVO) dataValue.get("TYPE");

					String sql = "insert into bam_businessview(id,name,code,description,ispersistviewdata,streamname"
							+ ",streamwindowname,streammodulename,type,metadata,folderid) values(s_bam_businessview.nextval,?,?,?,?,?,?,?,?,empty_clob(),?)";
					dmo.executeUpdateByDS(null, sql, dataValue.get("NAME"),
							dataValue.get("CODE"),
							dataValue.get("DESCRIPTION"),
							isPersistViewdataVo.getId(),
							dataValue.get("STREAMNAME"),
							dataValue.get("STREAMWINDOWNAME"),
							dataValue.get("STREAMMODULENAME"), typeVo.getId(),
							folderVo.getId());
					if (dataValue.get("METADATA") != null)
						dmo.executeUpdateClobByDS(null, "metadata",
								"bam_businessview", "code='".concat(code)
										.concat("'"), dataValue.get("METADATA")
										+ "");
					dmo.commit(null);
				} catch (Exception e) {
					dmo.rollback(null);
					streamModuleDeploymentManager.undeployStreamModule(
							CEPConst.DEFAULTPROVIDERNAME_CEP,
							dataValue.get("STREAMMODULENAME") + "");
					throw e;
				} finally {
					dmo.releaseContext(null);
				}
			}
		}
	}
}
