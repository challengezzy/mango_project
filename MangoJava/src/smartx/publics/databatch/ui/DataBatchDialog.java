package smartx.publics.databatch.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import smartx.framework.metadata.ui.NovaDialog;
import smartx.framework.metadata.ui.NovaEvent;
import smartx.framework.metadata.ui.NovaEventListener;
import smartx.framework.metadata.ui.NovaMessage;
import smartx.publics.databatch.bs.DataBatchConstant;



/**
 * <p>Title:导入到数据库的UI界面</p>
 * <p>Description: NOVA </p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: Gxlu inc., rm2 </p>
 * @author Yang Huan
 * @version 1.0
 */
public class DataBatchDialog extends NovaDialog implements NovaEventListener { 

	 /**
	 * 
	 */
	private static final long serialVersionUID = -7845769396189088196L;

	private JTextField jtf_excelPath = null;
	    
	private JTextField jtf_xmlPath = null;

	private JButton jbt_excelPath = null;
	    
	// private JButton jbt_xmlPath = null;
	 
	private String tableName = null;
	 
	private int importType = 0;
	 
	private String dataSourceName = null;
	 
	private int sucCount ;
	 
	private int failedCount;
	 
	private ArrayList failedStart = null;
	 
	private String resultMessage =  null;
	 
	private boolean flag = false;
	 
	 public DataBatchDialog(){
		 initDlg();
		 
	 }
	 public DataBatchDialog(String in_tableName, int in_importType,String in_dataSourceName){
		 tableName = in_tableName;
		 importType = in_importType;
		 dataSourceName = in_dataSourceName;
		 initDlg();
	 }
	 
	/**
	 * initDlg
	 */
	public void initDlg() {
		this.setLayout(new BorderLayout());
		this.setTitle("[批量增删]");
		this.setSize(new Dimension(500,200));
		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(getBody(),BorderLayout.CENTER); // 内容栏
		 
		this.setVisible(true);
		this.toFront();
		
	}
	

    public void onValueChanged(NovaEvent _evt){	
    }
    

