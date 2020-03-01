/**************************************************************************
 * $RCSfile: NewsManagerFrame.java,v $  $Revision: 1.2.10.1 $  $Date: 2009/12/04 07:07:17 $
 **************************************************************************/
package smartx.system.login.ui;

import java.awt.*;
import javax.swing.*;

import smartx.framework.common.ui.*;
import smartx.framework.metadata.ui.*;
import smartx.publics.styletemplet.ui.templet02.*;
import smartx.system.login.vo.*;


public class NewsManagerFrame extends AbstractTempletFrame02 {

    private static final long serialVersionUID = -7870282092481757876L; //

    public NewsManagerFrame() {
        super("公告栏管理");
        init();
    }

    /**
     * 获取窗口标题
     *
     * @return String
     */
    protected String getTempletTitle() {
        return "公告栏管理";
    }

    public String getTempletcode() {
        return "";
    }

    public String[] getSys_Selection_Path() {
        return null;
    }

    public String getCustomerpanel() {
        return null;
    }

    public boolean isShowsystembutton() {
        return true;
    }

    /**
     * 初始化本地变量
     */
    protected void initVars() {
        btn_save = new JButton("保存");
        btn_save_return = new JButton("保存并返回");
        btn_cancel_return = new JButton("放弃并返回");
        btn_save.setVisible(false);
        btn_return = new JButton("切换");
        btn_insert = new JButton("新增");
        btn_delete = new JButton("删除");
        btn_edit = new JButton("编辑");
        btn_view = new JButton("查看");
        btn_search = new JButton("查询");
        PUB_NEWS_CODE1_VO news_vo = new PUB_NEWS_CODE1_VO();
        table = new BillListPanel(news_vo, true, false); // 创建列表!!
        table.setLoadedFrame(this);
        table.addNovaEventListener(this); //
        table.initialize();
        table.setAllItemValue("listiseditable", NovaConstants.BILLCOMPENTEDITABLE_NONE);

        card = new BillCardPanel(news_vo); // 创建卡片!!!
        card.setLoadedFrame(this);
        card.addNovaEventListener(this); // 注册自己事件监听!!

        tablep = new JPanel();
        cardlayout = new CardLayout();

        tablep.setLayout(cardlayout);

        tablep.add(table, "table");
        tablep.add(card, "card");

    }

	public String getBsinterceptor() {
		return null;
	}

	public String getUiinterceptor() {
		return null;
	}
}
/**************************************************************************
 * $RCSfile: NewsManagerFrame.java,v $  $Revision: 1.2.10.1 $  $Date: 2009/12/04 07:07:17 $
 *
 * $Log: NewsManagerFrame.java,v $
 * Revision 1.2.10.1  2009/12/04 07:07:17  wangqi
 * *** empty log message ***
 *
 * Revision 1.2  2007/05/31 07:41:32  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 06:22:08  qilin
 * no message
 *
 * Revision 1.5  2007/03/27 08:01:39  shxch
 * *** empty log message ***
 *
 * Revision 1.4  2007/03/27 07:57:57  shxch
 * *** empty log message ***
 *
 * Revision 1.3  2007/03/05 09:59:28  shxch
 * *** empty log message ***
 *
 * Revision 1.2  2007/01/30 04:20:39  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/