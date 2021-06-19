package foo;

import aaa.ProjALib;

public class ProjMain {
	
	public ProjMain() {
		instanciate();
	}
	
	private void instanciate() {
		ProjALib lib = new ProjALib();
		lib.callProjA();
	}
	
	public static void main(String args[]) {
		ProjMain main = new ProjMain();
	}
}