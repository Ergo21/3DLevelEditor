package plugins;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import baseProgram.PluginManager;
import common.*;

/**
 * Creates a Tree to view the world, and a Table to view the Current Level.
 * @author Ergo21
 */

public class WorldTreeWin {
	private PluginManager pMRef;
	private Stage stage;
	private Stage curLevel;
	private TreeItem<YggItem> rootItem;
	private TableView<TabItem> table;
	ObservableList<TabItem> curLev;
	
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
		
		pMRef.getWorld().addResetWindow("WindowTree", ()->resetWindowTree());
	        
	    rootItem = new TreeItem<YggItem> (new YggItem("World", new TLEData("World", "Yggdrasil", "NA")));
	    rootItem.setExpanded(true);
	    
	    rootItem = createContentTree(rootItem);
	    
	    TreeView<YggItem> tree = new TreeView<YggItem> (rootItem); 
	    StackPane root = new StackPane();
	    root.getChildren().add(tree);
	    Scene subScene = new Scene(root, 200, 400);
	    stage.setScene(subScene);
	    
	    stage.show();
	    
	    curLevel = new Stage(StageStyle.UTILITY);
		curLevel.setWidth(200);
		curLevel.setHeight(200);
		curLevel.setTitle("Current Level");   
		
		pMRef.getWorld().addResetWindow("WindowCurrentLevel", ()->createCurLevList());
		
		table = createContentTable();
		createCurLevList();
		
		Scene curScene = new Scene(new Group());
		((Group)curScene.getRoot()).getChildren().add(table);
		table.prefWidthProperty().bind(curScene.widthProperty());
		table.prefHeightProperty().bind(curScene.heightProperty());
		
		curLevel.setScene(curScene);
		
