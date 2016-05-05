package graph_editor.view.shape;


import com.sun.javafx.geom.Vec2d;
import graph_editor.Main;
import graph_editor.controller.AreteController;
import graph_editor.model.Arete;
import graph_editor.util.Var;
import graph_editor.view.tool_panel.SelectedAreteToolPanel;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;


public class AreteView extends Polyline implements ChangeListener{

    private EventHandler<MouseEvent> clickToSelect = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            if(Main.currentMode == Main.MODE_UNDEFINED ||
                    Main.currentMode == Main.MODE_SELECTION_ARETE ||
                    Main.currentMode == Main.MODE_SELECTION_SOMMET ||
                    Main.currentMode == Main.MODE_MULTI_SELECT) {
                Main.toile.clearStates();
                Main.changeEditTool(new SelectedAreteToolPanel(AreteView.this));
                event.consume();
            }
        }
    };

    private SommetView sommetView1, sommetView2;

    private IntegerProperty poids;

    private Arete.Sens sens;

    private AreteController controller;

    private Point2D oldp1, oldp2;

    private Text poidsView;

    private Vec2d center;

    private IntegerProperty state;

    public AreteView(AreteController controller, SommetView s1, SommetView s2, int poids, Arete.Sens sens){
        this.controller = controller;

        this.poids = new SimpleIntegerProperty();

        state = new SimpleIntegerProperty();

        poidsView = new Text();

        center = new Vec2d();

        this.controller.setView(this);

        state.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if(newValue.intValue() == Var.STATE_NONE)
                    addBorder(Var.ARETE_COLOR_DEFAULT);
                else if(newValue.intValue() == Var.STATE_VISITED)
                    addBorder(Var.ALGO_COLOR_VISITED);
                else if(newValue.intValue() == Var.STATE_CURRENT)
                    addBorder(Var.ALGO_COLOR_CURRENT);
                else if(newValue.intValue() == Var.STATE_FOCUS)
                    addBorder(Var.ARETE_COLOR_FOCUS);
                else if(newValue.intValue() == Var.STATE_END)
                    addBorder(Var.ALGO_COLOR_END);
            }
        });

        this.poids.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if(newValue.intValue() > 0){
                    poidsView.setText(newValue.intValue()+"");
                }else{
                    poidsView.setText("?");
                }
            }
        });

        setPoids(poids);
        setSommetView1(s1);
        setSommetView2(s2);
        setSens(sens);

        addEventFilter(MouseEvent.MOUSE_CLICKED, clickToSelect);

        setStrokeWidth(Var.ARETE_WIDTH);
        setStroke(Var.ARETE_COLOR_DEFAULT);
    }

    public AreteView(SommetView s1, SommetView s2, int poids, Arete.Sens sens){
        this(new AreteController(), s1, s2, poids, sens);
    }

    public void drawArete(){
        if(sommetView1 == null || sommetView2 == null || sens == null)
            return;

        getPoints().clear();

        Point2D p1 = sommetView1.intersectionWithPointOnCenter(sommetView2.getCenter());
        if(p1 == null)
            p1 = oldp1;
        else
            oldp1 = p1;

        Point2D p2 = sommetView2.intersectionWithPointOnCenter(sommetView1.getCenter());
        if(p2 == null)
            p2 = oldp2;
        else
            oldp2 = p2;

        double distance  = p1.distance(p2);

        if(sens.equals(Arete.Sens.BOTH) || sens.equals(Arete.Sens.UNI)){
            double unrotatedX = p2.getX() + ((p1.getX() - p2.getX()) / distance) * Var.ARROW_LENGHT;
            double unrotatedY = p2.getY() + ((p1.getY() - p2.getY()) / distance) * Var.ARROW_LENGHT;

            Point2D rotated2a = new Point2D(
                    p2.getX() + (unrotatedX - p2.getX())*Math.cos(0.5) - (unrotatedY - p2.getY())*Math.sin(0.5),
                    p2.getY() + (unrotatedX - p2.getX())*Math.sin(0.5) + (unrotatedY - p2.getY())*Math.cos(0.5));

            Point2D rotated2b = new Point2D(
                    p2.getX() + (unrotatedX - p2.getX())*Math.cos(-0.5) - (unrotatedY - p2.getY())*Math.sin(-0.5),
                    p2.getY() + (unrotatedX - p2.getX())*Math.sin(-0.5) + (unrotatedY - p2.getY())*Math.cos(-0.5));

            getPoints().addAll(
                    rotated2a.getX(), rotated2a.getY(),
                    p2.getX(), p2.getY(),
                    rotated2b.getX(), rotated2b.getY()
            );
        }

        getPoints().addAll(
                p2.getX(), p2.getY(),
                p1.getX(), p1.getY());


        center.x = (p1.getX() + p2.getX()) / 2.0;
        center.y = (p1.getY() + p2.getY()) / 2.0;

        poidsView.setX(center.x);
        poidsView.setY(center.y);

        if(sens.equals(Arete.Sens.BOTH)){
            double unrotatedX = p1.getX() + ((p2.getX() - p1.getX()) / distance) * Var.ARROW_LENGHT;
            double unrotatedY = p1.getY() + ((p2.getY() - p1.getY()) / distance) * Var.ARROW_LENGHT;

            Point2D rotated1a = new Point2D(
                    p1.getX() + (unrotatedX - p1.getX())*Math.cos(0.5) - (unrotatedY - p1.getY())*Math.sin(0.5),
                    p1.getY() + (unrotatedX - p1.getX())*Math.sin(0.5) + (unrotatedY - p1.getY())*Math.cos(0.5));

            Point2D rotated1b = new Point2D(
                    p1.getX() + (unrotatedX - p1.getX())*Math.cos(-0.5) - (unrotatedY - p1.getY())*Math.sin(-0.5),
                    p1.getY() + (unrotatedX - p1.getX())*Math.sin(-0.5) + (unrotatedY - p1.getY())*Math.cos(-0.5));

            getPoints().addAll(
                    rotated1a.getX(), rotated1a.getY(),
                    p1.getX(), p1.getY(),
                    rotated1b.getX(), rotated1b.getY()
            );
        }
    }

    public void addToToile(){
        if(!Main.toile.getChildren().contains(this)){
            Main.toile.getChildren().addAll(this, poidsView);
        }
    }

    public void removeFromToile(){
        sommetView1.unbindAreteView(this);
        sommetView2.unbindAreteView(this);
        Main.toile.getChildren().removeAll(this, poidsView);
    }

    public void addToGraph(){
        controller.addToGraph();
    }

    public boolean isInside(Rectangle rectangle){
        return rectangle.contains(center.x, center.y);
    }

    public void addBorder(Color color){
        setStroke(color);
    }

    public void setSommetView1(SommetView sommetView){
        if(sommetView1 != null)
            sommetView1.unbindAreteView(this);

        sommetView1 = sommetView;
        sommetView1.bindAreteView(this);

        controller.updateModelSommetView1(sommetView);

        drawArete();
    }

    public SommetView getSommetView1(){
        return sommetView1;
    }

    public void setSommetView2(SommetView sommetView){
        if(sommetView2 != null)
            sommetView2.unbindAreteView(this);

        sommetView2 = sommetView;
        sommetView2.bindAreteView(this);

        controller.updateModelSommetView2(sommetView);

        drawArete();
    }

    public SommetView getSommetView2(){
        return sommetView2;
    }

    public void setSens(Arete.Sens sens){
        this.sens = sens;
        controller.updateModelSens(sens);
        drawArete();
    }

    public Arete.Sens getSens(){
        return sens;
    }


    public void setController(AreteController controller){
        this.controller = controller;
    }

    public AreteController getController(){
        return controller;
    }

    @Override
    public String toString() {
        if(controller != null && controller.getModel() != null)
            return controller.getModel().toString();
        return super.toString();
    }

    public void obtainFocus(){
        setState(Var.STATE_FOCUS);
    }

    public void looseFocus(){
        setState(Var.STATE_NONE);
    }

    public boolean hasFocus(){
        return state.getValue() == Var.STATE_FOCUS;
    }


    /*
            POIDS PROPERTY
    */
    public final int getPoids(){
        return poids.get();
    }

    public final void setPoids(int p){
        poids.set(p);
        drawArete();
    }

    public IntegerProperty poidsProperty(){
        return poids;
    }


    /*
        STATE PROPERTY
     */
    public final int getState(){
        return state.get();
    }

    public final void setState(int i){
        state.setValue(i);
        drawArete();
    }

    public final IntegerProperty stateProperty(){
        return state;
    }


    /*
        LISTENERS
     */

    @Override
    public void changed(ObservableValue observable, Object oldValue, Object newValue) {
        drawArete();
    }
}
