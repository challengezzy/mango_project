package smartx.flex.components.core.cep
{
	public interface IStreamControl
	{
		function getIcon():Class;
		function getName():String;
		function getDisplayName():String;
		function getType():String;
		function getDescription():String;
		function getPropertyEditorClassName():String;
		//计算某实例的输出流事件格式
		function printOutputStreamEvent(instance:StreamInstance):StreamEvent;
		//编译某实例为EPL
		function compile(instance:StreamInstance):String;
		//判断是否有不合法的编译错误
		function validate(instance:StreamInstance):ValidateResult;
	}
}