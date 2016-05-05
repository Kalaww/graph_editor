package graph_editor.view.tool_panel;

import graph_editor.Main;
import graph_editor.view.shape.SommetView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class SelectedSommetToolPanel extends SommetToolPanel {

    public static SommetView selectedSommetView;

    public SelectedSommetToolPanel(){
        super();

        getChildren().addAll(
                new Label("Supprimer"),
                groupDelete()
        );

        colorPicker.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(selectedSommetView != null){
                    selectedSommetView.getSommetShape().setFill(colorPicker.getValue());
                }
            }
        });

        fieldName.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(selectedSommetView != null)
                    selectedSommetView.setName(fieldName.getText());
            }
        });

        comboBoxShape.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                selectedSommetView.switchShape(newValue);
            }
        });

        buttonDelete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                selectedSommetView.getController().remove();
                changeSelectedSommetView(null);
            }
        });

    }

    public SelectedSommetToolPanel(SommetView selected){
        this();
        changeSelectedSommetView(selected);
    }

    public void changeSelectedSommetView(SommetView s){
        if(selectedSommetView != null){
            selectedSommetView.looseFocus();
        }

        if(SelectedAreteToolPanel.selectedAreteView != null){
            SelectedAreteToolPanel.selectedAreteView.looseFocus();
        }

        selectedSommetView = s;

        if(selectedSommetView == null){
            Main.changeEditTool(new ToolPanel());
            return;
        }

        colorPicker.setValue((Color) selectedSommetView.getSommetShape().getFill());
        fieldName.setText(selectedSommetView.getName());

        if(selectedSommetView.getSommetShape() instanceof Rectangle){
            comboBoxShape.setValue("Rectangle");
        }else if(selectedSommetView.getSommetShape() instanceof Circle){
            comboBoxShape.setValue("Cercle");
        }

        selectedSommetView.obtainFocus();
    }
}
