package plugins;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Cell;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
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
	TreeItem<YggItem> rootItem;
	
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
	        
	    rootItem = new TreeItem<YggItem> (new YggItem("World", new TLEData("World", "Yggdrasil")));
	    rootItem.setExpanded(true);
	    
	    rootItem = createContent(rootItem);
	    
	    TreeView<YggItem> tree = new TreeView<YggItem> (rootItem);   
	    /*tree.setCellFactory(new Callback<TreeView<String>, TreeCell<String>>(){
	    	@Override
	    	public TreeCell<String> call(TreeView<String> p) {
	    		return new YggCell();
	    	}
 	    });*/
	    //tree.set
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
    public TreeItem<YggItem> createContent(TreeItem<YggItem> root) {
        
        HashMap<String, ArrayList<TLEData>> tWor = pMRef.getWorld().getData();
        String[] keys = new String[0];		
        
        if(tWor != null){ 
        	keys = tWor.keySet().toArray(keys);
        	for(int i = 0; i < keys.length; i++){
        		TreeItem<YggItem> item = new TreeItem<YggItem> (new YggItem(keys[i], new TLEData(keys[i], keys[i] + " Tree")));
        		if(tWor.get(keys[i]).size() > 0){
        			for(int j = 0; j < tWor.get(keys[i]).size(); j++){
        				YggItem item2 = new YggItem(tWor.get(keys[i]).get(j).getName(), tWor.get(keys[i]).get(j));
        				item2.setOnMouseClicked(new EventHandler<MouseEvent>(){

							@Override
							public void handle(MouseEvent e) {
								if(e.getButton() == MouseButton.SECONDARY){
									ContextMenu m = new ContextMenu();
									MenuItem mI1 = new MenuItem("Load Mesh Into Current World");
					        		mI1.setOnAction(new EventHandler<ActionEvent>() {
					            		public void handle(ActionEvent t){
					            			System.out.println("Loaded model into current.");
					            			TLEData item3 = new TLEData(item2.getTLEData().getName(), item2.getTLEData().getID() + "01");
					            			//item3.setMesh(item2.getTLEData().getMesh());
					            			//Group g = new Group();
					            			//g.clone();
					            			item2.getTLEData().getChildren().add(item3);
					            			pMRef.getWorld().getData().get("CurrentLevel").add(item3);
					            			pMRef.getWorld().runResetWindow();
					            		}
					            	});
					        		m.getItems().add(mI1);
					        		m.show(stage, e.getScreenX(), e.getScreenY());
								}
							}
            				
            			});
        				TreeItem<YggItem> item3 = new TreeItem<YggItem>(item2);
        				
            			item.getChildren().add(item3);
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
    
    private class YggItem extends Text{
    	private TLEData item;
    	
    	public YggItem(String s, TLEData t){
    		super(s);
    		item = t;
    	}
    	
    	public TLEData getTLEData(){
    		return item;
    	}
    }
    
    private class YggCell extends TreeCell<String>{
    	private TextField textField;
    	private ContextMenu addMenu = new ContextMenu();
    	
    	public YggCell(){
    		System.out.println(getTreeItem());
    		addMenu.getItems().addAll(createMenu());
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
    	
    	private ArrayList<MenuItem> createMenu(){
    		ArrayList<MenuItem> m = new ArrayList<MenuItem>();
    		if(isWithin("Meshes", getTreeItem())){
    			MenuItem mI1 = new MenuItem("Load Mesh Into Current World");
        		mI1.setOnAction(new EventHandler<ActionEvent>() {
            		public void handle(ActionEvent t){
            			System.out.println("Loaded model into current.");
            			pMRef.getWorld().runResetWindow();
            		}
            	});
        		m.add(mI1);
    		}
    		
    		
    		/*EventHandler<ActionEvent> a1 = pMRef.getMWin().getButton("Load New Model");
    		MenuItem mI1 = new MenuItem("Load New Mesh");
    		if(a1 == null){
    			mI1.setOnAction(new EventHandler<ActionEvent>() {
        			public void handle(ActionEvent t){
        				System.out.println("Suitable loader not installed.");
        			}
        		});
    		}
    		else {
    			mI1.setOnAction(a1);
    		}
    		m.add(mI1);*/
    			
    		
    		return m;
    	}
    	
    	private boolean isWithin(String s, TreeItem<String> t){
    		if(t == null){
    			return false;
    		}
    		System.out.println(t.getValue());
    		if(t.getValue().equals(s)){
    			return true;
    		}
    		
    		if(t.getParent() == null){
    			return false;
    		}
    		
    		if(t.getParent().getValue().equals("World")){
    			return false;
    		}
    		else{
    			return isWithin(s, t.getParent());
    		}
    		
    	}
    	
    	private String getString(){
    		return getItem() == null ? "" : getItem().toString();
    	}
    }
}