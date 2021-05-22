package makieya.lib;

import makieya.util.SampleUtil;

public class SampleLib {
	
	public String getLibname() {
		return "Sample Lib " + new SampleUtil().getUtilname();
	}
	
}