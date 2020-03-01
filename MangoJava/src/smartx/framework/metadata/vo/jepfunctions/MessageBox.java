/**************************************************************************
 * $RCSfile: MessageBox.java,v $  $Revision: 1.1.2.1 $  $Date: 2010/01/19 06:27:33 $
 **************************************************************************/

package smartx.framework.metadata.vo.jepfunctions;


import java.util.Stack;

import javax.swing.JApplet;

import org.nfunk.jep.ParseException;
import org.nfunk.jep.function.PostfixMathCommand;

import smartx.framework.common.ui.NovaConstants;
import smartx.framework.common.utils.Sys;
import smartx.framework.metadata.ui.NovaMessage;

/**
 * 消息提示框
 * 用于在界面公式中提示指定的消息。
 * 应用范例：给某字段赋值，如果某条件不满足，则提示，否则直接赋值
 * FIELD=>if() messageBox("","#,##0.00");
 * @author James.W
 *
 */
public class MessageBox extends PostfixMathCommand {

	public MessageBox() {
        numberOfParameters = 3;
    }
	
	public void run(Stack inStack) throws ParseException {    	
    	checkStack(inStack);
        
        //判断返回值
        String rt=(String)inStack.pop();
        if(rt==null){rt="";}
        
        //判断消息类型 info/warn/error
        String type=(String)inStack.pop();
        if(rt==null){type="info";}
        int msgtype="warn".equalsIgnoreCase(type) ? NovaConstants.MESSAGE_WARN : ("error".equalsIgnoreCase(type) ? NovaConstants.MESSAGE_ERROR : NovaConstants.MESSAGE_INFO);
        
        //判断消息体
        String msg = (String)inStack.pop();
        if(msg==null){msg="没有什么可以提示的";}
        
        //提示消息
        JApplet applet= (JApplet)Sys.getInfo("applet");
        NovaMessage.show(applet, msg, msgtype);
        
        //返回值
        inStack.push(rt);
        
    }
	
}

/**************************************************************************
 * $RCSfile: MessageBox.java,v $  $Revision: 1.1.2.1 $  $Date: 2010/01/19 06:27:33 $
 **************************************************************************/
