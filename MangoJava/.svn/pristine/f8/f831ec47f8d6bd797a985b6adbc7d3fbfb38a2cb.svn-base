package smartx.publics.datatask;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import smartx.framework.common.bs.CommDMO;
import smartx.framework.common.utils.ExceptionUtil;
import smartx.framework.common.utils.ExportManager;
import smartx.framework.common.utils.FileUtil;
import smartx.framework.common.utils.StringUtil;
import smartx.framework.common.utils.ftp.FtpUtil;
import smartx.framework.common.vo.DMOConst;
import smartx.framework.common.vo.HashVO;
import smartx.framework.common.vo.NovaLogger;
import smartx.publics.datatask.vo.DataTaskConst;

/**
 * 通用数据预处理任务执行
 * 
 * @author zhangzy
 * @version 1.0
 * @created 2011/8/30 9:38:01
 */
public class DataTaskExecThread extends TaskAbstractThread {
	private Logger logger = NovaLogger.getLogger(this.getClass());

	public DataTaskExecThread() {
		this.setVersion(StringUtil.getTimestampMill());
	}

	/**
	 * 业务执行
	 */
	public String doAction(String taskContent, boolean isSubTask,String subtaskName,String curTaskId) throws Exception {
		String logPrefix = "";
		
		dealInitParams(taskContent);//初始化参数值处理
		
		if(isSubTask){
			logTaskRun("子数据任务=-=" +subtaskName + "== BEGIN{ =============================================", "");
			logPrefix = "子数据任务=-=" + subtaskName +  "=-=的";
		}else if(taskVo.getParamValue() != null ){
			//外部传入参数值
			String[] params = taskVo.getParamValue().trim().split(DataTaskConst.PARAM_SPLIT_S);
			for(int i=0;i<params.length;i++){
				String[] ps=params[i].trim().split(DataTaskConst.PARAM_KEYVALUE_S);
				if(ps.length!=2) 
					continue;
				
				addSharedValue(ps[0], ps[1]);
			}
			
		}
		
		//解析任务中的变量,任务实例生成时 !!!!一定要考虑子任务参数的解析
		taskContent = StringUtil.buildExpression(sharedPramMap, taskContent);
		
		Document doc = DocumentHelper.parseText(taskContent);
		Element root = doc.getRootElement();

		Element datataskE = root.element("datatask");
		//判断是否要执行后置任务
		String temp = sharedPramMap.get(DataTaskConst.PARAM_RUN_SUBSEQUENT);
		if(temp == null){
			String runSubsequentTask = datataskE.attributeValue("runSubsequentTask");
			sharedPramMap.put(DataTaskConst.PARAM_RUN_SUBSEQUENT, runSubsequentTask);
		}
		
		@SuppressWarnings("unchecked")
		List<Element> tasks = datataskE.elements();		
		String rate = null;
		if(!isSubTask){
			if(tasks != null ){
				rate = (100/tasks.size())+"";
			}
		}
		for (int i = 0; i < tasks.size(); i++) {
			//判断数据任务是否被强制终止
			if( getTaskStatus() == -1 ){
				logTaskRun("任务【"+taskVo.getName()+"】被强制终止!", "任务状态为-1，为强制终止");
				//throw new Exception("数据任务被强制终止！"); 
				//这里应该是直接结束任务执行，若抛出异常被认为是任务执行中碰到异常，与强制终止情况不符zhangzy 2012/5/17
				return DataTaskConst.TASKSTATUS_STOPED;
			}
			
			Element etask = tasks.get(i);
			String tag = etask.getName();
			String desc = etask.attributeValue("desc");
			String isNeedRun = etask.attributeValue("isNeedRun");
			if(isNeedRun != null && "false".equalsIgnoreCase(isNeedRun)){
				//此子任务不需要执行，跳过
				logTaskRun(logPrefix +"子任务" + tag + "【" + desc + "】设置为不执行，跳过!", "");
				continue;
			}
			String exceptionIgnore = etask.attributeValue("exceptionIgnore");//是否忽略该子任务的执行异常
			
			logTaskRun("开始"+ logPrefix +"子任务" + tag + "【" + desc + "】...", "");
			updateTaskRate(null, "子任务【" + desc + "】开始执行......");
			
			try {
				if ("sqlOperates".equalsIgnoreCase(tag)) {
					// 执行sql事务
					doTrans(etask);
				} else if ("ftpdownload".equalsIgnoreCase(tag)) {
					// 执行ftp下载
					ftpDownload(etask);
				} else if ("ftpupload".equalsIgnoreCase(tag)) {
					// 执行ftp上传
					ftpUpload(etask);
				} else if ("parsedata".equalsIgnoreCase(tag)) {
					// 执行文本数据解析
					paraseTxt(etask);
				} else if ("export_xls".equalsIgnoreCase(tag)) {
					// 执行导出xls
					exportXls(etask);
				} else if ("export_db".equalsIgnoreCase(tag)) {
					// 执行数据库间倒数据
					exportDb(etask);
				} else if ("data_extract".equalsIgnoreCase(tag)) {
					Element option = etask.element("option");
					String threadOrProcess = "process";//清空数据
					if(option.attributeValue("threadOrProcess") != null)
						threadOrProcess = option.attributeValue("threadOrProcess");
					
					//多进程或者多线程执行
					if("process".equalsIgnoreCase(threadOrProcess)){
						DT_MultiProcessExtract taskExec = new DT_MultiProcessExtract();
						taskExec.dataTaskExec(etask,this);
					}else if("thread".equalsIgnoreCase(threadOrProcess)){
						DT_MultiThreadExtract taskExec = new DT_MultiThreadExtract();
						taskExec.dataTaskExec(etask,this);
					}
					
				}else if ("subdatatask".equalsIgnoreCase(tag)) {
					// 执行数据预处理任务
					subDataTaskExec(etask,curTaskId);
				} else if ("custom_task".equalsIgnoreCase(tag)) {
					// 自定义任务情况
					customTaskExec(etask);
				} else if ("initparam".equalsIgnoreCase(tag)) {
					// 初始化节点，什么也不做
				}  else {
					logTaskRun("==============遇到不认识的处理节点：" + tag + " ====  跳过 ！ ==========", "");
				}
			} catch (Exception e) {
				logTaskRun("子任务【" + desc + "】执行中发生异常",ExceptionUtil.getStackTraceMessage(e));
				if(exceptionIgnore != null && "true".equalsIgnoreCase(exceptionIgnore)){
					logTaskRun("子任务异常设置为忽略，继续执行下一任务。");
				}else{
					throw e;//不忽略执行中异常
				}
			}
			
			if (etask.attributeValue("rate") != null) {
				rate = etask.attributeValue("rate");// 进度
			}else{
				if(!isSubTask){
					rate = (100/tasks.size())*(i+1)+"";
				}
			}
			updateTaskRate(rate, "子任务【" + desc + "】执行结束");
			logTaskRun( logPrefix + "子任务" + tag + "【" + desc + "】执行结束。", "");
		}
		
		if(isSubTask){
			logTaskRun("子数据任务=-=" +subtaskName + "== }END =============================================", "");
		}
		
		return DataTaskConst.TASKSTATUS_COMPLETED;
	}
	
