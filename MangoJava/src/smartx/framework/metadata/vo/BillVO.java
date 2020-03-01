/**************************************************************************
 * $RCSfile: BillVO.java,v $  $Revision: 1.8.2.5 $  $Date: 2010/01/12 02:07:40 $
 **************************************************************************/
package smartx.framework.metadata.vo;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Vector;

import smartx.framework.common.utils.StringUtil;
import smartx.framework.common.vo.HashVO;

public class BillVO implements Serializable {

	private static final long serialVersionUID = 153235551278950298L;

	private String templetCode = null; // 模板编码,可设可不设!!

	// 四大关键参数!!
	private String queryTableName = null; // 查询表名
	private String saveTableName = null; // 保存表名!!
	private String pkName = null; // 主键字段名
	private String sequenceName = null; // 序列名

	// 五大数组!!!
	private String[] keys = null; // 列名,对应于SQL中的itemkey!!
	private String[] names = null; // 列名,对应于SQL中的itemname!!
	private String[] itemType = null; // 控件类型 
	private String[] columnType = null; // 数据列的类型	
	private boolean[] needSave = null; // 是否需要保存

	private String toStringKeyName; // toString显示的Key的名称!!

	// 真正的数据对象
	private Object[] datas = null; // 真正的对象

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/*******以下都是属性操作****James.W************Begin***********/
	
	//数据数组
	public Object[] getDatas() {
		return datas;
	}
	public void setDatas(Object[] datas) {
		this.datas = datas;
	}
    //字段数组
	public String[] getKeys() {
		return keys;
	}
	public void setKeys(String[] keys) {
		this.keys = keys;
	}
	//字段名称数据
	public String[] getNames() {
		return names;
	}
	public void setNames(String[] names) {
		this.names = names;
	}
	//字段数据类型
	public String[] getColumnType() {
		return columnType;
	}
	public void setColumnType(String[] columnType) {
		this.columnType = columnType;
	}
	//字段控件类型
	public String[] getItemType() {
		return itemType;
	}
	public void setItemType(String[] itemType) {
		this.itemType = itemType;
	}
	//主键
	public String getPkName() {
		return pkName;
	}
	public void setPkName(String pkName) {
		this.pkName = pkName;
	}
	//检索表名
	public String getQueryTableName() {
		return queryTableName;
	}
	public void setQueryTableName(String queryTableName) {
		this.queryTableName = queryTableName;
	}
	//保存表名
	public String getSaveTableName() {
		return saveTableName;
	}
	public void setSaveTableName(String saveTableName) {
		this.saveTableName = saveTableName;
	}
	/**
	 * 设置某字段是否需要更新
	 * @param _key
	 * @param need
	 */
	public void setNeedSave(String _key, boolean need) {
		int li_index = findIndex(_key);
		if (li_index >= 0) {
			needSave[li_index] = need;
		}
	}
	/**
	 * 是否需要更新数组
	 * @return
	 */
	public boolean[] isNeedSave() {
		return needSave;
	}
	public void setNeedSaves(boolean[] needSave) {
		this.needSave = needSave;
	}
	public boolean[] getNeedSaves() {
		return needSave;
	}
	/**
	 * 取得数据.. 
	 * @param _key
	 * @return
	 */
	public Object getObject(String _key) {
		int li_index = findIndex(_key);
		if (li_index >= 0) {
			return getDatas()[li_index];
		}
		return null;
	}
	/**
	 * 设置数据.. 
	 * @param _key
	 * @param _obj
	 */
	public void setObject(String _key, Object _obj) {
		int li_index = findIndex(_key);
		if (li_index >= 0) {
			datas[li_index] = _obj; //
		}
	}
	
	/*******以下都是属性操作****James.W************End***********/
	


