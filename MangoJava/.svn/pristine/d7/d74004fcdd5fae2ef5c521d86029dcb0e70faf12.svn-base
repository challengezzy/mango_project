/**
 * 
 */
package smartx.bam.bs.dashboard;

import java.util.Map;

import org.apache.log4j.Logger;

import smartx.bam.bs.common.MetadataTempletManager;
import smartx.bam.vo.SysConst;
import smartx.framework.common.bs.CommDMO;
import smartx.framework.common.vo.HashVO;
import smartx.framework.metadata.vo.RefItemVO;
import smartx.system.common.constant.CommonSysConst;

/**
 * @author sky
 */
public class DashboardManager {
	private Logger logger = Logger.getLogger(this.getClass());

	private MetadataTempletManager mtm = new MetadataTempletManager();

	/**
	 * 判断仪表盘是否存在
	 * 
	 * @param code
	 * @throws Exception
	 */
	public boolean isExistDashboard(String code) throws Exception {
		logger.debug("是否存在仪表盘" + code + "]");
		CommDMO dmo = new CommDMO();
		String sql = "select b.id from bam_dashboard b where b.code =?";
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
	 * 复制仪表盘
	 * 
	 * @param dataValue
	 * @throws Exception
	 */
	public void copyDashboard(Map<String, Object> dataValue) throws Exception {
		String code = String.valueOf(dataValue.get("CODE"));
		if (isExistDashboard(code))
			throw new Exception("仪表盘已存在!");
		else {
			dataValue.put("LAYOUT_MTCODE",
					SysConst.DB_METADATA_PREFIX.concat(code));
			dataValue.put("MTTYPE", CommonSysConst.MT_TYPE_DASHBOARD);
			mtm.updateMetadataTemplet(dataValue.get("LAYOUT_MTCODE") + "",
					dataValue.get("CONTENT") + "", dataValue.get("NAME") + "",
					dataValue.get("OWNER") + "", dataValue.get("MTTYPE") + "");
			RefItemVO folderVo = (RefItemVO) dataValue.get("FOLDERID");
			CommDMO dmo = new CommDMO();
			try {
				String sql = "insert into bam_dashboard(id,folderid,name,code,description,refreshinterval,layout_mtcode,seq) "
						+ "values(s_bam_dashboard.nextval,?,?,?,?,?,?,?)";
				dmo.executeUpdateByDS(null, sql, folderVo.getId(),
						dataValue.get("NAME"), dataValue.get("CODE"),
						dataValue.get("DESCRIPTION"),
						dataValue.get("REFRESHINTERVAL"),
						dataValue.get("LAYOUT_MTCODE"), dataValue.get("SEQ"));
				dmo.commit(null);
			} catch (Exception e) {
				throw e;
			} finally {
				dmo.releaseContext(null);
			}
		}
	}
	
	/**
	 * 删除仪表盘相关权限
	 * @param dataValue
	 * @throws Exception
	 */
	public void delDashboardAuthority(String code) throws Exception{
		CommDMO dmo = new CommDMO();
		String sqlUser = "delete from bam_user_dashboard b where b.dashboardid=(select id from bam_dashboard where code='"+code+"')";
		String sqlRole = "delete from bam_role_dashboard b where b.dashboardid=(select id from bam_dashboard where code='"+code+"')";
		String sqlWorkposition = "delete from bam_workposition_dashboard b where b.dashboardid=(select id from bam_dashboard where code='"+code+"')";
		try{
			dmo.executeBatchByDS(null, new String[]{sqlUser,sqlRole,sqlWorkposition});
		}catch(Exception e){
			throw e;
		}
	}
}
