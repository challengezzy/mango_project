/**
 * 
 */
package smartx.bam.bs.dataanalyze;

import java.util.List;
import java.util.Map;

import smartx.framework.common.bs.CommDMO;
import smartx.framework.common.utils.StringUtil;
import smartx.framework.metadata.vo.Pub_Templet_1VO;
import smartx.publics.form.bs.service.FormInterceptor;
import smartx.publics.metadata.bs.service.SmartXMetadataTempletService;

/**
 * @author caohenghui
 * Jul 19, 2012
 */
public class DboAfFormInterceptor implements FormInterceptor {

	/* (non-Javadoc)
	 * @see smartx.publics.form.bs.service.FormInterceptor#doSomething(smartx.framework.metadata.vo.Pub_Templet_1VO, java.util.Map)
	 */
	@Override
	public void doSomething(Pub_Templet_1VO templetVO,
			Map<String, Object> dataValue) throws Exception {
		
		CommDMO dmo = new CommDMO();
		try{
			
			String mode = (String)dataValue.get("FORMSERVICE_MODIFYFLAG");
			
			String name = (String)dataValue.get("NAME");
			String mtCode = (String)dataValue.get("MTCODE");
			String content = (String)dataValue.get("CONTENT");
			
			if(mode.equalsIgnoreCase("insert")){
				String insertMT = "insert into pub_metadata_templet (id,name,code,owner,scope,content,type) values(s_pub_metadata_templet.nextval,?,?,?,?,?,?)";
				dmo.executeUpdateByDS(null, insertMT,name,mtCode,"admin","DQ",StringUtil.isEmpty(content)?"<chart />":content,4);
				dmo.commit(null);
				SmartXMetadataTempletService.getInstance().findMetadataTemplet(mtCode);
			}else if(mode.equalsIgnoreCase("update")){
				dmo.executeUpdateClobByDS(null, "content", "pub_metadata_templet", " code='"+mtCode+"'", content);
				dmo.commit(null);
				SmartXMetadataTempletService.getInstance().resetCacheByMtCode(mtCode);
			}else if(mode.equalsIgnoreCase("delete")){
				dmo.executeUpdateByDS(null, "delete from pub_metadata_templet where code = ?", mtCode);
				dmo.commit(null);
				SmartXMetadataTempletService.getInstance().resetCacheByMtCode(mtCode);
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
