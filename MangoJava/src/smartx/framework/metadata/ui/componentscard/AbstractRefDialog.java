/**************************************************************************
 * $RCSfile: AbstractRefDialog.java,v $  $Revision: 1.2.8.1 $  $Date: 2010/01/20 09:53:30 $
 **************************************************************************/
package smartx.framework.metadata.ui.componentscard;


import java.awt.Container;
import java.awt.Frame;
import java.util.HashMap;

import javax.swing.JOptionPane;

import smartx.framework.metadata.ui.NovaDialog;
import smartx.framework.metadata.vo.RefItemVO;


/**
 * 抽象参照窗口
 * @author James.W
 *
 */
public abstract class AbstractRefDialog extends NovaDialog implements RefDialogIFC {
    private String str_initRefID = null;

    protected HashMap _map = null;

    /**
     * 对话框标题
     */
    public abstract String getTitle();
    /**
	 * 对话框的操作类型：确认/取消/关闭
	 * @return 0-确认，1-取消，2-关闭
	 */
    public abstract int getCloseType(); 
    /**
     * 返回参照选项值
     * @return
     */
    public abstract RefItemVO getRefVO();

    public AbstractRefDialog(Container _parent, String _initRefID, HashMap _map) {
        super(_parent);
        this.str_initRefID = _initRefID;
        this._map = _map; //
        this.setTitle(getTitle()); //
        this.setSize(getInitWidth(), getInitHeight());
        this.setModal(true); //

        Frame frame = JOptionPane.getFrameForComponent(_parent);
        double ld_width = frame.getSize().getWidth();
        double ld_height = frame.getSize().getHeight();
        double ld_x = frame.getLocation().getX();
        double ld_y = frame.getLocation().getY();

        this.setSize(getInitWidth(), getInitHeight()); //

        double ld_thisX = ld_x + ld_width / 2 - getInitWidth() / 2; //
        double ld_thisY = ld_y + ld_height / 2 - getInitHeight() / 2; //
        if (ld_thisX < 0) {
            ld_thisX = 0;
        }

        if (ld_thisY < 0) {
            ld_thisY = 0;
        }

        this.setLocation(new Double(ld_thisX).intValue(), new Double(ld_thisY).intValue()); //
    }

    public int getInitWidth() {
        return 500;
    }

    public int getInitHeight() {
        return 300;
    }

    public final String getInitRefID() {
        return str_initRefID; 
    }

}
/**************************************************************************
 * $RCSfile: AbstractRefDialog.java,v $  $Revision: 1.2.8.1 $  $Date: 2010/01/20 09:53:30 $
 *
 * $Log: AbstractRefDialog.java,v $
 * Revision 1.2.8.1  2010/01/20 09:53:30  wangqi
 * *** empty log message ***
 *
 * Revision 1.2  2007/05/31 07:38:17  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 06:01:08  qilin
 * no message
 *
 * Revision 1.3  2007/02/10 08:51:57  shxch
 * *** empty log message ***
 *
 * Revision 1.2  2007/01/30 05:14:30  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/