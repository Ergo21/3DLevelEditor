package plugins;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.stage.Stage;
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
		DemoGameWin thiDem = new DemoGameWin(new Stage(), createLocalWorld());
		thiDem.pluginStart();
	}
	
	/**
	 * Creates copy of the world to play in so original is not changed.
	 * @return Clone of World
	 */
	public HashMap<String, ArrayList<TLEData>> createLocalWorld(){
		HashMap<String, ArrayList<TLEData>> oriWorld = mainPlMan.getWorld().getData();
		HashMap<String, ArrayList<TLEData>> cloWorld = new HashMap<String, ArrayList<TLEData>>();
		Object[] s = oriWorld.keySet().toArray();
		
		for(int i = 0; i < s.length; i++){
			ArrayList<TLEData> oriLevel = oriWorld.get(s[i]);
			ArrayList<TLEData> cloLevel = new ArrayList<TLEData>();
			for(int j = 0; j < oriLevel.size(); j++){
				cloLevel.add(mainPlMan.getWorld().runCloneTLE(oriLevel.get(j)));
			}
			
			cloWorld.put(s[i].toString(), cloLevel);
		}
		
		return cloWorld;
	}
}