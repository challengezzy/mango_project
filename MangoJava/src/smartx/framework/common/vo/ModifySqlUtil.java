/**************************************************************************
 * $RCSfile: ModifySqlUtil.java,v $  $Revision: 1.5.2.5 $  $Date: 2010/01/20 09:52:09 $
 **************************************************************************/
package smartx.framework.common.vo;

import java.io.*;
import java.util.*;

import smartx.framework.common.ui.*;
import smartx.framework.metadata.vo.*;


/**
 * SQL语句处理
 * @author cxj110
 *
 */
public class ModifySqlUtil implements Serializable {

    private static final long serialVersionUID = -2162276869135203860L;

    /**
     *
     */
    public ModifySqlUtil() {
    }

    /**
     * @param _sql
     * @return
     * @throws Exception
     */
    public String getModifiedSql(String _sql) throws Exception {
        String str_sql = "";
        try {
            str_sql = getModifySql(_sql);
        } catch (Exception e) {
            throw e;
        }
        return str_sql;
    }

    /**
     * @param _sql
     * @param _bracket_parameter
     * @return
     * @throws Exception
     */
    private String getModifySql(String _sql) throws Exception {
        String temp_sql = _sql;
        if (temp_sql.indexOf("{") < 0) {
            if (temp_sql.indexOf("}") >= 0) {
                throw new Exception("SQL语句错误，{}不匹配！");
            }
            return temp_sql;
        }

        String begin_sql = temp_sql.substring(0, temp_sql.indexOf("{"));
        String mid_sql = temp_sql.substring(temp_sql.indexOf("{"), temp_sql.length());

        int end_index = -1;
        if (mid_sql.indexOf("}") > 0) {
            end_index = mid_sql.indexOf("}");
        }
        if (end_index == -1) {
            throw new Exception("SQL语句错误，{}不匹配！");
        }
        String bracket_id = mid_sql.substring(1, end_index);

        String _bracket_parameter = (String) NovaClientEnvironment.getInstance().get(bracket_id);

        if (_bracket_parameter == null) {
            _bracket_parameter = "";
        }
        String end_sql = "";

        if (end_index + 1 < mid_sql.length()) {
            end_sql = getModifySql(mid_sql.substring(end_index + 1, mid_sql.length()));
        } else {
            end_sql = "";
        }

        return begin_sql + _bracket_parameter + end_sql;
    }

    private void getBracketID(String _sql, Vector _vec) throws Exception {
        String temp_sql = _sql;
        if (temp_sql.indexOf("{") < 0) {
            if (temp_sql.indexOf("}") >= 0) {
                throw new Exception("语句[" + _sql + "]中的大括号\"{}\"没有成对品配!!");
            }
            return;
        }
        String mid_sql = temp_sql.substring(temp_sql.indexOf("{"), temp_sql.length());

        int end_index = -1;
        if (mid_sql.indexOf("}") > 0) {
            end_index = mid_sql.indexOf("}");
        }
        if (end_index == -1) {
            throw new Exception("语句[" + _sql + " ]中的大括号\"{}\"没有成对品配!!");
        }
        String bracket_id = mid_sql.substring(1, end_index);
        _vec.add(bracket_id);

        getBracketID(mid_sql.substring(end_index + 1, mid_sql.length()), _vec);
    }

    public String getStrBracketID(String _sql) throws Exception {
        Vector vec = new Vector();
        try {
            getBracketID(_sql, vec);
        } catch (Exception e) {
            throw e;
        }
        if (vec.size() == 0) {
            return null;
        } else {
            return (String) vec.get(0);
        }
    }

    public Vector getVecBracketID(String _sql) throws Exception {
        Vector vec = new Vector();
        try {
            getBracketID(_sql, vec);
        } catch (Exception e) {
            throw e;
        }
        return vec;
    }

    public String getBaseSql(String _sql) {
        String temp_sql = _sql;
        int sql_index = temp_sql.toUpperCase().indexOf("WHERE");
        String result_str = temp_sql.substring(0, sql_index - 1);
        return result_str;
    }

    /**
     *
     * @param _inittext  比如:  getColValue("n1_menu","{menutype}","{menuid}");
     * @return  [menutype][menuid];
     * @throws Exception
     */
    public Vector findItemKey(String _inittext) throws Exception {
        Vector vec = new Vector();
        getBracketID(_inittext, vec);
        return vec;
    }

