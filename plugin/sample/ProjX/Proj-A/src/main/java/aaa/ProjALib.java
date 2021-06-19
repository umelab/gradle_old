package aaa;

import bbb.ProjBLib;

public class ProjALib {
	private ProjBLib lib = null;
	
	public ProjALib() {
		lib = new ProjBLib();
	}
	
	public void callProjA() {
		System.out.println("Called callProjA method");
		if (lib != null) {
			lib.execute();
		}
	}
	
}
