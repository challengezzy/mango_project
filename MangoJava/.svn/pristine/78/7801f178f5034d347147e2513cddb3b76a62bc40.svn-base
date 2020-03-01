/**
 * 
 */
package smartx.bam.bs.entitymodel;

import java.util.Map;

import smartx.bam.vo.DatabaseConst;
import smartx.framework.common.bs.CommDMO;
import smartx.framework.common.utils.StringUtil;
import smartx.framework.common.vo.HashVO;
import smartx.publics.metadata.bs.service.SmartXMetadataTempletService;

/**
 * @author caohenghui
 * Dec 2, 2011
 */
public class EntityDashbordService {
	
	private final String MT_HEAD = "MT_LAYOUT_";
	
	private final String FOLDER_HEAD = "ENTITYMODEL_FOLDER_";
	
	private final String DASHBORD_HEAD = "ENTITYMODEL_LAYOUT_";
	
	private SmartXMetadataTempletService smts = new SmartXMetadataTempletService();
	
	public String saveEntityDashbord(Map<String,String> map) throws Exception {
		
		CommDMO dmo = new CommDMO();
		String dashBordCode ="";
		try{
			String name = map.get("name");
			String modelCode = map.get("code");
			String desc = map.get("description");
			String content = map.get("content");
			String user = map.get("user");
			String refTime = map.get("reftime");
			
			String folderId = dmo.getSequenceNextValByDS(DatabaseConst.DATASOURCE_DEFAULT, "s_bam_folder");
			String folderCode = FOLDER_HEAD+modelCode+"_"+folderId;
			
			String insertFolder = "insert into bam_folder (seq,type,id,name,code) values(0,1,?,?,?)";
			dmo.executeUpdateByDS(DatabaseConst.DATASOURCE_DEFAULT, insertFolder,folderId,name,folderCode);
			dmo.commit(DatabaseConst.DATASOURCE_DEFAULT);
			
			String dashbordId = dmo.getSequenceNextValByDS(DatabaseConst.DATASOURCE_DEFAULT, "s_bam_dashboard");
			dashBordCode = DASHBORD_HEAD+modelCode+"_"+dashbordId;
			String mtCode = MT_HEAD+dashBordCode;
			
			String insertMT ="insert into pub_metadata_templet(ID,NAME,CODE,OWNER,TYPE) values(s_pub_metadata_templet.nextval,?,?,?,?)";
			dmo.executeUpdateByDS(DatabaseConst.DATASOURCE_DEFAULT, insertMT, name,mtCode,user,"3");
			dmo.commit(DatabaseConst.DATASOURCE_DEFAULT);
			
			dmo.executeUpdateClobByDS(DatabaseConst.DATASOURCE_DEFAULT, "content", "pub_metadata_templet", " code='"+mtCode+"'", content);
			dmo.commit(DatabaseConst.DATASOURCE_DEFAULT);
			
			String insertDashbord = "insert into bam_dashboard(id,folderid,name,code,description,refreshinterval,layout_mtcode,seq) values(s_bam_dashboard.nextval,?,?,?,?,?,?,?)";
			dmo.executeUpdateByDS(DatabaseConst.DATASOURCE_DEFAULT, insertDashbord, folderId,name,dashBordCode,desc,refTime,mtCode,"0");
			dmo.commit(DatabaseConst.DATASOURCE_DEFAULT);
			
		}catch(Exception e){
			dmo.rollback(DatabaseConst.DATASOURCE_DEFAULT);
		}finally{
			dmo.releaseContext(DatabaseConst.DATASOURCE_DEFAULT);
		}
		return dashBordCode;
	}
	
