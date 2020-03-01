/**
 * 
 */
package smartx.publics.form.bs.interceptors.datatask;

import java.util.List;
import java.util.Map;

import smartx.framework.common.bs.CommDMO;
import smartx.framework.metadata.vo.Pub_Templet_1VO;
import smartx.publics.form.bs.service.FormInterceptor;
import smartx.publics.form.bs.service.SmartXFormService;
import smartx.publics.metadata.MetadataTempletUtil;

/**
 * @author sky
 * Description 
 */
public class DataTaskDeleteAfter implements FormInterceptor
{
	DataTaskFormService dts = new DataTaskFormService();

	/* (non-Javadoc)
	 * @see smartx.publics.form.bs.service.FormInterceptor#doSomething(smartx.framework.metadata.vo.Pub_Templet_1VO, java.util.Map)
	 */
	@Override
	public void doSomething( Pub_Templet_1VO templetVO, Map<String, Object> dataValue ) throws Exception
	{
		//TODO Auto-generated method stub
	}

	/* (non-Javadoc)
	 * @see smartx.publics.form.bs.service.FormInterceptor#doSomething(smartx.framework.metadata.vo.Pub_Templet_1VO, java.util.List)
	 */
	@Override
	public void doSomething( Pub_Templet_1VO templetVO, List<Map<String, Object>> dataValueList ) throws Exception
	{
		//数据任务模板删除后，删除对应的元数据没按
		for (Map<String, Object> dataValue : dataValueList) {
			String flag = (String) dataValue.get(SmartXFormService.KEYNAME_MODIFYFLAG);
			String mtcode = (String)dataValue.get("MTCODE");
			if ("delete".equalsIgnoreCase(flag.trim())){
				MetadataTempletUtil.deleteMetadataTemplet(mtcode);
			}
		}

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