	/**
	 * 获得对应的HashVO
	 */
	public HashVO getHashVO(){
		String[] keys=this.getKeys();
		HashVO vo=new HashVO();
		for(int i=0;i<keys.length;i++){
			vo.setAttributeValue(keys[i], this.getObject(keys[i]));
		}
		return vo;
	}
	
	
	
	/**
	 * 忽略大小写寻找字段名对应字段序号
	 * @param _key
	 * @return
	 */
	private int findIndex(String _key) {
		for (int i = 0; i < getKeys().length; i++) {
			if (getKeys()[i].equalsIgnoreCase(_key)) {
				return i;
			}
		}
		return -1;
	}
		
	/**
	 * 获得序列配置
	 * @return
	 */
	public String getSequenceName() {
		return sequenceName;
	}

	/**
	 * 返回当前编辑状态 
	 * @return
	 */
	public String getEditType() {
		RowNumberItemVO rowNumberVO = (RowNumberItemVO) getDatas()[0]; // 行号VO!!!永远在第一列!!
		return rowNumberVO.getState(); //
	}

	/**
	 * 设置当前编辑状态
	 * @return
	 */
	public void setEditType(String _type) {
		RowNumberItemVO rowNumberVO = (RowNumberItemVO) getDatas()[0]; // 行号VO!!!永远在第一列!!
		rowNumberVO.setState(_type);
	}

	/**
	 * 返回当前编辑状态
	 * @return
	 */
	public RowNumberItemVO getRowNumberItemVO() {
		RowNumberItemVO rowNumberVO = (RowNumberItemVO) getDatas()[0]; // 行号VO!!!永远在第一列!!
		return rowNumberVO; //
	}

	/**
	 * 设置表对应的Sequnce名字
	 * @param sequenceName
	 */
	public void setSequenceName(String sequenceName) {
		this.sequenceName = sequenceName;
	}

	// 以下都是需要保存的数据的列,名称,控件类型,数据类型等
	public String[] getNeedSaveKeys() {
		Vector v_tmp = new Vector();
		for (int i = 0; i < keys.length; i++) {
			if (isNeedSave()[i]) {
				v_tmp.add(keys[i]);
			}
		}
		return (String[]) v_tmp.toArray(new String[0]);
	}

	public String[] getNeedSaveNames() {
		Vector v_tmp = new Vector();
		for (int i = 0; i < names.length; i++) {
			if (isNeedSave()[i]) {
				v_tmp.add(names[i]);
			}
		}
		return (String[]) v_tmp.toArray(new String[0]);
	}

	public String[] getNeedSaveItemType() {
		Vector v_tmp = new Vector();
		for (int i = 0; i < itemType.length; i++) {
			if (isNeedSave()[i]) {
				v_tmp.add(itemType[i]);
			}
		}
		return (String[]) v_tmp.toArray(new String[0]);
	}

	public String[] getNeedSaveColumnType() {
		Vector v_tmp = new Vector();
		for (int i = 0; i < columnType.length; i++) {
			if (isNeedSave()[i]) {
				v_tmp.add(columnType[i]);
			}
		}
		return (String[]) v_tmp.toArray(new String[0]);
	}

	public Object[] getNeedSaveData() {
		Vector v_tmp = new Vector();
		for (int i = 0; i < datas.length; i++) {
			if (isNeedSave()[i]) {
				v_tmp.add(datas[i]);
			}
		}
		return (Object[]) v_tmp.toArray(new Object[0]);
	}

