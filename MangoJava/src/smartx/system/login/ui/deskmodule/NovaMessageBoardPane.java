package smartx.system.login.ui.deskmodule;





import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Panel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import smartx.framework.common.ui.NovaRemoteServiceFactory;
import smartx.framework.common.ui.UIUtil;
import smartx.framework.common.vo.HashVO;
import smartx.framework.common.vo.NovaRemoteException;
import smartx.framework.metadata.ui.NovaDialog;
import smartx.system.login.ui.SystemLoginServiceIFC;

/**
 * 默认公告栏实现，
 * 继承了JScrollPane类，并非所有界面组件都要在JSCrollPane中，所以是否需要由各组件自行处理，也可以减少桌面管理器的复杂度。
 * @author James.W
 *
 */
public class NovaMessageBoardPane extends MouseAdapter implements NovaDeskTopModuleIFC {
	private DefaultListModel model=null;   //消息列表模型
	private String msgType=null;      //消息类型
	private String uId=null;          //用户
	private String sql=null;
	private String msgstr=null;       //公告条目格式定义
	private String boardtitle=null;   //公告明细显示时的标题
	private String boardcontent=null; //公告明细显示时的内容
	private String boardbmsg=null;    //公告明细显示时的底部信息
	
	private JList list=null;
	private HashVO[] msgs=null;      //实际消息
	private SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");//new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private SimpleDateFormat sdf2=new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
	
	private HashMap param=null;
	//TODO 根据消息类型和等级获得图标
	private static HashMap msgIconDesign=null;
	static{
		msgIconDesign=new HashMap();
		//default 默认图标集
		ArrayList defaults=new ArrayList();
		defaults.add("images/office/(04,10).png");
		defaults.add("images/office/(16,47).png");
		defaults.add("images/office/(14,47).png");
		defaults.add("images/office/(15,47).png");
		defaults.add("images/office/(28,36).png");
		msgIconDesign.put("default", defaults);				
	}
	
	
	
	
	public NovaMessageBoardPane(){
			
	}
	
	public void setParams(HashMap param){
		this.param=param;
	}
	
	public JComponent getModule()throws Exception{
		this.uId=(String)this.param.get("uId");
		if(uId==null)throw new Exception("用户ID不能为空。");
		this.msgType=(String)this.param.get("type");
		this.msgType=(msgType==null||msgType.trim().equals(""))?null:msgType;
		this.sql=(String)this.param.get("sql");		
		this.msgstr=(String)this.param.get("msgstr");
		this.msgstr=(msgstr==null||msgstr.trim().equals(""))?"【{type}】{title}[{createtime}]":msgstr;
		this.boardtitle=(String)this.param.get("boardtitle");
		this.boardtitle=(boardtitle==null||boardtitle.trim().equals(""))?"【{type}】{title}[{createtime}]":boardtitle;
		this.boardcontent=(String)this.param.get("boardcontent");
		this.boardcontent=(boardcontent==null||boardcontent.trim().equals(""))?"{content}":boardcontent;
		this.boardbmsg=(String)this.param.get("boardbmsg");
		this.boardbmsg=(boardbmsg==null||boardbmsg.trim().equals(""))?"发布时间：{deploytime}":boardbmsg;
		
		if(this.model==null){
			this.exec();
		}
		list = new JList(model);
		
		list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);		
        list.setBorder(BorderFactory.createTitledBorder("消息列表"));		
        list.setCellRenderer(new CellRenderer());
        list.setFont(new Font("宋体", Font.PLAIN, 12)); //
        
        list.addMouseListener(this);
        
