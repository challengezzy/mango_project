package smartx.publics.form.bs.interceptors.impl;

import java.util.List;
import java.util.Map;

import smartx.framework.common.bs.CommDMO;
import smartx.framework.metadata.vo.Pub_Templet_1VO;
import smartx.publics.form.bs.service.FormInterceptor;

public class DeleteRoleBfInterceptorImpl implements FormInterceptor {

	@Override
	public void doSomething(Pub_Templet_1VO templetVO,
			Map<String, Object> dataValue) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void doSomething(Pub_Templet_1VO templetVO,
			List<Map<String, Object>> dataValueList) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void doSomething(Map<String, Object> map) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void doSomething(List<Map<String, Object>> dataValueList)
			throws Exception {

		CommDMO dmo = new CommDMO();
		String datasource = null;
		try{
			
			for(Map<String,Object> map : dataValueList){
				String flag = map.get("flag").toString();
				String pkvalue = map.get("pkvalue").toString();
				datasource = map.get("datasource").toString();
				if(flag.trim().equalsIgnoreCase("delete")){
					String sql ="delete from pub_role_menu t where t.roleid=?";
					dmo.executeUpdateByDS(datasource, sql, pkvalue);
					dmo.commit(datasource);
				}
			}
			
			
		}catch(Exception e){
			dmo.rollback(datasource);
		}finally{
			dmo.releaseContext(datasource);
		}

	
	}

}
