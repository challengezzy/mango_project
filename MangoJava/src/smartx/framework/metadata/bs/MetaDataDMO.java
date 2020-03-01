package smartx.framework.metadata.bs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import smartx.framework.common.bs.AbstractDMO;
import smartx.framework.common.bs.CommDMO;
import smartx.framework.common.ui.NovaClientEnvironment;
import smartx.framework.common.ui.NovaConstants;
import smartx.framework.common.utils.StringUtil;
import smartx.framework.common.vo.FrameWorkTBUtil;
import smartx.framework.common.vo.HashVO;
import smartx.framework.common.vo.ModifySqlUtil;
import smartx.framework.common.vo.NovaLogger;
import smartx.framework.metadata.vo.BillVO;
import smartx.framework.metadata.vo.ComBoxItemVO;
import smartx.framework.metadata.vo.Pub_QueryTempletVO;
import smartx.framework.metadata.vo.Pub_QueryTemplet_ItemVO;
import smartx.framework.metadata.vo.Pub_Templet_1VO;
import smartx.framework.metadata.vo.Pub_Templet_1_ItemGroupVO;
import smartx.framework.metadata.vo.Pub_Templet_1_ItemVO;
import smartx.framework.metadata.vo.RefItemVO;
import smartx.framework.metadata.vo.RowNumberItemVO;
import smartx.framework.metadata.vo.TableDataStruct;
import smartx.framework.metadata.vo.jepfunctions.JepFormulaParse;

public class MetaDataDMO extends AbstractDMO {

	private Logger logger=NovaLogger.getLogger(MetaDataDMO.class);
	
    private JepFormulaParse jepParse = null; //公式解析器!!

    private ModifySqlUtil modifySqlUtil = null; //转换工具!!

    private HashMap refCache = null; //参照缓存!!

    private HashMap mapLoadFormulaCache = null; //加载公式缓存!!
    
    private static HashMap<String,Pub_Templet_1VO> templetCache = new HashMap<String,Pub_Templet_1VO>();
    
    /**
     * 刷新元原模板缓存
     * @param templetCode
     */
    public void refreshTempletCacheByCode(String templetCode){
    	templetCache.remove(templetCode);
    }
    
    /**
     * 刷新元原模板缓存（实体编辑模板）
     * @param entityModelCode
     */
    public void refreshTempletCacheByEntityModelCode(String entityModelCode){
    	for(Entry<String,Pub_Templet_1VO> entry : templetCache.entrySet()){
    		String key = entry.getKey();
    		if(key.startsWith("[ENTITY]")){
	    		String[] codes = key.split("//");
	    		if(codes[0].indexOf(entityModelCode) >= 0){
	    			templetCache.remove(key);
	    			break;
	    		}
    		}
    	}
    }
    
    /**
     * 清空元原模板缓存
     */
    public void refreshAllTempletCache(){
    	templetCache.clear();
    }

    /**
     * 从数据库中根据模板编码创建模板VO
     *
     * @param _code 如果是领域实体编辑模板CODE类型为（[ENTITY]//领域实体编码//实体编码//实体编辑模板编码）
     * @return
     * @throws Exception
     */
    public Pub_Templet_1VO getPub_Templet_1VO(String _code, NovaClientEnvironment _clientEnv) throws Exception {
    	if(templetCache.containsKey(_code)){
    		NovaLogger.getLogger(this.getClass()).debug("从缓存读取元原模板["+_code+"]");
    		return templetCache.get(_code);
    		
    	}
    	Pub_Templet_1VO vo = null;
    	CommDMO commDMO = new CommDMO();
    	HashVO[] vos = null;
    	if(_code.startsWith("[ENTITY]")){//实体模板编辑
    		String[] code = _code.split("//");
    		//实体领域模型CODE
    		String entityModelCode = code[0].replace("[ENTITY]", "");
    		//实体编码
    		String entityCode = code[1];
    		//编辑方案编码
    		String editorCode = code[2];
    		String sql = "select v.content from v_bam_entitymodel v where v.code = ?";
    		vos = commDMO.getHashVoArrayByDS(null, sql,entityModelCode);
    		if(vos == null || vos.length <= 0)
    			throw new Exception("编码为[" + entityModelCode + "]的领域实体模型不存在!!");
    		HashVO entityModelVo = vos[0];
    		String content = entityModelVo.getStringValue("content");
    		Document doc = DocumentHelper.parseText(content);
    		Element root = doc.getRootElement();
    		Element entity = null;
    		for(Object entityObj : root.element("entities").elements("entity")){
    			entity = (Element)entityObj;
    			if(entityCode.equals(entity.attributeValue("code")))
    				break;
    		}
    		String datasourceName = root.elementText("datasource");
    		vo = getEntityEditor_Pub_Templet_1VO(entity,root,_code,editorCode,datasourceName,_clientEnv);
    	}else{
	        String str_sql = "select * from pub_templet_1 where lower(templetcode)='" + ("" + _code).toLowerCase() + "'";
	        vos = commDMO.getHashVoArrayByDS(null, str_sql);
	        if (vos == null || vos.length <= 0) {
	            throw new Exception("编码为[" + _code + "]的模板不存在!!");
	        }
	        HashVO parentVO = vos[0]; //
	
	        String str_sql_item = "select * from pub_templet_1_item where pk_pub_templet_1='" +
	            parentVO.getStringValue("Pk_pub_templet_1") + "' order by showorder asc";
	        HashVO[] hashVOs_item = commDMO.getHashVoArrayByDS(null, str_sql_item);
	        
	        String str_sql_group = "select * from pub_templet_1_item_group where " +
	        		"templetid='"+parentVO.getStringValue("Pk_pub_templet_1")+"' order by seq asc";
	        HashVO[] hashVOs_group = commDMO.getHashVoArrayByDS(null, str_sql_group);
	        vo = getPub_Templet_1VO(parentVO, hashVOs_item, hashVOs_group,_clientEnv);
    	}
         
        templetCache.put(_code, vo);
        return vo;
    }
    
    /**
     * 直接根据实体创建模板VO
     *
     * @param entity 实体XML
     * @param content 领域实体的XML
     * @param datasourceName 数据源
     * @param templetCode 模板编码
     * @param editorCode 编辑方案编码
     * @param _clientEnv
     * @return
     * @throws Exception
     */

    public Pub_Templet_1VO getEntityEditor_Pub_Templet_1VO(Element entity,Element content,String templetCode,String editorCode,
    		String datasourceName,NovaClientEnvironment _clientEnv) throws
	    Exception {
    	CommDMO commDMO = new CommDMO();
    	Pub_Templet_1VO templet1VO = new Pub_Templet_1VO();
    	//处理模板表
    	templet1VO.setId("-1");
    	templet1VO.setTempletcode(templetCode);
    	for(Object editorObj : entity.element("editors").elements("editor")){
    		Element editor = (Element)editorObj;
    		if(editor.attributeValue("code").equals(editorCode)){
    			templet1VO.setTempletname(editor.attributeValue("name"));
    			templet1VO.setPksequencename(editor.attributeValue("pkSequence"));
    			templet1VO.setSavedtablename(editor.attributeValue("saveTable"));
    			templet1VO.setPkname(editor.attributeValue("pkName").toUpperCase());
    			break;
    		}
    	}
    	String mappingInfoType = entity.element("mappingInfo").attributeValue("type");
    	if("table".equals(mappingInfoType))//关系表
    		templet1VO.setTablename(entity.element("mappingInfo").elementText("tableName"));
    	else if("queryView".equals(mappingInfoType)) //查询视图
    		templet1VO.setTablename(entity.element("mappingInfo").elementText("queryView"));
    	
    	templet1VO.setDatasourcename(datasourceName);
    	templet1VO.setDataconstraint(entity.element("mappingInfo").elementText("filter"));
    	
    	
    	//实体领域模型中的元原模板只用于编辑使用，所以这里的检索表可以不用
	    if (templet1VO.getTablename() != null && !templet1VO.getTablename().trim().equals("")) {
	        String str_sql_getViewColumns = "select * from " + templet1VO.getTablename() + " where 1=2"; //
	        try {
	            TableDataStruct strct_viewtable = commDMO.getTableDataStructByDS(datasourceName,str_sql_getViewColumns);
	            if (strct_viewtable != null) {
	                templet1VO.setRealViewColumns(strct_viewtable.getTable_header()); // 查询数据的视图中的所有列
	            }
	        } catch (Exception ex) {
	            System.out.println("模板定义中的取数表[" + templet1VO.getTablename() + "]配置不对!!");
	            ex.printStackTrace();
	        }
	    }
	
	    //下面代码步骤的处理是从【保存表】获得检索出的字段列表
	    HashMap<String,String> map_savedtableColDataTypes = new HashMap<String,String>(); // 保存到表中的
	    if (templet1VO.getSavedtablename() != null && !templet1VO.getSavedtablename().trim().equals("")) {
	        String str_sql_savetable = "select * from " + templet1VO.getSavedtablename() + " where 1=2";
	        try {
	            TableDataStruct strct_savetable = commDMO.getTableDataStructByDS(datasourceName,str_sql_savetable);
	            if (strct_savetable != null) {
	                templet1VO.setRealSavedTableHaveColumns(strct_savetable.getTable_header()); // 保存的表中的所有列名
	                for (int i = 0; i < strct_savetable.getTable_header().length; i++) {
	                    map_savedtableColDataTypes.put(strct_savetable.getTable_header()[i].toUpperCase(),
	                        strct_savetable.getTable_body_type()[i].toUpperCase()); //
	                }
	            }
	        } catch (Exception ex) {
	            System.out.println("模板定义中的保存表[" + templet1VO.getTablename() + "]配置不对!!");
	            ex.printStackTrace();
	        }
	    }
	    
	    Map<String,String> mappingInfoMap = new HashMap<String, String>();
	    for(Object mapObj : entity.element("mappingInfo").element("attributeMapping").elements("map")){
	    	Element map = (Element)mapObj;
	    	mappingInfoMap.put(map.attributeValue("attributeName"), map.attributeValue("columnName"));
	    }
	    
	    Map<String,String> displayAttributeMap = new HashMap<String, String>();
	    for(Object displayAttObj : entity.element("attributes").element("displayAttributes").elements("attribute")){
	    	Element displayAtt = (Element)displayAttObj;
	    	displayAttributeMap.put(displayAtt.attributeValue("name"), displayAtt.attributeValue("isQuickQuery"));
	    }
	    
	    @SuppressWarnings("rawtypes")
		List attributes = entity.element("attributes").elements("attribute");
	    
	    Pub_Templet_1_ItemVO[] itemVOS = new Pub_Templet_1_ItemVO[attributes.size()];
	    for(int i=0;i<itemVOS.length;i++){
	    	Element attribute = (Element)attributes.get(i);
	    	
	        itemVOS[i] = new Pub_Templet_1_ItemVO();
	        itemVOS[i].setId("-1");
	        /*元原模板保存字段必须是数据库原始字段，所以ITEMKEY设置为属性相对应的关系表字段
	         *如果没有则说明是可计算属性，则不参与保存的，所以设置为属性名称
	         **/
	        itemVOS[i].setItemkey(mappingInfoMap.get(attribute.attributeValue("name"))==null?attribute.attributeValue("name").toUpperCase()
	        								:mappingInfoMap.get(attribute.attributeValue("name")).toUpperCase());
	        itemVOS[i].setItemname(attribute.attributeValue("label"));
	        itemVOS[i].setItemtype(getItemTypeByEntityAttribute(attribute));                 // 控件类型 
	        itemVOS[i].setComboxdesc("synref:".concat(attribute.attributeValue("dictionaryRefCode")==null?""
	        											:attribute.attributeValue("dictionaryRefCode")));    // 设置下拉框定义!!
	        itemVOS[i].setClientrefdesc("");
	        
	        if("true".equals(attribute.attributeValue("isCompute")))//可计算属性不参与保存
	        	itemVOS[i].setIssave(false);
	        else
	        	itemVOS[i].setIssave(true);            // 是否参与保存!（默认都参与保存）
	        
	        itemVOS[i].setDelfaultquerylevel("1"); // 是否默认查询条件 （默认都是快速查询）
	        itemVOS[i].setIsMustCondition(false);  // 作为默认查询条件时是否必输
	        
	        itemVOS[i].setDefaultCondition(""); // 作为默认查询条件时的默认值公式
	        itemVOS[i].setConditionItemType("");//条件控件类型
            itemVOS[i].setConditionComboxDesc("");//条件下拉设置
            itemVOS[i].setConditionRefDesc("");//条件参照设置
            itemVOS[i].setConditionShowOrder(1);//条件显示顺序
	        
	        itemVOS[i].setIsMustInput("true".equals(attribute.attributeValue("isNullable"))?false:true);          // 是否必输入项!
	
	        itemVOS[i].setLoadformula("");           // 加载公式
	        itemVOS[i].setDefaultvalueformula("");  //默认值公式
	        itemVOS[i].setEditformula("");           // 编辑公式
	        itemVOS[i].setColorformula("");         // 颜色公式
	        itemVOS[i].setShoworder(i);              // 显示顺序
	        itemVOS[i].setListwidth(150);              // 列表宽度
	        itemVOS[i].setCardwidth(150);              // 卡片宽度
	        
	        if(displayAttributeMap.containsKey(attribute.attributeValue("name"))){//是否为显示属性
		        itemVOS[i].setListisshowable(true);   // 列表是否显示
		        itemVOS[i].setCardisshowable(true);   // 卡片是否显示
	        }else{
	        	itemVOS[i].setListisshowable(false);   // 列表是否显示
			    itemVOS[i].setCardisshowable(false);   // 卡片是否显示
	        }
	        
	        if(attribute.attributeValue("name").equals(entity.attributeValue("idAttributeName"))){//如果是主键则不能编辑
		        itemVOS[i].setListiseditable("4");     // 列表是否可编辑
		        itemVOS[i].setCardiseditable("4");     // 卡片是否可编辑
	        }else{
	        	itemVOS[i].setListiseditable("1");     // 列表是否可编辑
		        itemVOS[i].setCardiseditable("1");     // 卡片是否可编辑
	        }
	        
	        itemVOS[i].setBcolorformula("");
	        itemVOS[i].setItemaction("");
	        
	        itemVOS[i].setSavedcolumndatatype( (String) map_savedtableColDataTypes.get(itemVOS[i].getItemkey().toUpperCase())); //保存时数据类型
	        
	        itemVOS[i].setExtattr01(mappingInfoMap.get(entity.attributeValue("idAttributeName"))==null?entity.attributeValue("idAttributeName").toUpperCase()
					:mappingInfoMap.get(entity.attributeValue("idAttributeName")).toUpperCase());
	        
	        itemVOS[i].setExtattr02(entity.attributeValue("name"));//实体名称
	                
	        // 如果下拉框中有内容则取出之,这里定义下拉框中必须是一条SQL
	        if ("下拉框".equals(itemVOS[i].getItemtype())){
            	resetComBoxItemvo(itemVOS[i],_clientEnv);
            }

            // 如果是参照  ：参照类型、最终检索sql、首列、数据源
            if ("参照".equals(itemVOS[i].getItemtype())) {
            	itemVOS[i].setRefdesc(getRefEntitySql(entity, content,attribute).concat("ds:").concat(datasourceName)); // 设置参照定义!!
            	buildRefDesc(templet1VO,itemVOS[i],_clientEnv);
            }
	
            templet1VO.getDirectItemVOs().add(itemVOS[i]);
            
            
	    }
	    templet1VO.setItemVos(itemVOS); // 为主VO设置各项
	    
	    Vector<String> v_savetableColumns = new Vector<String>();
	    for (int i = 0; i < itemVOS.length; i++) {
	        if (itemVOS[i].getIssave().booleanValue()) {
	            v_savetableColumns.add(itemVOS[i].getItemkey());
	        }
	    }
	    templet1VO.setRealSavedTableColumns( (String[]) v_savetableColumns.toArray(new String[0])); // 真正保存数据库存的列!!!做insert,update
	    // 拼SQL时就直接从这个数组转出来!!
	
	    // 为各项设置父VO的引用
	    for (int i = 0; i < itemVOS.length; i++) {
	        itemVOS[i].setPub_Templet_1VO(templet1VO);
	    }
    	
    	return templet1VO;
    }
    
