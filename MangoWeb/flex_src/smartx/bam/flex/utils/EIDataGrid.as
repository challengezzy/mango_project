package smartx.bam.flex.utils
{
	
	import flash.events.KeyboardEvent;  
	import flash.events.TextEvent;  
	import flash.text.TextField;  
	import flash.text.TextFieldType;  
	
	import mx.controls.*;  
	import mx.controls.dataGridClasses.DataGridColumn;  

	/**
	 * @author zzy
	 * @date Aug 27, 2011
	 * @description datagrid组件增加CRTL+C功能测试
	 */
	public class EIDataGrid extends DataGrid  
	{  
		public function EIDataGrid()  
		{  
			super();  
			this.addEventListener(KeyboardEvent.KEY_DOWN,KeyDownHandler);  
			this.addEventListener(KeyboardEvent.KEY_UP,KeyUpHandler);  
		}  
		private function KeyDownHandler(event:KeyboardEvent):void  
		{  
			if (event.ctrlKey && !this.getChildByName("clipboardProxy"))  
			{  
				// Add an invisible TextField object to the DataGrid  
				var textField:TextField = new TextField();  
				textField.name = "clipboardProxy";  
				this.addChild(textField);  
				textField.visible = false;				
				textField.type = TextFieldType.INPUT;  
				textField.multiline = true;  
				
				// Populate the TextField with selected data in TSV format 				
				textField.text = getTextFromItems(this.selectedItems);  
				//textField.text = getTextFromItems(dataGrid.dataProvider.source);  
				textField.setSelection(0, textField.text.length - 1);  
				
				// Listen for textInput event 				
				textField.addEventListener(TextEvent.TEXT_INPUT,clipboardProxyPasteHandler);
				
				// Set player-level focus to the TextField 				
				systemManager.stage.focus = textField;  
			}  
		}
		
		
		private function KeyUpHandler(event:KeyboardEvent):void  
		{  
			if (!event.ctrlKey)  
			{  
				var textField:TextField = TextField(this.getChildByName("clipboardProxy"));  
				if (textField)  
				{  
					this.removeChild(textField);  
				}
			} 
		}
		
		private function clipboardProxyPasteHandler(event:TextEvent):void  
		{  
			// Extract values from TSV format and populate the DataGrid			
			var items:Array = getItemsFromText(event.text);  
			for each (var item:Object in items)  
				this.dataProvider.addItem(item);  
		} 
		
		private function getItemsFromText(text:String):Array  
		{  
			var rows:Array = text.split("\n");  
			if (!rows[rows.length - 1])  
				rows.pop();  
			
			var columns:Array = this.columns;  
			var itemsFromText:Array = [];  
			
			for each (var rw:String in rows)  
			{  
				var fields:Array = rw.split("\t");  
				
				var n:int = Math.min(columns.length, fields.length);  
				var item:Object = {};  
				for (var i:int = 0; i < n; i++)  
					item[columns[i].dataField] = fields[i];  
				itemsFromText.push(item);  
			}  
			
			return itemsFromText;    
		}
		

		private function getTextFromItems(items:Array):String  
		{  
			var columns:Array = this.columns;  
			var textFromItems:String = "";  
			//add datagrid headtext   
			for each (var i: DataGridColumn in columns)  
			{  
				textFromItems += i.headerText + "\t";  
			}  
			textFromItems += "\n";  
			
			for each (var it:Object in items)  
			{  
				
				for each (var c: DataGridColumn in columns)  
				textFromItems += it[c.dataField] + "\t";  
				textFromItems += "\n";  
			}  
			
			return textFromItems;  
		} 
	}  
}