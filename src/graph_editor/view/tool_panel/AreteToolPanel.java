package graph_editor.view.tool_panel;


import graph_editor.Main;
import graph_editor.model.Arete;
import graph_editor.view.draw.AreteDraw;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class AreteToolPanel extends ToolPanel {

    protected static final String RADIO_NONE = "None", RADIO_UNI = "Uni", RADIO_BOTH = "Both";

    protected TextField fieldPoids;

    protected ToggleGroup groupArrowType;

    protected RadioButton radioButtonNone;
    protected RadioButton radioButtonUni;
    protected RadioButton radioButtonBoth;

    protected Button buttonDelete;

    public AreteToolPanel(){
        super();

        getChildren().addAll(
                new Label("Poids"),
                groupPoids(),
                new Separator(),
                new Label("Sens"),
                groupArrowType()
        );
    }

    private Pane groupArrowType(){
        HBox box = new HBox();
        box.setSpacing(10);

        radioButtonNone = new RadioButton(RADIO_NONE);
        radioButtonUni = new RadioButton(RADIO_UNI);
        radioButtonBoth = new RadioButton(RADIO_BOTH);

        groupArrowType = new ToggleGroup();
        radioButtonNone.setToggleGroup(groupArrowType);
        radioButtonUni.setToggleGroup(groupArrowType);
        radioButtonBoth.setToggleGroup(groupArrowType);

        radioButtonNone.setSelected(true);

        box.getChildren().addAll(
                radioButtonNone,
                radioButtonUni,
                radioButtonBoth
        );

        return box;
    }

    private Pane groupPoids(){
        HBox box = new HBox();
        box.setSpacing(10);

        fieldPoids = new TextField();
        fieldPoids.addEventFilter(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(!isValid(fieldPoids.getText()))
                    event.consume();
            }
        });

        fieldPoids.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(!isValid(newValue))
                    fieldPoids.setText("0");
            }
        });

        fieldPoids.setText("0");

        box.getChildren().addAll(
                fieldPoids
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



    private static boolean isValid(String value){
        if(value.length() == 0)
            return true;

        try{
            int i = Integer.parseInt(value);
            if(i >= 0)
                return true;
            return false;
        }catch (NumberFormatException e){
            return false;
        }
    }

    public Arete.Sens getSens(){
        RadioButton radio = (RadioButton) groupArrowType.getSelectedToggle();
        if(radio.getText().equals(RADIO_NONE))
            return Arete.Sens.NONE;
        if(radio.getText().equals(RADIO_UNI))
            return Arete.Sens.UNI;
        if(radio.getText().equals(RADIO_BOTH))
            return Arete.Sens.BOTH;
        return null;
    }

    public int getPoids(){
        return Integer.parseInt(fieldPoids.getText());
    }
}
