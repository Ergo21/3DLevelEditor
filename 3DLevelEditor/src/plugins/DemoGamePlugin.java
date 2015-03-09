package plugins;

import java.util.ArrayList;
import java.util.HashMap;

import baseProgram.PluginManager;
import common.*;

/**
 * This plugin creates a simple game to test the current World.
 * @author Ergo21
 *
 */

public class DemoGamePlugin extends TLEPlugin {
	
	public DemoGamePlugin(){
		pluginName = "DemoGamePlugin";
	}
	
	/**
	 * Adds new buttons under Plugins/Demo. 
	 * 
	 * @param pM	PluginManager for the Plugin to interact with the Base Program
	 */
	@Override
	public void install(PluginManager pM) {
		mainPlMan = pM;
		mainPlMan.getMWin().addMenuBarItem(event -> createWindow(), "Plugins", "Demo", "Play Current Level");
	}
	
	/**
	 * Creates a 3D Window showing the current level. Activated from "Plugins/Demo/Play Current Level" button. 
	 */
	public void createWindow(){
		DemoGameWin thiDem = new DemoGameWin((HashMap<String, ArrayList<TLEData>>) mainPlMan.getWorld().getData().clone());
		thiDem.pluginStart();
	}
}