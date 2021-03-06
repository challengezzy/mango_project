/**************************************************************************
 * $RCSfile: RefItemVO.java,v $  $Revision: 1.2.8.1 $  $Date: 2010/01/20 10:12:08 $
 **************************************************************************/
package smartx.framework.metadata.vo;

import java.io.*;

import smartx.framework.common.vo.*;


public class RefItemVO implements Serializable,ItemVO {
    private static final long serialVersionUID = 2978402575358156826L;
    protected String id=null;
    protected String code=null;
    protected String name=null;
    protected HashVO hashVO = null;

    public RefItemVO(HashVO _vo) {
        this.hashVO = _vo;
        this.id = hashVO.getStringValue(0);
        this.code = hashVO.getStringValue(1);
        this.name = hashVO.getStringValue(2);

    }

    public RefItemVO(String _id, String _code, String _name) {
        this.id = _id;
        this.code = _code;
        this.name = _name;

        this.hashVO = new HashVO();
        hashVO.setAttributeValue("ID", _id);
        hashVO.setAttributeValue("CODE", _code);
        hashVO.setAttributeValue("NAME", _name);
    }

    public RefItemVO(String _id, String _code, String _name, HashVO _vo) {
        this.id = _id;
        this.code = _code;
        this.name = _name;
        this.hashVO = _vo; //
    }
    
    public RefItemVO(){
    	
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

    public HashVO getHashVO() {
        return hashVO;
    }

    public void setHashVO(HashVO hashVO) {
        this.hashVO = hashVO;
    }

    /**
     * 得到某一项的值
     * @param _itemkey
     * @return
     */
    public String getItemValue(String _itemkey) {
        if (hashVO != null) {
            return hashVO.getStringValue(_itemkey); //...
        }
        return null;
    }

    public String toString() {
        return this.name;
    }

    public boolean equals(Object _obj) {
        if (_obj instanceof RefItemVO) {
            RefItemVO new_name = (RefItemVO) _obj;
            if (new_name.getId().equals(this.id) && new_name.getCode().equals(this.code) &&
                new_name.getName().equals(this.name)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

}
/**************************************************************************
 * $RCSfile: RefItemVO.java,v $  $Revision: 1.2.8.1 $  $Date: 2010/01/20 10:12:08 $
 *
 * $Log: RefItemVO.java,v $
 * Revision 1.2.8.1  2010/01/20 10:12:08  wangqi
 * *** empty log message ***
 *
 * Revision 1.2  2007/05/31 07:38:20  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 06:03:16  qilin
 * no message
 *
 * Revision 1.3  2007/02/27 06:03:02  shxch
 * *** empty log message ***
 *
 * Revision 1.2  2007/01/30 04:16:43  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/