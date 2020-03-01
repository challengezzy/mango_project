/**************************************************************************
 * $RCSfile: JLabelCellRender.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:38:21 $
 **************************************************************************/
package smartx.framework.metadata.ui.componentslist;

import java.util.*;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

import smartx.framework.common.ui.*;
import smartx.framework.common.vo.FrameWorkTBUtil;
import smartx.framework.metadata.ui.*;
import smartx.framework.metadata.vo.*;
import smartx.framework.metadata.vo.jepfunctions.*;


public class JLabelCellRender extends DefaultTableCellRenderer {

    private static final long serialVersionUID = 1L;

    Pub_Templet_1_ItemVO itemVO;

    int li_aa = 0; //

    public JLabelCellRender(Pub_Templet_1_ItemVO _itemVO) {
        this.itemVO = _itemVO;
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
        int row, int column) {
        JLabel label = (JLabel)super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (itemVO.getListiseditable() != null && !itemVO.getListiseditable().equals("1")) {
            label.setForeground(new java.awt.Color(66, 66, 66));
        } else {
            label.setForeground(java.awt.Color.BLACK);
        }
        if (itemVO.getItemtype().equals("数字框")) {
            label.setHorizontalAlignment(JLabel.RIGHT);
        }

        String colorFormula = itemVO.getColorformula(); //
        if (colorFormula != null && !colorFormula.trim().equals("")) {
            BillListModel model = (BillListModel) table.getModel(); //
            HashMap rowValueMap = model.getValueAtRowWithHashMap(row); //得到一行数据
            FrameWorkTBUtil tbutil = new FrameWorkTBUtil(); //
            try {
                String str_formula = tbutil.convertFormulaMacPars(colorFormula, NovaClientEnvironment.getInstance(),
                    rowValueMap);
                JepFormulaParse formulaParse = new JepFormulaParse(JepFormulaParse.li_ui); //
                Object obj = formulaParse.execFormula(str_formula); //
                if (obj != null) { //
                    label.setForeground(getColor("" + obj)); //
                }
            } catch (Exception e) {
                e.printStackTrace();
            } //
        }

        return label;
    }

    //..
    private java.awt.Color getColor(String _html) {
        String str_r = _html.substring(0, 2);
        String str_g = _html.substring(2, 4);
        String str_b = _html.substring(4, 6);
        return new java.awt.Color(getHexIntValue(str_r), getHexIntValue(str_g), getHexIntValue(str_b));
    }

    //...
    private int getHexIntValue(String _par) {
        String str_1 = _par.substring(0, 1);
        String str_2 = _par.substring(1, 2);
        int li_1 = convertHex(str_1);
        int li_2 = convertHex(str_2);
        return li_1 * 16 + li_2;
    }

    private int convertHex(String _par) {
        int li_return = 0;
        if (_par.equals("0")) {
            li_return = 0;
        } else if (_par.equals("1")) {
            li_return = 1;
        } else if (_par.equals("2")) {
            li_return = 2;
        } else if (_par.equals("3")) {
            li_return = 3;
        } else if (_par.equals("4")) {
            li_return = 4;
        } else if (_par.equals("5")) {
            li_return = 5;
        } else if (_par.equals("6")) {
            li_return = 6;
        } else if (_par.equals("7")) {
            li_return = 7;
        } else if (_par.equals("8")) {
            li_return = 8;
        } else if (_par.equals("9")) {
            li_return = 9;
        } else if (_par.equalsIgnoreCase("A")) {
            li_return = 10;
        } else if (_par.equalsIgnoreCase("B")) {
            li_return = 11;
        } else if (_par.equalsIgnoreCase("C")) {
            li_return = 12;
        } else if (_par.equalsIgnoreCase("D")) {
            li_return = 13;
        } else if (_par.equalsIgnoreCase("E")) {
            li_return = 14;
        } else if (_par.equalsIgnoreCase("F")) {
            li_return = 15;
        }
        return li_return;
    }
}
/**************************************************************************
 * $RCSfile: JLabelCellRender.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:38:21 $
 *
 * $Log: JLabelCellRender.java,v $
 * Revision 1.2  2007/05/31 07:38:21  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 06:02:00  qilin
 * no message
 *
 * Revision 1.9  2007/03/07 02:25:02  shxch
 * *** empty log message ***
 *
 * Revision 1.8  2007/03/07 02:01:55  shxch
 * *** empty log message ***
 *
 * Revision 1.7  2007/02/27 05:15:54  shxch
 * *** empty log message ***
 *
 * Revision 1.6  2007/02/05 08:29:14  shxch
 * *** empty log message ***
 *
 * Revision 1.5  2007/02/01 02:08:44  shxch
 * *** empty log message ***
 *
 * Revision 1.4  2007/02/01 02:04:53  shxch
 * *** empty log message ***
 *
 * Revision 1.3  2007/01/31 08:55:44  shxch
 * *** empty log message ***
 *
 * Revision 1.2  2007/01/30 05:05:40  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/
