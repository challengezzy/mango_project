package smartx.flex.components.styletemplate.ifc
{
	import smartx.flex.components.core.BillCardPanel;
	import smartx.flex.components.event.StyleTemplateEvent;

	public interface StyleTemplateListener
	{
		function processEvent(card:BillCardPanel):void;
	}
}