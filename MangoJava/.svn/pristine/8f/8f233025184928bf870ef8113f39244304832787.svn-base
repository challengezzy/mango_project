/**
 * 
 */

package smartx.bam.bs.bvmanager;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import smartx.framework.common.utils.StringUtil;
import smartx.framework.common.vo.NovaLogger;
import smartx.framework.metadata.vo.Pub_Templet_1VO;
import smartx.publics.cep.bs.service.SmartXCEPService;
import smartx.publics.cep.vo.CEPConst;
import smartx.publics.form.bs.service.FormInterceptor;
import smartx.publics.form.bs.service.SmartXFormService;

/**
 * @author sky Description
 */
public class BvDeleteAfFormInterceptor implements FormInterceptor
{
	private Logger				logger			= NovaLogger.getLogger( this.getClass() );

	private SmartXCEPService	sxCepservice	= new SmartXCEPService();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * smartx.publics.form.bs.service.FormInterceptor#doSomething(smartx.framework
	 * .metadata.vo.Pub_Templet_1VO, java.util.Map)
	 */
	@Override
	public void doSomething( Pub_Templet_1VO templetVO, Map<String, Object> dataValue ) throws Exception
	{
		try
		{
			String templeteCode = templetVO.getTempletcode();
			if(!StringUtil.isEmpty(templeteCode)&&templeteCode.trim().equalsIgnoreCase("T_BAM_BUSINESSVIEW")){
				String streammodulename = (String) dataValue.get( "STREAMMODULENAME" );
				sxCepservice.undeployStreamModule( CEPConst.DEFAULTPROVIDERNAME_CEP, streammodulename );
			}
		}
		catch( Exception e )
		{
			logger.debug( "卸载模块出现异常!", e );
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * smartx.publics.form.bs.service.FormInterceptor#doSomething(smartx.framework
	 * .metadata.vo.Pub_Templet_1VO, java.util.List)
	 */
	@Override
	public void doSomething( Pub_Templet_1VO templetVO, List<Map<String, Object>> dataValueList ) throws Exception
	{
		try
		{
			for( Map<String, Object> dataValue : dataValueList )
			{
				String flag = (String) dataValue.get( SmartXFormService.KEYNAME_MODIFYFLAG );
				String streammodulename = (String) dataValue.get( "STREAMMODULENAME" );
				String streamwindowname = (String) dataValue.get("STREAMWINDOWNAME");
				if( "delete".equalsIgnoreCase( flag.trim() ) ){
					sxCepservice.undeployStreamModule( CEPConst.DEFAULTPROVIDERNAME_CEP, streammodulename );
					BusinessViewManager.bvTableMap.remove(streamwindowname);
				}
			}

		}
		catch( Exception e )
		{
			logger.debug( "卸载模块出现异常!", e );
		}

	}

	/* (non-Javadoc)
	 * @see smartx.publics.form.bs.service.FormInterceptor#doSomething(java.util.Map)
	 */
	@Override
	public void doSomething(Map<String, Object> arg0) throws Exception {


	}

	/* (non-Javadoc)
	 * @see smartx.publics.form.bs.service.FormInterceptor#doSomething(java.util.List)
	 */
	@Override
	public void doSomething(List<Map<String, Object>> arg0) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("");
	}

}