		curLevel.show();
	}
	
	public void pluginClose() {
		stage.close();
		curLevel.show();
	}

	/**
	 * Creates all objects shown. Will be overwritten to load from Data when complete.
	 * @return Scene to add to Stage
	 */
    public TreeItem<YggItem> createContentTree(TreeItem<YggItem> root) {
        
        HashMap<String, ArrayList<TLEData>> tWor = pMRef.getWorld().getData();
        String[] keys = new String[0];		
        
        if(tWor != null){ 
        	keys = tWor.keySet().toArray(keys);
        	for(int i = 0; i < keys.length; i++){
        		if(!keys[i].equals("CurrentLevel") && !keys[i].startsWith("Level")){
        			root.getChildren().add(createLeaf(tWor, keys[i]));
        		}
        		else if(keys[i].startsWith("Level")){
        			TreeItem<YggItem> levBra = new TreeItem<YggItem> (new YggItem("Levels", new TLEData("Levels", "Levels" + " Tree", "NA")));
        			boolean found = false;
        			for(int j = 0; j < root.getChildren().size(); j++){
        				if(root.getChildren().get(j).getValue().getText().equals("Levels")){
        					levBra = root.getChildren().get(j);
        					found = true;
        					break;
        				}
        			}	
        			
        			if(!found){
        				root.getChildren().add(levBra);
        			}

        			levBra.getChildren().add(createLeaf(tWor, keys[i]));
        		}
        	}
        }

        return root;
    }    
    
    private TreeItem<YggItem> createLeaf(HashMap<String, ArrayList<TLEData>> tWor, String key){
    	TreeItem<YggItem> item = new TreeItem<YggItem> (new YggItem(key, new TLEData(key, key + " Tree", "NA")));
		
		if(tWor.get(key).size() > 0){
			for(int j = 0; j < tWor.get(key).size(); j++){
				YggItem item2 = new YggItem(tWor.get(key).get(j).getName(), tWor.get(key).get(j));
				
				item2.setOnMouseClicked(createContextMenu(key, item2));
				TreeItem<YggItem> item3 = new TreeItem<YggItem>(item2);
			
				item.getChildren().add(item3);
			}
		}
		return item;
    }
    
    private EventHandler<MouseEvent> createContextMenu(String key, YggItem curItem){
    	EventHandler<MouseEvent> eve;
    	if(key.startsWith("Level")){
    		eve = new EventHandler<MouseEvent>(){

    			@Override
    			public void handle(MouseEvent e) {
    				if(e.getButton() == MouseButton.SECONDARY){
    					ContextMenu m = new ContextMenu();
    					
    					MenuItem mI1 = new MenuItem("New Level");
    					mI1.setOnAction(new EventHandler<ActionEvent>() {
                			public void handle(ActionEvent t){
                				createNewLevel();
                			}
                		});
            			m.getItems().add(mI1);
    					
    					MenuItem mI2 = new MenuItem("Load Level");
            			mI2.setOnAction(new EventHandler<ActionEvent>() {
                			public void handle(ActionEvent t){
                				pMRef.getWorld().getData().put("CurrentLevel", pMRef.getWorld().getData().get(key));
                				pMRef.getWorld().runResetWindow();
                				System.out.println("Loaded level.");
                			}
                		});
            			m.getItems().add(mI2);
            			
            			MenuItem mI3 = new MenuItem("Delete Level");
            			mI3.setOnAction(new EventHandler<ActionEvent>() {
                			public void handle(ActionEvent t){
                				pMRef.getWorld().getData().remove(key);
                				pMRef.getWorld().runResetWindow();
                				System.out.println("Deleted level.");
                			}
                		});
            			m.getItems().add(mI3);
            			
            			m.show(stage, e.getScreenX(), e.getScreenY());
    				}
    			}
    		
    		};
    	}
    	else{
    		eve = new EventHandler<MouseEvent>(){

    			@Override
    			public void handle(MouseEvent e) {
    				if(e.getButton() == MouseButton.SECONDARY){
    					ContextMenu m = new ContextMenu();
    					MenuItem mI1 = new MenuItem("Load Mesh Into Current Level");
            			mI1.setOnAction(new EventHandler<ActionEvent>() {
                			public void handle(ActionEvent t){
                				TLEData item3 = pMRef.getWorld().runModelLoader(curItem.getTLEData().getMeshPath());				          
                				if(item3 != null){
                					pMRef.getWorld().getData().get("CurrentLevel").add(item3);
                    				pMRef.getWorld().runResetWindow();
                    				System.out.println("Loaded model into current level.");
                				}          				
                			}
                		});
            			m.getItems().add(mI1);
            			m.show(stage, e.getScreenX(), e.getScreenY());
    				}
    			}
    		
    		};
    	}
    	
    	return eve;
    }
    
    public void createNewLevel(){
    	String[] keys = new String[0];		
        keys = pMRef.getWorld().getData().keySet().toArray(keys);
        int levNo = 1;
        for(int i = 0; i < keys.length; i++){
        	if(keys[i].startsWith("Level")){
        		String number = keys[i].substring(5);
        		System.out.println(number);
        		int number2 = Integer.parseInt(number);
        		System.out.println(number2);
        		if(number2 >= levNo){
        			levNo = number2 + 1;
        		}
        	}
        }
        
        ArrayList<TLEData> newLev = new ArrayList<TLEData>();
        TLEData t1 = new TLEData("Cube", "Cube 1", "NA");
        Box c1 = new Box(1,1,1);
        c1.setMaterial(new PhongMaterial(Color.ORANGE));
        t1.setMesh(c1);
        newLev.add(t1);
		pMRef.getWorld().getData().put("Level" + levNo, newLev);
		
		pMRef.getWorld().runResetWindow();
		System.out.println("Created new level.");
    }
    
    private TableView<TabItem> createContentTable(){
    	TableView<TabItem> thiTab = new TableView<TabItem>();
    	
    	curLev = FXCollections.observableArrayList();
    	
    	TableColumn<TabItem, String> name = new TableColumn<TabItem, String>("Name");
    	name.setCellValueFactory(new PropertyValueFactory<>("itName"));
    	TableColumn<TabItem, String> id = new TableColumn<TabItem, String>("ID");
    	id.setCellValueFactory(new PropertyValueFactory<>("itID"));
    	TableColumn<TabItem, String> path = new TableColumn<TabItem, String>("Path");
    	path.setCellValueFactory(new PropertyValueFactory<>("itPath"));
    	thiTab.setItems(curLev);
    	thiTab.getColumns().add(name);
    	thiTab.getColumns().add(id);
    	thiTab.getColumns().add(path);
    	
    	return thiTab;
    }
    
    private void createCurLevList(){
    	ArrayList<TLEData> data = pMRef.getWorld().getData().get("CurrentLevel");
    	curLev.clear();
    	
    	for(int i = 0; i < data.size(); i++){
    		TabItem tI = new TabItem(data.get(i).getName(), data.get(i).getID(), data.get(i).getMeshPath()); 
    		curLev.add(tI);
    	}
    }
    
    /**
     *	Method passed to world to reset this window.
     */
    public void resetWindowTree(){
    	rootItem.getChildren().clear();
    	rootItem = createContentTree(rootItem);
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
    
    public class TabItem{
    	private StringProperty itName;
    	private StringProperty itID;
    	private StringProperty itPath;
    	
    	private TabItem(String name, String id, String path){
    		itName = new SimpleStringProperty(name);
    		itID = new SimpleStringProperty(id);
    		itPath = new SimpleStringProperty(path);
    	}
    	
    	 public StringProperty itNameProperty() {
             return itName;
         }
  
         public void setName(String name) {
             itName.set(name);
         }
  
         public StringProperty itIDProperty() {
             return itID;
         }
  
         public void setID(String i) {
             itID.set(i);
         }
  
         public StringProperty itPathProperty() {
             return itPath;
         }
  
         public void setPath(String p) {
             itPath.set(p);
         }
    }
    
}