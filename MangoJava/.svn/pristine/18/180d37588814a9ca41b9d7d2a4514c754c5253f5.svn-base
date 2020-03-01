/**************************************************************************
 * $RCSfile: SubString.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:38:25 $
 **************************************************************************/

package smartx.framework.metadata.vo.jepfunctions;

import java.util.*;

import org.nfunk.jep.*;
import org.nfunk.jep.function.*;

public class SubString extends PostfixMathCommand {

    private int li_begin = 0;

    private int li_end = 0;

    public SubString() {
        numberOfParameters = 3;
    }

    public void run(Stack inStack) throws ParseException {
        checkStack(inStack);
        Object param_1 = inStack.pop();
        Object param_2 = inStack.pop();
        Object param_3 = inStack.pop();
        String str_param;
        if (param_3 != null) {
            str_param = (String) param_3;
        } else {
            throw new ParseException("Invalid parameter string");
        }
        String str_result;
        if (param_2 instanceof Double) {
            li_begin = ( (Double) param_2).intValue();

            if (li_begin <= 0) {
                throw new ParseException("Invalid parameter beginIndex");
            }
            if (param_1 == null) {
                str_result = str_param.substring(li_begin);
            } else if (param_1 instanceof Double) {
                li_end = ( (Double) param_1).intValue();
                if (li_end < li_begin) {
                    throw new ParseException("Invalid parameter endIndex");
                }
                str_result = str_param.substring(li_begin, li_end);
            } else {
                throw new ParseException("Invalid parameter endIndex");
            }
            inStack.push(str_result);
        } else {
            throw new ParseException("Invalid parameter beginIndex");
        }
    }
}
/**************************************************************************
 * $RCSfile: SubString.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:38:25 $
 *
 * $Log: SubString.java,v $
 * Revision 1.2  2007/05/31 07:38:25  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 06:03:31  qilin
 * no message
 *
 * Revision 1.1  2007/03/07 02:04:18  shxch
 * *** empty log message ***
 *
 * Revision 1.2  2007/01/30 04:59:24  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/