package vos  
{  
	[Bindable]  
	public class TestVO  
	{  
		public function TestVO()  
		{  
		}  
		
		public var id:String;  
		public var label:String;  
		public var icon:String;  
		
		[Transient]  
		public var selected:Boolean = false;  
	}  
} 