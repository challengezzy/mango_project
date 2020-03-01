/**************************************************************************
 * $RCSfile: UIUtil.java,v $  $Revision: 1.11.2.10 $  $Date: 2010/01/20 09:44:52 $
 **************************************************************************/
package smartx.framework.common.ui;

import java.awt.Color;
import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;

import javax.swing.*;
import javax.swing.tree.*;

import smartx.framework.common.utils.StringUtil;
import smartx.framework.common.utils.Sys;
import smartx.framework.common.vo.*;
import smartx.framework.metadata.ui.*;
import smartx.framework.metadata.vo.*;


/**
 * 表现层工具类
 * @author James.W
 *
 */
public class UIUtil implements Serializable {

    private static final long serialVersionUID = -2128627981610471571L;

    //远程服务 提供数据库和文件上传等等接口
    private static FrameWorkCommService getService() throws NovaRemoteException, Exception {
        FrameWorkCommService service = (FrameWorkCommService) NovaRemoteServiceFactory.getInstance().lookUpService(
            FrameWorkCommService.class);
        return service;
    }

    //远程服务 提供元原模板相关数据
    private static FrameWorkMetaDataService getMetaDataService() throws NovaRemoteException, Exception {
        FrameWorkMetaDataService service = (FrameWorkMetaDataService) NovaRemoteServiceFactory.getInstance().
            lookUpService(FrameWorkMetaDataService.class);
        return service;
    }

    /**
     * 通用方法..
     *
     * @param _className
     * @param _functionName
     * @param _parMap
     * @return
     * @throws Exception
     */
    public synchronized static HashMap commMethod(String _className, String _functionName, HashMap _parMap) throws
        Exception {
        return getService().commMethod(_className, _functionName, _parMap);
    }

    /**
     * 获得服务端环境变量
     * @return HashMap
     * @throws NovaRemoteException
     * @throws Exception
     */
    public static HashMap getEnvironmentParam() throws NovaRemoteException, Exception {
    	HashMap rt=null;
    	if(Sys.getInfo("_EnvironmentParam")==null){
            rt=getService().getEnvironmentParam();
            Sys.putInfo("_EnvironmentParam", rt);
    	}else{
    		rt=(HashMap)Sys.getInfo("_EnvironmentParam");
    	}
    	return rt;
    }
    
    /**
     * 获得服务端环境变量
     * @param p
     * @return String
     * @throws NovaRemoteException
     * @throws Exception
     */
    public static String getEnvironmentParamStr(String p) throws NovaRemoteException, Exception {
    	String rt=(String)Sys.getInfo(p);
    	if(rt==null){
    		rt=getService().getEnvironmentParamStr(p);
    	}
    	Sys.putInfo(p, rt);
    	return rt;    	
    }
    
    /**
     * 获得服务端环境变量，如果发生错误就取默认值
     * @param p
     * @return String
     * @throws NovaRemoteException
     * @throws Exception
     */
    public static String getEnvironmentParamStr(String p, String defValue) {
    	String rt=null;
    	try{
    		rt=getEnvironmentParamStr(p);
    	}catch(Exception e){
    		rt=defValue;
    	}
    	return rt==null?defValue:rt;    	
    }
    
    /**
     * 创建颜色取值
     * @param set
     * @return
     */
    public static Color getColor(String set){
    	if(set==null)return null;
    	//#000000
    	if(set.startsWith("#")){
    		int c=StringUtil.parseIntHex(set.substring(1), 0);
    		return new Color(c);
    	}
    	
    	String[] cs=set.split(",");
    	int[] ci=new int[cs.length];
    	for(int i=0;i<ci.length;i++){
    		ci[i]=StringUtil.parseInt(cs[i],0);
    	}
    	switch (cs.length){
    		case 1 :
    			return new Color(ci[0]);
    		case 3 :
    			return new Color(ci[0],ci[1],ci[2]);
    		case 4 :
    			return new Color(ci[0],ci[1],ci[2],ci[3]);
    		default :
    			return null;
    	}
    }
    