		return new JScrollPane(list);
	}
	

	
	/**
	 * 通用接口，执行检索数据操作
	 */
	public void exec() throws Exception {
		if(this.model==null){
			this.model=new DefaultListModel();
		}
		try{
			int rows=this.model.size();
			for(int i=rows-1;i>=0;i--){
				this.model.remove(0);
			}			
		}catch(Exception e){
			System.out.println("清空执行错误");
			e.printStackTrace();
			this.model.clear();
		}
		
		msgs=fetchData();		
		try{
		for(int i=0;i<this.msgs.length;i++){
	    	//【{type}】{title}[{createtime}]
			this.model.addElement(getMsgTitle(i,this.msgstr));
    	}	
		}catch(Exception e){
			System.out.println("加入执行错误");
			e.printStackTrace();			
		}
		//this.list.updateUI();
	}
	
	//整合数据
	private String getMsgTitle(int idx,String str){
		String rt=str;
		String[] keys=this.msgs[idx].getKeys();
		for(int i=0;i<keys.length;i++){
			rt=rt.replaceAll("\\{"+keys[i]+"\\}", this.msgs[idx].getStringValue(keys[i]));
		}
				
		return rt;		
	}
	
	private HashVO[] fetchData()throws Exception{
		//远程查询检索消息
		try {
            /** modified by chenxj begin */
            SystemLoginServiceIFC service = (SystemLoginServiceIFC) NovaRemoteServiceFactory.getInstance().lookUpService(SystemLoginServiceIFC.class); //
            HashVO[] msgs=service.getMessages(this.uId, this.msgType, this.sql);
            return msgs;            
        } catch (InterruptedException e) {
        	throw new Exception("远程检索失去响应！",e);
        } catch (NovaRemoteException e) {
        	throw new Exception("发生远程异常！",e);
        } catch (Exception e) {
            throw new Exception("发生未知异常！",e);
        }
	}
	
	
	public void mouseClicked(MouseEvent e) {
		int index=this.list.locationToIndex(e.getPoint());
		if(index==-1)return;
		
		if (e.getClickCount()==2){
			NovaDialog dialog = new NovaDialog(this.list,"详细信息");
	        dialog.getContentPane().add(getMessageDetailPanel(index));
	        dialog.setSize(700, 500);
	        dialog.setLocation(200, 100);
	        dialog.setVisible(true);
		}
	}
	
    // 详细信息内容面板
    private JPanel getMessageDetailPanel(int index) {
        JPanel rpanel = new JPanel();
        rpanel.setLayout(new BorderLayout());
        HashVO hv = msgs[index];
        if (hv == null) {
            return new JPanel();
        }
        // HashVO只能取得VectorMap,而VectorMap不能取HashMap，所以这里只能自己新构造一个HashMap，给BillCardPanel赋值了.
//        String title = hv.getStringValue("TITLE");
//        Date stime = hv.getDateValue("STARTTIME");
//        Date etime = hv.getDateValue("ENDTIME");
//        String content = hv.getStringValue("CONTENT");
//        Date ctime = hv.getDateValue("DEPLOYTIME");
        //Date ctime = hv.getTimeStampValue("CREATETIME");

//        if (title == null) {
//        	title = "";
//        }
//        if (content == null) {
//        	content = "";
//        }        
//        String str_sdate = sdf2.format(stime);
//        String str_edate = sdf2.format(etime);
//        String str_cdate = sdf2.format(ctime);
        
        
        
        JLabel jlb_title = new JLabel(getMsgTitle(index,this.boardtitle), SwingConstants.CENTER);
        jlb_title.setFont(new Font("宋体", Font.PLAIN, 16));
        JPanel jpn_title = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        jpn_title.add(jlb_title);

        JTextArea jta_info = new JTextArea(getMsgTitle(index,this.boardcontent));
        jta_info.setEditable(false);
//        jta_info.setEnabled(false);
        jta_info.setLineWrap(true);
        jta_info.setWrapStyleWord(true);
        JScrollPane jsp_info = new JScrollPane(jta_info);
        jsp_info.setPreferredSize(new Dimension(400, 350));
        jsp_info.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        jsp_info.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        JPanel btn_panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 10));
        JLabel jlb_time = new JLabel(getMsgTitle(index,this.boardbmsg), SwingConstants.RIGHT);
        jlb_time.setFont(new Font("宋体", Font.PLAIN, 12));
        btn_panel.add(jlb_time);

        rpanel.add(jlb_title, BorderLayout.NORTH);
        rpanel.add(jsp_info, BorderLayout.CENTER);
        rpanel.add(btn_panel, BorderLayout.SOUTH);
        rpanel.add(new JLabel("  "), BorderLayout.WEST);
        rpanel.add(new JLabel("  "), BorderLayout.EAST);
        return rpanel;
    }


	class CellRenderer extends JLabel implements ListCellRenderer{
	   /*类CellRenderer继承JLabel并实作ListCellRenderer.由于我们利用JLabel易于插图的特性，因此CellRenderer继承了JLabel
	    *可让JList中的每个项目都视为是一个JLabel.
	    */
	    CellRenderer(){
	        setOpaque(true);
	    }
	    /*从这里到结束：实作getListCellRendererComponent()方法*/
	    public Component getListCellRendererComponent(JList list,
	                                                  Object value,
	                                                  int index,
	                                                  boolean isSelected,
	                                                  boolean cellHasFocus){   
	        /*我们判断list.getModel().getElementAt(index)所返回的值是否为null,例如上个例子中，若JList的标题是"你玩过哪
	         *些数据库软件"，则index>=4的项目值我们全都设为null.而在这个例子中，因为不会有null值，因此有没有加上这个判
	         *断并没有关系.
	         */
	    	
	    	//获得消息类型
	    	String msgtype=msgs[index].getStringValue("type");
	    	if(msgtype==null||msgtype.trim().equals("")||!msgIconDesign.containsKey(msgtype)){
	    		msgtype="default";
	    	}
	    	ArrayList icons=(ArrayList)msgIconDesign.get(msgtype);
	    	
	    	//获得消息等级
	    	int degree=msgs[index].getIntegerValue("degree").intValue();
	    	if(degree<0){
	    		degree=0;
	    	}else if(degree>=icons.size()){
	    		degree=icons.size()-1;
	    	}
	    	
	    	ImageIcon ic=UIUtil.getImage((String)icons.get(degree));
	    	if (value != null){
	            setText(value.toString());
	            setIcon(ic);	
	            setToolTipText(msgs[index].getStringValue("content"));
	        }
	        if (isSelected) {
	            setBackground(list.getSelectionBackground());
	            setForeground(list.getSelectionForeground());
	        }
	        else {
	            //设置选取与取消选取的前景与背景颜色.
	            setBackground(list.getBackground());
	            setForeground(list.getForeground());
	        }
	        return this;
	    }    
	}

	
		
}