    private String convertStr(String _str, int _begin, int _end, Vector _v, int _index) throws Exception {
        if (_str == null) {
            return null;
        }
        System.out.println("The get str is:" + _str);
        String str_begin = _str.substring(0, _begin);
        String str_mid = _str.substring(_begin + 1, _str.length());

        int end_index = str_mid.indexOf("}");
        if (end_index <= 0) {
            throw new Exception("SQL语句错误，{}不匹配！");
        }

        int begin_index = str_mid.indexOf("{");

        if (begin_index >= 0 && begin_index < end_index) {
            convertStr(str_mid, begin_index, end_index, _v, _index);
        }

        String _bracket_parameter = (String) _v.get(_index);

        if (_bracket_parameter == null) {
            _bracket_parameter = "";
        }

        String str_end = str_mid.substring(end_index + 1, str_mid.length());

        return str_begin + _bracket_parameter + str_end;
    }

    public String replace_ItemKey(String _inittext, Vector _v, int _index) throws Exception {
        String temp_sql = _inittext;

        int _begin = temp_sql.indexOf("{");
        if (_begin < 0) {
            if (temp_sql.indexOf("}") >= 0) {
                throw new Exception("SQL语句错误，{}不匹配！");
            }
            return temp_sql;
        }
        if (_index == _v.size()) {
            return temp_sql;
        }

        String begin_sql = temp_sql.substring(0, _begin);
        String mid_sql = temp_sql.substring(_begin + 1, temp_sql.length());

        int end_index = mid_sql.indexOf("}");
        if (end_index >= 0) {
            end_index = mid_sql.indexOf("}");
            int begin_index = mid_sql.indexOf("{");
            if (begin_index >= 0 && begin_index < end_index) {
                mid_sql = convertStr(mid_sql, begin_index, end_index, _v, _index);
                System.out.println("\nThe get mid_str is:" + mid_sql);
            }
        } else {
            throw new Exception("SQL语句错误，{}不匹配！");
        }
        end_index = mid_sql.indexOf("}");

        String _bracket_parameter = (String) _v.get(_index);

        if (_bracket_parameter == null) {
            _bracket_parameter = "";
        }
        String end_sql = "";

        if (end_index + 1 < mid_sql.length()) {
            end_sql = replace_ItemKey(mid_sql.substring(end_index + 1, mid_sql.length()), _v, _index + 1);
        } else {
            end_sql = "";
        }
        return begin_sql + _bracket_parameter + end_sql;
    }

    /**
     *
     * @param _inittext
     * @param _newtext
     * @return
     * @throws Exception
     */
    public String replace_ItemKey(String _inittext, Vector _v) throws Exception {
        if (_v.size() == 0) {
            return _inittext;
        }
        String str = "";
        try {
            str = replace_ItemKey(_inittext, _v, 0);
        } catch (Exception e) {
            throw e;
        }
        return str;
    }

    /**
     * 从客户端环境或页面中找出某个key的值
     * @param _key
     * @param _otherkey
     * @param _env
     * @param _map
     * @return
     */
    private String getParameter(String _key, String _otherkey, NovaClientEnvironment _env, HashMap _map) {
        String _bracket_parameter = null;
        Object obj = _env.get(_key); //从客户端环境变量取
        if (obj == null) {
            Object _obj = _map.get(_key); //再从页面数据取
            if (_obj == null) {
                _bracket_parameter = "";
            } else {
                if (_obj instanceof String) {
                    _bracket_parameter = _obj.toString();
                }
                if (_obj instanceof ComBoxItemVO) {
                    if (_otherkey == null) {
                        _bracket_parameter = ( (ComBoxItemVO) _obj).getId(); //
                    } else {
                        _bracket_parameter = ( (ComBoxItemVO) _obj).getItemValue(_otherkey); //取下拉框VO的其他字段!!!
                    }
                } else if (_obj instanceof RefItemVO) {
                    if (_otherkey == null) {
                        _bracket_parameter = ( (RefItemVO) _obj).getId(); //
                    } else {
                        _bracket_parameter = ( (RefItemVO) _obj).getItemValue(_otherkey); //取参照VO的其他字段!!!
                    }

                } else {
                    _bracket_parameter = _obj.toString();
                }
            }
        } else {
            _bracket_parameter = obj.toString(); //
        }
        return _bracket_parameter;
    }

