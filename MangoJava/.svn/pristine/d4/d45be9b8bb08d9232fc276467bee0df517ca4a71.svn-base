/**************************************************************************
 * $RCSfile: TempletCellRender.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:39:01 $
 **************************************************************************/
package smartx.publics.styletemplet.ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

public class TempletCellRender implements TableCellRenderer {
    JTextArea field = new JTextArea();
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
        int r, int c) {
        if (c != 3) {
            field.setText(value.toString());
            field.setForeground(Color.GRAY);
            if ( ( (Boolean) table.getValueAt(r, 3)).booleanValue() && c == 0) {
                field.setForeground(Color.BLACK);
            }

            field.setBackground(new Color(240, 240, 240));
            return field;
        }
        JCheckBox box = new JCheckBox();
        box.setForeground(Color.gray);
        box.setSelected( ( (Boolean) value).booleanValue());

        return box;
    }

}
/**************************************************************************
 * $RCSfile: TempletCellRender.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:39:01 $
 *
 * $Log: TempletCellRender.java,v $
 * Revision 1.2  2007/05/31 07:39:01  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 06:14:29  qilin
 * no message
 *
 * Revision 1.2  2007/01/30 04:23:45  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/