/**
 * 
 */
package smartx.bam.bs.dashboardobj;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import smartx.bam.bs.bvmanager.BusinessViewManager;
import smartx.bam.bs.common.MetadataTempletManager;
import smartx.bam.bs.entitymodel.EntityRuleRealTimeStatisticsService;
import smartx.bam.vo.SysConst;
import smartx.framework.common.bs.CommDMO;
import smartx.framework.common.vo.HashVO;
import smartx.framework.common.vo.SimpleHashVO;
import smartx.framework.metadata.vo.ComBoxItemVO;
import smartx.framework.metadata.vo.RefItemVO;
import smartx.publics.cep.bs.StreamModuleDeploymentManager;
import smartx.publics.form.bs.service.SmartXFormService;
import smartx.publics.metadata.bs.service.SmartXMetadataTempletService;
import smartx.publics.metadata.vo.MetadataTemplet;
import smartx.system.common.constant.CommonSysConst;

/**
 * @author sky Description
 */
public class DashboardObjManager {
	private Logger logger = Logger.getLogger(this.getClass());

	private MetadataTempletManager mtm = new MetadataTempletManager();

	private SmartXMetadataTempletService metadataService = new SmartXMetadataTempletService();

	private SmartXFormService smartXFormService = new SmartXFormService();
	
	private EntityRuleRealTimeStatisticsService errtss = new EntityRuleRealTimeStatisticsService();

	private boolean isHisBvData = false;
	
	private String datasource = null;

