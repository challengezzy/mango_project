/**
 * 
 */
package smartx.publics.form.bs.interceptors.impl;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import smartx.framework.common.bs.CommDMO;
import smartx.framework.common.vo.NovaLogger;
import smartx.framework.metadata.vo.Pub_Templet_1VO;
import smartx.publics.form.bs.service.FormInterceptor;

/**
 * @author caohenghui
 *
 */
public class DeleteMenuBfInterceptorImpl implements FormInterceptor {

	protected Logger logger = NovaLogger.getLogger(this.getClass());
	
	/* (non-Javadoc)
	 * @see smartx.publics.form.bs.service.FormInterceptor#doSomething(smartx.framework.metadata.vo.Pub_Templet_1VO, java.util.Map)
	 */
	@Override
	public void doSomething(Pub_Templet_1VO templetVO,
			Map<String, Object> dataValue) throws Exception {
		// TODO Auto-generated method stub
		
		CommDMO dmo = new CommDMO();
		String menuId = (String)dataValue.get("ID");
		try{
			
			String menuids = "select id from pub_menu t start with t.id="+menuId+" connect by prior t.id=t.parentmenuid";
			
			String puh = "delete from pub_user_historyrecord where menuid in("+menuids+")";
			String prm = "delete from pub_role_menu where menuid in("+menuids+")";
			String pwm = "delete from pub_workposition_menu where menuid in("+menuids+")";
			String pum = "delete from pub_user_menu where menuid in("+menuids+")";
			
			dmo.executeBatchByDS(null, new String[]{puh,prm,pwm,pum});
			dmo.commit(null);
			
		}catch(Exception e){
			logger.debug("",e);
		}finally{
			dmo.releaseContext(null);
		}

	}

	/* (non-Javadoc)
	 * @see smartx.publics.form.bs.service.FormInterceptor#doSomething(smartx.framework.metadata.vo.Pub_Templet_1VO, java.util.List)
	 */
	@Override
	public void doSomething(Pub_Templet_1VO templetVO,
			List<Map<String, Object>> dataValueList) throws Exception {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see smartx.publics.form.bs.service.FormInterceptor#doSomething(java.util.Map)
	 */
	@Override
	public void doSomething(Map<String, Object> map) throws Exception {
		CommDMO dmo = new CommDMO();
		String menuids = (String)map.get("menuids");
		String dsName = (String)map.get("datasource");
		try{
			
			String puh = "delete from pub_user_historyrecord where menuid in("+menuids+")";
			String prm = "delete from pub_role_menu where menuid in("+menuids+")";
			String pwm = "delete from pub_workposition_menu where menuid in("+menuids+")";
			String pum = "delete from pub_user_menu where menuid in("+menuids+")";
			
			dmo.executeBatchByDS(dsName, new String[]{puh,prm,pwm,pum});
			dmo.commit(dsName);
			
		}catch(Exception e){
			logger.debug("",e);
		}finally{
			dmo.releaseContext(dsName);
		}

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
