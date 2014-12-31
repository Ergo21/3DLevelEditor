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
		mainPlMan.getMWin().addMenuBarItem(event -> createWindow(), "Plugins", "3D Window", "New Window");
	}
	
	public void createWindow(){
		Window3D thiWin = new Window3D(mainPlMan);
		thiWin.pluginStart();
	}
}