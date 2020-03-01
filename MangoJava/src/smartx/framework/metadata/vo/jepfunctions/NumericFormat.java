/**************************************************************************
 * $RCSfile: NumericFormat.java,v $  $Revision: 1.1.2.1 $  $Date: 2010/01/04 08:45:58 $
 **************************************************************************/

package smartx.framework.metadata.vo.jepfunctions;


import java.text.DecimalFormat;
import java.util.Stack;

import org.nfunk.jep.ParseException;
import org.nfunk.jep.function.PostfixMathCommand;

/**
 * 格式化数值函数，类似0.00，#,##0.00，￥#,##0.00等等
 * CODE=>numericFormat("1234567.228","#,##0.00");
 */
public class NumericFormat extends PostfixMathCommand {
    private String def_format="#,##0.00";

    public NumericFormat() {
        numberOfParameters = 2;
    }

    public void run(Stack inStack) throws ParseException {
        Double v=null;
    	
    	checkStack(inStack);
        
        
        //判断格式化参数
        String fmt=(String)inStack.pop();
        if(fmt==null||fmt.equals("")){
        	fmt=def_format;
        }
        
        //判断数值是否存在
        Object val = inStack.pop();
        if (val instanceof Double) {
        	v=(Double)val;
        }
        
        //格式化
        inStack.push((new DecimalFormat(fmt)).format(v));
        
    }
}
/**************************************************************************
 * $RCSfile: NumericFormat.java,v $  $Revision: 1.1.2.1 $  $Date: 2010/01/04 08:45:58 $
 *
 **************************************************************************/