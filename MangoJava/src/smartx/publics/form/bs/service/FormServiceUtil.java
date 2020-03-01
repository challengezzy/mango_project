package smartx.publics.form.bs.service;

import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import smartx.framework.common.bs.CommDMO;
import smartx.framework.common.ui.NovaClientEnvironment;
import smartx.framework.common.vo.FrameWorkTBUtil;
import smartx.framework.common.vo.NovaLogger;
import smartx.framework.metadata.vo.Pub_Templet_1VO;

/**
 *@author zzy
 *@date Aug 16, 2011
 **/
public class FormServiceUtil {
	
	private static Logger logger = NovaLogger.getLogger(FormServiceUtil.class);
	
	
	/**
	 * 拼装原数据模板的SQL,不拼装排序语句,为计算数据准备
	 * @param templetVO
	 * @param _condition
	 * @param nce
	 * @return
	 * @throws Exception
	 */
	public static String getSQL4Count(Pub_Templet_1VO templetVO, String _condition,NovaClientEnvironment nce) throws Exception{
		if (_condition != null && _condition != "") {
			_condition = _condition.trim();

			// 以下是判断是否具有条件和排序子句（两个&之间），然后把条件重整了一下
			// 算法比较麻烦，应该重构去掉，传入的参数应该分别控制条件和排序
			int pos1 = _condition.indexOf("&");
			if (pos1 != -1) {
				String condition_split = _condition.substring(pos1 + 1);
				int pos2 = condition_split.indexOf("&");// if pos2-1>pos1+1
				pos2 = pos1 + 1 + pos2;
				String order = _condition.substring(pos1 + 1, pos2);
				String condition_split_2 = _condition.substring(pos2 + 1);
				int pos_and = condition_split_2.indexOf("and");
				pos_and = pos2 + 1 + pos_and;

				if (_condition.startsWith("&")) {
					String condition_head = _condition.substring(pos_and + 3);
					_condition = condition_head + " " + order;
				} else {
					String condition_head = _condition.substring(0, pos1);
					String condition_tail = _condition.substring(pos_and - 1);
					_condition = condition_head + condition_tail + " " + order;
				}
			}
		}

		String str_constraintFilterCondition = getDataconstraint(templetVO,nce);
		String str_return = null; //
		if (str_constraintFilterCondition == null) {
			if (_condition == null) {
				str_return = "select 1 from " + templetVO.getTablename();
			} else {
				str_return = "select 1 from " + templetVO.getTablename()
						+ " where " + _condition; // 把RowID加上!!
			}
		} else {
			if (_condition == null) {
				str_return = "select 1 from " + templetVO.getTablename()
						+ " where (" + str_constraintFilterCondition + ")";
			} else {
				str_return = "select 1 from " + templetVO.getTablename()
						+ " where (" + str_constraintFilterCondition
						+ ") and (" + _condition + ")"; // 把RowID加上!!
			}
		}
		
		return str_return;
	} 
	
	/**
	 * 拼装原数据模板的SQL
	 * @param templetVO
	 * @param _condition
	 * @param nce
	 * @return
	 * @throws Exception
	 */
	public static String getSQL(Pub_Templet_1VO templetVO, String _condition,NovaClientEnvironment nce) throws Exception{
		if (_condition != null && _condition != "") {
			_condition = _condition.trim();

			// 以下是判断是否具有条件和排序子句（两个&之间），然后把条件重整了一下
			// 算法比较麻烦，应该重构去掉，传入的参数应该分别控制条件和排序
			int pos1 = _condition.indexOf("&");
			if (pos1 != -1) {
				String condition_split = _condition.substring(pos1 + 1);
				int pos2 = condition_split.indexOf("&");// if pos2-1>pos1+1
				pos2 = pos1 + 1 + pos2;
				String order = _condition.substring(pos1 + 1, pos2);
				String condition_split_2 = _condition.substring(pos2 + 1);
				int pos_and = condition_split_2.indexOf("and");
				pos_and = pos2 + 1 + pos_and;

				if (_condition.startsWith("&")) {
					String condition_head = _condition.substring(pos_and + 3);
					_condition = condition_head + " " + order;
				} else {
					String condition_head = _condition.substring(0, pos1);
					String condition_tail = _condition.substring(pos_and - 1);
					_condition = condition_head + condition_tail + " " + order;
				}
			}
		}

		String str_constraintFilterCondition = getDataconstraint(templetVO,nce);
		String str_return = null; //
		if (str_constraintFilterCondition == null) {
			if (_condition == null) {
				str_return = "select * from " + templetVO.getTablename();
			} else {
				str_return = "select * from " + templetVO.getTablename()
						+ " where " + _condition; // 把RowID加上!!
			}
		} else {
			if (_condition == null) {
				str_return = "select * from " + templetVO.getTablename()
						+ " where (" + str_constraintFilterCondition + ")";
			} else {
				str_return = "select * from " + templetVO.getTablename()
						+ " where (" + str_constraintFilterCondition
						+ ") and (" + _condition + ")"; // 把RowID加上!!
			}
		}
		
    	String ordersetting =	templetVO.getOrdersetting();
    	ordersetting = (ordersetting==null || "".equals(ordersetting))?"":(" order by "+ordersetting);
    	
    	str_return+=ordersetting;

		return str_return;
	}
	
	/**
	 * 取得templetVO数据过滤条件
	 * @param templetVO
	 * @param nce
	 * @return
	 * @throws Exception
	 */
	public static String getDataconstraint(Pub_Templet_1VO templetVO,NovaClientEnvironment nce) throws Exception{
		CommDMO dmo = new CommDMO();
		try {
			if (templetVO.getDataconstraint() == null
					|| templetVO.getDataconstraint().trim().equals("null")
					|| templetVO.getDataconstraint().trim().equals("")) {
				return null; // 默认数据源
			} else {
				if(nce != null)
					return new FrameWorkTBUtil().convertExpression(nce, templetVO.getDataconstraint()); // 算出数据源!!
				else
					return new FrameWorkTBUtil().convertExpression(NovaClientEnvironment.getInstance(), templetVO.getDataconstraint()); 

			}
		} catch(Exception e){
			logger.error("取得templetVO数据过滤条件",e);
			throw e;
		}finally {
			dmo.releaseContext();
		}
	}
	
	/**
	 * 根据MAP构建NovaClientEnvironment
	 * @param map
	 * @return
	 */
	public static NovaClientEnvironment getNewNovaClientEnviorment(Map<String,Object> map){
		NovaClientEnvironment nce = new NovaClientEnvironment();
		try{
			Set<String> keys = map.keySet();
			for(String key : keys){
				nce.put(key, map.get(key));
			}
			
		}catch(Exception e){
			logger.debug("",e);
		}
		return nce;
	}
	
	/**
	 * 根据数据类型与长度自己算出默认的控件类型
	 * @param par_columnname
	 * @param par_datatype
	 * @param par_length
	 * @return
	 */
	public static String f_gettype(String par_columnname, String par_datatype,
			long par_length) {
		if (par_datatype.equals("VARCHAR2"))
			return "文本框";
		else if (par_datatype.equals("NUMBER")) {
			return "数字框";
		} else if (par_datatype.equals("DATE"))
			return "时间";
		else if (par_datatype.equals("CHAR")) {
			if (par_length == 19)
				return "时间";
			else if (par_length == 10)
				return "日历";
			else if (par_length == 1)
				return "勾选框";
		}

		return "文本框";
	}

}
