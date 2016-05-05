package graph_editor.view;

import graph_editor.model.Arete;
import graph_editor.util.Var;
import graph_editor.view.draw.Draw;
import graph_editor.view.shape.AreteView;
import graph_editor.view.shape.SommetView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.Iterator;


public class Toile extends Pane {

    private Draw drawTool;

    public Toile(){
        super();

        Rectangle clip = new Rectangle(800, 800);
        setClip(clip);
        clip.widthProperty().bind(widthProperty());
        clip.heightProperty().bind(heightProperty());


        ChangeListener<Number> paneListener = new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if(newValue.doubleValue() < oldValue.doubleValue()){
                    return;
                }

                Canvas grid = new Canvas(getWidth(), getHeight());
                GraphicsContext gc = grid.getGraphicsContext2D();

                double width = grid.getWidth();
                double height = grid.getHeight();

                gc.setFill(Color.WHITE);
                gc.fillRect(0, 0, width, height);

                gc.setLineWidth(0.5);
                gc.setStroke(Color.LIGHTGREY);

                for(int i = Var.TOILE_GRID_INTERVAL; i < height; i += Var.TOILE_GRID_INTERVAL){
                    gc.strokeLine(0, i, width, i);
                }

                for(int i = Var.TOILE_GRID_INTERVAL; i < width; i += Var.TOILE_GRID_INTERVAL){
                    gc.strokeLine(i, 0, i, height);
                }

                getChildren().remove(0);
                getChildren().add(0, grid);
            }
        };

        Canvas background = new Canvas();
        getChildren().add(0, background);
        widthProperty().addListener(paneListener);
        heightProperty().addListener(paneListener);
    }

    public void changeDrawTool(Draw drawTool){
        if(this.drawTool != null)
            this.drawTool.detachEvents(this);

        this.drawTool = drawTool;

        if(drawTool != null)
            this.drawTool.attachEvents(this);
    }

    public Draw getDrawTool(){
        return drawTool;
    }

    public void clearStates(){
        Iterator<Node> nodes = getChildren().iterator();
        while(nodes.hasNext()){
            Node current = nodes.next();
            if(current instanceof SommetView){
                SommetView v = (SommetView) current;
                v.setState(Var.STATE_NONE);
                if(v.getController().getModel() != null)
                    v.getController().getModel().setInto("");
            }

            if(current instanceof AreteView){
                ((AreteView) current).setState(Var.STATE_NONE);
            }
        }
    }

    public void initDragOthers(double x, double y, SommetView source){
        for(Node current : getChildren()){
            if(current instanceof SommetView && current != source){
                SommetView s = (SommetView) current;
                if(s.hasFocus())
                    s.initDrag(x, y, false);
            }
        }
    }

    public void dragOthers(double x, double y, SommetView source){
        for(Node current : getChildren()){
            if(current instanceof SommetView && current != source){
                SommetView s = (SommetView) current;
                if(s.hasFocus())
                    s.drag(x, y, false);
            }
        }
    }
}
