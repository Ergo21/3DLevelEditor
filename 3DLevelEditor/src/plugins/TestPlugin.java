package plugins;


import baseProgram.PluginManager;

import common.*;

public class TestPlugin extends TLEPlugin {
	TestMethods tm;
	
	public TestPlugin(){
		pluginName = "TestPlugin";
	}
	
	@Override
	public void install(PluginManager pM) {
		mainPlMan = pM;
		tm = new TestMethods();
		
		
		mainPlMan.getMWin().addMenuBarItem(event -> localMethod(), "Tests", "Local Method", "New Method");
		mainPlMan.getMWin().addMenuBarItem(event -> tm.printButton1(), "Tests", "Foreign Method 1");
		mainPlMan.getMWin().addMenuBarItem(event -> tm.printButton2(), "Tests", "Foreign Method 2");
	}
	
	public void localMethod() {
		System.out.println("Local method called");
	}
}