    /**
     * 根据实体属性获取ITEMTYPE
     * @param attribute
     * @return
     */
    private String getItemTypeByEntityAttribute(Element attribute){
    	String category = attribute.attributeValue("category");
    	if("normal".equals(category)){//普通
    		String type = attribute.attributeValue("type");
    		if("VARCHAR2".equals(type))
    			return "文本框";
    		else if("DATE".equals(type))
    			return "时间";
    		else if("NUMBER".equals(type))
    			return "数字框";
    	}else if("dictionary".equals(category)){//字典引用
    		return "下拉框";
    	}else if("entity".equals(category)){//实体引用
    		return "参照";
    	}
    	return "";
    }

    /**
     * 直接根据两个HashVO创建模板VO
     *
     * @param _parentVO
     * @param _childVOs
     * @param groupVOs 
     * @return
     * @throws Exception
     */

    public Pub_Templet_1VO getPub_Templet_1VO(HashVO _parentVO, HashVO[] _childVOs, HashVO[] hashGroupVOs, NovaClientEnvironment _clientEnv) throws
	    Exception {
	    CommDMO commDMO = new CommDMO();
	    // 处理模板主表!!!!!!!!!!!!!
	    Pub_Templet_1VO templet1VO = new Pub_Templet_1VO(); //
	    templet1VO.setId(_parentVO.getStringValue("PK_PUB_TEMPLET_1"));
	    templet1VO.setTempletcode(_parentVO.getStringValue("Templetcode"));       // 模板编码
	    templet1VO.setTempletname(_parentVO.getStringValue("Templetname"));       // 模板名称
	    templet1VO.setTablename(_parentVO.getStringValue("Tablename"));           // 表名
	    templet1VO.setDatasourcename(_parentVO.getStringValue("Datasourcename")); // 数据源名称!!!!!!后增加的
	    templet1VO.setDataconstraint(_parentVO.getStringValue("Dataconstraint")); // 数据权限!!!!!!后增加的
	    templet1VO.setPkname(_parentVO.getStringValue("Pkname"));                 //主键名
	    templet1VO.setPksequencename(_parentVO.getStringValue("Pksequencename")); // 主键对应序列名
	    templet1VO.setOrdersetting(_parentVO.getStringValue("Ordersetting"));     // 模板排序设置
	    templet1VO.setSavedtablename(_parentVO.getStringValue("Savedtablename")); // 保存数据的表名!!
	    templet1VO.setCardcustpanel(_parentVO.getStringValue("Cardcustpanel"));   // 卡片自定义面板的按钮!!
	    templet1VO.setListcustpanel(_parentVO.getStringValue("Listcustpanel"));   // 列表的自定义面板!!!
	
	    //XXX 这里没有必要如此麻烦，首先设置该元原模板的时候已经设置了所在数据源，如果在大费周章的判断数据源名称不太值得，应该快速的直接获取，
	    //    而且如果两个数据源内有同名的表，那么系统是无法自动区分的，所以下面这段过程没有多大实际意义。
	    
	    //获得数据源 从客户端变量内获取合法的数据源， 
	    FrameWorkTBUtil tbUtil = new FrameWorkTBUtil(); 
	    String str_templeteDatasourceName = null;
	    if (templet1VO.getDatasourcename() != null && !templet1VO.getDatasourcename().trim().equals("")) {
	        str_templeteDatasourceName = tbUtil.convertExpression(_clientEnv, templet1VO.getDatasourcename()); //  还要转换一下!
	    }
	    
	    //下面代码步骤的处理是从【检索表】获得检索出的字段列表
	    if (templet1VO.getTablename() != null && !templet1VO.getTablename().trim().equals("")) {
	        String str_sql_getViewColumns = "select * from " + templet1VO.getTablename() + " where 1=2"; //
	        try {
	            TableDataStruct strct_viewtable = commDMO.getTableDataStructByDS(str_templeteDatasourceName,str_sql_getViewColumns);
	            if (strct_viewtable != null) {
	                templet1VO.setRealViewColumns(strct_viewtable.getTable_header()); // 查询数据的视图中的所有列
	            }
	        } catch (Exception ex) {
	            System.out.println("模板定义中的取数表[" + templet1VO.getTablename() + "]配置不对!!");
	            ex.printStackTrace();
	        }
	    }
	
	    //下面代码步骤的处理是从【保存表】获得检索出的字段列表
	    HashMap<String,String> map_savedtableColDataTypes = new HashMap<String,String>(); // 保存到表中的
	    if (templet1VO.getSavedtablename() != null && !templet1VO.getSavedtablename().trim().equals("")) {
	        String str_sql_savetable = "select * from " + templet1VO.getSavedtablename() + " where 1=2";
	        try {
	            TableDataStruct strct_savetable = commDMO.getTableDataStructByDS(str_templeteDatasourceName,str_sql_savetable);
	            if (strct_savetable != null) {
	                templet1VO.setRealSavedTableHaveColumns(strct_savetable.getTable_header()); // 保存的表中的所有列名
	                for (int i = 0; i < strct_savetable.getTable_header().length; i++) {
	                    map_savedtableColDataTypes.put(strct_savetable.getTable_header()[i].toUpperCase(),
	                        strct_savetable.getTable_body_type()[i].toUpperCase()); //
	                }
	            }
	        } catch (Exception ex) {
	            System.out.println("模板定义中的保存表[" + templet1VO.getTablename() + "]配置不对!!");
	            ex.printStackTrace();
	        }
	    }
	    
	    //构建模板组的数组
	    HashMap<String, Pub_Templet_1_ItemGroupVO> groupVOMap = new HashMap<String, Pub_Templet_1_ItemGroupVO>();
	    for(int i=0;i<hashGroupVOs.length;i++){
	    	HashVO hashGroupVO = hashGroupVOs[i];
	    	Pub_Templet_1_ItemGroupVO groupVO = new Pub_Templet_1_ItemGroupVO();
	    	groupVO.setId(hashGroupVO.getStringValue("id"));
	    	groupVO.setName(hashGroupVO.getStringValue("name"));
	    	groupVO.setIsExpand(hashGroupVO.getBooleanValue("ISEXPAND"));
	    	groupVO.setIsShow(hashGroupVO.getBooleanValue("ISSHOW"));
	    	groupVO.setSeq(hashGroupVO.getIntegerValue("seq"));
	    	groupVO.setParentTempletVO(templet1VO);
	    	templet1VO.getItemGroups().add(groupVO);
	    	groupVOMap.put(groupVO.getId(), groupVO);
	    }
	
	    //下面代码步骤的处理是从【模板子表】获得检索出的字段列表
	    Pub_Templet_1_ItemVO[] itemVOS = new Pub_Templet_1_ItemVO[_childVOs.length]; //
	    //Vector v_keys = new Vector();
	    for (int i = 0; i < itemVOS.length; i++) {
	        itemVOS[i] = new Pub_Templet_1_ItemVO();
	        itemVOS[i].setId(_childVOs[i].getStringValue("PK_PUB_TEMPLET_1_ITEM"));
	        itemVOS[i].setItemkey(_childVOs[i].getStringValue("Itemkey"));
	        itemVOS[i].setItemname(_childVOs[i].getStringValue("Itemname"));
	        itemVOS[i].setItemtype(_childVOs[i].getStringValue("Itemtype"));                 // 控件类型 
	        itemVOS[i].setComboxdesc(_childVOs[i].getStringValue("Comboxdesc"));             // 设置下拉框定义!!
	        itemVOS[i].setRefdesc(_childVOs[i].getStringValue("Refdesc"));                   // 设置参照定义!!
	        itemVOS[i].setClientrefdesc(_childVOs[i].getStringValue("ClientRefdesc"));
	                
	        itemVOS[i].setIssave(_childVOs[i].getBooleanValue("Issave"));                    // 是否参与保存!
	        itemVOS[i].setDelfaultquerylevel(_childVOs[i].getStringValue("Isdefaultquery")); // 是否默认查询条件!
	        itemVOS[i].setIsMustCondition(_childVOs[i].getBooleanValue("isMustCondition"));  // 作为默认查询条件时是否必输
	        itemVOS[i].setDefaultCondition(_childVOs[i].getStringValue("DefaultCondition")); // 作为默认查询条件时的默认值公式
	        itemVOS[i].setConditionItemType(_childVOs[i].getStringValue("Condition_ItemType"));//条件控件类型
            itemVOS[i].setConditionComboxDesc(_childVOs[i].getStringValue("Condition_ComboxDesc"));//条件下拉设置
            itemVOS[i].setConditionRefDesc(_childVOs[i].getStringValue("Condition_RefDesc"));//条件参照设置
            itemVOS[i].setConditionShowOrder(_childVOs[i].getIntegerValue("Condition_ShowOrder"));//条件显示顺序
	        
	        itemVOS[i].setIsMustInput(_childVOs[i].getBooleanValue("Ismustinput"));          // 是否必输入项!
	
	        itemVOS[i].setLoadformula(_childVOs[i].getStringValue("Loadformula"));           // 加载公式
	        itemVOS[i].setDefaultvalueformula(_childVOs[i].getStringValue("Defaultvalueformula"));  //默认值公式
	        itemVOS[i].setEditformula(_childVOs[i].getStringValue("Editformula"));           // 编辑公式
	        itemVOS[i].setColorformula(_childVOs[i].getStringValue("Colorformula"));         // 颜色公式
	        itemVOS[i].setShoworder(_childVOs[i].getIntegerValue("Showorder"));              // 显示顺序
	        itemVOS[i].setListwidth(_childVOs[i].getIntegerValue("Listwidth"));              // 列表宽度
	        itemVOS[i].setCardwidth(_childVOs[i].getIntegerValue("Cardwidth"));              // 卡片宽度
	
	        itemVOS[i].setListisshowable(_childVOs[i].getBooleanValue("Listisshowable"));    // 列表是否显示
	        itemVOS[i].setListiseditable(_childVOs[i].getStringValue("Listiseditable"));     // 列表是否可编辑
	        itemVOS[i].setCardisshowable(_childVOs[i].getBooleanValue("Cardisshowable"));    // 卡片是否显示
	        itemVOS[i].setCardiseditable(_childVOs[i].getStringValue("Cardiseditable"));     // 卡片是否可编辑
	        
	        itemVOS[i].setBcolorformula(_childVOs[i].getStringValue("BColorformula"));        //
	        itemVOS[i].setItemaction(_childVOs[i].getStringValue("ItemAction"));              //
	        
	        itemVOS[i].setSavedcolumndatatype( (String) map_savedtableColDataTypes.get(itemVOS[i].getItemkey().toUpperCase())); //保存时数据类型
	        
	        itemVOS[i].setExtattr01(_childVOs[i].getStringValue("extattr01"));
	        itemVOS[i].setExtattr02(_childVOs[i].getStringValue("extattr02"));
	        itemVOS[i].setExtattr03(_childVOs[i].getStringValue("extattr03"));
	        itemVOS[i].setExtattr04(_childVOs[i].getStringValue("extattr04"));
	        itemVOS[i].setExtattr05(_childVOs[i].getStringValue("extattr05"));
	        itemVOS[i].setExtattr06(_childVOs[i].getStringValue("extattr06"));
	        itemVOS[i].setExtattr07(_childVOs[i].getStringValue("extattr07"));
	        itemVOS[i].setExtattr08(_childVOs[i].getStringValue("extattr08"));
	        itemVOS[i].setExtattr09(_childVOs[i].getStringValue("extattr09"));
	        itemVOS[i].setExtattr10(_childVOs[i].getStringValue("extattr10"));
	       
	                
	        // 如果下拉框中有内容则取出之,这里定义下拉框中必须是一条SQL
	        if ("下拉框".equals(_childVOs[i].getStringValue("Itemtype"))){
            	resetComBoxItemvo(itemVOS[i],_clientEnv);
            }
            if ("下拉框".equals(_childVOs[i].getStringValue("Condition_ItemType"))){
            	resetConditionComBoxItemvo(itemVOS[i],_clientEnv);
            }

            // 如果是参照  ：参照类型、最终检索sql、首列、数据源
            if ("参照".equals(_childVOs[i].getStringValue("Itemtype"))) {
            	buildRefDesc(templet1VO,itemVOS[i],_clientEnv);
            }
            if ("参照".equals(_childVOs[i].getStringValue("Condition_Itemtype"))) {
            	buildConditionRefDesc(templet1VO,itemVOS[i],_clientEnv);
            }
	
	        //v_keys.add(itemVOS[i].getItemkey());
            String groupId = _childVOs[i].getStringValue("TEMPLETITEMGROUPID");
            	
            if(groupId != null){//该模板项属于一个组
            	Pub_Templet_1_ItemGroupVO groupVO = groupVOMap.get(groupId);
            	itemVOS[i].setItemGroup(groupVO);
            	groupVO.getItemVOs().add(itemVOS[i]);
            }
            else{//直属一个模板
            	templet1VO.getDirectItemVOs().add(itemVOS[i]);
            }
	    }
	
	    templet1VO.setItemVos(itemVOS); // 为主VO设置各项
	
	    //String[] str_itemKeys = (String[]) v_keys.toArray(new String[0]);
	    //templet1VO.setItemKeys(str_itemKeys);
	
	    Vector v_savetableColumns = new Vector();
	    for (int i = 0; i < itemVOS.length; i++) {
	        if (itemVOS[i].getIssave().booleanValue()) {
	            v_savetableColumns.add(itemVOS[i].getItemkey());
	        }
	    }
	    templet1VO.setRealSavedTableColumns( (String[]) v_savetableColumns.toArray(new String[0])); // 真正保存数据库存的列!!!做insert,update
	    // 拼SQL时就直接从这个数组转出来!!
	
	    // 为各项设置父VO的引用
	    for (int i = 0; i < itemVOS.length; i++) {
	        itemVOS[i].setPub_Templet_1VO(templet1VO);
	    }
	
	    return templet1VO; //
	}

