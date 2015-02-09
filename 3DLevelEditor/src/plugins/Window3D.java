package plugins;

import java.util.ArrayList;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.paint.Color;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import baseProgram.PluginManager;
import common.*;

/**
 * Creates a 3D Window to display the world.
 * @author Ergo21
 */

public class Window3D {
	private PluginManager pMRef;
	private W3DController control;
	private Stage stage;
	private SubScene subScene;
	private PerspectiveCamera camera;
	private Group root;
	
	public Window3D(PluginManager pM) {
		pMRef = pM;
	}
	
	/**
	 *  Sets up plugin with stage, camera, and listeners
	 */
	public void pluginStart() {
		
		stage = new Stage(StageStyle.UTILITY);
		stage.setWidth(200);
		stage.setHeight(200);
		
		camera = new PerspectiveCamera(true);

        Scene scene = new Scene(createContent());
        
        control = new W3DController(pMRef, camera, stage, subScene, root);
        
        pMRef.getWorld().addResetWindow("Window3D", ()->control.resetWindow());
        
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

	/**
	 * Creates all objects shown. Will be overwritten to load from Data when complete.
	 * @return Scene to add to Stage
	 */
    public Parent createContent() {

        camera.getTransforms().add(new Translate(0, 0, -10));

        root = new Group();
        
        ArrayList<Node> tLev = pMRef.getWorld().getData().get("CurrentLevel");
        
        if(tLev != null){
        	root.getChildren().addAll(tLev);
        }       	

        subScene = new SubScene(root, 200, 200, true, SceneAntialiasing.BALANCED);
        subScene.setCamera(camera);
        subScene.setFill(Color.BLACK);
        subScene.widthProperty().bind(stage.widthProperty());
        subScene.heightProperty().bind(stage.heightProperty());
        

        return new Group(subScene);
    }    

}