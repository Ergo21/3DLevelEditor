package plugins;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import baseProgram.PluginManager;

import common.*;

/**
 * Creates a 3D Window to display the world.
 * @author Ergo21
 */

public class WorldTreeWin {
	private PluginManager pMRef;
	private Stage stage;
	TreeItem<String> rootItem;
	
	public WorldTreeWin(PluginManager pM) {
		pMRef = pM;
	}
	
	/**
	 *  Sets up plugin with stage, camera, and listeners
	 */
	public void pluginStart() {
		
		stage = new Stage(StageStyle.UTILITY);
		stage.setWidth(200);
		stage.setHeight(400);
		stage.setTitle("Yggdrasil");       
		
		pMRef.getWorld().addResetWindow("WindowTree", ()->resetWindow());
	        
	    rootItem = new TreeItem<String> ("World");
	    rootItem.setExpanded(true);
	    
	    rootItem = createContent(rootItem);
	    
	    TreeView<String> tree = new TreeView<String> (rootItem);        
	    StackPane root = new StackPane();
	    root.getChildren().add(tree);
	    Scene subScene = new Scene(root, 200, 400);
	    stage.setScene(subScene);
	    
	    stage.show();
	}
	
	public void pluginClose() {
		stage.close();
	}

	/**
	 * Creates all objects shown. Will be overwritten to load from Data when complete.
	 * @return Scene to add to Stage
	 */
    public TreeItem<String> createContent(TreeItem<String> root) {
        
        HashMap<String, ArrayList<TLEData>> tWor = pMRef.getWorld().getData();
        String[] keys = new String[1];
        tWor.keySet().toArray(keys);		
        
        if(tWor != null){   	
        	for(int i = 0; i < keys.length; i++){
        		TreeItem<String> item = new TreeItem<String> (keys[i]);
        		//tWor.get(keys[i]).get(0)
        		root.getChildren().add(item);
        	}
        }

        return root;
    }    

    public void resetWindow(){
    	rootItem.getChildren().clear();
    	rootItem = createContent(rootItem);
    }
}