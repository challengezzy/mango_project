/******************************************************************
*
*$RCSfile: UIAddPatchPanel.java,v $ $Revision: 1.2.2.2 $ $Author: wangqi $ $Date: 2008/03/27 05:05:11 $
*
*$Log: UIAddPatchPanel.java,v $
*Revision 1.2.2.2  2008/03/27 05:05:11  wangqi
**** empty log message ***
*
*Revision 1.5  2008/03/24 09:16:21  wangqi
**** empty log message ***
*
*Revision 1.4  2008/03/19 07:04:48  wangqi
**** empty log message ***
*
*Revision 1.3  2008/03/19 05:38:48  wangqi
**** empty log message ***
*
*Revision 1.2  2008/01/02 08:18:44  lst
*MR#:MR30-0000
*修改原有对类进行分组的算法
*
*Revision 1.1  2007/07/24 06:56:40  qilin
*patch管理工具
*
*
********************************************************************/
package smartx.system.patchmgmt.ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.jar.*;
import java.text.SimpleDateFormat;
import javax.swing.event.*;

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
public class UIAddPatchPanel extends JPanel {
    UIPatchMgmtFrame parent;
    File[] patchFiles;//补丁文件集合，存储补丁的文件对象
    HashMap changeFiles;//需要修改的文件集合，存储文件名称
    //整理的数据结构，为实际运行的合并文件算法作准备
    //key-String[patch文件]，value-HashMap(key-String[修改的目标文件]，value-Vector[class文件列表,jarEntry对象])
    HashMap patchFileGroup;
    //整理的数据结构，存储所有需要打在新建jar文件中的class和其对应的patch文件
    //key-String[patch文件]，value-Vector[class文件列表,jarEntry对象]
    HashMap newJarGroup;
    String currentTime;//当前时间，格式：YYYYMMDDHHMMSS
    String newJarFileName;//新建的jar文件名称
    boolean isCreateNewFile;//是否需要新建jar文件

    JPanel jPanel1 = new JPanel();
    JLabel jLabel1 = new JLabel();
    JPanel jPanel2 = new JPanel();
    JPanel jPanel3 = new JPanel();
    JTextField tfPath = new JTextField();
    JButton btSelPath = new JButton();
    JButton btOK = new JButton();
    JPanel jPanel4 = new JPanel();
    JPanel jPanel5 = new JPanel();
    JSplitPane jSplitPane1 = new JSplitPane();
    JTextArea tfDetails = new JTextArea();
    JTextArea tfFiles = new JTextArea();
    GridBagLayout gridBagLayout1 = new GridBagLayout();
    GridBagLayout gridBagLayout2 = new GridBagLayout();
    JPanel jPanel6 = new JPanel();
    BorderLayout borderLayout1 = new BorderLayout();
    JScrollPane jScrollPane1 = new JScrollPane();
    JPanel jPanel7 = new JPanel();
    JPanel jPanel8 = new JPanel();
    JScrollPane jScrollPane2 = new JScrollPane();
    BorderLayout borderLayout2 = new BorderLayout();
    BorderLayout borderLayout3 = new BorderLayout();
    JLabel jLabel2 = new JLabel();
    JLabel jLabel3 = new JLabel();
    JFileChooser fileChooser = new JFileChooser();

