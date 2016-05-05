package graph_editor.view;

import graph_editor.Main;
import graph_editor.view.draw.AreteDraw;
import graph_editor.view.draw.MultiSelectDraw;
import graph_editor.view.tool_panel.AlgoToolPanel;
import graph_editor.view.tool_panel.AreteToolPanel;
import graph_editor.view.tool_panel.DrawSommetToolPanel;
import graph_editor.view.tool_panel.ToolPanel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;


public class GeneralToolBar extends ToolBar {

    private Button buttonSommet;
    private Button buttonArete;
    private Button buttonHand;
    private Button buttonSnapshot;
    private Button buttonAlgo;

    public GeneralToolBar(){
        super();

        buttonSommet = new Button("Dessiner Sommet");
        buttonArete = new Button("Dessiner Arete");
        buttonHand = new Button("Sélectionner");
        buttonSnapshot = new Button("Snapshot");
        buttonAlgo = new Button("Algorithmes");

        buttonSommet.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Main.toile.clearStates();
                Main.toile.changeDrawTool(null);
                Main.changeEditTool(new DrawSommetToolPanel());
            }
        });

        buttonArete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Main.toile.clearStates();
                Main.toile.changeDrawTool(new AreteDraw());
                Main.changeEditTool(new AreteToolPanel());
            }
        });

        buttonHand.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Main.toile.clearStates();
                Main.toile.changeDrawTool(new MultiSelectDraw());
                Main.changeEditTool(new ToolPanel());
            }
        });

        buttonSnapshot.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Main.takeSnapshot();
            }
        });

        buttonAlgo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Main.toile.clearStates();
                Main.toile.changeDrawTool(null);
                Main.changeEditTool(new AlgoToolPanel());
            }
        });

        getItems().addAll(
                buttonHand,
                buttonSommet,
                buttonArete,
                buttonSnapshot,
                buttonAlgo);
    }
}
