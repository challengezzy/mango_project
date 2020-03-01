/**************************************************************************
 * $RCSfile: XchTestFrame.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:38:20 $
 **************************************************************************/
package smartx.framework.metadata.ui.componentscard;

import java.math.*;
import java.text.*;

import java.awt.*;
import javax.swing.*;
import javax.swing.text.*;

public class XchTestFrame extends JFrame {

    public XchTestFrame() {
        this.setSize(500, 300); //

        this.getContentPane().setLayout(new FlowLayout());

        JFormattedTextField tft3 = new JFormattedTextField(new BigDecimal("123.4567"));
        tft3.setPreferredSize(new Dimension(250, 20)); //
        DefaultFormatter fmt = new NumberFormatter(new DecimalFormat("#.0###############"));
        fmt.setValueClass(tft3.getValue().getClass());
        DefaultFormatterFactory fmtFactory = new DefaultFormatterFactory(fmt, fmt, fmt);
        tft3.setFormatterFactory(fmtFactory);

        // Retrieve the value from the text field
        BigDecimal bigValue = (BigDecimal) tft3.getValue();

        this.getContentPane().add(tft3);
        this.setVisible(true); //

    }

    public static void main(String[] args) {
        new XchTestFrame();
    }
}
/**************************************************************************
 * $RCSfile: XchTestFrame.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:38:20 $
 *
 * $Log: XchTestFrame.java,v $
 * Revision 1.2  2007/05/31 07:38:20  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 06:01:05  qilin
 * no message
 *
 * Revision 1.2  2007/01/30 05:14:31  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/