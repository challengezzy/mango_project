package smartx.publics.form.bs.service;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import smartx.framework.common.bs.CommDMO;
import smartx.framework.common.vo.HashVO;
import smartx.framework.common.vo.NovaLogger;

public class TempleteCopyService {
	
	private Logger logger = NovaLogger.getLogger(this.getClass());
	
	public void copyTempleteByCode(String templeteCode,String newName,String newCode) throws Exception{
		
		CommDMO dmo = new CommDMO();
		
		try{
			
		
			String newId = dmo.getSequenceNextValByDS(null,"s_pub_templet_1");
			
			//复制模板信息
			HashVO[] templeteVOS = dmo.getHashVoArrayByDS(null, "select * from pub_templet_1 where templetcode=?", templeteCode);
			if( templeteVOS != null && templeteVOS.length >0 ){
				HashVO vo = templeteVOS[0];
				String[] tempKeys = vo.getKeys();
				StringBuffer tempFields = new StringBuffer("");
				StringBuffer tempValues = new StringBuffer("");//这里面就是一些问号
				Object[] parameters = new Object[tempKeys.length];//这里面才是真正的参数值
				int index = 0 ;
				for(String key : tempKeys){
					if(key.toLowerCase().equals("pk_pub_templet_1")){
						if(tempFields.toString().equals("")){
							tempFields.append(key);
							tempValues.append("?");
							parameters[index] = newId;
						}else{
							tempFields.append(","+key);
							tempValues.append(",?");
							parameters[index] = newId;
						}
					}else if(key.toLowerCase().equals("templetcode")){
						if(tempFields.toString().equals("")){
							tempFields.append(key);
							tempValues.append("?");
							parameters[index] = newCode;
						}else{
							tempFields.append(","+key);
							tempValues.append(",?");
							parameters[index] = newCode;
						}
					}else if(key.toLowerCase().equals("templetname")){
						if(tempFields.toString().equals("")){
							tempFields.append(key);
							tempValues.append("?");
							parameters[index] = newName;
						}else{
							tempFields.append(","+key);
							tempValues.append(",?");
							parameters[index] = newName;
						}
					}else{
						if(tempFields.toString().equals("")){
							tempFields.append(key);
							tempValues.append("?");
							parameters[index] = vo.getStringValue(key);
						}else{
							tempFields.append(","+key);
							tempValues.append(",?");
							parameters[index] = getColumnValue(key,vo);
						}
					}
					index++;
				}
				
				String sql = "insert into pub_templet_1("+tempFields.toString()+") values ("+tempValues.toString()+")";
				dmo.executeUpdateByDS(null, sql, parameters);
				dmo.commit(null);
	//			listSqls.add(sql);
				
				//复制组信息
				HashVO[] groupVOS = dmo.getHashVoArrayByDS(null,"select * from pub_templet_1_item_group where templetid=?",vo.getIntegerValue("pk_pub_templet_1"));
				if(groupVOS != null && groupVOS.length >0 ){
					for(HashVO groupItem : groupVOS){
						String newGroupId = dmo.getSequenceNextValByDS(null,"s_pub_templet_1_item_group");
						StringBuffer fields = new StringBuffer("");
						StringBuffer values = new StringBuffer("");
						String[] keys = groupItem.getKeys();
						Object[] groupPara = new Object[keys.length];
						int groupIndex = 0;
						for(String key : keys){
							if(key.toLowerCase().equals("id")){
								if(fields.toString().equals("")){
									fields.append(key);
									values.append("?");
								}else{
									fields.append(","+key);
									values.append(",?");
								}
								groupPara[groupIndex] = newGroupId;
							}else if(key.toLowerCase().equals("templetid")){
								if(fields.toString().equals("")){
									fields.append(key);
									values.append("?");
								}else{
									fields.append(","+key);
									values.append(",?");
								}
								groupPara[groupIndex] = newId;
							}else{
								if(fields.toString().equals("")){
									fields.append(key);
									values.append("?");
								}else{
									fields.append(","+key);
									values.append(",?");
								}
								groupPara[groupIndex] = getColumnValue(key,groupItem);
							}
							groupIndex++;
						}
						String insertSQL = "insert into pub_templet_1_item_group("+fields.toString()+") values ("+values.toString()+")";
						dmo.executeUpdateByDS(null, insertSQL, groupPara);
						dmo.commit(null);
	//					listSqls.add(insertSQL);
						
						//复制被分组的模板子项
						
						HashVO[] templeteItemsVOS = dmo.getHashVoArrayByDS(null, "select * from pub_templet_1_item t where t.templetitemgroupid=? and t.pk_pub_templet_1 = ?",groupItem.getIntegerValue("id"), vo.getIntegerValue("pk_pub_templet_1"));
						if(templeteItemsVOS != null && templeteItemsVOS.length >0 ){
							for(HashVO item : templeteItemsVOS){
								String itemId = dmo.getSequenceNextValByDS(null,"s_pub_templet_1_item");
								StringBuffer itemFields = new StringBuffer("");
								StringBuffer itemValues = new StringBuffer("");
								String[] itemKeys = item.getKeys();
								Object[] itemsPara = new Object[itemKeys.length];
								int itemIndex = 0;
								for(String key : itemKeys){
									if(key.toLowerCase().equals("pk_pub_templet_1_item")){
										if(itemFields.toString().equals("")){
											itemFields.append(key);
											itemValues.append("?");
										}else{
											itemFields.append(","+key);
											itemValues.append(",?");
										}
										itemsPara[itemIndex] = itemId;
									}else if(key.toLowerCase().equals("pk_pub_templet_1")){
										if(itemFields.toString().equals("")){
											itemFields.append(key);
											itemValues.append("?");
										}else{
											itemFields.append(","+key);
											itemValues.append(",?");
										}
										itemsPara[itemIndex] = newId;
									}else if(key.toLowerCase().equals("templetitemgroupid")){
										if(itemFields.toString().equals("")){
											itemFields.append(key);
											itemValues.append("?");
										}else{
											itemFields.append(","+key);
											itemValues.append(",?");
										}
										itemsPara[itemIndex] = newGroupId;
									}else{
										if(itemFields.toString().equals("")){
											itemFields.append(key);
											itemValues.append("?");
										}else{
											itemFields.append(","+key);
											itemValues.append(",?");
										}
										itemsPara[itemIndex] = getColumnValue(key,item);
									}
									itemIndex++;
								}
								String insert = "insert into pub_templet_1_item("+itemFields.toString()+") values ("+itemValues.toString()+")";
								dmo.executeUpdateByDS(null, insert, itemsPara);
							}
						}
						
					}
				}
				
				//复制没有分组的模板子项
				HashVO[] templeteItemsVOS = dmo.getHashVoArrayByDS(null, "select * from pub_templet_1_item t where t.templetitemgroupid is null and t.pk_pub_templet_1 = ?", vo.getIntegerValue("pk_pub_templet_1"));
				if(templeteItemsVOS != null && templeteItemsVOS.length >0 ){
					for(HashVO itemVO : templeteItemsVOS){
						String noGroupItemId = dmo.getSequenceNextValByDS(null,"s_pub_templet_1_item");
						StringBuffer fields = new StringBuffer("");
						StringBuffer values = new StringBuffer("");
						String[] keys = itemVO.getKeys();
						Object[] noGroupItemPara = new Object[keys.length];
						int noGroupItemIndex = 0;
						for(String key : keys){
							if(key.toLowerCase().equals("pk_pub_templet_1_item")){
								if(fields.toString().equals("")){
									fields.append(key);
									values.append("?");
								}else{
									fields.append(","+key);
									values.append(",?");
								}
								noGroupItemPara[noGroupItemIndex] = noGroupItemId;
							}else if(key.toLowerCase().equals("pk_pub_templet_1")){
								if(fields.toString().equals("")){
									fields.append(key);
									values.append("?");
								}else{
									fields.append(","+key);
									values.append(",?");
								}
								noGroupItemPara[noGroupItemIndex] = newId;
							}else{
								if(fields.toString().equals("")){
									fields.append(key);
									values.append("?");
								}else{
									fields.append(","+key);
									values.append(",?");
								}
								noGroupItemPara[noGroupItemIndex] = getColumnValue(key,itemVO);
							}
							noGroupItemIndex ++;
						}
						String insertSQL = "insert into pub_templet_1_item("+fields.toString()+") values ("+values.toString()+")";
						dmo.executeUpdateByDS(null, insertSQL, noGroupItemPara);
						dmo.commit(null);
	//					listSqls.add(insertSQL);
					}
				}
			}
		}catch(Exception e){
			dmo.rollback(null);
			throw e;
		}finally{
			dmo.releaseContext(null);
		}
	}
	
	private Object getColumnValue(String key,HashVO vo){
		int type = vo.getColumnType(key);
        Object value = null;
        if (type == Types.VARCHAR || type == Types.CLOB) {
        	value = vo.getStringValue(key);
        } else if (type == Types.NUMERIC || type == Types.SMALLINT) {
        	value = vo.getIntegerValue(key);
        } else if (type == Types.DATE || type == Types.TIMESTAMP) {
        	value = vo.getTimeStampValue(key);
        } else if (type == Types.DECIMAL || type == Types.DOUBLE || type == Types.FLOAT) {
        	value = vo.getDoubleValue(key);
        } else {
            value = vo.getObjectValue(key);
        }
		return value;
	}
}
