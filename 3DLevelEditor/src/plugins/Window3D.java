package plugins;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
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

public class Window3D {
	private PluginManager pMRef;
	private Stage stage;
	private SubScene subScene;
	private PerspectiveCamera camera;
	private Point3D rotPoi;
	
	private Point3D camXAx;
	private Point3D camYAx;
	private Rotate rotateX;
	private Rotate rotateY;
	
	public Window3D(PluginManager pM) {
		pMRef = pM;
	}
	
	public void pluginStart() {
		
		camXAx = new Point3D(1,0,0);
		camYAx = new Point3D(0,1,0);
		
		stage = new Stage(StageStyle.UTILITY);
		stage.setWidth(200);
		stage.setHeight(200);

        Scene scene = new Scene(createContent());
        
        scene.setOnKeyPressed(event->handleKeyboard(event));
        scene.setOnMouseMoved(event->handleMouseMove(event));
        scene.setOnMouseClicked(event->handleMouseInput(event));
        scene.setOnMouseDragged(event->handleMouseMove(event));
        
        stage.setScene(scene);
        
        stage.show(); 
        
        play();
	}
	
	public void pluginClose() {
		stage.close();
	}
	
	private Timeline animation;

    public Parent createContent() {
        Cube c = new Cube(1, Color.GREEN);
        c.rx.setAngle(45);
        c.ry.setAngle(45);

        Cube c2 = new Cube(1, Color.BLUE);
        c2.setTranslateX(2);
        c2.rx.setAngle(45);
        c2.ry.setAngle(45);

        Cube c3 = new Cube(1, Color.RED);
        c3.setTranslateX(-2);
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

        camera = new PerspectiveCamera(true);
        camera.getTransforms().add(new Translate(0, 0, -10));

        Group root = new Group();
        root.getChildren().addAll(c, c2, c3);

        subScene = new SubScene(root, 200, 200, true, SceneAntialiasing.BALANCED);
        subScene.setCamera(camera);
        subScene.setFill(Color.BLACK);
        subScene.widthProperty().bind(stage.widthProperty());
        subScene.heightProperty().bind(stage.heightProperty());
        
        rotateX = new Rotate(0, 0,0,10, camXAx);
        rotateY = new Rotate(0, 0,0,10, camYAx);
        //rotateX = new Rotate(0, Rotate.X_AXIS);
        //rotateY = new Rotate(0, Rotate.Y_AXIS);

        return new Group(subScene);
    }

    public void play() {   	
        animation.play();
    }

    public void stop() {
        animation.pause();
    }
    
    public void handleKeyboard(KeyEvent ke){
        switch(ke.getText()){
    		case "x":
    		{
    			if(ke.isControlDown()){      			
    			
    			}
    			else if(ke.isShiftDown()){
    			
    			}
    		}
    		break;
    		case "y":
    		{
    			if(ke.isControlDown()){      			
    				rotateY.setAngle(1);
    		        camera.getTransforms().add(rotateY); 	
    			}
    			else if(ke.isShiftDown()){
    			
    			}
    		}
    		break;
    		case "z":
    		{
    			if(ke.isControlDown()){      			
    			
    			}
    			else if(ke.isShiftDown()){
    			
    			}
    		}
    		break;
        }
    }
    
    double curMX = 0;
	double curMY = 0;
	long timePaused = 0;
    public void handleMouseMove(MouseEvent me){  
    	double curChX = me.getSceneX() - curMX; 
		curChX = curChX*(180/stage.widthProperty().doubleValue());
		double curChY = me.getSceneY() - curMY;
		curChY = curChY*(180/stage.widthProperty().doubleValue());
    	if(me.isSecondaryButtonDown()) { 
    		camera.getTransforms().remove(rotateY);
    		rotateY.setAngle(rotateY.getAngle() + curChX);  		
    		camera.getTransforms().add(rotateY); 	
    		camera.getTransforms().remove(rotateX);
    		rotateX.setAngle(rotateX.getAngle() - curChY);  	
    		camera.getTransforms().add(rotateX); 	
    	}
    	else if(me.isShiftDown()){
    	}
    	
    	
    	curMX = me.getSceneX();
    	curMY = me.getSceneY();
    }
    
    public void handleMouseInput(MouseEvent me){
    	if(me.getTarget() != subScene){
    		System.out.println(me.getTarget().getClass());
    		selectObject((Box)me.getTarget());
    	}    	
    }
    
    public void selectObject(Box b){
    	Point3D p = new Point3D(0, 0, 0);
    	
    	p = p.add(b.getTranslateX(), b.getTranslateY(), b.getTranslateZ());
    	
    	camera.getTransforms().remove(rotateX);
    	camera.getTransforms().remove(rotateY);
    	
    	for(int i = 0; i < camera.getTransforms().size(); i++){
    		p = p.add(-camera.getTransforms().get(i).getTx(), 
    				  -camera.getTransforms().get(i).getTy(), 
    				  -camera.getTransforms().get(i).getTz());
    	}
    	
    	//lookAt(p);
    	
    	rotateX.setAngle(0);
        rotateY.setAngle(0);
        
    	camera.getTransforms().add(rotateX);
    	camera.getTransforms().add(rotateY);
    	lookAt(p);
    	
    }
    
    Rotate lookAtX;
    Rotate lookAtY;
    
    void lookAt(Point3D p){
    	camera.getTransforms().remove(lookAtX);
    	camera.getTransforms().remove(lookAtY);
    	Point3D p2 = new Point3D(0,0,0);
    	for(int i = 0; i < camera.getTransforms().size(); i++){
    		p2 = p2.add(-camera.getTransforms().get(i).getTx(), 
    				  -camera.getTransforms().get(i).getTy(), 
    				  -camera.getTransforms().get(i).getTz());
    	}
    	
    	lookAtX = new Rotate(Math.toDegrees(-Math.atan(p.getY()/p.getZ())), Rotate.X_AXIS);
    	lookAtY = new Rotate(Math.toDegrees(Math.atan(p.getX()/p.getZ())), Rotate.Y_AXIS);
    	camera.getTransforms().add(lookAtX);
        camera.getTransforms().add(lookAtY);
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