/**
 * 
 */
package smartx.bam.bs.entityrule;

import java.util.List;
import java.util.Map;

import smartx.framework.common.bs.CommDMO;
import smartx.framework.common.utils.StringUtil;
import smartx.framework.metadata.vo.Pub_Templet_1VO;
import smartx.publics.form.bs.service.FormInterceptor;

/**
 * @author caohenghui
 * May 10, 2012
 */
public class DQEntityRuleDeleteBfFormInterceptor implements FormInterceptor {

	/* (non-Javadoc)
	 * @see smartx.publics.form.bs.service.FormInterceptor#doSomething(smartx.framework.metadata.vo.Pub_Templet_1VO, java.util.Map)
	 */
	@Override
	public void doSomething(Pub_Templet_1VO templetVO,
			Map<String, Object> dataValue) throws Exception {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see smartx.publics.form.bs.service.FormInterceptor#doSomething(smartx.framework.metadata.vo.Pub_Templet_1VO, java.util.List)
	 */
	@Override
	public void doSomething(Pub_Templet_1VO templetVO,
			List<Map<String, Object>> dataValueList) throws Exception {
		// TODO Auto-generated method stub
		CommDMO dmo = new CommDMO();
		try{
			String datasource = templetVO.getDatasourcename();
			if(dataValueList != null){
				for(Map<String,Object> map : dataValueList){
					String opStr = (String)map.get("FORMSERVICE_MODIFYFLAG");
					String batchno = (String)map.get("BATCHNO");
					if(!StringUtil.isEmpty(opStr) && opStr.equalsIgnoreCase("delete")){
						dmo.executeUpdateByDS(datasource, "delete from dq_entity_statistics_group g where g.statisticsid in (select id from dq_entity_statistics t where t.batchno=?)", batchno);
						dmo.commit(datasource);
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
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
