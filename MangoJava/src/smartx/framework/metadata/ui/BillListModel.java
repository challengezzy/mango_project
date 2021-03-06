/**************************************************************************
 * $RCSfile: BillListModel.java,v $  $Revision: 1.3.8.1 $  $Date: 2009/02/02 16:12:54 $
 **************************************************************************/
package smartx.framework.metadata.ui;

import java.util.*;

import javax.swing.table.*;

import smartx.framework.common.ui.*;
import smartx.framework.common.utils.StringUtil;
import smartx.framework.common.vo.*;
import smartx.framework.metadata.vo.*;


public class BillListModel extends DefaultTableModel {

    private static final long serialVersionUID = 1L;

    private Pub_Templet_1VO templetVO = null;

    private Pub_Templet_1_ItemVO[] templetItemVOs = null;

    public static String str_rownumberMark = "_RECORD_ROW_NUMBER";

    public BillListModel(Object[][] data, Object[] columnNames, Pub_Templet_1VO _templetVO) {
        super(data, columnNames);
        this.templetVO = _templetVO;
        this.templetItemVOs = _templetVO.getItemVos();
    }

    public Object getValueAt(int _row, String _key) {
        int li_index = findModelIndex(_key);
        if (li_index >= 0) {
            return getValueAt(_row, li_index);
        }
        return null;
    }

    public HashMap getValueAtRowWithHashMap(int _row) {
        HashMap map = new HashMap(); //
        map.put(str_rownumberMark, getValueAt(_row, 0)); //
        for (int i = 0; i < this.templetItemVOs.length; i++) {
            String str_key = templetItemVOs[i].getItemkey();
            Object obj = getValueAt(_row, str_key);
            map.put(str_key, obj);
        }
        return map;
    }

    public VectorMap getValueAtRowWithVectorMap(int _row) {
        VectorMap map = new VectorMap(); //
        map.put(str_rownumberMark, getValueAt(_row, 0)); //
        for (int i = 0; i < this.templetItemVOs.length; i++) {
            String str_key = templetItemVOs[i].getItemkey();
            Object obj = getValueAt(_row, str_key);
            map.put(str_key, obj);
        }
        return map;
    }

    public VectorMap getValueAtModelWithVectorMap(int _row) {
        VectorMap map = new VectorMap(); //
        for (int i = 0; i < templetVO.getRealViewItemVOs().length; i++) {
            String str_key = templetVO.getRealViewItemVOs()[i].getItemkey().toLowerCase();
            Object obj = getValueAt(_row, str_key);
            if (obj == null) {
                obj = "";
            }
            map.put(str_key, obj);
        }
        return map;
    }

    public VectorMap getSavedValueAtModelWithVectorMap(int _row) {
        VectorMap map = new VectorMap(); //
        for (int i = 0; i < templetVO.getRealViewItemVOs().length; i++) {
            if (!templetVO.getRealViewItemVOs()[i].getIssave().booleanValue()) {
                continue;
            }
            String str_key = templetVO.getRealViewItemVOs()[i].getItemkey().toLowerCase();
            Object obj = getValueAt(_row, str_key);
            if (obj == null) {
                obj = "";
            }
            map.put(str_key, obj);
        }
        return map;
    }

    public Object[][] getValueAtAll() {
        int li_rowcount = getRowCount();
        int li_colcount = getColumnCount();
        Object[][] objs = new Object[li_rowcount][li_colcount];
        for (int i = 0; i < li_rowcount; i++) {
            for (int j = 0; j < objs[i].length; j++) {
                objs[i][j] = getValueAt(i, j);
            }
        }
        return objs;
    }

    public Object[] getValueAtRow(int _row) {

        Object[] objs = new Object[templetItemVOs.length + 1];
        for ( int i = 0; i < objs.length; i++ )
        {
            objs[i] = getValueAt( _row, i );
        }
        return objs;

//        int li_modelColCount = getColumnCount();
//        Object[] objs = new Object[li_modelColCount];
//        for (int j = 0; j < objs.length; j++) {
//            objs[j] = getValueAt(_row, j);
//        }
//        return objs;
    }

    public String getRealValueAtModel(int _row, int _col) {
        Object obj = getValueAt(_row, _col);
        return getObjectRealValue(obj);
    }

    public String getRealValueAtModel(int _row, String _key) {
        Object obj = getValueAt(_row, _key);
        return getObjectRealValue(obj);
    }