/**
 * 
select * from PUB_MESSAGES 
where type='abc'
order by createtime desc;


delete from PUB_MESSAGES;
    
INSERT INTO PUB_MESSAGES (
   ID, TITLE, STARTTIME, 
   ENDTIME, DEGREE, CONTENT, 
   CREATETIME, TYPE, VERSION) 
VALUES (S_PUB_MESSAGES.nextval, '消息'||to_char(sysdate,'yyyy-MM-dd HH24:mi:ss'), sysdate,
    add_months(sysdate,2), 4, '<b>消息哈哈</b>消息1消息1消息1消息1消息1消息1消息1消息1消息1消息1'||to_char(sysdate,'yyyy-MM-dd HH24:mm:ss'),
    sysdate, 'abc', 0);
    
commit;


 */

/*******************************************************************************
 * $RCSfile: NovaMessageBoardPane.java,v $ $Revision: 1.1.2.4 $ $Date: 2008/08/27 07:00:36 $
 *
 * $Log: NovaMessageBoardPane.java,v $
 * Revision 1.1.2.4  2008/08/27 07:00:36  wangqi
 * *** empty log message ***
 *
 * Revision 1.1.2.3  2008/08/26 09:29:00  wangqi
 * *** empty log message ***
 *
 * Revision 1.1.2.2  2008/07/11 05:24:59  wangqi
 * *** empty log message ***
 *
 *
 *
 ******************************************************************************/

