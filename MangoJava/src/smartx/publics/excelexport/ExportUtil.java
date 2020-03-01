package smartx.publics.excelexport;

import java.io.File;
import java.util.List;
import java.util.UUID;

import smartx.framework.common.utils.Sys;

/**
 *@author zzy
 *@date Apr 3, 2012
 **/
public class ExportUtil {
	
	//生成一个不重名的文件
	public static  String getNewFileName(String dirPath,String preName,String extName) throws Exception{
		String sysRootPath = (String) Sys.getInfo("NOVA2_SYS_ROOTPATH");
		File dir = new File(sysRootPath+dirPath);
		if(!dir.exists())
			dir.mkdirs();
		
		String filename = "";
		File file;
		do{//保证文件名不重复
			filename = sysRootPath+dirPath+"/"+ preName + UUID.randomUUID().toString()+"." + extName;
			file = new File(filename);
		}while(file.exists());
		
		return filename;
	}
	
	/**
	 * 合并二维数组的值
	 * @param dataValues
	 * @return
	 * @throws Exception
	 */
	public static Object[][] combine(List<Object[][]> dataValues) throws Exception{
		if(dataValues.size() == 0)
			return null;
		//当只有一个值的话，直接返回
		if(dataValues.size() == 1)
			return dataValues.get(0);
		Object[][] combineValue = null;
		int columnNum = 0;//列数
		int rowNum = 0;//行数
		for(Object[][] value : dataValues){
			if(value.length == 0)
				continue;
			rowNum += value.length;
			
			columnNum = Math.max(columnNum, value[0].length);
		}
		
		combineValue = new Object[rowNum][columnNum];
		
		int index = 0;
		for(Object[][] value : dataValues){
			for(int i=0;i<value.length;i++,index++){
				combineValue[index] = value[i];
			}
		}
		
		return combineValue;
	}
}
