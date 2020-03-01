/**************************************************************************
 * $RCSfile: RowNumberCellRender.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:38:22 $
 **************************************************************************/
package smartx.framework.metadata.ui.componentslist;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

import smartx.framework.metadata.vo.*;


public class RowNumberCellRender extends JLabel implements TableCellRenderer {

    private static final long serialVersionUID = 1L;

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
        int rowIndex, int vColIndex) {
        this.setOpaque(true);
        if (isSelected) {
            this.setBackground(table.getSelectionBackground());
        } else {
            this.setBackground(new Color(240, 240, 240));
        }

        if (value instanceof RowNumberItemVO) {
            RowNumberItemVO valueVO = (RowNumberItemVO) value; //
            if (valueVO != null) {
                if (valueVO.getState().equals("INIT")) {
                    this.setForeground(java.awt.Color.BLACK);
                    this.setText("" + (rowIndex + 1));
                } else if (valueVO.getState().equals("INSERT")) {
                    this.setForeground(java.awt.Color.GREEN);
                    this.setText("*" + (rowIndex + 1));
                } else if (valueVO.getState().equals("UPDATE")) {
                    this.setForeground(java.awt.Color.BLUE);
                    this.setText("*" + (rowIndex + 1));
                } else {
                    this.setForeground(java.awt.Color.BLACK);
                    this.setText("" + (rowIndex + 1));
                }
            } else {
                this.setForeground(java.awt.Color.BLACK);
                this.setText("" + (rowIndex + 1));
            }

            this.setHorizontalAlignment(SwingConstants.RIGHT);

            this.setToolTipText(value.toString());
        } else if (value instanceof String) {
            this.setText("" + (rowIndex + 1));
        }

        return this;
    }

    public void validate() {
    }

    public void revalidate() {
    }

    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
    }

    public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {
    }

}
/**************************************************************************
 * $RCSfile: RowNumberCellRender.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:38:22 $
 *
 * $Log: RowNumberCellRender.java,v $
 * Revision 1.2  2007/05/31 07:38:22  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 06:01:59  qilin
 * no message
 *
 * Revision 1.2  2007/01/30 05:05:40  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/