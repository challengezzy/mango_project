/**************************************************************************
 * $RCSfile: CommTBUtil.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:38:22 $
 **************************************************************************/
package smartx.framework.common.vo;

import java.io.*;

/**
 * tb包是与ui,vo,bs平级的东东,tb是ToolBox的简写,是工具包的意思,因为总有一些工具,既在Client端用到,又在Server端用到,
 * 如果把他们放在vo包中,又因为vo是ValueObject的意思,好象又觉得不妥,而它们是Tool一类的东东,所以建了一个tb包!!
 * 通用工具包!!
 * @author user
 *
 */
public class CommTBUtil implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 2636403747594023320L;

    /**
     * 替换字符串
     * @param str_par
     * @param old_item
     * @param new_item
     * @return
     */
    public String replaceAll(String str_par, String old_item, String new_item) {
        String str_return = "";
        String str_remain = str_par;
        boolean bo_1 = true;
        while (bo_1) {
            int li_pos = str_remain.indexOf(old_item);
            if (li_pos < 0) {
                break;
            } // 如果找不到,则返回
            String str_prefix = str_remain.substring(0, li_pos);
            str_return = str_return + str_prefix + new_item; // 将结果字符串加上原来前辍
            str_remain = str_remain.substring(li_pos + old_item.length(), str_remain.length());
        }
        str_return = str_return + str_remain; // 将剩余的加上
        return str_return;
    }

    public Object deepClone(Object obj) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(buf);
        out.writeObject(obj);
        ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(buf.toByteArray()));
        return in.readObject();
    }

}

/**************************************************************************
 * $RCSfile: CommTBUtil.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:38:22 $
 *
 * $Log: CommTBUtil.java,v $
 * Revision 1.2  2007/05/31 07:38:22  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 05:56:47  qilin
 * no message
 *
 * Revision 1.2  2007/03/16 02:51:05  shxch
 * *** empty log message ***
 *
 * Revision 1.1  2007/03/07 02:01:56  shxch
 * *** empty log message ***
 *
 * Revision 1.1  2007/02/27 05:15:54  shxch
 * *** empty log message ***
 *
 * Revision 1.2  2007/01/30 03:47:51  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/
