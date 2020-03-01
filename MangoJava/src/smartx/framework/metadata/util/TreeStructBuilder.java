/**********************************************************************
 *$RCSfile: TreeStructBuilder.java,v $  $Revision: 1.1.2.1 $  $Date: 2008/10/29 09:31:02 $
 *********************************************************************/ 
package smartx.framework.metadata.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import smartx.framework.common.vo.HashVO;


/**
 * <li>Title: TreeStructure.java</li>
 * <li>Description: 树结构处理类</li>
 * <li>Project: NOVA2_MAINBRANCH</li>
 * <li>Copyright: Copyright (c) 2008</li>
 * @Company: GXLU. All Rights Reserved.
 * @author James.W
 * @version 1.0
 */
public class TreeStructBuilder {
	
	/**
	 * 通过HashVO建立树
	 * @param root 根节点，非空-在已有树上增加，空-创建新的树
	 * @param vos HashVO的列表
	 * @param keyfield 键值字段
	 * @param parentfield 父键值字段
	 * @return 树根节点
	 */
	public static TreeNode buildTree(TreeNode root,HashVO[] vos,String keyfield,String parentfield){
		ArrayList lst=new ArrayList();
		int rows=vos.length;
		for(int i=0;i<rows;i++){
			lst.add(vos[i]);
		}
		return buildTree(root,lst,keyfield,parentfield);
	}
	
	/**
	 * 通过String[][]建立树
	 * @param root 根节点，非空-在已有树上增加，空-创建新的树
	 * @param data
	 * @param keyfield
	 * @param parentfield
	 * @return
	 */
	public static TreeNode buildTree(TreeNode root,String[][] data,int keyfield,int parentfield){
		//TODO 算法需要调整，要防止父节点在子节点后面的情况，甚至父节点不存在的情况。
		//     如父节点不存在，则自动作为根节点。
		//     如父节点在后面，则需要等父节点加入后才能加入子节点。
		//     参照下面方法的算法。
		
		root=(root==null)?(new TreeNode("",null)):root;
		String strkey=null,strparent=null;
		TreeNode node=null;
		int size=data.length;
		for(int i=0;i<size;i++){
			strkey=data[i][keyfield];
			strparent=data[i][parentfield];
			strparent=(strparent==null)?"":strparent;
			node=new TreeNode(strkey,data[i]);
			TreeNode parent=root.getChildDeep(strparent);
			if(parent==null){
				root.addChild(node);
			}else{
				parent.addChild(node);
			}
		}
		return root;
		
	}
	
	/**
	 * 通过HashVO建立树
	 * @param root 根节点，非空-在已有树上增加，空-创建新的树
	 * @param vct HashVO的列表
	 * @param keyfield 键值字段
	 * @param parentfield 父键值字段
	 * @return 树根节点
	 */
	public static TreeNode buildTree(TreeNode root,List lst,String keyfield,String parentfield){
		HashMap map=new HashMap();
		int rows=lst.size();
		for(int i=0;i<rows;i++){
			String strkey=((HashVO)lst.get(i)).getStringValue(keyfield);
			map.put(strkey,lst.get(i));
		}
		
		root=(root==null)?(new TreeNode("",null)):root;
		String strkey=null,strparent=null;
		TreeNode node=null;		
		while(map.size()>0){
			for(int i=0;i<rows;i++){
				HashVO vo=(HashVO)lst.get(i);
				strkey=vo.getStringValue(keyfield);
				if(!map.containsKey(strkey)){
					//节点不在map中存在，表示已经在树中加载过了
					continue;
				}
				strparent=vo.getStringValue(parentfield);
				strparent=(strparent==null)?"":strparent;
				
				if(strparent.equals("")){//无父节点，默认作为根节点加入
					node=new TreeNode(strkey,vo);
					root.addChild(node);
					map.remove(strkey);
				}else{
					TreeNode parent=root.getChildDeep(strparent);			
					if(parent!=null){
						node=new TreeNode(strkey,vo);
						parent.addChild(node);
						map.remove(strkey);					
					}else{
						if(map.containsKey(strparent)){
							//父节点存在但还没有加入树，所以当前节点本轮不加入，作为后面轮次处理，直到父节点已经被加入
							continue;							
						}else{
							//父节点不存在，默认作为根节点
							node=new TreeNode(strkey,vo);
							root.addChild(node);
							map.remove(strkey);
						}
					}
				}
				System.out.println("节点：key="+strkey+" parent:"+strparent);
			}
		}
		return root;
	}
	
	
	
}


/**********************************************************************
 *$RCSfile: TreeStructBuilder.java,v $  $Revision: 1.1.2.1 $  $Date: 2008/10/29 09:31:02 $
 *
 *$Log: TreeStructBuilder.java,v $
 *Revision 1.1.2.1  2008/10/29 09:31:02  wangqi
 **** empty log message ***
 *
 *********************************************************************/