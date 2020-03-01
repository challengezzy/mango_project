/**************************************************************************
 * $RCSfile: ToString.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:38:25 $
 **************************************************************************/

package smartx.framework.metadata.vo.jepfunctions;

import java.util.*;

import org.nfunk.jep.*;
import org.nfunk.jep.function.*;

public class ToString extends PostfixMathCommand {

    public ToString() {
        numberOfParameters = 1;
    }

    public ToString(int par_count) {
        numberOfParameters = par_count;
    }

    public void run(Stack inStack) throws ParseException {
        try {
            String str_return = "";
            checkStack(inStack); //
            Object param_1 = inStack.pop();
            if (param_1 instanceof Double) {
                Double ld_value = (Double) param_1;
                if (ld_value.intValue() == ld_value.doubleValue()) {
                    str_return = "" + ld_value.intValue();
                } else {
                    str_return = "" + param_1;
                }

            } else {
                str_return = "" + param_1;
            }
            inStack.push(str_return); //
        } catch (Throwable ex) {
            ex.printStackTrace();
        }

    }

}
/**************************************************************************
 * $RCSfile: ToString.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:38:25 $
 *
 * $Log: ToString.java,v $
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