    /**
     * 创建一组颜色
     * @param set
     * @return
     */
    public static Color[] getColors(String[] sets){
    	if(sets==null)return new Color[0];
    	Color[] rt=new Color[sets.length];
    	for(int i=0;i<sets.length;i++){
    		rt[i]=getColor(sets[i]);
    	}
    	return rt;    	
    }
    
    /**
     * 取得一个图片
     *
     * @param path
     * @return
     */
    public synchronized static ImageIcon getImage(String path) {
    	return getImage(path,null);
    }
    /**
     * 取得一个图片
     *
     * @param path
     * @param desc 图片解释
     * @return
     */
    public synchronized static ImageIcon getImage(String path,String desc) {
    	if(path==null) return null;    	
        try {
            URL urlImage = UIUtil.class.getClassLoader().getResource(path);
            if(desc!=null){
                return new ImageIcon(urlImage,desc);
            }else{
            	return new ImageIcon(urlImage);
            }
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * 获得本地日期
     * @return
     */
    public synchronized static String getCurrDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String str_date = sdf.format(new Date());
        return str_date;
    }
    
    /**
     * 获得本地日期时间
     * @return
     */

    public synchronized static String getCurrTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str_date = sdf.format(new Date());
        return str_date;
    }

    /**
     * 获得服务器端所有的图标文件的文件名
     *
     * @return
     * @throws NovaRemoteException
     * @throws Exception
     */
    public synchronized static String[] getImageFileNames() throws NovaRemoteException, Exception {
        return getService().getImageFileNames();
    }

    //	远程取数,如果_datasourcename为null,则从默认数据源取数!
    public synchronized static String getSequenceNextValByDS(String _datasourcename, String _sequenceName) throws
        NovaRemoteException, Exception {
        return getService().getSequenceNextValByDS(_datasourcename, _sequenceName);
    }

    //远程取数,如果_datasourcename为null,则从默认数据源取数!
    public synchronized static String[][] getStringArrayByDS(String _datasourcename, String _sql) throws
        NovaRemoteException, Exception {
        return getService().getStringArrayByDS(_datasourcename, _sql);
    }

    //远程取数,如果_datasourcename为null,则从默认数据源取数!
    public synchronized static TableDataStruct getTableDataStructByDS(String _datasourcename, String _sql) throws
        NovaRemoteException, Exception {
        return getService().getTableDataStructByDS(_datasourcename, _sql);
    }

    //远程取数,如果_datasourcename为null,则从默认数据源取数!
    public synchronized static HashVO[] getHashVoArrayByDS(String _datasourcename, String _sql) throws
        NovaRemoteException, Exception {
        return getService().getHashVoArrayByDS(_datasourcename, _sql);
    }

    //远程取数,如果_datasourcename为null,则从默认数据源取数!
    public synchronized static Vector getHashVoArrayReturnVectorByMark(String _datasourcename, String[] _sqls) throws
        NovaRemoteException, Exception {
        return getService().getHashVoArrayReturnVectorByDS(_datasourcename, _sqls);
    }

    //远程取数,如果_datasourcename为null,则从默认数据源取数!
    public synchronized static HashMap getHashVoArrayReturnMapByMark(String _datasourcename, String[] _sqls,
        String[] _keys) throws NovaRemoteException, Exception {
        return getService().getHashVoArrayReturnMapByDS(_datasourcename, _sqls, _keys);
    }

    //
    
    /**
     * 在指定数据源上,执行一条数据库修改语句,比如insert,delete,update,如果_datasourcename为null,则操作默认数据源!
     * @param _dsName
     * @param _sql
     * @return int 涉及行数
     */
    public synchronized static int executeUpdateByDS(String _dsName, String _sql) throws NovaRemoteException,
        Exception {
        return getService().executeUpdateByDS(_dsName, _sql);
    }

