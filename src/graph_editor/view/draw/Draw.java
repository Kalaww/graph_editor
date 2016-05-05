package graph_editor.view.draw;


import javafx.scene.Node;

public interface Draw {

    public void attachEvents(Node node);

    public void detachEvents(Node node);
}
