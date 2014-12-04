package baseProgram;

//import java.util.ArrayList;

import javafx.application.*;
import javafx.scene.Group;
import javafx.scene.Scene;
//import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
//import javafx.scene.layout.HBox;
//import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainWindow extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    private Group createContent() {
    	System.out.println("Running createContent()");
        Group root = new Group();
        //HBox hbox = new HBox(20);

        MenuBar menuBar = new MenuBar();
        createMenu(menuBar, "File","New", "World");
        createMenu(menuBar, "File","New","Level");
        createMenu(menuBar, "File","Load","World");
        createMenu(menuBar, "File","Load","Level");
        createMenu(menuBar, "Edit","Transform");
        createMenu(menuBar, "File","Exit");
        createMenu(menuBar, "Exit");
        createMenu(menuBar);
        createMenu(menuBar, "Plugins", "Model Loader", "Load Obj");
        
        /*Button btn1 = new Button("test");

        Button btn2 = new Button("add");
        btn2.setOnAction(event -> {
            VBox vbox = new VBox(0);
            vbox.getChildren().add(new Button("I came from plugin"));
            vbox.getChildren().add(new Button("So did I!"));

            hbox.getChildren().add(vbox);
        });

        hbox.getChildren().addAll(btn1, btn2);*/

        root.getChildren().addAll(menuBar);



        return root;
    }
    
    private void createMenu(MenuBar menuBar, String... tokens) {
    	
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
		mI.setOnAction(event -> System.out.println(tokens[tokens.length-1]));
    	
    	if(0 < tokens.length - 2) {
    		Menu finMen = iterAddMenu(1, tokens, mainMenu); 			
    		finMen.getItems().add(mI);
    	}
    	else {
    		mainMenu.getItems().add(mI);
    	}		
    	
    }
    
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
    		m = (Menu)toAddTo.getItems().get(foundAt);
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
    public void start(Stage stage) throws Exception {
        stage.setWidth(300);
        stage.setHeight(300);

        Scene scene = new Scene(createContent());

        stage.setScene(scene);

        stage.show();
    }

}