    /**
     * 在指定数据源上,执行一批数据库修改语句,比如insert,delete,update,如果_datasourcename为null,则操作默认数据源!
     * @param _dsName
     * @param _sqls
     * @return int[] 涉及行数
     * @throws Exception
     */
    public synchronized static int[] executeBatchByDS(String _dsName, String[] _sqls) throws Exception {
        return getService().executeBatchByDS(_dsName, _sqls); //
    }

    /**
     * 在指定数据源上,执行一批数据库修改语句,比如insert,delete,update,如果_datasourcename为null,则操作默认数据源!
     * @param _dsName
     * @param _sqllist
     * @return int[] 涉及行数
     * @throws Exception
     */
    public synchronized static int[] executeBatchByDS(String _dsName, java.util.List _sqllist) throws Exception {
        return getService().executeBatchByDS(_dsName, _sqllist); //
    }

    //存储过程,如果_datasourcename为null,则操作默认数据源!
    public synchronized static void callProcedure(String _datasourcename, String procedureName, String[] parmeters) throws
        Exception {
        getService().callProcedureByDS(_datasourcename, procedureName, parmeters);
    }

    //存储过程,如果_datasourcename为null,则操作默认数据源!
    public synchronized static String callProcedureByReturn(String _datasourcename, String procedureName,
        String[] parmeters) throws Exception {
        return getService().callProcedureReturnStrByDS(_datasourcename, procedureName, parmeters);
    }

    //存储函数,如果_datasourcename为null,则操作默认数据源!
    public synchronized static String callFunctionByReturnVarchar(String _datasourcename, String procedureName,
        String[] parmeters) throws Exception {
        return getService().callFunctionReturnStrByDS(_datasourcename, procedureName, parmeters);
    }

    //存储函数,如果_datasourcename为null,则操作默认数据源!
    public synchronized static String[][] callFunctionByReturnTable(String _datasourcename, String functionName,
        String[] parmeters) throws Exception {
        return getService().callFunctionReturnTableByDS(_datasourcename, functionName, parmeters);
    }

    //取元数据配置信息
    public synchronized static Pub_Templet_1VO getPub_Templet_1VO(String _code) throws Exception {
        return getMetaDataService().getPub_Templet_1VO(_code, NovaClientEnvironment.getInstance());
    }

    //取元数据配置信息
    public synchronized static Pub_Templet_1VO getPub_Templet_1VO(HashVO _parentVO, HashVO[] _childVOs) throws
        Exception {
        return getMetaDataService().getPub_Templet_1VO(_parentVO, _childVOs, NovaClientEnvironment.getInstance()); //
    }

    //	取元数据配置信息
    public synchronized static Pub_QueryTempletVO getPub_QueryTempletVO(String _code) throws Exception {
        return getMetaDataService().getPub_QueryTempletVO(_code, NovaClientEnvironment.getInstance());
    }

    //取元数据配置信息
    public synchronized static Pub_QueryTempletVO getPub_QueryTempletVO(HashVO _parentVO, HashVO[] _childVOs) throws
        Exception {
        return getMetaDataService().getPub_QueryTempletVO(_parentVO, _childVOs, NovaClientEnvironment.getInstance()); //
    }

    //元数据--取卡片数据!,如果_datasourcename为null,则操作默认数据源!
    public synchronized static Object[] getBillCardDataByDS(String _dsName, String _sql, Pub_Templet_1VO _templetVO,
        NovaClientEnvironment _env) throws NovaRemoteException, Exception {
        return getMetaDataService().getBillCardDataByDS(_dsName, _sql, _templetVO, _env);
    }

