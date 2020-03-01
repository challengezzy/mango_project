/**************************************************************************
 * $RCSfile: GetColValue.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:38:24 $
 **************************************************************************/

package smartx.framework.metadata.vo.jepfunctions;

import java.util.*;

import org.nfunk.jep.*;
import org.nfunk.jep.function.*;

import smartx.framework.common.bs.*;
import smartx.framework.common.ui.*;

public class GetColValue extends PostfixMathCommand {

    int li_type = -1;

    public GetColValue() {
        numberOfParameters = 4;
    }

    public GetColValue(int _type) {
        numberOfParameters = 4;
        li_type = _type;
    }

    public void run(Stack inStack) throws ParseException {
        try {
            //checkStack(inStack);
            Object param_1 = inStack.pop();
            Object param_2 = inStack.pop();
            Object param_3 = inStack.pop();
            Object param_4 = inStack.pop();

            String str_table_name = (String) param_4;
            String str_returnfieldname = (String) param_3;
            String str_wherefieldname = (String) param_2;
            String str_wherefieldvalue = (String) param_1;

            if (str_wherefieldvalue == null || str_wherefieldvalue.trim().equals("") ||
                str_wherefieldvalue.trim().toLowerCase().equals("null")) {
                inStack.push(null); //如果where值是null,就不取数据库了!!直接塞空值,这样效率会高许多!!
            } else {
                String str_sql = "select " + str_returnfieldname + " from " + str_table_name + " where " +
                    str_wherefieldname + "='" + str_wherefieldvalue + "'"; //
                String[][] str_data = null;
                if (li_type == JepFormulaParse.li_ui) { //如果是客户端调用
                    str_data = UIUtil.getStringArrayByDS(null, str_sql);
                } else if (li_type == JepFormulaParse.li_bs) { //如果是Server端调用
                    str_data = new CommDMO().getStringArrayByDS(null, str_sql);
                }

                if (str_data != null && str_data.length > 0) {
                    inStack.push(str_data[0][0]); //
                } else {
                    inStack.push(null); //
                }
            }
        } catch (Throwable ex) {
            ex.printStackTrace();
        }

    }
}
/**************************************************************************
 * $RCSfile: GetColValue.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:38:24 $
 *
 * $Log: GetColValue.java,v $
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
 * Revision 1.3  2007/03/02 05:02:50  shxch
 * *** empty log message ***
 *
 * Revision 1.2  2007/01/30 04:59:24  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/