	public void updateEntityDashbord(Map<String,String> map) throws Exception {
		CommDMO dmo = new CommDMO();
		try{
			
			String name = map.get("name");
			String code = map.get("code");
			String desc = map.get("description");
			String content = map.get("content");
			String user = map.get("user");
			String mtcode = map.get("mtcode");
			String refTime = map.get("reftime");
			
			String insertMT ="update pub_metadata_templet set name=?,OWNER=? where code=?";
			dmo.executeUpdateByDS(DatabaseConst.DATASOURCE_DEFAULT, insertMT, name,user,mtcode);
			dmo.commit(DatabaseConst.DATASOURCE_DEFAULT);
			
			dmo.executeUpdateClobByDS(DatabaseConst.DATASOURCE_DEFAULT, "content", "pub_metadata_templet", " code='"+mtcode+"'", content);
			dmo.commit(DatabaseConst.DATASOURCE_DEFAULT);
			
			String updateDashbord ="update bam_dashboard set NAME=?,DESCRIPTION=?,refreshinterval=? where code=?";
			dmo.executeUpdateByDS(DatabaseConst.DATASOURCE_DEFAULT, updateDashbord,name,desc,refTime,code);
			dmo.commit(DatabaseConst.DATASOURCE_DEFAULT);
			
			smts.resetCacheByMtCode(code);
			
		}catch(Exception e){
			dmo.rollback(DatabaseConst.DATASOURCE_DEFAULT);
			throw e;
		}finally{
			dmo.releaseContext(DatabaseConst.DATASOURCE_DEFAULT);
		}
	}
	
	public void deleteEntityDashbord(Map<String,String> map) throws Exception {

		CommDMO dmo = new CommDMO();
		try{
			
			String code = map.get("code");
			
			String searchSQL = "select t.folderid,t.layout_mtcode mtcode from bam_dashboard t where t.code=?";
			
			HashVO[] vos = dmo.getHashVoArrayByDS(DatabaseConst.DATASOURCE_DEFAULT, searchSQL, code);
			if(vos != null && vos.length>0){
				String forderId = vos[0].getStringValue("folderid");
				String mtcode = vos[0].getStringValue("mtcode");
				if(!StringUtil.isEmpty(forderId)){
					String updateForderId ="update bam_dashboard t set t.folderid= null where t.code=?";
					dmo.executeUpdateByDS(DatabaseConst.DATASOURCE_DEFAULT, updateForderId, code);
					dmo.commit(DatabaseConst.DATASOURCE_DEFAULT);
				}
				if(!StringUtil.isEmpty(mtcode)){
					String delMT = "delete from pub_metadata_templet where code=?";
					dmo.executeUpdateByDS(DatabaseConst.DATASOURCE_DEFAULT, delMT, mtcode);
					dmo.commit(DatabaseConst.DATASOURCE_DEFAULT);
				}
				
				String delDoUser = "delete from BAM_USER_DASHBOARD where DASHBOARDID in (select id from BAM_DASHBOARD where code='"+code+"')";
				dmo.executeUpdateByDS(DatabaseConst.DATASOURCE_DEFAULT, delDoUser);
				dmo.commit(DatabaseConst.DATASOURCE_DEFAULT);
				
				String delDoRole = "delete from bam_role_dashboard where DASHBOARDID in (select id from BAM_DASHBOARD where code='"+code+"')";
				dmo.executeUpdateByDS(DatabaseConst.DATASOURCE_DEFAULT, delDoRole);
				dmo.commit(DatabaseConst.DATASOURCE_DEFAULT);
				
				String delDoWk = "delete from bam_workposition_dashboard where DASHBOARDID in (select id from BAM_DASHBOARD where code='"+code+"')";
				dmo.executeUpdateByDS(DatabaseConst.DATASOURCE_DEFAULT, delDoWk);
				dmo.commit(DatabaseConst.DATASOURCE_DEFAULT);
				
				String delDo = "delete from bam_dashboard where code=?";
				dmo.executeUpdateByDS(DatabaseConst.DATASOURCE_DEFAULT, delDo, code);
				dmo.commit(DatabaseConst.DATASOURCE_DEFAULT);
				
				smts.resetCacheByMtCode(code);
			}
			
		}catch(Exception e){
			dmo.rollback(DatabaseConst.DATASOURCE_DEFAULT);
			throw e;
		}finally{
			dmo.releaseContext(DatabaseConst.DATASOURCE_DEFAULT);
		}
	}
}