    /**
     * 获得数据行数，
     * @param _ds
     * @param _sql
     * @param _templetVO
     * @param _env
     * @return int
     * @throws NovaRemoveException
     * @throws Exception
     */
    public synchronized static int getBillListRowCountByDS(String _ds,String _sql,
    	Pub_Templet_1VO _templetVO, NovaClientEnvironment _env) throws NovaRemoteException,Exception{
    	return getMetaDataService().getBillListRowCountByDS(_ds, _sql, _templetVO, _env);
    }    
    /**
     * 获取数据片段
     * @param _datasourcename
     * @param _sql
     * @param _templetVO
     * @param _env
     * @return
     * @throws NovaRemoteException
     * @throws Exception
     */
    public synchronized static Object[][] getBillListSubDataByDS(String _datasourcename, String _sql,int sRowNum,int eRowNum,
        Pub_Templet_1VO _templetVO, NovaClientEnvironment _env) throws NovaRemoteException, Exception {
        return getMetaDataService().getBillListSubDataByDS(_datasourcename, _sql,sRowNum,eRowNum, _templetVO, _env);
    }
    //元数据--取列表数据!,如果_datasourcename为null,则操作默认数据源!
    public synchronized static Object[][] getBillListDataByDS(String _datasourcename, String _sql,
        Pub_Templet_1VO _templetVO, NovaClientEnvironment _env) throws NovaRemoteException, Exception {
        return getMetaDataService().getBillListDataByDS(_datasourcename, _sql, _templetVO, _env);
    }
    

    //元数据--取查询模板数据!,如果_datasourcename为null,则操作默认数据源!
    public synchronized static Object[][] getQueryData(String _dsName, String _sql, Pub_Templet_1VO _templetVO,
        NovaClientEnvironment _env) throws NovaRemoteException, Exception {
        return getMetaDataService().getQueryDataByDS(_dsName, _sql, _templetVO, _env);

    }

    /**
     * 根据父亲主键构成树
     *
     * @param par_roottitle
     * @param par_tablename
     * @param par_key
     * @param par_code
     * @param par_name
     * @param par_coderule
     * @param par_wherecondition
     * @return
     */
    public synchronized static JTree getJTreeByParentPK(String _dsName, String par_roottitle, String par_tablename,
        String par_key, String par_code, String par_name, String par_parentpk, String par_wherecondition,
        String par_orderbycondition) {
        String str_sql = "select " + par_key + "," + par_code + "," + par_name + "," + par_parentpk + " from " +
            par_tablename + " where " + par_wherecondition + " " + par_orderbycondition;
        String[][] str_data;
        try {
            str_data = getService().getStringArrayByDS(_dsName, str_sql);
        } catch (Exception e) {
            return null;
        }
        DefaultMutableTreeNode node_root = new DefaultMutableTreeNode(par_roottitle); // 创建根结点
        CommBDTreeNodeHaveParentVO[] vos = new CommBDTreeNodeHaveParentVO[str_data.length]; // 创建所有数据数组
        for (int i = 0; i < vos.length; i++) {
            vos[i] = new CommBDTreeNodeHaveParentVO(str_data[i][0], str_data[i][1], str_data[i][2], str_data[i][3]); // 给每个结点的数据VO赋值
        }

        DefaultMutableTreeNode[] node_level_1 = new DefaultMutableTreeNode[vos.length]; // 创建所有结点数组
        for (int i = 0; i < vos.length; i++) {
            node_level_1[i] = new DefaultMutableTreeNode(vos[i]); // 创建各个结点
            node_root.add(node_level_1[i]); // 加入根结点
        }

        // 构建树
        for (int i = 0; i < node_level_1.length; i++) {
            CommBDTreeNodeHaveParentVO nodeVO = (CommBDTreeNodeHaveParentVO) node_level_1[i].getUserObject();
            // String str_pk = nodeVO.getPk(); //主键
            String str_pk_parentPK = nodeVO.getParentpk(); // 父亲主键
            for (int j = 0; j < node_level_1.length; j++) {
                CommBDTreeNodeHaveParentVO nodeVO_2 = (CommBDTreeNodeHaveParentVO) node_level_1[j].getUserObject();
                String str_pk_2 = nodeVO_2.getPk(); // 主键
                // String str_pk_parentPK_2 = nodeVO_2.getParentpk(); //父亲主键
                if (str_pk_2.equals(str_pk_parentPK)) { // 如果发现该结点主键正好是上层循环的父亲结点,则将基作为我的儿子处理加入
                    node_level_1[j].add(node_level_1[i]);
                }
            }
        }
        JTree aJTree = new JTree(new DefaultTreeModel(node_root));
        return aJTree;
    }
    
