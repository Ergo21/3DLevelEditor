package plugins;

import java.util.ArrayList;
import java.util.HashMap;

import common.TLEData;
import common.Global.TLEType;
import javafx.scene.AmbientLight;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Box;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

/**
 * The Level Editor's Main Window, which launches the rest of the program.
 * @author Ergo21
 *
 */

public class DemoGameWin {
	
	private Stage stage;
	private HashMap<String,ArrayList<TLEData>> data;
	private PerspectiveCamera camera;
	private AmbientLight globalLighting;
	private ArrayList<TLEType> litTypes;
	private DGController control;
	private SubScene subScene;
	private Group root;
	
	public DemoGameWin(Stage s, HashMap<String,ArrayList<TLEData>> d){
		data = d;
		stage = s;
	}
	
	public void pluginStart(){

		stage.setWidth(600);
		stage.setHeight(600);
		
		globalLighting = new AmbientLight();
		
		camera = new PerspectiveCamera(true);
		
		
		
		litTypes = new ArrayList<TLEType>();
        litTypes.add(TLEType.ACTIVATOR);
        litTypes.add(TLEType.LIGHT);
        litTypes.add(TLEType.SOUND);

        Scene scene = new Scene(createContent());
        
        control = new DGController(this);
        
        scene.setOnKeyPressed(event->control.handleKeyboard(event));
        scene.setOnKeyReleased(event->control.handleKeyboardRelease(event));
        scene.setOnMouseMoved(event->control.handleMouseMove(event));
        scene.setOnMouseClicked(event->control.handleMouseInput(event));
        scene.setOnMouseDragged(event->control.handleMouseMove(event));
        
        stage.setScene(scene);
        
        stage.show(); 
	}
	
	public void pluginClose() {
		stage.close();
	}
	
	public Parent createContent() {

        camera.getTransforms().add(new Translate(0, 0, -10));
        
        root = new Group();
        
        ArrayList<TLEData> tLev = data.get("CurrentLevel");
        
        if(tLev != null){
        	root.getChildren().addAll(tLev);
        }       	
        root.getChildren().add(globalLighting);
        for(int i = 0; i < tLev.size(); i++){
        	if(litTypes.contains(tLev.get(i).getType())){
        		globalLighting.getScope().add(tLev.get(i));
        	}
        }

        subScene = new SubScene(root, 600, 600, true, SceneAntialiasing.BALANCED);
        subScene.setCamera(camera);
        subScene.setFill(Color.GREY);
        subScene.widthProperty().bind(stage.widthProperty());
        subScene.heightProperty().bind(stage.heightProperty());
        

        return new Group(subScene);
    }
	
	public void changeLevel(String nL){
		ArrayList<TLEData> tLev = data.get(nL);
		
		if(tLev == null){
			return;
		}
		
		data.put("CurrentLevel", tLev);
		
		root.getChildren().clear();
		root.getChildren().addAll(tLev);
		
		globalLighting.getScope().clear();
		for(int i = 0; i < tLev.size(); i++){
        	if(litTypes.contains(tLev.get(i).getType())){
        		globalLighting.getScope().add(tLev.get(i));
        	}
        }
		root.getChildren().add(globalLighting);
		
		control.resetLevel();
	}
	
	public HashMap<String,ArrayList<TLEData>> getData(){
		return data;
	}
	
	public PerspectiveCamera getCamera(){
		return camera;
	}
	
	public Stage getStage(){
		return stage;
	}
	
	public AmbientLight getGLight(){
		return globalLighting;
	}
	
	public Group getRoot(){
		return root;
	}
	
	public SubScene getSubScene(){
		return subScene;
	}
    
}
