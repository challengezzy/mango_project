package smartx.publics.databatch.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

import smartx.framework.metadata.ui.NovaEvent;
import smartx.framework.metadata.ui.NovaEventListener;
import smartx.framework.metadata.ui.NovaMessage;
import smartx.publics.databatch.bs.*;
import smartx.publics.styletemplet.ui.AbstractStyleFrame;

import java.util.ArrayList;


/**
 * <p>Title: 将客户端excel导入到服务端oracle的界面</p>
 * <p>Description: NOVA </p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: Gxlu inc., rm2 </p>
 * @author Yangh Huan
 * @version 1.0
 */
public class DataBatchFrame extends AbstractStyleFrame implements NovaEventListener{
	
	private static final long serialVersionUID = 3262282355206416248L;
	
	private JTextField jtf_excelPath = null;
	    
	private JTextField jtf_xmlPath = null;

	private JButton jbt_path = null;
	    
	private JButton jbt_xmlpath = null;
	    
	private int sucCount ;
		 
	private int failedCount;
		 
	private int reCount;
		 
	private int totalNum;
		 
	private ArrayList failedStart = null;
		 
	private String resultMessage =  null;
		 
	private boolean flag = false;
	    
	   
	public DataBatchFrame() {
		init();
	}

	protected void init() {
		this.setLayout(new BorderLayout());
		this.setTitle("[批量增删]");
		this.setSize(new Dimension(1000,700));
		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(getBody(),BorderLayout.CENTER); // 内容栏
		 
		this.setVisible(true);
		this.toFront();
	}
	
	private JPanel getBody() {
		JPanel rpanel = new JPanel();
		rpanel.setLayout(new BorderLayout());
		rpanel.setPreferredSize(new Dimension(1000,60));

		JPanel jpn_excelPath = new JPanel();
        JLabel jlb_excelPath = new JLabel("导入路径:");
        jlb_excelPath.setPreferredSize(new Dimension(80, 20));
        jlb_excelPath.setHorizontalAlignment(JLabel.RIGHT);
        jtf_excelPath = new JTextField(System.getProperty("user.home") + "\\桌面");
        jtf_excelPath.setPreferredSize(new Dimension(320, 20));
        jbt_path = new JButton("路径…");
        jbt_path.setPreferredSize(new Dimension(75, 20));
        jbt_path.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onPath();
            }
        });
        jpn_excelPath.setLayout(new FlowLayout());
        jpn_excelPath.add(jlb_excelPath);
        jpn_excelPath.add(jtf_excelPath);
        jpn_excelPath.add(jbt_path);
        
        JPanel jpn_xmlpath = new JPanel();
        JLabel jlb_xmlpath = new JLabel("选择xml文件:");
        jlb_xmlpath.setPreferredSize(new Dimension(80, 20));
        jlb_xmlpath.setHorizontalAlignment(JLabel.RIGHT);
        jtf_xmlPath = new JTextField(System.getProperty("user.home") + "\\桌面");
        jtf_xmlPath.setPreferredSize(new Dimension(320, 20));
        jbt_xmlpath = new JButton("路径…");
        jbt_xmlpath.setPreferredSize(new Dimension(75, 20));
        jbt_xmlpath.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onXmlPath();
            }
        });
        jpn_xmlpath.add(jlb_xmlpath);
        jpn_xmlpath.add(jtf_xmlPath);
        jpn_xmlpath.add(jbt_xmlpath);

        rpanel.add(jpn_excelPath, BorderLayout.NORTH);
        //rpanel.add(jpn_xmlpath, BorderLayout.CENTER);
        rpanel.add(getBtnPanel(), BorderLayout.SOUTH);
       

        return rpanel;
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
			 DataBatchDeal databatchdeal = new DataBatchDeal();
				databatchdeal.DataBatch( "test", 0, "datasource_usermgmt");
			
			 /* String dataSourceName = "datasource_usermgmt";
			 String tableName = "test";
			 String excelPath = jtf_excelPath.getText();
			 String xmlPath = jtf_xmlPath.getText();
			 if (!excelPath.endsWith(DataBatchConstant.IMPORT__FILE_TAIL) || excelPath.equals("") || excelPath.equals(null)) {
				 NovaMessage.show(this, "导入文件格式不是Excel格式！");
			} else {
				DataBatchDeal databatchdeal = new DataBatchDeal();
				databatchdeal.DataBatch(excelPath, xmlPath, tableName, 0, dataSourceName);
				java.io.InputStream excelStream = new FileInputStream(new File(excelPath));
				java.util.Hashtable mapResult = (java.util.Hashtable)databatchdeal.insertDataBatch(excelStream, tableName, dataSourceName, 0);
				System.out.println("......mapResult"+mapResult);
				sucCount = ((Integer)mapResult.get("sucRowNum")).intValue();
				failedCount = ((Integer)mapResult.get("failedRowNum")).intValue();
				reCount =  ((Integer)mapResult.get("reRowNum")).intValue();
				totalNum =  ((Integer)mapResult.get("totalNum")).intValue();
				System.out.println("......reCount"+reCount);
				NovaMessage.show(this, getResultMessage());
			}*/
	    }catch(Exception e){
	    	NovaMessage.show(this, "导入失败" + getResultMessage() + e.getMessage());
	    }
}
	 public String getResultMessage(){
		 String newline = System.getProperty("line.separator");
		 resultMessage = "一共有记录"+totalNum+"条";
		 if(sucCount>0){
			 resultMessage += newline + DataBatchConstant.IMPORT_SUCCESS_HEAD+sucCount+DataBatchConstant.IMPORT__SUCCESS_TAIL;	
		 }
		 if(reCount>0){
			 resultMessage += newline + "没有导入的重复记录有" + reCount +"条;";
		 }
		 if(flag){
			 resultMessage += newline + "全部成功！";
		 }
		 if(failedCount > 0 ){
			 resultMessage += newline + "失败有"+failedCount+DataBatchConstant.IMPORT__SUCCESS_TAIL;
		 }
		 if(failedStart!=null ){
			 if(failedStart.size()!=0){
				 resultMessage += newline + "失败记录从第"+(sucCount+1)+"条" + failedStart.toString() +"开始";
			 }
		 }
		 return resultMessage;
	 }
	 protected void onCancel() {
	        this.dispose();
	    }

	public void onValueChanged(NovaEvent _evt) {
		if (_evt.getChangedType() == NovaEvent.CardChanged) {

		} else if (_evt.getChangedType() == NovaEvent.ListChanged) { // 如果是列表变化

		}
	}
	
	

}
