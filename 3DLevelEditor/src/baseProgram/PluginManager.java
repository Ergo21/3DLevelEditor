package baseProgram;

import java.util.HashMap;

import common.*;
import plugins.*;

public class PluginManager {

	private MainWindow thisWindow;
	private World thisWorld;
	private HashMap<String, TLEPlugin> pluginList;
	
	public PluginManager(MainWindow mW, World w){
		thisWindow = mW;
		thisWorld = w;
		pluginList = new HashMap<String, TLEPlugin>();
	}
	
	public void installPlugins() {
		
		pluginList.put("TestPluginK", new TestPlugin("TestPluginV"));
		
		System.out.println(pluginList.get("TestPluginK").getName());
	}
	

}