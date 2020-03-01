/**************************************************************************
 * $RCSfile: QuerySelectTempletConfigFrame.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:41:32 $
 **************************************************************************/
package smartx.system.login.ui;

/**
 * 查询选择界面.用模板8定制,增加导出数据功能
 * by sxf
 */
import java.awt.event.*;

import smartx.framework.common.ui.*;
import smartx.framework.metadata.ui.*;
import smartx.framework.metadata.vo.templetvo.*;
import smartx.publics.styletemplet.ui.templet08.*;


public class QuerySelectTempletConfigFrame extends AbstractTempletFrame08 {

    private static final long serialVersionUID = -7870282092481757878L; //

    public QuerySelectTempletConfigFrame() {
        super("查询选择框");
        init();
    }

    public String getTempletcode() {
        return "PUB_QUERYTEMPLET";
    }

    public String[] getSys_Selection_Path() {
        return new String[] {"查询选择"};
    }

    public String getUiinterceptor() {
        return null;
    }

    public String getBsinterceptor() {
        return null;
    }

    public boolean isShowsystembutton() {
        return true;
    }

    public String getChildTableFK() {
        return "PK_PUB_QUERYTEMPLET";
    }

    public String getChildTablePK() {
        return "PK_PUB_QUERYTEMPLET_ITEM";
    }

    public String getChildTableTempletcode() {
        return "PUB_QUERYTEMPLET_ITEM";
    }

    public String getParentTablePK() {
        return "PK_PUB_QUERYTEMPLET";
    }

    public String getParentTableTempletcode() {
        return "PUB_QUERYTEMPLET";
    }

    // 自定义面板.用于导出数据
    public String getCustomerpanel() {
        return "smartx.system.login.ui.QuerySelectTempletExportPanel";
    }

    public BillListPanel getParent_BillListPanel() {
        if (parent_BillListPanel != null) {
            return parent_BillListPanel;
        }
        parent_BillListPanel = new BillListPanel(new PUB_QUERYTEMPLET_VO());
        parent_BillListPanel.setLoadedFrame(this);
        parent_BillListPanel.setAllItemValue("listiseditable",
                                             NovaConstants.BILLCOMPENTEDITABLE_NONE);
        parent_BillListPanel.initialize();
        parent_BillListPanel.getTable().addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent arg0) {
                if (arg0.getButton() == MouseEvent.BUTTON1) {
                    onChildRefresh();
                }
            }

        });

        return parent_BillListPanel;
    }

    public BillCardPanel getParent_BillCardPanel() {
        if (parent_BillCardPanel != null) {
            return parent_BillCardPanel;
        }
        parent_BillCardPanel = new BillCardPanel(new PUB_QUERYTEMPLET_VO());
        parent_BillCardPanel.setLoadedFrame(this);
        return parent_BillCardPanel;
    }

    public BillListPanel getChild_BillListPanel() {
        if (child_BillListPanel != null) {
            return child_BillListPanel;
        }
        child_BillListPanel = new BillListPanel(new PUB_QUERYTEMPLET_ITEM_VO());
        child_BillListPanel.setLoadedFrame(this);
        child_BillListPanel
            .setCustomerNavigationJPanel(getChildCustomerJPanel());
        child_BillListPanel.setAllItemValue("listiseditable",
                                            NovaConstants.BILLCOMPENTEDITABLE_NONE);
        child_BillListPanel.initialize();
        getChildCustomerJPanel().setVisible(false);
        return child_BillListPanel;
    }

}
/**************************************************************************
 * $RCSfile: QuerySelectTempletConfigFrame.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:41:32 $
 *
 * $Log: QuerySelectTempletConfigFrame.java,v $
 * Revision 1.2  2007/05/31 07:41:32  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 06:22:08  qilin
 * no message
 *
 * Revision 1.4  2007/03/05 09:59:29  shxch
 * *** empty log message ***
 *
 * Revision 1.3  2007/02/27 08:41:37  sunxf
 * 修改平台管理中引用类的包名称
 *
 * Revision 1.2  2007/01/30 04:20:39  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/