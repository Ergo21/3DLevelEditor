package plugins;

import baseProgram.PluginManager;
import common.*;

/**
 * This plugin allows the user to create new items in the editor.
 * @author Ergo21
 *
 */

public class NewItemPlugin extends TLEPlugin {
	
	public NewItemPlugin(){
		pluginName = "NewItemPlugin";
	}
	
	@Override
	public void install(PluginManager pM) {
		mainPlMan = pM;
		mainPlMan.getMWin().addMenuBarItem(event -> createItemWindow(), "File", "New", "New Item in Current Level");
	}
	
	public void createItemWindow(){
		NItemWin thiWin = new NItemWin(mainPlMan);
		thiWin.pluginStart();
	}
}