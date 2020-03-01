/**
 * 
 */
package smartx.bam.bs.workspace;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import smartx.bam.bs.common.MetadataTempletManager;
import smartx.bam.bs.dashboard.DashboardManager;
import smartx.bam.bs.dashboardobj.DashboardObjManager;
import smartx.framework.common.bs.CommDMO;
import smartx.framework.common.vo.HashVO;
import smartx.publics.cep.bs.service.SmartXCEPService;
import smartx.publics.cep.vo.CEPConst;

/**
 * @author sky 建模工作集管理
 */
public class WorkspaceManager {

	private Logger logger = Logger.getLogger(this.getClass());

	private SmartXCEPService sxCepservice;
	
	private DashboardObjManager dbom;
	
	private DashboardManager dbm;
	
	private MetadataTempletManager mtm = new MetadataTempletManager();

	public static final String BUSINESSVIEW = "bv";

	public static final String QUERYVIEW = "qv";

	public static final String DASHBOARD = "db";

	public static final String DASHBOARDOBJECT = "do";
	
	public WorkspaceManager(SmartXCEPService sxCepservice,DashboardObjManager dbom,DashboardManager dbm){
		this.sxCepservice = sxCepservice;
		this.dbom = dbom;
		this.dbm = dbm;
	}

	/**
	 * 所有工作集的元数据
	 * 
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> getAllWorkspaceMetadata(String owner) throws Exception {
		logger.debug("查询所有工作集的元数据");
		CommDMO dmo = new CommDMO();
		Map<String, String> results = new HashMap<String, String>();
		try {
			HashVO[] vos = dmo.getHashVoArrayByDS(null,
					"select b.code,b.metadata from bam_workspace b where b.owner=?",owner);
			for (HashVO vo : vos) {
				results.put(vo.getStringValue("code"),
						vo.getStringValue("metadata"));
			}
			return results;
		} catch (Exception e) {
			logger.error("", e);
			throw e;
		} finally {
			dmo.releaseContext(null);
		}
	}

	/**
	 * 更新工作集
	 * 
	 * @param dataValue
	 * @throws Exception
	 */
	public void updateWorkspace(Map<String, Object> dataValue) throws Exception {
		logger.debug("更新工作集["+dataValue.get("NAME")+"]");
		CommDMO dmo = new CommDMO();
		boolean addFlag = true;
		String sql = "select id from bam_workspace where code=?";
		try {
			HashVO[] vos = dmo.getHashVoArrayByDS(null, sql,dataValue.get("CODE"));
			if(vos.length > 0){
				addFlag = false;
				sql = "update bam_workspace set lastupdatetime=sysdate where code=?";
				dmo.executeUpdateByDS(null, sql,dataValue.get("CODE"));
				dmo.executeUpdateClobByDS(null, "metadata",
						"bam_workspace",
						"code='"+dataValue.get("CODE")+"'", dataValue.get("METADATA") + "");
			}else{
				sql = "insert into bam_workspace values(s_bam_workspace.nextval,?,?,empty_clob(),?,sysdate,sysdate)";
				dmo.executeUpdateByDS(null, sql, dataValue.get("NAME"),
						dataValue.get("CODE"), dataValue.get("OWNER"));
				if (dataValue.get("METADATA") != null
						&& !"".equals(dataValue.get("METADATA")))
					dmo.executeUpdateClobByDS(null, "metadata", "bam_workspace",
							"code='" + dataValue.get("CODE") + "'",
							dataValue.get("METADATA") + "");
			}
			dmo.commit(null);
		} catch (Exception e) {
			dmo.rollback(null);
			logger.error("", e);
			if(addFlag)
				delAllRelateTables(dataValue.get("METADATA")+"");
			throw e;
		} finally {
			dmo.releaseContext(null);
		}
	}
	
	/**
	 * 删除工作集
	 * @param dataValue
	 * @throws Exception
	 */
	public void delWorkspace(String code,String metadata) throws Exception{
		CommDMO dmo = new CommDMO();
		try{
			String sql = "delete from bam_workspace where code=?";
			dmo.executeUpdateByDS(null, sql,code);
			delAllRelateTables(metadata);
		}catch (Exception e) {
			logger.error("", e);
			dmo.rollback(null);
			throw e;
		} finally {
			dmo.releaseContext(null);
		}
	}

	/**
	 * 删除工作集所有的关联表
	 * 
	 * @param metadata
	 * @throws Exception
	 */
	private void delAllRelateTables(String metadata) throws Exception {
		logger.debug("删除工作集中所有关联表");
		Document doc = DocumentHelper.parseText(metadata);
		Element root = doc.getRootElement();
		@SuppressWarnings("rawtypes")
		List folderElements = root.elements("node");
		for (int i = 0; i < folderElements.size(); i++) {
			Element folder = (Element) folderElements.get(i);
			@SuppressWarnings("rawtypes")
			List tableElements = folder.elements("node");
			for(int j = 0;j<tableElements.size();j++){
				Element table = (Element)tableElements.get(j);
				delRalateTableByType(table.attributeValue("type"),table.attributeValue("code"),table.attributeValue("refcode"));
			}
		}
	}

	/**
	 * 根据不同类型删除相关的关联表
	 * 
	 * @param type
	 *            类型
	 * @param code
	 *            编码
	 * @throws Exception
	 */
	public void delRalateTableByType(String type, String code,String name) throws Exception {
		CommDMO dmo = new CommDMO();
		String sql = "";
		try {
			if (BUSINESSVIEW.equals(type)) {
				sxCepservice.undeployStreamModule(CEPConst.DEFAULTPROVIDERNAME_CEP, name);
				sql = "delete from bam_businessview where code=?";
			}else if(QUERYVIEW.equals(type)){
				sql = "delete from bam_queryview where code=?";
			}else if(DASHBOARD.equals(type)){
				mtm.deleteMetadataTemplet(name);
				dbm.delDashboardAuthority(code);
				sql = "delete from bam_dashboard where code=?";
			}else if(DASHBOARDOBJECT.equals(type)){
				mtm.deleteMetadataTemplet(name);
				dbom.delDboRelateAccessFilter(code);
				sql = "delete from bam_dashboardobject where code=?";
			}
			dmo.executeUpdateByDS(null, sql, code);
			dmo.commit(null);
		} catch (Exception e) {
			dmo.rollback(null);
			logger.error("", e);
			throw e;
		} finally {
			dmo.releaseContext(null);
		}
	}
}
