package smartx.framework.metadata.util;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * <li>Title: TreeNode.java</li>
 * <li>Description: 树节点定义</li>
 * <li>Project: NOVA2_MAINBRANCH</li>
 * <li>Copyright: Copyright (c) 2008</li>
 * @Company: GXLU. All Rights Reserved.
 * @author James.W
 * @version 1.0
 */
public class TreeNode{ 
	public TreeNode(String key,Object data){
		this._key=key;
		this._data=data;
		this._parent=null;
	}
	//节点标记
	private String _key=null;
	//节点数据
	private Object _data=null;
	//父节点
	private TreeNode _parent=null;
	//子节点
	private ArrayList _children=new ArrayList();
	//下层节点的键值记录，用于查找使用，也为了放置键值重复
	private HashMap _keymap=new HashMap(); 
	
	/**
	 * 给当前节点增加子节点
	 * 如果节点已经存在，则默认放弃
	 */
	public void addChild(TreeNode node){
		String key=node.getKey();
		if(_keymap.containsKey(key)){
			return;
		}
		node.setParent(this);
		this._children.add(node);
		_keymap.put(key, key);
	}
	/**
	 * 给指定父节点节点增加子节点
	 * 如果父节点不存在，则默认放弃，也就是说这个节点会丢弃
	 */
	public void addChildDeep(TreeNode node,String parentkey){
		TreeNode parent=getChildDeep(parentkey);
		if(parent==null){
			return;
		}
		parent.addChild(node);
		node.setParent(this);		
	}
	
	/**
	 * 给当前节点的指定位置增加子节点
	 */
	public void addChild(TreeNode node,int idx){
		String key=node.getKey();
		if(_keymap.containsKey(key)){
			return;
		}
		node.setParent(this);
		this._children.add(idx, node);
		_keymap.put(key, key);		
	}
	/**
	 * 在当前节点的子节点中寻找指定key的子节点
	 * @param key
	 * @return
	 */
	public TreeNode getChild(String key){
		if(!_keymap.containsKey(key)){
			return null;
		}
		int size=this._children.size();
		for(int i=0;i<size;i++){
			if(((TreeNode)this._children.get(i)).getKey().equals(key)){
				return (TreeNode)this._children.get(i);
			}
		}
		return null;
	}
	
	/**
	 * 在当前节点的所有下层子节点中寻找指定key的节点
	 * @param key
	 * @return
	 */
	public TreeNode getChildDeep(String key){
		TreeNode node=getChild(key);
		if(node == null){
			int size=this._children.size();
			for(int i=0;i<size;i++){
				node=((TreeNode)this._children.get(i)).getChildDeep(key);
				if(node != null){
					return node;
				}
			}
		}		
		return node;
	}
	
	/**
	 * 在当前节点的子节点中寻找指定序号的子节点
	 * @param idx
	 * @return
	 */
	public TreeNode getChild(int idx){
		if(idx>=this._children.size()){
			return null;
		}
		return ((TreeNode)this._children.get(idx));
	}
	
	/**
	 * 移除节点
	 * @param key
	 * @return
	 */
	public TreeNode removeChild(String key){
		if(!_keymap.containsKey(key)){
			return null;
		}
		int size=this._children.size();
		for(int i=0;i<size;i++){
			if(((TreeNode)this._children.get(i)).getKey().equals("key")){
				return (TreeNode)this._children.remove(i);
			}
		}
		return null;
	}
	/**
	 * 移除节点，深层移除
	 * @param key
	 * @return
	 */
	public TreeNode removeChildDeep(String key){
		TreeNode node=removeChild(key);
		if(node == null){
			int size=this._children.size();
			for(int i=0;i<size;i++){
				node=((TreeNode)this._children.get(i)).removeChildDeep(key);
				if(node != null){
					return node;
				}
			}
		}
		return node;
	}
	
	/**
	 * 是否叶子节点
	 * @return
	 */
	public boolean isLeaf(){
		return this._children.size()==0;
	}
	
	/**
	 * 子节点数
	 * @return
	 */
	public int getCount(){
		return this._children.size();
	}
	
	/**
	 * 子节点数
	 * @return
	 */
	public int getCountDeep(){
		int count=0;
		int chs=this._children.size();
		for(int i=0;i<chs;i++){
			count+=((TreeNode)this._children.get(i)).getCountDeep();
		}
		return count+chs;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public String getKey() {
	
		return _key;
	}
	
	public Object getData() {
	
		return _data;
	}
	
	public TreeNode getParent() {
	
		return _parent;
	}
	public void setParent(TreeNode parent) {
	
		this._parent = parent;
	}
}

/**********************************************************************
 *$RCSfile: TreeNode.java,v $  $Revision: 1.1.2.1 $  $Date: 2008/10/29 09:31:03 $
 *
 *$Log: TreeNode.java,v $
 *Revision 1.1.2.1  2008/10/29 09:31:03  wangqi
 **** empty log message ***
 *
 *********************************************************************/