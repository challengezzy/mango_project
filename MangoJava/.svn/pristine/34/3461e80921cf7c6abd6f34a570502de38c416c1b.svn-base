/***********************************************************************
 * $RCSfile: NovaDeskTopPane.java,v $  $Revision: 1.6.2.3 $  $Date: 2009/09/03 07:07:17 $
 * $Log: NovaDeskTopPane.java,v $
 * Revision 1.6.2.3  2009/09/03 07:07:17  wangqi
 * *** empty log message ***
 *
 * Revision 1.6.2.2  2009/08/14 09:23:53  wangqi
 * *** empty log message ***
 *
 * Revision 1.6.2.1  2009/03/16 07:24:47  wangqi
 * *** empty log message ***
 *
 * Revision 1.6  2007/11/29 03:59:25  wangqi
 * *** empty log message ***
 *
 * Revision 1.5  2007/11/09 05:55:49  qilin
 * no message
 *
 * Revision 1.4  2007/06/08 03:52:12  qilin
 * no message
 *
 * Revision 1.3  2007/06/02 01:56:23  qilin
 * no message
 *
 * Revision 1.2  2007/05/31 12:02:32  qilin
 * 菜单树上增加窗体控制功能
 *
 * Revision 1.1  2007/05/31 07:02:18  qilin
 * 界面重构，所有的JFrame改为JInternalFrame样式
 *
 *************************************************************************/
package smartx.system.login.ui;

import javax.swing.*;

import smartx.framework.common.ui.FrameWorkCommService;
import smartx.framework.common.ui.NovaRemoteServiceFactory;
import smartx.framework.common.ui.UIUtil;
import smartx.framework.common.ui.component.NovaInternalFrame;

import java.awt.*;
import java.util.Vector;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.HashMap;

public class NovaDeskTopPane extends JDesktopPane {
	private static final long serialVersionUID = 2959364064283541440L;
	
	private int cascadeX = 0;
    private int cascadeY = 0;
    private int cascadeXIncrement = 30;
    private int cascadeYIncrement = 30;