	/**
	 * 初始化参数处理，作为变量的默认参数
	 * @throws Exception
	 */
	private void dealInitParams(String taskContent) throws Exception{
		Document doc = DocumentHelper.parseText(taskContent);
		Element root = doc.getRootElement();
		Element paramsEle = root.element("datatask").element("initparam");
		
		if(paramsEle != null){
			@SuppressWarnings("unchecked")
			List<Element> paramsList = paramsEle.elements();
			for(Element paramItem : paramsList){
				String key = paramItem.attributeValue("name");
				String value = paramItem.getText();
				
				if(!sharedPramMap.containsKey(key)){
					sharedPramMap.put(key, value);
				}
			}
		}
	}
	
	/**
	 * 执行数据任务子节点
	 * @param e
	 * @throws Exception
	 */
	private void subDataTaskExec(Element e,String parentTaskId) throws Exception{
		String desc = e.attributeValue("desc");		
		String subMtcode = e.attributeValue("mtcode");
		HashVO subTaskVo = getSubDataTask(subMtcode);
		if(subTaskVo == null){
			String expMsg = "子数据任务【" + desc + "】定义的数据任务MTCODE=" + subMtcode + "未找到对应的元数据！";
			logger.error(expMsg);
			logTaskRun(expMsg,"");
			throw new Exception(expMsg);
		}
		String taskContent = subTaskVo.getStringValue("CONTENT");
		
		//解析子任务变量
		taskContent = StringUtil.buildExpression(sharedPramMap, taskContent);
		
		CommDMO dmo = new CommDMO();
		String curTaskId = dmo.getSequenceNextValByDS(DMOConst.DS_DEFAULT, "S_PUB_DATATASK");
		String sql="INSERT INTO PUB_DATATASK(ID,PARENTTASKID, DATATASKTEMPLETID, NAME, STATUS, TASKCONTENT,PARAMVALUE,BEGINTIME)" +
				" VALUES(?,?,?,?,1,?,?,sysdate)";//执行中状态
		
		dmo.executeUpdateByDS(DMOConst.DS_DEFAULT, sql,curTaskId,parentTaskId,subTaskVo.getStringValue("ID"),
					subTaskVo.getStringValue("NAME"),taskContent,taskVo.getParamValue());
		dmo.commit(DMOConst.DS_DEFAULT);
		int beginIndex = taskLogStr.length();
		
		logTaskRun("开始执行子数据任务【" + desc + "】","子数据任务元数据MTCODE=" + subMtcode);
		String execStatus = DataTaskConst.TASKSTATUS_COMPLETED;
		try{
			execStatus = doAction(taskContent, true,desc,curTaskId);
			
		}catch (Exception ex) {
			execStatus = DataTaskConst.TASKSTATUS_STOPED;
			throw ex;
		}finally{
			logTaskRun("结束执行子数据任务【" + desc + "】", "");
			//int endIndex = taskLogStr.length();
			dmo.executeUpdateClobByDS(DMOConst.DS_DEFAULT, "execlog", "pub_datatask", " id="+curTaskId,taskLogStr.substring(beginIndex));
			dmo.executeUpdateByDS(DMOConst.DS_DEFAULT, "update pub_datatask set status=?,rate=100,endtime=sysdate where id=?",execStatus,curTaskId);
			dmo.commit(DMOConst.DS_DEFAULT);
		}
		
	}