  //组织 参照
    private void buildRefDesc(Pub_Templet_1VO vo,Pub_Templet_1_ItemVO itemvo,NovaClientEnvironment _clientEnv){
    	FrameWorkTBUtil tbUtil = new FrameWorkTBUtil(); 
    	if (itemvo.getRefdesc() != null 
    			&& !itemvo.getRefdesc().trim().equalsIgnoreCase("null") 
    			&& !itemvo.getRefdesc().trim().equals("")) {
            String str_allrefdesc = itemvo.getRefdesc().trim(); // 原始的参照定义

            //把参照定义转换一下，把变量都切换一下
            str_allrefdesc=tbUtil.convertExpression(_clientEnv, str_allrefdesc); 
            
            String[] str_refdefines = new ModifySqlUtil().getRefDescTypeAndSQL(str_allrefdesc);
            String str_type = str_refdefines[0]; // 类型,比如是TABLE,TREE,MUTITREE,CUST等!!
            String str_descql = str_refdefines[1]; // 定义的SQL,其中包括{}
            String str_datasourcename = str_refdefines[6]; // 数据源名称!!!!

            itemvo.setRefdesc_type(str_type); // 设置参照类型..
            itemvo.setRefdesc_realsql(str_descql); // 设置参照真正的SQL!!!!
            if (str_datasourcename != null) {
            	itemvo.setRefdesc_datasourcename(str_datasourcename); // 设置数据源名称
            }
            
            //元原模板编码
            String tcode=vo.getTempletcode();
            String ikey=itemvo.getItemkey();
            String iname=itemvo.getItemname();

            if (str_type.equals("TABLE") || str_type.equals("TREE")) { // 如果第一种表型或树型参照!!
                if (str_descql == null || str_descql.trim().equals("")) {
                    logger.warn("警告:模板[" + tcode + "]子项[" + ikey + "][" + iname + "]参照定义的SQL是为空,将会产生错误！");
                } else {
                    String str_descql_upperCase = str_descql.toUpperCase();
                    if (str_descql_upperCase.indexOf("WHERE") <= 0) {
                        logger.warn("警告:模板[" + tcode + "]子项[" + ikey + "][" + iname + "]参照定义的SQL[" +
                            str_descql + "]没有Where条件定义,将会产生错误！");
                    } else {
                        logger.info("模板[" + tcode + "]子项[" + ikey + "][" + iname + "]参照定义的SQL是[" + str_descql + "]");
                    }

                    String str_descql_firstName = new ModifySqlUtil().findSQLFirstName(str_descql); // 找出其中第一个列的名称!!!
                    itemvo.setRefdesc_firstColName(str_descql_firstName); // 设置第一列的名称!!
                }
            }else if(str_type.equals("MUTITREE")){
            	str_descql = str_refdefines[4];
            	if (str_descql == null || str_descql.trim().equals("")) {
                    logger.warn("警告:模板[" + tcode + "]子项[" + ikey + "][" + iname + "]参照定义的SQL是为空,将会产生错误!!");
                } else {
                    String str_descql_upperCase = str_descql.toUpperCase();
                    if (str_descql_upperCase.indexOf("WHERE") <= 0) {
                        logger.warn("警告:模板[" + tcode + "]子项[" + ikey + "][" + iname + "]参照定义的SQL[" + str_descql + "]没有Where条件定义,将会产生错误!!!!");
                    } else {
                        logger.info("模板[" + tcode + "]子项[" + ikey + "][" + iname + "]参照定义的SQL是[" + str_descql + "]");
                    }
                    itemvo.setRefdesc_realsql(str_descql); // 设置参照真正的SQL!!!!
                    String str_descql_firstName = new ModifySqlUtil().findSQLFirstName(str_descql); // 找出其中第一个列的名称!!!
                    itemvo.setRefdesc_firstColName(str_descql_firstName); // 设置第一列的名称!!
                }
            }
        }
        
    }
    //组织 条件参照
    private void buildConditionRefDesc(Pub_Templet_1VO vo,Pub_Templet_1_ItemVO itemvo,NovaClientEnvironment _clientEnv){
    	FrameWorkTBUtil tbUtil = new FrameWorkTBUtil(); 
    	if (itemvo.getConditionRefDesc() != null 
    			&& !itemvo.getConditionRefDesc().trim().equalsIgnoreCase("null") 
    			&& !itemvo.getConditionRefDesc().trim().equals("")) {
            String str_allrefdesc = itemvo.getConditionRefDesc().trim(); // 原始的参照定义

            //把参照定义转换一下，把变量都切换一下
            str_allrefdesc=tbUtil.convertExpression(_clientEnv, str_allrefdesc); 
            
            String[] str_refdefines = new ModifySqlUtil().getRefDescTypeAndSQL(str_allrefdesc);
            String str_type = str_refdefines[0]; // 类型,比如是TABLE,TREE,MUTITREE,CUST等!!
            String str_descql = str_refdefines[1]; // 定义的SQL,其中包括{}
            String str_datasourcename = str_refdefines[6]; // 数据源名称!!!!

            itemvo.setConditionRefDescType(str_type); // 设置参照类型..
            itemvo.setConditionRefDescRealSql(str_descql); // 设置参照真正的SQL!!!!
            if (str_datasourcename != null) {
            	itemvo.setConditionRefDescDataSourceName(str_datasourcename); // 设置数据源名称
            }
            
            //元原模板编码
            String tcode=vo.getTempletcode();
            String ikey=itemvo.getItemkey();
            String iname=itemvo.getItemname();

            if (str_type.equals("TABLE") || str_type.equals("TREE")) { // 如果第一种表型或树型参照!!
                if (str_descql == null || str_descql.trim().equals("")) {
                    logger.warn("警告:模板[" + tcode + "]子项[" + ikey + "][" + iname + "]参照定义的SQL是为空,将会产生错误！");
                } else {
                    String str_descql_upperCase = str_descql.toUpperCase();
                    if (str_descql_upperCase.indexOf("WHERE") <= 0) {
                        logger.warn("警告:模板[" + tcode + "]子项[" + ikey + "][" + iname + "]参照定义的SQL[" +
                            str_descql + "]没有Where条件定义,将会产生错误！");
                    } else {
                        logger.info("模板[" + tcode + "]子项[" + ikey + "][" + iname + "]参照定义的SQL是[" + str_descql + "]");
                    }

                    String str_descql_firstName = new ModifySqlUtil().findSQLFirstName(str_descql); // 找出其中第一个列的名称!!!
                    itemvo.setConditionRefDescFirstColName(str_descql_firstName); // 设置第一列的名称!!
                }
            }else if(str_type.equals("MUTITREE")){
            	str_descql = str_refdefines[4];
            	if (str_descql == null || str_descql.trim().equals("")) {
                    logger.warn("警告:模板[" + tcode + "]子项[" + ikey + "][" + iname + "]参照定义的SQL是为空,将会产生错误!!");
                } else {
                    String str_descql_upperCase = str_descql.toUpperCase();
                    if (str_descql_upperCase.indexOf("WHERE") <= 0) {
                        logger.warn("警告:模板[" + tcode + "]子项[" + ikey + "][" + iname + "]参照定义的SQL[" + str_descql + "]没有Where条件定义,将会产生错误!!!!");
                    } else {
                        logger.info("模板[" + tcode + "]子项[" + ikey + "][" + iname + "]参照定义的SQL是[" + str_descql + "]");
                    }
                    itemvo.setConditionRefDescRealSql(str_descql); // 设置参照真正的SQL!!!!
                    String str_descql_firstName = new ModifySqlUtil().findSQLFirstName(str_descql); // 找出其中第一个列的名称!!!
                    itemvo.setConditionRefDescFirstColName(str_descql_firstName); // 设置第一列的名称!!
                }
            }
        }
        
    }
    
