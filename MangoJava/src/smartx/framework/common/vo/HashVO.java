/**************************************************************************
 * $RCSfile: HashVO.java,v $  $Revision: 1.5.2.4 $  $Date: 2009/12/02 05:47:39 $
 **************************************************************************/

package smartx.framework.common.vo;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;

import smartx.framework.common.ui.NovaClientEnvironment;
import smartx.framework.common.ui.NovaRemoteServiceFactory;
import smartx.framework.metadata.bs.MetaDataDMO;
import smartx.framework.metadata.ui.FrameWorkMetaDataService;
import smartx.framework.metadata.vo.BillVO;
import smartx.framework.metadata.vo.ComBoxItemVO;
import smartx.framework.metadata.vo.Pub_Templet_1VO;
import smartx.framework.metadata.vo.RefItemVO;

/**
 * HashVO 是一个VectorMap用来存储的数据结构
 */
public class HashVO implements Serializable, Cloneable {

    private static final long serialVersionUID = -8645721192600426005L;

    private VectorMap m_hData = new VectorMap(); // VectorMap,按输入顺序排序
    
    private VectorMap m_hData_type = new VectorMap(); // VectorMap,字段类型

    private VectorMap m_hData_tablename = new VectorMap(); // VectorMap,按输入顺序排序,存储表名

    public HashVO() {
        super();
    }
    
    public int getColumnType(String attributeName){
    	return Integer.parseInt(m_hData_type.get(attributeName.toLowerCase()).toString().trim());
    }
    
    public void setColumnType(String attributeName,int _value){
    	m_hData_type.put(attributeName.toLowerCase(), _value);
    }

    public Object getAttributeValue(String attributeName) {
        return m_hData.get(attributeName.toLowerCase());
    }

    public void setAttributeValue(String key, Object value) {
        m_hData.put(key.toLowerCase(), value);
    }

    public int length() {
        return getKeys().length;
    }

    public java.lang.String[] getKeys() {
        return m_hData.getKeysAsString();
    }
    public Object[] getValues() {
        return m_hData.getValues();
    }

    public java.lang.String[] getValuesAsString() {
        int li_length = length();
        String[] str_return = new String[li_length];
        for (int i = 0; i < li_length; i++) {
            str_return[i] = getStringValue(i);
        }
        return str_return;
    }

    /**
     * 设置表名
     * @param _key
     * @param _tableName
     */
    public void setTableName(String _key, String _tableName) {
        m_hData_tablename.put(_key.toLowerCase(), _tableName);
    }

    /**
     * 取得表名
     * @param _key
     * @return
     */
    public String getTableName(String _key) {
        return "" + m_hData_tablename.get(_key);
    }

    /**
     * 取得某一列对应的表名
     * @param _pos
     * @return
     */
    public String getTableName(int _pos) {
        return getTableName(getKeys()[_pos]);
    }

    public Object getObjectValue(String attrname) {
        return getAttributeValue(attrname);
    }

    public Object getObjectValue(int _pos) {
        return getObjectValue(getKeys()[_pos]);
    }

    /**
     * 取得字符值
     *
     * @param attrname
     * @return
     */
    public String getStringValue(String attrname) {
        Object o = getAttributeValue(attrname);
        if (o == null) {
            return null;
        }

        if (o instanceof String) {
            return o.toString();
        } else if (o instanceof BigDecimal) {
            return o.toString();
        } else if (o instanceof java.sql.Timestamp) {
            java.sql.Timestamp ts = (java.sql.Timestamp) o;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String str_date = sdf.format(new java.util.Date(ts.getTime())); //
            return str_date;
        } else if (o instanceof Integer) {
            return o.toString();
        } else if (o instanceof Long) {
            return o.toString();
        } else if (o instanceof Double) {
            return o.toString();
        } else if (o instanceof java.util.Date) {
            java.util.Date date = (java.util.Date) o;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String str_date = sdf.format(date); //
            return str_date;
        } else {
            return o.toString();
        }
    }
    public String getStringValue(int _pos) {
        return getStringValue(getKeys()[_pos]);
    }
    