    /**
     * 处理内嵌的{}
     * @param _str
     * @param _begin
     * @param _end
     * @param _env
     * @param _map
     * @return
     * @throws Exception
     */
    private String convert(String _str, int _begin, int _end, NovaClientEnvironment _env, HashMap _map) throws
        Exception {
        if (_str == null) {
            return null;
        }
        String str_begin = _str.substring(0, _begin);
        String str_mid = _str.substring(_begin + 1, _str.length());

        int end_index = str_mid.indexOf("}");
        if (end_index <= 0) {
            throw new Exception("SQL语句错误，{}不匹配！");
        }

        int begin_index = str_mid.indexOf("{");

        if (begin_index >= 0 && begin_index < end_index) {
            convert(str_mid, begin_index, end_index, _env, _map);
        }

        String bracket_id = str_mid.substring(0, end_index); //这是{}中间的内容
        int li_pos = bracket_id.indexOf("."); //
        String str_itemkey = null;
        String str_itemkey_otherfield = null;
        if (li_pos > 0) { //如果发现有个"."
            str_itemkey = bracket_id.substring(0, li_pos);
            str_itemkey_otherfield = bracket_id.substring(li_pos + 1, bracket_id.length()); //处理内嵌"{}"
        } else {
            str_itemkey = bracket_id;
        }

        String _bracket_parameter = getParameter(str_itemkey, str_itemkey_otherfield, _env, _map);
        if (_bracket_parameter == null) {
            _bracket_parameter = "";
        }

        String str_end = str_mid.substring(end_index + 1, str_mid.length());

        return str_begin + _bracket_parameter + str_end;
    }

    

