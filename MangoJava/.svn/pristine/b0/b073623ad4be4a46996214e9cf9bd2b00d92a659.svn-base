/**
 * 
 */
package smartx.bam.bs.dashboard;

import java.util.List;
import java.util.Map;

import smartx.bam.bs.common.MetadataTempletManager;
import smartx.bam.vo.SysConst;
import smartx.framework.metadata.vo.Pub_Templet_1VO;
import smartx.publics.form.bs.service.FormInterceptor;
import smartx.system.common.constant.CommonSysConst;

/**
 * @author sky
 * Description 
 */
public class DbInsertBfFormInterceptor implements FormInterceptor
{
	private MetadataTempletManager mtm = new MetadataTempletManager();
	
	private DashboardManager dbm = new DashboardManager();
	/* (non-Javadoc)
	 * @see smartx.publics.form.bs.service.FormInterceptor#doSomething(smartx.framework.metadata.vo.Pub_Templet_1VO, java.util.Map)
	 */
	@Override
	public void doSomething( Pub_Templet_1VO templetVO, Map<String, Object> dataValue ) throws Exception
	{
		String code = String.valueOf(dataValue.get( "CODE" ) );
		if(dbm.isExistDashboard(code))
			throw new Exception("仪表盘已存在!");
		else{
			dataValue.put( "LAYOUT_MTCODE", SysConst.DB_METADATA_PREFIX.concat( code ));
			dataValue.put( "MTTYPE",CommonSysConst.MT_TYPE_DASHBOARD );
			mtm.updateMetadataTemplet( dataValue.get( "LAYOUT_MTCODE" )+"", dataValue.get( "CONTENT" )+"",dataValue.get( "NAME" )+"", dataValue.get( "OWNER" )+"",dataValue.get( "MTTYPE" )+"" );
		}
	}

	/* (non-Javadoc)
	 * @see smartx.publics.form.bs.service.FormInterceptor#doSomething(smartx.framework.metadata.vo.Pub_Templet_1VO, java.util.List)
	 */
	@Override
	public void doSomething( Pub_Templet_1VO templetVO, List<Map<String, Object>> dataValueList ) throws Exception
	{
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
