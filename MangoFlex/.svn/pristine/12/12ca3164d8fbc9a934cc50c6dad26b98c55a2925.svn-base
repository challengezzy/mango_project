package smartx.flex.components.util
{
	import flash.utils.ByteArray;

	/**
	 * @author zzy
	 * @date Aug 26, 2011
	 * @description 利用ByteArray对象的编码转换和存储功能，活用ByteArray类，能处理绝大部分的AS3编码问题
	 * 经测试这个编码转换不是很好用，使用前请注意详细测试,供参考
	 */
	public class CharacterEncodeUtil
	{
		public function CharacterEncodeUtil()
		{
		}
		
		/**
		 * URL转成GBK编码
		 * */
		public static function encodeGBK(str:String):String{
			var result:String ="";
			var bytes:ByteArray =new ByteArray();
			bytes.writeMultiByte(str,"gb2312");
			for(var i:int;i<bytes.length;i++){
				result += String.fromCharCode(bytes[i]);
			}
			return result;
			
//			var result:String ="";
//			var bytes:ByteArray = ByteArray(str);
//			var pos_now:uint = 0;
//			while (pos_now < bytes.length) {
//				if (bytes[pos_now] < 160) {
//					result += bytes.readMultiByte(1,'utf-8');
//					pos_now++;
//				} else {	
//					result += bytes.readMultiByte(2,'GBK');	
//					pos_now = pos_now + 2;
//				}
//			}
//			
//			return result;
		}		
		
		/**
		 * URL转成GBK编码
		 * */
		public static function urlEncodeGBK(str:String):String{
			var result:String ="";
			var byte:ByteArray =new ByteArray();
			byte.writeMultiByte(str,"gb2312");
			for(var i:int;i<byte.length;i++){
				result += escape(String.fromCharCode(byte[i]));
			}
			return result;
		}
		
		public static function urlEncodeUTF8(str:String):String{
			var result:String ="";
			var byte:ByteArray =new ByteArray();
			byte.writeMultiByte(str,"utf-8");
			for(var i:int;i<byte.length;i++){
				result += escape(String.fromCharCode(byte[i]));
			}
			return result;
		}
	}
}