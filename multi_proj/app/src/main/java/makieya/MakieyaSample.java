package makieya;

import makieya.lib.SampleLib;
import makieya.net.SampleNet;
import makieya.aa.SampleAA;

public class MakieyaSample {
	
	public MakieyaSample(){
		SampleLib lib = new SampleLib();
		System.out.println("Makieya Constructor:" + lib.getLibname());
		System.out.println("Create net component: " + new SampleNet().getNetname());
		System.out.println("Create aa component: " + new SampleAA().getAAname());
	}
	
	public static void main(String args[]) {
		new MakieyaSample();
	}
}
