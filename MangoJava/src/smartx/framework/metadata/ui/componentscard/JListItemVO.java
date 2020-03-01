/**************************************************************************
 * $RCSfile: JListItemVO.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:38:17 $
 **************************************************************************/
package smartx.framework.metadata.ui.componentscard;

import java.io.*;

public class JListItemVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private String code;

    private String name;

    public JListItemVO() {
    }

    public JListItemVO(String _id, String _name, String _code) {
        this.id = _id;
        this.code = _name;
        this.name = _name; //
    }

    public String toString() {
        return this.name;
    }

    public boolean equals(Object _obj) {
        if (_obj instanceof JListItemVO) {
            JListItemVO new_name = (JListItemVO) _obj;
            if (new_name.getId().equals(this.id) && new_name.getCode().equals(this.getCode()) &&
                new_name.getName().equals(this.getName())) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
/**************************************************************************
 * $RCSfile: JListItemVO.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:38:17 $
 *
 * $Log: JListItemVO.java,v $
 * Revision 1.2  2007/05/31 07:38:17  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 06:01:07  qilin
 * no message
 *
 * Revision 1.2  2007/01/30 05:14:30  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/