package plugins;

import java.util.ArrayList;

import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Box;
import javafx.scene.shape.DrawMode;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

public class W3DController{
	
	private PerspectiveCamera camera;
	private Stage stage;
	private Group root;
	private SubScene subScene;
	
	private Rotate rotateX;
	private Rotate rotateY;
	private Box targetBox;
	private ArrayList<Box> selected;
	
	public W3DController(PerspectiveCamera c, Stage s, SubScene ss, Group r){
		camera = c;
		stage = s;
		
		subScene = ss;
		root = r;
		
		selected = new ArrayList<Box>();
		
		rotateX = new Rotate(0, Rotate.X_AXIS);
        rotateY = new Rotate(0, Rotate.Y_AXIS);
	}
	

    public void handleKeyboard(KeyEvent ke){
        switch(ke.getText()){
        	case "w":
        	{
        		camera.getTransforms().add(new Translate(0, 0, 1));
        	}
        	break;
        	case "a":
        	{
        		camera.getTransforms().add(new Translate(-1, 0, 0));
        	}
        	break;
        	case "s":
        	{
        		camera.getTransforms().add(new Translate(0, 0, -1));
        	}
        	break;
        	case "d":
        	{
        		camera.getTransforms().add(new Translate(1, 0, 0));
        	}
        	break;
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
    
    double curMX = 0;
	double curMY = 0;
	long timePaused = 0;
    
    public void handleMouseInput(MouseEvent me){
    	if(me.getButton() == MouseButton.PRIMARY && me.getTarget() != subScene && me.getTarget() != targetBox && !selected.contains(me.getTarget())){ 		
    		if(!me.isControlDown()){
            	selected.clear();
    		}
        	selected.add((Box) me.getTarget());
    		selectObject((Box)me.getTarget());
    	}    	
    	else if(me.getButton() == MouseButton.PRIMARY && me.getTarget() == subScene){
    		root.getChildren().remove(targetBox);
    		targetBox = null;
    		selected.clear();
    		camera.getTransforms().remove(rotateX);
    		camera.getTransforms().remove(rotateY);
    		rotateX = new Rotate(0, Rotate.X_AXIS);
            rotateY = new Rotate(0, Rotate.Y_AXIS);
    	}
    }
    
    public void selectObject(Box b){
    	
    	Point3D p = new Point3D(0, 0, 0);
    	
    	camera.getTransforms().remove(rotateX);
    	camera.getTransforms().remove(rotateY);
    	root.getChildren().remove(targetBox);
    	
    	double trX = 0, trY = 0, trZ = 0, trW = 0, trH = 0, trD = 0;
    	double trXMi = selected.get(0).getTranslateX(), trXMa = selected.get(0).getTranslateX(), 
    			trYMi = selected.get(0).getTranslateY(), trYMa = selected.get(0).getTranslateY(), 
    			trZMi = selected.get(0).getTranslateZ(), trZMa = selected.get(0).getTranslateZ();
    	
    	for(int i = 0; i < selected.size(); i++){
    		trX += selected.get(i).getTranslateX();
    		trY += selected.get(i).getTranslateY();
    		trZ += selected.get(i).getTranslateZ();
    		if(selected.get(i).getTranslateX() < trXMi){
    			trXMi = selected.get(i).getTranslateX(); 
    		}
    		else if(selected.get(i).getTranslateX() > trXMa){
    			trXMa = selected.get(i).getTranslateX(); 
    		}
    		
    		if(selected.get(i).getTranslateY() < trYMi){
    			trYMi = selected.get(i).getTranslateY(); 
    		}
    		else if(selected.get(i).getTranslateY() > trYMa){
    			trYMa = selected.get(i).getTranslateY(); 
    		}
    		
    		if(selected.get(i).getTranslateZ() < trZMi){
    			trZMi = selected.get(i).getTranslateZ(); 
    		}
    		else if(selected.get(i).getTranslateZ() > trZMa){
    			trZMa = selected.get(i).getTranslateZ(); 
    		}
    		
    		if(selected.get(i).getWidth() > trW){
    			trW = selected.get(i).getWidth();
    		}
    		
    		if(selected.get(i).getHeight() > trH){
    			trH = selected.get(i).getHeight();
    		}
    		
    		if(selected.get(i).getDepth() > trD){
    			trD = selected.get(i).getDepth();
    		}
    		
    	}
    	trX = trX/selected.size();
    	trY = trY/selected.size();
    	trZ = trZ/selected.size();
    	double trW2 = Math.sqrt(Math.pow(trW/2, 2) + Math.pow(trH/2, 2) + Math.pow(trD/2, 2));
    	double trH2 = Math.sqrt(Math.pow(trW/2, 2) + Math.pow(trH/2, 2) + Math.pow(trD/2, 2));
    	double trD2 = Math.sqrt(Math.pow(trW/2, 2) + Math.pow(trH/2, 2) + Math.pow(trD/2, 2));
    	trW += Math.abs(trXMa - trXMi) + trW2;
    	trH += Math.abs(trYMa - trYMi) + trH2;
    	trD += Math.abs(trZMa - trZMi) + trD2;
    	
    	p = p.add(trX, trY, trZ);
    	
    	targetBox = new Box(trW+1, trH+1, trD+1);
    	targetBox.getTransforms().add(new Translate(trX, trY, trZ));
    	
    	targetBox.setDrawMode(DrawMode.LINE);
    	
    	root.getChildren().add(targetBox);
    	
    	for(int i = 0; i < camera.getTransforms().size(); i++){
    		p = p.add(-camera.getTransforms().get(i).getTx(), 
    				  -camera.getTransforms().get(i).getTy(), 
    				  -camera.getTransforms().get(i).getTz());
    	}
    	
    	lookAt(p); 	
    	
    	double len = Math.sqrt((Math.pow(p.getX(), 2) + Math.pow(p.getY(), 2) + Math.pow(p.getZ(), 2) )); 

    	rotateX = new Rotate(0, 0, 0, len, Rotate.X_AXIS);
        rotateY = new Rotate(0, 0, 0, len, Rotate.Y_AXIS);
        
    	camera.getTransforms().add(rotateX);
    	camera.getTransforms().add(rotateY);
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
}