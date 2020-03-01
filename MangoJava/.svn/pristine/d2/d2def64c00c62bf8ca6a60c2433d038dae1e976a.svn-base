/**************************************************************************
 * $RCSfile: NovaSplashWindow.java,v $  $Revision: 1.6.6.3 $  $Date: 2009/06/24 08:38:39 $
 **************************************************************************/
package smartx.framework.common.ui;

import java.util.*;
import java.util.Timer;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import smartx.framework.common.utils.Sys;
import smartx.framework.metadata.ui.*;


/**
 * 启动一个线程，来显示请求用户稍等的Splash
 *
 * @author Administrator
 *
 */
public class NovaSplashWindow extends NovaDialog {
    private static final long serialVersionUID = 6351024182198896342L;

    private JLabel jlb_pro;

    private String str_info = Sys.getSysRes("query.show.message.default");

    private AbstractAction action = null;

    private lableThread lablethread = null;

    private actionThread actionthread = null;

    //静态变量控制，当前系统只有一个窗口
    public static NovaSplashWindow window = null;

    /**
     * @param _com:调用该等待框的母板sa
     * @param _action AbstractAction
     */
    public NovaSplashWindow(Container _com, AbstractAction _action) {
        super(_com, "等待", 300, 100);
        this.setModal(true);
        this.action = _action;

        this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE); //
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                return;
            }
        });
        initWindow();
    }

    /**
     *
     * @param _com Container 调用该等待框的母板sa
     * @param _str_info String 面板要显示的提示信息
     * @param _action AbstractAction
     */
    public NovaSplashWindow(Container _com, String _str_info, AbstractAction _action) {
        super(_com, "等待", 300, 100);
        this.setModal(true);
        this.action = _action;
        str_info = _str_info;
        initWindow();
    }

    
    private void initWindow() {

        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(new JLabel(UIUtil.getImage("images/platform/clock.gif")), BorderLayout.WEST);
        this.getContentPane().add(new JLabel("   " + str_info), BorderLayout.CENTER);
        jlb_pro = new JLabel("(0)秒");
        this.getContentPane().add(jlb_pro, BorderLayout.EAST); //
        window = this; //
        actionthread = new actionThread(action);
        lablethread = new lableThread(jlb_pro, actionthread);
        lablethread.start();
        actionthread.start();
        this.validate();

        Dimension dialogSize = getSize();
        Dimension userScreen = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) (userScreen.width / 2 - dialogSize.width / 2);
        int y = (int) (userScreen.height / 2 - dialogSize.height / 2);
        setLocation(x, y);

        this.setVisible(true);
    }

    public void closeWindow() {
        this.dispose();
    }

    class lableThread extends Thread {

        private actionThread actionthread = null;

        private Timer timer = null;

        private int li_count = 0;

        public lableThread(JLabel _label, actionThread _actionthread) {
            actionthread = _actionthread;
        }

        public void run() {
            timer = new Timer();
            timer.schedule(new TimerTask() {
                public void run() {
                    refreshMsg();
                }
            }, 1000, 1000);
        }

        private void refreshMsg() {
            if (!actionthread.isAlive()) {
                timer.cancel();
            }
            li_count++;
            jlb_pro.setText("(" + li_count + ")秒");
        }
    }

    class actionThread extends Thread {

        private AbstractAction action = null;

        public actionThread(AbstractAction _action) {
            action = _action;
        }

        public void run() {
            try {
                //long ll_1 = System.currentTimeMillis();
                action.actionPerformed(null);
                //slong ll_2 = System.currentTimeMillis();
                //System.out.println("等待框执行一个耗时操作结束,耗时[" + (ll_2 - ll_1) + "]!");
            } catch (Throwable ex) {
            	System.err.println("执行等待框出错！");
                ex.printStackTrace();
            } finally {
                closeWindow();
            }
        }
    }
}
/*******************************************************************************
 * $RCSfile: NovaSplashWindow.java,v $ $Revision: 1.6.6.3 $ $Date: 2007/01/31
 * 01:46:46 $
 *
 * Revision 1.6 2007/01/31 09:18:02 shxch *** empty log message *** Revision 1.5
 * 2007/01/31 05:00:40 shxch *** empty log message *** Revision 1.4 2007/01/31
 * 02:36:50 shxch 处理登录界面中出现的问题，即线程的延迟，导致登录过后还会出现等待框 Revision 1.3 2007/01/31
 * 01:46:46 shxch *** empty log message ***
 *
 *
 ******************************************************************************/
