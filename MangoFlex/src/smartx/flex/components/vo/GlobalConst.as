package smartx.flex.components.vo
{
	public class GlobalConst
	{
		public static const SERVICE_FORM:String = "smartXFormService";
		public static const SERVICE_JOB:String = "smartXJobService";
		public static const SERVICE_WORKFLOW:String = "smartXWorkflowService";
		public static const SERVICE_CEP:String = "smartXCEPService";
		public static const SERVICE_METADATATEMPLET:String = "smartXMetadataTempletService";
		
		public static const ITEMCOMPONENTMODE_CARD:int = 1;
		public static const ITEMCOMPONENTMODE_LIST:int = 2;
		public static const ITEMCOMPONENTMODE_CONDITION:int = 3;
		
		public static const KEYNAME_MODIFYFLAG:String = "FORMSERVICE_MODIFYFLAG";
		public static const KEYNAME_TEMPLETCODE:String = "FORMSERVICE_TEMPLETCODE";
		
		public static const MTCODE_SYSTEMCONFIG:String = "MT_SYSTEMCONFIG";
		public static const MTCODE_SYNONYMS:String = "MT_SYNONYMS";
		public static const MTCODE_DATATASKTYPE:String = "MT_DATATASKTYPE";
		public static const MTCODE_ENTITY_MODEL:String = "MT_ENTITY_MODEL";
		public static const MTCODE_INITMETADATA:String = "mt_initmetadata";
		public static const MTCODE_DQCCHECKITEM:String = "MT_DQCCHECKITEM";
		
		public static const KEYNAME_SHAREDOBJECT_USERINFO:String = "userInfo"; 
		public static const KEYNAME_SHAREDOBJECT_USERSTYLE:String = "userStyle"; 
		public static const KEYNAME_SHAREDOBJECT_MTINFO:String = "mtInfo";
		public static const KEYNAME_SHAREDOBJECT_FILTERINFO:String = "filterInfo";
		public static const KEYNAME_SHAREDOBJECT_MT_NAMES:String = "somtInfo";//所有保存在SharedObject元数据编码集合
		
		public static const LAYOUTTYPE_BILLCARDPANEL_VBOX:String = "VBOX"; 
		public static const LAYOUTTYPE_BILLCARDPANEL_TILE:String = "TILE"; 
		
		public static const DEFAULTPROVIDERNAME_CEP:String = "DEFAULTPROVIDERNAME";
		
		public static const CEP_DATASOURCE_PREFIX:String = "datasource_cep_";
		
		public static const MODE_PIVOTCOMPONENT_SQL:String = "SQL";
		public static const MODE_PIVOTCOMPONENT_CONFIGFILE:String = "CONFIGFILE";
		public static const MODE_PIVOTCOMPONENT_MONDRIAN:String = "MONDRIAN";
		
		//分页查询是使用
		public static const DATA_ROWCOUNT:String = "ROWCOUNT";
		public static const DATA_SIMPLEHASHVOARRAY:String = "SIMPLEHASHVOARRAY";
		public static const DATA_DATAMAP:String = "dataMap";
		
		public static const DBAUTH_READONLY:String = "readonly";
		public static const DBAUTH_READWRITE:String = "readwrite";
		
		public static const TEMPDATAFOLDER:String = "download/";
		
		public static const MSG_CHANNELSET_AMF:String = "my-amf";
		public static const MSG_CHANNELSET_STREAMING:String = "my-streaming-amf";
		public static const MSG_CHANNELSET_POLLING:String = "my-polling-amf";
		public static const MSG_DESTINATION:String = "smartx_message_push";
		
		//billlistPanel过滤器类型
		public static const FILTER_TYPE_DICTIONARY:String ="dictionary";//字典过滤器
		public static const FILTER_TYPE_EQUALVALUE:String ="equalValue";//等值过滤器
		public static const FILTER_TYPE_NULLVALUE:String ="nullValue";//空值过滤器
		public static const FILTER_TYPE_NUMBERSCOPE:String ="numberScope";//数值范围过滤器
		public static const FILTER_TYPE_STRINGLENGTH:String ="stringLength";//字符长度过滤器
		public static const FILTER_TYPE_STRINGSIMILAR:String ="stringSimilar";//字符模糊匹配过滤器
		public static const FILTER_TYPE_ROWCOUNT:String ="rowCount";//返回行数上限
		public static const FILTER_TYPE_RELATION:String = "relation";
		public static const FILTER_TYPE_SQL:String = "sql";
		public static const FILTER_TYPE_DATETIME:String = "datetime";
		
		//菜单摆放方式
		public static const MENU_TYPE_HORIZONTAL:String = "horizontal";//水平
		public static const MENU_TYPE_VERTICAL:String = "vertical";//垂直
		public static const MENU_TYPE_BIGIMAGE:String = "bigImage";//水平大图标
		
		//风格模板中操作按钮显示方式
		public static const STYLETEMPLATE_OPERATION_MENU:String = "menu";//菜单方式显示
		public static const STYLETEMPLATE_OPERATION_BUTTON:String = "button";//按钮方式显示
		
		//风格模板编辑界面的元数据
		public static const STYLETEMPLATE_EDIT_METADATAXML:XML  = <root>
	<templetCode>T_PUB_METADATA_TEMPLET</templetCode>
	<editable>true</editable>
	<customListButton>
		<button id="flushServerCacheButton" label="刷新服务端缓存" icon="smartx/flex/modules/assets/images/refresh.png">
			<listeners>
				<listener>smartx.flex.modules.basic.system.mtdesigner.FlushServerCacheButtonListener</listener>
			</listeners>
		</button>
		<button id="flushClientCacheButton" label="刷新客户端缓存" icon="smartx/flex/modules/assets/images/flush.png">
			<listeners>
				<listener>smartx.flex.modules.basic.system.mtdesigner.FlushClientCacheButtonListener</listener>
			</listeners>
		</button>
		<button id="importButton" label="导入" toolTip="导入" icon="smartx/flex/modules/assets/images/import.png">
			<listeners>
				<listener>smartx.flex.modules.basic.system.mtdesigner.ImportMetadataButtonListener</listener>
			</listeners>
		</button>
		<button id="exportButton" label="导出" toolTip="导出" icon="smartx/flex/modules/assets/images/export.png">
			<listeners>
				<listener>smartx.flex.modules.basic.system.mtdesigner.ExportMetadataButtonListener</listener>
			</listeners>
		</button>
	</customListButton>
 <customCardButton>
		<button id="designButton" label="设计" icon="smartx/flex/modules/assets/images/design.png">
			<listeners>
				<listener>smartx.flex.modules.basic.system.mtdesigner.DesignButtonListener</listener>
			</listeners>
		</button>
 </customCardButton>
 <interceptors>
 	<afIncList>smartx.publics.form.bs.interceptors.impl.DeleteMTAfInterceptorImpl</afIncList>
	<clientInsertAfIncCard>smartx.flex.modules.basic.system.mtdesigner.FlushCacheAfInterceptor</clientInsertAfIncCard>
	<clientUpdateAfIncCard>smartx.flex.modules.basic.system.mtdesigner.FlushCacheAfInterceptor</clientUpdateAfIncCard>
	<clientInsertBfIncCard>smartx.flex.modules.basic.system.mtdesigner.MetadataUpdateBfInterceptor</clientInsertBfIncCard>
	<clientUpdateBfIncCard>smartx.flex.modules.basic.system.mtdesigner.MetadataUpdateBfInterceptor</clientUpdateBfIncCard>
</interceptors>
</root>;   
	}
}