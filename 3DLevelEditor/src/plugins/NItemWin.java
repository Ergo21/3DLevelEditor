package plugins;

import common.TLEData;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.DrawMode;
import javafx.stage.Stage;
import baseProgram.PluginManager;

public class NItemWin {
	private PluginManager pMRef;
	private Stage stage;
	private TextField nameField;
	private ComboBox<String> itemComboBox;
	private Button okayBut;
	private Button cancBut;
	
	private TextField colourRed;
	private TextField colourGreen;
	private TextField colourBlue;
	private TextField colourAlpha;
	
	public NItemWin(PluginManager p){
		pMRef = p;
	}
	
	public void pluginStart(){

		stage = new Stage();
		stage.setWidth(400);
		stage.setHeight(400);
		stage.setTitle("Create Item");

        Scene scene = createContent();
        
        stage.setScene(scene);
        
        stage.show(); 
	}
	
	public Scene createContent(){
		Scene curScene = new Scene(new Group(), 200, 200);
		
		itemComboBox = new ComboBox<String>();
		itemComboBox.getItems().addAll("Cube", "Point Light");
		nameField = new TextField();
		colourRed = new TextField();
		colourGreen = new TextField();
		colourBlue = new TextField();
		colourAlpha = new TextField();
		
		GridPane grid = new GridPane();
		grid.setVgap(4);
		grid.setHgap(10);
		grid.setPadding(new Insets(5,5,5,5));
		grid.add(new Label("Name"), 0, 0);
		grid.add(nameField, 1, 0);
		grid.add(new Label("Item Type"), 0, 1);
		grid.add(itemComboBox, 1, 1);
		
		okayBut = new Button("Create");
		okayBut.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent e) {
				createItem(itemComboBox.getValue());
				stage.close();
			}
		});
		grid.add(okayBut, 0, 5);
		
		cancBut = new Button("Cancel");
		cancBut.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent e) {
				stage.close();
			}
		});
		grid.add(cancBut, 1, 5);
		
		itemComboBox.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent e) {
				createInputs(grid, itemComboBox.getValue());
				System.out.println("Action " + itemComboBox.getValue());
			}
		});
		
		Group root = (Group) curScene.getRoot();
		root.getChildren().add(grid);
		
		return curScene;
	}
	
	private void createInputs(GridPane grid, String item){
		grid.getChildren().clear();
		
		grid.add(new Label("Name"), 0, 0);
		grid.add(nameField, 1, 0);
		grid.add(new Label("Item Type"), 0, 1);
		grid.add(itemComboBox, 1, 1);
		grid.add(okayBut, 0, 5);
		grid.add(cancBut, 1, 5);
		
		switch(item){
			case "":{
			}
			break;
			case "Cube":{
				
			}
			break;
			case "Point Light":{
				grid.add(new Label("Colour RGBA (0-255)"), 0, 2);
				grid.add(colourRed, 0, 3);
				grid.add(colourGreen, 1, 3);
				grid.add(colourBlue, 0, 4);
				grid.add(colourAlpha, 1, 4);
				
			}
		}
	}
	
	private void createItem(String item){
		if(item == null){
			return;
		}
		switch(item){
			case "":{
			}
			break;
			case "Cube":{
			
			}
			break;
			case "Point Light":{
				System.out.println("Creating point light");
				try{
					System.out.println("R: " + colourRed.getText() + 
										" G: " + colourGreen.getText() +
										" B: " + colourBlue.getText() +
										" A: " + colourAlpha.getText());
					int red = Integer.parseInt(colourRed.getText());
					int green = Integer.parseInt(colourGreen.getText());
					int blue = Integer.parseInt(colourBlue.getText());
					int alpha = Integer.parseInt(colourAlpha.getText());
					System.out.println("Ints created");
					
					TLEData newItem = new TLEData(nameField.getText(), nameField.getText()+1, "NA");
					newItem.setLight(new PointLight(
									new Color(red/255, green/255, blue/255, alpha/255)));
					Box lightMesh = new Box(2,2,2);
		        	lightMesh.setMaterial(new PhongMaterial(Color.YELLOW));
		        	lightMesh.setDrawMode(DrawMode.LINE);
		        	newItem.setMesh(lightMesh);
		        	pMRef.getWorld().getData().get("CurrentLevel").add(newItem);
		        	pMRef.getWorld().runResetWindow();
		        	System.out.println("Added point light");
				}
				catch (Exception e){
					System.out.println(e.getMessage());
				}			
			}
			break;
		}
	}
}