/**
 * 
 */
package smartx.bam.bs.accountset;

import java.util.Map;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import smartx.framework.common.bs.CommDMO;
import smartx.framework.common.utils.StringUtil;
import smartx.framework.common.vo.HashVO;
import smartx.framework.common.vo.NovaLogger;
import smartx.framework.common.vo.SimpleHashVO;

/**
 * @author caohenghui
 * 
 */
public class AccountSettingService {
	
	protected Logger logger = NovaLogger.getLogger(this.getClass());

	public void dealAcccountAttribute(Map<String,Object> map) {
		
		CommDMO dmo = new CommDMO();
		String sql = "SELECT ID,USERNAME,MTCODE,CONTENT FROM V_BAM_ACCOUNTSETTINGS WHERE USERNAME=? AND MTCODE=?";
		HashVO[] vos;

		try
		{
			String refreshtime =  (String)map.get("refreshtime");
			String dashbordcode = (String)map.get("dashbordcode");
			String rowcount = (String)map.get("alertrowcount");
			
			String username = (String)map.get("username")==null?"sa":(String)map.get("username");
			
			String mtCode = "MT_ACCOUNTSET_"+username;
			
			if(!StringUtil.isNumber(refreshtime)){
				refreshtime = "5000";
			}else{
				refreshtime = Integer.parseInt(refreshtime)*1000+"";
			}
			
			vos = dmo.getHashVoArrayByDS( null, sql,username,mtCode);
			if(vos.length > 0){
				String mtcode = vos[0].getStringValue("MTCODE");
				sql = "update pub_metadata_templet set content=? where code=?";
				dmo.executeUpdateByDS(null, sql, getContectXML(refreshtime,dashbordcode,rowcount),mtcode);
				
			}else{
				
				String insertSQL="insert into BAM_ACCOUNTSETTINGS(id,USERNAME,mtcode) values(S_BAM_ACCOUNTSETTINGS.Nextval,?,?)";
				dmo.executeUpdateByDS( null, insertSQL,username,mtCode);
				
				sql ="insert into pub_metadata_templet(id,name,code,owner,scope,content,type) values(S_PUB_METADATA_TEMPLET.Nextval,?,?,?,'',?,0)";
				dmo.executeUpdateByDS( null, sql,username,mtCode,username,getContectXML(refreshtime,dashbordcode,rowcount));
				
			}
			dmo.commit(null);
		}
		catch( Exception e ){
			
		}finally{
			dmo.releaseContext( null );
		}
	}
	
	private String getContectXML(String refreshtime,String dashbordcode,String rowcount){
		
		StringBuffer content = new StringBuffer();
		
		content.append("<user-args>\n");
		
		content.append("<dashbord-refresh-interval>");
		content.append(refreshtime);
		content.append("</dashbord-refresh-interval>\n");
		
		content.append("<default-dashbord-code>");
		content.append(dashbordcode);
		content.append("</default-dashbord-code>\n");
		
		content.append("<alert-row-count>");
		content.append(rowcount);
		content.append("</alert-row-count>\n");
		
		content.append("</user-args>");
		
		return content.toString();
	}

	public SimpleHashVO[] getAttributeSimpleHashVo(String ds, String userName)
			throws Exception {
		CommDMO dmo = new CommDMO();
		try {
			String sql ="SELECT ID,USERNAME,MTCODE,CONTENT FROM V_BAM_ACCOUNTSETTINGS WHERE USERNAME=?";
			HashVO[] vos = dmo.getHashVoArrayByDS(ds, sql,userName);
			SimpleHashVO[] result = new SimpleHashVO[vos.length];
			for (int i = 0; i < vos.length; i++) {
				HashVO temp = new HashVO();
				Document doc = DocumentHelper.parseText(vos[i].getStringValue("CONTENT")==null?"":vos[i].getStringValue("CONTENT"));
				Element root = doc.getRootElement();
				String refreshtime = root.element("dashbord-refresh-interval").getText();
				String dashbordcode = root.element("default-dashbord-code").getText();
				String alertrowcount = root.element("alert-row-count").getText();
				
				if(!StringUtil.isNumber(refreshtime)){
					refreshtime = "5";
				}else{
					refreshtime = Integer.parseInt(refreshtime)/1000+"";
				}
				
				temp.setAttributeValue("dashbordcode", dashbordcode);
				temp.setAttributeValue("dashbordname", this.getDBONameByCode(dashbordcode));
				
				temp.setAttributeValue("refreshtime", refreshtime);
				temp.setAttributeValue("alertrowcount", alertrowcount);
				SimpleHashVO vo = new SimpleHashVO(temp);
				result[i] = vo;
			}
			return result;
		} finally {
			dmo.releaseContext(ds);
		}
	}
	
	private String getDBONameByCode(String dboCode){
		CommDMO dmo = new CommDMO();
		String dboName = "";
		try{
			
			String sql ="select t.NAME from bam_dashboard t where t.code=?";
			HashVO[] vos = dmo.getHashVoArrayByDS(null, sql,dboCode);
			for (HashVO vo : vos) {
				
				dboName = vo.getStringValue("NAME");
				
			}
		}catch(Exception e){
			logger.debug("",e);
		}finally{
			dmo.releaseContext(null);
		}
		
		return dboName;
	}

}
