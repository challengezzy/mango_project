package smartx.bam.flex.modules.common.variableControl
{
	import mx.core.UIComponent;
	
	import smartx.bam.flex.modules.common.variableControl.vo.VariableVo;
	import smartx.bam.flex.vo.BAMConst;

	public class VariableControlBuilder
	{
		private static var instance:VariableControlBuilder = new VariableControlBuilder();
		
		public static function getBuilder():VariableControlBuilder{
			return instance;
		}
		
		public function VariableControlBuilder(){
			if(instance)
				throw new Error("已存在实例对象！");
		}
		
		public function getControl(variable:VariableVo,destination:String=null,endpoint:String=null):VariableControlComponent{
			switch (variable.type){
				case BAMConst.VARIABLE_CONTROL_COMBOBOX:
					return ComboBoxFactory.getInstance().build(variable);
				case BAMConst.VARIABLE_CONTROL_TEXTINPUT:
					return TextInputFactory.getInstance().build(variable);
				case BAMConst.VARIABLE_CONTROL_REFPANEL:
					return RefPanelFactory.getInstance().build(variable,destination,endpoint);
			}
			return null;
		}
	}
}
import com.adobe.utils.StringUtil;

import flash.events.Event;

import mx.collections.ArrayCollection;
import mx.controls.ComboBox;
import mx.controls.TextInput;
import mx.core.UIComponent;

import smartx.bam.flex.modules.common.variableControl.VariableControlComboBox;
import smartx.bam.flex.modules.common.variableControl.VariableControlRefPanel;
import smartx.bam.flex.modules.common.variableControl.VariableControlTextInput;
import smartx.bam.flex.modules.common.variableControl.vo.VariableVo;
import smartx.bam.flex.vo.BAMConst;
import smartx.flex.components.itemcomponent.ItemRefPanel;
import smartx.flex.components.util.MetadataTempletUtil;
import smartx.flex.components.vo.GlobalConst;
import smartx.flex.components.vo.MetadataTemplet;
import smartx.flex.components.vo.SimpleRefItemVO;
import smartx.flex.components.vo.TempletItemVO;

class ComboBoxFactory{
	private static var instance:ComboBoxFactory = new ComboBoxFactory();
	
	public function ComboBoxFactory(){};
	
	public static function getInstance():ComboBoxFactory{
		return instance;
	}
	
	public function build(data:VariableVo):VariableControlComboBox{
		
		var defineCbo:String = String(data.defineCbo);
		var dataProvider:ArrayCollection = new ArrayCollection();
		var cbo:VariableControlComboBox = null;
		
		if(StringUtil.trim(defineCbo) != ""){
			var obj:Object;
			//使用同义词
			if(data.isUseSynonyms){
				var metadataTemplet:MetadataTemplet = MetadataTempletUtil.getInstance().findMetadataTemplet(GlobalConst.MTCODE_SYNONYMS);
				var synonymsXml:XML = metadataTemplet.contentXML;
				var code:String = defineCbo.split("@@@").length > 1?defineCbo.split("@@@")[1]:defineCbo.split("@@@")[0];
				var variableXmlList:XMLList = synonymsXml.synonyms.(@code==code).length() > 0 ? synonymsXml.synonyms.(@code==code)[0].variable:new XMLList();
				for each(var variableXml:XML in variableXmlList){
					obj = {label:String(variableXml.@name),data:String(variableXml.@value)};
					dataProvider.addItem(obj);
				}
			}else{
				var cboDatas:ArrayCollection = new ArrayCollection(defineCbo.split("],["));
				
				for each(var cboData:String in cboDatas){
					if(StringUtil.beginsWith(cboData,"["))
						cboData = StringUtil.replace(cboData,"[","");
					if(StringUtil.endsWith(cboData,"]"))
						cboData = StringUtil.replace(cboData,"]","");
					obj = {label:cboData.split("@@")[0],data:cboData.split("@@")[1]};
					dataProvider.addItem(obj);
				}
			}
			cbo = new VariableControlComboBox(dataProvider,data);
			cbo.data = data;
		}
		
		return cbo;
	}
}

class TextInputFactory{
	private static var instance:TextInputFactory = new TextInputFactory();
	
	public function TextInputFactory(){};
	
	public static function getInstance():TextInputFactory{
		return instance;
	}
	
	public function build(data:VariableVo):VariableControlTextInput{
		var textInput:VariableControlTextInput = new VariableControlTextInput(data);
		textInput.data = data;
		return textInput;
	}
}

class RefPanelFactory{
	private static var instance:RefPanelFactory = new RefPanelFactory();
	
	public function RefPanelFactory(){};
	
	public static function getInstance():RefPanelFactory{
		return instance;
	}
	
	public function build(data:VariableVo,destination:String,endpoint:String):VariableControlRefPanel{
		var itemVO:TempletItemVO = new TempletItemVO();
		itemVO.itemkey = data.name;
		itemVO.itemname = data.label;
		itemVO.refdesc = data.defineRefPanel;
		var returnVO:SimpleRefItemVO = new SimpleRefItemVO();
		returnVO.id = returnVO.code = data.defaultValue;
		returnVO.name = data.defaultValueLabel;
		var dataValue:Object = new Object();
		dataValue[data.name] = returnVO;
		var refPanel:ItemRefPanel = new ItemRefPanel(itemVO,destination,endpoint,false,false,dataValue);
		/*refPanel.callLater(function(d:Object,r:ItemRefPanel):void{
			r.data = d;
		},[dataValue,refPanel]);*/
		
		var vcRefPanel:VariableControlRefPanel = new VariableControlRefPanel(refPanel,data);
		return vcRefPanel;
	}
}