/**************************************************************************
 * $RCSfile: TestToString.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:38:25 $
 **************************************************************************/

package smartx.framework.metadata.vo.jepfunctions;

import java.util.*;

import org.nfunk.jep.*;
import org.nfunk.jep.function.*;

public class TestToString extends PostfixMathCommand {

    public TestToString() {
        numberOfParameters = -1;
    }

    public void run(Stack inStack) throws ParseException {
        checkStack(inStack);
        Object[] obj = new Object[curNumberOfParameters];

        String str_return = "";
        for (int i = 0; i < obj.length; i++) {
            obj[i] = inStack.pop();
            str_return = str_return + obj[i].toString();
        }
        inStack.push(str_return);
    }

}
/**************************************************************************
 * $RCSfile: TestToString.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:38:25 $
 *
 * $Log: TestToString.java,v $
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