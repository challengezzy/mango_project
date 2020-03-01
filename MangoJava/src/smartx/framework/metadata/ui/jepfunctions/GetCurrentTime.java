/**************************************************************************
 * $RCSfile: GetCurrentTime.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:38:25 $
 **************************************************************************/

package smartx.framework.metadata.ui.jepfunctions;

import java.text.*;
import java.util.*;

import org.nfunk.jep.ParseException;
import org.nfunk.jep.function.*;

public class GetCurrentTime extends PostfixMathCommand {

    private Date d_curr = null;
    public GetCurrentTime() { // 这个参数必须要有，，，TNND，搞了半天，真是太不爽了.
        numberOfParameters = 0;
    }

    public void run(Stack inStack) throws ParseException {
        checkStack(inStack);
        d_curr = new Date();
        SimpleDateFormat sdf_curr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.SIMPLIFIED_CHINESE);
        String str_date = sdf_curr.format(d_curr);
        inStack.push(str_date);
    }
}
/**************************************************************************
 * $RCSfile: GetCurrentTime.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:38:25 $
 *
 * $Log: GetCurrentTime.java,v $
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
 * Revision 1.2  2007/01/30 04:59:23  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/