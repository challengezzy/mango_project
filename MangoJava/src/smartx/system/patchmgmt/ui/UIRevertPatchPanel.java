/******************************************************************
*
*$RCSfile: UIRevertPatchPanel.java,v $ $Revision: 1.1 $ $Author: qilin $ $Date: 2007/07/24 06:56:39 $
*
*$Log: UIRevertPatchPanel.java,v $
*Revision 1.1  2007/07/24 06:56:39  qilin
*patch管理工具
*
*
********************************************************************/
package smartx.system.patchmgmt.ui;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.border.EtchedBorder;

import smartx.framework.common.ui.NovaSplashWindow;
import smartx.framework.metadata.ui.NovaMessage;

import java.util.*;
import java.io.*;

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
public class UIRevertPatchPanel extends JPanel {
    UIPatchMgmtFrame parent;
    Vector segmengInfo=new Vector();

    BorderLayout borderLayout1 = new BorderLayout();
    JPanel jPanel1 = new JPanel();
    BorderLayout borderLayout2 = new BorderLayout();
    JPanel jPanel4 = new JPanel();
    JPanel jPanel5 = new JPanel();
    JButton btOK = new JButton();
    JLabel jLabel1 = new JLabel();
    GridBagLayout gridBagLayout1 = new GridBagLayout();
    JComboBox cbDate = new JComboBox();
    FlowLayout flowLayout1 = new FlowLayout();