	/**
	 * 得到需要保存数据的直接塞到Oracle中的实际值,它主要是对下拉框VO与参照VO进行了一下处理,其他的都是直接用 toString方法!!
	 * 
	 * @return
	 */
	public String[] getNeedSaveDataRealValue() {
		Object[] objs = getNeedSaveData();
		String[] str_return = new String[objs.length];
		for (int i = 0; i < str_return.length; i++) {
			if (objs[i] == null) {
				str_return[i] = null;
			} else {
				if (objs[i] instanceof String) {
					str_return[i] = (String) objs[i];
				} else if (objs[i] instanceof ComBoxItemVO) {
					ComBoxItemVO vo = (ComBoxItemVO) objs[i];
					str_return[i] = vo.getId();
				} else if (objs[i] instanceof RefItemVO) {
					RefItemVO vo = (RefItemVO) objs[i];
					str_return[i] = vo.getId();
				} else {
					str_return[i] = "" + objs[i]; //
				}

				if (str_return[i] != null && str_return[i].trim().equals("")) {
					str_return[i] = null;
				}
			}
		}

		return str_return;
	}

	/**
	 * 真正的SQL语句
	 * 
	 * @return
	 */
	public String getInsertSQL() {
		StringBuffer sb_sql = new StringBuffer();
		sb_sql.append("insert into " + getSaveTableName()); //
		sb_sql.append("(");

		String[] str_keys = getNeedSaveKeys(); // 取得所有需要参与保存的列!!
		String[] str_itemTypes = getNeedSaveItemType(); // 控件类型
		String[] str_columnTypes = getNeedSaveColumnType(); // 数据列的类型

		// 先拼出所有的列名
		for (int i = 0; i < str_keys.length; i++) {
			sb_sql.append(str_keys[i]);
			if (i != str_keys.length - 1) {
				sb_sql.append(",");
			}
		}

		sb_sql.append(")");
		sb_sql.append(" values ");
		sb_sql.append("(");

		// 再拼出所有的Value值!!
		String[] str_realValue = getNeedSaveDataRealValue(); //
		for (int i = 0; i < str_keys.length; i++) {
			if (str_keys[i].equalsIgnoreCase("version")) { // 如果某一列叫version,则直接用1.0处理之
				sb_sql.append("'1'"); // 如果是版本号的话则直接用1.0开始!!
			} else { // 如果不是版本号
				String str_value = convertSQLValue(str_realValue[i]);
				if (str_value == null || str_value.trim().equals("")) {
					sb_sql.append("null"); // 真正的值!!
				} else {
					if (str_itemTypes[i].equals("日历")) {
						if (str_columnTypes[i] != null
								&& str_columnTypes[i].equals("DATE")) {
							sb_sql.append("to_date('" + str_value
									+ "','YYYY-MM-DD')"); // 真正的值!!
						} else {
							sb_sql.append("'" + str_value + "'"); // 真正的值!!
						}
					} else if (str_itemTypes[i].equals("时间")) {
						if (str_columnTypes[i] != null
								&& str_columnTypes[i].equals("DATE")) {
							sb_sql.append("to_date('" + str_value
									+ "','YYYY-MM-DD HH24:MI:SS')"); // 真正的值!!
						} else {
							sb_sql.append("'" + str_value + "'"); // 真正的值!!
						}
					} else {
						sb_sql.append("'" + str_value + "'"); // 真正的值!!
					}
				}

			}

			if (i != str_keys.length - 1) {
				sb_sql.append(",");
			}
		}

		sb_sql.append(")"); //
		return sb_sql.toString();
	}


