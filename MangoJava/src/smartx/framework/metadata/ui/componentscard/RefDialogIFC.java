/**
 * 
 */
package smartx.framework.metadata.ui.componentscard;


import java.awt.event.WindowListener;

import smartx.framework.metadata.vo.RefItemVO;

/**
 * 参照对话框接口
 * @author James.W
 *
 */
public interface RefDialogIFC {

	/**
	 * 对话框的操作类型：确认/取消/关闭
	 * @return 0-确认，1-取消，2-关闭
	 */
    public int getCloseType(); 
    
    /**
     * 返回参照选项VO
     * @return
     */
    public RefItemVO getRefVO();

    
    public void dispose();
	
    public void addWindowListener(WindowListener wlst);
    
}
