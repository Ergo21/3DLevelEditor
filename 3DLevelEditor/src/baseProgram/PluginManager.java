package baseProgram;

import java.util.ArrayList;
import java.util.HashMap;

import common.*;
import plugins.*;

public class PluginManager {

	private MainWindow thisWindow;
	private World thisWorld;
	private ArrayList<String> pluginNames;
	private HashMap<String, TLEPlugin> pluginList;
	
	public PluginManager(MainWindow mW, World w){
		thisWindow = mW;
		thisWorld = w;
		pluginList = new HashMap<String, TLEPlugin>();
	}
	
	public void installPlugins() {
		pluginNames = new ArrayList<String>();
		ArrayList<TLEPlugin> plugins = getPlugins();
		
		if(plugins.size() <= 0){
			return;
		}
		
		for(int i = 0; i < plugins.size(); i++) {
			pluginNames.add(plugins.get(i).getName());
			pluginList.put(plugins.get(i).getName(), plugins.get(i));
		}
		
		//pluginList.put("TestPluginK", new TestPlugin("TestPluginV"));
		
		//System.out.println(pluginList.get("TestPluginK").getName());
		
		
		/*public void install(){
			mainWindow.addButton("Name", event -> localFunction());
			LocalPluginClass plug = ETC.
			mainWindow.addButton("Name2", event -> plug.foreignFunction());
		}*/
		
	}
	
	private ArrayList<TLEPlugin> getPlugins() {
		ArrayList<TLEPlugin> plugins = new ArrayList<TLEPlugin>();
		
		if(TLEPlugin.class.isAssignableFrom(TestPlugin.class)){
			TestPlugin tem = new TestPlugin();
			tem.install(this);
			plugins.add(tem);
		}
		
		
		return plugins;
	}
	
	
	public MainWindow getMWin() {
		return thisWindow;
	}
	
	public World getWorld() {
		return thisWorld;
	}

}