	/**
	 * 自定义数据任务
	 * @param e
	 * @throws Exception
	 */
	private void customTaskExec(Element e) throws Exception {
		String implClass = e.attributeValue("implclass");
		String desc = e.attributeValue("desc");
		if (implClass == null || "".equals(implClass)){
			String expMsg = "自定义数据任务【" + desc + "】，实现类为空！";
			logger.error(expMsg);
			logTaskRun(expMsg,"");
			throw new Exception(expMsg);
		}

		DataTaskExecuteIFC taskExec = (DataTaskExecuteIFC) Class.forName(implClass).newInstance();

		logTaskRun("开始执行自定义数据任务【" + desc + "】，实现类：" + implClass, "");
		taskExec.dataTaskExec(e,this);// 执行自定义数据任务
		logTaskRun("自定义数据任务【" + desc + "】执行结束。", "");
	}

	/**
	 * 从指定FtpServer下载数据文件
	 * @param e 文件下载任务节点
	 * @throws Exception
	 */
	protected void ftpDownload(Element e) throws Exception {
		// 从指定FtpServer下载数据文件
		String server = null, port = null, user = null, passwd = null;
		String remotepath = null, localpath = null, localfile = null, remotefile = null;
		server = e.attributeValue("server");
		port = e.attributeValue("port");
		user = e.attributeValue("user");
		passwd = e.attributeValue("passwd");
		//动态替换文件名
		remotepath = StringUtil.buildExpression( sharedPramMap,e.attributeValue("remotepath") );
		localpath = StringUtil.buildExpression( sharedPramMap,e.attributeValue("localpath") );
		remotefile = StringUtil.buildExpression( sharedPramMap,e.attributeValue("remotefile") );
		localfile = StringUtil.buildExpression( sharedPramMap,e.attributeValue("localfile") );

		//创建下载目录
		FileUtil.createDirIfNotExists(localpath);
		//文件下载
		FtpUtil.downloadFile(server, port, user, passwd, localpath + localfile, remotepath, remotefile);
	}

