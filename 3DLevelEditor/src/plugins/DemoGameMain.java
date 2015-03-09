package plugins;

import java.util.ArrayList;
import java.util.HashMap;

import common.TLEData;

import javafx.application.Application;
import javafx.stage.Stage;

public class DemoGameMain extends Application{

	public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage s) throws Exception {    	
    	
    	//Load data
    	DemoGameLoader loader = new DemoGameLoader();
    	
    	HashMap<String, ArrayList<TLEData>> data = loader.loadWorldObj();
    	
    	if(data != null){
    		DemoGameWin win = new DemoGameWin(new Stage(), data);
    		win.pluginStart();
    	}
    	else{
    		System.exit(-1);
    	}
        
    }
}