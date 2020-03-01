package smartx.framework.metadata.vo.jepfunctions;

import java.util.*;

import org.nfunk.jep.*;
import org.nfunk.jep.function.*;

import smartx.framework.common.bs.*;
import smartx.framework.common.ui.*;

public class GetColValueByDS extends PostfixMathCommand {

    int li_type = -1;

    public GetColValueByDS() {
        numberOfParameters = 5;
    }

    public GetColValueByDS(int _type) {
        numberOfParameters = 5;
        li_type = _type;
    }

    public void run(Stack inStack) throws ParseException {
        try {
            //checkStack(inStack);
            Object param_1 = inStack.pop();
            Object param_2 = inStack.pop();
            Object param_3 = inStack.pop();
            Object param_4 = inStack.pop();
            Object param_5 = inStack.pop();

            String str_dsname = (String) param_5;
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
                    str_data = UIUtil.getStringArrayByDS(str_dsname, str_sql);
                } else if (li_type == JepFormulaParse.li_bs) { //如果是Server端调用
                    str_data = new CommDMO().getStringArrayByDS(str_dsname, str_sql);
                }

                if (str_data != null && str_data.length > 0) {
                    inStack.push("" + str_data[0][0]); //
                } else {
                    inStack.push(""); //
                }
            }
        } catch (Throwable ex) {
            ex.printStackTrace();
        }

    }
}