    /**
     * 从数据库中根据模板编码创建查询模板VO
     *
     * @param _code
     * @return
     * @throws Exception
     */
    public Pub_QueryTempletVO getPub_QueryTempletVO(String _code, NovaClientEnvironment _clientEnv) throws Exception {
        CommDMO commDMO = new CommDMO();
        String str_sql = "select * from pub_querytemplet where lower(templetcode)='" + ("" + _code).toLowerCase() + "'";
        HashVO[] vos = commDMO.getHashVoArrayByDS(null, str_sql);
        if (vos == null || vos.length <= 0) {
            throw new Exception("编码为[" + _code + "]的查询模板不存在!!");
        }
        HashVO parentVO = vos[0]; //

        String str_sql_item = "select * from pub_querytemplet_item where pk_pub_querytemplet='" +
            parentVO.getStringValue("pk_pub_querytemplet") + "' order by showorder asc";
        HashVO[] hashVOs_item = commDMO.getHashVoArrayByDS(null, str_sql_item);
        return getPub_QueryTempletVO(parentVO, hashVOs_item, _clientEnv);
    }

    /**
     * 直接根据两个HashVO创建模板VO
     *
     * @param _parentVO
     * @param _childVOs
     * @return
     * @throws Exception
     */

    public Pub_QueryTempletVO getPub_QueryTempletVO(HashVO _parentVO, HashVO[] _childVOs,
        NovaClientEnvironment _clientEnv) throws Exception {
        CommDMO commDMO = new CommDMO();
        // 处理模板主表!!!!!!!!!!!!!
        Pub_QueryTempletVO templet1VO = new Pub_QueryTempletVO(); //
        templet1VO.setTempletcode(_parentVO.getStringValue("Templetcode")); // 模板编码
        templet1VO.setTempletname(_parentVO.getStringValue("Templetname")); // 表名
        templet1VO.setSql(_parentVO.getStringValue("Sql"));

        // 处理模板子表!!!!!!!!!!!!!
        Pub_QueryTemplet_ItemVO[] itemVOS = new Pub_QueryTemplet_ItemVO[_childVOs.length]; //
        Vector v_keys = new Vector();
        for (int i = 0; i < itemVOS.length; i++) {
            itemVOS[i] = new Pub_QueryTemplet_ItemVO();
            itemVOS[i].setItemKey(_childVOs[i].getStringValue("Itemkey"));
            itemVOS[i].setItemName(_childVOs[i].getStringValue("Itemname"));
            itemVOS[i].setItemType(_childVOs[i].getStringValue("Itemtype"));
            itemVOS[i].setComboxdesc(_childVOs[i].getStringValue("Comboxdesc")); // 设置下拉框定义!!
            itemVOS[i].setRefdesc(_childVOs[i].getStringValue("Refdesc")); // 设置参照定义!!

            // 如果下拉框中有内容则取出之,这里定义下拉框中必须是一条SQL
            if (_childVOs[i].getStringValue("Itemtype").equals("下拉框")) { //
                if (itemVOS[i].getComboxdesc() != null && !itemVOS[i].getComboxdesc().trim().equals("")) {
                    String str_comboxitem_sql = itemVOS[i].getComboxdesc().trim();
                    try { //
                        String modify_str = null; //
                        try {
                            modify_str = new FrameWorkTBUtil().convertExpression( _clientEnv,str_comboxitem_sql); //
                        } catch (Exception ex) {
                            System.out.println("模板[" + templet1VO.getTempletcode() + "]子项[" + itemVOS[i].getItemKey() +
                                               "][" + itemVOS[i].getItemName() + "]下拉框定义的SQL[" + str_comboxitem_sql +
                                               "]转换失败!!!");
                            ex.printStackTrace();
                        }

                        if (modify_str != null) { // 如果成功转换!!
                            HashVO[] hvs = null;
                            try {
                                System.out.println("准备执行下拉框[" + templet1VO.getTempletcode() + "][" +
                                    itemVOS[i].getItemKey() + "][" + itemVOS[i].getItemName() + "]的转换后的SQL[" +
                                    modify_str + "]");
                                hvs = commDMO.getHashVoArrayByDS(null, modify_str); // 根据下拉框中的SQL取得下拉框中所有数据的HashVO,可能会有异常!!
                            } catch (Exception ex) {
                                System.out.println("执行下拉框[" + templet1VO.getTempletcode() + "][" +
                                    itemVOS[i].getItemKey() + "][" + itemVOS[i].getItemName() + "]定义转换后SQL[" +
                                    modify_str + "]失败!!原因:" + ex.getMessage());
                                ex.printStackTrace();
                            }

                            if (hvs != null) {
                                ComBoxItemVO[] comBoxItemVOs = new ComBoxItemVO[hvs.length];
                                for (int j = 0; j < hvs.length; j++) {
                                    comBoxItemVOs[j] = new ComBoxItemVO(hvs[j]); //
                                }
                                itemVOS[i].setItemVOs(comBoxItemVOs); // 设置下拉框数据!!!!!
                            }
                        }
                    } catch (Exception ex) {
                        System.out.println("模板[" + templet1VO.getTempletcode() + "]子项[" + itemVOS[i].getItemKey() +
                                           "][" + itemVOS[i].getItemName() + "]下拉框定义SQL[" + str_comboxitem_sql +
                                           "]有问题!!!"); //
                        ex.printStackTrace();
                    }
                }
            }

            // 如果是参照
            if (_childVOs[i].getStringValue("Itemtype").equals("参照")) {
                if (itemVOS[i].getRefdesc() != null && !itemVOS[i].getRefdesc().trim().equalsIgnoreCase("null") &&
                    !itemVOS[i].getRefdesc().trim().equals("")) {
                    String str_allrefdesc = itemVOS[i].getRefdesc().trim(); // 原始的参照定义
                    itemVOS[i].setRefdesc(str_allrefdesc); // 设置参照真正的SQL!!!!
                }
            }

            v_keys.add(itemVOS[i].getItemKey());
        }

        templet1VO.setItemVOs(itemVOS); // 为主VO设置各项

        String[] str_itemKeys = (String[]) v_keys.toArray(new String[0]);
        templet1VO.setItemKeys(str_itemKeys);

        // Vector v_savetableColumns = new Vector();
        // for (int i = 0; i < itemVOS.length; i++) {
        // if (itemVOS[i].getIssave().booleanValue()) {
        // v_savetableColumns.add(itemVOS[i].getItemkey());
        // }
        // }
        // templet1VO.setRealSavedTableColumns((String[]) v_savetableColumns
        // .toArray(new String[0])); // 真正保存数据库存的列!!!做insert,update
        // // 拼SQL时就直接从这个数组转出来!!

        // 为各项设置父VO的引用
        // for (int i = 0; i < itemVOS.length; i++) {
        // itemVOS[i].setPub_Templet_1VO(templet1VO);
        // }

        return templet1VO; //
    }

    /**
     * 取得BillListData
     *
     * @param _sql
     * @param _templetVO
     * @param _env
     * @return
     * @throws Exception
     */
    public Object[] getBillCardDataByDS(String _datasourcename, String _sql, Pub_Templet_1VO _templetVO,
                                        NovaClientEnvironment _env) throws Exception {
        if (_datasourcename == null) {
            _datasourcename = NovaServerEnvironment.getInstance().getDefaultDataSourceName();
        }
        Object[][] objs_formuls = getBillListDataByDS(_datasourcename, _sql, _templetVO, _env); // 执行加载公式后的值
        if (objs_formuls != null && objs_formuls.length > 0) {
            return objs_formuls[0]; // 返回第一行
        } else {
            return null;
        }
    }

    /**
     * 获得BillVO数组....
     * @return
     * @throws Exception
     */
    public BillVO[] getBillVOsByDS(String _datasourcename, String _sql, Pub_Templet_1VO _templetVO) throws Exception {
        if (_datasourcename == null) {
            _datasourcename = NovaServerEnvironment.getInstance().getDefaultDataSourceName();
        }
        //先取得所有数据
        Object[][] objs = getBillListDataByDS(_datasourcename, _sql, _templetVO, this.getClientEnv()); //取得所有数据!!
        return getBillVOs(_templetVO,objs);
    }
    /**
     * 根据 模板编码,hashvo 生成billvo
     * @param templetcode
     * @param hashvo
     * @param clientenv
     * @return
     * @throws Exception
     */
    public BillVO[] getBillVOs(String templetcode,HashVO vo,NovaClientEnvironment clientenv) throws Exception
    {
    	if(templetcode==null||vo==null||templetcode.equals("")) return null;
    	Pub_Templet_1VO templetvo = getPub_Templet_1VO(templetcode, clientenv);
    	return getBillVOs(templetvo,buildObject(templetvo,new HashVO[]{vo},clientenv));
    	//return getBillVOs(templetvo,calculateBillVOValues(templetvo,hashvo));
    }
    //根据 Pub_Templet_1VO,hashvo 生成billvo
    public BillVO[] getBillVOs(Pub_Templet_1VO tvo,HashVO vo) throws Exception
    {
    	if(tvo==null||vo==null) return null;
    	return getBillVOs(tvo,buildObject(tvo,new HashVO[]{vo},null));
    	//return getBillVOs(_templetVO,calculateBillVOValues(_templetVO,hashvo));
    	
    }
	private BillVO[] getBillVOs(Pub_Templet_1VO _templetVO,Object[][] objs) throws Exception
	{
		if(_templetVO==null||objs==null)
			return null;
		Pub_Templet_1_ItemVO[] templetItemVOs = _templetVO.getItemVos(); // 各子项.
		int li_length = _templetVO.getItemVos().length;
		 	BillVO[] billVOs = new BillVO[objs.length]; //
		 	for (int r = 0; r < billVOs.length; r++) {
		 		billVOs[r] = new BillVO();
		 		billVOs[r].setQueryTableName(_templetVO.getTablename()); //设置查询表
		 		billVOs[r].setSaveTableName(_templetVO.getSavedtablename()); //设置保存表
		 		billVOs[r].setPkName(_templetVO.getPkname()); //设置主键字段名
		 		billVOs[r].setSequenceName(_templetVO.getPksequencename()); // 序列名
	
		 		// 所有ItemKey
		 		String[] all_Keys = new String[li_length + 1]; //
		 		all_Keys[0] = "_RECORD_ROW_NUMBER"; // 行号
		 		for (int i = 1; i < all_Keys.length; i++) {
		 			all_Keys[i] = _templetVO.getItemKeys()[i - 1];
		 		}
	
		 		// 所有的名称
		 		String[] all_Names = new String[li_length + 1]; //
		 		all_Names[0] = "行号"; // 行号
		 		for (int i = 1; i < all_Names.length; i++) {
		 			all_Names[i] = _templetVO.getItemNames()[i - 1];
		 		}
	
		 		String[] all_Types = new String[li_length + 1]; //
		 		all_Types[0] = "行号"; // 行号
		 		for (int i = 1; i < all_Types.length; i++) {
		 			all_Types[i] = _templetVO.getItemTypes()[i - 1];
		 		}
	
		 		String[] all_ColumnTypes = new String[li_length + 1]; //
		 		all_ColumnTypes[0] = "NUMBER"; // 行号
		 		for (int i = 1; i < all_ColumnTypes.length; i++) {
		 			all_ColumnTypes[i] = templetItemVOs[i - 1].getSavedcolumndatatype(); //
		 		}
	
		 		boolean[] bo_isNeedSaves = new boolean[li_length + 1];
		 		bo_isNeedSaves[0] = false; // 行号
		 		for (int i = 1; i < bo_isNeedSaves.length; i++) {
		 			bo_isNeedSaves[i] = templetItemVOs[i - 1].isNeedSave();
		 		}
	
		 		billVOs[r].setKeys(all_Keys); //设置所有的key
		 		billVOs[r].setNames(all_Names); //设置所有的Name
		 		billVOs[r].setItemType(all_Types); // 控件类型!!设置所有的类型!!
		 		billVOs[r].setColumnType(all_ColumnTypes); // 数据库类型!!
		 		billVOs[r].setNeedSaves(bo_isNeedSaves); // 是否需要保存!!
		 		billVOs[r].setDatas(objs[r]); // 设置真正的数据!!
		 	}
		 	return billVOs; //
	}
    
	
	/**
     * 取得BillListPanel需要的BillListData,包括行号!!经过参照SQL宏转换,加载公式处理!!
     *
     * @param _sql,查询的SQL
     * @param _templetVO,模板定义的VO,从前台传过来!!
     * @param NovaClientEnvironment
     *            cnv, 客户端缓存变量
     * @return 数据行数
     * @throws Exception
     */
    public int getBillListRowCountByDS(String _ds, String _sql, Pub_Templet_1VO _templetVO,
                                          NovaClientEnvironment _clientEnv) throws Exception {
        CommDMO commDMO = new CommDMO();        
        HashVO[] hashVOs = commDMO.getHashVoArrayByDS(_ds, "select count(*) rowcount from ("+_sql+") t");
        return hashVOs[0].getIntegerValue("rowcount").intValue();        
    }
	
