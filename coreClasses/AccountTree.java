package coreClasses;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseAdapter;

import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

public class AccountTree extends JTree {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public AccountTree(TreeModel model) {
		super(model);
		
		
		MouseListener ml = new MouseAdapter() {
		     public void mousePressed(MouseEvent e) {
		         if(SwingUtilities.isRightMouseButton(e)){
		         int selRow = getRowForLocation(e.getX(), e.getY());
		         TreePath selPath = getPathForLocation(e.getX(), e.getY());
		                 setSelectionPath(selPath); 
		                 if (selRow>-1){
		                    setSelectionRow(selRow); 
		                 }
		     }
		 }
		};
		 this.addMouseListener(ml);
	}
	
	public void expandAll() {
		for(int i = 0; i <this.getRowCount(); i++) {
			expandRow(i);
		}
	}
	
	
}