    public synchronized static JTree getJTreeByParentPK_HashVO( String _dsName, String par_roottitle, String _sql, String _keyname, String _parentkeyname ){
        String str_sql = _sql;
        HashVO[] hashVOs;
        try{
            hashVOs = getService().getHashVoArrayByDS( _dsName, rebuildLazyLoadTreeSQL(str_sql));
        }catch ( Exception e ){
            return null;
        }
        
        DefaultMutableTreeNode node_root = new DefaultMutableTreeNode( par_roottitle ); // 创建根结点

        DefaultMutableTreeNode[] node_level_1 = new DefaultMutableTreeNode[hashVOs.length]; // 创建所有结点数组
        Hashtable ht_node = new Hashtable(); //--使用Hashtable来避免第二次的循环
        for ( int i = 0; i < hashVOs.length; i++ )
        {
            node_level_1[i] = new DefaultMutableTreeNode( hashVOs[i] ); // 创建各个结点
//            node_root.add( node_level_1[i] ); // 加入根结点
            ht_node.put( hashVOs[i].getStringValue( _keyname ), node_level_1[i] ); //--此时不将所有的节点放入跟节点
        }

        // 构建树
        for ( int i = 0; i < node_level_1.length; i++ ){ // 构建树---使用Hashtable比进行循环效率要高
            HashVO nodeVO = ( HashVO ) node_level_1[i].getUserObject();
            String str_pk_parentPK = nodeVO.getStringValue( _parentkeyname ); // 父亲主键
            if ( str_pk_parentPK == null || str_pk_parentPK.equals( "" ) )
            {
                node_root.add( node_level_1[i] ); // 加入根结点
                continue;
            }else{
                DefaultMutableTreeNode parentNode = ( DefaultMutableTreeNode ) ht_node.get( str_pk_parentPK );
                if(parentNode==null){
                	node_root.add( node_level_1[i] ); // 加入根结点
                }else{
                	parentNode.add( node_level_1[i] );
                }
            }

        }

        JTree aJTree = new JTree( new DefaultTreeModel( node_root ) );

        return aJTree;
    }
    
    /**
     * 创建全加载树
     * @param _ds
     * @param _title
     * @param _sql
     * @param _key
     * @param _parentkey
     * @param _tree 树对象，空表示直接生成新的tree。
     * @return
     */    
    public synchronized static JTree getLoadedJTree( String _ds, String _title, String _sql, String _key, String _parentkey, JTree _tree)throws Exception{
        String str_sql = _sql;
        HashVO[] vos = getService().getHashVoArrayByDS( _ds, rebuildLazyLoadTreeSQL(str_sql));
        
        //创建根结点
    	DefaultMutableTreeNode root=null;
    	if(_tree==null){
    		root=new DefaultMutableTreeNode(_title);
    	}else{
    		root=(DefaultMutableTreeNode)_tree.getModel().getRoot();
    		root.removeAllChildren();
    	}
        
        Hashtable ht = new Hashtable();
        for ( int i=0; i<vos.length; i++ ){
            ht.put(vos[i].getStringValue(_key), new DefaultMutableTreeNode( vos[i] ) ); 
        }
        String[] keys=(String[])ht.keySet().toArray(new String[0]);

        for ( int i = 0; i < keys.length; i++ ){
        	DefaultMutableTreeNode node = (DefaultMutableTreeNode)ht.get(keys[i]);
            String str_parentPK = ((HashVO)node.getUserObject()).getStringValue( _parentkey ); // 父亲主键
            if ( str_parentPK == null || str_parentPK.equals( "" )){
            	root.add( node );                
            }else{
                DefaultMutableTreeNode parent = ( DefaultMutableTreeNode ) ht.get( str_parentPK );
                if(parent==null){
                	root.add( node );       
                }else{
                	parent.add( node );
                }
            }

        }
        
        if(_tree==null){
    		return new JTree( new DefaultTreeModel( root ));
    	}else{
    		_tree.setModel(new DefaultTreeModel( root ));
    		return _tree;
    	}
    }
    