	/**
	 * ftp上传文件处理
	 * @param e 文件上传任务节点
	 * @throws Exception
	 */
	protected void ftpUpload(Element e) throws Exception {
		String server = null, user = null, passwd = null, remotepath = null, localpath = null, remotefile = null, localfile = null, port = null;

		server = e.attributeValue("server");
		port = e.attributeValue("port");
		user = e.attributeValue("user");
		passwd = e.attributeValue("passwd");
		remotepath = e.attributeValue("remotepath");
		localpath = e.attributeValue("localpath");
		remotefile = e.attributeValue("remotefile");
		localfile = e.attributeValue("localfile");

		FtpUtil.uploadFile(server, port, user, passwd, localpath + localfile, remotepath, remotefile);
	}

	/**
	 * 解析文本文件导入数据库
	 * @param e 文本解析节点
	 * @throws Exception
	 */
	protected void paraseTxt(Element e) throws Exception {
		String srctype = null, filename = null, localPath = null, srcfields = null, srcfieldidxs = null, hastitle = null, tagds = null, tagfields = null, datasplit = null;
		filename = e.attributeValue("filename");
		localPath = e.attributeValue("localPath");
		srctype = e.attributeValue("srctype");
		srcfields = e.attributeValue("srcfields");
		srcfieldidxs = e.attributeValue("srcfieldidxs");
		hastitle = e.attributeValue("hastitle");
		tagds = e.attributeValue("datasource");
		tagfields = e.attributeValue("tagfields");
		datasplit = e.attributeValue("datasplit");
		//filename=StringUtil.buildExpression(param, filename);
		//按照参数整理一下文件，文件名是动态的

		if (srctype.equals("txt")) {
			parseTxt(filename, localPath, datasplit, srcfields, srcfieldidxs, hastitle, tagds, tagfields, e);
		} else {
			throw new Exception("不支持的类型");
		}
	}

	/**
	 * 导出数据到另一数据库 <textarea> <export_db desc="xxx"> <!-- 数据来源设置：
	 * ds/driver/url/uid/upwd 数据源，如果ds不设置，则取driver/url/uid/upwd。 sql
	 * 检索sql，如果sql属性未设置则直接取from的内容。 cols 检索数据设置，col列序号1开始 --> <from ds="ds"
	 * driver="driver" url="url" uid="uid" upwd="upwd" sql="sql"
	 * cols="col1,col2,col3"><![CDATA[sql]]></from> <!-- 目标数据设置： sql
	 * 插入数据sql，如果sql属性未设置则直接取to的内容。 batch 提交的批大小，防止批的数据过多导致溢出 --> <to sql="sql"
	 * batch="10000"><![CDATA[sql]]></to> </export_db> </textarea>
	 * 
	 * @param param
	 * @param e 跨库导数据节点
	 * @throws Exception
	 */
	protected void exportDb(Element e) throws Exception {
		Element efrom = e.element("from");
		String fromds = efrom.attributeValue("datasource");

		String fromdriver = efrom.attributeValue("driver");
		String fromurl = efrom.attributeValue("url");
		String fromuid = efrom.attributeValue("uid");
		String fromupwd = efrom.attributeValue("upwd");
		String fromsql = efrom.attributeValue("sql");

		if (fromsql == null || fromsql.equals(""))
			fromsql = efrom.getText();

		String[] cols = efrom.attributeValue("cols").split(",");
		int[] fromcols = new int[cols.length];

		for (int i = 0; i < cols.length; i++) {
			fromcols[i] = Integer.parseInt(cols[i]);
		}

		Element eto = e.element("to");
		String tods = eto.attributeValue("datasource");
		String tosql = eto.attributeValue("sql");
		if (tosql == null || tosql.equals(""))
			tosql = eto.getText();
		
		Element option = e.element("option");
		String truncateTable = "false";//清空数据
		String disableIndex = "false";//禁用索引
		String analyzeTable = "false";//进行表分析
		String toTable = null;
		
		if(option != null){
			truncateTable = option.attributeValue("truncateTable");
			disableIndex = option.attributeValue("disableIndex");
			analyzeTable = option.attributeValue("analyzeTable");
			toTable = option.attributeValue("toTable");
			if(toTable == null){
				logTaskRun("导入选项中，目标表名不能为空！", "");
				throw new Exception("导入选项中，目标表名不能为空！");
			}
		}
		
		CommDMO dmo = new CommDMO();
		if("true".equalsIgnoreCase(truncateTable)){
			logTaskRun(" 清空表数据: " + toTable, "");
			dmo.executeUpdateByDS(tods, "TRUNCATE TABLE " + toTable);
		}
		if("true".equalsIgnoreCase(disableIndex)){
			logTaskRun(" 禁用表索引: " + toTable, "");
			dmo.executeUpdateByDS(tods, "call drop_ind_table_proc('"+ toTable +"')");
		}

		int batch = Integer.parseInt(eto.attributeValue("batch"));
		
		try {
			if (fromds == null || fromds.equals("")) {
				dmo.executeImportByDS(fromdriver, fromurl, fromuid, fromupwd, fromsql, fromcols, tods, tosql, batch);
			} else {
				dmo.executeImportByDS(fromds, fromsql, fromcols, tods, tosql, batch);
			}
			// fromds都是查询语句，只处理tods
			dmo.commit(tods);
			if("true".equalsIgnoreCase(disableIndex)){
				logTaskRun(" 恢复表索引: " + toTable, "");
				dmo.executeUpdateByDS(tods, "call recover_ind_table_proc('"+ toTable +"')");
			}
			if("true".equalsIgnoreCase(analyzeTable)){
				logTaskRun(" 执行表分析: " + toTable, "");
				dmo.executeUpdateByDS(tods, "ANALYZE TABLE "+toTable+" COMPUTE STATISTICS");
			}
			
		} catch (Exception ex) {
			dmo.rollback(tods);
			logger.error("执行数据库数据导入时异常：", ex);
			logTaskRun("执行数据库数据导入时异常：", ExceptionUtil.getStackTraceMessage(ex));
			throw ex;
		} finally {
			dmo.releaseContext(fromds);
			dmo.releaseContext(tods);
		}
		
	}

