package graph_editor.view.tool_panel;

import graph_editor.Main;
import graph_editor.model.Arete;
import graph_editor.view.shape.AreteView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;

public class SelectedAreteToolPanel extends AreteToolPanel{

    public static AreteView selectedAreteView;

    public SelectedAreteToolPanel(){
        super();

        selectedAreteView = null;

        getChildren().addAll(
                new Label("Editer"),
                groupDelete()
        );

        fieldPoids.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(selectedAreteView != null){
                    selectedAreteView.setPoids(getPoids());
                }
            }
        });

        radioButtonNone.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(selectedAreteView != null && radioButtonNone.isSelected()){
                    selectedAreteView.setSens(getSens());
                }
            }
        });

        radioButtonUni.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(selectedAreteView != null && radioButtonUni.isSelected()){
                    selectedAreteView.setSens(getSens());
                }
            }
        });

        radioButtonBoth.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(selectedAreteView != null && radioButtonBoth.isSelected()){
                    selectedAreteView.setSens(getSens());
                }
            }
        });

        buttonDelete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                selectedAreteView.getController().remove();
                changeSelectedAreteView(null);
            }
        });
    }

    public SelectedAreteToolPanel(AreteView selected){
        this();

        changeSelectedAreteView(selected);
    }

    public void changeSelectedAreteView(AreteView a){
        if(selectedAreteView != null){
            selectedAreteView.looseFocus();
        }

        if(SelectedSommetToolPanel.selectedSommetView != null){
            SelectedSommetToolPanel.selectedSommetView.looseFocus();
        }

        selectedAreteView = a;

        if(selectedAreteView == null){
            Main.changeEditTool(new ToolPanel());
            return;
        }

        fieldPoids.setText(selectedAreteView.getPoids()+"");
        Arete.Sens sens = selectedAreteView.getSens();
        if(sens.equals(Arete.Sens.NONE)){
            radioButtonNone.setSelected(true);
        }else if(sens.equals(Arete.Sens.UNI)){
            radioButtonUni.setSelected(true);
        }else if(sens.equals(Arete.Sens.BOTH)){
            radioButtonBoth.setSelected(true);
        }

        selectedAreteView.obtainFocus();
    }
}
