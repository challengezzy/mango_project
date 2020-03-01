/**
 * 
 */
package smartx.bam.bs.accountset;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import smartx.framework.common.bs.CommDMO;
import smartx.framework.common.vo.HashVO;
import smartx.framework.metadata.vo.Pub_Templet_1VO;
import smartx.publics.form.bs.service.FormInterceptor;

/**
 * @author caohenghui
 *
 */
public class AccountsetInsertBfInterceptor implements FormInterceptor {
	
	private Logger	logger = Logger.getLogger( this.getClass() );

	/* (non-Javadoc)
	 * @see smartx.publics.form.bs.service.FormInterceptor#doSomething(smartx.framework.metadata.vo.Pub_Templet_1VO, java.util.Map)
	 */
	@Override
	public void doSomething(Pub_Templet_1VO arg0, Map<String, Object> dataValue){

		logger.debug( "更新元数据模板["+dataValue.get("MTCODE")+"]" );
		CommDMO dmo = new CommDMO();
		String sql = "select p.id from pub_metadata_templet p where p.code=?";
		HashVO[] vos;

		try
		{
			String mtcode =  (String)dataValue.get("MTCODE");
			String content = (String)dataValue.get("CONTENT");
			String username = (String)dataValue.get("USERNAME")==null?"sa":(String)dataValue.get("USERNAME");
			
			vos = dmo.getHashVoArrayByDS( null, sql,mtcode );
			if(vos.length > 0){
				dmo.executeUpdateClobByDS( null, "content", "pub_metadata_templet", "code='".concat( mtcode ).concat( "'" ), content );
			}else{
				mtcode = "MT_ACCOUNTSET_"+username;
				dataValue.put("MTCODE", mtcode);
				sql ="insert into pub_metadata_templet(id,name,code,owner,scope,content,type) values(S_PUB_METADATA_TEMPLET.Nextval,?,?,?,'',empty_clob(),0)";
				dmo.executeUpdateByDS( null, sql,username,mtcode,username );
				if(content != null)
					dmo.executeUpdateClobByDS( null, "content", "pub_metadata_templet", "code='".concat( mtcode ).concat( "'" ), content );
			}
			dmo.commit(null);
		}
		catch( Exception e )
		{
			logger.error("更新元数据模板["+dataValue.get("MTCODE")+"]出错!", e );
			
		}finally{
			dmo.releaseContext( null );
		}
	
		
		
		
	}

	/* (non-Javadoc)
	 * @see smartx.publics.form.bs.service.FormInterceptor#doSomething(smartx.framework.metadata.vo.Pub_Templet_1VO, java.util.List)
	 */
	@Override
	public void doSomething(Pub_Templet_1VO arg0, List<Map<String, Object>> arg1)
			throws Exception {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see smartx.publics.form.bs.service.FormInterceptor#doSomething(java.util.Map)
	 */
	@Override
	public void doSomething(Map<String, Object> arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see smartx.publics.form.bs.service.FormInterceptor#doSomething(java.util.List)
	 */
	@Override
	public void doSomething(List<Map<String, Object>> arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