	public String getUpdateSQL() {
		StringBuffer sb_sql = new StringBuffer();
		sb_sql.append("update " + getSaveTableName()); //
		sb_sql.append(" set ");

		String[] str_keys = getNeedSaveKeys(); //
		String[] str_itemTypes = getNeedSaveItemType(); // 控件类型
		String[] str_columnTypes = getNeedSaveColumnType(); // 数据列的类型

		String[] str_realValue = getNeedSaveDataRealValue(); // 取得真正的值!!!!
		for (int i = 0; i < str_keys.length; i++) {
			if (str_keys[i].equalsIgnoreCase("version")) {
				sb_sql.append("version=NVL(version,0) + 1"); // 如果是版本号则直接加1
			} else {
				String str_value = convertSQLValue(str_realValue[i]); // 转换一下SQL!!
				if (str_value == null || str_value.trim().equals("")) {
					sb_sql.append(str_keys[i] + "=null"); // 如果是空值,则直接填空!!
				} else {
					if (str_itemTypes[i].equals("日历")) {
						if (str_columnTypes[i] != null
								&& str_columnTypes[i].equals("DATE")) {
							sb_sql.append(str_keys[i] + "=to_date('"
									+ str_value + "','YYYY-MM-DD')"); // 真正的值!!
						} else {
							sb_sql.append(str_keys[i] + "='" + str_value + "'"); // 真正的值!!
						}
					} else if (str_itemTypes[i].equals("时间")) {
						if (str_columnTypes[i] != null
								&& str_columnTypes[i].equals("DATE")) {
							sb_sql.append(str_keys[i] + "=to_date('"
									+ str_value + "','YYYY-MM-DD HH24:MI:SS')"); // 真正的值!!
						} else {
							sb_sql.append(str_keys[i] + "='" + str_value + "'"); // 真正的值!!
						}
					} else {
						sb_sql.append(str_keys[i] + "='" + str_value + "'"); // 真正的值!!
					}
				}

			}

			if (i != str_keys.length - 1) { // 如果不是最后一位,且最后一位又不是主键
				sb_sql.append(",");
			}
		}

		sb_sql.append(" where " + getUpdateWhereCondition()); //
		return sb_sql.toString();
	}

	/**
	 * 取得删除的SQL!!!!!!
	 * 
	 * @return
	 */
	public String getDeleteSQL() {
		return "delete from " + getSaveTableName() + " where "
				+ getUpdateWhereCondition(); //
	}

	
	public String getPkValue() {
		return findPkValue();
	}

	/**
	 * 找到主键的值
	 * 
	 * @return
	 */
	public String findPkValue() {
		if (pkName == null || pkName.trim().equals("")) {
			return ""; //
		} else { //
			// 去所有控件中找,如果找到则返回之
			for (int i = 0; i < keys.length; i++) {
				if (keys[i].equalsIgnoreCase(pkName)) {
					return "" + ((datas[i] == null) ? "" : datas[i]); //
				}
			} // ..
			return ""; // 如果没找到则返回空
		}
	} // ....

	/**
	 * 拼成做update时where后面的条件!!!专门为做update操作时用!!! 届时只要在 where 后面加上指示这些条件即可!!!
	 */
	public String getUpdateWhereCondition() {
		String str_return = " ";
		if (pkName != null && !pkName.trim().equals("")) { // 如果主键不为null,且同时不是空字符串!!!!!即是有主键的!!!
			str_return = str_return + pkName + "='" + findPkValue() + "' "; // //..
		} else {
			RowNumberItemVO rowNumberVO = (RowNumberItemVO) getDatas()[0]; // 行号VO!!!永远在第一列!!非常麻烦!!!

			String[] str_savedKeys = getNeedSaveKeys(); // 需要保存的列名
			String[] str_itemtypes = getNeedSaveItemType(); // 控件类型
			String[] str_columnTypes = getNeedSaveColumnType(); // 数据库列类型!!

			for (int i = 0; i < str_savedKeys.length; i++) {
				String key = str_savedKeys[i];
				String itemtype = str_itemtypes[i]; //
				String columntype = str_columnTypes[i]; //

				if (i != 0) { // 如果不是第一个条件则要加上and
					str_return = str_return + " and ";
				}

				String str_oldValue = rowNumberVO.getOldValue(key); // 存放在行号VO中的原来的数据!!!!!!
				str_oldValue = convertSQLValue(str_oldValue); // 替换一下其中的单引号'
				if (str_oldValue.equals("")) { // 如果原来数据为空,则是判断为空!!
					str_return = str_return + key + " is null "; //
				} else { // 如果值不为空
					if (itemtype.equals("文本框")) {
						str_return = str_return + key + "='" + str_oldValue
								+ "'"; //
					} else if (itemtype.equals("下拉框")) { //
						str_return = str_return + key + "='" + str_oldValue
								+ "'"; //
					} else if (itemtype.equals("参照")) {
						str_return = str_return + key + "='" + str_oldValue
								+ "'"; //
					} else if (itemtype.equals("日历")) {
						if (columntype != null && columntype.equals("DATE")) {
							str_return = str_return + key + "=to_date('"
									+ str_oldValue + "','YYYY-MM-DD')"; //
						} else {
							str_return = str_return + key + "='" + str_oldValue
									+ "' "; //
						}
					} else if (itemtype.equals("时间")) { // 如果是时间控件!!
						if (columntype != null && columntype.equals("DATE")) {
							str_return = str_return + key + "=to_date('"
									+ str_oldValue
									+ "','YYYY-MM-DD HH24:MI:SS')"; //
						} else {
							str_return = str_return + key + "='" + str_oldValue
									+ "'"; //
						}
					} else {
						str_return = str_return + key + "='" + rowNumberVO.getOldValue(key) + "'"; //
					}
				}
			} // end for 循环!!!
		}

		return str_return;
	}

