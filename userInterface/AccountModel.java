package userInterface;

import java.util.Vector;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import coreClasses.Account;

public class AccountModel implements TreeModel {

	private Account root;
	private Vector<TreeModelListener> treeModelListeners = new Vector<TreeModelListener>();

	public AccountModel(Account root) {
		this.root = root;

	}

	@Override
	public void addTreeModelListener(TreeModelListener l) {
		treeModelListeners.addElement(l);
	}

	@Override
	public Object getChild(Object parent, int index) {

		Account account = (Account) parent;
		return account.getChildren().get(index);
	}

	@Override
	public int getChildCount(Object parent) {
		return ((Account) parent).getChildren().size();
	}

	@Override
	public int getIndexOfChild(Object parent, Object child) {
		return ((Account) parent).getChildren().indexOf(child);
	}

	@Override
	public Object getRoot() {
		return root;
	}

	@Override
	public boolean isLeaf(Object node) {
		Account account = (Account) node;
		if (account.getChildren().size() == 0) {
			return true;
		} else
			return false;
	}

	@Override
	public void removeTreeModelListener(TreeModelListener arg0) {
		treeModelListeners.removeElement(arg0);

	}

	@Override
	public void valueForPathChanged(TreePath path, Object newValue) {
		System.out.println("*** valueForPathChanged : " + path + " --> "
				+ newValue);

	}
}
