package baseProgram;

import java.util.ArrayList;
import java.util.HashMap;

import common.*;

public class World {
	MainWindow rootWindow;
	HashMap<String, ArrayList<TLEData>> worldData;
	
	public World(MainWindow mW){
		rootWindow = mW;
	}
}