    /**
     * 取得BillListPanel需要的BillListData数据片段,包括行号
     * 经过参照SQL宏转换,加载公式处理
     *
     * @param _sql,查询的SQL
     * @param _templetVO,模板定义的VO,从前台传过来!!
     * @param sRowNum
     * @param eRowNum
     * @param NovaClientEnvironment cnv, 客户端缓存变量
     * @return
     * @throws Exception
     */
    public Object[][] getBillListSubDataByDS(String _ds, String _sql, int sRowNum,int eRowNum,
    		Pub_Templet_1VO _templetVO,NovaClientEnvironment _clientEnv) throws Exception {
        CommDMO commDMO = new CommDMO();
        int li_length = _templetVO.getItemKeys().length; // 总共有多少列,加上行号,还应多一列!!
        Pub_Templet_1_ItemVO[] itemVos = _templetVO.getItemVos(); // 各子项.
        String[] itemKeys = _templetVO.getItemKeys(); // 各项的key
        String[] itemTypes = _templetVO.getItemTypes(); // 各项的类型
        String str_pkFieldName = _templetVO.getPkname();

        //加入数据片段处理（RowNum判断）
        String sql="select * from (select t.*,rownum rNum from ("+_sql+") t) where rNum>="+(sRowNum+1)+" and rNum<="+(eRowNum+1);
        sql = "select * from (select rt.*,rownum rNum from ("+_sql+") rt where  rownum <="+(eRowNum+1)+") where rNum >="+(sRowNum+1);
        HashVO[] hashVOs = commDMO.getHashVoArrayByDS(_ds, sql);
        
        
        Object[][] valueData = new Object[hashVOs.length][li_length + 1]; // 创建数据对象!!!,比模板多一列是行号,永远在第一列!!

        FrameWorkTBUtil tbUtil = new FrameWorkTBUtil(); 

        // 遍历各列
        for (int i = 0; i < hashVOs.length; i++) { 
            Object[] rowobjs = new Object[li_length + 1]; // 一行的数据
            HashMap map_cache = new HashMap(); // 一行数据中的缓存!!!其Value可能是String,ComBoxItemVO,RefItemVO

            RowNumberItemVO rowNumberVO = new RowNumberItemVO(); // 行号VO
            rowNumberVO.setState(NovaConstants.BILLDATAEDITSTATE_INIT); //
            rowNumberVO.setRecordIndex(hashVOs[i].getIntegerValue("rNum").intValue()); // 记录号(分页读取时已经由数据库生成了)

            //首先获得一行的值，同时创建每个值的展现（文本框、下拉框、参照等等）
            String[] str_realvalues = new String[li_length]; //
            for (int j = 0; j < li_length; j++) { // 遍历各列
                String str_key = itemKeys[j];
                String str_type = itemTypes[j]; //
                if (str_type.equals("日历")) {
                    str_realvalues[j] = hashVOs[i].getStringValueForDay(str_key); //
                } else if (str_type.equals("时间")) {
                    str_realvalues[j] = hashVOs[i].getStringValueForSecond(str_key); //
                } else {
                    str_realvalues[j] = hashVOs[i].getStringValue(str_key); //
                }

                if (str_realvalues[j] != null) {
                    if (itemTypes[j].equals("文本框")) { // 如果是文本框
                        map_cache.put(str_key, str_realvalues[j]);
                    } else if (itemTypes[j].equals("下拉框")) {
                        map_cache.put(str_key, new ComBoxItemVO(str_realvalues[j], str_realvalues[j], str_realvalues[j]));
                    } else if (itemTypes[j].equals("参照")) {
                        map_cache.put(str_key, new RefItemVO(str_realvalues[j], str_realvalues[j], str_realvalues[j]));
                    } else {
                        map_cache.put(str_key, str_realvalues[j]); //
                    }
                }
            }

            //判断是否设置更新主键，如果没有设置，则直接使用所有的取出列作为唯一条件。如果出现取出列完全相同的情况，会出现不能更新的情况。
            if (str_pkFieldName == null || str_pkFieldName.trim().equals("")) { //如果没有设主键则则原来的值放在行号里,这样才能拼updateSQL
                rowNumberVO.setItemKeys(itemKeys); // 所有的列的key
                rowNumberVO.setItemOldValues(str_realvalues); // 各列的实际值	
            }

            rowobjs[0] = rowNumberVO; // 第一列永远是行号VO

            // 然后再处理每一列的值
            //TODO 这一轮循环覆盖了前面一轮放入的值吗？map_cache.put(str_key,XXXX);
            for (int j = 0; j < li_length; j++) { // 遍历各列
                String str_key = itemKeys[j];
                String str_type = itemTypes[j];

                String str_value = null;
                if (str_type.equals("日历")) {
                    str_value = hashVOs[i].getStringValueForDay(str_key); //
                } else if (str_type.equals("时间")) {
                    str_value = hashVOs[i].getStringValueForSecond(str_key); //
                } else {
                    str_value = hashVOs[i].getStringValue(str_key); //
                }

                if (str_value != null) {
                    if (itemTypes[j].equals("文本框")) { // 如果是文本框
                        rowobjs[j + 1] = str_value;
                    } else if (itemTypes[j].equals("下拉框")) { // 如果是下拉框..
                        ComBoxItemVO[] comBoxItemVos = itemVos[j].getComBoxItemVos();
                        ComBoxItemVO matchVO = findComBoxItemVO(comBoxItemVos, str_value); // //..
                        if (matchVO != null) {
                            rowobjs[j + 1] = matchVO;
                        } else {
                            rowobjs[j + 1] = new ComBoxItemVO(str_value, str_value, str_value); // 下拉框VO
                        }
                    } else if (itemTypes[j].equals("参照")) {
                        if (itemVos[j].getRefdesc_type().equalsIgnoreCase("TABLE") ||
                            itemVos[j].getRefdesc_type().equalsIgnoreCase("TREE")||itemVos[j].getRefdesc_type().equalsIgnoreCase("MUTITREE")) { // 如果是表或者树
                            String str_refsql = itemVos[j].getRefdesc_realsql(); //
                            if (str_refsql != null && !str_refsql.trim().equals("")) {
                                String modify_str = null; //

                                try {
                                    modify_str = new FrameWorkTBUtil().convertFormulaMacPars(str_refsql, _clientEnv, map_cache); // !!!!得到转换后的SQL,即直接可以执行的SQL,即将其中的{aaa}进行转换,!!!这是关键
                                } catch (Exception ex) {
                                    System.out.println("转换参照[" + _templetVO.getTempletcode() + "][" +
                                        itemVos[j].getItemkey() + "][" + itemVos[j].getItemname() + "]参照定义SQL[" +
                                        str_refsql + "]失败!!!");
                                    ex.printStackTrace(); //
                                }

                                if (modify_str != null) { // 如果成功转换SQL!!
                                    modify_str = StringUtil.replaceAll(modify_str, "1=1",
                                        itemVos[j].getRefdesc_firstColName() + "='" + str_value + "'"); // 将SQL语句中的1=1替换成
                                    // System.out.println("开始执行参照定义转换后的SQL[" +
                                    // itemVos[j].getRefdesc_datasourcename() +
                                    // "]:" + modify_str); //
                                    HashVO[] ht_allRefItemVOS = null; //
                                    if (getRefCache().containsKey(modify_str)) { //如果缓存中有!!
                                        ht_allRefItemVOS = (HashVO[]) getRefCache().get(modify_str);
                                    } else {
                                        try {
                                            ht_allRefItemVOS = commDMO.getHashVoArrayByDS(itemVos[j].
                                                getRefdesc_datasourcename(), modify_str); // 执行SQL!!!可能会抛异常!!
                                            getRefCache().put(modify_str, ht_allRefItemVOS); //往缓存中塞!!
                                        } catch (Exception ex) {
                                            System.out.println("执行参照[" + _templetVO.getTempletcode() + "][" +
                                                itemVos[j].getItemkey() + "][" + itemVos[j].getItemname() + "]转换后SQL[" +
                                                modify_str + "]失败,原因:" + ex.getMessage()); //
                                            ex.printStackTrace();
                                        }
                                    }

                                    if (ht_allRefItemVOS != null) { // 如果取得到数据!!
                                        boolean bo_iffindref = false;
                                        for (int pp = 0; pp < ht_allRefItemVOS.length; pp++) { // 遍历去找!!!
                                            if (str_value.equals(ht_allRefItemVOS[pp].getStringValue(0))) {
                                                rowobjs[j + 1] = new RefItemVO(ht_allRefItemVOS[pp]); //
                                                bo_iffindref = true; // 如果非常幸运的找到品配的了!!!!!!!!!
                                                break;

                                            }
                                        }
                                        if (!bo_iffindref) { // 如果没找到品配的,则直接创建参照VO
                                            rowobjs[j + 1] = new RefItemVO(str_value, str_value, str_value); // 参照VO
                                        }
                                    } else
                                    // 如果执行SQL取数失败!!
                                    {
                                        rowobjs[j + 1] = new RefItemVO(str_value, str_value, str_value); // 参照VO
                                    }
                                } else { // 如果转换SQL失败
                                    rowobjs[j + 1] = new RefItemVO(str_value, str_value, str_value); // 参照VO
                                }
                            } else {
                                rowobjs[j + 1] = new RefItemVO(str_value, str_value, str_value); // 参照VO
                            }
                        } else { // 如果不是表型1或树型1参照!!,比如自定义,TABLE2,TREE2等
                            rowobjs[j + 1] = new RefItemVO(str_value, str_value, str_value); // 参照VO
                        }
                    } else { // 如果是其他控件
                        rowobjs[j + 1] = str_value;
                    }
                } else { // 如果没取到数,则为空!
                    rowobjs[j + 1] = null;
                }
                //TODO 此处是否覆盖了前面的设置？
                map_cache.put(str_key, rowobjs[j + 1]); // 往缓存中送入
            } // 一行数据中的各列全部处理处理结束!!!

            valueData[i] = rowobjs; //
        } // 所有行数据处理结束!!

        Object[][] objs_formuls = execLoadformula(valueData, _templetVO, _clientEnv); // 执行加载公式!!...
        return objs_formuls; //
    } //
    
    
	/**
     * 取得BillListPanel需要的BillListData,包括行号!!经过参照SQL宏转换,加载公式处理!!
     *
     * @param _sql,查询的SQL
     * @param _templetVO,模板定义的VO,从前台传过来!!
     * @param NovaClientEnvironment
     *            cnv, 客户端缓存变量
     * @return
     * @throws Exception
     */
    public Object[][] getBillListDataByDS(String _datasourcename, String _sql, Pub_Templet_1VO _templetVO,
            NovaClientEnvironment _clientEnv) throws Exception {
		if (_datasourcename == null || _datasourcename.trim().equals("")) {
			_datasourcename = NovaServerEnvironment.getInstance()
					.getDefaultDataSourceName();
		}

		CommDMO commDMO = new CommDMO();
		//HashVO[] hashVOs = commDMO.getHashVoArrayByDS(_datasourcename, _sql); // 取得真正的数据!!!!
		HashVO[] hashVOs = commDMO.getHashVoArrayByDSUnlimitRows(_datasourcename, _sql);//取出全部数据，不受行数限制
		
		return buildObject(_templetVO, hashVOs, _clientEnv);

	}

