package plugins;


import baseProgram.PluginManager;

import common.*;

/**
 * This plugin creates a 3D Window to display the current level.
 * @author Ergo21
 *
 */

public class Window3DPlugin extends TLEPlugin {
	
	public Window3DPlugin(){
		pluginName = "Window3DPlugin";
	}
	
	/**
	 * Adds new buttons under Plugins/3D Window. 
	 * 
	 * @param pM	PluginManager for the Plugin to interact with the Base Program
	 */
	@Override
	public void install(PluginManager pM) {
		mainPlMan = pM;
		mainPlMan.getMWin().addMenuBarItem(event -> createWindow(), "Plugins", "3D Window", "New Window");
	}
	
	/**
	 * Creates a 3D Window showing the current level. Activated from "Plugins/3D Window/New Window" button. 
	 */
	public void createWindow(){
		Window3D thiWin = new Window3D(mainPlMan);
		thiWin.pluginStart();
	}
}