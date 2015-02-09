package plugins;


import java.io.File;

import com.interactivemesh.jfx.importer.obj.ObjModelImporter;

import javafx.scene.shape.MeshView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import baseProgram.PluginManager;
import common.*;

public class MLoader3DPlugin extends TLEPlugin {
	
	public MLoader3DPlugin(){
		pluginName = "Window3DPlugin";
	}
	
	/**
	 * Adds new buttons under Plugins/3D Window. 
	 * 
	 * @param pM	PluginManager for the Plugin to interact with the Base Program
	 */
	@Override
	public void install(PluginManager pM) {
		mainPlMan = pM;
		mainPlMan.getMWin().addMenuBarItem(event -> loadModel(), "Plugins", "3D Model Loader", "Load Model");
	}
	
	/**
	 * Creates a 3D Window showing the current level. Activated from "Plugins/3D Window/New Window" button. 
	 */
	public void loadModel(){
		Stage stage = new Stage();
		stage.setWidth(200);
		stage.setHeight(200);
		
		FileChooser fileChooser = new FileChooser();
		 fileChooser.setTitle("Open Resource File");
		 fileChooser.getExtensionFilters().addAll(
		         new FileChooser.ExtensionFilter("OBJ Model Files", "*.obj"));
		 File selectedFile = fileChooser.showOpenDialog(stage);
		 
		 if(selectedFile != null){
		 	ObjModelImporter oIm = new ObjModelImporter();
		 	oIm.read(selectedFile);
		 	MeshView[] models = oIm.getImport();
		 
		 	if(models != null && models.length > 0){ 
		 		for(int i = 0; i < models.length; i++){
		 			mainPlMan.getWorld().getData().get("CurrentLevel").add(models[i]);
		 		}
		 	}
		}
		 mainPlMan.getWorld().runResetWindow();
	}
}