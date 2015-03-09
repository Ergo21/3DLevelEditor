package plugins;

import common.*;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

/**
 * Handles the input of Window3D, and manipulates the world.
 * @author Ergo21
 *
 */

public class DGController{
	private DemoGameWin mainWin;
	private PerspectiveCamera camera;
	private Stage stage;
	private Group root;
	
	private Rotate rotateX;
	private Rotate rotateY;

    private double curMX = 0;
    private double curMY = 0;
	
	
	/**
	 * Handles user input to control the world.
	 * 
	 * @param w Main window that is controlled.
	 */
	public DGController(DemoGameWin w){
		mainWin = w;
		camera = mainWin.getCamera();
		stage = mainWin.getStage();
		
		root = mainWin.getRoot();
		
		rotateX = new Rotate(0, Rotate.X_AXIS);
        rotateY = new Rotate(0, Rotate.Y_AXIS);
	}
	
	/**
	 * Handles keyboard event, handles object transforms and camera movement.
	 * @param ke KeyEvent
	 */
	
    public void handleKeyboard(KeyEvent ke){	
        switch(ke.getCode()){
        	case W:
        	{
        		camera.getTransforms().add(new Translate(0, 0, 1));
           	}
        	break;
        	case A:
        	{
        		camera.getTransforms().add(new Translate(-1, 0, 0));
        	}
        	break;
        	case S:
        	{
        		camera.getTransforms().add(new Translate(0, 0, -1));
        	}
        	break;
        	case D:
        	{
        		camera.getTransforms().add(new Translate(1, 0, 0));
        	}
        	break;
		default:
			break;
        }
    }
    
    public void handleKeyboardRelease(KeyEvent ke){
    		
    }
	
    /**
     * Handles mouse movement event, handles camera rotation.
     * @param me MouseEvent
     */

    public void handleMouseMove(MouseEvent me){ 
    	
    	if(curMX == 0 && curMY == 0){
    		curMX = me.getSceneX();
    		curMY = me.getSceneY();
    	}

    	double curChX = me.getSceneX() - curMX; 
		curChX = curChX*(180/stage.widthProperty().doubleValue());
		double curChY = me.getSceneY() - curMY;
		curChY = curChY*(180/stage.widthProperty().doubleValue());	
    	camera.getTransforms().remove(rotateY);
        rotateY.setAngle(rotateY.getAngle() + curChX);  		
        camera.getTransforms().add(rotateY); 	
        camera.getTransforms().remove(rotateX);
        rotateX.setAngle(rotateX.getAngle() - curChY);  	
        camera.getTransforms().add(rotateX); 
		
        curMX = me.getSceneX();
    	curMY = me.getSceneY();
    }
    
	
	/**
	 *  Handles Mouse input, handles selecting object/s.
	 * @param me MouseEvent
	 */
    
    public void handleMouseInput(MouseEvent me){
    	if(me.getButton() == MouseButton.PRIMARY){ 		
    		
    		Node n = (Node)me.getTarget();
    		if(n.getParent() != null && n.getParent() != root){
    			while(n != null && n != root && n.getClass() != TLEData.class){
    				n = n.getParent();
    			}
    			if(n != null && n != root){
            		//Handle activator
    			}			
    		}
    		
    	}   
    }
}