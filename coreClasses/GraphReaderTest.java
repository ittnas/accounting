package coreClasses;

import java.util.ArrayList;
import java.util.HashMap;

public class GraphReaderTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		HashMap<String, Account> accounts = new HashMap<String, Account>();
		Account jee = new Account("jee", "en");
		Account joo = new Account("joo", "en");
		AccountMap map = new AccountMap(accounts);
		map.addAccount(jee);
		map.addAccount(joo);
		GraphReader reader = new GraphReader("graph_test.grp", map);
		ArrayList<GraphState> res = reader.readGraph();
		res.toString();
	}

}