    /**
     * 取指定列的值，如果是null, 转换成空字符串
     * @param _pos
     * @return
     */
    public String getStringValueNotNull(int _pos){
    	String value = getStringValue(_pos);
    	if( value == null )
    		value = "";
    	
    	return value;
    }
    
    
    /**
     * 取指定列的值，如果是null, 转换成空字符串
     * @param attrname
     * @return
     */
    public String getStringValueNotNull(String attrname){
    	String value = getStringValue(attrname);
    	if( value == null )
    		value = "";
    	
    	return value;
    }
    
    /**
     * 强行将对象转换成2006-12-25的样子的字符串
     * @param attrname
     * @return
     */
    public String getStringValueForDay(String attrname) {
        Object o = getAttributeValue(attrname);
        if (o == null) {
            return null;
        }

        if (o instanceof java.sql.Timestamp) {
            java.sql.Timestamp ts = (java.sql.Timestamp) o;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String str_date = sdf.format(new java.util.Date(ts.getTime())); //
            return str_date;
        } else if (o instanceof java.util.Date) {
            java.util.Date date = (java.util.Date) o;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String str_date = sdf.format(date); //
            return str_date;
        } else {
            return o.toString();
        }
    }

    /**
     * 强行将对象转换成2006-12-25 12:25:32的样子的字符串
     * @param attrname
     * @return
     */
    public String getStringValueForSecond(String attrname) {
        Object o = getAttributeValue(attrname);
        if (o == null) {
            return null;
        }

        if (o instanceof java.sql.Timestamp) {
            java.sql.Timestamp ts = (java.sql.Timestamp) o;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String str_date = sdf.format(new java.util.Date(ts.getTime())); //
            return str_date;
        } else if (o instanceof java.util.Date) {
            java.util.Date date = (java.util.Date) o;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String str_date = sdf.format(date); //
            return str_date;
        } else {
            return o.toString();
        }
    }

    

    /**
     * 取得Integer值
     *
     * @param attrname
     * @return
     */
    public Integer getIntegerValue(String attrname) {
        Object o = getAttributeValue(attrname);
        if (o == null) {
            return null;
        }

        if (o instanceof Integer) {
            return (Integer) o;
        }else if(o instanceof Boolean){
        	if( ((Boolean) o).booleanValue() == true ){
        		return new Integer(1);
        	}else{
        		return new Integer(0);
        	}
        	
        }else if (o instanceof BigDecimal) { //如果是BigDecimal
            BigDecimal bd = (BigDecimal) o;
            return new Integer(bd.intValue());
        } else if (o instanceof RefItemVO) { //如果是下拉框
            RefItemVO bd = (RefItemVO) o;
            return new Integer(bd.getId());
        } else if (o instanceof ComBoxItemVO) { //如果是下拉框
            ComBoxItemVO bd = (ComBoxItemVO) o;
            return new Integer(bd.getId());
        } else {
            String str_value = o.toString();
            int li_pos = str_value.indexOf(".");
            if (li_pos >= 0) {
                str_value = str_value.substring(0, li_pos);
            }
            return new Integer(str_value);
        }
    }
    public Integer getIntegerValue(int _pos) {
        return getIntegerValue(getKeys()[_pos]);
    }

    /**
     * 取得Long值
     *
     * @param attrname
     * @return
     */
    public Long getLongValue(String attrname) {
        Object o = getAttributeValue(attrname);
        if (o == null) {
            return null;
        }
        if (o instanceof Long) {
            return (Long) o;
        } else if (o instanceof BigDecimal) { //如果是BigDecimal
            BigDecimal bd = (BigDecimal) o;
            return new Long(bd.longValue());
        } else {
            String str_value = o.toString(); //
            return new Long(str_value); //
        }
    }
    public Long getLongValue(int _pos) {
        return getLongValue(getKeys()[_pos]);
    }
    public Long getLognValue(String attrname) {
    	return this.getLongValue(attrname);
    }
    public Long getLognValue(int _pos) {
    	return this.getLongValue(_pos);
    }

