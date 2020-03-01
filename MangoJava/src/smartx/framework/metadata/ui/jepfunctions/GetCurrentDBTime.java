/**************************************************************************
 * $RCSfile: GetCurrentDBTime.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:38:25 $
 **************************************************************************/

package smartx.framework.metadata.ui.jepfunctions;

import java.util.*;

import org.nfunk.jep.*;
import org.nfunk.jep.function.*;

import smartx.framework.common.bs.*;
import smartx.framework.common.ui.*;
import smartx.framework.common.vo.*;
import smartx.framework.metadata.vo.jepfunctions.*;

public class GetCurrentDBTime extends PostfixMathCommand {
    private int li_type = -1;

    private GetCurrentDBTime() { // 这个参数必须要有，，，TNND，搞了半天，真是太不爽了.
        numberOfParameters = 0;
    }

    public GetCurrentDBTime(int _type) { // 这个参数必须要有，，，TNND，搞了半天，真是太不爽了.
        numberOfParameters = 0;
        li_type = _type; //
    }

    public void run(Stack inStack) throws ParseException {
        checkStack(inStack);
        String str_sql = "";
        String[][] str_data = null;
        str_sql = "select to_char(sysdate,'yyyy-mm-dd hh24:mi:ss') from dual";
        if (!str_sql.equals("")) {
            try {
                if (li_type == JepFormulaParse.li_ui) { //如果是客户端调用该公式!!
                    str_data = UIUtil.getStringArrayByDS(null, str_sql);
                } else { //如果是服务器端调用该公式!!
                    str_data = new CommDMO().getStringArrayByDS(null, str_sql);
                }

            } catch (NovaRemoteException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (str_data != null && str_data.length > 0) {
                inStack.push(str_data[0][0]); //
            }
        }
    }
}
/**************************************************************************
 * $RCSfile: GetCurrentDBTime.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:38:25 $
 *
 * $Log: GetCurrentDBTime.java,v $
 * Revision 1.2  2007/05/31 07:38:25  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 06:02:53  qilin
 * no message
 *
 * Revision 1.7  2007/03/30 08:12:15  shxch
 * *** empty log message ***
 *
 * Revision 1.6  2007/03/07 02:47:23  shxch
 * *** empty log message ***
 *
 * Revision 1.1  2007/03/07 02:04:18  shxch
 * *** empty log message ***
 *
 * Revision 1.4  2007/03/07 02:01:56  shxch
 * *** empty log message ***
 *
 * Revision 1.3  2007/03/02 05:02:50  shxch
 * *** empty log message ***
 *
 * Revision 1.2  2007/01/30 04:59:24  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/
