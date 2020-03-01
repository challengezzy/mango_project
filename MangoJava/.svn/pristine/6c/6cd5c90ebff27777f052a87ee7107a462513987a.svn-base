/**************************************************************************
 * $RCSfile: CommGetValue.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:38:24 $
 **************************************************************************/

package smartx.framework.metadata.vo.jepfunctions;

/**
 * 通用的取数函数!
 * CODE=>commGetValue("xxx.formulafunction.TestFUN","myFn","{登录人员所属区域}","{局向}","{ID}");
 */
import java.util.*;

import org.nfunk.jep.*;
import org.nfunk.jep.function.*;

public class CommGetValue extends PostfixMathCommand {
    private String str_function;

    public CommGetValue() {
        numberOfParameters = -1;
    }

    public void run(Stack inStack) throws ParseException {
        checkStack(inStack);

        String[] str_pa = new String[curNumberOfParameters - 1];
        for (int i = str_pa.length - 1; i >= 2; i--) { // 倒叙获得函数参数
            str_pa[i] = (String) inStack.pop();
        }
        Object method_name = inStack.pop(); // 方法名
        String class_name = (String) inStack.pop(); // 类名
        if (method_name != null) {
            str_function = (String) method_name;
        } else {
            throw new ParseException("Invalid parameter function name");
        }
        Object result = null;
        try {
            result = Class.forName(class_name).getClass().getMethod(
                str_function, new Class[] {String[].class}).invoke(
                    Class.forName(class_name).newInstance(), str_pa);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
        inStack.push(result);
    }
}
/**************************************************************************
 * $RCSfile: CommGetValue.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:38:24 $
 *
 * $Log: CommGetValue.java,v $
 * Revision 1.2  2007/05/31 07:38:24  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 06:03:31  qilin
 * no message
 *
 * Revision 1.1  2007/03/07 02:04:18  shxch
 * *** empty log message ***
 *
 * Revision 1.2  2007/01/30 04:59:23  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/