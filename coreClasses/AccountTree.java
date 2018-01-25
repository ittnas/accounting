package coreClasses;

import javax.swing.JTree;
import javax.swing.tree.TreeModel;

public class AccountTree extends JTree {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public AccountTree(TreeModel model) {
		super(model);
	}
	
	public void expandAll() {
		for(int i = 0; i <this.getRowCount(); i++) {
			expandRow(i);
		}
	}
}