	/**
	 * 取得Where条件..
	 * 
	 * @return
	 */
	public String getWhereCondition() {
		return getUpdateWhereCondition();
	}

	/**
	 * 替换SQL中的单引号,因为单引号会导致保存失败!!
	 * 
	 * @param _value
	 * @return
	 */
	private String convertSQLValue(String _value) {
		if (_value == null) {
			return null;
		} else {
			return replaceAll(_value, "'", "''");
		}
	}

	/**
	 * 替换字符
	 * 
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
			str_remain = str_remain.substring(li_pos + old_item.length(),
					str_remain.length());
		}
		str_return = str_return + str_remain; // 将剩余的加上
		return str_return;
	}

	/**
	 * 判断是否要处理版本号,即是否需要处理乐观锁!!! 需要处理乐观锁的前提是,必须有一个字段叫version,而且该列是参与保存的!!!
	 * 
	 * @return
	 */
	public boolean isDealVersion() {
		String[] needSaveKeys = getNeedSaveKeys();
		for (int i = 0; i < needSaveKeys.length; i++) {
			if (needSaveKeys[i].equalsIgnoreCase("version")) {
				return true;
			}
		}
		return false; //
	}

	/**
	 * 取得当前记录版本号!!
	 * 
	 * @return
	 */
	public Integer getVersion() {
		Object ver = getObject("version");
		return new Integer(StringUtil.parseInt(ver,1));
	}

	public void updateVersion() {
		Object ver=this.getObject("version");		
		int version=0;
    	if(ver==null){
    		version=0;
    	}else if(ver instanceof String){
    		String tmp=(String)ver;
    		if(tmp.indexOf(".")>=0){
    			try{
        			version=(new Double(tmp)).intValue();
        		}catch(Exception e){
        			version=0;
        		}	
    		}
    		try{
    			version=Integer.parseInt(tmp);
    		}catch(Exception e){
    			version=0;
    		}
    		
    	}else if(ver instanceof Integer){
    		version=((Integer)ver).intValue()+1;
    	}else{
    		version=0;
    	}
    	ver=new Integer(version+1);
		this.setObject("version", ver);
	}

	public String getRealValue(String _key) {
		Object obj = getObject(_key);
		if (obj == null) {
			return null;
		}
		if (obj instanceof String) {
			return (String) obj;
		} else if (obj instanceof ComBoxItemVO) {
			ComBoxItemVO vo = (ComBoxItemVO) obj;
			return vo.getId();
		} else if (obj instanceof RefItemVO) {
			RefItemVO vo = (RefItemVO) obj;
			return vo.getId();
		} else {
			return "" + obj; //
		}

	}

