package smartx.publics.metadata;

import org.apache.log4j.Logger;

import smartx.framework.common.bs.CommDMO;
import smartx.framework.common.vo.HashVO;
import smartx.framework.common.vo.NovaLogger;

/**
 *@author zzy
 *@date Feb 21, 2012
 **/
public class MetadataTempletUtil {
	
	private static Logger logger = NovaLogger.getLogger(MetadataTempletUtil.class);
	
	/**
	 * 新建或更新元数据模板
	 * @param mtcode
	 * @param content
	 * @throws Exception
	 */
	public static void updateMetadataTemplet(String mtcode, String content,String name, String owner, String type) throws Exception {
		logger.debug("更新元数据模板" + mtcode + "]");
		CommDMO dmo = new CommDMO();
		String sql = "select p.id from pub_metadata_templet p where p.code=?";
		HashVO[] vos;
		try {
			vos = dmo.getHashVoArrayByDS(null, sql, mtcode);
			if (vos.length > 0) {
				sql = "update pub_metadata_templet p set p.type=? where p.code=?";
				dmo.executeUpdateByDS(null, sql, type, mtcode);
				dmo.executeUpdateClobByDS(null, "content",
						"pub_metadata_templet",
						"code='".concat(mtcode).concat("'"), content);
			} else {
				sql = "insert into pub_metadata_templet(id,name,code,owner,scope,content,type) values(S_PUB_METADATA_TEMPLET.Nextval,?,?,?,'',empty_clob(),?)";
				dmo.executeUpdateByDS(null, sql, name, mtcode, owner, type);
				if (content != null)
					dmo.executeUpdateClobByDS(null, "content",
							"pub_metadata_templet", "code='".concat(mtcode)
									.concat("'"), content);
			}
		} catch (Exception e) {
			logger.error(e);
			throw e;
		}
	}

	/**
	 * 删除元数据模板
	 * @param mtcode
	 * @throws Exception
	 */
	public static void deleteMetadataTemplet(String mtcode) throws Exception {
		logger.debug("删除元数据模板" + mtcode + "]");
		if (mtcode == null || "".equals(mtcode))
			throw new Exception("元数据模板编码不能为空!");
		CommDMO dmo = new CommDMO();
		String sql = "delete from pub_metadata_templet p where p.code=?";
		try {
			dmo.executeUpdateByDS(null, sql, mtcode);
		} catch (Exception e) {
			logger.error(e);
			throw e;
		}
	}

}
