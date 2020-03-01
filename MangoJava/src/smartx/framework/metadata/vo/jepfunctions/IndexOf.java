/**************************************************************************
 * $RCSfile: IndexOf.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:38:24 $
 **************************************************************************/

package smartx.framework.metadata.vo.jepfunctions;

import java.util.*;

import org.nfunk.jep.*;
import org.nfunk.jep.function.*;

public class IndexOf extends PostfixMathCommand {
    private char ch_index = 0;

    private String str_index = null;

    public IndexOf() {
        numberOfParameters = 2;
    }

    public void run(Stack inStack) throws ParseException {
        checkStack(inStack);
        Object param_1 = inStack.pop();
        Object param_2 = inStack.pop();
        String str_param;
        int li_result;
        if (param_2 != null) {
            str_param = (String) param_2;
        } else {
            throw new ParseException("Invalid parameter string");
        }
        if (param_1 instanceof Double) {
            ch_index = (char) ( (Double) param_1).intValue();
            li_result = str_param.indexOf(ch_index);
        } else if (param_1 instanceof String) {
            str_index = (String) param_1;
            li_result = str_param.indexOf(str_index);
        } else {
            throw new ParseException("Invalid parameter beginIndex");
        }

        inStack.push(new Integer(li_result));
    }
}
/**************************************************************************
 * $RCSfile: IndexOf.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:38:24 $
 *
 * $Log: IndexOf.java,v $
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