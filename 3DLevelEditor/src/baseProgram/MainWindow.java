package baseProgram;

import javafx.application.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
//import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
//import javafx.scene.layout.HBox;
//import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * The Level Editor's Main Window, which launches the rest of the program.
 * @author Ergo21
 *
 */

public class MainWindow extends Application {
	
	private Stage stage;
	private MenuBar menuBar;
	private PluginManager rootPM;
	private World rootWorld;
	
    public static void main(String[] args) {
        launch(args);
    }
    
    /**
     * Creates the MenuBar in the main window, to which we later add MenuItems, and returns a Group.
     * <p>
     * 
     * @return		the root display
     * @see			Group
     * @see			MenuBar
     */
    private Group createContent() {
        Group root = new Group();

        menuBar = new MenuBar();

        root.getChildren().addAll(menuBar);

        return root;
    }
    
    /**
     * Creates a new MenuItem at directory passed, with event menuFunction and name as the final String.
     * <p>
     * Requires at least 2 Strings, as MenuBar doesn't add MenuItems.
     * 
     * @param menuBar	MenuBar to add to
     * @param menuFunction	Function to run when MenuItem clicked on
     * @param tokens	Ellipsis of Strings that determine the directory, final String is MenuItem name
     */
    private void createMenu(MenuBar menuBar, EventHandler<ActionEvent> menuFunction, String... tokens) {
    	
    	if(tokens.length < 2) {
    		System.out.println("Adding a menu to menu bar requires > 1 directories");
    		return;
    	}
    		
    	int foundAt = -1;
        for(int i = 0; i < menuBar.getMenus().size(); i++) {
        	if(!menuBar.getMenus().isEmpty() && menuBar.getMenus().get(i).getText().equals(tokens[0])) {
        		foundAt = i;
        		break;
        	}
        }
    		
        Menu mainMenu = new Menu();
    	mainMenu.setText(tokens[0]);
    	
    	if(foundAt != -1) {
    		mainMenu = menuBar.getMenus().get(foundAt);
    	}
    	else {
    		menuBar.getMenus().add(mainMenu);
    	}
    		
    	MenuItem mI = new MenuItem();
		mI.setText(tokens[tokens.length - 1]);
		mI.setOnAction(menuFunction);
    	
    	if(0 < tokens.length - 2) {
    		Menu finMen = iterAddMenu(1, tokens, mainMenu); 			
    		finMen.getItems().add(mI);
    	}
    	else {
    		mainMenu.getItems().add(mI);
    	}		
    	
    }
    
    /**
     * Returns the Menu to which the MenuItem is added. Creates or gets existing directories from file[].
     * <p>
     * Self calls to always return the final directory.
     * 
     * @param num	Which String of file[] the function is on.
     * @param file	String array that determines MenuItem directory and name
     * @param toAddTo	Menu we add new or existing Menu to
     * @return	If at the end of file[] return the Menu added to toAddTo, else call itself with 
     * parameters (num+1, file[], new or existing Menu we added to toAddTo)
     */
    private Menu iterAddMenu(int num, String[] file, Menu toAddTo) {
    	
    	int foundAt = -1;
    	for(int i = 0; i < toAddTo.getItems().size(); i++) {
    		if(!toAddTo.getItems().isEmpty() && toAddTo.getItems().get(i).getText().equals(file[num])) {
    			foundAt = i;
    			break;
    		}
    	}
    	
    	Menu m = new Menu();
		m.setText(file[num]);
    	
    	if(foundAt != -1) {
    		if(toAddTo.getItems().get(foundAt).getClass() == MenuItem.class){
    			toAddTo.getItems().remove(foundAt);
    			toAddTo.getItems().add(foundAt, m);
    		}
    		else {
    			m = (Menu)toAddTo.getItems().get(foundAt);
    		}
    		
    	}
    	else {
    		toAddTo.getItems().add(m);
    	}
    	
    	
    	if(num < file.length - 2) {
    		return iterAddMenu(num + 1, file, m);
    	}
    	return m;
    }

    @Override
    public void start(Stage s) throws Exception {
    	rootWorld = new World(this);
        rootPM = new PluginManager(this, rootWorld);
    	
    	stage = s;
        stage.setWidth(300);
        stage.setHeight(300);

        Scene scene = new Scene(createContent());

        stage.setScene(scene);

        stage.show();      
        
        addMenuBarItem("File", "New");
        addMenuBarItem("Edit", "Transform");
        addMenuBarItem(event -> rootPM.installPlugins(), "Plugins", "Plugin Manager");
        addMenuBarItem(event -> System.out.println("Testing method passing to addMenuBarItem"), "File", "Load");
        
    }
    
    /**
     * Public method to add MenuItem to MenuBar with directory directories[], 
     * with a function to print MenuItem's name, or last of directories[].
     * 
     * @param directories Directory and name of MenuItem
     */
    public void addMenuBarItem(String... directories) {
    	createMenu(menuBar, event -> System.out.println(directories[directories.length - 1]), directories);
    }
    
    /**
     * Public method to add MenuItem to MenuBar with directory directories[], 
     * with function menuFunction.
     * 
     * @param menuFunction	Method called when MenuItem clicked
     * @param directories	Directory and name of MenuItem
     */
    public void addMenuBarItem(EventHandler<ActionEvent> menuFunction, String... directories) {
    	createMenu(menuBar, menuFunction, directories);
    }

}