    /**
     * 根据参照原始定义取出参照的类型与SQL
     * TODO 需要进一步说明参照参数含义
     * @param _refdesc
     * @return
     */
    public String[] getRefDescTypeAndSQL(String _refdesc) {
        if (_refdesc == null) {
            return null;
        }
        
        //2012.2.20 add by xuzhilin 先去除[]中扩展参数的内容
        int index = _refdesc.indexOf("[");
        if(index>0){
        	_refdesc = _refdesc.substring(0,index);
        }

        String str_type = null;                //参照类型
        String str_realsql = null;             //真正的SQL
        String str_parentfieldname = null;     //树型参照的父亲字段
        String str_pkfieldname = null;         //树型参照的主键
        String str_datasourcename = null;      //数据源名称
        String str_loadall = null;             //树型参照，是否全部加载
        String str_refvalid=null;                 //参照前置校验计算式，返回“true”表示校验通过
        
        
        String str_table = null;
        String str_table_fk = null;
        
        
        String _ref=_refdesc;//用来运算参照配置的变量
        //判断有没有前缀 TABLE/TABLE2/TABLE3/TREE/TREE2/TREE3/MUTITREE 默认作为TABLE
        if(!_refdesc.startsWith("TABLE")&&!_refdesc.startsWith("TREE")&&!_refdesc.startsWith("MUTITREE")&&!_refdesc.startsWith("CUST")){
        	str_type = "TABLE";
        }else{
        	int li=_refdesc.indexOf(":");
        	str_type=_refdesc.substring(0,li).toUpperCase();
        	_ref=_refdesc.substring(li+1).trim();        	
        }
        
        //判断是否存在参照校验计算式，如果有，先把这个计算式剔出来
        if(_ref.indexOf("refvalid")>0){
        	str_refvalid=_ref.substring(_ref.indexOf("refvalid"));
        	str_refvalid= str_refvalid.substring(str_refvalid.indexOf("=")+1);
        	if(str_refvalid.endsWith(";")) str_refvalid.substring(0, str_refvalid.length()-2);
        	_ref=_ref.substring(0, _ref.indexOf("refvalid")-1).trim();
        	if(_ref.endsWith(";")) _ref.substring(0, _ref.length()-2);
        }else{
        	str_refvalid="true";
        }
        
        
        if (str_type.equalsIgnoreCase("TABLE") || str_type.equalsIgnoreCase("TABLE2") || str_type.equalsIgnoreCase("TABLE3")) {
            String str_remain = _ref; //剩下的
            String[] str_arrays = str_remain.split(";"); //
            str_realsql = str_arrays[0];            
            if (str_arrays.length == 1) {
                str_datasourcename = NovaClientEnvironment.getInstance().getDefaultDatasourceName(); //默认数据源!!!
            } else if (str_arrays.length == 2) {
                str_datasourcename = getDataSourceName(str_arrays[1]); //
            }
        } else if (str_type.equalsIgnoreCase("TREE") || str_type.equalsIgnoreCase("TREE2") || str_type.equalsIgnoreCase("TREE3")) { //树型..
            try {
                String str_remain = _ref; //
                String[] str_arrays = str_remain.split(";"); //
                //[0]-tree检索sql
                str_realsql = str_arrays[0].trim();
                //[1]-tree父子关系
                String[] str_treeFieldNames = getTreeFielNames(str_arrays[1].trim()); //
                str_parentfieldname = str_treeFieldNames[0];
                str_pkfieldname = str_treeFieldNames[1];
                //[2-n]-其他参数
                //TODO 这里向下的算法有问题。
                //str_arrays 是参数列表 其中 [0]-是检索sql，其他都是参数（默认写法是key=value）
                //  error：当前的算法是固定了参数的位置，如果改变参数的次序，则无法解析。
                //  fix: 把所有参数分解为key和value，放入map中，key变为小写，然后再判断参数是否存在做相应处理。
                HashMap map=new HashMap();
                for(int i=2;i<str_arrays.length;i++){
                	int sp=str_arrays[i].indexOf("=");
                	String key=str_arrays[i].substring(0,sp); 
                	String value=str_arrays[i].substring(sp+1);
                	map.put(key.trim().toLowerCase(), value);                	
                }
                
                str_datasourcename=(map.containsKey("ds"))?((String)map.get("ds")):(NovaClientEnvironment.getInstance().getDefaultDatasourceName());
                str_loadall=(map.containsKey("loadall"))?((String)map.get("loadall")):"true";
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if(str_type.equalsIgnoreCase("MUTITREE")){
        	try {
                String str_remain = _ref; //
                String[] str_arrays = str_remain.split(";"); //
                //[0]-tree检索sql
                str_realsql = str_arrays[0].trim();
                //[1]-tree父子关系
                String[] str_treeFieldNames = getTreeFielNames(str_arrays[1].trim()); //
                str_parentfieldname = str_treeFieldNames[0];
                str_pkfieldname = str_treeFieldNames[1];
                //[2]-检索表名
                str_table = str_arrays[2];
                //[3]-检索外键
                str_table_fk = str_arrays[3];
                
                //[4-n]-其他参数
                //TODO 这里向下的算法有问题。
                //str_arrays 是参数列表 其中 [0]-是检索sql，其他都是参数（默认写法是key=value）
                //  error：当前的算法是固定了参数的位置，如果改变参数的次序，则无法解析。
                //  fix: 把所有参数分解为key和value，放入map中，key变为小写，然后再判断参数是否存在做相应处理。
                HashMap map=new HashMap();
                for(int i=4;i<str_arrays.length;i++){
                	int sp=str_arrays[i].indexOf("=");
                	String key=str_arrays[i].substring(0,sp-1);
                	String value=str_arrays[i].substring(sp+1);
                	map.put(key.trim().toLowerCase(), value);                	
                }
                
                str_datasourcename=(map.containsKey("ds"))?((String)map.get("ds")):(NovaClientEnvironment.getInstance().getDefaultDatasourceName());
                str_loadall=(map.containsKey("loadall"))?((String)map.get("loadall")):"true";
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if (str_type.equalsIgnoreCase("CUST")) {
            str_realsql = _ref; //表型
        } else {
        	
        }
        //类型、检索sql、父字段、主键、检索子表、子表外键、数据源、是否全部加载、参照前置校验公式
        return new String[] {str_type, str_realsql, str_parentfieldname, str_pkfieldname,str_table, str_table_fk, str_datasourcename, str_loadall, str_refvalid}; //
    }
    private String isLoadAll(String _des)
    {
    	String str_new = _des;
        int li_pos = str_new.indexOf("="); //
        str_new = str_new.substring(li_pos + 1, str_new.length()).trim();
        return str_new;//取得是否全部加载参数
    }
    private String getDataSourceName(String _des) {
        String str_new = _des;
        int li_pos = str_new.indexOf("="); //
        str_new = str_new.substring(li_pos + 1, str_new.length()).trim();

        if (str_new.startsWith("\"") || str_new.startsWith("'")) {
            str_new = str_new.substring(1, str_new.length());
        }

        if (str_new.endsWith("\"") || str_new.endsWith("'")) {
            str_new = str_new.substring(0, str_new.length() - 1);
        }

        return str_new; //取得数据源名称
    }

    private String[] getTreeFielNames(String _des) {
        int li_pos = _des.indexOf("="); //
        String str_parentfieldname = _des.substring(0, li_pos); // ParentFieldID
        String str_pkfieldname = _des.substring(li_pos + 1, _des.length()); // PKFieldID

        str_parentfieldname = str_parentfieldname.trim(); // 截去空格
        str_pkfieldname = str_pkfieldname.trim(); // 截去空格
        return new String[] {str_parentfieldname, str_pkfieldname};
    }

    /**
     * 转换一条SQL,替换其中的{}为'',然后强行加上1=2,以使快速转出其中的结构!!
     * @param _sql
     * @return
     */
    public String convertSQLToNullRecordSQL(String _sql) {
        return null;
    }

    public String complieWhereConditionOnSQL(String _sql, String _pkname, String _pkvalue) {
        String str_sql_1 = _sql;
        String str_sql_2 = _sql.toUpperCase(); //

        int li_pos_where = str_sql_2.lastIndexOf("WHERE"); //
        int li_pos_order = str_sql_2.lastIndexOf("ORDER"); //

        if (li_pos_where > 0) { //如果有Where条件
            if (li_pos_order > 0) { //如果有order
                String str_prefix = str_sql_1.substring(0, li_pos_order); //前辍
                String str_subfix = str_sql_1.substring(li_pos_order, str_sql_1.length()); //后辍
                return str_prefix + " AND " + _pkname + "='" + _pkvalue + "' " + str_subfix; //
            } else {
                return str_sql_1 + " AND " + _pkname + "='" + _pkvalue + "'";
            }
        } else { //如果没有where条件
            if (li_pos_order > 0) { //如果有order
                String str_prefix = str_sql_1.substring(0, li_pos_order); //前辍
                String str_subfix = str_sql_1.substring(li_pos_order, str_sql_1.length()); //后辍
                return str_prefix + " WHERE " + _pkname + "='" + _pkvalue + "' " + str_subfix; //
            } else {
                return str_sql_1 + " WHERE " + _pkname + "='" + _pkvalue + "'";
            }
        }
    }

    /**
     * 分析一条SQL,找出其中第一列的名称!
     * @param _sql
     * @return
     */
    public String findSQLFirstName(String _sql) {
        if (_sql == null) {
            return null;
        }

        try {
            String str_sql = _sql.trim().toUpperCase(); //
            int li_pos = str_sql.indexOf(","); //找出第一个
            str_sql = str_sql.substring(6, li_pos).trim();
            //modify by zhangzz 20120418 begin
            /**
             * TODO 获取第一列时，如果是select distinct columnA aliasA,ColumnB from dual这种情况
             * 将会出现截取的是distinct关键字
             * 判断第一列这里是否为3个字符串，如果是的话，则默认取中间的为第一列
             * 如果是2个字符串，则默认为第一个为第一列
             * 这里可能出现select distinct columnA,ColumnB from dual这种情况，所以写参照的时候
             * 如果有distinct之类的关键字，请在第一列中添加别名
             */
//            int li_pos_2 = str_sql.indexOf(" ");
//            if (li_pos_2 > 0) {
//                str_sql = str_sql.substring(0, li_pos_2).trim();
//            } else { //
//            }
            String[] firstColumnSplitArr = str_sql.split(" ");
            if(firstColumnSplitArr.length == 3)
            	str_sql = firstColumnSplitArr[1];
            else
            	str_sql = firstColumnSplitArr[0];
            
            //modify by zhangzz 20120418 end
            return str_sql;
        } catch (Exception ex) {
            System.out.println("找出参照SQL[" + _sql + "]的第一列名失败!!");
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * 将一个SQL中有{}的地方替换成_env中的值或_,map中的值!!!
     * @param _inittext
     * @param _env
     * @param _map
     * @return
     * @throws Exception
     */
    public String convertMacroStr(String _inittext, NovaClientEnvironment _env, HashMap _map) throws Exception {
        String temp_sql = _inittext;

        int _begin = temp_sql.indexOf("{");
        if (_begin < 0) {
            if (temp_sql.indexOf("}") >= 0) {
                throw new Exception("SQL语句错误，{}不匹配！");
            }
            return temp_sql;
        }

        String begin_sql = temp_sql.substring(0, _begin);
        String mid_sql = temp_sql.substring(_begin + 1, temp_sql.length());

        int end_index = mid_sql.indexOf("}");
        if (end_index >= 0) {
            end_index = mid_sql.indexOf("}");
            int begin_index = mid_sql.indexOf("{");
            if (begin_index >= 0 && begin_index < end_index) {
                mid_sql = convert(mid_sql, begin_index, end_index, _env, _map); //处理内嵌"{}"
            }
        } else {
            throw new Exception("SQL语句错误，{}不匹配！");
        }

        end_index = mid_sql.indexOf("}");

        String bracket_id = mid_sql.substring(0, end_index); //这是{}中间的内容
        int li_pos = bracket_id.indexOf("."); //
        String str_itemkey = null;
        String str_itemkey_otherfield = null;
        if (li_pos > 0) { //如果发现有个"."
            str_itemkey = bracket_id.substring(0, li_pos);
            str_itemkey_otherfield = bracket_id.substring(li_pos + 1, bracket_id.length()); //
        } else {
            str_itemkey = bracket_id;
        }

        String _bracket_parameter = getParameter(str_itemkey, str_itemkey_otherfield, _env, _map);
        String end_sql = "";
        if (end_index + 1 < mid_sql.length()) {
            end_sql = convertMacroStr(mid_sql.substring(end_index + 1, mid_sql.length()), _env, _map);
        } else {
            end_sql = "";
        }
        
        //James.W 增加处理。
        // Han Weili: 平台的加载公式结果为空的时候显示'null'的问题
        if (_bracket_parameter == null)
            _bracket_parameter = "";

        return begin_sql + _bracket_parameter + end_sql;
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
            str_remain = str_remain.substring(li_pos + old_item.length(), str_remain.length());
        }
        str_return = str_return + str_remain; // 将剩余的加上
        return str_return;
    }

    public static void main(String[] argv) {

        //		String text2 = "DD + AA{({BB},CC)} + {EE}";
        //		Vector vec_replace2 = new Vector();
        //		vec_replace2.add("***");
        //		vec_replace2.add("+++");
        //		vec_replace2.add("---");
        //		vec_replace2.add("aaa");
        //		vec_replace2.add("bbb");
        //		vec_replace2.add("ccc");
        //
        //		Vector vec2 = new Vector();
        //		try {
        //			vec2 = new ModifySqlUtil().findItemKey(text2);
        //		} catch (Exception e) {
        //			// TODO Auto-generated catch block
        //			e.printStackTrace();
        //		}
        //
        //		for (int i = 0; i < vec2.size(); i++) {
        //			System.out.println("\nThe item is:" + vec2.get(i));
        //		}
        //
        //		try {
        //			String str = new ModifySqlUtil().replace_ItemKey(text2, vec_replace2);
        //			System.out.println("\n\n\nThe new str is:" + str);
        //		} catch (Exception e) {
        //			// TODO Auto-generated catch block
        //			e.printStackTrace();
        //		}

        String str_sql = new ModifySqlUtil().findSQLFirstName(
            "select a.id 主键,code,namefrom n1_menu a,pub_menu b where 1=1 and a.id=12"); //

        System.out.println(str_sql); //
    }
}

/**************************************************************************
 * $RCSfile: ModifySqlUtil.java,v $  $Revision: 1.5.2.5 $  $Date: 2010/01/20 09:52:09 $
 *
 * $Log: ModifySqlUtil.java,v $
 * Revision 1.5.2.5  2010/01/20 09:52:09  wangqi
 * *** empty log message ***
 *
 * Revision 1.5.2.4  2009/02/05 12:02:43  wangqi
 * *** empty log message ***
 *
 * Revision 1.5.2.3  2008/09/18 06:49:42  wangqi
 * *** empty log message ***
 *
 * Revision 1.5.2.2  2008/09/18 04:51:42  wangqi
 * *** empty log message ***
 *
 * Revision 1.5.2.1  2008/08/28 09:15:43  wangqi
 * *** empty log message ***
 *
 * Revision 1.5  2008/01/15 09:04:08  wangqi
 * *** empty log message ***
 *
 * Revision 1.4  2007/10/15 03:41:09  wangqi
 * *** empty log message ***
 *
 * Revision 1.3  2007/07/11 11:11:33  sunxf
 * 增加MutiTree
 *
 * Revision 1.2  2007/05/31 07:38:22  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 05:56:47  qilin
 * no message
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