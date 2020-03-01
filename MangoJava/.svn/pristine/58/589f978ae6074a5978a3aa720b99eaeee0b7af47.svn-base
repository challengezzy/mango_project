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
 * Jul 19, 2012
 */
public class DBInsertAfFormInterceptor implements FormInterceptor {

	/* (non-Javadoc)
	 * @see smartx.publics.form.bs.service.FormInterceptor#doSomething(smartx.framework.metadata.vo.Pub_Templet_1VO, java.util.Map)
	 */
	@Override
	public void doSomething(Pub_Templet_1VO templetVO,
			Map<String, Object> dataValue) throws Exception {
		CommDMO dmo = new CommDMO();
		try{
			
			String name = (String)dataValue.get("NAME");
			String code = (String)dataValue.get("CODE");
			
			String dbCode = (String)dataValue.get("DASHBOARDCODE");
			
			String querySQL = "select count(*) cou from bam_folder t where t.type = 1 and t.code=?";
			HashVO[] vos = dmo.getHashVoArrayByDS(null, querySQL,"DQ_DATAVISUAL");
			if(vos != null){
				String dirId =  "-1";
				if(vos[0].getIntegerValue("cou") <= 0){
					dirId = dmo.getSequenceNextValByDS(null, "s_bam_folder");
					dmo.executeUpdateByDS(null,"insert into bam_folder(id,name,code,type) values(?,?,?,1)",dirId,"数据分析应用","DQ_DATAVISUAL");
					dmo.commit(null);
				}else{
					
					vos = dmo.getHashVoArrayByDS(null,"select * from bam_folder t where t.type = 1 and t.code=?", "DQ_DATAVISUAL");
					dirId = vos[0].getStringValue("id");
				}
				
				String mtCode = "MT_LAYOUT_"+dbCode;
				
				String insertSQL = "insert into bam_dashboard(id,folderid,name,code,refreshinterval,layout_mtcode) values(s_bam_dashboard.nextval,?,?,?,?,?)";
				dmo.executeUpdateByDS(null, insertSQL, dirId,name,dbCode,-1,mtCode);
				dmo.commit(null);
				
				String insertMT = "insert into pub_metadata_templet (id,name,code,owner,scope,content,type) values(s_pub_metadata_templet.nextval,?,?,?,?,?,?)";
				dmo.executeUpdateByDS(null, insertMT,name,mtCode,"admin","DQ","<dashboard />",3);
				dmo.commit(null);
				
				SmartXMetadataTempletService.getInstance().findMetadataTemplet(mtCode);
				
//				dataValue.put("DASHBOARDCODE", dbCode);
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
