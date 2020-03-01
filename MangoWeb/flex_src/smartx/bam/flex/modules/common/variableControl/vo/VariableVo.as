package smartx.bam.flex.modules.common.variableControl.vo
{
	import smartx.bam.flex.vo.BAMConst;

	public class VariableVo
	{
		public static const IGNORE_VALUE:String = "IGNORE";//忽略值
		
		public var name:String;//变量名
		
		public var type:String;//控件类型
		
		public var label:String;//控件标签
		
		public var defaultValue:String;//默认值
		
		public var defaultValueLabel:String;//默认显示名称，用于参照控件
		
		public var defineRefPanel:String;//参照说明
		
		public var isUseSynonyms:Boolean;//下拉框控件是否使用同义词
		
		public var defineCbo:String;//下拉框说明(同义词:synonymsName@@@synonymsCode)
		
		public var isIgnoreDefaultValue:Boolean;//是否忽略变量值
		
		public function VariableVo(name:String,type:String="",label:String="",defaultValue:String=""
								   ,defaultValueLabel:String="",defineRefPanel:String="",isUseSynonyms:Boolean=false,defineCbo:String=""
								   ,isIgnoreDefaultValue:Boolean=false){
			this.name = name;
			this.type = type==""?BAMConst.VARIABLE_CONTROL_TEXTINPUT:type;
			this.label = label;
			this.defaultValue = defaultValue;
			this.defaultValueLabel = defaultValueLabel;
			this.defineRefPanel = defineRefPanel;
			this.isUseSynonyms = isUseSynonyms;
			this.defineCbo = defineCbo;
			this.isIgnoreDefaultValue = isIgnoreDefaultValue;
		}
		
	}
}