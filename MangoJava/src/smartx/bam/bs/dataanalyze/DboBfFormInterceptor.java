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

/**
 * @author caohenghui
 * Jul 19, 2012
 */
public class DboBfFormInterceptor implements FormInterceptor {

	/* (non-Javadoc)
	 * @see smartx.publics.form.bs.service.FormInterceptor#doSomething(smartx.framework.metadata.vo.Pub_Templet_1VO, java.util.Map)
	 */
	@Override
	public void doSomething(Pub_Templet_1VO templetVO,
			Map<String, Object> dataValue) throws Exception {
		CommDMO dmo = new CommDMO();
		try{

			String querySQL = "select count(*) cou from bam_folder t where t.type = 2 and t.code=?";
			HashVO[] vos = dmo.getHashVoArrayByDS(null, querySQL,"DQ_DATAVISUAL_DBO");
			if(vos != null){
				String dirId =  "-1";
				if(vos[0].getIntegerValue("cou") <= 0){
					dirId = dmo.getSequenceNextValByDS(null, "s_bam_folder");
					dmo.executeUpdateByDS(null,"insert into bam_folder(id,name,code,type) values(?,?,?,2)",dirId,"数据分析应用仪表盘对象","DQ_DATAVISUAL_DBO");
					dmo.commit(null);
				}else{
					
					vos = dmo.getHashVoArrayByDS(null,"select * from bam_folder t where t.type = 2 and t.code=?", "DQ_DATAVISUAL_DBO");
					dirId = vos[0].getStringValue("id");
				}
				dataValue.put("FOLDERID", dirId);
			}
			
		}catch(Exception e){
			dmo.rollback(null);
			throw e;
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
