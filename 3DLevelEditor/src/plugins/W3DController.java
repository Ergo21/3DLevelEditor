package plugins;

import java.util.ArrayList;

import common.*;
import baseProgram.PluginManager;
import javafx.geometry.Point3D;
import javafx.scene.AmbientLight;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.SubScene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Box;
import javafx.scene.shape.DrawMode;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

/**
 * Handles the input of Window3D, and manipulates the world.
 * @author Ergo21
 *
 */

public class W3DController{
	private PluginManager pMRef;
	private Window3D mainWin;
	private PerspectiveCamera camera;
	private Stage stage;
	private Group root;
	private SubScene subScene;
	private AmbientLight globalLighting;
	
	private Rotate rotateX;
	private Rotate rotateY;
	private ArrayList<Box> targetBoxes;		//Boxes that show what is selected
	private ArrayList<Node> selected;		//Objects to be transformed
	private String choAct;
	
    private Rotate lookAtX;
    private Rotate lookAtY;
    

    private double curMX = 0;
    private double curMY = 0;
	
	
	/**
	 * Handles user input to control the world.
	 * 
	 * @param p PluginManager
	 * @param w The display window
	 */
	public W3DController(PluginManager p, Window3D w){
		mainWin = w;
		pMRef = p;
		camera = mainWin.getCamera();
		stage = mainWin.getStage();
		
		subScene = mainWin.getSubScene();
		root = mainWin.getRoot();
		
		globalLighting = mainWin.getGLight();
		
		targetBoxes = new ArrayList<Box>();
		selected = new ArrayList<Node>();
		
		rotateX = new Rotate(0, Rotate.X_AXIS);
        rotateY = new Rotate(0, Rotate.Y_AXIS);
        choAct = "";
        
        lookAtX = new Rotate(0, Rotate.X_AXIS);
    	lookAtY = new Rotate(0, Rotate.Y_AXIS);
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
        		if(selected.size() > 0){
        			updateRotPoint();
        		}
           	}
        	break;
        	case A:
        	{
        		if(!ke.isAltDown()){
        			camera.getTransforms().add(new Translate(-1, 0, 0));
        			if(selected.size() > 0){
        				updateRotPoint();
        			}
        		}
        		else{
        			choAct = "sA";
        		}
        	}
        	break;
        	case S:
        	{
        		camera.getTransforms().add(new Translate(0, 0, -1));
        		if(selected.size() > 0){
        			updateRotPoint();
        		}
        	}
        	break;
        	case D:
        	{
        		camera.getTransforms().add(new Translate(1, 0, 0));
        		if(selected.size() > 0){
        			updateRotPoint();
        		}
        	}
        	break;
    		case X:
    		{
    			if(ke.isControlDown()){      			
    				choAct = "rX";
    			}
    			else if(ke.isShiftDown()){
    				choAct = "mX";
    			}
    			else if(ke.isAltDown()){
    				choAct = "sX";
    			}
    		}
    		break;
    		case Y:
    		{
    			if(ke.isControlDown()){      			
    				choAct = "rY";
    			}
    			else if(ke.isShiftDown()){
    				choAct = "mY";
    			}
    			else if(ke.isAltDown()){
    				choAct = "sY";
    			}
    		}
    		break;
    		case Z:
    		{
    			if(ke.isControlDown()){      			
    				choAct = "rZ";
    			}
    			else if(ke.isShiftDown()){
    				choAct = "mZ";
    			}
    			else if(ke.isAltDown()){
    				choAct = "sZ";
    			}
    			
    		}
    		break;
    		case L:
    		{
    			updateRotPoint();
    		}
    		break; 
    		case R:
    		{
    			resetWindow();
    		}
    		break;
    		case T:
    		{
    			
    		}
    		break;
    		case DELETE:
    		{
    			targetBoxes.clear();
    			for(int i = 0; i < selected.size(); i++){   				
    				pMRef.getWorld().getData().get("CurrentLevel").remove(selected.get(i));		
    			}
    			
    			selected.clear();
    			pMRef.getWorld().runResetWindow();
    		}
    		break;
		default:
			break;
        }
    }
    
    public void handleKeyboardRelease(KeyEvent ke){
    	switch(ke.getCode()){
    		case X:
    		{
    			if(ke.isControlDown()){      			
    				choAct = "rX";
    			}
    			else if(ke.isShiftDown()){
    				choAct = "mX";
    			}
    			else if(ke.isAltDown()){
    				choAct = "sX";
    			}
    			else{
    				choAct = "";
    			}
    		}
    		break;
    		case Y:
    		{
    			if(ke.isControlDown()){      			
    				choAct = "rY";
    			}
    			else if(ke.isShiftDown()){
    				choAct = "mY";
    			}
    			else if(ke.isAltDown()){
    				choAct = "sY";
    			}
    			else{
    				choAct = "";
    			}
    		}
    		break;
    		case Z:
    		{
    			if(ke.isControlDown()){      			
    				choAct = "rZ";
    			}
    			else if(ke.isShiftDown()){
    				choAct = "mZ";
    			}
    			else if(ke.isAltDown()){
    				choAct = "sZ";
    			}
    			else{
    				choAct = "";
    			}
    		}
    		break;
    		default:{
    			choAct = "";
    		}
    	}
    }
	
    /**
     * Handles mouse movement event, handles camera rotation.
     * @param me MouseEvent
     */

    public void handleMouseMove(MouseEvent me){  
    	double curChX = me.getSceneX() - curMX; 
		curChX = curChX*(180/stage.widthProperty().doubleValue());
		double curChY = me.getSceneY() - curMY;
		curChY = curChY*(180/stage.widthProperty().doubleValue());
    	if(me.isSecondaryButtonDown() && choAct.equals("")) {  	
			camera.getTransforms().remove(rotateY);
    		rotateY.setAngle(rotateY.getAngle() + curChX);  		
    		camera.getTransforms().add(rotateY); 	
    		camera.getTransforms().remove(rotateX);
    		rotateX.setAngle(rotateX.getAngle() - curChY);  	
    		camera.getTransforms().add(rotateX); 
    	}
    	else if(!choAct.equals("")){
    		if(choAct.charAt(0) == 'm'){
    			double mov = 0;
    			if(choAct.charAt(1) == 'X'){
    				mov = curChX;
    			}
    			else if(choAct.charAt(1) == 'Y'){
    				mov = curChY;
    			}
    			else if(choAct.charAt(1) == 'Z'){
    				mov = (curChX-curChY)/2;
    			}
    			
    			translate(choAct.charAt(0), mov, choAct.charAt(1));
    		}
    		else if(choAct.charAt(0) == 'r'){
    			double rot = 0;
    			if(choAct.charAt(1) == 'X'){
    				rot = curChX;
    			}
    			else if(choAct.charAt(1) == 'Y'){
    				rot = curChY;
    			}
    			else if(choAct.charAt(1) == 'Z'){
    				rot = (curChX-curChY)/2;
    			}
    			
    			translate(choAct.charAt(0), rot, choAct.charAt(1));
    		}
    		else if(choAct.charAt(0) == 's'){
    			double sca = 0;
    			if(choAct.charAt(1) == 'X'){
    				sca = curChX;
    			}
    			else if(choAct.charAt(1) == 'Y'){
    				sca = curChY;
    			}
    			else {
    				sca = (curChX-curChY)/2;
    			}
    			
    			translate(choAct.charAt(0), sca, choAct.charAt(1));
    		}
    	}
    	
    	
    	curMX = me.getSceneX();
    	curMY = me.getSceneY();
    }
    
	
	/**
	 *  Handles Mouse input, handles selecting object/s.
	 * @param me MouseEvent
	 */
    
    public void handleMouseInput(MouseEvent me){
    	if(me.getButton() == MouseButton.PRIMARY && me.getTarget() != subScene &&  !targetBoxes.contains(me.getTarget()) && !selected.contains(me.getTarget())){ 		
    		if(!me.isControlDown()){
    			for(int i = 0; i < targetBoxes.size(); i++){
        			root.getChildren().remove(targetBoxes.get(i));
        		}
            	selected.clear();
    		}
    		Node n = (Node)me.getTarget();
    		if(n.getParent() != null && n.getParent() != root){
    			while(n != null && n != root && n.getClass() != TLEData.class){
    				n = n.getParent();
    			}
    			if(n != null && n != root){
    				selected.add(n);
            		createTarBox();
    			}			
    		}
    		else{
            	selected.add((Node)me.getTarget());
        		createTarBox();
    		}
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
    
    
    /**
     * Creates box that encompasses newly selected item.
     */
    public void createTarBox(){
    	
    	Point3D p = new Point3D(0, 0, 0);
    	
    	camera.getTransforms().remove(rotateX);
    	camera.getTransforms().remove(rotateY);
    	
    	double trX = (selected.get(selected.size()-1).boundsInParentProperty().get().getMaxX() + 
    			selected.get(selected.size()-1).boundsInParentProperty().get().getMinX())/2, 
    			trY = (selected.get(selected.size()-1).boundsInParentProperty().get().getMaxY() + 
    	    			selected.get(selected.size()-1).boundsInParentProperty().get().getMinY())/2, 
    			trZ = (selected.get(selected.size()-1).boundsInParentProperty().get().getMaxZ() + 
    	    			selected.get(selected.size()-1).boundsInParentProperty().get().getMinZ())/2, 
    			trW = selected.get(selected.size()-1).boundsInParentProperty().get().getWidth(), 
    			trH = selected.get(selected.size()-1).boundsInParentProperty().get().getHeight(), 
    			trD = selected.get(selected.size()-1).boundsInParentProperty().get().getDepth();
    	
    	
    	targetBoxes.add(new Box(trW, trH, trD));
    	targetBoxes.get(targetBoxes.size()-1).setTranslateX(trX);
    	targetBoxes.get(targetBoxes.size()-1).setTranslateY(trY);
    	targetBoxes.get(targetBoxes.size()-1).setTranslateZ(trZ);
    	globalLighting.getScope().add(targetBoxes.get(targetBoxes.size()-1));
    	
    	targetBoxes.get(targetBoxes.size()-1).setDrawMode(DrawMode.LINE);
    	
    	root.getChildren().add(targetBoxes.get(targetBoxes.size()-1));
    	
    	double pTX = 0, pTY = 0, pTZ = 0;
    	for(int i = 0; i < selected.size(); i++){
    		pTX += (selected.get(i).boundsInParentProperty().get().getMaxX() +
    				selected.get(i).boundsInParentProperty().get().getMinX())/2;
    		pTY += (selected.get(i).boundsInParentProperty().get().getMaxY() +
    				selected.get(i).boundsInParentProperty().get().getMinY())/2;
    		pTZ += (selected.get(i).boundsInParentProperty().get().getMaxZ() +
    				selected.get(i).boundsInParentProperty().get().getMinZ())/2;
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
    
    /**
     * Rotates Camera to focus on point p.
     * @param p Point the camera looks at.
     */
    
    public void lookAt(Point3D p){
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
    
    /**
     * Updates point camera rotates around. Mainly used when camera moves.
     */
    
    public void updateRotPoint(){

    	Point3D p = new Point3D(0, 0, 0);
    	
    	camera.getTransforms().remove(rotateX);
    	camera.getTransforms().remove(rotateY);
    	
    	double pTX = 0, pTY = 0, pTZ = 0, cTX = 0, cTY = 0, cTZ = 0;
    	for(int i = 0; i < selected.size(); i++){
    		pTX += (selected.get(i).boundsInParentProperty().get().getMaxX() +
    				selected.get(i).boundsInParentProperty().get().getMinX())/2;
    		pTY += (selected.get(i).boundsInParentProperty().get().getMaxY() +
    				selected.get(i).boundsInParentProperty().get().getMinY())/2;
    		pTZ += (selected.get(i).boundsInParentProperty().get().getMaxZ() +
    				selected.get(i).boundsInParentProperty().get().getMinZ())/2;
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
    
    private void translate(char t, double m, char a){
    	switch(a){
    		case 'X':
    		{
    			if(t == 'm'){
    				for(int i = 0; i < selected.size(); i++){
        				selected.get(i).setTranslateX(selected.get(i).getTranslateX()+m);
        			}
        			for(int i = 0; i < targetBoxes.size(); i++){
        				targetBoxes.get(i).setTranslateX(targetBoxes.get(i).getTranslateX()+m);
        			}
    			}
    			else if(t == 'r'){
    				Rotate rota = new Rotate(m, Rotate.X_AXIS);
    				
    				for(int i = 0; i < selected.size(); i++){
        				selected.get(i).getTransforms().add(rota);
        			}
    			}
    			else if(t == 's'){
    				for(int i = 0; i < targetBoxes.size(); i++){
    					targetBoxes.get(i).setScaleX(targetBoxes.get(i).getScaleX()+m);
        			}
    				for(int i = 0; i < selected.size(); i++){
        				selected.get(i).setScaleX(selected.get(i).getScaleX()+m);
        			}
    			}
    			
    		}
    		break;
    		case 'Y':
    		{
    			if(t == 'm'){
    				for(int i = 0; i < selected.size(); i++){
        				selected.get(i).setTranslateY(selected.get(i).getTranslateY()+m);
        			}
        			for(int i = 0; i < targetBoxes.size(); i++){
        				targetBoxes.get(i).setTranslateY(targetBoxes.get(i).getTranslateY()+m);
        			}
    			}
    			else if(t == 'r'){
    				Rotate rota = new Rotate(m, Rotate.Y_AXIS);
    				
    				for(int i = 0; i < selected.size(); i++){
        				selected.get(i).getTransforms().add(rota);
        			}
    			}
    			else if(t == 's'){
    				for(int i = 0; i < targetBoxes.size(); i++){
    					targetBoxes.get(i).setScaleY(targetBoxes.get(i).getScaleY()+m);
        			}
    				for(int i = 0; i < selected.size(); i++){
        				selected.get(i).setScaleY(selected.get(i).getScaleY()+m);
        			}
    			}
    			
    		}
    		break;
    		case 'Z':
    		{
    			if(t == 'm'){
        			for(int i = 0; i < selected.size(); i++){
        				selected.get(i).setTranslateZ(selected.get(i).getTranslateZ()+m);
        			}
        			for(int i = 0; i < targetBoxes.size(); i++){
        				targetBoxes.get(i).setTranslateZ(targetBoxes.get(i).getTranslateZ()+m);
        			}
    			}
    			else if(t == 'r'){
    				Rotate rota = new Rotate(m, Rotate.Z_AXIS);
    				
    				for(int i = 0; i < selected.size(); i++){
        				selected.get(i).getTransforms().add(rota);
        			}
    			}
    			else if(t == 's'){
    				for(int i = 0; i < targetBoxes.size(); i++){
    					targetBoxes.get(i).setScaleZ(targetBoxes.get(i).getScaleZ()+m);
        			}
    				for(int i = 0; i < selected.size(); i++){
        				selected.get(i).setScaleZ(selected.get(i).getScaleZ()+m);
        			}
    			}
    		}
    		break;
    		case 'A':{
    			if(t == 's'){
    				m = m/2;
    				for(int i = 0; i < targetBoxes.size(); i++){
    					targetBoxes.get(i).setScaleX(targetBoxes.get(i).getScaleX()+m);
    					targetBoxes.get(i).setScaleY(targetBoxes.get(i).getScaleY()+m);
    					targetBoxes.get(i).setScaleZ(targetBoxes.get(i).getScaleZ()+m);
        			}
    				for(int i = 0; i < selected.size(); i++){
    					selected.get(i).setScaleX(selected.get(i).getScaleX()+m);
    					selected.get(i).setScaleY(selected.get(i).getScaleY()+m);
        				selected.get(i).setScaleZ(selected.get(i).getScaleZ()+m);
        			}
    			}
    		}
    		break;
    	}
    }
    
    public void resetWindow(){
    	ArrayList<TLEData> tLev = pMRef.getWorld().getData().get("CurrentLevel");
        
        if(tLev != null){
        	root.getChildren().clear();
        	root.getChildren().addAll(tLev);
        	
        	boolean addGL = false;
        	for(int i = 0; i < tLev.size(); i++){
        		if(mainWin.getLitTypes().contains(tLev.get(i).getType())){
            		globalLighting.getScope().add(tLev.get(i));
            		addGL = true;
            	}
            }
        	
        	if(addGL){
        		root.getChildren().add(globalLighting);
        	}
        	
        	rotateX.setAngle(0);
        	rotateY.setAngle(0);
        	camera.getTransforms().clear();
        	camera.getTransforms().add(new Translate(0, 0, -10));
        	targetBoxes.clear();
        	selected.clear();
        } 
    	
    }
}