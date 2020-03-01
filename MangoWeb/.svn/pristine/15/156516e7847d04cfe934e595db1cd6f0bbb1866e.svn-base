package smartx.bam.flex.modules.userlibmanage
{
	import mx.managers.PopUpManager;
	
	import smartx.flex.components.basic.ClientEnviorment;
	import smartx.flex.components.basic.DeskTopFrame;
	import smartx.flex.components.core.BillListPanel;
	import smartx.flex.components.event.BasicEvent;
	import smartx.flex.components.styletemplate.ifc.ListButtonListener;
	
	/**
	 * sky zhangzz
	 **/
	public class AddJarFileButtonListener implements ListButtonListener
	{
		private var billListPanel:BillListPanel;
		private var desktop:DeskTopFrame = ClientEnviorment.getInstance().getVar(ClientEnviorment.DESKTOP_FRAME) as DeskTopFrame;
		
		public function AddJarFileButtonListener()
		{
			
		}
		
		public function buttonClick(blp:BillListPanel):void{
			this.billListPanel = blp;
			var jfup:JarFileUploadPanel = new JarFileUploadPanel();
			
			jfup.addEventListener(BasicEvent.LOADDATA_SUCCESSFUL,loadDataSuccessHander);
			
			PopUpManager.addPopUp(jfup,desktop,true);
			PopUpManager.centerPopUp(jfup);
			
		}
		
		private function loadDataSuccessHander(event:BasicEvent):void{
			billListPanel.query(false,true,true);
		}

	}
}