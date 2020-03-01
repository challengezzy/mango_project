/**
 * 
 */
package smartx.bam.bs.qvmanager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import smartx.framework.common.bs.CommDMO;
import smartx.framework.common.vo.HashVO;
import smartx.framework.metadata.vo.ComBoxItemVO;
import smartx.framework.metadata.vo.RefItemVO;

/**
 * @author sky
 * 
 */
public class QueryViewManager {

	private Logger logger = Logger.getLogger(this.getClass());

	/**
	 * 查询所有有依赖的业务视图编码
	 * 
	 * @param sql
	 * @return
	 */
	public Map<String, String> queryAllDependBv(String code) throws Exception {
		CommDMO dmo = new CommDMO();
		Map<String, String> result = new HashMap<String, String>();
		try {
			String sql = "select v.metadata,v.name,v.code from v_bam_businessview v where v.type=1";
			HashVO[] vos = dmo.getHashVoArrayByDS(null, sql);
			for (HashVO vo : vos) {
				String metadata = vo.getStringValue("metadata");
				if(metadata != null && !"".equals(metadata)){
					Document doc = DocumentHelper.parseText(metadata);
					if (isDependBv(code, doc))
						result.put(vo.getStringValue("code"),
								vo.getStringValue("name"));
				}
			}
		} catch (Exception e) {
			logger.error("", e);
			throw e;
		} finally {
			dmo.releaseContext(null);
		}
		return result;
	}

	private boolean isDependBv(String code, Document doc) {
		Element root = doc.getRootElement();
		Element refbvs = root.element("refbvs");
		if (refbvs != null) {
			List<?> nodes = refbvs.elements("node");
			for (int i = 0; i < nodes.size(); i++) {
				Element node = (Element) nodes.get(i);
				if (code.equals(node.attributeValue("eventAlias")))
					return true;
			}
		}
		return false;
	}

	/**
	 * 复制查询视图
	 * 
	 * @param data
	 * @throws Exception
	 */
	public void copyQueryView(Map<String, Object> dataValue) throws Exception {
		if(isExistQv(dataValue.get("CODE")+""))
			throw new Exception("该查询视图已经存在！");
		else{
			CommDMO dmo = new CommDMO();
			try {
				RefItemVO folderVo = (RefItemVO) dataValue.get("FOLDERID");
				ComBoxItemVO datasourceVo = (ComBoxItemVO) dataValue
						.get("DATASOURCENAME");
				String sql = "insert into bam_queryview(id,name,code,datasourcename,sql,folderid,description)"
						+ " values(s_bam_queryview.nextval,?,?,?,?,?,?)";
				dmo.execAtOnceByDS(null, sql, dataValue.get("NAME"),
						dataValue.get("CODE"), datasourceVo.getId(),
						dataValue.get("SQL"), folderVo.getId(),
						dataValue.get("DESCRIPTION"));
			} catch (Exception e) {
				logger.error("", e);
				throw e;
			} finally {
				dmo.releaseContext(null);
			}
		}
	}
	
	/**
	 * 判断查询视图是否存在
	 * 
	 * @param code
	 * @throws Exception
	 */
	public boolean isExistQv(String code) throws Exception {
		logger.debug("是否存在查询视图[" + code + "]");
		CommDMO dmo = new CommDMO();
		String sql = "select b.id from bam_queryview b where b.code =?";
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
}
