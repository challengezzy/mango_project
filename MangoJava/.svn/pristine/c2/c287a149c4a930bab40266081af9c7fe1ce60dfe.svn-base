/**************************************************************************
 * $RCSfile: GetFnValue.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:38:24 $
 **************************************************************************/

package smartx.framework.metadata.vo.jepfunctions;

import java.util.*;

import org.nfunk.jep.*;
import org.nfunk.jep.function.*;

import smartx.framework.common.bs.*;
import smartx.framework.common.ui.*;

public class GetFnValue extends PostfixMathCommand {

    private String str_function;

    private int li_type = -1;

    public GetFnValue() { // 这个参数必须要有，，，TNND，搞了半天，真是太不爽了.
        numberOfParameters = -1;
    }

    public GetFnValue(int _type) {
        numberOfParameters = -1;
        li_type = _type;
    }

    public void run(Stack inStack) throws ParseException {
        checkStack(inStack);

        String[] str_pa = new String[curNumberOfParameters - 1];
        for (int i = str_pa.length - 1; i >= 1; i--) { //倒叙获得函数参数
            str_pa[i] = (String) inStack.pop();
        }
        Object obj_fn = inStack.pop(); //最后获得函数名

        if (obj_fn != null) {
            str_function = (String) obj_fn;
        } else {
            throw new ParseException("Invalid parameter function name");
        }
        String str_data = null;
        try {
            if (li_type == JepFormulaParse.li_ui) { // 如果是客户端调用
                str_data = UIUtil.callFunctionByReturnVarchar(null, str_function, str_pa);
            } else if (li_type == JepFormulaParse.li_bs) { // 如果是Server端调用
                str_data = new CommDMO().callFunctionReturnStrByDS(null, str_function, str_pa);
            }
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
        inStack.push(str_data); //
    }
}
/**************************************************************************
 * $RCSfile: GetFnValue.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:38:24 $
 *
 * $Log: GetFnValue.java,v $
 * Revision 1.2  2007/05/31 07:38:24  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 06:03:31  qilin
 * no message
 *
 * Revision 1.2  2007/03/07 02:25:01  shxch
 * *** empty log message ***
 *
 * Revision 1.1  2007/03/07 02:04:18  shxch
 * *** empty log message ***
 *
 * Revision 1.5  2007/03/02 05:16:43  shxch
 * *** empty log message ***
 *
 * Revision 1.4  2007/03/02 05:02:50  shxch
 * *** empty log message ***
 *
 * Revision 1.3  2007/03/01 07:20:47  shxch
 * *** empty log message ***
 *
 * Revision 1.2  2007/01/30 04:59:24  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/
