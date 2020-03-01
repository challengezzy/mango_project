/**************************************************************************
 * $RCSfile: ReplaceAll.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:38:25 $
 **************************************************************************/

package smartx.framework.metadata.vo.jepfunctions;

import java.util.*;

import org.nfunk.jep.*;
import org.nfunk.jep.function.*;

public class ReplaceAll extends PostfixMathCommand {

    public ReplaceAll() {
        numberOfParameters = 3;
    }

    public void run(Stack inStack) throws ParseException {
        checkStack(inStack);
        Object param_1 = inStack.pop();
        Object param_2 = inStack.pop();
        Object param_3 = inStack.pop();
        String str_param;
        String str_regex;
        String str_replace;
        if (param_3 != null) {
            str_param = (String) param_3;
        } else {
            throw new ParseException("Invalid parameter string");
        }
        if (param_2 != null) {
            str_regex = (String) param_2;
        } else {
            throw new ParseException("Invalid parameter regx");
        }
        if (param_1 != null) {
            str_replace = (String) param_1;
        } else {
            throw new ParseException("Invalid parameter replacement");
        }
        str_param = str_param.replaceAll(str_regex, str_replace);
        inStack.push(str_param);
    }
}
/**************************************************************************
 * $RCSfile: ReplaceAll.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:38:25 $
 *
 * $Log: ReplaceAll.java,v $
 * Revision 1.2  2007/05/31 07:38:25  qilin
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