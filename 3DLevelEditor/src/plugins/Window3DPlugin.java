package plugins;


import baseProgram.PluginManager;

import common.*;

public class Window3DPlugin extends TLEPlugin {
	
	public Window3DPlugin(){
		pluginName = "Window3DPlugin";
	}
	
	@Override
	public void install(PluginManager pM) {
		mainPlMan = pM;
	}
}