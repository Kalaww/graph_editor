package graph_editor.view.draw;


import com.sun.javafx.geom.Vec2d;
import graph_editor.Main;
import graph_editor.model.Sommet;
import graph_editor.view.shape.AreteView;
import graph_editor.view.shape.SommetView;
import graph_editor.view.tool_panel.MultiSelectedToolPanel;
import graph_editor.view.tool_panel.SelectedAreteToolPanel;
import graph_editor.view.tool_panel.SelectedSommetToolPanel;
import graph_editor.view.tool_panel.ToolPanel;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class MultiSelectDraw implements Draw{

    private EventHandler<MouseEvent> mousePressed;
    private EventHandler<MouseEvent> mouseReleased;
    private EventHandler<MouseEvent> mouseDragged;

    private boolean dragged;

    private Vec2d posPressed;
    private Rectangle rectSelection;

    private ArrayList<Object> selectedNodes;

    public MultiSelectDraw(){

        selectedNodes = new ArrayList<Object>();

        rectSelection = new Rectangle();
        rectSelection.setFill(Color.TRANSPARENT);
        rectSelection.setStroke(Color.BLACK);

        mousePressed = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                posPressed = new Vec2d(event.getX(), event.getY());
                dragged = false;
            }
        };

        mouseDragged = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(!dragged){
                    dragged = true;
                    if(!Main.toile.getChildren().contains(rectSelection))
                        Main.toile.getChildren().add(rectSelection);
                }

                double x = Math.min(posPressed.x, event.getX());
                double y = Math.min(posPressed.y, event.getY());
                double w = Math.abs(posPressed.x - event.getX());
                double h = Math.abs(posPressed.y - event.getY());

                rectSelection.setX(x);
                rectSelection.setY(y);
                rectSelection.setWidth(w);
                rectSelection.setHeight(h);

                selectedNodes.clear();
                for(Node current : Main.toile.getChildren()){
                    if(current instanceof SommetView){
                        SommetView s = (SommetView) current;
                        if(s.isInside(rectSelection)) {
                            s.obtainFocus();
                            selectedNodes.add(s);
                        }else {
                            s.looseFocus();
                        }
                    }else if(current instanceof AreteView){
                        AreteView a = (AreteView) current;
                        if(a.isInside(rectSelection)) {
                            a.obtainFocus();
                            selectedNodes.add(a);
                        }else {
                            a.looseFocus();
                        }
                    }
                }

                if(selectedNodes.size() > 1)
                    Main.changeEditTool(new MultiSelectedToolPanel(selectedNodes));
                else if(selectedNodes.size() == 0)
                    Main.changeEditTool(new ToolPanel());
                else{
                    Object o = selectedNodes.get(0);
                    if(o instanceof SommetView)
                        Main.changeEditTool(new SelectedSommetToolPanel((SommetView) o));
                    else if(o instanceof AreteView)
                        Main.changeEditTool(new SelectedAreteToolPanel((AreteView) o));
                }
            }
        };

        mouseReleased = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(dragged) {
                    Main.toile.getChildren().remove(rectSelection);
                    event.consume();
                    dragged = false;
                }
            }
        };
    }

    @Override
    public void attachEvents(Node node) {
        node.addEventHandler(MouseEvent.MOUSE_PRESSED, mousePressed);
        node.addEventHandler(MouseEvent.MOUSE_DRAGGED, mouseDragged);
        node.addEventHandler(MouseEvent.MOUSE_RELEASED, mouseReleased);
    }

    @Override
    public void detachEvents(Node node) {
        node.removeEventHandler(MouseEvent.MOUSE_PRESSED, mousePressed);
        node.removeEventHandler(MouseEvent.MOUSE_DRAGGED, mouseDragged);
        node.removeEventHandler(MouseEvent.MOUSE_RELEASED, mouseReleased);
    }
}
