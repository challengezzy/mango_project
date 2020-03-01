/**
 * 
 */
package smartx.bam.bs.entitymodel;

import java.util.List;
import java.util.Map;

import smartx.bam.bs.common.MetadataTempletManager;
import smartx.framework.metadata.vo.Pub_Templet_1VO;
import smartx.publics.form.bs.service.FormInterceptor;

/**
 * @author sky
 * 
 */
public class EmUpdateBfFormInterceptor implements FormInterceptor {
	
	private MetadataTempletManager mtm = new MetadataTempletManager();
	@Override
	public void doSomething(Pub_Templet_1VO templetVO,
			Map<String, Object> dataValue) throws Exception {
		mtm.updateMetadataTemplet(dataValue.get("MTCODE") + "",
				dataValue.get("CONTENT") + "", dataValue.get("NAME") + "",
				dataValue.get("OWNER") + "", dataValue.get("MTTYPE") + "");

	}

	@Override
	public void doSomething(Pub_Templet_1VO templetVO,
			List<Map<String, Object>> dataValueList) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void doSomething(Map<String, Object> map) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void doSomething(List<Map<String, Object>> dataValueList)
			throws Exception {
		// TODO Auto-generated method stub

	}

}
