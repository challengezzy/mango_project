/**************************************************************************
 * $RCSfile: MenuCommdURLPathPanel.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:39:01 $
 **************************************************************************/
package smartx.publics.styletemplet.ui;

import java.awt.*;
import javax.swing.*;

import smartx.framework.common.vo.*;


public class MenuCommdURLPathPanel extends AbstractTempletRefPars {

    private static final long serialVersionUID = -6147862007359706108L;

    JTextField text = null;

    public MenuCommdURLPathPanel() {
        this.setLayout(new BorderLayout());
        this.add(getCenterPanel(""));
    }

    public MenuCommdURLPathPanel(String _text) {
        this.setLayout(new BorderLayout());
        this.add(getCenterPanel(_text));

    }

    private JPanel getCenterPanel(String _text) {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        JLabel label = new JLabel("纯对JFrame的路径:", SwingConstants.RIGHT); //
        label.setPreferredSize(new Dimension(150, 20));

        text = new JTextField(_text);
        text.setPreferredSize(new Dimension(350, 20));

        panel.add(label); //
        panel.add(text); //

        return panel;
    }

    public VectorMap getParameters() {
        VectorMap map = new VectorMap(); //		
        map.put("frame", text.getText().trim());
        return map;
    }

    public void stopEdit() {

    }

    protected String bsInformation() {
        return null;
    }

    protected String uiInformation() {
        return null;
    }

}
/**************************************************************************
 * $RCSfile: MenuCommdURLPathPanel.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:39:01 $
 *
 * $Log: MenuCommdURLPathPanel.java,v $
 * Revision 1.2  2007/05/31 07:39:01  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 06:14:29  qilin
 * no message
 *
 * Revision 1.3  2007/02/27 06:57:19  shxch
 * *** empty log message ***
 *
 * Revision 1.2  2007/01/30 04:23:45  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/