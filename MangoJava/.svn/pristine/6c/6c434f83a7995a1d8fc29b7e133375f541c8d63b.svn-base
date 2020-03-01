/**
 * 
 */
package smartx.bam.bs.dashboardobj;

import java.util.List;
import java.util.Map;

import smartx.bam.bs.common.MetadataTempletManager;
import smartx.bam.vo.SysConst;
import smartx.framework.metadata.vo.ComBoxItemVO;
import smartx.framework.metadata.vo.Pub_Templet_1VO;
import smartx.publics.form.bs.service.FormInterceptor;

/**
 * @author sky
 * Description 
 */
public class DboInsertBfFormInterceptor implements FormInterceptor
{
	private DashboardObjManager dbom = new DashboardObjManager();
	
	private MetadataTempletManager mtm = new MetadataTempletManager();


	@Override
	public void doSomething( Pub_Templet_1VO templetVO, Map<String, Object> dataValue ) throws Exception
	{
		String code = dataValue.get( "CODE" ) +"";
		if(dbom.isExistDashboardObj(code))
			throw new Exception("仪表盘对象已存在！");
		else{
			if(dataValue.get( "TYPE" ) instanceof ComBoxItemVO){
				String id = ((ComBoxItemVO)dataValue.get( "TYPE" )).getId();
				int type = Integer.parseInt( id );
				dataValue.put( "MTTYPE", dbom.getMtTypeByChartType( type ) );
			}
			dataValue.put( "MTCODE", SysConst.DO_METADATA_PREFIX.concat( code ));
			mtm.updateMetadataTemplet( dataValue.get( "MTCODE" )+"", dataValue.get( "CONTENT" )+"",dataValue.get( "NAME" )+"", dataValue.get( "OWNER" )+"",dataValue.get( "MTTYPE" )+"" );
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
