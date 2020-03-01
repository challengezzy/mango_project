/**************************************************************************
 * $RCSfile: TestFrame.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:38:18 $
 **************************************************************************/
package smartx.framework.metadata.ui.componentscard;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

//import org.jdesktop.jdic.browser.*;

public class TestFrame extends JFrame {

    private static final long serialVersionUID = 594714435267753929L;

    JTextField textField = null;

    //public static WebBrowser wb = null;

    JPanel mainPanel = new JPanel();

    JScrollPane scollPanel = new JScrollPane();

    static {
        System.out.println("输出一句话!!");
    }

    public TestFrame() {
        try {
            System.setProperty("shxch", "徐长华");
            this.setTitle("Swing嵌入IE浏览器");
            this.setSize(1000, 600);
            //this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            JPanel panel_north = new JPanel();
            panel_north.setLayout(new FlowLayout());

            textField = new JTextField(System.getProperty("WebURL") + "/applet/index.html");
            textField.setPreferredSize(new Dimension(600, 20));
            JButton btn_1 = new JButton("查询");
            btn_1.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    onSearch();
                }
            });

            JButton btn_exit = new JButton("Exit");
            btn_exit.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    //System.exit(0);
                    //wb.dispose();
                }
            });

            panel_north.add(textField);
            panel_north.add(btn_1);
            panel_north.add(btn_exit); //

            this.getContentPane().setLayout(new BorderLayout());

            this.getContentPane().add(panel_north, BorderLayout.NORTH);

            //mainPanel.setLayout(new BorderLayout());
            //mainPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
            this.getContentPane().add(scollPanel, BorderLayout.CENTER);
            this.setVisible(true);
        } catch (Throwable ex) {
            ex.printStackTrace(); //
        }
    }

    private void onSearch() {
        loadIEWeb();
        //PressTest();
    }

    private void loadIEWeb() {
        try {
            //WebBrowser wb = new WebBrowser(new java.net.URL(textField.getText())); //
            //wb.repaint();
            //wb.refresh();
            scollPanel.getViewport().removeAll();
            //scollPanel.getViewport().add(wb);
            scollPanel.updateUI();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void PressTest() {
        int li_cycle = 5000;
        long ll_1 = System.currentTimeMillis();
        for (int i = 0; i < li_cycle; i++) {
            new Thread_1(i).start();
            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        long ll_2 = System.currentTimeMillis();

        JOptionPane.showMessageDialog(this, "总耗时[" + (ll_2 - ll_1) + "],每秒[" + li_cycle * 1000 / (ll_2 - ll_1) + "]");
    }

    public static void main(String[] args) {
        new TestFrame();
    }

}

class Thread_1 extends Thread {
    int li_cycle = 0;

    Thread_1(int _cycle) {
        li_cycle = _cycle;
    }

    public void run() {
        //		//String[][] str_data = UIUtil.getStringArray("select * from n1_entity");
        //		String[][] str_data = new UIUtilWrapper().getStringArray("select * from n1_entity");
        //		System.out.println("第[" + li_cycle + "]循环取到结果记录数[" + str_data.length + "]");
    }
}
/**************************************************************************
 * $RCSfile: TestFrame.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:38:18 $
 *
 * $Log: TestFrame.java,v $
 * Revision 1.2  2007/05/31 07:38:18  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 06:01:06  qilin
 * no message
 *
 * Revision 1.3  2007/03/13 08:41:20  shxch
 * *** empty log message ***
 *
 * Revision 1.2  2007/01/30 05:14:30  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/