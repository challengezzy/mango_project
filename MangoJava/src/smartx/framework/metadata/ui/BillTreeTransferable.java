package smartx.framework.metadata.ui;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
/**
 * BillTreePanel的鼠标托放数据存放类.
 * @author sunxf
 */
public class BillTreeTransferable implements Transferable {
	private Object value = null;

	private DataFlavor flavor = null;

	public BillTreeTransferable(Object _value, DataFlavor _flavor) {
		this.value = _value;
		this.flavor = _flavor;
	}

	public Object getTransferData(DataFlavor flavor)
			throws UnsupportedFlavorException, IOException {
		if (flavor.equals(flavor))
			return value;
		return null;
	}

	public DataFlavor[] getTransferDataFlavors() {
		return new DataFlavor[]{flavor};
	}

	public boolean isDataFlavorSupported(DataFlavor flavor) {
		if(flavor.equals(flavor))
			return true;
		return false;
	}
	public void releaseObject()
	{
		this.value = null;
	}
}
