/**
 * 
 */
package smartx.bam.bs.entityrule;

import java.util.Map;

import smartx.framework.common.bs.CommDMO;
import smartx.framework.common.vo.HashVO;
import smartx.publics.form.bs.service.VirtualColumnHandlerIFC;

/**
 * @author caohenghui
 * Mar 29, 2012
 */
public class DQEntityRuleContentColumnImpl implements VirtualColumnHandlerIFC {

	/* (non-Javadoc)
	 * @see smartx.publics.form.bs.service.VirtualColumnHandlerIFC#handler(java.util.Map)
	 */
	@Override
	public Object handler(Map<?, ?> rowMap) {
		String content = "";
		CommDMO dmo = new CommDMO();
		try{
			String mtCode = (String)rowMap.get("MTCODE");
			HashVO[] vos = dmo.getHashVoArrayByDS(null, "select * from pub_metadata_templet where code=?",mtCode);
			if(vos != null && vos.length>0){
				content = vos[0].getStringValue("content");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return content;
	}

}
