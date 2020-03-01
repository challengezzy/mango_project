/**************************************************************************
 * $RCSfile: BillCardDialog.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:38:14 $
 **************************************************************************/
package smartx.framework.metadata.ui;

import java.util.*;

import java.awt.*;

public class BillCardDialog extends NovaDialog {

    private static final long serialVersionUID = 1L;

    String str_templete_code = null;

    BillCardPanel cardPanel = null;

    public BillCardDialog(Container _parent, String _code, Object[] _objs) {
        super(_parent, "快速卡片查看", 600, 400); //
        str_templete_code = _code;
        this.getContentPane().setLayout(new BorderLayout());
        cardPanel = new BillCardPanel(str_templete_code);
        cardPanel.setValue(_objs);
        cardPanel.setEditable(false);
        this.getContentPane().add(cardPanel, BorderLayout.CENTER);
        this.setVisible(true);
    }

    public BillCardDialog(Container _parent, String _code, HashMap _map) {
        super(_parent, "快速卡片查看", 600, 400); //
        str_templete_code = _code;
        this.getContentPane().setLayout(new BorderLayout());
        cardPanel = new BillCardPanel(str_templete_code);
        cardPanel.setValue(_map);
        cardPanel.setEditable(false);
        this.getContentPane().add(cardPanel, BorderLayout.CENTER);
        this.setVisible(true);
    }

}
/**************************************************************************
 * $RCSfile: BillCardDialog.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:38:14 $
 *
 * $Log: BillCardDialog.java,v $
 * Revision 1.2  2007/05/31 07:38:14  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 06:01:37  qilin
 * no message
 *
 * Revision 1.3  2007/02/10 08:51:58  shxch
 * *** empty log message ***
 *
 * Revision 1.2  2007/01/30 04:56:14  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/