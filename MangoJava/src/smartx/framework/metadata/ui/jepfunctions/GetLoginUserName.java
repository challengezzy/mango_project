/**************************************************************************
 * $RCSfile: GetLoginUserName.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:38:25 $
 **************************************************************************/

package smartx.framework.metadata.ui.jepfunctions;

import java.util.*;

import org.nfunk.jep.*;
import org.nfunk.jep.function.*;

import smartx.framework.common.ui.*;

public class GetLoginUserName extends PostfixMathCommand {

    public GetLoginUserName() {
        numberOfParameters = 0;
    }

    public void run(Stack inStack) throws ParseException {
        try {
            checkStack(inStack); //
            String str_result = (String) NovaClientEnvironment.getInstance().get("SYS_LOGINUSER_NAME");
            inStack.push(str_result); //
        } catch (Throwable ex) {
            ex.printStackTrace();
        }

    }

}
/**************************************************************************
 * $RCSfile: GetLoginUserName.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:38:25 $
 *
 * $Log: GetLoginUserName.java,v $
 * Revision 1.2  2007/05/31 07:38:25  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 06:02:53  qilin
 * no message
 *
 * Revision 1.4  2007/03/07 02:47:23  shxch
 * *** empty log message ***
 *
 * Revision 1.1  2007/03/07 02:04:18  shxch
 * *** empty log message ***
 *
 * Revision 1.2  2007/01/30 04:59:24  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/