	/**
	 * 导出数据到xls文件
	 * @param param
	 * @param e 导出xls文件节点
	 * @throws Exception
	 */
	protected void exportXls(Element e) throws Exception {
		String fileName = null, localPath = null, coltitle = null, coltype = null, fetchds = null, fetchsql = null;
		fileName = e.attributeValue("fileName");
		localPath = e.attributeValue("localPath");
		fetchds = e.attributeValue("datasource");// 数据源

		coltitle = e.elementText("coltitle");
		coltype = e.elementText("coltype");
		fetchsql = e.elementText("fetchsql");
		String[] coltitles = coltitle.split(",");
		String[] coltypes = coltype.split(",");

		// 删除旧文件
		FileUtil.removeFileIfExists(localPath + fileName);
		try {
			// 检索并生成本地文件
			ExportManager epm = new ExportManager();
			epm.setExportType(ExportManager.EXPORT_TYPE_XLS);
			epm.addExportParam("path", localPath + fileName);
			String[][] titles = new String[1][coltitles.length];
			titles[0] = coltitles;
			epm.addExportParam("titles", titles);
			epm.addExportParam("types", coltypes);
			// 获得数据

			HashVO[] vos = (new CommDMO()).getHashVoArrayByDS(fetchds, fetchsql);
			Object[][] objs = (vos.length == 0) ? new Object[0][0] : new Object[vos.length][vos[0].length()];
			for (int i = 0; i < vos.length; i++) {
				objs[i] = vos[i].getM_hData().getValues();
			}
			epm.exportData(objs);
			epm.stopExport();
		} catch (Exception ex) {
			logger.error("导出临时文件发生错误！", ex);
			throw new Exception("导出临时文件发生错误！", ex);
		}
	}