    public UIRevertPatchPanel(UIPatchMgmtFrame _parent) {
        parent = _parent;
        try {
            jbInit();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        this.setLayout(borderLayout1);
        jPanel1.setLayout(borderLayout2);
        btOK.setMnemonic('O');
        btOK.setText("确定(O)");
        btOK.addActionListener(new UIRevertPatchPanel_btOK_actionAdapter(this));
        jLabel1.setForeground(Color.blue);
        jLabel1.setText("选择恢复时间点");
        jPanel5.setLayout(gridBagLayout1);
        jPanel4.setLayout(flowLayout1);
        flowLayout1.setAlignment(FlowLayout.LEFT);
        jPanel1.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED,
            Color.white, new Color(165, 163, 151)), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        this.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        this.add(jPanel1, java.awt.BorderLayout.NORTH);
        jPanel1.add(jPanel4, java.awt.BorderLayout.NORTH);
        jPanel4.add(jLabel1);
        jPanel1.add(jPanel5, java.awt.BorderLayout.SOUTH);
        jPanel5.add(cbDate, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0
                                                   , GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                                                   new Insets(3, 3, 3, 3), 0, 0));
        jPanel5.add(btOK, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
                                                 , GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                                                 new Insets(3, 3, 3, 3), 0, 0));
        cbDate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(cbDate.getSelectedItem()!=null && !cbDate.getSelectedItem().equals("")) {
                    btOK.setEnabled(true);
                } else {
                    btOK.setEnabled(false);
                }
            }
        });
    }

    public void refreshPatchDate() {
        DefaultComboBoxModel model = (DefaultComboBoxModel) cbDate.getModel();
        model.removeAllElements();
        model.addElement("");
        readPatchDate();
        if (segmengInfo != null && segmengInfo.size() > 0) {
            for (int i = 0; i < segmengInfo.size(); i++) {
                model.addElement(((PatchSegmentInfoVO)segmengInfo.get(i)).getDate());
            }
        }
    }

    private void readPatchDate() {
        segmengInfo = new Vector();

        FileReader file = null;
        try {
            file = new FileReader(parent.PATH_CURRENT + parent.PATH_PATCHBACKUP + File.separator +
                                  parent.HISTORYFILENAME);
        } catch (FileNotFoundException ex) {
            return;
        }
        BufferedReader in = new BufferedReader(file);
        try {
            Vector tmpV = null;
            while (true) {
                String s = in.readLine();
                if (s == null) {
                    break;
                }
                if (s.startsWith(parent.LOG_SEGMENT_TITLE)) {
                    tmpV = new Vector();
                }
                tmpV.add(s);
                if (s.startsWith(parent.LOG_SEGMENT_END)) {
                    String date = null;
                    String directory=null;
                    String createFiles = null;
                    String changeFiles = null;
                    for (int i = 0; i < tmpV.size(); i++) {
                        if ( ( (String) tmpV.get(i)).startsWith(parent.LOG_SEGMENT_DATE)) {
                            date = ((String) tmpV.get(i)).substring(((String) tmpV.get(i)).indexOf(":") + 1, ((String) tmpV.get(i)).length()).trim();
                        }
                        if ( ( (String) tmpV.get(i)).startsWith(parent.LOG_SEGMENT_DIRECTORY)) {
                            directory = ((String) tmpV.get(i)).substring(((String) tmpV.get(i)).indexOf(":") + 1, ((String) tmpV.get(i)).length()).trim();
                        }
                        if ( ( (String) tmpV.get(i)).startsWith(parent.LOG_SEGMENT_CREATEDFILES)) {
                            createFiles = ((String) tmpV.get(i)).substring(((String) tmpV.get(i)).indexOf(":") + 1, ((String) tmpV.get(i)).length()).trim();
                        }
                        if ( ( (String) tmpV.get(i)).startsWith(parent.LOG_SEGMENT_CHANGEDFILES)) {
                            changeFiles = ((String) tmpV.get(i)).substring(((String) tmpV.get(i)).indexOf(":") + 1, ((String) tmpV.get(i)).length()).trim();
                        }
                    }
                    if (date != null) {
                        PatchSegmentInfoVO info = new PatchSegmentInfoVO(date);
                        info.setDirectory(directory);
                        info.setTexts(tmpV);
                        if (createFiles != null) {
                            info.setCreateFile(splitFileString(createFiles));
                        }
                        if (changeFiles != null) {
                            info.setChangeFile(splitFileString(changeFiles));
                        }
                        segmengInfo.add(info);
                    }
                }
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

    private Vector splitFileString(String s) {
        Vector result=new Vector();
        if(s!=null) {
            StringTokenizer token=new StringTokenizer(s,",");
            while(token.hasMoreTokens()) {
                String tmp=token.nextToken().trim();
                if(!tmp.equals("")) {
                    result.add(tmp);
                }
            }
        }
        return result;
    }

    private void actionRevertPatch() {
        try {
            //恢复文件
            revertFiles();

            //写历史记录
            writeHistory();

            //重新缓冲
            parent.initLibJars();

            NovaMessage.show(parent, "恢复补丁成功，安装记录请查看历史记录。");
        } catch (Exception ex) {
            NovaMessage.show(parent, "恢复补丁失败：" + ex.getMessage());
        }
    }

    private void revertFiles() throws Exception {
        if(segmengInfo!=null) {
            for (int i = segmengInfo.size() - 1; i >= 0; i--) {
                PatchSegmentInfoVO info=(PatchSegmentInfoVO)segmengInfo.get(i);
                //恢复
                Vector vChange=info.getChangeFiles();
                if (vChange != null) {
                    for (int j = 0; j < vChange.size(); j++) {
                        parent.copyFile(new File(parent.PATH_CURRENT + parent.PATH_PATCHBACKUP + File.separator +
                                                 info.getDirectory() + File.separator +
                                                 parent.PATH_PATCHBACKU_FORCHANGE + File.separator + vChange.get(j)),
                                        new File(parent.PATH_CURRENT + parent.PATH_LIB + File.separator + vChange.get(j)));
                    }
                }
                //删除
                Vector vCreate=info.getCreateFiles();
                if(vCreate!=null) {
                    for(int j=0;j<vCreate.size();j++) {
                        new File(parent.PATH_CURRENT + parent.PATH_LIB + File.separator + vCreate.get(j)).delete();
                    }
                }
                if(info.getDate().equals(cbDate.getSelectedItem())) {
                    return;
                }
            }
        }
    }

    private void writeHistory() throws Exception {
        FileWriter fWriter = null;
        PrintWriter out = null;
        try {
            File fHistory = new File(parent.PATH_CURRENT + parent.PATH_PATCHBACKUP + File.separator +
                                     parent.HISTORYFILENAME);
            fWriter = new FileWriter(fHistory, false);
            out = new PrintWriter(fWriter);
            if (segmengInfo != null) {
                for (int i = 0; i < segmengInfo.size(); i++) {
                    if(((PatchSegmentInfoVO)segmengInfo.get(i)).getDate().equals(cbDate.getSelectedItem())) {
                        break;
                    }
                    Vector texts=((PatchSegmentInfoVO)segmengInfo.get(i)).getTexts();
                    for(int j=0;j<texts.size();j++) {
                        out.println(texts.get(j));
                    }
                }
            }
        } catch (IOException ex3) {
        } finally {
            try {
                out.close();
            } catch (Exception ex6) {
            }
            try {
                fWriter.close();
            } catch (Exception ex2) {
            }
        }
    }

    public void btOK_actionPerformed(ActionEvent e) {
        if (NovaMessage.confirm(parent, "确定执行恢复补丁操作么？")) {
            new NovaSplashWindow(parent, "正在恢复补丁文件,请稍候。。。", new AbstractAction() {
                private static final long serialVersionUID = -287905438900197436L;
                public void actionPerformed(ActionEvent e) {
                    actionRevertPatch();
                    refreshPatchDate();
                }
            });
        }
    }
}

class UIRevertPatchPanel_btOK_actionAdapter implements ActionListener {
    private UIRevertPatchPanel adaptee;
    UIRevertPatchPanel_btOK_actionAdapter(UIRevertPatchPanel adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.btOK_actionPerformed(e);
    }
}

class PatchSegmentInfoVO {
    private String date;
    private String directory;
    private Vector createFiles = new Vector();
    private Vector changeFiles = new Vector();
    private Vector texts=new Vector();
    public PatchSegmentInfoVO(String _date) {
        date = _date;
    }

    public void setTexts(Vector v) {
        texts=v;
    }

    public void setCreateFile(Vector v) {
        createFiles=v;
    }

    public void setChangeFile(Vector v) {
        changeFiles=v;
    }

    public Vector getTexts() {
        return texts;
    }

    public Vector getCreateFiles() {
        return createFiles;
    }

    public Vector getChangeFiles() {
        return changeFiles;
    }

    public String getDate() {
        return date;
    }

    public void setDirectory(String _directory) {
        directory=_directory;
    }

    public String getDirectory() {
        return directory;
    }
}
