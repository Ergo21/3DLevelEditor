package plugins;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import baseProgram.PluginManager;
import common.*;

/**
 * Creates a 3D Window to display the world.
 * @author Ergo21
 *
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
        
        control = new W3DController(camera, stage, subScene, root);
        
        scene.setOnKeyPressed(event->control.handleKeyboard(event));
        scene.setOnKeyReleased(event->control.handleKeyboardRelease(event));
        scene.setOnMouseMoved(event->control.handleMouseMove(event));
        scene.setOnMouseClicked(event->control.handleMouseInput(event));
        scene.setOnMouseDragged(event->control.handleMouseMove(event));
        
        stage.setScene(scene);
        
        stage.show(); 
        
        play();
	}
	
	public void pluginClose() {
		stage.close();
	}
	
	private Timeline animation;

	/**
	 * Creates all objects shown. Will be overwritten to load from Data when complete.
	 * @return Scene to add to Stage
	 */
    public Parent createContent() {
        Cube c = new Cube(1, Color.GREEN);
        c.rx.setAngle(45);
        c.ry.setAngle(45);

        Cube c2 = new Cube(1, Color.BLUE);
        c2.setTranslateX(2);
        c2.rx.setAngle(45);
        c2.ry.setAngle(45);

        Cube c3 = new Cube(5, Color.RED);
        c3.setTranslateX(-10);
        c3.rx.setAngle(45);
        c3.ry.setAngle(45);

        animation = new Timeline();
        animation.getKeyFrames().addAll(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(c.ry.angleProperty(), 30d),
                        new KeyValue(c2.rx.angleProperty(), 0d),
                        new KeyValue(c3.rz.angleProperty(), 0d)),
                        new KeyFrame(Duration.seconds(1),
                                new KeyValue(c.ry.angleProperty(), 360d),
                                new KeyValue(c2.rx.angleProperty(), 360d),
                                new KeyValue(c3.rz.angleProperty(), 360d)));
        animation.setCycleCount(Timeline.INDEFINITE);

        camera.getTransforms().add(new Translate(0, 0, -10));

        root = new Group();
        root.getChildren().addAll(c, c2, c3);

        subScene = new SubScene(root, 200, 200, true, SceneAntialiasing.BALANCED);
        subScene.setCamera(camera);
        subScene.setFill(Color.BLACK);
        subScene.widthProperty().bind(stage.widthProperty());
        subScene.heightProperty().bind(stage.heightProperty());
        

        return new Group(subScene);
    }

    public void play() {   	
        //animation.play();
    }

    public void stop() {
        animation.pause();
    }
    

    class Cube extends Box {

        final Rotate rx = new Rotate(0, Rotate.X_AXIS);
        final Rotate ry = new Rotate(0, Rotate.Y_AXIS);
        final Rotate rz = new Rotate(0, Rotate.Z_AXIS);

        public Cube(double size, Color color) {
            super(size, size, size);
            setMaterial(new PhongMaterial(color));
            getTransforms().addAll(rz, ry, rx);
        }
    }

}