	// 给billvo设真实值
	public void setRealValue(String _key, String _value) {
		Object obj = getObject(_key);
		if (obj == null) {
			return;
		}
		if (obj instanceof String) {
			obj = _value;
		} else if (obj instanceof ComBoxItemVO) {
			ComBoxItemVO vo = (ComBoxItemVO) obj;
			vo.setId(_value);
			vo.setCode(_value);
			vo.setName(_value);
		} else if (obj instanceof RefItemVO) {
			RefItemVO vo = (RefItemVO) obj;
			vo.setId(_value);
			vo.setCode(_value);
			vo.setName(_value);
		}
	}

	/**
	 * 将该BillVO快速转换成某一个具体的实际类!!具体的实际类就是包括set,get方法的一些实际类!!即将参数级的一些API变成方法级了!!
	 * 用法如下: smartx.system.login.vo.TestRealVO realVO =
	 * (smartx.system.login.vo.TestRealVO)
	 * billVO.convertToRealOBJ(smartx.system.login.vo.TestRealVO.class); //
	 * 
	 * @param _class
	 *            实际的类名
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public Object convertToRealOBJ(java.lang.Class _class)
			throws InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		Object returnobj = _class.newInstance(); // 创建实例!!
		Method[] methods = _class.getMethods(); // 取得所有方法!!!包括所有父类的!!!
		String[] all_keys = this.getKeys();
		for (int i = 0; i < all_keys.length; i++) {
			for (int j = 0; j < methods.length; j++) {
				Class[] parClass = methods[j].getParameterTypes();
				// System.out.println("方法名:" + methods[j].getName());
				if (methods[j].getName().equalsIgnoreCase("set" + all_keys[i])
						&& parClass.length == 1) { // 该方法名正好是以set开头,且set后面的名称正好又是该弄的名称,并且又是只有一个参数!!
					// System.out.println("找到对应方法:" + methods[j].getName()); //
					String str_realvalue = getRealValue(all_keys[i]); // 取得真正的值
					if (str_realvalue == null) { // 如果是空对象
						methods[j].invoke(returnobj, new Object[] { null }); // 设置值!!
					} else { //
						Object setObject = null;
						if (parClass[0].equals(java.lang.String.class)) { // 如果是String类型
							setObject = str_realvalue;
						} else if (parClass[0].equals(java.lang.Integer.class)) { // 如果是Integer类型
							setObject = new Integer(str_realvalue); //
						} else if (parClass[0].equals(java.lang.Long.class)) { // 如果是Long类型
							setObject = new java.lang.Long(str_realvalue); //
						} else if (parClass[0].equals(java.lang.Double.class)) { // 如果是Double类型
							setObject = new java.lang.Double(str_realvalue); //
						} else if (parClass[0]
								.equals(java.math.BigDecimal.class)) { // 如果是BigDecimal类型
							setObject = new java.math.BigDecimal(str_realvalue); //
						} else {
							setObject = str_realvalue; //
						}

						methods[j]
								.invoke(returnobj, new Object[] { setObject }); // 设置值!!!
					}
					break; // 中断内层循环!!提交效率,即找到后就不往下找了!!
				}
			}
		}
		return returnobj;
	}

	public String getTempletCode() {
		return templetCode;
	}

	public void setTempletCode(String templetCode) {
		this.templetCode = templetCode;
	}

	public BillVO deepClone() throws IOException, ClassNotFoundException {
		ByteArrayOutputStream buf = new ByteArrayOutputStream();
		ObjectOutputStream out = new ObjectOutputStream(buf);
		out.writeObject(this);
		ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(
				buf.toByteArray()));
		return (BillVO) in.readObject();
	}

	// 如果设置了toString显示的列名,则显示之,否则显示第三列!!!!
	public String toString() {
		if (getToStringKeyName() == null) {
			String key = getKeys()[2]; // ..
			Object obj = getObject(key); // ..
			return obj == null ? "" : ("" + obj); // ..
		} else {
			Object obj = getObject(getToStringKeyName());
			return obj == null ? "" : ("" + obj);
		}
	}

	public String getToStringKeyName() {
		return toStringKeyName;
	}

	public void setToStringKeyName(String _toStringKeyName) {
		this.toStringKeyName = _toStringKeyName;
	}

	public boolean containsKey(String _itemkey) {
		String[] str_keys = getKeys();
		for (int i = 0; i < str_keys.length; i++) {
			if (str_keys[i].equalsIgnoreCase(_itemkey)) {
				return true;
			}
		}
		return false;
	}

	public String getStringValue(String _itemkey) {
		Object obj = getObject(_itemkey);
		return obj==null?null:obj.toString();  //
	}
}

/*******************************************************************************
 * $RCSfile: BillVO.java,v $ $Revision: 1.8.2.5 $ $Date: 2010/01/12 02:07:40 $
 * 
 * $Log: BillVO.java,v $
 * Revision 1.8.2.5  2010/01/12 02:07:40  wangqi
 * *** empty log message ***
 *
 * Revision 1.8.2.4  2009/12/04 07:06:54  wangqi
 * *** empty log message ***
 *
 * Revision 1.8.2.3  2009/01/16 08:51:10  wangqi
 * *** empty log message ***
 *
 * Revision 1.8.2.2  2008/10/13 10:44:41  wangqi
 * *** empty log message ***
 *
 * Revision 1.8.2.1  2008/09/26 06:57:19  wangqi
 * *** empty log message ***
 *
 * Revision 1.8  2008/01/23 04:46:34  wangqi
 * *** empty log message ***
 *
 * Revision 1.7  2008/01/03 08:04:05  wangqi
 * *** empty log message ***
 * Revision 1.6 2007/08/01 06:07:13 qilin
 * VERSION为null时用0进行操作
 * 
 * Revision 1.5 2007/07/31 08:22:13 sunxf MR#:Nova 20-17
 * 
 * Revision 1.4 2007/07/23 10:58:58 sunxf Nova 20-14 平台层面更新数据的地方提供乐观锁控制，解决并发访问问题
 * 
 * Revision 1.3 2007/07/11 08:02:04 sunxf 增加方法setRealValue
 * 
 * Revision 1.2 2007/05/31 07:38:20 qilin code format
 * 
 * Revision 1.1 2007/05/17 06:03:16 qilin no message
 * 
 * Revision 1.16 2007/04/06 04:46:35 sunxf *** empty log message ***
 * 
 * Revision 1.15 2007/04/02 05:30:43 shxch *** empty log message ***
 * 
 * Revision 1.14 2007/03/30 10:00:08 shxch *** empty log message ***
 * 
 * Revision 1.13 2007/03/22 06:26:39 shxch *** empty log message ***
 * 
 * Revision 1.12 2007/03/16 03:27:06 shxch *** empty log message ***
 * 
 * Revision 1.11 2007/03/16 02:49:39 shxch *** empty log message ***
 * 
 * Revision 1.10 2007/03/15 03:51:46 shxch *** empty log message ***
 * 
 * Revision 1.9 2007/03/15 03:15:55 shxch *** empty log message ***
 * 
 * Revision 1.8 2007/03/07 01:48:46 shxch *** empty log message ***
 * 
 * Revision 1.7 2007/03/05 08:10:24 shxch *** empty log message ***
 * 
 * Revision 1.6 2007/03/05 04:04:23 shxch *** empty log message ***
 * 
 * Revision 1.5 2007/03/05 02:48:29 shxch *** empty log message ***
 * 
 * Revision 1.4 2007/03/05 01:53:51 shxch *** empty log message ***
 * 
 * Revision 1.3 2007/03/05 01:46:03 shxch *** empty log message ***
 * 
 * Revision 1.2 2007/01/30 04:16:43 lujian *** empty log message ***
 * 
 * 
 ******************************************************************************/
