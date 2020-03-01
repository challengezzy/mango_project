/**
 * 
 */
package smartx.publics.form.bs.interceptors.impl;

import java.util.Map;

import smartx.framework.metadata.vo.Pub_Templet_1VO;

/**
 * @author sky
 *
 */
public class UpdateMenuBfInterceptorImpl extends MenuInterceptorImpl {

	@Override
	public void doSomething(Pub_Templet_1VO templetVO,
			Map<String, Object> dataValue) throws Exception {
		String code = dataValue.get("NAME")+"";
		if(isExistMenu(code))
			throw new Exception("该菜单已存在!");
		
	}
}