	private Object[][] buildObject(Pub_Templet_1VO _templetVO,
			HashVO[] hashVOs, NovaClientEnvironment _clientEnv) {
		int li_length = _templetVO.getItemKeys().length; // 总共有多少列,加上行号,还应多一列!!
		Pub_Templet_1_ItemVO[] itemVos = _templetVO.getItemVos(); // 各子项.
		String[] itemKeys = _templetVO.getItemKeys(); // 各项的key
		String[] itemTypes = _templetVO.getItemTypes(); // 各项的类型
		String str_pkFieldName = _templetVO.getPkname();

		Object[][] valueData = new Object[hashVOs.length][li_length + 1]; // 创建数据对象!!!,比模板多一列是行号,永远在第一列!!

		CommDMO commDMO = new CommDMO();

		// HashMap hm_RefItemVos = new HashMap(); // 一下子为各个参照灌入所有的参照数据!!!
		// ModifySqlUtil sqlUtil = new ModifySqlUtil(); // 创建工具类
		FrameWorkTBUtil tbUtil = new FrameWorkTBUtil(); //

		for (int i = 0; i < hashVOs.length; i++) { // 遍历各行
			Object[] rowobjs = new Object[li_length + 1]; // 一行的数据
			HashMap map_cache = new HashMap(); // 一行数据中的缓存!!!其Value可能是String,ComBoxItemVO,RefItemVO

			// 首先一下子将所有值灌入....
			String[] str_realvalues = new String[li_length]; //
			for (int j = 0; j < li_length; j++) { // 遍历各列
				String str_key = itemKeys[j];
				String str_type = itemTypes[j]; //
				if (str_type.equals("日历")) {
					str_realvalues[j] = hashVOs[i]
							.getStringValueForDay(str_key); //
				} else if (str_type.equals("时间")) {
					str_realvalues[j] = hashVOs[i]
							.getStringValueForSecond(str_key); //
				} else {
					str_realvalues[j] = hashVOs[i].getStringValue(str_key); //
				}

				if (str_realvalues[j] != null) {
					if (itemTypes[j].equals("文本框")) { // 如果是文本框
						map_cache.put(str_key, str_realvalues[j]);
					} else if (itemTypes[j].equals("下拉框")) {
						map_cache.put(str_key, new ComBoxItemVO(
								str_realvalues[j], str_realvalues[j],
								str_realvalues[j]));
					} else if (itemTypes[j].equals("参照")) {
						map_cache.put(str_key, new RefItemVO(str_realvalues[j],
								str_realvalues[j], str_realvalues[j]));
					} else {
						map_cache.put(str_key, str_realvalues[j]); //
					}
				}
			}

			/**
			 * James.W 加注 此处是标志每一行的行头的重要信息，下面是一个重要的判断： - 将来执行更新控制的依据是行头对象。 -
			 * 如果主键存在，则形成Sql的定位依据就是主键 where PK_name=PK_value -
			 * 如果主键不存在，则形成的sql以所有字段作为条件 where File01=Field01Value and ...
			 */
			RowNumberItemVO rowNumberVO = new RowNumberItemVO(); // 行号VO
			rowNumberVO.setState(NovaConstants.BILLDATAEDITSTATE_INIT); //
			rowNumberVO.setRecordIndex(i); // 记录号
			// 如果没有设主键则则原来的值放在行号里,这样才能拼updateSQL
			if (str_pkFieldName == null || str_pkFieldName.trim().equals("")) {
				rowNumberVO.setItemKeys(itemKeys); // 所有的列的key
				rowNumberVO.setItemOldValues(str_realvalues); // 各列的实际值
			}

			rowobjs[0] = rowNumberVO; // 第一列永远是行号VO

			// 然后再处理每一列的值
			for (int j = 0; j < li_length; j++) { // 遍历各列
				String str_key = itemKeys[j];
				String str_type = itemTypes[j];

				String str_value = null;
				if (str_type.equals("日历")) {
					str_value = hashVOs[i].getStringValueForDay(str_key); //
				} else if (str_type.equals("时间")) {
					str_value = hashVOs[i].getStringValueForSecond(str_key); //
				} else {
					str_value = hashVOs[i].getStringValue(str_key); //
				}

				if (str_value != null) {
					if (itemTypes[j].equals("文本框")) { // 如果是文本框
						rowobjs[j + 1] = str_value;
					} else if (itemTypes[j].equals("下拉框")) { // 如果是下拉框..
						ComBoxItemVO[] comBoxItemVos = itemVos[j]
								.getComBoxItemVos();
						ComBoxItemVO matchVO = findComBoxItemVO(comBoxItemVos,
								str_value); // //..
						if (matchVO != null) {
							rowobjs[j + 1] = matchVO;
						} else {
							rowobjs[j + 1] = new ComBoxItemVO(str_value,
									str_value, str_value); // 下拉框VO
						}
					} else if (itemTypes[j].equals("参照")) {
						if (itemVos[j].getRefdesc_type().equalsIgnoreCase("TABLE")
								|| itemVos[j].getRefdesc_type().equalsIgnoreCase("TREE")
								|| itemVos[j].getRefdesc_type().equalsIgnoreCase("MUTITREE")) { // 如果是表或者树
							String str_refsql = itemVos[j].getRefdesc_realsql(); //
							if (str_refsql != null && !str_refsql.trim().equals("")) {
								String modify_str = null; //
								try {
									modify_str = new FrameWorkTBUtil().convertFormulaMacPars(str_refsql, _clientEnv, map_cache); // !!!!得到转换后的SQL,即直接可以执行的SQL,即将其中的{aaa}进行转换,!!!这是关键
								} catch (Exception ex) {
									System.out.println("转换参照["
											+ _templetVO.getTempletcode()
											+ "][" + itemVos[j].getItemkey()
											+ "][" + itemVos[j].getItemname()
											+ "]参照定义SQL[" + str_refsql
											+ "]失败!!!");
									ex.printStackTrace(); //
								}

								if (modify_str != null) { // 如果成功转换SQL!!
									// 将SQL语句中的1=1替换成条件子句
									modify_str = StringUtil.replaceAll(modify_str,"1=1", itemVos[j].getRefdesc_firstColName()+ "='" + str_value + "'");
									// 清除子句-=【 xxxxxx 】=-
									modify_str = StringUtil.clearSubstring(	modify_str, "-=【", "】=-");

									// System.out.println("开始执行参照定义转换后的SQL[" +
									// itemVos[j].getRefdesc_datasourcename() +
									// "]:" + modify_str); //
									HashVO[] ht_allRefItemVOS = null; //
									if (getRefCache().containsKey(modify_str)) { // 如果缓存中有!!
										ht_allRefItemVOS = (HashVO[]) getRefCache().get(modify_str);
									} else {
										try {
											ht_allRefItemVOS = commDMO.getHashVoArrayByDS(itemVos[j].getRefdesc_datasourcename(), modify_str); //
											getRefCache().put(modify_str, ht_allRefItemVOS); // 往缓存中塞!!
										} catch (Exception ex) {
											System.out.println("执行参照["
													+ _templetVO
															.getTempletcode()
													+ "]["
													+ itemVos[j].getItemkey()
													+ "]["
													+ itemVos[j].getItemname()
													+ "]转换后SQL[" + modify_str
													+ "]失败,原因:"
													+ ex.getMessage()); //
											ex.printStackTrace();
										}
									}

									if (ht_allRefItemVOS != null) { // 如果取得到数据!!
										boolean bo_iffindref = false;
										for (int pp = 0; pp < ht_allRefItemVOS.length; pp++) { // 遍历去找!!!
											if (str_value
													.equals(ht_allRefItemVOS[pp]
															.getStringValue(0))) {
												rowobjs[j + 1] = new RefItemVO(
														ht_allRefItemVOS[pp]); //
												bo_iffindref = true; // 如果非常幸运的找到品配的了!!!!!!!!!
												break;

											}
										}
										if (!bo_iffindref) { // 如果没找到品配的,则直接创建参照VO
											rowobjs[j + 1] = new RefItemVO(
													str_value, str_value,
													str_value); // 参照VO
										}
									} else
									// 如果执行SQL取数失败!!
									{
										rowobjs[j + 1] = new RefItemVO(
												str_value, str_value, str_value); // 参照VO
									}
								} else { // 如果转换SQL失败
									rowobjs[j + 1] = new RefItemVO(str_value,
											str_value, str_value); // 参照VO
								}
							} else {
								rowobjs[j + 1] = new RefItemVO(str_value,
										str_value, str_value); // 参照VO
							}
						} else { // 如果不是表型1或树型1参照!!,比如自定义,TABLE2,TREE2等
							rowobjs[j + 1] = new RefItemVO(str_value,
									str_value, str_value); // 参照VO
						}
					} else { // 如果是其他控件
						rowobjs[j + 1] = str_value;
					}
				} else { // 如果没取到数,则为空!
					rowobjs[j + 1] = null;
				}

				map_cache.put(str_key, rowobjs[j + 1]); // 往缓存中送入
			} // 一行数据中的各列全部处理处理结束!!!

			valueData[i] = rowobjs; //
		} // 所有行数据处理结束!!

		Object[][] objs_formuls = execLoadformula(valueData, _templetVO,
				_clientEnv); // 执行加载公式!!...
		return objs_formuls; //
	} //

