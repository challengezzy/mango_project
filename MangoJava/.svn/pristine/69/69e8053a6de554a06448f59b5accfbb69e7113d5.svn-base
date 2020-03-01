package smartx.framework.metadata.ui;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.lang.reflect.Method;

import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.TransferHandler;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * BillTreePanel的鼠标托放处理类,托放时传送的对象为结点中的UserObject.
 * 
 * 使用说明：假设托放的目的地为target,托放源BillTreePanel为billtree
 * 则调用代码为:
 * target.setDragEnabled(true);
   target.setTransferHandler(billtree.getTransferHandler());
   try {
		 ((BillTreeTransferHandler)billtree.getTransferHandler()).setTargetDealMethod(this, this.getClass().getMethod("托放目的地处理方法名",new Class[]{Object.class}));
	}catch(SecurityException e) {}
 * 
 * 如果想得到托拽时鼠标释放的位置，托放目标需要实现接口DropTargetListener
 * 并设置接收目标DropTarget方法，如:target.setDropTarget(new java.awt.dnd.DropTarget(target,this));
 * 
 * @author sunxf
 * 
 */
public class BillTreeTransferHandler extends TransferHandler {
	private static final long serialVersionUID = 1L;

	private static DataFlavor flavors = null;

	BillTreeTransferable transferable = null;

	DefaultMutableTreeNode node = null;

	private Object obj = null;

	private Method method = null;

	public BillTreeTransferHandler() {
		try {
			flavors = new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean canImport(JComponent comp, DataFlavor[] transferFlavors) {
		for (int i = 0; i < transferFlavors.length; i++) {
			if (flavors.equals(transferFlavors[i]))
				return true;
		}
		return false;
	}

	public int getSourceActions(JComponent c) {
		return TransferHandler.COPY;
	}

	public Transferable createTransferable(JComponent comp) {
		if (comp instanceof JTree) {
			if (node != null) {
				Object userobj = node.getUserObject();
				transferable = new BillTreeTransferable(userobj, flavors);
				return transferable;
			}
		}
		return null;
	}

	public void release() {
		if (transferable != null) {
			transferable.releaseObject();
			transferable = null;
		}

	}

	// 此处调用托放目标所在的类的方法,以实现业务逻辑.
	public boolean importData(JComponent comp, Transferable t) {
		if (comp instanceof JTree)
			return false;
		if (obj != null && method != null && method != null) {
			try {
				Object value = t.getTransferData(flavors);
				method.invoke(obj, new Object[] { value });
				return true;
			} catch (Exception e) {
				System.out.println("BillTreeTransferHandler.importData方法中调用["
						+ obj.getClass().getName() + "]的["
						+ method.getName() + "]方法失败.");
				e.printStackTrace();
			}
		}
		return false;
	}

	public void setTargetDealMethod(Object obj, Method _method) {
		this.obj = obj;
		method = _method;
	}

	public BillTreeTransferable getTransferable() {
		return transferable;
	}

	public DefaultMutableTreeNode getNode() {
		return node;
	}

	public void setNode(DefaultMutableTreeNode node) {
		this.node = node;
	}

}
