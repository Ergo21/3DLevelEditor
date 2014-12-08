package baseProgram;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;

/*import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;*/

import common.*;
import sun.misc.ClassLoaderUtil;

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
		
		
			Class<? extends TLEPlugin> testPlug = getPlugin();
			
			if(testPlug == null){
				System.out.println("Null returned");
			}
			else if(TLEPlugin.class.isAssignableFrom(testPlug)){
				System.out.println("TLEPlugin isAssignable from TestPlugin");
				//TestPlugin tem = new TestPlugin();
				try {
					TLEPlugin tem = testPlug.newInstance();
					tem.install(this);
					plugins.add(tem);
				}
				catch(Exception e){
					System.out.println(e);
				}
				
			}
		
		
		return plugins;
	}
	
	private Class<? extends TLEPlugin> getPlugin() {
		try{
			JarFile jFile = new JarFile("src/plugins/TestJar.jar");
			Enumeration<JarEntry> e = jFile.entries();
			URL[] urls = {new URL("jar:file:" + "src/plugins/TestJar.jar" + "!/")};
			URLClassLoader cl = URLClassLoader.newInstance(urls);
			
			while(e.hasMoreElements()){
				JarEntry je = (JarEntry) e.nextElement();
				if(je.isDirectory() || !je.getName().endsWith(".class")) {
					continue;
				}
				
				String className = je.getName().substring(0, je.getName().length()-6);
				className = className.replace('/', '.');
				Class c = cl.loadClass(className);
				
				jFile.close();
				System.out.println(c.getName());
				System.out.println(c.getSuperclass());
				
				if(TLEPlugin.class.isAssignableFrom(c)) {
					System.out.println(c.getName() + " Class found to extend TLEPlugin");
					return c;
					//return thiClass.getClass();
				}
				
			}
		}
		catch(Exception e){
			System.out.println("Error");
		}
		
		return null;
		
		/*
		URLClassLoader clazzLoader ;
		Class clazz;
		try {
			//String filePath = "bin/plugins/TestJar.jar";
			//ClassLoaderUtil.addFile(filePath);
			String filePath = "jar:file://" + "bin/plugins/TestJar.jar" + "!/";
			URL url = new File(filePath).toURL();
			clazzLoader = new URLClassLoader(new URL[]{url});
			clazz = clazzLoader.loadClass("plugins/TestPlugin");
			//URLClassLoader uCL = new URLClassLoader(new URL[]{new URL("bin/plugins/TestJar.jar")});
			//Class<?> thiClass = uCL.loadClass("TestPlugin.class");
			//if(TLEPlugin.class.isAssignableFrom(thiClass.getClass())) {
				//System.out.println(thiClass.getName() + " Class found to extend TLEPlugin");
				//return thiClass.getClass();
			//}
			
			
			clazzLoader.close();
			
			if(TLEPlugin.class.isAssignableFrom(clazz.getClass())) {
				System.out.println(clazz.getName() + " Class found to extend TLEPlugin");
				
				return clazz;
			}
		}
		catch(Exception e){
			System.out.println(e);
		}
		
		
		return null; */
		
		/*try {
			System.out.println(new java.io.File(".").getCanonicalPath());
			System.out.println(System.getProperty("user.dir"));
		}
		catch(Exception e){
			
		}
			
		
		Reflections refs = new Reflections(new ConfigurationBuilder()
						.setUrls(ClasspathHelper.forPackage("bin/plugins/TestJar.jar"))
						.setScanners(new SubTypesScanner()));	
		
		//Reflections refs = new Reflections();
		
		Set<Class<? extends TLEPlugin>> thiClass = refs.getSubTypesOf(TLEPlugin.class);
		Iterator<Class<? extends TLEPlugin>> iter = thiClass.iterator();
		
		if(!thiClass.isEmpty()){
			return iter.next();
		}
		
		return null;*/
		
		/*JarEntry thiClass;
		
		try {
			JarInputStream jarIn = new JarInputStream(new FileInputStream("bin/plugins/TestJar.jar"));
			thiClass = jarIn.getNextJarEntry();
			
			while(thiClass != null) {
				System.out.println(thiClass.getClass());
				System.out.println(thiClass.getClass().getSuperclass());
				System.out.println(thiClass.getClass().getSuperclass().getSuperclass());
				
				if(TLEPlugin.class.isAssignableFrom(thiClass.getClass())) {
					System.out.println(thiClass.getName() + " Class found to extend TLEPlugin");
					break;
					//return thiClass.getClass();
				}
				thiClass = jarIn.getNextJarEntry();
			}
			
		}
		catch(Exception e){
			System.out.println("Jar reading exception");
		}
		
		return null;*/
	}
	
	
	public MainWindow getMWin() {
		return thisWindow;
	}
	
	public World getWorld() {
		return thisWorld;
	}

}