package graph_editor.view.draw;

import graph_editor.Main;
import graph_editor.util.Var;
import graph_editor.view.tool_panel.AreteToolPanel;
import graph_editor.view.shape.AreteView;
import graph_editor.view.shape.SommetView;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.util.Iterator;


/*
    ARROW LINE:
    http://www.guigarage.com/2014/11/hand-drawing-effect-javafx/
 */
public class AreteDraw implements Draw {

    private SommetView sommetView1, sommetView2;

    private EventHandler<MouseEvent> cancel = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            if(event.getButton().equals(MouseButton.SECONDARY)){
                if(tmpAreteView == null)
                    return;

                tmpAreteView.removeFromToile();
                tmpAreteView = null;

                if (sommetView1 != null) {
                    sommetView1.removeBorder();
                    sommetView1 = null;
                }

                if(sommetView2 != null){
                    sommetView2.removeBorder();
                    sommetView2 = null;
                }
            }
        }
    };

    private AreteView tmpAreteView;


    public AreteDraw(){

        sommetView1 = null;
        sommetView2 = null;
    }

    public void addSommetView(SommetView sommetView, boolean clicked){
        if(sommetView1 == null && clicked){
            sommetView1 = sommetView;
            sommetView1.addBorder(Var.SOMMET_COLOR_FOCUS);

        }else if(sommetView1 != null && sommetView1 != sommetView && !clicked){
            sommetView2 = sommetView;
            sommetView2.addBorder(Var.SOMMET_COLOR_FOCUS);

            AreteToolPanel panel = (AreteToolPanel) Main.toolPanel;

            tmpAreteView = new AreteView(sommetView1, sommetView2, panel.getPoids(), panel.getSens());
            tmpAreteView.addToToile();
        }else if(sommetView1 != null && sommetView2 != null && clicked){
            tmpAreteView.addToGraph();
            tmpAreteView = null;
            sommetView1.removeBorder();
            sommetView2.removeBorder();

            sommetView1 = null;
            sommetView2 = null;
        }
    }

    public void rmSommetView(){
        if(sommetView1 != null && sommetView2 != null){
            tmpAreteView.removeFromToile();
            sommetView2.removeBorder();
        }
    }



    @Override
    public void attachEvents(Node node) {
        node.addEventFilter(MouseEvent.MOUSE_CLICKED, cancel);

        Iterator<Node> nodes = Main.toile.getChildren().iterator();
        while(nodes.hasNext()){
            Node current = nodes.next();
            if(current instanceof SommetView){
                ((SommetView) current).addEventAreteDrawTool();
            }
        }
    }

    @Override
    public void detachEvents(Node node) {
        node.removeEventFilter(MouseEvent.MOUSE_CLICKED, cancel);

        Iterator<Node> nodes = Main.toile.getChildren().iterator();
        while(nodes.hasNext()){
            Node current = nodes.next();
            if(current instanceof SommetView){
                ((SommetView) current).rmEventAreteDrawTool();
            }
        }
    }
}