    public NovaDeskTopPane() {
        setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);
        setAutoscrolls(true);
    }

    public NovaInternalFrame getOpenedWindow(String menuPath) {
        NovaInternalFrame frame = null;
        if (!menuPath.equals("") && getAllFrames() != null) {
            for (int i = 0; i < getAllFrames().length; i++) {
                if ( ( (NovaInternalFrame) getAllFrames()[i]).getMenuPath().equals(menuPath)) {
                    frame = (NovaInternalFrame) getAllFrames()[i];
                }
            }
        }
        return frame;
    }
    
    public void showWindow(Component comp) {
		if (comp instanceof NovaInternalFrame) {
			NovaInternalFrame frame = (NovaInternalFrame) comp;
			super.add(frame);
			activeFrame(frame);
		}
	}

    public void showWindow(Component comp, String menuPath) {
        if (comp instanceof NovaInternalFrame) {
            NovaInternalFrame frame = (NovaInternalFrame) comp;
            frame.setMenuPath(menuPath);

            if (getOpenedWindow(menuPath) == null) {
                super.add(frame);
            }
            activeFrame(frame);
        }
    }

    private void activeFrame(NovaInternalFrame frame) {
        frame.setVisible(true);
        try {
            DesktopManager desktopManager = getDesktopManager();
            if (frame.isIcon()) {
                desktopManager.deiconifyFrame(frame);
            }
            desktopManager.activateFrame(frame);
            frame.setSelected(true);
            if (frame.getSize().getHeight() > getSize().getHeight() ||
                frame.getSize().getWidth() > getSize().getWidth()) {
                frame.setMaximum(true);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    protected void paintComponent(Graphics g) {
        setBackground(new Color(118,154,200));
    	
    	String bgstr = UIUtil.getEnvironmentParamStr("IMAGE_DESKTOPBACKGROUND", "images/logo/bg.jpg");
        ImageIcon bgimg=UIUtil.getImage(bgstr);
        if (bgimg != null) {            
        	g.drawImage(bgimg.getImage(), 0, 0, new Color(236, 233, 216), null);
        }
    	
        String strImageCenter = UIUtil.getEnvironmentParamStr("IMAGE_DESKTOPCENTER", "images/logo/main.gif");
        ImageIcon bkImage=UIUtil.getImage(strImageCenter);
        if (bkImage == null) {
            return;
        }
        g.drawImage(bkImage.getImage(), (int) Math.max(getBounds().width - bkImage.getIconWidth(), 0) / 2,
                    (int) Math.max(getBounds().height - bkImage.getIconHeight(), 0) / 2, new Color(236, 233, 216), null);
    }

    public void addWindowsMenu(JPopupMenu popmenu_header) {
        JInternalFrame[] frames = getAllFrames();
        if (frames != null && frames.length > 0) {
            ImageIcon icon_tile = null;
            ImageIcon icon_cascade = null;
            ImageIcon icon_closeCurrent = null;
            ImageIcon icon_closeAll = null;
            try {
                icon_tile = UIUtil.getImage("images/platform/tilehoriz.gif");
                icon_cascade = UIUtil.getImage("images/platform/cascade.gif");
                icon_closeCurrent = UIUtil.getImage("images/platform/closewindow.gif");
                icon_closeAll = UIUtil.getImage("images/platform/closeallwindow.gif");
            } catch (Exception ex) {
            }

            JMenuItem item_tile = new JMenuItem("平铺窗口", icon_tile);
            JMenuItem item_cascade = new JMenuItem("层叠窗口", icon_cascade);
            JMenuItem item_closeCurrent = new JMenuItem("关闭当前窗口", icon_closeCurrent);
            JMenuItem item_closeAll = new JMenuItem("关闭全部窗口", icon_closeAll);

            item_tile.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    tileWindowsHorizontally();
                }
            });
            item_cascade.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    cascadeWindows();
                }
            });

            item_closeCurrent.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    closeCurrentWindow();
                }
            });

            item_closeAll.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    closeAllWindows();
                }
            });

            popmenu_header.addSeparator();
            popmenu_header.add(item_tile);
            popmenu_header.add(item_cascade);
            popmenu_header.addSeparator();
            popmenu_header.add(item_closeCurrent);
            popmenu_header.add(item_closeAll);
            popmenu_header.addSeparator();

            for (int i = 0; i < frames.length; i++) {
                NovaInternalFrame frame = (NovaInternalFrame) frames[i];
                String menuPath = frame.getMenuPath();
                String title = menuPath.substring(menuPath.lastIndexOf("-") + 1, menuPath.length());
                MyMenuItem item_frame = new MyMenuItem(title);
                item_frame.setUserObject(frame);
                item_frame.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        activeMenuSelectedWindow(e);
                    }
                });
                if (getSelectedFrame() == frame) {
                    ImageIcon icon_active = null;
                    try {
                        icon_active = UIUtil.getImage("images/platform/active.gif");
                        item_frame.setIcon(icon_active);
                    } catch (Exception ex) {
                    }
                } else {
                    item_frame.setText("   " + item_frame.getText());
                }
                popmenu_header.add(item_frame);
            }
        }
    }

    public void activeMenuSelectedWindow(ActionEvent e) {
        NovaInternalFrame frame = (NovaInternalFrame) ( (MyMenuItem) e.getSource()).getUserObject();
        activeFrame(frame);
    }

    public void closeCurrentWindow() {
        remove(getSelectedFrame());
        updateUI();
    }

    public void closeAllWindows() {
        removeAll();
        updateUI();
    }

    public void cascadeWindows() {
        JInternalFrame frames[] = getAllFrames();
        if (frames.length == 0) {
            return;
        }
        cascadeX = 0;
        cascadeY = 0;
        for (int i = frames.length - 1; i >= 0; i--) {
            cascadeFrame(frames[i]);
        }
        sizingToScrollPane();
    }

    private void cascadeFrame(JInternalFrame jinternalframe) {
        if (!isDisplayable()) {
            return;
        }
        Dimension dimension = calcCascadeSize();
        jinternalframe.setBounds(cascadeX, cascadeY, dimension.width, dimension.height);
        cascadeX += cascadeXIncrement;
        cascadeY += cascadeYIncrement;
    }

    private Dimension calcCascadeSize() {
        Dimension cascadeSize = new Dimension(0, 0);
        cascadeSize.width = (int) (0.80000000000000004D * (double) getSize().width);
        cascadeSize.height = (int) (0.80000000000000004D * (double) getSize().height);

        return cascadeSize;
    }

    public void tileWindowsHorizontally() {
        JInternalFrame frames[] = getAllFrames();
        if (frames.length == 0) {
            return;
        }

        javax.swing.JInternalFrame.JDesktopIcon ajdesktopicon[] = getAllIconifiedFrames();
        int i = ajdesktopicon.length <= 0 ? 0 : ajdesktopicon[0].getHeight();
        int j = getSize().height - i;
        int k = j / frames.length - 1;
        int l = j % frames.length;
        for (int i1 = 0; i1 < frames.length; i1++) {
            if (i1 < l) {
                frames[i1].setBounds(0, i1 * k, getSize().width, k + 1);
            } else {
                frames[i1].setBounds(0, i1 * k, getSize().width, k);
            }
        }

        repaint();
        sizingToScrollPane();
    }

    private void sizingToScrollPane() {
        JScrollPane jscrollpane = (JScrollPane) SwingUtilities.getAncestorOfClass(javax.swing.JScrollPane.class, this);
        if (jscrollpane != null) {
            revalidate();
        }
    }

    public javax.swing.JInternalFrame.JDesktopIcon[] getAllIconifiedFrames() {
        int nComponentCount = getComponentCount();

        Vector vector = new Vector(nComponentCount);
        for (int i = 0; i < nComponentCount; i++) {
            Component component = getComponent(i);
            if (component instanceof javax.swing.JInternalFrame.JDesktopIcon) {
                JInternalFrame jinternalframe = ( (javax.swing.JInternalFrame.JDesktopIcon) component).getInternalFrame();
                if (jinternalframe != null) {
                    vector.addElement(component);
                }
            }
        }

        javax.swing.JInternalFrame.JDesktopIcon ajdesktopicon[] = new javax.swing.JInternalFrame.JDesktopIcon[vector.
            size()];
        vector.copyInto(ajdesktopicon);
        return ajdesktopicon;
    }

}

class MyMenuItem extends JMenuItem {
    private Object userObject = null;
    public MyMenuItem(String text) {
        super(text, (Icon)null);
    }

    public void setUserObject(Object obj) {
        this.userObject = obj;
    }

    public Object getUserObject() {
        return this.userObject;
    }
}
