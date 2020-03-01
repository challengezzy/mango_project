/**************************************************************************
 * $RCSfile: CheckBoxCellRender.java,v $  $Revision: 1.2.8.1 $  $Date: 2010/01/20 09:58:46 $
 **************************************************************************/
package smartx.framework.metadata.ui.componentslist;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

/**
 * 选择框展现
 * @author James.W
 *
 */
public class CheckBoxCellRender extends JCheckBox implements TableCellRenderer {
    private static final long serialVersionUID = -6898489947702539815L;

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
        int rowIndex, int vColIndex) {
        
        this.setHorizontalAlignment(SwingConstants.CENTER);
        if (isSelected) {
            this.setBackground(table.getSelectionBackground());
        } else {
            this.setBackground(Color.WHITE);
        }
        
        if (value != null && value.toString().equals("Y")) {
            this.setSelected(true);
        } else {
            this.setSelected(false);
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
 * $RCSfile: CheckBoxCellRender.java,v $  $Revision: 1.2.8.1 $  $Date: 2010/01/20 09:58:46 $
 *
 * $Log: CheckBoxCellRender.java,v $
 * Revision 1.2.8.1  2010/01/20 09:58:46  wangqi
 * *** empty log message ***
 *
 * Revision 1.2  2007/05/31 07:38:21  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 06:02:00  qilin
 * no message
 *
 * Revision 1.2  2007/01/30 05:05:40  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/