	/**
	 * SQL脚本执行 
	 * <trans><sql><![CDATA[某个脚本]]></sql> </trans>
	 * @param param
	 * @param e sqlOperates节点，执行脚本定义节点
	 * @throws Exception
	 */
	protected void doTrans(Element e) throws Exception {
		CommDMO dmo = new CommDMO();
		String sql = null, sqldesc = null;
		List trans = e.elements("trans");
		// 执行所有事务
		for (int i = 0; i < trans.size(); i++) {
			// 每段事务都是独立的，可以指定的不同的数据源上
			Element tran = (Element) trans.get(i);
			String transDesc = tran.attributeValue("desc");
			String dataSource = tran.attributeValue("datasource");
			String rate = tran.attributeValue("rate");// 进度
			try {

				transDesc = transDesc == null ? "" : transDesc;
				logTaskRun("开始执行第" + (i + 1) + "段事务【" + transDesc + "】...", "");
				List sqls = tran.elements("sql");
				for (int j = 0; j < sqls.size(); j++) {
					Element sqle = (Element) sqls.get(j);
					sql = sqle.getText();
					sqldesc = sqle.attributeValue("desc");
					sqldesc = sqldesc == null ? "" : sqldesc;

					logTaskRun("开始执行第" + (i + 1) + "段事务第" + (j + 1) + "个脚本【" + sqldesc + "】。", sql);
					dmo.executeUpdateByDS(dataSource, sql);
				}
				// 这里按照需要再决定是否启用，可以按照配置文件的trans段进行提交，也可以最后一起提交。
				dmo.commit(dataSource);
				updateTaskRate(rate, "事务：" + transDesc + "执行结束");
				logTaskRun("事务【" + transDesc + "】执行结束。", "");
			} catch (Exception ex) {
				dmo.rollback(dataSource);
				logger.error("执行事务【" + transDesc + "】异常：", ex);
				logTaskRun("执行事务【" + transDesc + "】异常：", ExceptionUtil.getStackTraceMessage(ex));
				throw ex;
			} finally {
				dmo.releaseContext(dataSource);
			}
		}

	}

