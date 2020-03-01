package smartx.system.login.ui;


import java.awt.Component;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.JOptionPane;

import smartx.framework.common.ui.FrameWorkCommService;
import smartx.framework.common.ui.NovaClientEnvironment;
import smartx.framework.common.ui.NovaRemoteServiceFactory;
import smartx.system.common.constant.CommonSysConst;
import smartx.system.login.ui.ClientPanelBeforeLogoutInterface;
import smartx.system.login.ui.SystemLoginServiceIFC;

public class NovaAppletLogout {

	//登出
    public void doLogout(Component comp){
    	
    	try{
    		Vector secondProjectClientBeforeLogout = null; //附加客户端登出前处理
			FrameWorkCommService service = (FrameWorkCommService) NovaRemoteServiceFactory.getInstance().lookUpService(FrameWorkCommService.class);
			HashMap enParam = service.getEnvironmentParam();
			secondProjectClientBeforeLogout = (Vector) enParam.get(CommonSysConst.CLIENTBEFORELOGOUT);
			//首先处理登出前
			initSecondProjectClientBeforeLogout(secondProjectClientBeforeLogout);
    	}catch (Exception e1) {
			//e1.printStackTrace();
			JOptionPane.showMessageDialog(comp, e1.getMessage());			
		}
    	
    	
    	
    	
    	// 远程访问处理该用户退出处理逻辑..比如修改该用户在线标记等!
        try {
            String userID = (String) NovaClientEnvironment.getInstance().get("SYS_LOGINUSER_ID");
            if (userID != null && !userID.equals("")) {
                SystemLoginServiceIFC loginService = (SystemLoginServiceIFC) NovaRemoteServiceFactory.getInstance().
                    lookUpService(SystemLoginServiceIFC.class);
                loginService.logout(userID);
            }
        } catch (java.lang.ClassNotFoundException ex) {
            //ex.printStackTrace(); //
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }
    //登出前处理
	private void initSecondProjectClientBeforeLogout(Vector v) {
		if (v != null) {
			for (int i = 0; i < v.size(); i++) {
				String className = (String) v.get(i);
				try {
					if (className != null && !className.equals("")) {
						ClientPanelBeforeLogoutInterface client = (ClientPanelBeforeLogoutInterface) Class.forName(className).newInstance();
						client.init();
					}
				} catch (Exception ex) {
					System.out.println("客户端附加登出处理错误：" + className + "\n" + ex.getMessage());
				}
			}
		}
	}
	
	
	
	
}
