/******************************************************************
*
*$RCSfile: UIPatchMgmtFrame.java,v $ $Revision: 1.1.6.2 $ $Author: wangqi $ $Date: 2008/03/27 05:05:10 $
*
*$Log: UIPatchMgmtFrame.java,v $
*Revision 1.1.6.2  2008/03/27 05:05:10  wangqi
**** empty log message ***
*
*Revision 1.3  2008/03/19 06:05:10  wangqi
**** empty log message ***
*
*Revision 1.2  2008/03/19 05:38:40  wangqi
**** empty log message ***
*
*Revision 1.1  2007/07/24 06:56:40  qilin
*patch管理工具
*
*
********************************************************************/
package smartx.system.patchmgmt.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.jar.*;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

import smartx.framework.common.ui.NovaSplashWindow;
import smartx.framework.metadata.ui.NovaMessage;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class UIPatchMgmtFrame extends JFrame {
    String projectName;
    public static String PATH_CURRENT="."+File.separator;
//    public static String PATH_CURRENT="."+File.separator+"WebRootBizservice"+File.separator+"WEB-INF"+File.separator;

    public static final String PATH_LIB="lib";

    public static final String PATCH_NEWFILE_PREFIX="PATCH_";
    public static final String PATH_PATCHBACKUP="patchbak";
    public static final String PATH_PATCHBACKU_FORCHANGE="libbak";
    public static final String PATH_PATCHBACKU_FORPATCH="patch";
    public static final String HISTORYFILENAME="patchinfo.txt";

    public static final String LOG_SEGMENT_TITLE="【PATCH SEGMENT】";
    public static final String LOG_SEGMENT_END="【END SEGMENT】";
    public static final String LOG_SEGMENT_DIRECTORY="DIRECTORY";
    public static final String LOG_SEGMENT_DATE="DATE";
    public static final String LOG_SEGMENT_CHANGEDFILES="CHANGED FILES";
    public static final String LOG_SEGMENT_CREATEDFILES="CREATED FILES";
    public static final String LOG_SEGMENT_PATCHFILES="PATCH FILES";
    public static final String LOG_SEGMENT_DETAILS="DETAILS";

    public static final int BUFFERSIZE=1024;

    public static Hashtable jartable = new Hashtable();

    String title = "补丁管理";
    JPanel panelMain = new JPanel();
    JTabbedPane tabbedPanel = new JTabbedPane();
    UIAddPatchPanel addPanel = new UIAddPatchPanel(this);
    UIRevertPatchPanel revertPanel = new UIRevertPatchPanel(this);
    JTextArea tfHistory = new JTextArea();

    public UIPatchMgmtFrame(String path) {
    	PATH_CURRENT=path;
    	init();
    }
    public UIPatchMgmtFrame() {
    	init();
    }
    public void init() {
        try {
            projectName = getProjectName();
            jbInit();
            new NovaSplashWindow(this, "正在缓存现有系统文件,请稍候。。。", new AbstractAction() {
                private static final long serialVersionUID = -287905438900197436L;
                public void actionPerformed(ActionEvent e) {
                    initLibJars();
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        if (projectName != null) {
            title = title + " —— " + projectName;
        }
        this.setTitle(title);
        panelMain.setLayout(new BorderLayout());
        this.addWindowListener(new UIPatchMgmtFrame_this_windowAdapter(this));
        this.getContentPane().add(panelMain, java.awt.BorderLayout.CENTER);
        panelMain.add(tabbedPanel, java.awt.BorderLayout.CENTER);
        tabbedPanel.add("安装补丁", addPanel);
        tabbedPanel.add("补丁恢复", revertPanel);
        tabbedPanel.add("历史记录", getHistoryInfoPanel());
        tabbedPanel.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                if(tabbedPanel.getSelectedIndex()==1) {
                    revertPanel.refreshPatchDate();
                }
            }
        });
    }

    private JPanel getHistoryInfoPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        JScrollPane jScrollPane1 = new JScrollPane();
        panel.add(jScrollPane1, java.awt.BorderLayout.CENTER);
        jScrollPane1.getViewport().add(tfHistory);

        JPanel jPanel2 = new JPanel();
        JButton btRefresh = new JButton();
        FlowLayout flowLayout1 = new FlowLayout();
        btRefresh.setMnemonic('R');
        btRefresh.setText("刷新(R)");
        jPanel2.setLayout(flowLayout1);
        flowLayout1.setAlignment(FlowLayout.RIGHT);
        panel.add(jPanel2, java.awt.BorderLayout.NORTH);
        jPanel2.add(btRefresh);
        btRefresh.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                tfHistory.setText("");
                FileReader file = null;
                try {
                    file = new FileReader(PATH_CURRENT + PATH_PATCHBACKUP + File.separator +
                                          HISTORYFILENAME);
                } catch (FileNotFoundException ex) {
                    NovaMessage.show("没有找到历史文件:" + PATH_CURRENT + PATH_PATCHBACKUP + File.separator +
                                     HISTORYFILENAME);
                    return;
                }
                BufferedReader in = new BufferedReader(file);
                try {
                    while (true) {
                        String s=in.readLine();
                        if(s==null) {
                            break;
                        }
                        tfHistory.setText(tfHistory.getText() + s+"\n");
                    }
                } catch (IOException ex1) {
                } finally {
                    try {
                        file.close();
                    } catch (IOException ex2) {
                    }
                    try {
                        in.close();
                    } catch (IOException ex3) {
                    }
                }
            }
        });
        return panel;
    }

    private String getProjectName() {
        String str_filePath = PATH_CURRENT+"Nova2Config.xml"; //
        try {
            org.jdom.input.SAXBuilder builder = new org.jdom.input.SAXBuilder(); //
            org.jdom.Document doc = builder.build(new File(str_filePath)); // 加载XML
            java.util.List initparams = doc.getRootElement().getChildren("init-param");
            if (initparams != null) {
                for (int i = 0; i < initparams.size(); i++) {
                    if (initparams.get(i) instanceof org.jdom.Element) {
                        org.jdom.Element param = (org.jdom.Element) initparams.get(i);
                        if (param != null) {
                            String str_key = param.getAttributeValue("key");
                            String str_value = param.getAttributeValue("value");
                            if (str_key.equals("PROJECT_NAME")) {
                                return str_value;
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            NovaMessage.show(this,"请在Nova平台所支持应用的 WEB-INF 目录中运行此工具！");
            System.exit(0);
        }
        return null;
    }

    public void initLibJars() {
        try {
            jartable.clear();
            String realpath=PATH_CURRENT+PATH_LIB+File.separator;
            File lib_dir = new File(realpath);
            File[] jar_files = lib_dir.listFiles(new FileFilter() {
                public boolean accept(File fi) {
                    return fi.getPath().toLowerCase().endsWith(".jar");
                }
            });

            for (int i = 0; i < jar_files.length; i++) {
                try {
                    String str_filename = jar_files[i].getName();
                    FileInputStream fileIn=new FileInputStream(realpath + str_filename);
                    JarInputStream inputStream = new JarInputStream(fileIn);

                    while (true) { // 循环释放jar下的文件，并赋予一个jarEntry对象(jarEntry对象我们可将它理解成解压jar后的每一个元素)
                        JarEntry myJarEntry = inputStream.getNextJarEntry();
                        //System.out.println("缓冲文件【"+str_filename+"】内容："+myJarEntry.getName());
                        
                        if (myJarEntry == null) { // 如果jarEntry为空则中断循环
                            break;
                        } else if (myJarEntry.isDirectory() || isInvalidPatchFile(myJarEntry.getName())) { // 如果是目录或无效patch文件则不处理
                            continue;
                        }
                        String str_key = myJarEntry.getName();
                        if (str_key != null) {
                            Object obj = jartable.get(str_key); // 是否已有
                            if (obj == null) { // 如果没有,则新建一个
                                Vector v_jars = new Vector();
                                v_jars.add(str_filename);
                                jartable.put(str_key, v_jars);
                            } else {
                                Vector v_jars = (Vector) obj; // 如果有,则在原来的基础上加一个
                                if (!v_jars.contains(str_filename)) {
                                    v_jars.add(str_filename);
                                    jartable.put(str_key, v_jars);
                                }
                            }
                        }
                    }
                    inputStream.close();
                    fileIn.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception ex) {
            NovaMessage.show(this,"无法初始化 "+projectName+" 现有文件，系统将退出！");
            System.exit(0);
        }
    }

    public static boolean isInvalidPatchFile(String fileName) {
        if(fileName.toLowerCase().endsWith(".java") ||
           fileName.startsWith("META-INF")) {
            return true;
        }
        return false;
    }

    public static void copyFile(File in, File out) throws Exception {
        FileInputStream fis = new FileInputStream(in);
        FileOutputStream fos = new FileOutputStream(out);
        byte[] buf = new byte[BUFFERSIZE];
        int i = 0;
        while ( (i = fis.read(buf)) != -1) {
            fos.write(buf, 0, i);
        }
        fis.close();
        fos.close();
    }

    public void showFrame() {
        this.setSize(new Dimension(800, 600));
        Dimension dialogSize = getSize();
        Dimension userScreen = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) (userScreen.width / 2 - dialogSize.width / 2);
        int y = (int) (userScreen.height / 2 - dialogSize.height / 2);
        setLocation(x, y);
        //this.show();
        this.setVisible(true);
    }

    

    public void this_windowClosing(WindowEvent e) {
        System.exit(0);
    }
    
    public static void main(String[] args) {
        UIPatchMgmtFrame uipatchmgmtframe = null;        
        if(args.length>0){//如果有参数则默认第一个参数是路径
        	uipatchmgmtframe = new UIPatchMgmtFrame(args[0]);
        }else{
        	uipatchmgmtframe = new UIPatchMgmtFrame();
        }
        uipatchmgmtframe.showFrame();
    }
    

}

class UIPatchMgmtFrame_this_windowAdapter extends WindowAdapter {
    private UIPatchMgmtFrame adaptee;
    UIPatchMgmtFrame_this_windowAdapter(UIPatchMgmtFrame adaptee) {
        this.adaptee = adaptee;
    }

    public void windowClosing(WindowEvent e) {
        adaptee.this_windowClosing(e);
    }
}
