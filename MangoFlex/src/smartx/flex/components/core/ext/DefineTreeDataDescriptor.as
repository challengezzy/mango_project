package smartx.flex.components.core.ext
{
	import mx.collections.ArrayCollection;
	import mx.collections.ICollectionView;
	import mx.collections.XMLListCollection;
	import mx.controls.treeClasses.DefaultDataDescriptor;
	
	public class DefineTreeDataDescriptor extends DefaultDataDescriptor
	{
		public function DefineTreeDataDescriptor(){
			super();
		}
		
		override public function getChildren(node:Object, model:Object=null):ICollectionView{
			//如果node是XML则返回一个XMLListCollection对象，否则调用mx.collections.HierarchicalCollectionView.xmlNotification()方法会报错
			if(node && node is XML && node.@hasChildren == "false")
				return new XMLListCollection();
			return super.getChildren(node,model);
		}
		
		override public function isBranch(node:Object, model:Object=null):Boolean{
			if(node && node is XML && node.@hasChildren == "false")
				return false;
			return super.isBranch(node,model);
		}
	}
}