    private JPanel getBody() {
		JPanel rpanel = new JPanel();
		rpanel.setLayout(new BorderLayout());
		rpanel.setPreferredSize(new Dimension(300,60));
		
		JPanel jpn_path = new JPanel();
		jpn_path.setLayout(new BorderLayout());
		jpn_path.setPreferredSize(new Dimension(300,60));

		JPanel jpn_excelPath = new JPanel();
        JLabel jlb_excelPath = new JLabel(DataBatchConstant.IMPORT__FILE_TITLE);
        jlb_excelPath.setPreferredSize(new Dimension(80, 20));
        jlb_excelPath.setHorizontalAlignment(JLabel.RIGHT);
        jtf_excelPath = new JTextField();//System.getProperty("user.home") + "\\桌面"
        jtf_excelPath.setPreferredSize(new Dimension(320, 20));
        jbt_excelPath = new JButton(DataBatchConstant.IMPORT__FILE_PATH);
        jbt_excelPath.setPreferredSize(new Dimension(75, 20));
        jbt_excelPath.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onPath();
            }
        });
        jpn_excelPath.setLayout(new FlowLayout());
        jpn_excelPath.add(jlb_excelPath);
        jpn_excelPath.add(jtf_excelPath);
        jpn_excelPath.add(jbt_excelPath);
        
        //用户选择配置文件的界面.暂时不需要
        /*JPanel jpn_xmlPath = new JPanel();
        JLabel jlb_xmlPath = new JLabel("选择xml文件:");
        jlb_xmlPath.setPreferredSize(new Dimension(80, 20));
        jlb_xmlPath.setHorizontalAlignment(JLabel.RIGHT);
        jtf_xmlPath = new JTextField();
        jtf_xmlPath.setPreferredSize(new Dimension(320, 20));
        jbt_xmlPath = new JButton("路径…");
        jbt_xmlPath.setPreferredSize(new Dimension(75, 20));
        jbt_xmlPath.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onXmlPath();
            }
        });
        jpn_xmlPath.add(jlb_xmlPath);
        jpn_xmlPath.add(jtf_xmlPath);
        jpn_xmlPath.add(jbt_xmlPath);*/
        
        jpn_path.add(jpn_excelPath, BorderLayout.NORTH);
        //jpn_path.add(jpn_xmlPath, BorderLayout.SOUTH);    
       
        rpanel.add(jpn_path, BorderLayout.NORTH);
        if(tableName!=null ){
        	rpanel.add(getPanelIn(), BorderLayout.CENTER);
        }
        rpanel.add(getBtnPanel(), BorderLayout.SOUTH);
       

        return rpanel;
	}
    
    
    private JPanel getPanelIn(){
    	 JPanel jpn_tableName = new JPanel();
         JLabel jlb_tableName = new JLabel("数据库表名为:");
         jlb_tableName.setPreferredSize(new Dimension(80, 20));
         JTextField jtf_tableName = new JTextField(tableName);
         jtf_tableName.setPreferredSize(new Dimension(80, 20));
         jtf_tableName.setEditable(false);
         jpn_tableName.add(jlb_tableName);
         jpn_tableName.add(jtf_tableName);
         JPanel jpn_importType = new JPanel();
         JLabel jlb_importType = new JLabel("导入方式为:");
         jlb_importType.setPreferredSize(new Dimension(75, 20));
         JTextField jtf_importType = new JTextField(importType==0?"插入":"删除");
         jtf_importType.setPreferredSize(new Dimension(75, 20));
         jtf_importType.setEditable(false);
         jpn_importType.add(jlb_importType);
         jpn_importType.add(jtf_importType);
         
         JPanel jpn_in = new JPanel();
         jpn_in.setLayout(new BorderLayout());
         jpn_in.setPreferredSize(new Dimension(75,60));
         jpn_in.add(jpn_tableName, BorderLayout.SOUTH);
         jpn_in.add(jpn_importType, BorderLayout.NORTH);
         
         return jpn_in;
    }
	
	
	 private JPanel getBtnPanel() {
	        JPanel jpn_btn = new JPanel();
	        JButton jbt_in = new JButton("导入");
	        JButton jbt_cancel = new JButton("关闭");
	        jbt_in.setPreferredSize(new Dimension(75, 20));
	        jbt_cancel.setPreferredSize(new Dimension(75, 20));
	        jbt_in.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                onIn();
	            }
	        });
	        jbt_cancel.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                onCancel();
	            }
	        });

	        jpn_btn.setLayout(new FlowLayout());
	        jpn_btn.add(jbt_in);
	        jpn_btn.add(jbt_cancel);

	        return jpn_btn;
	    }
	 
	 
	 public void onXmlPath(){
		  JFileChooser xmlChooser = new JFileChooser();
		  xmlChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
	        int result = xmlChooser.showSaveDialog(this);
	        if (result != 0) {
	            return;
	        }
	        jtf_xmlPath.setText(xmlChooser.getSelectedFile().getPath());
			
		}
	    protected void onPath() {
	        JFileChooser chooser = new JFileChooser();
	        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
	        int result = chooser.showSaveDialog(this);
	        if (result != 0) {
	            return;
	        }
	        jtf_excelPath.setText(chooser.getSelectedFile().getPath());
	    }
	    
	    
	 protected void onIn() {
		 try{
			 String excelPath = jtf_excelPath.getText();
			 //String xmlPath = jtf_xmlPath.getText();
			 if (!excelPath.endsWith(DataBatchConstant.IMPORT__FILE_TAIL) || excelPath.equals("") || excelPath.equals(null)) {
				 NovaMessage.show(this, DataBatchConstant.IMPORT__FILE_NOTEXCEL);
			} else {
				System.out.println("...excelPath="+ excelPath + "tableName=" + tableName);
				
				DataBatchDeal databatchdeal = new DataBatchDeal();
				//暂时不提供支持配置文件由用户上传的方式
				//databatchdeal.DataBatch(excelPath, xmlPath, tableName, 0, dataSourceName);
				flag = databatchdeal.DataBatch(excelPath, tableName, importType, dataSourceName);
				
				sucCount = databatchdeal.getSuccCount();
				failedCount = databatchdeal.getFailedCount();
				failedStart = databatchdeal.getFailedStart();
				System.out.println(getResultMessage());
				NovaMessage.show(this, getResultMessage());
			}

		 }catch(Exception e){
			 NovaMessage.show(this, e.getMessage());
			 //System.out.println(getResultMessage());
			 System.out.println(DataBatchConstant.IMPORT_FIELD);
	         e.printStackTrace();
		 }
	    }
	 
	 public String getResultMessage(){
		 resultMessage = "";
		 if(sucCount>0){
			 resultMessage += "\n"+DataBatchConstant.IMPORT_SUCCESS_HEAD+sucCount+DataBatchConstant.IMPORT__SUCCESS_TAIL;	
		 }
		 if(flag){
			 resultMessage += "\n全部成功！";
		 }
		 if(failedCount > 0 ){
			 resultMessage +="\n"+"失败有"+failedCount+DataBatchConstant.IMPORT__SUCCESS_TAIL;
		 }
		 if(failedStart!=null ){
			 if(failedStart.size()!=0){
				 resultMessage += "\n失败记录从第"+(sucCount+1)+"条" + failedStart.toString() +"开始";
			 }
		 }
		 return resultMessage;
	 }
	 
	 protected void onCancel() {
	        this.dispose();
	 }
	 
	 
	 public int getFailedCount(){
			return failedCount;
	 }
		
	 
	 public int getSuccCount(){
			return sucCount;
	 }
	 
		
	 public ArrayList getFailedStart(){
			return failedStart;
	 }

}