    public String[][] getRealValueAtModel() {
        Object[][] allObjects = getValueAtAll();
        if (allObjects.length <= 0) {
            return null;
        }

        String[][] str_data = new String[allObjects.length][allObjects[0].length];
        for (int i = 0; i < str_data.length; i++) {
            for (int j = 0; j < str_data[0].length; j++) {
                str_data[i][j] = getObjectRealValue(allObjects[i][j]);
            }
        }
        return str_data;
    }

    private String getObjectRealValue(Object _obj) {
        if (_obj == null) {
            return null;
        }

        if (_obj instanceof String) {
            return (String) _obj;
        } else if (_obj instanceof ComBoxItemVO) {
            ComBoxItemVO vo = (ComBoxItemVO) _obj;
            return vo.getId();
        } else if (_obj instanceof RefItemVO) {
            RefItemVO vo = (RefItemVO) _obj;
            return vo.getId();
        } else {
            return _obj.toString();
        }
    }

    public void setValueAt(Object _obj, int _row, String _key) {
        int li_index = findModelIndex(_key);
        if (li_index >= 0) {
            this.setValueAt(_obj, _row, li_index);
        }
    }

    //要补充一下
    public void setRealValueAt(String _value, int _row, String _key) {
        int li_index = findModelIndex(_key); //取得第几列!!
        if (li_index >= 0) {
            String str_type = getItemType(_key);
            if (str_type.equals("文本框")) {
                this.setValueAt(_value, _row, li_index);
            } else if (str_type.equals("数字框")) {
                this.setValueAt(_value, _row, li_index); //
            } else if (str_type.equals("下拉框")) { //如果是下拉框!!
                ComBoxItemVO[] comItemVOs = getTempletItemVO(_key).getComBoxItemVos();
                for (int i = 0; i < comItemVOs.length; i++) {
                    if (comItemVOs[i].getId().equals(_value)) {
                        this.setValueAt(comItemVOs[i], _row, li_index); //
                        return;
                    }
                }
            } else if (str_type.equals("参照")) { // 应该去找出其SQL定义然后检索后创建RefItemVO!!
                String str_reftype = getTempletItemVO(_key).getRefdesc_type();
                if (str_reftype.equals("TABLE") || str_reftype.equals("TREE")) { // 如果是表型或树型参照
                    Pub_Templet_1_ItemVO itemVO = getTempletItemVO(_key);
                    String str_refdescsql = getTempletItemVO(_key).getRefdesc_realsql();
                    FrameWorkTBUtil tbUtil = new FrameWorkTBUtil(); //
                    String str_newsql = null;
                    try {
                        str_newsql = tbUtil.convertFormulaMacPars(str_refdescsql, NovaClientEnvironment.getInstance(),
                            getValueAtRowWithHashMap(_row));
                    } catch (Exception e) {
                        System.out.println("设置默认值公式时,参照[" + itemVO.getItemkey() + "][" + itemVO.getItemname() +
                                           "]的SQL[" + str_refdescsql + "]转换转换失败!!"); //
                        e.printStackTrace();
                    }

                    if (str_newsql != null) {
                        String str_refidFieldName = itemVO.getRefdesc_firstColName();
                        String str_newsql2 = StringUtil.replaceAll(str_newsql, "1=1",
                            str_refidFieldName + "='" + _value + "'"); //
                        System.out.println("真正执行参照定义的SQL:" + str_newsql2); //
                        HashVO[] vos = null;
                        try {
                            vos = UIUtil.getHashVoArrayByDS(getDataSourceName(), str_newsql2); //
                        } catch (NovaRemoteException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        } //

                        if (vos != null && vos.length > 0) {
                            RefItemVO refVO = new RefItemVO(vos[0]); //
                            this.setValueAt(refVO, _row, li_index);
                            return;
                        }
                    }
                } else {
                    this.setValueAt(new RefItemVO(_value, _value, _value), _row, li_index);
                }
                this.setValueAt(_value, _row, li_index);
            } else { //如果是其他控件!!
                this.setValueAt(_value, _row, li_index);
            }
        }
    }

    public void setValueAtAll(Object[][] _data) {
        for (int i = 0; i < _data.length; i++) {
            int row = addEmptyRow();

            for (int j = 0; j < this.getColumnCount(); j++) {
                setValueAt(_data[i][j], row, j);
            }
        }
    }

