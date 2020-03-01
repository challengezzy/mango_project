/**
 * 
 */
package smartx.publics.form.bs.service;

import java.util.List;
import java.util.Map;

import smartx.framework.metadata.vo.Pub_Templet_1VO;

/**
 * @author sky
 * Description 元原模板保存拦截器
 */
/**
 * @author caohenghui
 *
 */
public interface FormInterceptor
{
	/**
	 * 针对卡片的拦截器实现方法
	 * @param templetVO 元原模板对象
	 * @param dataValue 要更新的数据对象
	 * @throws Exception
	 */
	void doSomething( Pub_Templet_1VO templetVO, Map<String, Object> dataValue) throws Exception;
	
	
	/**
	 * 针对列表的拦截器实现方法
	 * @param templetVO 元原模板对象
	 * @param dataValueList 要更新的数据对象列表
	 * @throws Exception
	 */
	void doSomething( Pub_Templet_1VO templetVO, List<Map<String,Object>> dataValueList) throws Exception;
	
	/**
	 * 该方法主要针对BillTreePanel,不过BillCardPanel,BillListPanel也可以实倒此方法,只需要把参数封装到map里再解析出来即可
	 * @param map 此对像是从客户端传过来的,里面可以封装任何形式的参数
	 * @throws Exception
	 */
	void doSomething(Map<String,Object> map) throws Exception;
	
	/**
	 * 对于批量更新的方法,建议实现此方法
	 * @param dataValueList
	 * @throws Exception
	 */
	void doSomething(List<Map<String,Object>> dataValueList) throws Exception;
}
