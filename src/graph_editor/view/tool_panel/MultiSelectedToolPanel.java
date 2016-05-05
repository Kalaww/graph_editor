package graph_editor.view.tool_panel;


import graph_editor.Main;
import graph_editor.view.shape.AreteView;
import graph_editor.view.shape.SommetView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.util.Callback;

import java.util.ArrayList;

public class MultiSelectedToolPanel extends ToolPanel {

    private ListView<Object> list;

    private ObservableList<Object> observableList;

    public MultiSelectedToolPanel(ArrayList<Object> items){
        super();

        list = new ListView<Object>();

        observableList = FXCollections.observableList(items);

        list.setItems(observableList);
        list.setCellFactory(new Callback<ListView<Object>, ListCell<Object>>() {
            @Override
            public ListCell<Object> call(ListView<Object> param) {
                ListCell<Object> cell = new ListCell<Object>(){
                    @Override
                    protected void updateItem(Object item, boolean empty) {
                        super.updateItem(item, empty);
                        if(item != null){
                            setText(item.toString());
                        }
                    }
                };

                return cell;
            }
        });
        
        list.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
            @Override
            public void changed(ObservableValue<?> observable, Object oldValue, Object newValue) {
                if(newValue == null)
                    return;

                if(newValue instanceof SommetView){
                    Main.toile.clearStates();
                    Main.changeEditTool(new SelectedSommetToolPanel((SommetView) newValue));
                }else if(newValue instanceof AreteView){
                    Main.toile.clearStates();
                    Main.changeEditTool(new SelectedAreteToolPanel((AreteView) newValue));
                }
            }
        });

        getChildren().addAll(
                new Label("Liste des éléments selectionnés"),
                new ScrollPane(list)
        );
    }
}
