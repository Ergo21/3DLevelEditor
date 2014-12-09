package common;

import baseProgram.*;

/**
 * Abstract class used by PluginManager to interact with extended code.
 * @author Ergo21
 *
 */
public abstract class TLEPlugin {
	protected String pluginName;
	protected PluginManager mainPlMan;
	
	/**
	 * The only time the Base Program talks to the Plugin, afterwards only the Plugin talks to the Base Program.
	 * <p>
	 * Hence when overridden will need to do everything you want in that method.
	 * @param pM	PluginManager for the Plugin to interact with the Base Program
	 */
	public void install(PluginManager pM) {
		mainPlMan = pM;
	}
	
	
	/**
	 * Returns the unique name for this plugin. 
	 * @return String name of Plugin
	 */
	public String getName() {
		return pluginName;
	}
}