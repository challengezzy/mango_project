/**
 * 
 */
package smartx.bam.bs.bvmanager;

import java.util.List;
import java.util.Map;

import smartx.framework.common.utils.StringUtil;
import smartx.framework.metadata.vo.ComBoxItemVO;
import smartx.framework.metadata.vo.Pub_Templet_1VO;
import smartx.publics.cep.vo.CEPConst;
import smartx.publics.form.bs.service.FormInterceptor;

/**
 * @author sky
 * Description 
 */
public class BvInsertBfFormInterceptor implements FormInterceptor
{
	
	BusinessViewManager bvm = new BusinessViewManager();
	@Override
	public void doSomething( Pub_Templet_1VO templetVO, Map<String, Object> dataValue ) throws Exception
	{
		String code = dataValue.get("CODE") + "";
		if(bvm.isExistBv(code)){
			throw new Exception("该业务视图已存在！");
		}else{
			String templeteCode = (String) dataValue.get("FORMSERVICE_TEMPLETCODE");
			if (!StringUtil.isEmpty(templeteCode)
					&& templeteCode.trim().equalsIgnoreCase("T_BAM_BUSINESSVIEW")) {
				bvm.deployStreamModule(CEPConst.DEFAULTPROVIDERNAME_CEP,
						code, dataValue.get("SOURCE")==null?"":dataValue.get("SOURCE")+"",
						dataValue.get("CREATOR") + "",
						BusinessViewManager.INSERT_TYPE);
				dataValue.put(
						"STREAMMODULENAME",
						BusinessViewManager.MODULENAME_PREFIX.concat(code));
				if(dataValue.get( "ISPERSISTVIEWDATA" ) instanceof ComBoxItemVO){
					String id = ((ComBoxItemVO)dataValue.get( "ISPERSISTVIEWDATA" )).getId();
					int type = Integer.parseInt( id );
					if(type == 1)
						BusinessViewManager.bvTableMap.put(dataValue.get( "STREAMWINDOWNAME" )+"",dataValue.get( "CODE" )+"");
				}
			}
		}
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
