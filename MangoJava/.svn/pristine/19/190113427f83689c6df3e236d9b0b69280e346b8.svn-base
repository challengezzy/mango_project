/**************************************************************************
 * $RCSfile: NovaDialog.java,v $  $Revision: 1.6.8.2 $  $Date: 2010/01/21 08:24:40 $
 **************************************************************************/
package smartx.framework.metadata.ui;

import java.awt.*;

import javax.swing.*;

public class NovaDialog extends JDialog {

    private static final long serialVersionUID = 1L;

    public NovaDialog() throws HeadlessException {
        super();
    }

    public NovaDialog(String title) throws HeadlessException {
        super( (Frame)null, title);
    }

    public NovaDialog(Dialog owner, boolean modal) throws HeadlessException {
        super(owner, modal);
    }

    public NovaDialog(Dialog owner, String title, boolean modal, GraphicsConfiguration gc) throws HeadlessException {
        super(owner, title, modal, gc);
    }

    public NovaDialog(Dialog owner, String title, boolean modal) throws HeadlessException {
        super(owner, title, modal);
    }

    public NovaDialog(Dialog owner, String title) throws HeadlessException {
        super(owner, title);
    }

    public NovaDialog(Dialog owner) throws HeadlessException {
        super(owner);
    }

    public NovaDialog(Frame owner, boolean modal) throws HeadlessException {
        super(owner, modal);
    }

    public NovaDialog(Frame owner, String title, boolean modal, GraphicsConfiguration gc) {
        super(owner, title, modal, gc);
    }

    public NovaDialog(Frame owner, String title, boolean modal) throws HeadlessException {
        super(owner, title, modal);
    }

    public NovaDialog(Frame owner, String title) throws HeadlessException {
        super(owner, title);
    }

    public NovaDialog(Frame owner) throws HeadlessException {
        super(owner);
    }

    public NovaDialog(Container _parent) {
        super(JOptionPane.getFrameForComponent(_parent));
    }

    public NovaDialog(Container _parent, String title, boolean modal) throws HeadlessException {
        super(JOptionPane.getFrameForComponent(_parent), title);
        this.setModal(modal);
    }

    public NovaDialog(Container _parent, int _width, int li_height) {
        super(JOptionPane.getFrameForComponent(_parent), true);
        this.setSize(new Dimension(_width, li_height));
        centerDialogFrame();
    }

    public NovaDialog(Container _parent, String _title) {
        super(JOptionPane.getFrameForComponent(_parent), _title);
    }

    public NovaDialog(Container _parent, String _title, int _width, int li_height) {
        super(JOptionPane.getFrameForComponent(_parent), _title, true);
        this.setSize(new Dimension(_width, li_height));
        centerDialogFrame();
    }

    JDesktopPane desktop;

    public void setDeskTopPane(JDesktopPane _desktop) {
        this.desktop=_desktop;
    }

    public JDesktopPane getDeskTopPane() {
        return desktop;
    }

    String menuPath;

    public void setMenuPath(String _menuPath) {
        this.menuPath=_menuPath;
    }
    public String getMenuPath() {
        return menuPath;
    }

    /**
     * 基于父对象窗口居中
     */
    protected void centerDialogFrame() {
    	Dimension dialogSize = getSize();
    	Frame frame = JOptionPane.getFrameForComponent(getParent());
        
    	int x=0,y=0;
    	if(frame!=null){      
    		Dimension fsize = frame.getSize();
	        double ld_width = fsize.getWidth();
	        double ld_height = fsize.getHeight();
	        double ld_x = frame.getLocation().getX();
	        double ld_y = frame.getLocation().getY();	        
	        double ld_thisX = ld_x + ld_width / 2 - dialogSize.width  / 2;
	        double ld_thisY = ld_y + ld_height / 2 - dialogSize.height / 2;
	        if (ld_thisX < 0) { ld_thisX = 0; }
	        x=new Double(ld_thisX).intValue();	
	        if (ld_thisY < 0) { ld_thisY = 0; }
	        y=new Double(ld_thisY).intValue();	        
    	}

        setLocation(x, y);
    }
    
    /**
     * 基于整个屏幕居中
     */
    protected void centerDialog() {
        Dimension dialogSize = getSize();
        Dimension userScreen = Toolkit.getDefaultToolkit().getScreenSize();

        int x = (int) (userScreen.width / 2 - dialogSize.width / 2);
        int y = (int) (userScreen.height / 2 - dialogSize.height / 2);
        setLocation(x, y);
    }
    
    /**
     * 基于整个屏幕居中
     * @deprecated 不建议外部使用，而直接由继承者内部使用。如果确实需要请继承者另外包装public方法，而不要用本静态方法。
     */
    public static void centerDialog(Container c) {
        Dimension dialogSize = c.getSize();
        Dimension userScreen = Toolkit.getDefaultToolkit().getScreenSize();

        int x = (int) (userScreen.width / 2 - dialogSize.width / 2);
        int y = (int) (userScreen.height / 2 - dialogSize.height / 2);
        c.setLocation(x, y);
    }
}
/**************************************************************************
 * $RCSfile: NovaDialog.java,v $  $Revision: 1.6.8.2 $  $Date: 2010/01/21 08:24:40 $
 *
 * $Log: NovaDialog.java,v $
 * Revision 1.6.8.2  2010/01/21 08:24:40  wangqi
 * *** empty log message ***
 *
 * Revision 1.6.8.1  2010/01/15 04:33:02  wangqi
 * *** empty log message ***
 *
 * Revision 1.6  2007/06/20 03:20:30  qilin
 * no message
 *
 * Revision 1.5  2007/06/14 02:27:56  qilin
 * no message
 *
 * Revision 1.4  2007/06/13 12:01:15  qilin
 * no message
 *
 * Revision 1.3  2007/05/31 07:38:15  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 06:01:37  qilin
 * no message
 *
 * Revision 1.1  2007/02/10 08:51:58  shxch
 * *** empty log message ***
 *
 * Revision 1.2  2007/01/30 05:14:30  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/
