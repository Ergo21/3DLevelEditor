package baseProgram;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import common.*;

public class World {
	MainWindow rootWindow;
	HashMap<String, ArrayList<Node>> worldData;
	HashMap<String, Runnable> resetWindows;
	Runnable resetWindow;
	
	public World(MainWindow mW){
		rootWindow = mW;
		worldData = new HashMap<String, ArrayList<Node>>();
		resetWindows = new HashMap<String, Runnable>();

        Cube c2 = new Cube(1, Color.BLUE);
        c2.setTranslateX(2);
        c2.rx.setAngle(45);
        c2.ry.setAngle(45);

        Cube c3 = new Cube(5, Color.RED);
        c3.setTranslateX(-10);
        c3.rx.setAngle(45);
        c3.ry.setAngle(45);
        
        ArrayList<Node> cC = new ArrayList<Node>();
        cC.add(c2);
        cC.add(c3);
        
        worldData.put("CurrentLevel", cC);
        rootWindow.addMenuBarItem(event -> clearLevel(), "File", "Delete Current Level");
	}
	
	public HashMap<String, ArrayList<Node>> getData(){
		return worldData; 
	}
	
	public void clearLevel(){
		worldData.get("CurrentLevel").clear();
		Cube c = new Cube(2, Color.AZURE);
		c.setTranslateX(-10);
        c.rx.setAngle(45);
        c.ry.setAngle(45);
        worldData.get("CurrentLevel").add(c);
        runResetWindow();
	}
	
	public void addResetWindow(String rN, Runnable rW){
		resetWindows.put(rN, rW);
	}
	
	public void runResetWindow(){
		if(resetWindow != null){
			resetWindow.run();
		}
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