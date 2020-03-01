/**
 * 
 */
package smartx.bam.bs.scenariomnager;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import smartx.framework.common.vo.NovaLogger;
import smartx.framework.metadata.vo.Pub_Templet_1VO;
import smartx.framework.metadata.vo.RefItemVO;
import smartx.publics.form.bs.service.FormInterceptor;

/**
 * @author caohenghui
 *
 */
public class OrderUpdateBfInterceptor implements FormInterceptor {
	
	protected Logger logger = NovaLogger.getLogger(this.getClass());

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

	/* (non-Javadoc)
	 * @see smartx.publics.form.bs.service.FormInterceptor#doSomething(smartx.framework.metadata.vo.Pub_Templet_1VO, java.util.Map)
	 */
	@Override
	public void doSomething(Pub_Templet_1VO arg0, Map<String, Object> dataValue)
			throws Exception {

		
		try{
			
			RefItemVO vo = (RefItemVO)dataValue.get("OBJECTNAME");
			
			String tempcn = vo.getCode();
			
			RefItemVO typeVo = new RefItemVO();
			
			String type = "0";
			
			if(tempcn.trim().equalsIgnoreCase("用户")){
				type  = "0";
			}else if(tempcn.trim().equalsIgnoreCase("角色")){
				type  = "1";
			}else if(tempcn.trim().equalsIgnoreCase("工位")){
				type  = "2";
			}
			
			typeVo.setId(type);
			typeVo.setCode(tempcn);
			typeVo.setName(tempcn);
			
			dataValue.put("TYPE", typeVo);
			
		}catch(Exception e){
			logger.debug("",e);
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

}
