package baseProgram;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import common.*;

/**
 * The Level Editor's Plugin Manager, which loads Plugins that extends TLEPlugin.
 * @author Ergo21
 *
 */

public class PluginManager {

	private MainWindow thisWindow;
	private World thisWorld;
	private ArrayList<String> pluginNames;
	private HashMap<String, TLEPlugin> pluginList;
	
	/**
	 * Constructor for plugin manager, remembers parameters and creates a HashMap for plugins to be added to.
	 * 
	 * @param mW	MainWindow to allow plugins to interact with it.
	 * @param w		World to allow plugins to interact with it.
	 */
	public PluginManager(MainWindow mW, World w){
		thisWindow = mW;
		thisWorld = w;
		pluginList = new HashMap<String, TLEPlugin>();
	}
	
	/**
	 * Installs compatible plugins from plugins folder. 
	 */
	
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
		
	}
	
	String pluginFolder = "src/plugins/";
	
	private ArrayList<TLEPlugin> getPlugins() {
		ArrayList<TLEPlugin> plugins = new ArrayList<TLEPlugin>();
		File[] list = new File(pluginFolder).listFiles();
		
		for(int i = 0; i < list.length; i++) {
			
			if(list[i].getName().endsWith(".jar")) {
				
				Class<? extends TLEPlugin> testPlug = getPlugin(pluginFolder + list[i].getName());
			
				if(testPlug == null){
					System.out.println("Null returned");
				}
				else if(TLEPlugin.class.isAssignableFrom(testPlug)){
					System.out.println("TLEPlugin isAssignable from TestPlugin");
					try {
						TLEPlugin tem = testPlug.newInstance();
						tem.install(this);
						plugins.add(tem);
					}
					catch(Exception e){
						System.out.println(e);
					}
				
				}
			}
		}
		
		
		return plugins;
	}
	
	private Class<? extends TLEPlugin> getPlugin(String classDir) {
		try{
			JarFile jFile = new JarFile(classDir);
			Enumeration<JarEntry> ent = jFile.entries();
			URL[] urls = {new URL("jar:file:" + classDir + "!/")};
			URLClassLoader cl = URLClassLoader.newInstance(urls);
			
			while(ent.hasMoreElements()){
				JarEntry je = (JarEntry) ent.nextElement();
				
				if(je.isDirectory() || !je.getName().endsWith(".class")) {
					continue;
				}
				
				String className = je.getName().substring(0, je.getName().length()-6);
				className = className.replace('/', '.');
				Class<?> c = cl.loadClass(className);
				
				
				if(TLEPlugin.class.isAssignableFrom(c)) {
					System.out.println(c.getName() + " Class found to extend TLEPlugin");
					jFile.close();
					return (Class<? extends TLEPlugin>) c;
				}
				jFile.close();
				
			}
		}
		catch(Exception e){
			System.out.println("Error in PluginManager.getPlugin():" + e);
		}
		
		return null;
	}
	
	/**
	 * Returns the names of the loaded plugins.
	 * @return PluginNames to check what has been loaded.
	 */
	public ArrayList<String> getLoadedPluginsN(){
		return pluginNames;
	}
	
	
	/**
	 * Returns the plugins loaded by manager. Unadvised.
	 * @return PluginsLoaded to allow plugin interaction.
	 */
	public HashMap<String, TLEPlugin> getLoadedPluginsP(){
		return pluginList;
	}
	
	/**
	 * Returns the MainWindow passed in constructor.
	 * @return	MainWindow for plugins to interact with.
	 */
	public MainWindow getMWin() {
		return thisWindow;
	}
	
	/**
	 * Returns the World passed in constructor.
	 * @return	World for plugins to interact with.
	 */
	public World getWorld() {
		return thisWorld;
	}

}