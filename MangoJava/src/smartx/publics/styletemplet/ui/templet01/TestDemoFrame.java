/**************************************************************************
 * $RCSfile: TestDemoFrame.java,v $  $Revision: 1.2.8.1 $  $Date: 2009/12/16 04:13:50 $
 **************************************************************************/
package smartx.publics.styletemplet.ui.templet01;

import java.util.HashMap;

public class TestDemoFrame extends DefaultMainFrame {
	private static final long serialVersionUID = -804940514201909193L;

	public TestDemoFrame(HashMap map){
    	super(map);
    }
	
	public String getTempletcode() {
        return "PUB_MENU_CODE_1";
    }

    public String getTempletTitle() {
        return "测试";
    }

	
}
/**************************************************************************
 * $RCSfile: TestDemoFrame.java,v $  $Revision: 1.2.8.1 $  $Date: 2009/12/16 04:13:50 $
 *
 * $Log: TestDemoFrame.java,v $
 * Revision 1.2.8.1  2009/12/16 04:13:50  wangqi
 * *** empty log message ***
 *
 * Revision 1.2  2007/05/31 07:39:02  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 06:14:26  qilin
 * no message
 *
 * Revision 1.3  2007/03/05 09:53:53  shxch
 * *** empty log message ***
 *
 * Revision 1.2  2007/01/30 04:48:33  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/