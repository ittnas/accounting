package userInterface;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;

import coreClasses.Account;

public class TreeTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Account root = new Account("juuri", "Tämä tili on juuri.");
		Account tili1 = new Account("1", "tili1");
		Account tili2 = new Account("2", "tili2");
		Account tili3 = new Account("3", "tili3");
		Account tili4 = new Account("4", "tili4");
		Account tili5 = new Account("5", "tili5");
		Account tili6 = new Account("6", "tili6");
		Account tili7 = new Account("7", "tili7");
		Account tili8 = new Account("8", "tili8");
		Account tili9 = new Account("9", "tili9");
		Account tili10 = new Account("10", "tili10");
		root.addChild(tili1);
		tili1.addChild(tili2);
		tili2.addChild(tili3);
		tili1.addChild(tili4);
		tili4.addChild(tili5);
		tili4.addChild(tili6);
		tili6.addChild(tili7);
		tili2.addChild(tili8);
		tili5.addChild(tili9);
		tili9.addChild(tili10);
		AccountModel model = new AccountModel(root);
		
		JTree tree = new JTree(model);
		for(int i = 0;i < tree.getRowCount(); i++) {
			tree.expandRow(i);
		}
		tree.setShowsRootHandles(false);
		JScrollPane pane = new JScrollPane(tree);
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel panel = new JPanel();
		panel.add(pane);
		frame.add(panel);
		frame.pack();
		frame.setVisible(true);
	}

}
