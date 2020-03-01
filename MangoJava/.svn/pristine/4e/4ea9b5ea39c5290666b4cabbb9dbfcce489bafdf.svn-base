/**
 * 
 */
package smartx.bam.bs.dataanalyze;

import java.util.List;
import java.util.Map;

import smartx.framework.common.bs.CommDMO;
import smartx.framework.common.vo.HashVO;
import smartx.framework.metadata.vo.Pub_Templet_1VO;
import smartx.publics.form.bs.service.FormInterceptor;
import smartx.publics.metadata.bs.service.SmartXMetadataTempletService;

/**
 * @author caohenghui
 * Jul 17, 2012
 */
public class DBDeleteBfFormInterceptor implements FormInterceptor {

	/* (non-Javadoc)
	 * @see smartx.publics.form.bs.service.FormInterceptor#doSomething(smartx.framework.metadata.vo.Pub_Templet_1VO, java.util.Map)
	 */
	@Override
	public void doSomething(Pub_Templet_1VO templetVO,
			Map<String, Object> dataValue) throws Exception {

	}

	/* (non-Javadoc)
	 * @see smartx.publics.form.bs.service.FormInterceptor#doSomething(smartx.framework.metadata.vo.Pub_Templet_1VO, java.util.List)
	 */
	@Override
	public void doSomething(Pub_Templet_1VO templetVO,
			List<Map<String, Object>> dataValueList) throws Exception {
		
		CommDMO dmo = new CommDMO();
		try{
			
			for(Map<String,Object> dataValue : dataValueList){
				
				String mode = (String)dataValue.get("FORMSERVICE_MODIFYFLAG");
				
				if(mode.equalsIgnoreCase("delete")){
					
					String code = (String)dataValue.get("CODE");
					
					String dbCode = "DQ_"+code;
					String mtCode = "MT_LAYOUT_"+dbCode;
					
					dmo.executeUpdateByDS(null, "delete from pub_metadata_templet where code=?", mtCode);
					dmo.commit(null);
					
					dmo.executeUpdateByDS(null, "delete from bam_dashboard where code=?", dbCode);
					dmo.commit(null);
					
					SmartXMetadataTempletService.getInstance().resetCacheByMtCode(mtCode);
				}
				
			}
			
		}catch(Exception e){
			dmo.rollback(null);
			throw e;
		}finally{
			dmo.releaseContext(null);
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
