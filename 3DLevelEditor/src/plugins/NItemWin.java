package plugins;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import baseProgram.PluginManager;

public class NItemWin {
	private PluginManager pMRef;
	private Stage stage;
	
	public NItemWin(PluginManager p){
		pMRef = p;
	}
	
	public void pluginStart(){

		stage = new Stage();
		stage.setWidth(200);
		stage.setHeight(200);
		stage.resizableProperty().set(false);
		stage.setTitle("Create Item");

        Scene scene = createContent();
        
        stage.setScene(scene);
        
        stage.show(); 
	}
	
	public Scene createContent(){
		Scene curScene = new Scene(new Group(), 200, 200);
		ComboBox<String> itemComboBox = new ComboBox<String>();
		itemComboBox.getItems().addAll("Point Light");
		
		itemComboBox.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent e) {
				System.out.println("Action " + itemComboBox.getValue());
			}
		});
		
		GridPane grid = new GridPane();
		grid.setVgap(4);
		grid.setHgap(10);
		grid.setPadding(new Insets(5,5,5,5));
		grid.add(new Label("Item Type"), 0, 0);
		grid.add(itemComboBox,1,0);
		
		Button okayBut = new Button("Create");
		okayBut.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent e) {
				stage.close();
			}
		});
		grid.add(okayBut, 0, 5);
		
		Button cancBut = new Button("Cancel");
		cancBut.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent e) {
				stage.close();
			}
		});
		grid.add(cancBut, 1, 5);
		Group root = (Group) curScene.getRoot();
		root.getChildren().add(grid);
		
		return curScene;
	}
}