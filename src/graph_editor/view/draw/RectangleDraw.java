package graph_editor.view.draw;


import graph_editor.Main;
import graph_editor.view.tool_panel.DrawSommetToolPanel;
import graph_editor.util.Var;
import graph_editor.view.shape.SommetView;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class RectangleDraw implements Draw {

    public EventHandler<MouseEvent> mouseConsume;

    public EventHandler<MouseEvent> mouseClicked;
    public EventHandler<MouseEvent> mouseEntered;
    public EventHandler<MouseEvent> mouseExited;
    public EventHandler<MouseEvent> mouseMoved;

    private SommetView tmpShape;

    public RectangleDraw(){

        mouseConsume = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                event.consume();
            }
        };


        mouseEntered  = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                DrawSommetToolPanel panel = (DrawSommetToolPanel) Main.toolPanel;
                tmpShape = SommetView.newRectangle();
                tmpShape.setName(panel.getName());

                Rectangle rect = (Rectangle) tmpShape.getSommetShape();

                tmpShape.setLayoutX(event.getX() - Var.RECTANGLE_WIDTH/2);
                tmpShape.setLayoutY(event.getY() - Var.RECTANGLE_HEIGHT/2);

                rect.setX(0);
                rect.setY(0);

                rect.setWidth(Var.RECTANGLE_WIDTH);
                rect.setHeight(Var.RECTANGLE_HEIGHT);

                Color color = panel.getColorChoosed();
                Color c = new Color(color.getRed(), color.getGreen(), color.getBlue(), Var.OPACITY);
                rect.setFill(c);

                tmpShape.addToToile();
            }
        };

        mouseExited = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                tmpShape.removeFromToile();
            }
        };

        mouseMoved = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                tmpShape.setLayoutX(event.getX() - Var.RECTANGLE_WIDTH/2);
                tmpShape.setLayoutY(event.getY() - Var.RECTANGLE_HEIGHT/2);
            }
        };

        mouseClicked = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                DrawSommetToolPanel panel = (DrawSommetToolPanel) Main.toolPanel;

                SommetView sommetView = SommetView.newRectangle();
                sommetView.setName(panel.getName());

                Rectangle rect = (Rectangle) sommetView.getSommetShape();
                Rectangle tmpRect = (Rectangle) tmpShape.getSommetShape();

                sommetView.setLayoutX(tmpShape.getLayoutX());
                sommetView.setLayoutY(tmpShape.getLayoutY());

                rect.setX(0);
                rect.setY(0);
                rect.setWidth(Var.RECTANGLE_WIDTH);
                rect.setHeight(Var.RECTANGLE_HEIGHT);
                rect.setFill(tmpRect.getFill());

                sommetView.addToToile();
                sommetView.addToGraph();
            }
        };

    }


    @Override
    public void attachEvents(Node node){
        node.addEventFilter(MouseEvent.ANY, mouseConsume);

        node.addEventFilter(MouseEvent.MOUSE_MOVED, mouseMoved);
        node.addEventFilter(MouseEvent.MOUSE_ENTERED, mouseEntered);
        node.addEventFilter(MouseEvent.MOUSE_EXITED, mouseExited);
        node.addEventFilter(MouseEvent.MOUSE_CLICKED, mouseClicked);
    }

    @Override
    public void detachEvents(Node node) {
        node.removeEventFilter(MouseEvent.ANY, mouseConsume);

        node.removeEventFilter(MouseEvent.MOUSE_MOVED, mouseMoved);
        node.removeEventFilter(MouseEvent.MOUSE_ENTERED, mouseEntered);
        node.removeEventFilter(MouseEvent.MOUSE_EXITED, mouseExited);
        node.removeEventFilter(MouseEvent.MOUSE_CLICKED, mouseClicked);
    }


}
