package graph_editor;

import graph_editor.model.Algorithme;
import graph_editor.model.Arete;
import graph_editor.model.Sommet;
import graph_editor.util.IOFile;
import graph_editor.util.Var;
import graph_editor.view.GeneralToolBar;
import graph_editor.view.draw.MultiSelectDraw;
import graph_editor.view.shape.AreteView;
import graph_editor.view.shape.SommetView;
import graph_editor.view.tool_panel.*;
import graph_editor.view.Toile;
import graph_editor.model.Graph;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class Main extends Application {

    public static final int
            MODE_UNDEFINED = 0,
            MODE_SELECTION_SOMMET = 1,
            MODE_SELECTION_ARETE = 2,
            MODE_SOMMET_DRAW = 3,
            MODE_ARETE_DRAW = 4,
            MODE_MULTI_SELECT = 5,
            MODE_ALGO = 6;

    public static GeneralToolBar generalToolBar;

    public static ToolPanel toolPanel;

    public static Toile toile;

    public static BorderPane editPanelPan;

    public static Graph graph;

    public static Stage primaryStage;

    public static Algorithme algorithme;

    public static String filename;

    public static int currentMode = MODE_UNDEFINED;


    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane borderPane = new BorderPane();

        generalToolBar = new GeneralToolBar();

        editPanelPan = new BorderPane();
        changeEditTool(new ToolPanel());

        toile = new Toile();

        graph = new Graph();

        BorderPane paneTop = new BorderPane();
        paneTop.setTop(setUpMenuBar());
        paneTop.setCenter(generalToolBar);

        borderPane.setTop(paneTop);
        borderPane.setLeft(editPanelPan);
        borderPane.setCenter(toile);

        Scene scene = new Scene(borderPane, 1200, 800);

        Main.primaryStage = primaryStage;

        toile.changeDrawTool(new MultiSelectDraw());
        changeEditTool(new ToolPanel());

        primaryStage.setTitle("Graph Editor");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private MenuBar setUpMenuBar(){
        MenuBar menuBar = new MenuBar();

        Menu fichier = new Menu("Fichier");
        Menu editer = new Menu("Editer");
        Menu selection = new Menu("Sélection");

        MenuItem quit = new MenuItem("Quitter");
        quit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Platform.exit();
            }
        });
        quit.setAccelerator(new KeyCodeCombination(KeyCode.Q, KeyCombination.CONTROL_DOWN));

        MenuItem selectAll = new MenuItem("Tous");
        selectAll.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(currentMode == MODE_MULTI_SELECT ||
                        currentMode == MODE_SELECTION_SOMMET ||
                        currentMode == MODE_SELECTION_ARETE ||
                        currentMode == MODE_UNDEFINED){
                    toile.clearStates();
                    ArrayList<Object> all = new ArrayList<Object>();
                    for(Sommet current : Main.graph.getSommets()){
                        current.setState(Var.STATE_FOCUS);
                        all.add(current.getController().getView());
                    }

                    for(Arete current : Main.graph.getAretes()){
                        current.setState(Var.STATE_FOCUS);
                        all.add(current.getController().getView());
                    }

                    if(all.size() > 1)
                        Main.changeEditTool(new MultiSelectedToolPanel(all));
                    else if(all.size() == 0)
                        Main.changeEditTool(new ToolPanel());
                    else{
                        Object o = all.get(0);
                        if(o instanceof SommetView)
                            Main.changeEditTool(new SelectedSommetToolPanel((SommetView) o));
                        else if(o instanceof AreteView)
                            Main.changeEditTool(new SelectedAreteToolPanel((AreteView) o));
                    }
                }
            }
        });
        selectAll.setAccelerator(new KeyCodeCombination(KeyCode.A, KeyCombination.CONTROL_DOWN));

        MenuItem selectSommets = new MenuItem("Sommets");
        selectSommets.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(currentMode == MODE_MULTI_SELECT ||
                        currentMode == MODE_SELECTION_SOMMET ||
                        currentMode == MODE_SELECTION_ARETE ||
                        currentMode == MODE_UNDEFINED){
                    toile.clearStates();
                    ArrayList<Object> all = new ArrayList<Object>();
                    for(Sommet current : Main.graph.getSommets()){
                        current.setState(Var.STATE_FOCUS);
                        all.add(current.getController().getView());
                    }

                    if(all.size() > 1)
                        Main.changeEditTool(new MultiSelectedToolPanel(all));
                    else if(all.size() == 0)
                        Main.changeEditTool(new ToolPanel());
                    else{
                        Object o = all.get(0);
                        if(o instanceof SommetView)
                            Main.changeEditTool(new SelectedSommetToolPanel((SommetView) o));
                    }
                }
            }
        });
        selectSommets.setAccelerator(new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_DOWN));

        MenuItem selectAretes = new MenuItem("Aretes");
        selectAretes.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(currentMode == MODE_MULTI_SELECT ||
                        currentMode == MODE_SELECTION_SOMMET ||
                        currentMode == MODE_SELECTION_ARETE ||
                        currentMode == MODE_UNDEFINED){
                    toile.clearStates();
                    ArrayList<Object> all = new ArrayList<Object>();
                    for(Arete current : Main.graph.getAretes()){
                        current.setState(Var.STATE_FOCUS);
                        all.add(current.getController().getView());
                    }

                    if(all.size() > 1)
                        Main.changeEditTool(new MultiSelectedToolPanel(all));
                    else if(all.size() == 0)
                        Main.changeEditTool(new ToolPanel());
                    else{
                        Object o = all.get(0);
                        if(o instanceof AreteView)
                            Main.changeEditTool(new SelectedAreteToolPanel((AreteView) o));
                    }
                }
            }
        });
        selectAretes.setAccelerator(new KeyCodeCombination(KeyCode.E, KeyCombination.CONTROL_DOWN));

        MenuItem removeSelected = new MenuItem("Supprimer");
        removeSelected.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(currentMode == MODE_MULTI_SELECT ||
                        currentMode == MODE_SELECTION_SOMMET ||
                        currentMode == MODE_SELECTION_ARETE ||
                        currentMode == MODE_UNDEFINED){
                    for(int i = Main.graph.getSommets().size() -1; i >= 0; i--){
                        Sommet current = Main.graph.getSommets().get(i);
                        if(current.getState() == Var.STATE_FOCUS){
                            Main.graph.removeSommet(current);
                        }
                    }

                    for(int i = Main.graph.getAretes().size() -1; i >= 0; i--){
                        Arete current = Main.graph.getAretes().get(i);
                        if(current.getState() == Var.STATE_FOCUS){
                            Main.graph.removeArete(current);
                        }
                    }
                }
            }
        });
        removeSelected.setAccelerator(new KeyCodeCombination(KeyCode.D, KeyCombination.CONTROL_DOWN));

        MenuItem openGraph = new MenuItem("Ouvrir un graphe");
        openGraph.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FileChooser fc = new FileChooser();
                fc.setTitle("Sélectionner un fichier");
                fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Graphe", "*.graph"));
                File rep = fc.showOpenDialog(null);
                if(rep == null)
                    return;

                filename = rep.getAbsolutePath();

                IOFile.readGraphFromFile(filename);
            }
        });
        openGraph.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));

        MenuItem saveGraph = new MenuItem("Sauvegarder un graphe");
        saveGraph.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FileChooser fc = new FileChooser();
                fc.setTitle("Sauvegarde du graphe");
                fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Graphe", "*.graph"));
                File rep = fc.showSaveDialog(null);
                if(rep == null)
                    return;

                filename = rep.getAbsolutePath();

                IOFile.saveGraphInFile(graph, filename);
            }
        });
        saveGraph.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN));

        MenuItem quickSaveGraph = new MenuItem("Sauvegarde rapide du graphe");
        quickSaveGraph.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(filename == null){
                    FileChooser fc = new FileChooser();
                    fc.setTitle("Sauvegarde du graphe");
                    fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Graphe", "*.graph"));
                    File rep = fc.showSaveDialog(null);
                    if(rep == null)
                        return;

                    filename = rep.getAbsolutePath();
                }

                IOFile.saveGraphInFile(graph, filename);
            }
        });
        quickSaveGraph.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));

        fichier.getItems().addAll(
                openGraph,
                saveGraph,
                quickSaveGraph,
                quit
        );

        editer.getItems().addAll(
                removeSelected
        );

        selection.getItems().addAll(
                selectAll,
                selectSommets,
                selectAretes
        );

        menuBar.getMenus().addAll(
                fichier,
                editer,
                selection
        );

        return menuBar;
    }

    public static void changeEditTool(ToolPanel newToolPanel){
        if(newToolPanel instanceof SelectedSommetToolPanel)
            currentMode = MODE_SELECTION_SOMMET;
        else if(newToolPanel instanceof SelectedAreteToolPanel)
            currentMode = MODE_SELECTION_ARETE;
        else if(newToolPanel instanceof DrawSommetToolPanel)
            currentMode = MODE_SOMMET_DRAW;
        else if(newToolPanel instanceof AreteToolPanel)
            currentMode = MODE_ARETE_DRAW;
        else if(newToolPanel instanceof MultiSelectedToolPanel)
            currentMode = MODE_MULTI_SELECT;
        else if(newToolPanel instanceof AlgoToolPanel)
            currentMode = MODE_ALGO;
        else if(newToolPanel instanceof ToolPanel)
            currentMode = MODE_UNDEFINED;

        editPanelPan.getChildren().clear();
        editPanelPan.setCenter(newToolPanel);
        toolPanel = newToolPanel;
    }

    public static void takeSnapshot(){
        WritableImage image = new WritableImage((int)toile.getWidth(), (int)toile.getHeight());
        toile.snapshot(null, image);

        FileChooser f = new FileChooser();
        f.setTitle("Snapshot file");
        f.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG", "*.png"));

        File selectedFile = f.showSaveDialog(primaryStage);
        if(selectedFile != null){
            try{
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", selectedFile);
            }catch (IOException e){
                System.err.println(e.getMessage());
            }
        }
    }

    public static void infoDialog(String title, String content){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.setHeaderText(null);

        alert.show();
    }

    public static void warningDialog(String title, String content){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setContentText(null);
        alert.setHeaderText(content);

        alert.show();
    }

}