    public int findModelIndex(String _key) {
        if (_key.equalsIgnoreCase(str_rownumberMark)) { // 如果是行号则直接返回
            return 0;
        }

        for (int i = 0; i < templetItemVOs.length; i++) {
            if (templetItemVOs[i].getItemkey().equalsIgnoreCase(_key)) {
                return i + 1;
            }
        }
        return -1;
    }

    public int addEmptyRow() {
        int li_colcount = getColumnCount();
        Object[] allobjs = new Object[li_colcount];
        for (int i = 0; i < allobjs.length; i++) {
            allobjs[i] = null;
        }
        addRow(allobjs);
        return getRowCount() - 1;

    }

    public int insertEmptyRow(int _row) {
        int li_colcount = getColumnCount();
        Object[] allobjs = new Object[li_colcount];
        for (int i = 0; i < allobjs.length; i++) {
            allobjs[i] = null;
        }
        insertRow(_row, allobjs);
        return _row; //

    }

    public void addRow(HashMap _map) {
        String[] str_keys = (String[]) _map.keySet().toArray(new String[0]);
        int li_newRow = addEmptyRow();
        setValueAt(new RowNumberItemVO("INSERT", li_newRow), li_newRow, this.str_rownumberMark);
        for (int i = 0; i < str_keys.length; i++) {
            setValueAt(_map.get(str_keys[i]), li_newRow, str_keys[i]);
        }
    }

    public void addRow(VectorMap _map) {
        String[] str_keys = _map.getKeysAsString();
        int li_newRow = addEmptyRow();
        setValueAt(new RowNumberItemVO("INSERT", li_newRow), li_newRow, this.str_rownumberMark);
        for (int i = 0; i < str_keys.length; i++) {
            setValueAt(_map.get(str_keys[i]), li_newRow, str_keys[i]);
        }
    }

    public Pub_Templet_1_ItemVO[] getTempletItemVOs() {
        return templetItemVOs;
    }

    private Pub_Templet_1_ItemVO getTempletItemVO(String _itemKey) {
        Pub_Templet_1_ItemVO[] vos = getTempletItemVOs();
        for (int i = 0; i < vos.length; i++) {
            if (vos[i].getItemkey().equalsIgnoreCase(_itemKey)) {
                return vos[i];
            }
        }
        return null;
    }

    public Pub_Templet_1VO getTempletVO() {
        return templetVO;
    }

    public boolean containsItemKey(String _itemKey) {
        return this.getTempletVO().containsItemKey(_itemKey);
    }

    private String getItemType(String _itemKey) {
        String[] str_keys = templetVO.getItemKeys(); //
        String[] str_itemTypes = templetVO.getItemTypes(); //
        for (int i = 0; i < str_keys.length; i++) {
            if (str_keys[i].equals(_itemKey)) {
                return str_itemTypes[i]; //
            }
        }
        return null;
    }

    /**
     * 得到数据源名称
     * @return
     */
    public String getDataSourceName() {
        if (templetVO.getDatasourcename() == null || templetVO.getDatasourcename().trim().equals("null") ||
            templetVO.getDatasourcename().trim().equals("")) {
            return NovaClientEnvironment.getInstance().getDefaultDatasourceName(); // 默认数据源
        } else {
            return new FrameWorkTBUtil().convertExpression(NovaClientEnvironment.getInstance(),templetVO.getDatasourcename()); // 算出数据源!!
        }
    }

}
/**************************************************************************
 * $RCSfile: BillListModel.java,v $  $Revision: 1.3.8.1 $  $Date: 2009/02/02 16:12:54 $
 *
 * $Log: BillListModel.java,v $
 * Revision 1.3.8.1  2009/02/02 16:12:54  wangqi
 * *** empty log message ***
 *
 * Revision 1.3  2007/06/22 03:12:46  lst
 * no message
 *
 * Revision 1.2  2007/05/31 07:38:14  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 06:01:37  qilin
 * no message
 *
 * Revision 1.8  2007/03/30 10:13:29  shxch
 * *** empty log message ***
 *
 * Revision 1.7  2007/03/30 10:00:08  shxch
 * *** empty log message ***
 *
 * Revision 1.6  2007/03/28 05:48:07  shxch
 * *** empty log message ***
 *
 * Revision 1.5  2007/03/23 05:05:19  shxch
 * *** empty log message ***
 *
 * Revision 1.4  2007/02/27 06:57:18  shxch
 * *** empty log message ***
 *
 * Revision 1.3  2007/02/07 03:44:45  lujian
 * 修改setValueAt()方法，开放findModelIndex()方法
 *
 * Revision 1.2  2007/01/30 04:56:14  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/
