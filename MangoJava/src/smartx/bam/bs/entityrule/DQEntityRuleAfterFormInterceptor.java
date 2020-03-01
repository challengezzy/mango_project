/**
 * 
 */
package smartx.bam.bs.entityrule;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import smartx.bam.vo.DatabaseConst;
import smartx.framework.common.bs.CommDMO;
import smartx.framework.common.utils.StringUtil;
import smartx.framework.common.vo.NovaLogger;
import smartx.framework.metadata.vo.Pub_Templet_1VO;
import smartx.publics.form.bs.service.FormInterceptor;
import smartx.publics.metadata.bs.service.SmartXMetadataTempletService;

/**
 * @author caohenghui
 * Mar 26, 2012
 */
public class DQEntityRuleAfterFormInterceptor implements FormInterceptor {
	
	protected Logger logger = NovaLogger.getLogger(this.getClass());

	/* (non-Javadoc)
	 * @see smartx.publics.form.bs.service.FormInterceptor#doSomething(smartx.framework.metadata.vo.Pub_Templet_1VO, java.util.Map)
	 */
	@Override
	public void doSomething(Pub_Templet_1VO templetVO,
			Map<String, Object> dataValue) throws Exception {
		CommDMO dmo = new CommDMO();
		try{
			
			String modifyType = (String)dataValue.get("FORMSERVICE_MODIFYFLAG");
			
			String content = (String)dataValue.get("RULECONTENT");
			String mtCode = (String)dataValue.get("MTCODE");
			String name = (String)dataValue.get("NAME");
			
			if(modifyType.equalsIgnoreCase("insert")){
				dmo.executeUpdateByDS(DatabaseConst.DATASOURCE_DEFAULT, "insert into pub_metadata_templet(ID,NAME,CODE,OWNER,SCOPE,TYPE) values(s_pub_metadata_templet.nextval,?,?,?,?,?)", name,mtCode,"admin","DQ",107);
				dmo.commit(DatabaseConst.DATASOURCE_DEFAULT);
				
				content = StringUtil.isEmpty(content)?"<rule />":content;
				dmo.executeUpdateClobByDS(DatabaseConst.DATASOURCE_DEFAULT, "CONTENT", "pub_metadata_templet", " code='"+mtCode+"'", content);
				dmo.commit(DatabaseConst.DATASOURCE_DEFAULT);

				
			}else if(modifyType.equalsIgnoreCase("update")){
				
				dmo.executeUpdateByDS(DatabaseConst.DATASOURCE_DEFAULT, "update pub_metadata_templet t set t.name=? where code=?", name,mtCode);
				dmo.commit(DatabaseConst.DATASOURCE_DEFAULT);
				
				content = StringUtil.isEmpty(content)?"<rule />":content;
				dmo.executeUpdateClobByDS(DatabaseConst.DATASOURCE_DEFAULT, "CONTENT", "pub_metadata_templet", " code='"+mtCode+"'", content);
				dmo.commit(DatabaseConst.DATASOURCE_DEFAULT);

			}
			
		}catch(Exception e){
			logger.debug("",e);
		}

	}

	/* (non-Javadoc)
	 * @see smartx.publics.form.bs.service.FormInterceptor#doSomething(smartx.framework.metadata.vo.Pub_Templet_1VO, java.util.List)
	 */
	@Override
	public void doSomething(Pub_Templet_1VO templetVO,
			List<Map<String, Object>> dataValueList) throws Exception {
		CommDMO dmo = new CommDMO();
		try{
			SmartXMetadataTempletService metaDataService = new SmartXMetadataTempletService();
			if( dataValueList != null){
				for(Map<String,Object> map : dataValueList){
					String modifyType = (String)map.get("FORMSERVICE_MODIFYFLAG");
					String mtCode = (String)map.get("MTCODE");
					if(modifyType.equalsIgnoreCase("delete")){
						dmo.executeUpdateByDS(DatabaseConst.DATASOURCE_DEFAULT,"delete from pub_metadata_templet t where t.code=?", mtCode);
						dmo.commit(DatabaseConst.DATASOURCE_DEFAULT);
						metaDataService.resetCacheByMtCode(mtCode);
					}
				}
			}
			
		}catch(Exception e){
			logger.debug("",e);
		}

	}

	/* (non-Javadoc)
	 * @see smartx.publics.form.bs.service.FormInterceptor#doSomething(java.util.Map)
	 */
	@Override
	public void doSomething(Map<String, Object> map) throws Exception {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see smartx.publics.form.bs.service.FormInterceptor#doSomething(java.util.List)
	 */
	@Override
	public void doSomething(List<Map<String, Object>> dataValueList)
			throws Exception {
		// TODO Auto-generated method stub

	}

}
