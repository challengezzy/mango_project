/**
 * 
 */
package smartx.publics.form.bs.interceptors.impl;

import java.util.List;
import java.util.Map;

import smartx.framework.common.bs.CommDMO;
import smartx.framework.common.vo.HashVO;
import smartx.framework.metadata.vo.Pub_Templet_1VO;
import smartx.publics.form.bs.service.FormInterceptor;

/**
 * @author sky
 *
 */
public class MenuInterceptorImpl implements FormInterceptor {

	/* (non-Javadoc)
	 * @see smartx.publics.form.bs.service.FormInterceptor#doSomething(smartx.framework.metadata.vo.Pub_Templet_1VO, java.util.Map)
	 */
	@Override
	public void doSomething(Pub_Templet_1VO templetVO,
			Map<String, Object> dataValue) throws Exception {
		// TODO Auto-generated method stub

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
	
	/**
	 * 是否存在菜单
	 * @param code
	 * @return
	 * @throws Exception
	 */
	protected boolean isExistMenu(String code) throws Exception{
		CommDMO dmo = new CommDMO();
		String sql ="select id from PUB_MENU where name=?";
		try{
			HashVO[] vos = dmo.getHashVoArrayByDS(null, sql,code);
			if(vos.length > 0)
				return true;
		}catch(Exception e){
			throw e;
		}finally{
			dmo.releaseContext(null);
		}
		return false;
	}

}