	//文本数据解析导入实现
	private void parseTxt(String filename, String localPath, String datasplit, String srcfields, String srcfieldidxs,
			String hastitle, String tagds, String tagfields, Element e) throws Exception {

		String[] initctrls = getSubSql(e.element("init-ctrl"));
		String[] linectrls = getSubSql(e.element("line-ctrl"));
		String[] endingctrls = getSubSql(e.element("ending-ctrl"));

		String charset = e.attributeValue("charset");
		if(charset == null || "".equals(charset))
			charset = "GBK";
		
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(localPath + filename), charset));
		//br = new BufferedReader(new FileReader(localPath + filename));

		String[] stitles = null;
		String[] sitems = null;
		int[] sidxs = null;
		String[] tfields = null;
		String line = "";
		try {
			// 判断数据起始行，数据行前面的一行默认为字段名
			if ("true".equals(hastitle)) {
				while ((line = br.readLine().trim()) != null) {
					if(",".equals(datasplit) || ";".equals(datasplit)){
						stitles = StringUtil.splitString2Array(line, datasplit, true);
					}else{
						stitles = line.split(datasplit);
					}
					
					break;
				}
			}

			// 处理源数据字段列表
			if (srcfieldidxs != null && !srcfieldidxs.equals("")) {
				// 设置了源字段序号
				String[] tmp = srcfieldidxs.split(";");
				sidxs = new int[tmp.length];
				for (int i = 0; i < tmp.length; i++) {
					sidxs[i] = Integer.parseInt(tmp[i]);
				}
			} else if (srcfields != null && !srcfields.equals("")) {
				// 设置了源字段名
				String[] tmp = srcfields.split(";");
				ArrayList<String> lst = new ArrayList<String>(Arrays.asList(stitles));

				sidxs = new int[tmp.length];
				for (int i = 0; i < tmp.length; i++) {
					sidxs[i] = lst.indexOf(tmp[i]);
					// 这种数组查找内容的方法可以包装一下，比如给定一系列数据，查找在某个数组中的一系列位置。
				}
			} else {
				sidxs = null;
			}

			// 处理目标数据字段列表，如果tagfields不存在，则自动以源数据里边的字段为准
			if (tagfields == null || tagfields.equals("")) {
				tfields = stitles;
				// 这里还有一种情况，就是源数据里边没有字段，那么在后面字段处理中就直接以源字段序号作为参数名。
				// 比如：update set xxx='{2}', yyy='{3}' where zzz='{1}'
			} else {
				tfields = tagfields.split(";");
			}

			// 判断目标数据字段和源字段
			if (tfields == null && sidxs == null) {
				;// 在每行数据生成的时候处理
			} else if (tfields == null && sidxs != null) {
				tfields = new String[sidxs.length];
				for (int i = 0; i < sidxs.length; i++) {
					tfields[i] = String.valueOf(sidxs[i]);
				}
			} else if (tfields != null && sidxs == null) {
				throw new Exception("来源字段和目标字段不对应，不能处理！");
			} else if (tfields.length != sidxs.length) {
				throw new Exception("来源字段和目标字段不对应，不能处理！");
			} else {
				;// 经过前面判断，来源和目标应该是对应的。
			}

			CommDMO dmo = new CommDMO();
			try {
				// 处理导入数据
				// HashMap pp=(HashMap)p.clone();
				HashMap<String, Object> pp = new HashMap<String, Object>();
				String sql = null;
				// 导入前初始化 initctrls
				for (int i = 0; i < initctrls.length; i++) {
					// sql=StringUtil.buildExpression(pp, initctrls[i]);
					dmo.executeUpdateByDS(tagds, initctrls[i]);
				}
				dmo.commit(tagds);
				logTaskRun("导入前初始化结束。", "");
				
				
				// 导入明细数据
				int rowCount = 0;//计算导入数据数量
				Connection conn = dmo.getConn(tagds).getConn();
				Statement stat = conn.createStatement();
				
				while ((line = br.readLine()) != null) {
					line = line.trim();
					if ("".equals(line)) {
						continue;
					}
					
					if(",".equals(datasplit) || ";".equals(datasplit)){
						sitems = StringUtil.splitString2Array(line, datasplit, true);
					}else{
						sitems = line.split(datasplit);
					}
					// 准备参数
					if (sidxs == null) {
						for (int i = 0; i < sitems.length; i++) {
							pp.put(String.valueOf(i), sitems[i]);
						}
					} else {
						for (int i = 0; i < sidxs.length; i++) {
							pp.put(tfields[i], sitems[sidxs[i]]);
						}
					}

					// 执行明细处理 linectrls,insert语句的变量写法: '[@变量]'
					for (int i = 0; i < linectrls.length; i++) {
						sql = StringUtil.buildExpression(pp, linectrls[i], "[@", "]");
						//dmo.executeUpdateByDS(tagds, sql);
						stat.addBatch(sql);
					}
					rowCount ++;
					if(rowCount %5000 == 0){//每5000条提交一次
						stat.executeBatch();
						dmo.commit(tagds);
					}
				}
				stat.executeBatch();
				dmo.commit(tagds);
				logTaskRun("明细数据导入结束,共导入【"+ rowCount +"】条数据。", "");
				// 导入后收尾 endingctrls
				for (int i = 0; i < endingctrls.length; i++) {
					dmo.executeUpdateByDS(tagds, endingctrls[i]);
				}
				logTaskRun("导入后收尾处理结束。", "");
				dmo.commit(tagds);
			} catch (Exception ex) {
				logger.error(ex.getMessage(), ex);
				dmo.rollback(tagds);
				throw ex;
			} finally {
				dmo.releaseContext(tagds);
			}
		} finally {
			br.close();
		}
	}
	
	//获取元数据编码获取子任务内容
	private HashVO getSubDataTask(String subMtcode) throws Exception{
		CommDMO dmo=new CommDMO();		
		try{
			String sql = "SELECT T.ID,T.NAME,T.CODE,MT.CONTENT FROM PUB_METADATA_TEMPLET MT,PUB_DATATASK_TEMPLET T WHERE T.MTCODE=MT.CODE AND MT.CODE=?";
			HashVO[] vos= dmo.getHashVoArrayByDS(DMOConst.DS_DEFAULT, sql,subMtcode);
			
			if(vos.length > 0)
				return vos[0];
			else
				return null;
		}finally{
			try{dmo.releaseContext(DMOConst.DS_DEFAULT);}catch(Exception e){};
		}
	}
	
	//解析子节点SQL
	private String[] getSubSql(Element e) {
		if (e == null)
			return new String[] {};
		List lst = e.elements("sql");
		int n = lst.size();
		String[] rt = new String[n];
		for (int i = 0; i < n; i++) {
			rt[i] = ((Element) lst.get(i)).getText();
		}
		return rt;
	}

}
