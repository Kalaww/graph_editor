package graph_editor.view.tool_panel;


import graph_editor.Main;
import graph_editor.view.draw.CircleDraw;
import graph_editor.view.draw.RectangleDraw;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class DrawSommetToolPanel extends SommetToolPanel {

    public DrawSommetToolPanel(){
        super();


        comboBoxShape.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue.equals("Rectangle")){
                    Main.toile.changeDrawTool(new RectangleDraw());
                }else if(newValue.equals("Cercle")){
                    Main.toile.changeDrawTool(new CircleDraw());
                }
            }
        });

        comboBoxShape.setValue("Rectangle");


    }
}