    /**
     * 取得Double值
     *
     * @param attrname
     * @return
     */
    public Double getDoubleValue(String attrname) {
        Object o = getAttributeValue(attrname);
        if (o == null) {
            return null;
        } else if (o instanceof Double) {
            return (Double) o;
        }else if(o instanceof Boolean){
        	if( ((Boolean) o).booleanValue() == true ){
        		return new Double(1);
        	}else{
        		return new Double(0);
        	}
        	
        } else if (o instanceof BigDecimal) { //如果是BigDecimal
            BigDecimal bd = (BigDecimal) o;
            return new Double(bd.doubleValue());
        } else {
            return new Double(o.toString());
        }
    }
    public Double getDoubleValue(int _pos) {
        return getDoubleValue(getKeys()[_pos]);
    }
    /**
     * 取得Byte值
     *
     * @param attrname
     * @return
     */
    public Byte getByteValue(String attrname) {
        Object o = getAttributeValue(attrname);
        if (o == null) {
            return null;
        }else if (o instanceof Byte) {
            return (Byte)o;
        }else{
        	return Byte.valueOf(String.valueOf(o));
        }
    }
    public Byte getByteValue(int _pos) {
        return getByteValue(this.getKeys()[_pos]);
    }
    
    public byte[] getBytesValue(String attrname) {
    	Object o = getAttributeValue(attrname);
        if (o instanceof byte[]) {
            return (byte[])o;
        }
        return null;
    }
    
    public byte[] getBytesValue(int _pos) {
        return getBytesValue(this.getKeys()[_pos]);
    }
    /**
     * 取得Double值
     *
     * @param attrname
     * @return
     */
    public BigDecimal getBigDecimalValue(String attrname) {
        Object o = getAttributeValue(attrname);
        if (o == null) {
            return null;
        } else if (o instanceof BigDecimal) { //如果是BigDecimal
            BigDecimal bd = (BigDecimal) o;
            return bd;
        } else {
            return new BigDecimal(o.toString());
        }
    }
    public BigDecimal getBigDecimalValue(int _pos) {
        return getBigDecimalValue(getKeys()[_pos]);
    }

    /**
     * 取得Boolean值
     *
     * @param attrname
     * @return
     */
    public Boolean getBooleanValue(String attrname) {
        Object o = getAttributeValue(attrname);
        if (o == null) {
        	//return Boolean.TRUE;//为什么此处默认返回TRUE，一般情况下应该返回FALSE
        	return Boolean.FALSE;  
        }

        if (o instanceof Boolean) {
            return (Boolean) o;
        }

        if (o.toString().trim().equalsIgnoreCase("Y") || o.toString().trim().equalsIgnoreCase("yes") ||
            o.toString().trim().equalsIgnoreCase("是") || o.toString().trim().equalsIgnoreCase("1")) {
            return Boolean.TRUE;
        } else if (o.toString().trim().equalsIgnoreCase("N") || o.toString().trim().equalsIgnoreCase("no") ||
                   o.toString().trim().equalsIgnoreCase("否") || o.toString().trim().equalsIgnoreCase("0")) {
            return Boolean.FALSE;
        }

        return new Boolean(o.toString()); // 先直接试下
    }
    public Boolean getBooleanValue(int _pos) {
        return getBooleanValue(getKeys()[_pos]);
    }

    /**
     * 强行将对象转换成Date,精确到天!!
     *
     * @param attrname
     * @return
     */
    public java.sql.Date getDateValue(String attrname) {
        Object o = getAttributeValue(attrname);
        if (o == null) {
            return null;
        }

        if (o instanceof java.sql.Timestamp) {
            java.sql.Timestamp ts = (java.sql.Timestamp) o;
            return new java.sql.Date(ts.getTime());
        } else if (o instanceof java.sql.Date) {
            return (java.sql.Date) o;
        } else {
            String str_value = o.toString().trim();
            try {
                if (str_value.length() == 10) { //如果是10位
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    java.util.Date date = sdf.parse(str_value);
                    return new java.sql.Date(date.getTime());
                } else if (str_value.length() == 19) { //如果是19位
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    java.util.Date date = sdf.parse(str_value);
                    return new java.sql.Date(date.getTime());
                } else {
                    return null;
                }
            } catch (Exception ex) {
                System.out.println("将字符串[" + str_value + "]转换日期失败!!!");
                ex.printStackTrace();
                return null;
            }
        }
    }
    public java.sql.Date getDateValue(int _pos) {
        return getDateValue(getKeys()[_pos]);
    }

