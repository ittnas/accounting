package coreClasses;

public class AccountTest {
	public static void main(String args[]) {
		AccountMapReader reader = new AccountMapReader("testi.acc");
		AccountMap map = reader.readMap();
		System.out.print(reader.printErrors());
		System.out.print(map.getAccounts().toString());
	}
}
