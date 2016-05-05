package graph_editor.view.tool_panel;


import graph_editor.util.Var;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class SommetToolPanel extends ToolPanel {

    protected ComboBox<String> comboBoxShape;

    protected ColorPicker colorPicker;

    protected TextField fieldName;

    protected Button buttonDelete;

    public SommetToolPanel(){
        super();

        Label labelForme = new Label("Forme");
        labelForme.setAlignment(Pos.CENTER);

        getChildren().addAll(
                labelForme,
                groupForme(),
                new Separator(),
                new Label("Couleur"),
                groupColor(),
                new Separator(),
                new Label("Attributs"),
                groupAttributs()
        );
    }

    private Pane groupForme(){
        HBox box = new HBox();

        comboBoxShape = new ComboBox<String>(FXCollections.observableArrayList(
                "Rectangle",
                "Cercle"
        ));

        box.getChildren().addAll(
                comboBoxShape
        );

        return box;
    }

    private Pane groupColor(){
        colorPicker = new ColorPicker(Var.SOMMET_DEFAULT_COLOR);

        HBox box = new HBox();

        box.getChildren().addAll(
                colorPicker
        );

        return box;
    }

    private Pane groupAttributs(){
        HBox box = new HBox();
        box.setSpacing(10);

        String text = "Nom du sommet";
        fieldName = new TextField();
        fieldName.setPrefColumnCount(text.length());
        fieldName.setPromptText(text);

        box.getChildren().addAll(
                fieldName
        );

        return box;
    }

    protected Pane groupDelete(){
        HBox box = new HBox();
        box.setSpacing(10);

        buttonDelete = new Button("Supprimer");

        box.getChildren().addAll(buttonDelete);

        return box;
    }

    public String getName(){
        return fieldName.getText();
    }

    public String getForme(){
        return comboBoxShape.getValue();
    }

    public Color getColorChoosed(){
        return colorPicker.getValue();
    }

}
