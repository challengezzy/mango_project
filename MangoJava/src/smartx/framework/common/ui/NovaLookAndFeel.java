/**************************************************************************
 * $RCSfile: NovaLookAndFeel.java,v $  $Revision: 1.2.8.1 $  $Date: 2009/03/16 07:24:41 $
 **************************************************************************/
package smartx.framework.common.ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.metal.MetalLookAndFeel;

import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;

public class NovaLookAndFeel extends MetalLookAndFeel {
//public class NovaLookAndFeel extends WindowsLookAndFeel {
    private static final long serialVersionUID = -1514571451294676756L;
    Color clrBackground;

    public NovaLookAndFeel() {
        clrBackground = new Color(0xe9e9e9);
    }

    public boolean isNativeLookAndFeel() {
        return false;
    }

    public boolean isSupportedLookAndFeel() {
        return true;
    }

    public String getDescription() {
        return "The XCH Java(tm) Look and Feel";
    }

    public String getID() {
        return "XCH";
    }

    public String getName() {
        return "XCH";
    }

    protected void initComponentDefaults(UIDefaults uidefaults) {
        super.initComponentDefaults(uidefaults);
        Object aobj[] = {
            "Label.font", new FontUIResource(new Font("宋体", 0, 12)),
            "Label.background", new ColorUIResource(new Color(0xFFFFFF)),
            //"Panel.background", new ColorUIResource(new Color(207,222,237)),
            "Panel.background", new ColorUIResource(new Color(0xFFFFFF)),

            "Button.font", new FontUIResource(new Font("宋体", 0, 12)),
            "Button.background", new ColorUIResource(new Color(0xe9e9e9)),

            "TextField.font", new FontUIResource(new Font("宋体", 0, 12)),
            "TextField.inactiveBackground", new ColorUIResource(new Color(0xe9e9e9)),

            "ToggleButton.font", new FontUIResource(new Font("宋体", 0, 12)),

            "ComboBox.font", new FontUIResource(new Font("宋体", 0, 12)),
            "ComboBox.background", new ColorUIResource(new Color(0xe9e9e9)),
            "ScrollBar.background", new ColorUIResource(new Color(0xe9e9e9)),
            "OptionPane.questionDialog.titlePane.background", new ColorUIResource(new Color(0xe9e9e9)),
            "ScrollBar.background", new ColorUIResource(new Color(0xe9e9e9)),
            //"TableHeader.background", new ColorUIResource(new Color(0xe9e9e9)),
            "TableHeader.cellBorder", BorderFactory.createBevelBorder(0),
            "SplitPane.background", new ColorUIResource(new Color(0xe9e9e9)),
            "SplitPaneDivider.border", BorderFactory.createEtchedBorder(),
            "MenuBar.font", new FontUIResource(new Font("宋体", 0, 12)),
            "MenuBar.background", new Color(0xe9e9e9),
            "Menu.font", new FontUIResource(new Font("宋体", 0, 12)),
            "MenuItem.font", new FontUIResource(new Font("宋体", 0, 12)),
            "RadioButtonMenuItem.font", new FontUIResource(new Font("宋体", 0, 12)),
            "ToolBar.font", new FontUIResource(new Font("宋体", 0, 12)),
            "ToolBar.background", new Color(0xe9e9e9),
            "Tree.font", new FontUIResource(new Font("宋体", 0, 12)),

            "Viewport.background", new ColorUIResource(new Color(0xFFFFFF)),

            "TabbedPane.font", new FontUIResource(new Font("宋体", 0, 12)),
//				"TabbedPane.tabAreaBackground",  new ColorUIResource(Color.WHITE),
//		         "TabbedPane.background",  new ColorUIResource(Color.LIGHT_GRAY),
//		         "TabbedPane.light", new ColorUIResource(Color.GRAY),
//		         "TabbedPane.focus",  new ColorUIResource(Color.WHITE),
            "TabbedPane.selected", new ColorUIResource(new Color(200, 227, 255)),
//		         "TabbedPane.selectHighlight",  new ColorUIResource(Color.LIGHT_GRAY),
//		
            "ToolTip.font", new FontUIResource(new Font("宋体", 0, 12)),
            "ToolTip.background", new Color(0xFFFFD9),

        };
        uidefaults.putDefaults(aobj);
    }

    public static void main(String args[]) {
    }
}
/**************************************************************************
 * $RCSfile: NovaLookAndFeel.java,v $  $Revision: 1.2.8.1 $  $Date: 2009/03/16 07:24:41 $
 *
 * $Log: NovaLookAndFeel.java,v $
 * Revision 1.2.8.1  2009/03/16 07:24:41  wangqi
 * *** empty log message ***
 *
 * Revision 1.2  2007/05/31 07:38:23  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 05:56:36  qilin
 * no message
 *
 * Revision 1.2  2007/01/30 03:41:28  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/