    /**
     * 取得TimeStamp值
     *
     * @param attrname
     * @return
     */
    public java.sql.Timestamp getTimeStampValue(String attrname) {
        Object o = getAttributeValue(attrname);
        if (o == null) {
            return null;
        }

        if (o instanceof java.sql.Timestamp) {
            java.sql.Timestamp ts = (java.sql.Timestamp) o;
            return ts;
        } else if (o instanceof java.sql.Date) {
            java.sql.Date date = (java.sql.Date) o;
            return new java.sql.Timestamp(date.getTime());
        } else {
            String str_value = o.toString().trim();
            try {
                if (str_value.length() == 10) { //如果是10位
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    java.util.Date date = sdf.parse(str_value);
                    return new java.sql.Timestamp(date.getTime());
                } else if (str_value.length() == 19) { //如果是19位
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    java.util.Date date = sdf.parse(str_value);
                    return new java.sql.Timestamp(date.getTime());
                } else {
                    return null;
                }
            } catch (Exception ex) {
                System.out.println("将字符串[" + str_value + "]转换Timestamp失败!!!");
                ex.printStackTrace();
                return null;
            }
        }
    }
    public java.sql.Timestamp getTimeStampValue(int _pos) {
        return getTimeStampValue(getKeys()[_pos]);
    }

    /**
     * 重构ToString方法
     */
    //    modified by john_liu, 2007.10.23    for MR#: 本处方法不好，另，解决HashVO对象在树结点的显示功能, MR#:BIZM10-218     begin
//    public String toString() {
//        try {
//            //此处需要重新设计.从数据库取出的值不一定有name,更不一定第二个位置.
//            //不从数据库得到的VO里name不一定在第二个位置(BillCard的getAllObjectValuesWithHashMap()会打乱原先顺序).
//            String name = getStringValue(2);
//            if (name == null) {
//                return "";
//            } else {
//                return name;
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            return null;
//        }
//    }

    private String toStringValue = "";
    public void setToStringValue(String _stringValue)
    {
        toStringValue = _stringValue;
    }

    public String toString()
    {
        if ( toStringValue != null && !( toStringValue.equals( "" ))){
            return toStringValue;
        }
        
        
        String name = getStringValue(2);
        if(name==null){
        	name = getStringValue("name");
        }
        return (name == null)?"":name;        
    }
    //    modified by john_liu, 2007.10.23    for MR#: 本处方法不好，另，解决HashVO对象在树结点的显示功能, MR#:BIZM10-218     end

    public Object clone() throws CloneNotSupportedException {
        HashVO vo = new HashVO();
        String[][] temp = m_hData.getAllDataAsString();
        for (int i = 0; i < temp.length; i++) {
            vo.setAttributeValue(new String(temp[i][0]), new String(temp[i][1]));
        }
        return vo;
    }

    public void removeItem(String _key) {
        m_hData.remove(_key);
    }

    public VectorMap getM_hData() {
        return m_hData;
    }

    public void setM_hData(VectorMap data) {
        m_hData = data;
    }

    public VectorMap getM_hData_type() {
		return m_hData_type;
	}

	public void setM_hData_type(VectorMap data_type) {
		m_hData_type = data_type;
	}

