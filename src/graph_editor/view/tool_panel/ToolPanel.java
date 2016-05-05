package graph_editor.view.tool_panel;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import graph_editor.util.Var;


public class ToolPanel extends VBox {

    public ToolPanel(){
        super();

        setPrefWidth(Var.SELECTION_TOOLBAR_WIDTH);
        setMaxWidth(Var.SELECTION_TOOLBAR_WIDTH);
        setMinHeight(Var.SELECTION_TOOLBAR_WIDTH);

        setPadding(new Insets(10));
        setSpacing(20);
        setAlignment(Pos.TOP_CENTER);
        setStyle(Var.BORDER_BLACK_1);
    }
}
