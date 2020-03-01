package smartx.bam.flex.modules
{
	import flash.display.Sprite;
	
	import smartx.bam.flex.modules.alertmessage.AlertMsgDelButtonListener;
	import smartx.bam.flex.modules.alertmessage.AlertMsgSubButtonListener;
	import smartx.bam.flex.modules.alertmessage.TaskAssignButtonListener;
	import smartx.bam.flex.modules.analyzeview.AnalyzeViewExportButtonListener;
	import smartx.bam.flex.modules.analyzeview.AnalyzeViewImportButtonListener;
	import smartx.bam.flex.modules.analyzeview.AnalyzeViewQueryButtonListener;
	import smartx.bam.flex.modules.analyzeview.AnalyzeViewStartButtonListener;
	import smartx.bam.flex.modules.analyzeview.AnalyzeViewStopButtonListener;
	import smartx.bam.flex.modules.analyzeview.AnalyzeviewItemChangeListener;
	import smartx.bam.flex.modules.businessscenario.CreateRuleButtonListener;
	import smartx.bam.flex.modules.businessscenario.ScenarioExportButtonListener;
	import smartx.bam.flex.modules.businessscenario.ScenarioImportButtonListener;
	import smartx.bam.flex.modules.businessscenario.ScenarioItemChangeEventListener;
	import smartx.bam.flex.modules.businessscenario.TempleteRuleCreateButtonListener;
	import smartx.bam.flex.modules.businessscenario.TempleteRuleDelButtonListener;
	import smartx.bam.flex.modules.businessscenario.TempleteRuleEditButtonListener;
	import smartx.bam.flex.modules.dashboard.DBExportButtonListener;
	import smartx.bam.flex.modules.dashboard.DBImportButtonListener;
	import smartx.bam.flex.modules.dashboardobject.DBOExportButtonListener;
	import smartx.bam.flex.modules.dashboardobject.DBOImportButtonListener;
	import smartx.bam.flex.modules.dataanalyze.DataAnalyzeClientInterceptor;
	import smartx.bam.flex.modules.dataanalyze.DataAnalyzeDesigner;
	import smartx.bam.flex.modules.dataanalyze.DataAnalyzeExportListener;
	import smartx.bam.flex.modules.dataanalyze.DataAnalyzeImportListener;
	import smartx.bam.flex.modules.dataanalyze.DataAnalyzeTaskDesigner;
	import smartx.bam.flex.modules.dataanalyze.DboInsertClientInterceptor;
	import smartx.bam.flex.modules.dataanalyze.DboViewListener;
	import smartx.bam.flex.modules.dataprofile.AnalyzerDesignListener;
	import smartx.bam.flex.modules.dataprofile.AnalyzerToTaskListener;
	import smartx.bam.flex.modules.datasource.ExportdsButtonListener;
	import smartx.bam.flex.modules.datasource.ImportdsButtonListener;
	import smartx.bam.flex.modules.datasource.RelationDBAdvButtonListener;
	import smartx.bam.flex.modules.datasource.ViewDSInfoButtonListener;
	import smartx.bam.flex.modules.datatask.StartAndRunTaskListener;
	import smartx.bam.flex.modules.datatask.ViewExtractTaskLogListener;
	import smartx.bam.flex.modules.dqc.listener.AdvDataGridTestClickListener;
	import smartx.bam.flex.modules.dqc.listener.ChkAndDoEmTreeItemClickListener;
	import smartx.bam.flex.modules.dqc.listener.ChkDetailListButtonListener;
	import smartx.bam.flex.modules.dqc.listener.ExcelExportBtnListener;
	import smartx.bam.flex.modules.dqc.listener.GroupEntityRuleClickListener;
	import smartx.bam.flex.modules.dqc.listener.HisEntityRuleClickListener;
	import smartx.bam.flex.modules.dqc.listener.OnuDeviceExportListener;
	import smartx.bam.flex.modules.dqc.listener.OnuPortExportListener;
	import smartx.bam.flex.modules.dqc.listener.RuleCheckTreeItemClickListener;
	import smartx.bam.flex.modules.dqc.listener.RuleListClickListener;
	import smartx.bam.flex.modules.dqc.listener.TNNeExportListener;
	import smartx.bam.flex.modules.dqc.listener.fttb.DetailFTTBMAC0ListBtnListener;
	import smartx.bam.flex.modules.dqc.listener.fttb.DetailFTTBMAC1ListBtnListener;
	import smartx.bam.flex.modules.dqc.listener.fttb.DetailFTTBMAC2ListBtnListener;
	import smartx.bam.flex.modules.dqc.listener.fttb.DetailFTTBMAC3ListBtnListener;
	import smartx.bam.flex.modules.dqc.listener.fttb.DetailFTTBMAC4ListBtnListener;
	import smartx.bam.flex.modules.entitymodel.listener.DQEmDesignCardButtonListener;
	import smartx.bam.flex.modules.entitymodel.listener.DQEmExportListButtonListener;
	import smartx.bam.flex.modules.entitymodel.listener.DQEmImportListButtonListener;
	import smartx.bam.flex.modules.entitymodel.listener.DQEmRuleAfInterceptor;
	import smartx.bam.flex.modules.entitymodel.listener.DQEmRuleDesignCardButtonListener;
	import smartx.bam.flex.modules.entitymodel.listener.DQEmRuleDesignTaskButtonListener;
	import smartx.bam.flex.modules.entitymodel.listener.DQEmRuleExportButton;
	import smartx.bam.flex.modules.entitymodel.listener.DQEmRuleImportButton;
	import smartx.bam.flex.modules.entitymodel.listener.DQEmRuleInsertBfInterceptor;
	import smartx.bam.flex.modules.entitymodel.listener.DQEmRuleUpdateBfInterceptor;
	import smartx.bam.flex.modules.entitymodel.listener.DQEmUpdateAfInterceptor;
	import smartx.bam.flex.modules.entitymodel.listener.EmDesignCardButtonListener;
	import smartx.bam.flex.modules.entitymodel.listener.EmInsertAfInterceptor;
	import smartx.bam.flex.modules.entitymodel.listener.EmUpdateAfInterceptor;
	import smartx.bam.flex.modules.entitymodel.listener.ExportEntityModelListener;
	import smartx.bam.flex.modules.entitymodel.listener.ImportEntityModelListener;
	import smartx.bam.flex.modules.eventadapter.ExporteventButtonListener;
	import smartx.bam.flex.modules.eventadapter.ImporteventButtonListener;
	import smartx.bam.flex.modules.eventadapter.StartListButtonListener;
	import smartx.bam.flex.modules.eventadapter.StopListButtonListener;
	import smartx.bam.flex.modules.gis.widget.DrawTestWidget;
	import smartx.bam.flex.modules.queryview.QueryViewExportButtonListener;
	import smartx.bam.flex.modules.queryview.QueryViewImportButtonListener;
	import smartx.bam.flex.modules.syssetting.RefreshCacheListButtonListener;
	import smartx.bam.flex.modules.task.listener.AddTaskCommentButtonListener;
	import smartx.bam.flex.modules.task.listener.TaskTypeManagerListener;
	import smartx.bam.flex.modules.userlibmanage.AddJarFileButtonListener;
	import smartx.bam.flex.modules.userlibmanage.DelJarFileButtonListener;
	import smartx.flex.components.core.mtchart.listener.MTChartClickListener;
	import smartx.flex.components.util.render.NumberColorRenderer;

	/**
	 * sky zhangzz
	 **/
	public class CustomLibrary extends Sprite
	{
		private var startListButtonListener:StartListButtonListener;
		private var stopListButtonListener:StopListButtonListener;
		private var refreshCacheListButtonListener:RefreshCacheListButtonListener;
		private var relationDBAdvButtonListener:RelationDBAdvButtonListener;
		private var importds:ImportdsButtonListener;
		private var exportds:ExportdsButtonListener;
		private var importevent:ImporteventButtonListener;
		private var exportevent:ExporteventButtonListener;
		private var addTaskCommentBtnListener:AddTaskCommentButtonListener;
		private var createRuleButtonListener:CreateRuleButtonListener;
		private var addJarFileButtonListener:AddJarFileButtonListener;
		private var delJarFileButtonListener:DelJarFileButtonListener;
		private var taskTypeManagerListener:TaskTypeManagerListener;
		private var alertMsgDelButtonListener:AlertMsgDelButtonListener;
		private var taskAssignButtonListener:TaskAssignButtonListener;
		private var alertMsgSubButtonListener:AlertMsgSubButtonListener;
		private var templeteRuleCreateButtonListener:TempleteRuleCreateButtonListener;
		private var templeteRuleDelButtonListener:TempleteRuleDelButtonListener;
		private var templeteRuleEditButtonListener:TempleteRuleEditButtonListener;
		private var scenarioItemChangeEventListener:ScenarioItemChangeEventListener;
		private var scenarioExportButtonListener:ScenarioExportButtonListener;
		private var scenarioImportButtonListener:ScenarioImportButtonListener;
		private var queryViewExportButtonListener:QueryViewExportButtonListener;
		private var queryViewImportButtonListener:QueryViewImportButtonListener;
		private var dboExportButtonListener:DBOExportButtonListener;
		private var dboImportButtonListener:DBOImportButtonListener;
		private var dbImportButtonListener:DBImportButtonListener;
		private var dbExportButtonListener:DBExportButtonListener;
		private var analyzeViewExportButtonListener:AnalyzeViewExportButtonListener;
		private var analyzeViewImportButtonListener:AnalyzeViewImportButtonListener;
		private var analyzeviewItemChangeListener:AnalyzeviewItemChangeListener;
		private var analyzeViewQueryButtonListener:AnalyzeViewQueryButtonListener;
		private var analyzeViewStartButtonListener:AnalyzeViewStartButtonListener;
		private var analyzeViewStopButtonListener:AnalyzeViewStopButtonListener;
		private var chkDetailListButtonListener:ChkDetailListButtonListener;
		private var emDesignCardButtonListener:EmDesignCardButtonListener;
		private var chkAndDoEmTreeItemClickListener:ChkAndDoEmTreeItemClickListener;
		private var ruleCheckTreeItemClickListener:RuleCheckTreeItemClickListener;
		private var emInsertAfInterceptor:EmInsertAfInterceptor;
		private var emUpdateAfInterceptor:EmUpdateAfInterceptor;
		private var dtw:DrawTestWidget;
		private var viewDSInfoButtonListener:ViewDSInfoButtonListener;
		private var dQEmRuleUpdateBfInterceptor:DQEmRuleUpdateBfInterceptor;
		private var analyzerDesignListener:AnalyzerDesignListener;
		private var toTaskLis:AnalyzerToTaskListener;
//		private var reportAVGOnlineTimeListener:ReportAVGOnlineTimeListener;
		
		private var dqEmDesignCardButtonListener:DQEmDesignCardButtonListener;
		private var dqEmUpdateAfInterceptor:DQEmUpdateAfInterceptor;
		
		private var onuportExportListener:OnuPortExportListener;
		private var onudeviceExportListener:OnuDeviceExportListener;
		private var neExportListener:TNNeExportListener;
		private var excelExportListener:ExcelExportBtnListener;//EXLE文件导出
		private var dQEmRuleDesignCardButtonListener:DQEmRuleDesignCardButtonListener;
		
		private var dQEmRuleAfInterceptor:DQEmRuleAfInterceptor;
		private var dQEmRuleInsertBfInterceptor:DQEmRuleInsertBfInterceptor;
		private var dQEmRuleDesignTaskButtonListener:DQEmRuleDesignTaskButtonListener;
		
		private var dqEmExportListButtonListener:DQEmExportListButtonListener;
		private var dqEmImportListButtonListener:DQEmImportListButtonListener;
		
		private var detailFttbMac0:DetailFTTBMAC0ListBtnListener;
		private var detailFttbMac1:DetailFTTBMAC1ListBtnListener;
		private var detailFttbMac2:DetailFTTBMAC2ListBtnListener;
		private var detailFttbMac3:DetailFTTBMAC3ListBtnListener;
		private var detailFttbMac4:DetailFTTBMAC4ListBtnListener;
		
		private var groupEntityRuleClickListener:GroupEntityRuleClickListener;
		private var hisEntityRuleClickListener:HisEntityRuleClickListener;
		private var ruleListClickListener:RuleListClickListener;
		
		private var startRunTaskListener:StartAndRunTaskListener;
		private var viewTaskLogListener:ViewExtractTaskLogListener;
		
		private var dQEmRuleExportButton:DQEmRuleExportButton;
		private var dQEmRuleImportButton:DQEmRuleImportButton;
		private var expEntityModelLstr:ExportEntityModelListener;
		private var impEntityModelLstr:ImportEntityModelListener;
		
		private var dataAnalyzeDesigner:DataAnalyzeDesigner;
		private var dataAnalyzeClientInterceptor:DataAnalyzeClientInterceptor;
		private var dboInsertClientInterceptor:DboInsertClientInterceptor;
		private var dboViewListener:DboViewListener;
		private var dataAnalyzeTaskDesigner:DataAnalyzeTaskDesigner;
		private var dataAnalyzeExportListener:DataAnalyzeExportListener;
		private var dataAnalyzeImportListener:DataAnalyzeImportListener;
		
		private var numberColorRenderer:NumberColorRenderer;
		
		private var chartClickListener:MTChartClickListener;
		private var adgTestListener:AdvDataGridTestClickListener;
		
	}
}