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
import javafx.scene.text.Text;
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
		//curScene.widthProperty().
		
		curLevel.setScene(curScene);
		
		curLevel.show();
	}
	
	public void pluginClose() {
		stage.close();
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
        		if(!keys[i].equals("CurrentLevel")){
        			TreeItem<YggItem> item = new TreeItem<YggItem> (new YggItem(keys[i], new TLEData(keys[i], keys[i] + " Tree", "NA")));
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
					            				TLEData item3 = new TLEData(item2.getTLEData().getName(), item2.getTLEData().getID(), item2.getTLEData().getMeshPath());
					            				item3.setMesh(item2.getTLEData().getMesh());					          
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
        }

        return root;
    }    
    
    public TableView<TabItem> createContentTable(){
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