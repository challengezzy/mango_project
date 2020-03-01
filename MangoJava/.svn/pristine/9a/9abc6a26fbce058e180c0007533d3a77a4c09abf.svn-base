package smartx.publics.form.bs.interceptors.impl;

import java.util.List;
import java.util.Map;

import smartx.framework.common.bs.CommDMO;
import smartx.framework.metadata.vo.Pub_Templet_1VO;
import smartx.publics.form.bs.service.FormInterceptor;

public class DeleteWorkbenchBfInterceptorImpl implements FormInterceptor {

	@Override
	public void doSomething(Pub_Templet_1VO templetVO,
			Map<String, Object> dataValue) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void doSomething(Pub_Templet_1VO templetVO,
			List<Map<String, Object>> dataValueList) throws Exception {

		CommDMO dmo = new CommDMO();
		String datasource = templetVO.getDatasourcename();
		try{
			
			for(Map<String,Object> map : dataValueList){
				String flag = map.get("FORMSERVICE_MODIFYFLAG").toString();
				String pkvalue = map.get("ID").toString();
				if(flag.trim().equalsIgnoreCase("delete")){
					String sql ="delete from pub_workposition_menu t where t.workpositionid=?";
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

	@Override
	public void doSomething(Map<String, Object> map) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void doSomething(List<Map<String, Object>> dataValueList)
			throws Exception {
		
		
	}

}
