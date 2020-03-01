package smartx.flex.components.styletemplate.ifc
{
	import smartx.flex.components.core.BillListPanel;

	/**
	 * BillListPanel每一行中按钮控件响应事件监听器
	 **/
	public interface ListEmbedButtonListener
	{
		function buttonClick(listPanel:BillListPanel):void;
	}
}