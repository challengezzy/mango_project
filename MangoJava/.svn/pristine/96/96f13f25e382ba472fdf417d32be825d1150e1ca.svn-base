package smartx.publics.styletemplet.ui;

import javax.swing.*;

import smartx.framework.common.ui.*;
import smartx.framework.common.ui.component.*;


public class AbstractStyleFrame extends NovaInternalFrame {

    private static final long serialVersionUID = 8396695203700335350L;
    
    

    public AbstractStyleFrame() {
        super();
    }

    public AbstractStyleFrame(String _title) {
        super(_title);
    }

    // 得到最上方面标题面板
    protected JPanel getTitlePanel() {
        return null;
    }

    // 得到下面的状态栏面板!
    protected JPanel getStatuPanel() {
        return null;
    }

    protected StyleTempletServiceIfc getService() throws Exception {
        StyleTempletServiceIfc service = (StyleTempletServiceIfc) NovaRemoteServiceFactory.getInstance().lookUpService(
            StyleTempletServiceIfc.class);
        return service;
    }
}
