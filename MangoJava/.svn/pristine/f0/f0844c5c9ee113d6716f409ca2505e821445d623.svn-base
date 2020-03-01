/**************************************************************************
 * $RCSfile: INovaCompent.java,v $  $Revision: 1.3.2.4 $  $Date: 2009/02/13 09:58:46 $
 **************************************************************************/
package smartx.framework.metadata.ui.componentscard;


import java.awt.Color;
import java.io.*;

import javax.swing.*;

import smartx.framework.metadata.vo.Pub_Templet_1_ItemVO;

public interface INovaCompent extends Serializable {
	
	/**
	 * 获得组件的key
	 * @return
	 */
    public String getKey();

    /**
     * 获得组件名称
     * @return
     */
    public String getName();

    /**
     * 获得组件取值（对于复杂组件，取到的是显示值）
     * @return
     */
    public String getValue();

    /**
     * 获得组件真实值
     * @return
     */
    public String getInputValue();
    
    /**
     * 获得组件取值对象
     * @return
     */
    public Object getObject(); //

    /**
     * 设置组件值
     * @param _value
     */
    public void setObject(Object _obj);
    
    /**
     * 重置组件
     */
    public void reset(); //

    /**
     * 设置是否允许编辑
     * @param _bo
     */
    public void setEditable(boolean _bo);

    /**
     * 获得组件标签对象
     * @return
     */
    public JLabel getLabel();

    /**
     * 组件设置焦点
     */
    public void focus();
    
    /**
     * 获得组件模板设置对象
     * @return
     */
    public Pub_Templet_1_ItemVO getTempletItemVO();

    
    
    
}
/**************************************************************************
 * $RCSfile: INovaCompent.java,v $  $Revision: 1.3.2.4 $  $Date: 2009/02/13 09:58:46 $
 *
 * $Log: INovaCompent.java,v $
 * Revision 1.3.2.4  2009/02/13 09:58:46  wangqi
 * *** empty log message ***
 *
 * Revision 1.3.2.3  2008/11/05 05:21:06  wangqi
 * *** empty log message ***
 *
 * Revision 1.2  2008/10/30 08:19:04  wangqi
 * *** empty log message ***
 *
 * Revision 1.1  2008/09/01 07:38:11  wangqi
 * *** empty log message ***
 *
 * Revision 1.4  2008/06/11 16:40:35  wangqi
 * *** empty log message ***
 *
 * Revision 1.3  2007/11/28 05:35:18  wangqi
 * *** empty log message ***
 *
 * Revision 1.2  2007/05/31 07:38:17  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 06:01:07  qilin
 * no message
 *
 * Revision 1.4  2007/03/28 09:51:09  shxch
 * *** empty log message ***
 *
 * Revision 1.3  2007/03/28 09:22:09  shxch
 * *** empty log message ***
 *
 * Revision 1.2  2007/01/30 05:14:29  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/