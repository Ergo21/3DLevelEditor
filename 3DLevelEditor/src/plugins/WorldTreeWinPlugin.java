package plugins;

import baseProgram.PluginManager;
import common.*;

/**
 * This plugin displays the overall structure of the World as a TreeView and Table.
 * @author Ergo21
 *
 */

public class WorldTreeWinPlugin extends TLEPlugin {
	private WorldTreeWin ygg;
	
	public WorldTreeWinPlugin(){
		pluginName = "WorldTreeWinPlugin";
	}
	
	/**
	 * Adds new buttons under Plugins/3D Model Loader. 
	 * 
	 * @param pM	PluginManager for the Plugin to interact with the Base Program
	 */
	@Override
	public void install(PluginManager pM) {
		mainPlMan = pM;
		ygg = new WorldTreeWin(mainPlMan);
		mainPlMan.getMWin().addMenuBarItem(event -> ygg.createNewLevel(), "File", "New", "Level");
		mainPlMan.getMWin().addMenuBarItem(event -> showYggdrasil(), "Plugins", "Data Tree", "New Tree Windows");
		mainPlMan.getMWin().addMenuBarItem(event -> ygg.ragnarok(), "File", "Delete", "Delete empty levels");
	}
	
	/**
	 * Method passed to start the Tree Window.
	 */
	public void showYggdrasil(){
		
    	ygg.pluginStart();
	}
}