	/**
	 * 根据code获得dashboardobject的mtcode
	 * 
	 * @param code
	 */
	public Map<String, Object> getMtCodeByCode(String code, String config)
			throws Exception {
		logger.debug("通过仪表盘对象[" + code + "]查询mtcode");
		if (code == null || "".equals(code))
			throw new Exception("仪表盘对象编码不能为空!");
		CommDMO dmo = new CommDMO();
		String sql = "select a.condition,do.mtcode,do.type from bam_accessfilter a,bam_r_accessfilter_do r,bam_dashboardobject do "
			+ " where a.id(+) =r.accessfilterid and r.dashboardobjectid(+) =do.id and do.code =?";
		HashVO[] vos;
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			vos = dmo.getHashVoArrayByDS(null, sql, code);
			if (vos.length > 0) {
				result.put("code", vos[0].getStringValue("code"));
				result.put("mtcode", vos[0].getStringValue("mtcode"));
				result.put("type", vos[0].getStringValue("type"));
				result.put("config", config);
				
				String accessFilter = "";
				//返回仪表盘对象过滤条件,多个过滤条件进行拼装
				for(int i=0;i<vos.length; i++){
					if(vos[i].getStringValue("condition") != null)
						accessFilter = accessFilter + " and " + vos[i].getStringValue("condition");
				}
				if(accessFilter.length() > 4)
					accessFilter = accessFilter.substring(5, accessFilter.length());
				
				result.put("accessFilter", accessFilter);
			}
		} catch (Exception e) {
			logger.error(e);
			throw e;
		} finally {
			dmo.releaseContext(null);
		}
		return result;
	}

	/**
	 * 通过元数据编码获取图表数据
	 * 
	 * @param mtcode
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Map<String, Object>[]> getChartData(String mtcode,
			String providerName, String chartType, String id, String filter,String accessFilter)
			throws Exception {
		Map<String, Map<String, Object>[]> result = new HashMap<String, Map<String, Object>[]>();
		MetadataTemplet metadataTemplet = metadataService
				.findMetadataTemplet(mtcode);
		Document doc = DocumentHelper.parseText(metadataTemplet.getContent());
		datasource = doc.getRootElement().elementText("datasource");
		String epl = parseChartXmlToEpl(doc, filter);
		String realSql = epl;
		if(accessFilter != null && !"".equals(accessFilter.trim()) )
			realSql = "select * from (" + epl + ") where " + accessFilter;
		
		logger.debug("查询图表数据SQL[" + realSql + "]");
		Map<String, Object>[] map;
		SimpleHashVO[] shvs = smartXFormService.getSimpleHashVoArrayUnlimitedByDS(
						isHisBvData ? null
								: (datasource == null ? StreamModuleDeploymentManager.JDBC_DATASOURCE_PREFIX
										.concat(providerName) : datasource),
										realSql);
		map = new Map[shvs.length];
		for (int i = 0; i < shvs.length; i++) {
			map[i] = shvs[i].getDataMap();
		}
		result.put(id, map);
		return result;

	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map<String, Map<String, Object>[]> getChartDataByMtContent(String mtContent,
			String providerName, String chartType, String id, String filter,String accessFilter)
			throws Exception {
		Map<String, Map<String, Object>[]> result = new HashMap<String, Map<String, Object>[]>();
		Document doc = DocumentHelper.parseText(mtContent);
		datasource = doc.getRootElement().elementText("datasource");
		String epl = parseChartXmlToEpl(doc, filter);
		String realSql = epl;
		if(accessFilter != null && !"".equals(accessFilter.trim()) )
			realSql = "select * from (" + epl + ") where " + accessFilter;
		
		logger.debug("查询图表数据SQL[" + realSql + "]");
		Map[] map;
		SimpleHashVO[] shvs = smartXFormService
				.getSimpleHashVoArrayByDS(
						isHisBvData ? null
								: (datasource == null ? StreamModuleDeploymentManager.JDBC_DATASOURCE_PREFIX
										.concat(providerName) : datasource),
										realSql);
		map = new HashMap[shvs.length];
		for (int i = 0; i < shvs.length; i++) {
			map[i] = shvs[i].getDataMap();
		}
		result.put(id, map);
		return result;

	}

	/**
	 * 解析图表元数据转换为EPL语句
	 * 
	 * @param doc
	 * @return
	 */
	public String parseChartXmlToEpl(Document doc, String filter) throws Exception {
		isHisBvData = false;
		// 获取表名
		StringBuilder epl = new StringBuilder("select ");

		Element root = doc.getRootElement();
		String groupbycolumn = root.elementText("groupbycolumn");
		if (groupbycolumn != null && !"".equals(groupbycolumn)) {
			epl.append(groupbycolumn);
		} else
			epl.append("*");
		String tablename = "";
		if (root.elementText("viewname") == null) {
			Element datasourceEle = root.element("datasource");
			if (datasourceEle.attributeValue("queryViewCode") != null) { // 查询视图
				String viewCode = datasourceEle.attributeValue("queryViewCode");
				CommDMO dmo = new CommDMO();
				try{
					String sql = "select sql,datasourcename from bam_queryview where code = '"
							+ viewCode + "'";
					HashVO[] vos = dmo.getHashVoArrayByDS(null, sql);
					if (vos.length > 0) {
						tablename = "(" + vos[0].getStringValue("sql") + ")";
						datasource = vos[0].getStringValue("datasourcename"); 
					} else 
						throw new Exception("未找到查询视图[" + viewCode + "]对应的sql语句");
				}catch(Exception e){
					throw e;
				}finally{
					dmo.releaseContext(null);
				}
			}else if (datasourceEle.attributeValue("tablename") != null){// 查询表的情况
				tablename = datasourceEle.attributeValue("tablename");
			}else if(datasourceEle.attributeValue("type") != null && datasourceEle.attributeValue("type").equalsIgnoreCase("entity")){
				String modelCode = datasourceEle.attributeValue("modelCode");
				String entityCode = datasourceEle.attributeValue("entityCode");
				if(errtss.initEntitiesInfo(errtss.getEntityModelContent(modelCode), modelCode)){
					tablename = "("+errtss.getSQL(modelCode, entityCode, null)+")";
				}
			}else{
				tablename = "(" + datasourceEle.attributeValue("sql") + ")";// 自定义SQL
			}
				
		} else {
			tablename = root.elementText("viewname");
			Element view = root.element("viewname");
			if ("true".equals(view.attributeValue("isHisData"))) {
				tablename = BusinessViewManager.BVTABLE_PREFIX + tablename;
				isHisBvData = true;
			}
		}
		epl.append(" from ").append(tablename).append(" ");
		if (filter != null && !filter.trim().equals("")){
			//if(filter.startsWith("where")||filter.startsWith("WHERE")){
				epl.append(filter);
			//}else{
			//	epl.append(" where "+filter);
			//}
		}
			

		String eplSql = "";
		String rowsLimit = root.elementText("rowsLimit");
		if (root.elementText("viewname") == null && rowsLimit != null
				&& !"".equals(rowsLimit)) {
			eplSql = "select * from (" + epl.toString() + ") where rownum<="
					+ root.elementText("rowsLimit");
		} else {
			eplSql = epl.toString();
		}

		return eplSql;
	}

	public String getMtTypeByChartType(int type) {
		String mttype = "0";
		switch (type) {
		case SysConst.PIECHART:
			mttype = CommonSysConst.MT_TYPE_PIECHART;
			break;
		case SysConst.COMBINATIONCHART:
			mttype = CommonSysConst.MT_TYPE_COMBICHART;
			break;
		case SysConst.DISTRIBUTIONCHART:
			mttype = CommonSysConst.MT_TYPE_DISTRIBUTIONCHART;
			break;
		case SysConst.GEOGRAPHYCHART:
			mttype = CommonSysConst.MT_TYPE_GEOGRAPHYCHART;
			break;
		case SysConst.INDICATOR:
			mttype = CommonSysConst.MT_TYPE_INDICATOR;
			break;
		case SysConst.TABLE:
			mttype = CommonSysConst.MT_TYPE_TABLE;
			break;
		case SysConst.PIVOTCOMBINATIONCHART:
			mttype = CommonSysConst.MT_TYPE_PIVOTCOMBINATIONCHART;
			break;
		}
		return mttype;
	}

	/**
	 * 判断仪表盘对象是否存在
	 * 
	 * @param code
	 * @throws Exception
	 */
	public boolean isExistDashboardObj(String code) throws Exception {
		logger.debug("是否存在仪表盘对象" + code + "]");
		CommDMO dmo = new CommDMO();
		String sql = "select b.id from bam_dashboardobject b where b.code =?";
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
	 * 复制仪表盘对象
	 * 
	 * @param dataValue
	 * @throws Exception
	 */
	public void copyDashboardObj(Map<String, Object> dataValue)
			throws Exception {
		String code = dataValue.get("CODE") + "";
		if (isExistDashboardObj(code)) {
			throw new Exception("仪表盘对象已存在！");
		} else {
			String id = "";
			RefItemVO folderVo = (RefItemVO) dataValue.get("FOLDERID");
			if (dataValue.get("TYPE") instanceof ComBoxItemVO) {
				id = ((ComBoxItemVO) dataValue.get("TYPE")).getId();
				int type = Integer.parseInt(id);
				dataValue.put("MTTYPE", getMtTypeByChartType(type));
			}
			dataValue.put("MTCODE",
					SysConst.DO_METADATA_PREFIX.concat(code));
			mtm.updateMetadataTemplet(dataValue.get("MTCODE") + "",
					dataValue.get("CONTENT") + "", dataValue.get("NAME") + "",
					dataValue.get("OWNER") + "", dataValue.get("MTTYPE") + "");
			CommDMO dmo = new CommDMO();
			try {
				String sql = "insert into bam_dashboardobject(id,folderid,name,code,description,type,mtcode) "
						+ "values(s_bam_dashboardobject.nextval,?,?,?,?,?,?)";
				dmo.executeUpdateByDS(null, sql, folderVo.getId(),
						dataValue.get("NAME"), dataValue.get("CODE"),
						dataValue.get("DESCRIPTION"), id,
						dataValue.get("MTCODE"));
				dmo.commit(null);
			} catch (Exception e) {
				throw e;
			} finally {
				dmo.releaseContext(null);
			}
		}
	}

	/**
	 * 删除与仪表盘对象相关联的访问控制过滤器
	 * @param dataValue
	 * @throws Exception
	 */
	public void delDboRelateAccessFilter(String code) throws Exception{
		CommDMO dmo = new CommDMO();
		String sql = "delete from bam_r_accessfilter_do where dashboardobjectid=(select id from bam_dashboardobject where code=?)";
		try{
			dmo.executeUpdateByDS(null, sql,code);
		}catch (Exception e) {
			throw e;
		} 
	}

}
