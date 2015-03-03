package plugins;

import common.TLEData;
import common.Global.TLEType;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.DrawMode;
import javafx.scene.transform.Scale;
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
	private TextField itemSize;
	private TextArea actiArea;
	
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
		itemComboBox.getItems().addAll("Activator", "Cube", "Point Light");
		nameField = new TextField();
		colourRed = new TextField();
		colourGreen = new TextField();
		colourBlue = new TextField();
		colourAlpha = new TextField();
		itemSize = new TextField();
		actiArea = new TextArea();
		actiArea.setPrefWidth(200);
		
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
		grid.add(okayBut, 0, 10);
		
		cancBut = new Button("Cancel");
		cancBut.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent e) {
				stage.close();
			}
		});
		grid.add(cancBut, 1, 10);
		
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
		grid.add(okayBut, 0, 10);
		grid.add(cancBut, 1, 10);
		
		switch(item){
			case "":{
			}
			break;
			case "Activator":{
				grid.add(new Label("Activator Script"), 0, 2);
				grid.add(actiArea, 0, 3);
			}
			break;
			case "Cube":{
				grid.add(new Label("Size"), 0, 2);
				grid.add(itemSize, 1, 2);
				grid.add(new Label("Colour RGBA (0-255)"), 0, 3);
				grid.add(new Label("Red"), 0, 4);
				grid.add(colourRed, 1, 4);
				grid.add(new Label("Green"), 0, 5);
				grid.add(colourGreen, 1, 5);
				grid.add(new Label("Blue"), 0, 6);
				grid.add(colourBlue, 1, 6);
				grid.add(new Label("Alpha"), 0, 7);
				grid.add(colourAlpha, 1, 7);
			}
			break;
			case "Point Light":{
				grid.add(new Label("Colour RGBA (0-255)"), 0, 2);
				grid.add(new Label("Red"), 0, 3);
				grid.add(colourRed, 1, 3);
				grid.add(new Label("Green"), 0, 4);
				grid.add(colourGreen, 1, 4);
				grid.add(new Label("Blue"), 0, 5);
				grid.add(colourBlue, 1, 5);
				grid.add(new Label("Alpha"), 0, 6);
				grid.add(colourAlpha, 1, 6);
				
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
			case "Activator":{
				TLEData newItem = new TLEData(nameField.getText(), nameField.getText()+1, TLEType.ACTIVATOR);
				Box actiMesh = new Box(1, 1, 1);
	        	actiMesh.setMaterial(new PhongMaterial(Color.RED));
	        	actiMesh.getTransforms().add(new Scale(2, 2, 2));
	        	actiMesh.setDrawMode(DrawMode.LINE);
	        	newItem.setMesh(actiMesh);
	        	newItem.setActivator(actiArea.getText());
	        	pMRef.getWorld().getData().get("CurrentLevel").add(newItem);
	        	pMRef.getWorld().runResetWindow();
	        	System.out.println("Added activator");
	        	System.out.println("Code: " + newItem.getActivator());
			}
			break;
			case "Cube":{
				System.out.println("Creating cube");
				try{
					System.out.println("R: " + colourRed.getText() + 
										" G: " + colourGreen.getText() +
										" B: " + colourBlue.getText() +
										" A: " + colourAlpha.getText());
					int r = Integer.parseInt(colourRed.getText());
					double red = (double)r/255; 
					int g = Integer.parseInt(colourGreen.getText());
					double green = (double)g/255; 
					int b = Integer.parseInt(colourBlue.getText());
					double blue = (double)b/255; 
					int a = Integer.parseInt(colourAlpha.getText());
					double alpha = (double)a/255; 
					int size = Integer.parseInt(itemSize.getText());
					System.out.println("Doubles created");
					System.out.println("R: " + red + 
							" G: " + green +
							" B: " + blue +
							" A: " + alpha);
					
					TLEData newItem = new TLEData(nameField.getText(), nameField.getText()+1, TLEType.CUBE);
					Box cubeMesh = new Box(1, 1, 1);
					Color c = new Color(red, green, blue, alpha);
		        	cubeMesh.setMaterial(new PhongMaterial(c));
		        	cubeMesh.getTransforms().add(new Scale(size, size, size));
		        	newItem.setMesh(cubeMesh);
		        	newItem.setColour(c);
		        	pMRef.getWorld().getData().get("CurrentLevel").add(newItem);
		        	pMRef.getWorld().runResetWindow();
		        	System.out.println("Added box");
				}
				catch (Exception e){
					System.out.println(e.getMessage());
				}	
			}
			break;
			case "Point Light":{
				System.out.println("Creating point light");
				try{
					System.out.println("R: " + colourRed.getText() + 
										" G: " + colourGreen.getText() +
										" B: " + colourBlue.getText() +
										" A: " + colourAlpha.getText());
					int r = Integer.parseInt(colourRed.getText());
					double red = (double)r/255; 
					int g = Integer.parseInt(colourGreen.getText());
					double green = (double)g/255; 
					int b = Integer.parseInt(colourBlue.getText());
					double blue = (double)b/255; 
					int a = Integer.parseInt(colourAlpha.getText());
					double alpha = (double)a/255; 
					System.out.println("Doubles created");
					System.out.println("R: " + red + 
							" G: " + green +
							" B: " + blue +
							" A: " + alpha);
					
					TLEData newItem = new TLEData(nameField.getText(), nameField.getText()+1, TLEType.LIGHT);
					newItem.setLight(new PointLight(
									new Color(red, green, blue, alpha)));
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