    public UIAddPatchPanel(UIPatchMgmtFrame _parent) {
        parent = _parent;
        try {
            jbInit();
            initFileChooser();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        this.setLayout(new BorderLayout());
        jPanel1.setLayout(new BorderLayout());
        jLabel1.setForeground(Color.blue);
        jLabel1.setText("补丁文件位置");
        tfPath.setEditable(false);
        btSelPath.setText("...");
        btSelPath.addActionListener(new UIAddPatchPanel_btSelPath_actionAdapter(this));
        jPanel2.setLayout(gridBagLayout2);
        jPanel3.setLayout(new BorderLayout());
        btOK.setMnemonic('O');
        btOK.setText("确定(O)");
        btOK.addActionListener(new UIAddPatchPanel_btOK_actionAdapter(this));
        jPanel4.setLayout(new BorderLayout());
        jPanel5.setLayout(new FlowLayout(FlowLayout.LEFT));
        jPanel3.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED,
            Color.white, new Color(165, 163, 151)), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        jSplitPane1.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED,
            Color.white, new Color(165, 163, 151)), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        jSplitPane1.setOneTouchExpandable(true);
        jPanel4.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED,
            Color.white, new Color(165, 163, 151)), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        jPanel1.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));
        jPanel6.setLayout(borderLayout1);
        jPanel6.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        jPanel7.setLayout(borderLayout2);
        jPanel8.setLayout(borderLayout3);
        jLabel2.setForeground(Color.blue);
        jLabel2.setText("文件列表");
        jLabel3.setForeground(Color.blue);
        jLabel3.setText("补丁执行结果预览");
        tfFiles.setEditable(false);
        tfDetails.setEditable(false);
        jPanel4.add(jPanel5, java.awt.BorderLayout.NORTH);
        jPanel5.add(jLabel1);
        jPanel1.add(jPanel3, java.awt.BorderLayout.EAST);
        jPanel1.add(jPanel4, java.awt.BorderLayout.CENTER);
        jPanel4.add(jPanel2, java.awt.BorderLayout.SOUTH);
        jPanel2.add(btSelPath, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            , GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
        jPanel2.add(tfPath, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0
            , GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
        jPanel6.add(jSplitPane1, java.awt.BorderLayout.CENTER);
        this.add(jPanel6, java.awt.BorderLayout.CENTER);
        jSplitPane1.add(jPanel8, JSplitPane.LEFT);
        jSplitPane1.add(jPanel7, JSplitPane.RIGHT);
        jPanel7.add(jScrollPane1, java.awt.BorderLayout.CENTER);
        jPanel7.add(jLabel3, java.awt.BorderLayout.NORTH);
        jPanel8.add(jScrollPane2, java.awt.BorderLayout.CENTER);
        jPanel8.add(jLabel2, java.awt.BorderLayout.NORTH);
        jScrollPane2.getViewport().add(tfFiles);
        jScrollPane1.getViewport().add(tfDetails);
        this.add(jPanel1, java.awt.BorderLayout.NORTH);
        jPanel3.add(btOK, java.awt.BorderLayout.NORTH);
        jSplitPane1.setDividerLocation(300);

        btOK.setEnabled(false);
        tfFiles.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent evt) {
                setButtonStatus();
            }

            public void removeUpdate(DocumentEvent evt) {
                setButtonStatus();
            }

            public void changedUpdate(DocumentEvent evt) {
                setButtonStatus();
            }
        });
    }

    private void setButtonStatus() {
        if (tfFiles.getText() != null && !tfFiles.getText().equals("")) {
            btOK.setEnabled(true);
        } else {
            btOK.setEnabled(false);
        }
    }

    private void initFileChooser() {
        fileChooser.setDialogTitle("选择补丁文件位置");
        fileChooser.setCurrentDirectory(new File("."));
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    }

    public void btSelPath_actionPerformed(ActionEvent e) {
        new NovaSplashWindow(parent, "正在读取补丁文件,请稍候。。。", new AbstractAction() {
            private static final long serialVersionUID = -287905438900197436L;
            public void actionPerformed(ActionEvent e) {
                readPatchFiles();
            }
        });
    }

    private void readPatchFiles() {
        if (fileChooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
            File path = fileChooser.getSelectedFile();
            if (path == null) {
                return;
            } else {
                tfPath.setText(path.getAbsolutePath());
                changeFiles = new HashMap(); //重置缓存数据
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                currentTime = sdf.format(new Date());
                isCreateNewFile = false;
                patchFileGroup=new HashMap();
                newJarGroup=new HashMap();
                newJarFileName = parent.PATCH_NEWFILE_PREFIX + currentTime + ".jar"; //重置缓存数据
                patchFiles = path.listFiles(new FileFilter() {
                    public boolean accept(File fi) {
                        return fi.getPath().toLowerCase().endsWith(".jar");
                    }
                });
                StringBuffer s = new StringBuffer();
                if (patchFiles != null && patchFiles.length > 0) {
                    for (int i = 0; i < patchFiles.length; i++) {
                        s.append(patchFiles[i].getName() + "\n");
                    }
                }
                tfFiles.setText(s.toString());
                showDetails();
            }
        }
    }

    private void showDetails() {
        StringBuffer sDetails = new StringBuffer();
        if (patchFiles != null && patchFiles.length > 0) {
            long count = 1;
            HashMap patchDetails=new HashMap();
            for (int i = 0; i < patchFiles.length; i++) {
                try {
                    String str_filename = patchFiles[i].getName();
                    FileInputStream fileInputStream=new FileInputStream(tfPath.getText() + File.separator +str_filename);
                    JarInputStream jarInputStream = new JarInputStream(fileInputStream);

                    //整理数据结构，当前patch文件对应的修改文件
                    HashMap changeFileGroup=new HashMap();
                    //整理数据结构，当前patch文件中需要新jar的class列表
                    Vector newJarClasses=new Vector();

                    while (true) { // 循环释放jar下的文件，并赋予一个jarEntry对象(jarEntry对象我们可将它理解成解压jar后的每一个元素)
                        JarEntry myJarEntry = jarInputStream.getNextJarEntry();
                        if (myJarEntry == null) { // 如果jarEntry为空则中断循环
                            break;
                        } else if (myJarEntry.isDirectory() || parent.isInvalidPatchFile(myJarEntry.getName())) { // 如果是目录或无效patch文件则不处理
                            continue;
                        }
                        String str_key = myJarEntry.getName();
                        if (str_key != null) {
                            if(patchDetails.containsKey(str_key)) {//patch中发现重复的文件
                                NovaMessage.show("错误！补丁文件中存在相同内容：\n"+
                                                 str_key+" —— "+"["+patchDetails.get(str_key)+"]"+"["+str_filename+"]");
                                tfDetails.setText("");
                                return;
                            } else {
                                patchDetails.put(str_key, str_filename);
                                System.out.println("patchDetails["+str_key+"]:"+str_filename);
                            }
                            String sCount=String.valueOf(count++);
                            if(sCount.length()<3) {//格式化一下
                                for(int j=sCount.length();j<3;j++) {
                                    sCount=" "+sCount;
                                }
                            }
                            sDetails.append(sCount + " : ");
                            sDetails.append("[" + str_filename + "]" + str_key.replace('/', '.'));
                            sDetails.append(" —— ");
                            Vector v = getJarNameByClassName(str_key);
                            if (v != null) {
                                StringBuffer tmps = new StringBuffer();
                                tmps.append("[");
                                for (int j = 0; j < v.size(); j++) {
                                    String changeFileName=(String)v.get(j);
                                    tmps.append(changeFileName);
                                    tmps.append(",");
                                    changeFiles.put(changeFileName, null);
                                    //整理数据结构-begin
                                    if(changeFileGroup.get(changeFileName)==null) {
                                        Vector classFile=new Vector();
                                        classFile.add(myJarEntry);
                                        changeFileGroup.put(changeFileName,classFile);
                                        System.out.println("patchFileGroup["+changeFileName+"]:"+myJarEntry.getName());
                                    } else {
                                        Vector classFile=(Vector)changeFileGroup.get(changeFileName);
                                        classFile.add(myJarEntry);
                                        System.out.println("patchFileGroup["+changeFileName+"]:"+myJarEntry.getName());
                                    }
                                    //整理数据结构-end
                                }
                                tmps.deleteCharAt(tmps.length() - 1);
                                tmps.append("]");
                                sDetails.append(tmps.toString());
                            }
                            else {
                                isCreateNewFile = true;
                                sDetails.append("[" + newJarFileName + "]");
                                //整理数据结构-begin
                                newJarClasses.add(myJarEntry);
                                //整理数据结构-end
                            }
                            sDetails.append("\n");
                        }
                    }
                    //整理数据结构
                    patchFileGroup.put(str_filename,changeFileGroup);
                    newJarGroup.put(str_filename,newJarClasses);

                    fileInputStream.close();
                    jarInputStream.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        tfDetails.setText(sDetails.toString());
    }

    /**
     *
     * @param cn String 类名称
     * @return Vector 如果cn存在于parent.jartable的keyset中,就返回parent.jartable.get(cn)
     * 如果cn不存在于parent.jartable的keyset中,就返回第一个跟cn同路径的类所在的lib
     * 如果以上两者都不,就返回空
     */
    private Vector getJarNameByClassName(String cn){
        if (parent.jartable.get(cn)!=null)
            return (Vector)parent.jartable.get(cn);
        String path1 = cn.substring(0,cn.lastIndexOf("/"));//取得该class的路径
        Iterator it1 = parent.jartable.keySet().iterator();
        while (it1.hasNext()){
            String classname = (String)it1.next();
            //James.W 增加新文件路径判断，如果路径不存在的处理
            int lie=classname.lastIndexOf("/");
            String path2=""; 
            if(lie!=-1){
            	path2= classname.substring(0,classname.lastIndexOf("/"));
            }
            if (path1.equals(path2)){
                return (Vector)parent.jartable.get(classname);
            }
        }
        return null;
    }

    private boolean checkPatchValid() {
          return true;
    }

    private void actionAddPatch() {
        if (!checkPatchValid()) {
            return;
        }
        try {
            //备份patch文件
            backupFiles(patchFiles, createBackupPath(parent.PATH_PATCHBACKU_FORPATCH));
            //备份被修改的文件
            Vector v = new Vector(this.changeFiles.keySet());
            File[] changeFiles = new File[v.size()];
            for (int i = 0; i < v.size(); i++) {
                changeFiles[i] = new File(parent.PATH_CURRENT + parent.PATH_LIB + File.separator + v.get(i));
            }
            backupFiles(changeFiles, createBackupPath(parent.PATH_PATCHBACKU_FORCHANGE));
            //修改文件打patch
            doAddPatch();
            //写历史记录
            writeHistory();
            //重新缓冲
            parent.initLibJars();
            //按钮不可用
            btOK.setEnabled(false);

            NovaMessage.show(parent,"补丁安装成功，安装记录请查看历史记录。");
        } catch (Exception ex) {
            NovaMessage.show(parent, "安装补丁失败：" + ex.getMessage()+"\n请手动恢复当前操作，原有文件备份于：\n"+
                             parent.PATH_CURRENT + parent.PATH_PATCHBACKUP + File.separator + currentTime +
                             File.separator + parent.PATH_PATCHBACKU_FORCHANGE);
        }

    }

    private String createBackupPath(String path) {
        File patchPath = new File(parent.PATH_CURRENT + parent.PATH_PATCHBACKUP);
        if (!patchPath.exists()) {
            patchPath.mkdir();
        }
        patchPath = new File(parent.PATH_CURRENT + parent.PATH_PATCHBACKUP + File.separator + currentTime);
        if (!patchPath.exists()) {
            patchPath.mkdir();
        }
        patchPath = new File(parent.PATH_CURRENT + parent.PATH_PATCHBACKUP + File.separator + currentTime +
                             File.separator + path);
        patchPath.mkdir();
        return patchPath.getAbsolutePath();
    }

    private void writeHistory() throws Exception {
        File fHistory = new File(parent.PATH_CURRENT + parent.PATH_PATCHBACKUP + File.separator + parent.HISTORYFILENAME);
        FileWriter fWriter = new FileWriter(fHistory, true);
        PrintWriter out = new PrintWriter(fWriter);
        out.println(parent.LOG_SEGMENT_TITLE);
        //时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date=sdf.parse(currentTime);
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        out.println(parent.LOG_SEGMENT_DATE+" : " + sdf2.format(date));
        //目录
        out.println(parent.LOG_SEGMENT_DIRECTORY+" : " + currentTime);
        //记录修改的文件
        StringBuffer cFiles = new StringBuffer();
        Iterator it1 = changeFiles.keySet().iterator();
        while (it1.hasNext()) {
            cFiles.append(it1.next());
            cFiles.append(" , ");
        }
        out.println(parent.LOG_SEGMENT_CHANGEDFILES+" : " + cFiles.substring(0, cFiles.length() > 3 ? cFiles.length() - 3 : cFiles.length()));
        //记录新增的文件
        if(isCreateNewFile) {
            out.println(parent.LOG_SEGMENT_CREATEDFILES + " : " + newJarFileName);
        } else {
            out.println(parent.LOG_SEGMENT_CREATEDFILES + " : ");
        }
        //记录patch文件
        StringBuffer pFiles = new StringBuffer();
        for (int i = 0; i < patchFiles.length; i++) {
            pFiles.append(patchFiles[i].getName());
            pFiles.append(" , ");
        }
        out.println(parent.LOG_SEGMENT_PATCHFILES+" : " + pFiles.substring(0, pFiles.length() - 3));

        out.println();
        out.println(parent.LOG_SEGMENT_DETAILS+" : ");
        //详细内容
        String s = tfDetails.getText();
        StringTokenizer token = new StringTokenizer(s, "\n");
        while (token.hasMoreTokens()) {
            out.println(token.nextToken());
        }
        out.println(parent.LOG_SEGMENT_END);
        out.println();
        out.println();
        out.println();
        out.flush();
        out.close();
        fWriter.close();
    }

    private void backupFiles(File[] files, String destination) throws Exception {
        if (files != null && files.length > 0) {
            for (int i = 0; i < files.length; i++) {
                parent.copyFile(files[i], new File(destination + File.separator + files[i].getName()));
            }
        }
    }

    public void btOK_actionPerformed(ActionEvent e) {
        if (NovaMessage.confirm(parent, "确定执行安装补丁操作么？")) {
            new NovaSplashWindow(parent, "正在安装补丁文件,请稍候。。。", new AbstractAction() {
                private static final long serialVersionUID = -287905438900197436L;
                public void actionPerformed(ActionEvent e) {
                    actionAddPatch();
                }
            });
        }
    }

    //加入补丁
    private void doAddPatch() throws Exception {
        //遍历每个patch文件，与修改的文件进行合并
        Iterator it = patchFileGroup.keySet().iterator();
        while(it.hasNext()) {
            String patchFileName=(String)it.next();
            HashMap changeMap=(HashMap)patchFileGroup.get(patchFileName);
            if(changeMap.size()>0) {
                Iterator itChange=changeMap.keySet().iterator();
                //遍历当前patch对应的每个需要修改的文件
                while(itChange.hasNext()) {
                    String changeFileName=(String)itChange.next();
                    System.out.println("从："+changeFileName+"  更新："+patchFileName);
                    Vector vClass=(Vector)changeMap.get(changeFileName);
                    mergePatchFile(changeFileName,patchFileName,vClass);
                }
            }
        }
        //生成新的jar文件
        createPatchFile();
    }

    //对每一个补丁文件处理
    public void mergePatchFile(String changeFileName, String patchFileName,Vector vClass) throws Exception {
        JarOutputStream jarOut = null;
        JarFile origFile=null;
        JarFile patchFile = null;
        try {
            if(vClass==null || vClass.size()==0) {
                return;
            }
            patchFile = new JarFile(parent.PATH_CURRENT + parent.PATH_PATCHBACKUP + File.separator + currentTime +
                                    File.separator + parent.PATH_PATCHBACKU_FORPATCH+ File.separator +patchFileName);
            //源文件可能前面被修改过，因此这里要根据lib下的文件创建临时文件
            parent.copyFile(new File(parent.PATH_CURRENT + parent.PATH_LIB + File.separator +changeFileName),
                            new File(parent.PATH_CURRENT + parent.PATH_PATCHBACKUP + File.separator +changeFileName));
            origFile = new JarFile(parent.PATH_CURRENT + parent.PATH_PATCHBACKUP + File.separator +changeFileName);

            jarOut = new JarOutputStream(new FileOutputStream(parent.PATH_CURRENT+parent.PATH_LIB+File.separator+changeFileName));

            //建立更新缓冲
            HashMap ordermap=new HashMap();
            HashMap filesrc=new HashMap();
            ArrayList paths=new ArrayList();
            //把旧文件的所有内容放到缓冲 begin
            Enumeration enumeration=origFile.entries();
            while(enumeration.hasMoreElements()) {
            	JarEntry jarEntry = (JarEntry)enumeration.nextElement();
                String cls=jarEntry.getName();                
                System.out.println("处理文件【"+patchFileName+"】内容："+cls);
                ordermap.put(cls, jarEntry);
                filesrc.put(cls, origFile);
                paths.add(cls);
            }//把旧文件的所有内容放到缓冲 end
            //把补丁文件的所有内容放到缓冲 begin
            enumeration=patchFile.entries();
            while(enumeration.hasMoreElements()) {
            	JarEntry jarEntry = (JarEntry)enumeration.nextElement();
                String cls=jarEntry.getName();                
                if(ordermap.get(cls)==null){
	                System.out.println("加入新文件【"+patchFileName+"】内容："+cls);
	                ordermap.put(cls, jarEntry);
	                filesrc.put(cls, patchFile);
	                paths.add(cls);
                }else{
                	if(jarEntry.isDirectory() || parent.isInvalidPatchFile(jarEntry.getName())){
                		continue;
                	}
                	System.out.println("更新旧文件【"+patchFileName+"】内容："+cls);                	
	                ordermap.put(cls, jarEntry);//覆盖旧文件	       
	                filesrc.put(cls, patchFile);
                }
            }//把补丁文件的所有内容放到缓冲 begin
            //生产目标文件
            long fsize=0L;
            for(int i=0;i<paths.size();i++){
            	JarEntry jarEntry = (JarEntry)ordermap.get((String)paths.get(i));
            	jarOut.putNextEntry(jarEntry);
            	InputStream in=((JarFile)filesrc.get((String)paths.get(i))).getInputStream(jarEntry);            	
            	BufferedInputStream bIn = new BufferedInputStream(in, parent.BUFFERSIZE);
                int count;
                
                byte data[] = new byte[parent.BUFFERSIZE];
                while ( (count = bIn.read(data, 0, parent.BUFFERSIZE)) != -1) {
                    jarOut.write(data, 0, count);
                    fsize+=count;
                }
            }
            System.out.println("写入文件："+fsize);
            
            
            /*
            Enumeration enumeration=origFile.entries();            
            while(enumeration.hasMoreElements()) {
                JarEntry jarEntry = (JarEntry)enumeration.nextElement();
                String cls=jarEntry.getName();                
                System.out.println("处理文件【"+patchFileName+"】内容："+cls);
                if(cls.endsWith("BillListPanel$29.class")){
                	System.out.println("处理文件！！！！！【"+patchFileName+"】内容："+cls);
                }
                //System.out.println("升级文件【"+changeFileName+"】内容："+jarEntry.getName()
                //		+" 压缩大小："+jarEntry.getCompressedSize()+" 原文件大小："+jarEntry.getSize());
                InputStream in;
                int position=findJarEntryPosition(vClass,jarEntry);
                if(position!=-1) {
                    jarEntry=(JarEntry)vClass.get(position);
                    System.out.println("补丁文件【"+patchFileName+"】内容："+jarEntry.getName());
                    //System.out.println("补丁文件【"+patchFileName+"】内容："+jarEntry.getName()
                    //		+" 压缩大小："+jarEntry.getCompressedSize()+" 原文件大小："+jarEntry.getSize());
                    jarOut.putNextEntry(jarEntry);
                    in = patchFile.getInputStream(jarEntry);
                } else {
                    jarOut.putNextEntry(jarEntry);
                    in = origFile.getInputStream(jarEntry);
                }
                BufferedInputStream bIn = new BufferedInputStream(in, parent.BUFFERSIZE);
                int count;
                long fsize=0L;
                byte data[] = new byte[parent.BUFFERSIZE];
                while ( (count = bIn.read(data, 0, parent.BUFFERSIZE)) != -1) {
                    jarOut.write(data, 0, count);
                    fsize+=count;
                }
                //System.out.println("写入文件："+fsize);
                
            }
            */
            jarOut.flush();
            jarOut.finish();
            patchFile.close();
            origFile.close();
            //删除临时复制的文件
            new File(parent.PATH_CURRENT + parent.PATH_PATCHBACKUP + File.separator +changeFileName).delete();
        } catch (Exception ex) {
        	ex.printStackTrace();
            throw ex;
        } finally {
            try {
                jarOut.close();
            }
            catch (IOException ex1) {
            }
        }
    }

    private int findJarEntryPosition(Vector v,JarEntry entry) {
        for(int i=0;i<v.size();i++) {
            if(((JarEntry)v.get(i)).getName().equals(entry.getName())) {
                return i;
            }
        }
        return -1;
    }

    private void createPatchFile() throws Exception {
        if(isCreateNewFile) {
            JarOutputStream jarOut = null;
            JarFile patchFile = null;
            try {
                FileOutputStream fileOutputStream=new FileOutputStream(parent.PATH_CURRENT + parent.PATH_LIB + File.separator +newJarFileName);
                jarOut = new JarOutputStream(fileOutputStream);
                Iterator it=newJarGroup.keySet().iterator();
                while(it.hasNext()) {
                    String patchFileName=(String)it.next();
                    Vector vClass=(Vector)newJarGroup.get(patchFileName);
                    if(vClass==null || vClass.size()==0) {
                        continue;
                    }
                    patchFile = new JarFile(parent.PATH_CURRENT + parent.PATH_PATCHBACKUP + File.separator + currentTime +
                                            File.separator + parent.PATH_PATCHBACKU_FORPATCH +  File.separator +patchFileName);
                    for(int i=0;i<vClass.size();i++) {
                        JarEntry jarEntry = (JarEntry) vClass.get(i);
                        jarOut.putNextEntry(jarEntry);
                        InputStream in = patchFile.getInputStream(jarEntry);
                        BufferedInputStream bIn = new BufferedInputStream(in, parent.BUFFERSIZE);
                        int count;
                        byte data[] = new byte[parent.BUFFERSIZE];
                        while ( (count = bIn.read(data, 0, parent.BUFFERSIZE)) != -1) {
                            jarOut.write(data, 0, count);
                        }
                    }
                }
                jarOut.flush();
                jarOut.finish();
                fileOutputStream.close();
                patchFile.close();
            } catch (Exception ex) {
                throw ex;
            } finally {
                try {
                    jarOut.close();
                } catch (IOException ex1) {
                }
            }
        }
    }
}

class UIAddPatchPanel_btOK_actionAdapter implements ActionListener {
    private UIAddPatchPanel adaptee;
    UIAddPatchPanel_btOK_actionAdapter(UIAddPatchPanel adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.btOK_actionPerformed(e);
    }
}

class UIAddPatchPanel_btSelPath_actionAdapter implements ActionListener {
    private UIAddPatchPanel adaptee;
    UIAddPatchPanel_btSelPath_actionAdapter(UIAddPatchPanel adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.btSelPath_actionPerformed(e);
    }
}