    /**
     * 执行加载公式!!!
     *
     * @param _data
     * @param _templetVO
     * @param _env
     * @return
     */
    private Object[][] execLoadformula(Object[][] _data, Pub_Templet_1VO _templetVO, NovaClientEnvironment _env) {
        if (_data == null) {
            return null;
        }

        int li_length = _templetVO.getItemKeys().length; // 总共有多少列,加上行号,还应多一列!!
        Pub_Templet_1_ItemVO[] itemVos = _templetVO.getItemVos(); // 各子项.
        String[] itemKeys = _templetVO.getItemKeys(); // 各项的key
        String[] itemTypes = _templetVO.getItemTypes(); // 各项的类型

        for (int i = 0; i < _data.length; i++) {
            HashMap mapRowCache = new HashMap();
            for (int j = 0; j < li_length; j++) {
                mapRowCache.put(itemKeys[j], _data[i][j + 1]); // 先一下子在缓存中加入数据
            }

            for (int j = 0; j < li_length; j++) {
                String str_loadformula_desc = itemVos[j].getLoadformula(); //
                if (str_loadformula_desc != null && !str_loadformula_desc.trim().equals("")) {
                    String[] str_loadformulas = str_loadformula_desc.trim().split(";");
                    for (int k = 0; k < str_loadformulas.length; k++) { // 遍历公式!!
                        String[] str_execResult = dealFormula(str_loadformulas[k], _env, mapRowCache); // 真正执行公式!!!
                        mapRowCache.put(str_execResult[0], str_execResult[1]); // 在缓存中加入!!!
                        int li_pos = findItemPos(itemVos, str_execResult[0]); // 去页面是找位置
                        if (li_pos >= 0) { // 看公式前辍是不是在模板中定义的,如果是
                            String str_type = findItemType(itemVos, str_execResult[0]); // 看是什么类型
                            if (str_type.equals("文本框")) { // 如果是文本框,则直接赋值,绝大多数情况是这样!!!
                                _data[i][li_pos + 1] = str_execResult[1];
                            } else if (str_type.equals("下拉框")) { // 这里其实还要判断参照类型,如果是Table则还要自动根据对照的SQL取数!!!
                                _data[i][li_pos +
                                    1] = new ComBoxItemVO(str_execResult[1], str_execResult[1], str_execResult[1]);
                            } else if (str_type.equals("参照")) {
                                _data[i][li_pos +
                                    1] = new RefItemVO(str_execResult[1], str_execResult[1], str_execResult[1]);
                            } else {
                                _data[i][li_pos + 1] = str_execResult[1];
                            }
                        }
                    }
                }
            }

        }
        return _data;
    }
    //获取下拉框内容
    public ComBoxItemVO[] resetConditionComBoxItemvo(Pub_Templet_1_ItemVO itemVOS,NovaClientEnvironment _clientEnv){
    	if(itemVOS==null)
    		return null;
    	 ComBoxItemVO[] comBoxItemVOs = null;
    	 if (itemVOS.getConditionComboxDesc() != null && !itemVOS.getConditionComboxDesc().trim().equals("")) {
             String str_comboxdesc = itemVOS.getConditionComboxDesc().trim();
             String[] str_array = str_comboxdesc.split(";");

             String str_comboxitem_sql = str_array[0]; //
             String str_datasourcename = null;
             FrameWorkTBUtil tbUtil = new FrameWorkTBUtil(); // 创建工具
             if (str_array.length == 1) {
                 str_datasourcename = _clientEnv.getDefaultDatasourceName(); // 默认数据源!!
             } else if (str_array.length == 2) {
                 str_datasourcename = tbUtil.convertExpression(_clientEnv, getDataSourceName(str_array[1]));
             }

             try { //
                 String modify_str = null; //
                 try {
                     modify_str = new FrameWorkTBUtil().convertExpression(_clientEnv,str_comboxitem_sql); //
                 } catch (Exception ex) {
                     logger.error("[" + itemVOS.getItemkey() +"][" + itemVOS.getItemname() + "]下拉框定义的SQL[" + str_comboxitem_sql + "]转换失败！",ex);                     
                 }

                 if (modify_str != null) { // 如果成功转换!!
                     HashVO[] hvs = null;
                     try {
                         //System.out.println("准备执行下拉框[" + templet1VO.getTempletcode() + "][" + itemVOS[i].getItemkey() + "][" + itemVOS[i].getItemname() + "]的转换后的SQL[" + str_datasourcename + "][" + modify_str + "]");
                         hvs = new CommDMO().getHashVoArrayByDS(str_datasourcename, modify_str); // 根据下拉框中的SQL取得下拉框中所有数据的HashVO,可能会有异常!!
                     } catch (Exception ex) {
                    	 logger.error("执行下拉框[" + itemVOS.getItemname() + "]定义转换后SQL[" + str_datasourcename + "][" + modify_str + "]失败！原因:" + ex.getMessage(),ex);                         
                     }

                     if (hvs != null) {
                          comBoxItemVOs = new ComBoxItemVO[hvs.length];
                         for (int j = 0; j < hvs.length; j++) {
                             comBoxItemVOs[j] = new ComBoxItemVO(hvs[j]); //
                         }
                         itemVOS.setConditionComBoxItemVos(comBoxItemVOs); // 设置下拉框数据!!!!!
                     }
                 }
             } catch (Exception ex) {
            	 logger.error("[" + itemVOS.getItemname() + "]下拉框定义SQL[" + str_comboxitem_sql + "]有问题！",ex); //
             }
         }
    	 return comBoxItemVOs;
    }
    /**
     * 获取下拉框数据
     * @param itemVOS
     * @param _clientEnv
     * @return
     */
    public ComBoxItemVO[] resetComBoxItemvo(Pub_Templet_1_ItemVO itemVOS,NovaClientEnvironment _clientEnv){
    	if(itemVOS==null)
    		return null;
    	 ComBoxItemVO[] comBoxItemVOs = null;
    	 if (itemVOS.getComboxdesc() != null && !itemVOS.getComboxdesc().trim().equals("")) {
             String str_comboxdesc = itemVOS.getComboxdesc().trim();
             if(str_comboxdesc.startsWith("synref:")){
            	 //add by xuzhilin 20111207 支持同义词引用
            	 String syncode = str_comboxdesc.substring(7);
            	 String sql = "select content from pub_metadata_templet where code='MT_SYNONYMS'";
            	 try {
					HashVO[] vos = new CommDMO().getHashVoArrayByDS(null, sql);
					if(vos.length == 0){
						NovaLogger.getLogger(this.getClass()).error("无法查询到同义词信息");
						return null;
					}
					String content = vos[0].getStringValue(0);
					Document doc = DocumentHelper.parseText(content);
					for(Object o : doc.getRootElement().elements()){
						Element e = (Element) o;
						if(syncode.equals(e.attributeValue("code"))){
							comBoxItemVOs = new ComBoxItemVO[e.elements().size()];
							int i = 0;
							for(Object o2 : e.elements()){
								Element v = (Element) o2;
								ComBoxItemVO vo = new ComBoxItemVO(v.attributeValue("value"),v.attributeValue("name"),v.attributeValue("name"));
								comBoxItemVOs[i] = vo;
								i++;
							}
							itemVOS.setComBoxItemVos(comBoxItemVOs);
							return comBoxItemVOs;
						}
					}
					return null;
					
				} catch (Exception e) {
					NovaLogger.getLogger(this.getClass()).error(e);
					return null;
				}
             }
             String[] str_array = str_comboxdesc.split(";");

             String str_comboxitem_sql = str_array[0]; //
             String str_datasourcename = null;
             FrameWorkTBUtil tbUtil = new FrameWorkTBUtil(); // 创建工具
             if (str_array.length == 1) {
                 str_datasourcename = _clientEnv.getDefaultDatasourceName(); // 默认数据源!!
             } else if (str_array.length == 2) {
                 str_datasourcename = tbUtil.convertExpression(_clientEnv, getDataSourceName(str_array[1]));
             }

             try { //
                 String modify_str = null; //
                 try {
                     modify_str = new FrameWorkTBUtil().convertExpression(_clientEnv,str_comboxitem_sql); //
                 } catch (Exception ex) {
                     System.out.println("[" + itemVOS.getItemkey() +"][" + itemVOS.getItemname() + "]下拉框定义的SQL[" + str_comboxitem_sql + "]转换失败!!!");
                     ex.printStackTrace();
                 }

                 if (modify_str != null) { // 如果成功转换!!
                     HashVO[] hvs = null;
                     try {
                         //System.out.println("准备执行下拉框[" + templet1VO.getTempletcode() + "][" + itemVOS[i].getItemkey() + "][" + itemVOS[i].getItemname() + "]的转换后的SQL[" + str_datasourcename + "][" + modify_str + "]");
                         hvs = new CommDMO().getHashVoArrayByDS(str_datasourcename, modify_str); // 根据下拉框中的SQL取得下拉框中所有数据的HashVO,可能会有异常!!
                     } catch (Exception ex) {
                         System.out.println("执行下拉框[" + itemVOS.getItemname() + "]定义转换后SQL[" +
                             str_datasourcename + "][" + modify_str + "]失败!!原因:" + ex.getMessage());
                         ex.printStackTrace();
                     }

                     if (hvs != null) {
                          comBoxItemVOs = new ComBoxItemVO[hvs.length];
                         for (int j = 0; j < hvs.length; j++) {
                             comBoxItemVOs[j] = new ComBoxItemVO(hvs[j]); //
                         }
                         itemVOS.setComBoxItemVos(comBoxItemVOs); // 设置下拉框数据!!!!!
                     }
                 }
             } catch (Exception ex) {
                 System.out.println("[" + itemVOS.getItemname() + "]下拉框定义SQL[" + str_comboxitem_sql +
                                    "]有问题!!!"); //
                 ex.printStackTrace();
             }
         }
    	 return comBoxItemVOs;
    }
    /**
     * 真正执行某一个公式..使用JEP去执行!!
     *
     * @param _formula
     */
    private String[] dealFormula(String _formula, NovaClientEnvironment _env, HashMap _map) {
        String str_formula = replaceAll(_formula, " ", "");
        int li_pos = str_formula.indexOf("=>");
        String str_prefix = str_formula.substring(0, li_pos);
        String str_subfix = str_formula.substring(li_pos + 2, str_formula.length());

        String str_subfix_new = str_subfix;
        try {
            str_subfix_new = getModifySqlUtil().convertMacroStr(str_subfix, _env, _map); // 新的公式,即过转换过后的公式
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        String str_return = "";
        if (getLoadFormulaCache().containsKey(str_subfix_new)) { //看缓存中有没有计算过的值,如果有则直接返回!!!这个缓存处理非常重要!!!对性能优化极大!
            str_return = (String) getLoadFormulaCache().get(str_subfix_new); //直接返回!!
        } else {
            Object obj = getJepParse().execFormula(str_subfix_new); //执行公式!!!!
            if (obj == null) {
                str_return = "";
            } else {
                str_return = "" + obj;
            }
            getLoadFormulaCache().put(str_subfix_new, str_return); //
        }

        //System.out.println("加载公式项:[" + str_formula + "],前辍:[" + str_prefix + "],后辍:[" + str_subfix + "],宏代码转换后的后辍的:[" + str_subfix_new + "],JEP运算结果:[" + str_return + "]"); //
        return new String[] {str_prefix, str_return}; //
    }

    private int findItemPos(Pub_Templet_1_ItemVO[] itemVos, String _itemkey) {
        for (int i = 0; i < itemVos.length; i++) {
            if (itemVos[i].getItemkey().equalsIgnoreCase(_itemkey)) {
                return i;
            }
        }
        return -1;
    }

    private String findItemType(Pub_Templet_1_ItemVO[] itemVos, String _itemkey) {
        for (int i = 0; i < itemVos.length; i++) {
            if (itemVos[i].getItemkey().equalsIgnoreCase(_itemkey)) {
                return itemVos[i].getItemtype();
            }
        }
        return null;
    }

    /**
     * 要到匹配的ComBoxItemVO
     *
     * @param _vos
     * @param _id
     * @return
     */
    private ComBoxItemVO findComBoxItemVO(ComBoxItemVO[] _vos, String _id) {
        for (int k = 0; k < _vos.length; k++) {
            if (_vos[k].getId().equals(_id)) {
                return _vos[k];
            }
        }
        return null;
    }

    public Object[][] getQueryDataByDS(String _datasourcename, String _sql, Pub_Templet_1VO _templetVO,
                                       NovaClientEnvironment _clientEnv) throws Exception {
        CommDMO commDMO = new CommDMO(); //
        int li_length = _templetVO.getItemKeys().length; // 总共有多少列,加上行号,还应多一列!!
        Pub_Templet_1_ItemVO[] itemVos = _templetVO.getItemVos(); // 各子项.
        String[] itemKeys = _templetVO.getItemKeys(); // 各项的key
        String[] itemTypes = _templetVO.getItemTypes(); // 各项的类型

        HashVO[] hashVOs = commDMO.getHashVoArrayByDS(_datasourcename, _sql); // 取得真正的数据

        Object[][] valueData = new Object[hashVOs.length][li_length]; // 创建数据对象!!!,比模板多一列是行号,永远在第一列!!

        //HashMap hm_RefItemVos = new HashMap(); // 一下子为各个参照灌入所有的参照数据!!!
        for (int i = 0; i < hashVOs.length; i++) { // 遍历各行
            Object[] rowobjs = new Object[li_length]; // 一行的数据
            HashMap map_cache = new HashMap(); // 一行数据中的缓存!!!其Value可能是String,ComBoxItemVO,RefItemVO

            // 首先一下子将所有值灌入....
            String[] str_realvalues = new String[li_length]; //
            for (int j = 0; j < li_length; j++) { // 遍历各列
                String str_key = itemKeys[j];
                String str_type = itemTypes[j]; //
                if (str_type.equals("日历")) {
                    str_realvalues[j] = hashVOs[i].getStringValueForDay(str_key); //
                } else if (str_type.equals("时间")) {
                    str_realvalues[j] = hashVOs[i].getStringValueForSecond(str_key); //
                } else {
                    str_realvalues[j] = hashVOs[i].getStringValue(str_key); //
                }

                if (str_realvalues[j] != null) {
                    if (itemTypes[j].equals("文本框")) { // 如果是文本框
                        map_cache.put(str_key, str_realvalues[j]);
                    } else if (itemTypes[j].equals("下拉框")) {
                        map_cache.put(str_key, new ComBoxItemVO(str_realvalues[j], str_realvalues[j], str_realvalues[j]));
                    } else if (itemTypes[j].equals("参照")) {
                        map_cache.put(str_key, new RefItemVO(str_realvalues[j], str_realvalues[j], str_realvalues[j]));
                    } else {
                        map_cache.put(str_key, str_realvalues[j]); //
                    }
                }
            }
            // 然后再处理每一列的值
            for (int j = 0; j < li_length; j++) { // 遍历各列
                String str_key = itemKeys[j];
                String str_type = itemTypes[j];
                String str_value = null;
                if (str_type.equals("日历")) {
                    str_value = hashVOs[i].getStringValueForDay(str_key); //
                } else if (str_type.equals("时间")) {
                    str_value = hashVOs[i].getStringValueForSecond(str_key); //
                } else {
                    str_value = hashVOs[i].getStringValue(str_key); //
                }
                if (str_value != null) {
                    if (itemTypes[j].equals("文本框")) { // 如果是文本框
                        rowobjs[j] = str_value;
                    } else if (itemTypes[j].equals("下拉框")) { // 如果是下拉框..
                        ComBoxItemVO[] comBoxItemVos = itemVos[j].getComBoxItemVos();
                        ComBoxItemVO matchVO = findComBoxItemVO(comBoxItemVos, str_value); // //..
                        if (matchVO != null) {
                            rowobjs[j] = matchVO;
                        } else {
                            rowobjs[j] = new ComBoxItemVO(str_value, str_value, str_value); // 下拉框VO
                        }
                    } else if (itemTypes[j].equals("参照")) {
                        if (itemVos[j].getRefdesc_type().equalsIgnoreCase("TABLE") ||
                            itemVos[j].getRefdesc_type().equalsIgnoreCase("TREE")) { // 如果是表或者树
                            String str_refsql = itemVos[j].getRefdesc_realsql(); //
                            if (str_refsql != null && !str_refsql.trim().equals("")) {
                                String modify_str = null; //
                                try {
                                    modify_str = new FrameWorkTBUtil().convertFormulaMacPars(str_refsql, _clientEnv, map_cache); // !!!!得到转换后的SQL,即直接可以执行的SQL,即将其中的{aaa}进行转换,!!!这是关键
                                } catch (Exception ex) {
                                    System.out.println("转换参照[" + _templetVO.getTempletcode() + "][" +
                                        itemVos[j].getItemkey() + "][" + itemVos[j].getItemname() + "]参照定义SQL[" +
                                        str_refsql + "]失败!!!");
                                    ex.printStackTrace(); //
                                }

                                if (modify_str != null) { // 如果成功转换SQL!!
                                    FrameWorkTBUtil tbUtil = new FrameWorkTBUtil();
                                    modify_str = StringUtil.replaceAll(modify_str, "1=1",
                                        itemVos[j].getRefdesc_firstColName() + "='" + str_value + "'"); // 将SQL语句中的1=1替换成..
                                    System.out.println("开始执行参照定义转换后的SQL:" + modify_str); //
                                    HashVO[] ht_allRefItemVOS = null;
                                    try {
                                        ht_allRefItemVOS = commDMO.getHashVoArrayByDS(_datasourcename, modify_str); // 执行SQL!!!可能会抛异常!!
                                    } catch (Exception ex) {
                                        System.out.println("执行参照[" + _templetVO.getTempletcode() + "][" +
                                            itemVos[j].getItemkey() + "][" + itemVos[j].getItemname() + "]转换后SQL[" +
                                            modify_str + "]失败,原因:" + ex.getMessage()); //
                                        ex.printStackTrace();
                                    }

                                    if (ht_allRefItemVOS != null) { // 如果取得到数据!!
                                        boolean bo_iffindref = false;
                                        for (int pp = 0; pp < ht_allRefItemVOS.length; pp++) { // 遍历去找!!!
                                            if (str_value.equals(ht_allRefItemVOS[pp].getStringValue(0))) {
                                                rowobjs[j] = new RefItemVO(ht_allRefItemVOS[pp]); //
                                                bo_iffindref = true; // 如果非常幸运的找到品配的了!!!!!!!!!
                                                break;

                                            }
                                        }
                                        if (!bo_iffindref) { // 如果没找到品配的,则直接创建参照VO
                                            rowobjs[j] = new RefItemVO(str_value, str_value, str_value); // 参照VO
                                        }
                                    } else
                                    // 如果执行SQL取数失败!!
                                    {
                                        rowobjs[j] = new RefItemVO(str_value, str_value, str_value); // 参照VO
                                    }
                                } else { // 如果转换SQL失败
                                    rowobjs[j] = new RefItemVO(str_value, str_value, str_value); // 参照VO
                                }
                            } else {
                                rowobjs[j] = new RefItemVO(str_value, str_value, str_value); // 参照VO
                            }
                        } else { // 如果不是表型1或树型1参照!!,比如自定义,TABLE2,TREE2等
                            rowobjs[j] = new RefItemVO(str_value, str_value, str_value); // 参照VO
                        }
                    } else { // 如果是其他控件
                        rowobjs[j] = str_value;
                    }
                } else { // 如果没取到数,则为空!
                    rowobjs[j] = null;
                }

                map_cache.put(str_key, rowobjs[j]); // 往缓存中送入
            } // 一行数据中的各列全部处理处理结束!!!

            valueData[i] = rowobjs; //
        } // 所有行数据处理结束!!

        return valueData; //
    } //

    //提交BillVO进行数据库操作!!
    public void commitBillVO(String _dsName, BillVO[] _deleteVOs, BillVO[] _insertVOs, BillVO[] _updateVOs) throws
        Exception {
        CommDMO commDMO = new CommDMO(); //
        String[] str_sql_deletes = null;
        if (_deleteVOs != null && _deleteVOs.length > 0) {
            str_sql_deletes = new String[_deleteVOs.length]; //
            for (int i = 0; i < str_sql_deletes.length; i++) {
                str_sql_deletes[i] = _deleteVOs[i].getDeleteSQL(); //
            }
        }

        String[] str_sql_inserts = null;
        if (_insertVOs != null && _insertVOs.length > 0) {
            str_sql_inserts = new String[_insertVOs.length]; //
            for (int i = 0; i < str_sql_inserts.length; i++) {
                str_sql_inserts[i] = _insertVOs[i].getInsertSQL(); //
            }
        }

        String[] str_sql_updates = null;
        if (_updateVOs != null && _updateVOs.length > 0) {
            str_sql_updates = new String[_updateVOs.length]; //
            for (int i = 0; i < str_sql_updates.length; i++) {
                str_sql_updates[i] = _updateVOs[i].getUpdateSQL(); //
            }
        }

        if (str_sql_deletes != null) {
            commDMO.executeBatchByDS(_dsName, str_sql_deletes); //先删除!!
        }

        if (str_sql_inserts != null) {
            commDMO.executeBatchByDS(_dsName, str_sql_inserts); //后插入!!
        }

        if (str_sql_updates != null) {
            commDMO.executeBatchByDS(_dsName, str_sql_updates); //最后修改!!
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
    private String replaceAll(String str_par, String old_item, String new_item) {
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

        return str_new; // 取得数据源名称
    }

    private ModifySqlUtil getModifySqlUtil() {
        if (modifySqlUtil == null) {
            modifySqlUtil = new ModifySqlUtil();
        }
        return modifySqlUtil; //
    }

    private JepFormulaParse getJepParse() {
        if (jepParse == null) {
            jepParse = new JepFormulaParse(JepFormulaParse.li_bs);
        }
        return jepParse;
    }

    //参照缓存
    private HashMap getRefCache() {
        if (refCache == null) {
            refCache = new HashMap();
        }
        return refCache;
    }

    //加载公式缓存!!
    private HashMap getLoadFormulaCache() {
        if (mapLoadFormulaCache == null) {
            mapLoadFormulaCache = new HashMap();
        }
        return mapLoadFormulaCache;
    }
    private Object[][] calculateBillVOValues(Pub_Templet_1VO templetvo,HashVO hashvo)
    {
    	Object[] data_obj = new Object[templetvo.getItemVos().length+1];
    	 RowNumberItemVO rowNumberVO = new RowNumberItemVO(); // 行号VO
         rowNumberVO.setState(NovaConstants.BILLDATAEDITSTATE_INIT); //
         rowNumberVO.setRecordIndex(1); // 记录号
    	data_obj[0]=rowNumberVO;
		for (int i = 1; i < data_obj.length; i++) {
			String itemkey = templetvo.getItemVos()[i-1].getItemkey();
			String itemtype = templetvo.getItemVos()[i-1].getItemtype();
			data_obj[i] = hashvo.getObjectValue(itemkey);
			if(data_obj[i]==null)
				continue;
			if(itemtype.equals("下拉框"))
			{
				data_obj[i]= hashvo.getIntegerValue(itemkey);
				String value = data_obj[i].toString();
				data_obj[i] = new ComBoxItemVO(value,value,value);
			}else if(itemtype.equals("参照"))
			{
				data_obj[i]= hashvo.getIntegerValue(itemkey);
				String value = data_obj[i].toString();
				data_obj[i] = new RefItemVO(value,value,value);
			}else if (itemtype.equals("日历")) {
				data_obj[i]= hashvo.getStringValueForDay(itemkey);
            }else if (itemtype.equals("时间")) {
            	data_obj[i]= hashvo.getStringValueForSecond(itemkey);
            } else
            {
            	if(data_obj[i] instanceof java.math.BigDecimal)
            		data_obj[i]=((java.math.BigDecimal)data_obj[i]).doubleValue()+"";
            }
		}
    	return new Object[][]{data_obj};
    }
    
    /**
     * 实体引用SQL
     * @param entity 实体XML
     * @param content 领域实体XML
     * @param att 实体引用属性
     * @return
     */
    private String getRefEntitySql(Element entity,Element content,Element att) throws Exception{
    	StringBuilder sqlBuf = new StringBuilder("select ");
    	String refEntityCode = att.attributeValue("refEntity");
		Element refEntity = null;
		for(Object entityObj : content.element("entities").elements("entity")){
			if(refEntityCode.equals(((Element)entityObj).attributeValue("code"))){
				refEntity = (Element)entityObj;
				break;
			}
		}
		List<Element> refEntitiesList = getAllInheritEntity(refEntity,content);
		sqlBuf.append(att.attributeValue("refEntityAtt")).append(" ID#,").append(att.attributeValue("refEntityShowAtt"))
		.append(" 编码#,").append(att.attributeValue("refEntityShowAtt")).append(" 名称").append(" from (").append(getFetchSql(refEntitiesList))
		.append(")");
    	return sqlBuf.toString();
    }
    
    /**
	 * 得到所有有父子关系的实体(不包括抽象实体和虚拟实体)
	 * @param entity
	 * @return
	 */
	private List<Element> getAllInheritEntity(Element entity,Element content){
		List<Element> entitiesList = new ArrayList<Element>();
		entitiesList.add(entity);
		String parentEntityCode = entity.attributeValue("parentEntityCode");
		if (parentEntityCode != null && !"".equals(parentEntityCode)){
			entitiesList = recursiveAllInheritEntity(parentEntityCode,entitiesList,content);
			Collections.reverse(entitiesList);//对所有实体排序 （祖父-父-子-孙）
		}
		return entitiesList;
	}
	
	/**
	 * 递归查询当前实体的所有的父级实体
	 * 
	 * @param parentEntityCode
	 * @param entities
	 * @param attributes
	 */
	private List<Element> recursiveAllInheritEntity(String parentEntityCode,List<Element> entitiesList,Element content) {
		for (Object obj : content.element("entities").elements("entity")) {
			Element entity = (Element) obj;
			if ("false".equals(entity.attributeValue("isAbstract"))
					&& parentEntityCode.equals(entity.attributeValue("code"))) {
				entitiesList.add(entity);
				recursiveAllInheritEntity(entity.attributeValue("parentEntityCode"),entitiesList,content);
			}
		}
		return entitiesList;
	}
	
	/**
	 * 解析实体生成读取语句
	 * @param entitiesList 所有有继承关系的实体
	 * @return
	 * @throws Exception
	 */
	private String getFetchSql(List<Element> entitiesList) throws Exception {
		String sqlStr = "";
		String pkAttribute = "";// 主键属性
		String entityCode = "";//实体编码
		Map<String,String> atts = new HashMap<String,String>();
		for (Element entity : entitiesList) {
			StringBuilder sql = new StringBuilder("select ");
			String pkColumn = "";//主键字段
			entityCode = entity.attributeValue("code");
			if(!"".equals(pkAttribute)){//如果有主键属性，说明有父实体
				for(Entry<String,String> attName : atts.entrySet()){
					sql.append(entity.attributeValue("parentEntityCode")).append(".").append(attName.getValue())
					.append(" ").append(attName.getKey()).append(",");
				}
			}
			Element mappingInfo =  entity.element("mappingInfo");
			String filter = mappingInfo.elementText("filter");
			Map<String,String> tempMap = new HashMap<String, String>();
			for (Object mapObj :mappingInfo.element("attributeMapping").elements("map")) {
				Element map = (Element) mapObj;
				if(entity.attributeValue("idAttributeName").equals(map.attributeValue("attributeName")))
					pkColumn = map.attributeValue("columnName");
				tempMap.put(map.attributeValue("attributeName"), map.attributeValue("columnName"));
			}
			Element attributeInfo = entity.element("attributes");
			for(Object attObj : attributeInfo.elements("attribute")){
				Element att = (Element) attObj;
				if("true".equals(att.attributeValue("isCompute"))){//计算属性
					sql.append("(").append(att.attributeValue("computeExpr")).append(")")
					.append(" ").append(att.attributeValue("name")).append(",");
				}else{
					sql.append(entityCode).append(".").append(tempMap.get(att.attributeValue("name")))
					.append(" ").append(att.attributeValue("name")).append(",");
				}
				atts.put(att.attributeValue("name"), att.attributeValue("name"));
			}

			if("".equals(pkColumn))
				throw new Exception("实体["+entityCode+"]缺少主键字段，无法生成SQL！");
			sql = new StringBuilder(sql.substring(0, sql.length()-1)).append(" from ");
			if("table".equals(mappingInfo.attributeValue("type"))){
				sql.append("(select * from ").append(mappingInfo.elementText("tableName")).append(" where 1=1");
				if(filter != null && !"".equals(filter.trim()))
					sql.append(" and ").append(filter);
				sql.append(") ").append(entityCode);
			}else if("queryView".equals(mappingInfo.attributeValue("type"))){
				String viewCode = mappingInfo.attributeValue("queryView").split("@@")[0];
				CommDMO dmo = new CommDMO();
				String queryViewSql = "select sql from bam_queryview where code = '"
						+ viewCode + "'";
				try{
					HashVO[] vos = dmo.getHashVoArrayByDS(null, queryViewSql);
					if (vos.length > 0){
						sql.append("(select * from (").append(vos[0].getStringValue("sql")).append(") where 1=1");
						if(filter != null && !"".equals(filter.trim()))
							sql.append(" and ").append(filter);
						sql.append(") ").append(entityCode);
					}else 
						throw new Exception("未找到查询视图[" + viewCode + "]对应的sql语句");
				}catch(Exception e){
					throw e;
				}finally{
					dmo.releaseContext(null);
				}
			}
			if(!"".equals(pkAttribute)){
				sql.append(",(").append(sqlStr).append(") ").append(entity.attributeValue("parentEntityCode"))
				.append(" where ").append(entityCode).append(".").append(pkColumn).append("=").append(entity.attributeValue("parentEntityCode"))
				.append(".").append(pkAttribute);
			}else
				sql.append(" where 1=1 ");
			
			pkAttribute = entity.attributeValue("idAttributeName");
			sqlStr = sql.toString();
		}
		return sqlStr;
	}
    
}