	/**
     * 深度克隆,即复制一个对象!!不影响原来的对象!!
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public HashVO deepClone() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(buf);
        out.writeObject(this);
        ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(buf.toByteArray()));
        return (HashVO) in.readObject();
    }
    /**
     * 转换为billvo
     * templetcode 为hashvo数据来源的元元模板编码
     */
    public BillVO convertToBillVO(String templetcode)
    {
    	if(templetcode==null||templetcode.equals(""))
    		return null;
    	BillVO[] billvo = null;
    	boolean is_server = false;
    	String jvmmode = System.getProperty("JVMSITE");
    	if(jvmmode!=null&&jvmmode.equalsIgnoreCase("SERVER"))
    	{
    		is_server = true;
    	}
    	if(is_server)
    	{	
    		MetaDataDMO metadmo = new MetaDataDMO();
			try {
				billvo = metadmo.getBillVOs(templetcode, this,NovaClientEnvironment.getInstance());
				return billvo[0];
			} catch (Exception e) {
				System.out.println("获取模板编码为[ "+templetcode+" ]的BillVO失败.");
				e.printStackTrace();
			}
    	}
    	else
    	{
    		try {
    			FrameWorkMetaDataService service = getMetaService();
				billvo = service.getBillVOs(templetcode, this, NovaClientEnvironment.getInstance());
				return billvo[0];
			} catch (Exception e) {
				System.out.println("获取模板编码为[ "+templetcode+" ]的BillVO失败.");
				e.printStackTrace();
			}
    	}
    	return null;
    }
    /**
     * 转换为billvo
     */
    public BillVO convertToBillVO(Pub_Templet_1VO _templetVO)
    {
    	if(_templetVO==null)
    		return null;
    	BillVO[] billvo = null;
    	boolean is_server = false;
    	String jvmmode = System.getProperty("JVMSITE");
    	//判断在服务顺端转换还是在客户端转换
    	if(jvmmode!=null&&jvmmode.equalsIgnoreCase("SERVER"))
    	{
    		is_server = true;
    	}
    	if(is_server)
    	{	
    		MetaDataDMO metadmo = new MetaDataDMO();
			try {
				billvo = metadmo.getBillVOs(_templetVO, this);
				return billvo[0];
			} catch (Exception e) {
				System.out.println("获取模板编码为[ "+_templetVO.getTempletname()+" ]的BillVO失败.");
				e.printStackTrace();
			}
    	}
    	else
    	{
    		try {
    			FrameWorkMetaDataService service = getMetaService();
				billvo = service.getBillVOs(_templetVO, this);
				return billvo[0];
			} catch (Exception e) {
				System.out.println("获取模板编码为[ "+_templetVO.getTempletname()+" ]的BillVO失败.");
				e.printStackTrace();
			}
    	}
    	return null;
    }
    private FrameWorkMetaDataService getMetaService() throws Exception
    {
    	return (FrameWorkMetaDataService)NovaRemoteServiceFactory.getInstance().lookUpService(FrameWorkMetaDataService.class);
    }
}

/**************************************************************************
 * $RCSfile: HashVO.java,v $  $Revision: 1.5.2.4 $  $Date: 2009/12/02 05:47:39 $
 *
 * $Log: HashVO.java,v $
 * Revision 1.5.2.4  2009/12/02 05:47:39  wangqi
 * *** empty log message ***
 *
 * Revision 1.5.2.3  2009/04/21 07:55:34  wangqi
 * *** empty log message ***
 *
 * Revision 1.5.2.2  2008/09/17 07:36:31  wangqi
 * patch   : 20080917
 * file    : nova_20080128_20080917.jar
 * content : 处理 MR nova20-83
 *
 * Revision 1.5.2.1  2008/04/23 05:20:28  wangqi
 * *** empty log message ***
 *
 * Revision 1.5  2008/01/21 06:44:10  wangqi
 * *** empty log message ***
 *
 * Revision 1.4  2007/10/23 07:51:23  john_liu
 * 2007.10.23 by john_liu
 * MR#: BIZM10-218
 *
 * Revision 1.3  2007/07/05 07:10:04  sunxf
 * HASHVO 转换为 billvo
 *
 * Revision 1.2  2007/05/31 07:38:22  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 05:56:47  qilin
 * no message
 *
 * Revision 1.4  2007/03/29 10:29:53  sunxf
 * *** empty log message ***
 *
 * Revision 1.3  2007/03/16 05:20:07  shxch
 * *** empty log message ***
 *
 * Revision 1.2  2007/02/27 06:57:19  shxch
 * *** empty log message ***
 *
 * Revision 1.1  2007/02/27 06:03:02  shxch
 * *** empty log message ***
 *
 * Revision 1.2  2007/01/30 04:16:43  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/