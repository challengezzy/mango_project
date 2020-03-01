package smartx.flex.components.core.mtchart.vo
{
	/**
	 * 流程图控件设置与显示中相关常量定义
	 * @author zzy
	 * @date Jun 17, 2011
	 */
	public class ProcessChartConst
	{
		public static const STARTNODE:String = "StartNode"; //开始节点
		
		//布局方式
		public static const LAYOUT_CONCENTRICRADIAL:String = "ConcentricRadial";
		public static const LAYOUT_PARENTCENTEREDRADIAL:String = "ParentCenteredRadial";
		public static const LAYOUT_SINGLECYCLECIRCLE:String = "SingleCycleCircle";
		public static const LAYOUT_HYPERBOLIC:String = "Hyperbolic";
		public static const LAYOUT_HIERARCHICAL:String = "Hierarchical"
		public static const LAYOUT_FORCEDIRECTED:String = "ForceDirected";
		public static const LAYOUT_ISOM:String = "ISOM";
		public static const LAYOUT_DIRECTPLACEMENT:String = "DirectPlacement";
		public static const LAYOUT_PHYLLOTACTIC:String = "Phyllotactic";
			
		//层次布局方向
		public static const HIERORIENTATION_TOPDOWN:String = "Top Down";
		public static const HIERORIENTATION_BOTTOMUP:String = "Bottom Up";
		public static const HIERORIENTATION_LEFTRIGHT:String = "Left-Right";
		public static const HIERORIENTATION_RIGHTLEFT:String = "Right-Left";	
			
		//节点风格
		public static const NODESYTLE_DEFAULT :String = "Default";
		public static const NODESYTLE_BASIC :String = "Basic";
		public static const NODESYTLE_BASICEFFECTS :String = "Basic+Effects";
		public static const NODESYTLE_SIMPLECIRCLE :String = "SimpleCircle";
		public static const NODESYTLE_SIMPLECIRCLEEFFECTS :String = "SimpleCircle+Effects";
		public static const NODESYTLE_ICONS :String = "Icons";
		public static const NODESYTLE_ROTATEDRECTANGLE :String = "Rotated Rectangle";
		public static const NODESYTLE_SIZEBYVALUE :String = "Size by Value";
		public static const NODESYTLE_WFB :String = "WFB";
				
		//连线风格
		public static const EDGESTYLE_DEFAULT :String = "Default";
		public static const EDGESTYLE_DIRECTEDARROWS :String = "DirectedArrows";
		public static const EDGESTYLE_DIRECTEDBALLOONS :String = "DirectedBalloons";
		public static const EDGESTYLE_ORTHOGONAL :String = "Orthogonal";
		public static const EDGESTYLE_FLOW :String = "Flow";
		public static const EDGESTYLE_BEZIER :String = "Bezier";
		public static const EDGESTYLE_CIRCULAR :String = "Circular";
		public static const EDGESTYLE_HYPERBOLIC :String = "Hyperbolic";
		
		//连线图标风格
		public static const EDGELABELSTYLE_DEFAULT :String = "Default";
		public static const EDGELABELSTYLE_BASIC :String = "Basic";
		public static const EDGELABELSTYLE_WFB :String = "WFB";
		
		
		
		
			
	}
}