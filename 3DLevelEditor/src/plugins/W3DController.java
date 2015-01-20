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
	private ArrayList<Box> targetBoxes;
	private ArrayList<Box> selected;
	
	public W3DController(PerspectiveCamera c, Stage s, SubScene ss, Group r){
		camera = c;
		stage = s;
		
		subScene = ss;
		root = r;
		
		targetBoxes = new ArrayList<Box>();
		selected = new ArrayList<Box>();
		
		rotateX = new Rotate(0, Rotate.X_AXIS);
        rotateY = new Rotate(0, Rotate.Y_AXIS);
	}
	

    public void handleKeyboard(KeyEvent ke){
        switch(ke.getText()){
        	case "w":
        	{
        		camera.getTransforms().add(new Translate(0, 0, 1));
        		updateRotPoint();
        	}
        	break;
        	case "a":
        	{
        		camera.getTransforms().add(new Translate(-1, 0, 0));
        		updateRotPoint();
        	}
        	break;
        	case "s":
        	{
        		camera.getTransforms().add(new Translate(0, 0, -1));
        		updateRotPoint();
        	}
        	break;
        	case "d":
        	{
        		camera.getTransforms().add(new Translate(1, 0, 0));
        		updateRotPoint();
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
    	if(me.getButton() == MouseButton.PRIMARY && me.getTarget() != subScene &&  !targetBoxes.contains(me.getTarget()) && !selected.contains(me.getTarget())){ 		
    		if(!me.isControlDown()){
    			for(int i = 0; i < targetBoxes.size(); i++){
        			root.getChildren().remove(targetBoxes.get(i));
        		}
            	selected.clear();
    		}
        	selected.add((Box) me.getTarget());
    		selectObject((Box)me.getTarget());
    	}    	
    	else if(me.getButton() == MouseButton.PRIMARY && me.getTarget() == subScene && !me.isControlDown()){ 		
    		for(int i = 0; i < targetBoxes.size(); i++){
    			root.getChildren().remove(targetBoxes.get(i));
    		}
    		targetBoxes.clear();
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
    	
    	double trX = selected.get(selected.size()-1).getTranslateX(), 
    			trY = selected.get(selected.size()-1).getTranslateY(), 
    			trZ = selected.get(selected.size()-1).getTranslateZ(), 
    			trW = selected.get(selected.size()-1).getWidth(), 
    			trH = selected.get(selected.size()-1).getHeight(), 
    			trD = selected.get(selected.size()-1).getDepth();
    	
    	trW = Math.sqrt(Math.pow(trW/2, 2) + Math.pow(trH/2, 2) + Math.pow(trD/2, 2));
    	trH = Math.sqrt(Math.pow(trW/2, 2) + Math.pow(trH/2, 2) + Math.pow(trD/2, 2));
    	trD = Math.sqrt(Math.pow(trW/2, 2) + Math.pow(trH/2, 2) + Math.pow(trD/2, 2));
    	
    	
    	
    	targetBoxes.add(new Box(trW*2, trH*2, trD*2));
    	targetBoxes.get(targetBoxes.size()-1).getTransforms().add(new Translate(trX, trY, trZ));
    	
    	targetBoxes.get(targetBoxes.size()-1).setDrawMode(DrawMode.LINE);
    	
    	root.getChildren().add(targetBoxes.get(targetBoxes.size()-1));
    	
    	double pTX = 0, pTY = 0, pTZ = 0;
    	for(int i = 0; i < selected.size(); i++){
    		pTX += selected.get(i).getTranslateX();
    		pTY += selected.get(i).getTranslateY();
    		pTZ += selected.get(i).getTranslateZ();
    	}
    	
    	pTX = pTX/selected.size();
    	pTY = pTY/selected.size();
    	pTZ = pTZ/selected.size();
    	
    	p = p.add(pTX, pTY, pTZ);
    	
    	
    	for(int i = 0; i < camera.getTransforms().size(); i++){
    		p = p.add(-camera.getTransforms().get(i).getTx(), 
    				  -camera.getTransforms().get(i).getTy(), 
    				  -camera.getTransforms().get(i).getTz());
    	}
    	
    	lookAt(p); 	
    	
    	double len = Math.sqrt((Math.pow(p.getX(), 2) + Math.pow(p.getY(), 2) + Math.pow(p.getZ(), 2) )); 

    	rotateX = new Rotate(rotateX.getAngle(), 0, 0, len, Rotate.X_AXIS);
        rotateY = new Rotate(rotateY.getAngle(), 0, 0, len, Rotate.Y_AXIS);
        
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
    
    void updateRotPoint(){

    	Point3D p = new Point3D(0, 0, 0);
    	
    	camera.getTransforms().remove(rotateX);
    	camera.getTransforms().remove(rotateY);
    	
    	double pTX = 0, pTY = 0, pTZ = 0, cTX = 0, cTY = 0, cTZ = 0;
    	for(int i = 0; i < selected.size(); i++){
    		pTX += selected.get(i).getTranslateX();
    		pTY += selected.get(i).getTranslateY();
    		pTZ += selected.get(i).getTranslateZ();
    	}
    	
    	pTX = pTX/selected.size();
    	pTY = pTY/selected.size();
    	pTZ = pTZ/selected.size();
    	
    	p = p.add(pTX, pTY, pTZ);
    	
    	for(int i = 0; i < camera.getTransforms().size(); i++){
    		cTX += -camera.getTransforms().get(i).getTx(); 
    		cTY += -camera.getTransforms().get(i).getTy();
    		cTZ += -camera.getTransforms().get(i).getTz();
    	}
    	
    	p = p.add(cTX, cTY, cTZ);
    	
    	double len = Math.sqrt((Math.pow(p.getX(), 2) + Math.pow(p.getY(), 2) + Math.pow(p.getZ(), 2) )); 

    	rotateX = new Rotate(rotateX.getAngle(), cTX, cTY, len, Rotate.X_AXIS);
        rotateY = new Rotate(rotateY.getAngle(), cTX, cTY, len, Rotate.Y_AXIS);
        
    	camera.getTransforms().add(rotateX);
    	camera.getTransforms().add(rotateY);
    }
}