    /**
     * 创建懒加载的树
     * @param _ds
     * @param _title
     * @param _sql
     * @param _parentkey
     * @return JTree 
     */
    public synchronized static JTree getLazyLoadJTree( String _ds, String _title, String _sql, String _parentkey ){
    	try{
        	return  getLazyLoadTree(_ds, _title, _sql, _parentkey, null);
        }catch ( Exception e ){              
            return null;
        }
    }
    
    /**
     * 创建懒加载的树
     * @param _ds
     * @param _title
     * @param _sql
     * @param _parentkey
     * @param _tree 树对象，空表示直接生成新的tree。
     * @return JTree
     */
    public synchronized static JTree getLazyLoadTree(String _ds, String _title, String _sql, String _parentkey, JTree _tree)throws Exception{
    	//创建根结点
    	DefaultMutableTreeNode root=null;
    	if(_tree==null){
    		root=new DefaultMutableTreeNode(_title);
    	}else{
    		root=(DefaultMutableTreeNode)_tree.getModel().getRoot();
    		root.removeAllChildren();
    	}
 
    	HashVO[] hashVOs = getService().getHashVoArrayByDS( _ds, rebuildLazyLoadTreeSQL(_sql, null) );//分级加载的树默认只加载第一层
        for ( int i = 0; i < hashVOs.length; i++ ){
        	root.add(new DefaultMutableTreeNode( hashVOs[i] )); 
        }
        
        if(_tree==null){
    		return new JTree( new DefaultTreeModel( root ));
    	}else{
    		_tree.setModel(new DefaultTreeModel( root ));
    		return _tree;
    	}
    }
    
    /**
     * 获得树所有节点的table，不包含根节点。
     * @param _table
     * @param _key
     * @return
     */
    public static Hashtable getTreeHashVONodeTable(JTree _tree, Hashtable _table, String _key){
    	if(_table==null){
    		_table=new Hashtable();
    	}else{
    		_table.clear();
    	}
    	if(_tree==null) return _table;
    	DefaultMutableTreeNode root=(DefaultMutableTreeNode)_tree.getModel().getRoot();
    	Enumeration enums=root.children();
    	while(enums.hasMoreElements()){
    		getTreeHashVONodeTable((DefaultMutableTreeNode)enums.nextElement(), _table, _key);
    	}
    	return _table;
    }
    
    /**
     * 获得树所有节点的table，包含当前节点。
     * @param tbl
     * @param _key
     * @return
     */
    public static Hashtable getTreeHashVONodeTable(DefaultMutableTreeNode _node, Hashtable _table, String _key){
    	if(_table==null){
    		_table=new Hashtable();
    	}
    	HashVO vo=(HashVO)_node.getUserObject();
    	_table.put(vo.getStringValue(_key), _node);
    	
    	Enumeration enums=_node.children();
    	while(enums.hasMoreElements()){
    		getTreeHashVONodeTable((DefaultMutableTreeNode)enums.nextElement(), _table, _key);
    	}
    	return _table;    	
    }
    
    
    /**
     * 处理树节点检索sql。考虑了主检索sql是复杂sql的情况，因此父节点的条件是作为嵌入条件放入的。
     * @param _sql 包含0到多个子句" -=【 and _treeparent_ 】=- "
     * @return
     */
    public static String rebuildLazyLoadTreeSQL(String _sql){
    	/**
    	 * 清除子句-=【 and _treeparent_ 】=-
    	 */
    	return clearSubstring(_sql,"-=【","】=-");
    }
    
