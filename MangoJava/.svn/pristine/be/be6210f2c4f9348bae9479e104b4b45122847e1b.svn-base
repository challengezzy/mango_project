/**************************************************************************
 * $RCSfile: TempletParameterRefDialog.java,v $  $Revision: 1.2.8.5 $  $Date: 2010/01/23 05:14:55 $
 **************************************************************************/
package smartx.publics.styletemplet.ui;


import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Constructor;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import smartx.framework.common.ui.UIUtil;
import smartx.framework.common.utils.Sys;
import smartx.framework.common.vo.NovaLogger;
import smartx.framework.common.vo.VectorMap;
import smartx.framework.metadata.ui.componentscard.AbstractRefDialog;
import smartx.framework.metadata.util.UIComponentUtil;
import smartx.framework.metadata.vo.ComBoxItemVO;
import smartx.framework.metadata.vo.RefItemVO;


public class TempletParameterRefDialog extends AbstractRefDialog {
    private static final long serialVersionUID = -7521165672198422057L;
    protected static HashMap InitTempletMap=null;
    static {
    	InitTempletMap=(HashMap)Sys.getInfo("SYS_DESKTOP_STYLETEMPLET");
    }
    protected AbstractTempletRefPars parap = null;
    protected String result = "";
    protected String initstring = null;
    protected HashMap value = null;
    protected int li_closeType = -1;  //0-确认 1-清空 2-退出(取消)
	protected RefItemVO refVO=null;   //返回参照对象
    
    public TempletParameterRefDialog(Container _parent, String _initRefID, HashMap value) {

        super(_parent, _initRefID, value);
        this.value = value;
        this.initstring = _initRefID;
        initialize();
        this.setVisible(true);
    }
    
    public String getTitle() {
        return "参数配置";
    }

    public void initialize() {
        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(getMainPanel());
        this.getContentPane().add(getSthPanel(), BorderLayout.SOUTH);
    }

    private JPanel getSthPanel() {
        
        
        JButton btn_ok = UIComponentUtil.getButton(
            Sys.getSysRes("sys.button.ok.msg"), 
            UIUtil.getImage(Sys.getSysRes("sys.button.ok.icon")),
            UIComponentUtil.getButtonDefaultSize(),
            true,false,
            new ActionListener() {
                public void actionPerformed(ActionEvent a) {
                	if (buildResult()) {
                    	li_closeType=0;
                    	refVO=new RefItemVO(result, result, result);
                        exit();
                    }
                }
            }
        );
        
        JButton btn_cancel = UIComponentUtil.getButton(
            Sys.getSysRes("sys.button.cancel.msg"), 
            UIUtil.getImage(Sys.getSysRes("sys.button.cancel.icon")),
            UIComponentUtil.getButtonDefaultSize(),
            true,false,
            new ActionListener() {
                public void actionPerformed(ActionEvent a) {
                	if (buildResult()) {
                		li_closeType=2;
                        exit();
                    }
                }
            }
        );
        
        
        JButton btn_close = UIComponentUtil.getButton(
            Sys.getSysRes("sys.button.return.msg"), 
            UIUtil.getImage(Sys.getSysRes("sys.button.return.icon")),
            UIComponentUtil.getButtonDefaultSize(),
            true,false,
            new ActionListener() {
                public void actionPerformed(ActionEvent a) {
                	if (buildResult()) {
                		li_closeType=2;
                        exit();
                    }
                }
            }
        );
        
        JPanel rpanel = new JPanel();
        rpanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        rpanel.add(btn_ok);
        rpanel.add(btn_cancel);
        rpanel.add(btn_close);
        return rpanel;
    }

    private boolean buildResult() {
        result = "";
        parap.stopEdit();
        VectorMap pars = parap.getParameters();
        String[][] data = pars.getAllDataAsString();
        if ( ( (ComBoxItemVO) value.get("COMMANDTYPE")).getId().equals("0")) {
            for (int i = 0; i < data.length; i++) {
                result = result + data[i][1];
            }
        } else {
            for (int i = 0; i < data.length; i++) {
                if (data[i][1].equals("")) {
                    JOptionPane.showMessageDialog(this, "请设置全部参数");
                    return false;
                }
                result = result + data[i][0] + "=" + data[i][1] + ";";
            }
            result = result.substring(0, result.length() - 1);
        }

        return true;
    }

    private void setResult(String _result) {
        this.result = _result;
    }

    private void exit() {
    	this.dispose();
    }
    
    /**
     * 返回参照选项值
     * @return
     */
    public RefItemVO getRefVO() {
        return refVO;
    }
    
    
    /**
	 * 对话框的操作类型：确认/取消/关闭
	 * @return
	 */
    public int getCloseType() {
        return li_closeType;
    }

    private JPanel getMainPanel() {
    	try{
    		Class cls = Class.forName((String)InitTempletMap.get(((ComBoxItemVO) value.get("COMMANDTYPE")).getId()+"_refdialog"));
	    	if (initstring != null && !initstring.equals("")) {
	    		Constructor cst=cls.getConstructor(new Class[]{String.class});
	    		parap=(AbstractTempletRefPars)cst.newInstance(new Object[]{initstring});
	        } else {
	        	parap=(AbstractTempletRefPars)cls.newInstance();	        	
	        }
	    	return parap;
    	}catch(Exception e){
    		NovaLogger.getLogger(this).error("获得参数配置panel错误",e);
    		return null;
    	}        
    }

    public int getInitWidth() {
        return 620;
    }

    public int getInitHeight() {
        return 400;
    }
}
/**************************************************************************
 * $RCSfile: TempletParameterRefDialog.java,v $  $Revision: 1.2.8.5 $  $Date: 2010/01/23 05:14:55 $
 *
 * $Log: TempletParameterRefDialog.java,v $
 * Revision 1.2.8.5  2010/01/23 05:14:55  wangqi
 * *** empty log message ***
 *
 * Revision 1.2.8.4  2010/01/20 10:16:22  wangqi
 * *** empty log message ***
 *
 * Revision 1.2.8.3  2008/09/19 03:42:15  wangqi
 * *** empty log message ***
 *
 * Revision 1.2.8.2  2008/09/19 02:50:17  wangqi
 * *** empty log message ***
 *
 * Revision 1.2.8.1  2008/09/19 02:22:07  wangqi
 * *** empty log message ***
 *
 * Revision 1.2  2007/05/31 07:39:01  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 06:14:29  qilin
 * no message
 *
 * Revision 1.3  2007/02/27 06:57:19  shxch
 * *** empty log message ***
 *
 * Revision 1.2  2007/01/30 04:23:45  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/