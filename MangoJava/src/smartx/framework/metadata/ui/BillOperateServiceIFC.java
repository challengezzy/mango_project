package smartx.framework.metadata.ui;

import smartx.framework.common.ui.NovaRemoteCallServiceIfc;
import smartx.framework.metadata.vo.BillVO;

/**
 * Card面板操作处理服务
 * @author James.W
 *
 */
public interface BillOperateServiceIFC extends NovaRemoteCallServiceIfc {

	/**
	 * 执行Card保存处理
	 * @param ds
	 * @param bsIntercept
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public int doCardSave(String ds, String bsIntercept, BillVO vo) throws Exception;
	
	
}
