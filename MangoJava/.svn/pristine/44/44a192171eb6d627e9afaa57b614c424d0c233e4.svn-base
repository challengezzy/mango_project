/**
 * 
 */
package smartx.bam.bs.entitymodel;

import java.util.List;
import java.util.Map;

import smartx.bam.bs.common.MetadataTempletManager;
import smartx.bam.vo.SysConst;
import smartx.framework.metadata.vo.Pub_Templet_1VO;
import smartx.publics.form.bs.service.FormInterceptor;
import smartx.system.common.constant.CommonSysConst;

/**
 * @author sky
 *
 */
public class EmInsertBfFormInterceptor implements FormInterceptor {
	
	private MetadataTempletManager mtm = new MetadataTempletManager();
	
	private EntityModelManager emm = new EntityModelManager();
	/* (non-Javadoc)
	 * @see smartx.publics.form.bs.service.FormInterceptor#doSomething(smartx.framework.metadata.vo.Pub_Templet_1VO, java.util.Map)
	 */
	@Override
	public void doSomething(Pub_Templet_1VO templetVO,
			Map<String, Object> dataValue) throws Exception {
		String code = dataValue.get( "CODE" ) +"";
		if(emm.isExistEntityModel(code))
			throw new Exception("领域实体模型已存在！");
		else{
			dataValue.put( "MTTYPE",CommonSysConst.MT_TYPE_ENTITYMODEL );
			dataValue.put( "MTCODE",SysConst.EM_METADATA_PREFIX.concat( code ));
			mtm.updateMetadataTemplet( dataValue.get( "MTCODE" )+"", dataValue.get( "CONTENT" )+"",dataValue.get( "NAME" )+"", dataValue.get( "OWNER" )+"",dataValue.get( "MTTYPE" )+"" );
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
