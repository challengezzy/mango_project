/**************************************************************************
 * $RCSfile: TestRefDialog.java,v $  $Revision: 1.2.8.1 $  $Date: 2010/01/20 09:54:43 $
 **************************************************************************/
package smartx.framework.metadata.ui.componentscard;


import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JLabel;

import smartx.framework.metadata.vo.RefItemVO;

public class TestRefDialog extends AbstractRefDialog {
	private static final long serialVersionUID = 838531426023313552L;
	protected RefItemVO refVO=null;
	protected int rettype=0;

    public TestRefDialog(Container _parent, String _initRefID, HashMap _map) {
        super(_parent, _initRefID, _map);
        initialize();
    }

    
    

    private void initialize() {
        this.getContentPane().setLayout(new BorderLayout()); //
        this.getContentPane().add(new JLabel("ID" + getInitRefID()), BorderLayout.CENTER); //	
        JButton btn_confirm = new JButton("确定");
        btn_confirm.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onConfirm();
            }
        });

        this.getContentPane().add(btn_confirm, BorderLayout.SOUTH); //	
    }

    private void onConfirm() {
    	this.refVO=new RefItemVO("123456", "123456", "123456");
    	this.dispose();
    }

    /**
     * 对话框标题
     */
    public String getTitle() {
        return "自定义的参照例子";
    }
    
    /**
	 * 对话框的操作类型：确认/取消/关闭
	 * @return 0-确认，1-取消，2-关闭
	 */
	public int getCloseType() {		
		return this.rettype;
	}

	/**
     * 返回参照选项值
     * @return
     */
	public RefItemVO getRefVO() {
		return null;
	}
    
    
    

}
/**************************************************************************
 * $RCSfile: TestRefDialog.java,v $  $Revision: 1.2.8.1 $  $Date: 2010/01/20 09:54:43 $
 *
 
 **************************************************************************/