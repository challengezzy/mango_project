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
 * Nov 28, 2011
 */
public class EntityDataViewerService {
	
	private final String MT_HEAD = "MT_DO_";
	
	private final String FOLDER_HEAD = "ENTITYMODEL_FOLDER_";
	
	private SmartXMetadataTempletService smts = new SmartXMetadataTempletService();
	
	public String saveEntityViewer(Map<String,String> map) throws Exception{
		CommDMO dmo = new CommDMO();
		String doCode ="";
		try{
			
			String name = map.get("name");
			String code = map.get("code");
			String desc = map.get("description");
			String content = map.get("content");
			String type = map.get("type");
			String user = map.get("user");
			String entityName = map.get("entityName");
			
			String id = dmo.getSequenceNextValByDS(null, "s_bam_dashboardobject");
			
			doCode = code+"_"+id;
			
			String mtCode = MT_HEAD+doCode;
			
			String folderId = dmo.getSequenceNextValByDS(DatabaseConst.DATASOURCE_DEFAULT, "s_bam_folder");
			String folderCode = FOLDER_HEAD+doCode+"_"+folderId;
			
			String insertFolder = "insert into bam_folder (seq,type,id,name,code) values(0,1,?,?,?)";
			dmo.executeUpdateByDS(DatabaseConst.DATASOURCE_DEFAULT, insertFolder,folderId,entityName,folderCode);
			dmo.commit(DatabaseConst.DATASOURCE_DEFAULT);
			
			String insertMT ="insert into pub_metadata_templet(ID,NAME,CODE,OWNER,TYPE) values(s_pub_metadata_templet.nextval,?,?,?,?)";
			dmo.executeUpdateByDS(DatabaseConst.DATASOURCE_DEFAULT, insertMT, name,mtCode,user,type);
			dmo.commit(DatabaseConst.DATASOURCE_DEFAULT);
			
			dmo.executeUpdateClobByDS(DatabaseConst.DATASOURCE_DEFAULT, "content", "pub_metadata_templet", " code='"+mtCode+"'", content);
			dmo.commit(DatabaseConst.DATASOURCE_DEFAULT);
			
			String insertDo ="insert into bam_dashboardobject(ID,folderid,NAME,CODE,DESCRIPTION,TYPE,MTCODE) values(?,?,?,?,?,?,?)";
			dmo.executeUpdateByDS(DatabaseConst.DATASOURCE_DEFAULT, insertDo,id,folderId,name,doCode,desc,type,mtCode);
			dmo.commit(DatabaseConst.DATASOURCE_DEFAULT);
			
		}catch(Exception e){
			dmo.rollback(DatabaseConst.DATASOURCE_DEFAULT);
			throw e;
		}finally{
			dmo.releaseContext(DatabaseConst.DATASOURCE_DEFAULT);
		}
		return doCode;
	}
	
	public void updateEntityViewer(Map<String,String> map) throws Exception{
		CommDMO dmo = new CommDMO();
		try{
			
			String name = map.get("name");
			String code = map.get("code");
			String desc = map.get("description");
			String content = map.get("content");
			String type = map.get("type");
			String user = map.get("user");
			String mtcode = map.get("mtcode");
			
			String insertMT ="update pub_metadata_templet set name=?,OWNER=?,type=? where code=?";
			dmo.executeUpdateByDS(DatabaseConst.DATASOURCE_DEFAULT, insertMT, name,user,type,mtcode);
			dmo.commit(DatabaseConst.DATASOURCE_DEFAULT);
			
			dmo.executeUpdateClobByDS(DatabaseConst.DATASOURCE_DEFAULT, "content", "pub_metadata_templet", " code='"+mtcode+"'", content);
			dmo.commit(DatabaseConst.DATASOURCE_DEFAULT);
			
			String insertDo ="update bam_dashboardobject set NAME=?,DESCRIPTION=?,TYPE=? where code=?";
			dmo.executeUpdateByDS(DatabaseConst.DATASOURCE_DEFAULT, insertDo,name,desc,type,code);
			dmo.commit(DatabaseConst.DATASOURCE_DEFAULT);
			
			smts.resetCacheByMtCode(code);
			
		}catch(Exception e){
			dmo.rollback(DatabaseConst.DATASOURCE_DEFAULT);
			throw e;
		}finally{
			dmo.releaseContext(DatabaseConst.DATASOURCE_DEFAULT);
		}
	}
	
	public void deleteEntityViewer(Map<String,String> map) throws Exception{
		CommDMO dmo = new CommDMO();
		try{
			
			String code = map.get("code");
			
			String searchSQL = "select t.folderid,t.mtcode from bam_dashboardobject t where t.code=?";
			
			HashVO[] vos = dmo.getHashVoArrayByDS(DatabaseConst.DATASOURCE_DEFAULT, searchSQL, code);
			if(vos != null && vos.length>0){
				String forderId = vos[0].getStringValue("folderid");
				String mtcode = vos[0].getStringValue("mtcode");
				if(!StringUtil.isEmpty(forderId)){
					String updateForderId ="update bam_dashboardobject t set t.folderid= null where t.code=?";
					dmo.executeUpdateByDS(DatabaseConst.DATASOURCE_DEFAULT, updateForderId, code);
					dmo.commit(DatabaseConst.DATASOURCE_DEFAULT);
				}
				if(!StringUtil.isEmpty(mtcode)){
					String delMT = "delete from pub_metadata_templet where code=?";
					dmo.executeUpdateByDS(DatabaseConst.DATASOURCE_DEFAULT, delMT, mtcode);
					dmo.commit(DatabaseConst.DATASOURCE_DEFAULT);
				}
				
				String delDoWk = "delete from bam_r_accessfilter_do where DASHBOARDOBJECTID in (select id from bam_dashboardobject where code='"+code+"')";
				dmo.executeUpdateByDS(DatabaseConst.DATASOURCE_DEFAULT, delDoWk);
				dmo.commit(DatabaseConst.DATASOURCE_DEFAULT);
				
				String delDo = "delete from bam_dashboardobject where code=?";
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
