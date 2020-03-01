/**
 * 
 */
package smartx.framework.metadata.ui;

import smartx.framework.metadata.vo.Pub_Templet_1VO;

/**
 * 响应QuickQueryPanel的检索动作的控件接口
 * @author James.W
 *
 */
public interface QuickQueryActionIFC {
	
	/**
	 * 获得对应的元模板定义对象
	 * @return
	 */
	public Pub_Templet_1VO getTempletVO();
	
	/**
	 * 执行检索
	 * @param sql
	 */
	public void doQuery(String sql);
	
	/**
	 * 
	 * @param subSql
	 * @return 完整检索语句
	 */
	public String buildSql(String subSql);

}