    /**
     * 处理树节点检索sql。考虑了主检索sql是复杂sql的情况，因此父节点的条件是作为嵌入条件放入的。
     * @param _sql 包含0到多个子句" -=【 and key _treeparent_ 】=- "
     * @param parentkeyValue   字段值
     * @return
     */
    public static String rebuildLazyLoadTreeSQL(String _sql, String parentkeyValue){
    	/**
    	 * 1、判断parentkeyValue是否有值，处理子句 -=【 and key _treeparent_ 】=- 
    	 *   替换_treeparent_为 key = value 或者 key is null
    	 */
    	if(parentkeyValue==null){
    		//清空“-=【”“】=-”之间的子句
    		return _sql.replaceAll("-=【", "")
		               .replaceAll("】=-", "")
		               .replaceAll("_treeparent_", " is null");
    		    		
    	}else{
    	    return _sql.replaceAll("-=【", "")
                      .replaceAll("】=-", "")
                      .replaceAll("_treeparent_", "="+parentkeyValue);
    	}
    }    
    private static String clearSubstring(final String src,final String start,final String end){
    	int istart=src.indexOf(start);
    	if(istart==-1){
    		return src;
    	}
    	int iend=src.indexOf(end,istart+start.length());
    	if(iend==-1){
    		return src;
    	}
    	String tmp=src.substring(0,istart)+src.substring(iend+end.length());
    	//递归一下，看看还有没有
    	return clearSubstring(tmp,start,end);    	
    }
    /**
     * 取当前结点的下一级子结点,并加入当前结点下。
     * @param parentKeyValue 当前双击的结点的ID
     * @param parentKey 表中的父键
     * @return
     */
    public synchronized static DefaultMutableTreeNode fetchChildofTreeNode(String _dsName, String _sql,DefaultMutableTreeNode parentNode, String parentKey,String parentKeyValue)throws Exception{
    	 HashVO[] hashVOs = getService().getHashVoArrayByDS( _dsName, rebuildLazyLoadTreeSQL(_sql,parentKeyValue));
         for ( int i = 0; i < hashVOs.length; i++ ){
        	 parentNode.add(new DefaultMutableTreeNode( hashVOs[i] )); 
         }
         
         return parentNode;
    }
    //上传文件
    public synchronized static HashMap uploadFileFromClient(String _templetcode, String tablename, String fieldname,
        ClassFileVO _vo) throws Exception {
        return getService().uploadFileFromClient(_templetcode, tablename, fieldname, _vo);
    }

    //下载文件
    public synchronized static ClassFileVO downloadToClient(String filename) throws Exception {
        return getService().downloadToClient(filename);
    }

