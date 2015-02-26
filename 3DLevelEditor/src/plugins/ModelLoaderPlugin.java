package plugins;

import java.io.File;
import java.util.function.Function;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import baseProgram.PluginManager;

import com.interactivemesh.jfx.importer.col.ColModelImporter;
import com.interactivemesh.jfx.importer.fxml.FxmlModelImporter;
import com.interactivemesh.jfx.importer.obj.ObjModelImporter;
import com.interactivemesh.jfx.importer.stl.StlMeshImporter;
import com.interactivemesh.jfx.importer.tds.TdsModelImporter;
import com.interactivemesh.jfx.importer.x3d.X3dModelImporter;

import common.TLEData;
import common.TLEPlugin;


public class ModelLoaderPlugin extends TLEPlugin{
	private PluginManager pMRef;
	private File currentFile;
	
	public ModelLoaderPlugin(){
		pluginName = "ModelLoaderPlugin";
	}
	
	@Override
	public void install(PluginManager pM) {
		mainPlMan = pM;
		mainPlMan.getMWin().addMenuBarItem(event -> loadModel(), "Plugins", "3D Model Loader", "Load New Model");
		
		Function<File,TLEData> readFileFunc = f -> {
			TLEData t = readFile(f);
			return t;
		};
		mainPlMan.getWorld().setModelLoader(readFileFunc);
	}

	/**
	 * Opens a window to load a valid model. Activated from "Plugins/3D Model Loader/Load Model" button. 
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
			TLEData t = readFile(currentFile);
			if(t != null){
				pMRef.getWorld().getData().get("Meshes").add(t);
			}
		}
		pMRef.getWorld().runResetWindow();
	}
	
	public TLEData readFile(File f){
		
		if(f.getName().endsWith(".obj")){
			ObjModelImporter mI = new ObjModelImporter();
			mI.read(f);
		 	
		 	MeshView[] models = mI.getImport();
		 	mI.close();
		 	
		 	if(models != null && models.length > 0){ 
		 		Group gNode = new Group();
		 		gNode.getChildren().addAll(models);
		 		TLEData t = new TLEData(f.getName(), "obj1", f.getAbsolutePath());
		 		t.setMesh(gNode);
		 		if(!checkIfLoaded(t)){
		 			return t;
		 		}
		 		else{
		 			System.out.println("File already loaded");
		 		}
		 		
		 	}
		 	
		}
		else if(f.getName().endsWith(".3ds")){
			TdsModelImporter mI = new TdsModelImporter();
			mI.read(f);
		 	
		 	Node[] models = mI.getImport();
		 	mI.close();
		 	
		 	if(models != null && models.length > 0){ 
		 		Group gNode = new Group();
		 		gNode.getChildren().addAll(models);
		 		
		 		TLEData t = new TLEData(f.getName(), "3ds1", f.getAbsolutePath());
		 		t.setMesh(gNode);
		 		if(!checkIfLoaded(t)){
		 			return t;
		 		}
		 		else{
		 			System.out.println("File already loaded");
		 		}
		 	}
		 	
		}
		else if(f.getName().endsWith(".dae") || f.getName().endsWith(".zae")){
			ColModelImporter mI = new ColModelImporter();
			mI.read(f);
		 	
		 	Node[] models = mI.getImport();
		 	mI.close();
		 	
		 	if(models != null && models.length > 0){ 
		 		Group gNode = new Group();
		 		gNode.getChildren().addAll(models);
		 		
		 		TLEData t = new TLEData(f.getName(), "d/zae1", f.getAbsolutePath());
		 		t.setMesh(gNode);
		 		if(!checkIfLoaded(t)){
		 			return t;
		 		}
		 		else{
		 			System.out.println("File already loaded");
		 		}
		 	}
		}
		else if(f.getName().endsWith(".fxml")){
			FxmlModelImporter mI = new FxmlModelImporter();
			mI.read(f);
		 	
		 	Node models = mI.getImport();
		 	mI.close();
		 	
		 	TLEData t = new TLEData(f.getName(), "fxml1", f.getAbsolutePath());
	 		t.setMesh(models);
	 		if(!checkIfLoaded(t)){
	 			return t;
	 		}
	 		else{
	 			System.out.println("File already loaded");
	 		}
		}
		else if(f.getName().endsWith(".stl")){
			StlMeshImporter mI = new StlMeshImporter();
			mI.read(f);
		 	
		 	TriangleMesh models = mI.getImport();
		 	mI.close();
		 	MeshView mV = new MeshView(models);
		 	
		 	TLEData t = new TLEData(f.getName(), "stl1", f.getAbsolutePath());
	 		t.setMesh(mV);
	 		if(!checkIfLoaded(t)){
	 			return t;
	 		}
	 		else{
	 			System.out.println("File already loaded");
	 		}
		}
		else if(f.getName().endsWith(".x3d") || f.getName().endsWith(".x3dz")){
			X3dModelImporter mI = new X3dModelImporter();
			mI.read(f);
		 	
		 	Node[] models = mI.getImport();
		 	mI.close();
		 	
		 	if(models != null && models.length > 0){ 
		 		Group gNode = new Group();
		 		gNode.getChildren().addAll(models);
		 		
		 		TLEData t = new TLEData(f.getName(), "x3d/z1", f.getAbsolutePath());
		 		t.setMesh(gNode);
		 		if(!checkIfLoaded(t)){
		 			return t;
		 		}
		 		else{
		 			System.out.println("File already loaded");
		 		}
		 	}
		}
	 	
		return null;
	}
	
	/**
	 * Currently not implemented as Meshes are not either cloned or inherited, so is reloaded.
	 * @param t Model to be checked.
	 * @return Is the model t already loaded?
	 */
	public boolean checkIfLoaded(TLEData t){
		/*ArrayList<TLEData> m = mainPlMan.getWorld().getData().get("Meshes");
		for(int i = 0; i < m.size(); i++){
			if(m.get(i).getName().equals(t.getName())){
				return true;
			}
		}*/
		return false;
	}
}