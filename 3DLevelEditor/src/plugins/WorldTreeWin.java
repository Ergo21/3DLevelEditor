package plugins;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
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
	    tree.setCellFactory(new Callback<TreeView<String>, TreeCell<String>>(){
	    	@Override
	    	public TreeCell<String> call(TreeView<String> p) {
	    		return new YggCell();
	    	}
 	    });
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
        String[] keys = new String[0];		
        
        if(tWor != null){ 
        	keys = tWor.keySet().toArray(keys);
        	for(int i = 0; i < keys.length; i++){
        		TreeItem<String> item = new TreeItem<String> (keys[i]);
        		if(tWor.get(keys[i]).size() > 0){
        			for(int j = 0; j < tWor.get(keys[i]).size(); j++){
            			TreeItem<String> item2 = new TreeItem<String> (tWor.get(keys[i]).get(j).getName());
            			
            			item.getChildren().add(item2);
            		}
        		}      		
        		
        		root.getChildren().add(item);
        	}
        }

        return root;
    }    

    public void resetWindow(){
    	rootItem.getChildren().clear();
    	rootItem = createContent(rootItem);
    }
    
    private class YggCell extends TreeCell<String>{
    	private TextField textField;
    	private ContextMenu addMenu = new ContextMenu();
    	
    	public YggCell(){
    		MenuItem addMenuItem = new MenuItem("Load Mesh");
    		addMenu.getItems().add(addMenuItem);
    		addMenuItem.setOnAction(new EventHandler<ActionEvent>() {
    			public void handle(ActionEvent t){
    				System.out.println(t.getEventType());
    			}
    		});
    	}
    	
    	@Override
    	public void updateItem(String item, boolean empty){
    		super.updateItem(item, empty);
    		
    		if(empty){
    			setText(null);
    			setGraphic(null);
    		}
    		else{
    			setText(getString());
    		}
    		
    		if(getTreeItem() != null && getTreeItem().isLeaf()){
    			setContextMenu(addMenu);
    		}
    	}
    	
    	private String getString(){
    		return getItem() == null ? "" : getItem().toString();
    	}
    }
}