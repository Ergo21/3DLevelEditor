package common;

import baseProgram.*;


public abstract class TLEPlugin {
	protected String pluginName;
	protected PluginManager mainPlMan;
	
	public void install(PluginManager pM) {
		mainPlMan = pM;
	}
	
	public String getName() {
		return pluginName;
	}
}