    /**
     * 替换字符
     *
     * @param str_par
     * @param old_item
     * @param new_item
     * @return
     */
    public static synchronized String replaceAll(String str_par, String old_item, String new_item) {
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

}

/*******************************************************************************
 * $RCSfile: UIUtil.java,v $ $Revision: 1.11.2.10 $ $Date: 2010/01/20 09:44:52 $
 *
 * $Log: UIUtil.java,v $
 * Revision 1.11.2.10  2010/01/20 09:44:52  wangqi
 * *** empty log message ***
 *
 * Revision 1.11.2.9  2010/01/05 09:10:49  wangqi
 * *** empty log message ***
 *
 * Revision 1.11.2.8  2009/09/03 06:41:08  wangqi
 * *** empty log message ***
 *
 * Revision 1.11.2.7  2009/03/16 07:24:41  wangqi
 * *** empty log message ***
 *
 * Revision 1.11.2.6  2008/12/02 09:31:59  wangqi
 * *** empty log message ***
 *
 * Revision 1.11.2.5  2008/11/05 05:21:07  wangqi
 * *** empty log message ***
 *
 * Revision 1.11.2.4  2008/09/16 06:11:31  wangqi
 * patch   : 20080916
 * file    : nova_20080128_20080916.jar
 * content : 处理 MR nova20-87,nova20-30；
 * 另外，改写了快速查询面板的处理。
 *
 * Revision 1.11.2.3  2008/08/27 07:00:12  wangqi
 * *** empty log message ***
 *
 * Revision 1.11.2.2  2008/03/27 05:52:12  wangqi
 * *** empty log message ***
 *
 * Revision 1.12  2008/01/28 09:04:13  wangqi
 * *** empty log message ***
 *
 * Revision 1.11  2008/01/23 14:19:17  wangqi
 * *** empty log message ***
 *
 * Revision 1.10  2008/01/23 04:46:22  wangqi
 * *** empty log message ***
 *
 * Revision 1.9  2008/01/15 09:04:15  wangqi
 * *** empty log message ***
 *
 * Revision 1.8  2008/01/08 00:47:06  wangqi
 * *** empty log message ***
 *
 * Revision 1.7  2008/01/04 08:35:21  wangqi
 * *** empty log message ***
 *
 * Revision 1.6  2007/11/10 11:16:40  john_liu
 * 2007.11.10 by john_liu
 * MR#: MR#: BIZM10-277, BIZM20071023-53
 *
 * Revision 1.5  2007/05/31 07:38:24  qilin
 * code format
 *
 * Revision 1.4  2007/05/22 10:17:20  qilin
 * no message
 *
 * Revision 1.3  2007/05/22 07:58:47  qilin
 * no message
 *
 * Revision 1.2  2007/05/22 02:02:54  sunxb
 * *** empty log message ***
 *
 * Revision 1.1  2007/05/17 05:56:36  qilin
 * no message
 *
 * Revision 1.20  2007/03/30 08:25:24  shxch
 * *** empty log message ***
 *
 * Revision 1.19  2007/03/21 05:52:19  shxch
 * *** empty log message ***
 *
 * Revision 1.18  2007/03/14 06:58:01  shxch
 * *** empty log message ***
 *
 * Revision 1.17  2007/03/07 02:01:55  shxch
 * *** empty log message ***
 *
 * Revision 1.16  2007/03/02 05:51:05  shxch
 * *** empty log message ***
 *
 * Revision 1.15  2007/03/02 05:28:07  shxch
 * *** empty log message ***
 *
 * Revision 1.14  2007/03/02 05:16:43  shxch
 * *** empty log message ***
 *
 * Revision 1.13  2007/03/02 05:02:50  shxch
 * *** empty log message ***
 *
 * Revision 1.12  2007/03/01 09:06:56  shxch
 * *** empty log message ***
 *
 * Revision 1.11  2007/03/01 07:20:47  shxch
 * *** empty log message ***
 *
 * Revision 1.10  2007/03/01 02:57:21  shxch
 * 添加获取图标文件名数组的方法
 * Revision 1.9 2007/02/28 03:17:26 shxch *** empty log
 * message ***
 *
 * Revision 1.8 2007/02/27 09:38:47 shxch *** empty log message ***
 *
 * Revision 1.7 2007/02/27 06:03:03 shxch *** empty log message ***
 *
 * Revision 1.6 2007/02/25 09:01:19 shxch *** empty log message ***
 *
 * Revision 1.5 2007/01/31 10:03:25 shxch *** empty log message ***
 *
 * Revision 1.4 2007/01/31 07:38:27 shxch *** empty log message ***
 *
 * Revision 1.3 2007/01/30 05:34:27 sunxf 修改生成树代码
 *
 * Revision 1.2 2007/01/30 03:41:28 lujian *** empty log message ***
 *
 *
 ******************************************************************************/
