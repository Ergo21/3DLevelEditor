package plugins;


import java.io.File;

import com.interactivemesh.jfx.importer.ModelImporter;
import com.interactivemesh.jfx.importer.col.ColModelImporter;
import com.interactivemesh.jfx.importer.fxml.FxmlModelImporter;
import com.interactivemesh.jfx.importer.obj.ObjModelImporter;
import com.interactivemesh.jfx.importer.stl.StlMeshImporter;
import com.interactivemesh.jfx.importer.tds.TdsModelImporter;
import com.interactivemesh.jfx.importer.x3d.X3dModelImporter;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import baseProgram.PluginManager;
import common.*;

public class MLoader3DPlugin extends TLEPlugin {
	File currentFile;
	public MLoader3DPlugin(){
		pluginName = "Window3DPlugin";
	}
	
	/**
	 * Adds new buttons under Plugins/3D Model Loader. 
	 * 
	 * @param pM	PluginManager for the Plugin to interact with the Base Program
	 */
	@Override
	public void install(PluginManager pM) {
		mainPlMan = pM;
		mainPlMan.getMWin().addMenuBarItem(event -> loadModel(), "Plugins", "3D Model Loader", "Load Model");
	}
	
	/**
	 * Opens a window to load a valid model Activated from "Plugins/3D Model Loader/Load Model" button. 
	 */
	public void loadModel(){
		Stage stage = new Stage();
		stage.setWidth(200);
		stage.setHeight(200);
		
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Resource File");
		fileChooser.getExtensionFilters().addAll(
		        new FileChooser.ExtensionFilter("OBJ Model Files", "*.obj"),
		        new FileChooser.ExtensionFilter("3DS Model Files", "*.3ds"),
		        new FileChooser.ExtensionFilter("COLLADA Model Files", "*.dae", "*.zae"),
		        new FileChooser.ExtensionFilter("FXML Model Files", "*.fxml"),
		        new FileChooser.ExtensionFilter("STL Model Files", "*.stl"),
		        new FileChooser.ExtensionFilter("X3D Model Files", "*.x3d", "*.x3dz"));
		 
		if(currentFile != null){
			fileChooser.setInitialDirectory(currentFile.getParentFile());
		}
		 
		currentFile = fileChooser.showOpenDialog(stage);
		 
		if(currentFile != null){
			readFile(currentFile);
		}
		mainPlMan.getWorld().runResetWindow();
	}
	
	private void readFile(File f){
		
		if(f.getName().endsWith(".obj")){
			ObjModelImporter mI = new ObjModelImporter();
			mI.read(f);
		 	
		 	MeshView[] models = mI.getImport();
		 	
		 	if(models != null && models.length > 0){ 
		 		Group gNode = new Group();
		 		gNode.getChildren().addAll(models);

		 		mainPlMan.getWorld().getData().get("CurrentLevel").add(gNode);
		 	}
		 	mI.close();
		}
		else if(f.getName().endsWith(".3ds")){
			TdsModelImporter mI = new TdsModelImporter();
			mI.read(f);
		 	
		 	Node[] models = mI.getImport();
		 	
		 	if(models != null && models.length > 0){ 
		 		Group gNode = new Group();
		 		gNode.getChildren().addAll(models);

		 		mainPlMan.getWorld().getData().get("CurrentLevel").add(gNode);
		 	}
		 	mI.close();
		}
		else if(f.getName().endsWith(".dae") || f.getName().endsWith(".zae")){
			ColModelImporter mI = new ColModelImporter();
			mI.read(f);
		 	
		 	Node[] models = mI.getImport();
		 	
		 	if(models != null && models.length > 0){ 
		 		Group gNode = new Group();
		 		gNode.getChildren().addAll(models);

		 		mainPlMan.getWorld().getData().get("CurrentLevel").add(gNode);
		 	}
		 	mI.close();
		}
		else if(f.getName().endsWith(".fxml")){
			FxmlModelImporter mI = new FxmlModelImporter();
			mI.read(f);
		 	
		 	Node models = mI.getImport();
		 	
		 	mainPlMan.getWorld().getData().get("CurrentLevel").add(models);
		 	mI.close();
		}
		else if(f.getName().endsWith(".stl")){
			StlMeshImporter mI = new StlMeshImporter();
			mI.read(f);
		 	
		 	TriangleMesh models = mI.getImport();
		 	MeshView mV = new MeshView(models);
		 	mainPlMan.getWorld().getData().get("CurrentLevel").add(mV);
		 	mI.close();
		}
		else if(f.getName().endsWith(".x3d") || f.getName().endsWith(".x3dz")){
			X3dModelImporter mI = new X3dModelImporter();
			mI.read(f);
		 	
		 	Node[] models = mI.getImport();
		 	
		 	if(models != null && models.length > 0){ 
		 		Group gNode = new Group();
		 		gNode.getChildren().addAll(models);

		 		mainPlMan.getWorld().getData().get("CurrentLevel").add(gNode);
		 	}
		 	mI.close();
		}
	 	
	}
}