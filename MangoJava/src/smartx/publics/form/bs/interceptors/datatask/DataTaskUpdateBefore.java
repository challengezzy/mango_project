/**
 * 
 */
package smartx.publics.form.bs.interceptors.datatask;

import java.util.List;
import java.util.Map;

import smartx.framework.metadata.vo.Pub_Templet_1VO;
import smartx.publics.form.bs.service.FormInterceptor;
import smartx.publics.metadata.MetadataTempletUtil;
import smartx.system.common.constant.CommonSysConst;

/**
 * @author sky
 * Description 
 */
public class DataTaskUpdateBefore implements FormInterceptor
{

	/* (non-Javadoc)
	 * @see smartx.publics.form.bs.service.FormInterceptor#doSomething(smartx.framework.metadata.vo.Pub_Templet_1VO, java.util.Map)
	 */
	@Override
	public void doSomething( Pub_Templet_1VO templetVO, Map<String, Object> dataValue ) throws Exception
	{
		String code = dataValue.get( "CODE" ) +"";
		dataValue.put( "MTTYPE", CommonSysConst.MT_TYPE_DATATASK);
		dataValue.put( "MTCODE", DataTaskFormService.METADATA_PREFIX.concat( code ));
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
