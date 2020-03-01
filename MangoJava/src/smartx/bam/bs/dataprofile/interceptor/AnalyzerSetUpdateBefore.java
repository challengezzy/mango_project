/**
 * 
 */
package smartx.bam.bs.dataprofile.interceptor;

import java.util.List;
import java.util.Map;

import smartx.bam.bs.dataprofile.AnalyzerSetTaskService;
import smartx.framework.metadata.vo.Pub_Templet_1VO;
import smartx.publics.form.bs.service.FormInterceptor;
import smartx.publics.metadata.MetadataTempletUtil;
import smartx.system.common.constant.CommonSysConst;

/**
 * @author ZZY
 * Description 
 */
public class AnalyzerSetUpdateBefore implements FormInterceptor
{

	/* (non-Javadoc)
	 * @see smartx.publics.form.bs.service.FormInterceptor#doSomething(smartx.framework.metadata.vo.Pub_Templet_1VO, java.util.Map)
	 */
	@Override
	public void doSomething( Pub_Templet_1VO templetVO, Map<String, Object> dataValue ) throws Exception
	{
		String code = dataValue.get( "CODE" ) +"";
		dataValue.put( "MTTYPE", CommonSysConst.MT_TYPE_ANALYZERSET);
		dataValue.put( "MTCODE", AnalyzerSetTaskService.METADATA_PREFIX.concat( code ));
		dataValue.put("DATATASKCODE", AnalyzerSetTaskService.DATATASK_PREFIX+ code);
		MetadataTempletUtil.updateMetadataTemplet( dataValue.get( "MTCODE" )+"", dataValue.get( "CONTENT" )+"",dataValue.get( "NAME" )+"", dataValue.get( "OWNER" )+"",dataValue.get( "MTTYPE" )+"" );
	}

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
