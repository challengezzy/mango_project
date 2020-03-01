package smartx.flex.components.itemcomponent
{
	import mx.controls.TextInput;
	import smartx.flex.components.itemcomponent.ext.AdvTextInput;
	import smartx.flex.components.vo.TempletItemVO;
	
	public class ItemPasswordField extends ItemTextField
	{
		private var textInput:AdvTextInput;
		
		public function ItemPasswordField(templetItemVO:TempletItemVO)
		{
			super(templetItemVO);
			textInput = input as AdvTextInput;
			this.enableDetailEdit = false;
			textInput.displayAsPassword = true;
		}
		
	}
}