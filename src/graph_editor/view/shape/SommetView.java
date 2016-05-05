package graph_editor.view.shape;


import graph_editor.Main;
import graph_editor.controller.SommetController;
import graph_editor.util.Var;
import graph_editor.view.tool_panel.SelectedSommetToolPanel;
import graph_editor.view.draw.AreteDraw;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;

public class SommetView extends StackPane{

    private EventHandler<MouseEvent> clickToSelect = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            if(!isDragged && (Main.currentMode == Main.MODE_UNDEFINED ||
                    Main.currentMode == Main.MODE_SELECTION_ARETE ||
                    Main.currentMode == Main.MODE_SELECTION_SOMMET ||
                    Main.currentMode == Main.MODE_MULTI_SELECT)) {
                Main.toile.clearStates();
                Main.changeEditTool(new SelectedSommetToolPanel(SommetView.this));
                event.consume();
            }
        }
    };

    private EventHandler<MouseEvent> pressed = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            isDragged = false;
            initDrag(event.getX(), event.getY(), true);
            event.consume();
        }
    };

    private EventHandler<MouseEvent> dragged = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            drag(event.getX(), event.getY(), true);
            event.consume();
        }
    };

    private EventHandler<MouseEvent> clickedAreteDrawTool = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            if(event.getButton().equals(MouseButton.PRIMARY))
                ((AreteDraw) Main.toile.getDrawTool()).addSommetView(SommetView.this, true);
        }
    };

    private EventHandler<MouseEvent> addSommetToAreteDrawTool = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            ((AreteDraw) Main.toile.getDrawTool()).addSommetView(SommetView.this, false);
        }
    };

    private EventHandler<MouseEvent> rmSommetToArreteDrawTool = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            ((AreteDraw) Main.toile.getDrawTool()).rmSommetView();
        }
    };

    private Point2D initTranslation, anchor;

    private Shape shape;

    private Text title;

    private SommetController controller;

    private boolean isDragged;

    private IntegerProperty state;

    private StringProperty name;

    private StringProperty info;


    public SommetView(SommetController controller, Shape s) {
        super();

        state = new SimpleIntegerProperty();
        name = new SimpleStringProperty();
        info = new SimpleStringProperty();

        addEventFilter(MouseEvent.MOUSE_CLICKED, clickToSelect);

        this.controller = controller;
        this.controller.setView(this);

        shape = s;

        title = new Text();

        name.addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                updateText(newValue, info.get());
            }
        });

        info.addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                updateText(name.get(), newValue);
            }
        });

        state.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if(newValue.intValue() == Var.STATE_NONE)
                    removeBorder();
                else if(newValue.intValue() == Var.STATE_VISITED)
                    addBorder(Var.ALGO_COLOR_VISITED);
                else if(newValue.intValue() == Var.STATE_CURRENT)
                    addBorder(Var.ALGO_COLOR_CURRENT);
                else if(newValue.intValue() == Var.STATE_END)
                    addBorder(Var.ALGO_COLOR_END);
                else if(newValue.intValue() == Var.STATE_FOCUS){
                    addBorder(Var.SOMMET_COLOR_FOCUS);
                    addEventFilter(MouseEvent.MOUSE_PRESSED, pressed);
                    addEventFilter(MouseEvent.MOUSE_DRAGGED, dragged);
                }

                if(oldValue.intValue() == Var.STATE_FOCUS){
                    removeEventFilter(MouseEvent.MOUSE_PRESSED, pressed);
                    removeEventFilter(MouseEvent.MOUSE_DRAGGED, dragged);
                }
            }
        });

        getChildren().addAll(
                shape,
                title
        );
    }


    public static SommetView newRectangle(SommetController controller){
        Rectangle r = new Rectangle(Var.RECTANGLE_WIDTH, Var.RECTANGLE_HEIGHT, Var.SOMMET_DEFAULT_COLOR);
        SommetView sommetView = new SommetView(controller, r);
        sommetView.looseFocus();
        return sommetView;
    }

    public static SommetView newRectangle(){
        return newRectangle(new SommetController());
    }

    public static SommetView newCircle(SommetController controller){
        SommetView sommetView = new SommetView(controller, new Circle(Var.CIRCLE_RADIUS, Var.SOMMET_DEFAULT_COLOR));
        sommetView.looseFocus();
        return sommetView;
    }

    public static SommetView newCircle(){
        return newCircle(new SommetController());
    }

    public void switchShape(String value){
        if(shape instanceof Rectangle && value.equals("Cercle")){
            Rectangle old = (Rectangle) shape;
            Circle circle = new Circle();

            circle.setFill(old.getFill());
            circle.setCenterX(old.getX() + old.getWidth()/2.0);
            circle.setCenterY(old.getY() + old.getHeight()/2.0);
            circle.setRadius(Var.CIRCLE_RADIUS);

            removeFromToile();

            shape = circle;
            addToToile();

            obtainFocus();

            setLayoutX(getLayoutX()+1);
            setLayoutX(getLayoutX()-1);
        }else if(shape instanceof Circle && value.equals("Rectangle")){
            Circle old = (Circle) shape;
            Rectangle rect = new Rectangle();

            rect.setX(old.getCenterX() - Var.RECTANGLE_WIDTH/2.0);
            rect.setY(old.getCenterY() - Var.RECTANGLE_HEIGHT/2.0);
            rect.setWidth(Var.RECTANGLE_WIDTH);
            rect.setHeight(Var.RECTANGLE_HEIGHT);
            rect.setFill(old.getFill());

            removeFromToile();

            shape = rect;
            addToToile();

            obtainFocus();

            setLayoutX(getLayoutX()+1);
            setLayoutX(getLayoutX()-1);
        }
    }

    public Point2D getCenter(){
        return new Point2D(getLayoutX() + getTranslateX() + getWidth()/2, getLayoutY() +getTranslateY() + getHeight()/2);
    }

    public Point2D intersectionWithPointOnCenter(Point2D out){
        if(shape instanceof Rectangle){
            Rectangle rect = (Rectangle) shape;
            Point2D coord = localToParent(rect.localToParent(rect.getX(), rect.getY()));

            double x = coord.getX();
            double y = coord.getY();
            double w = x + Var.RECTANGLE_WIDTH;
            double h = y + Var.RECTANGLE_HEIGHT;
            double xCenter = x + Var.RECTANGLE_WIDTH/2.0;
            double yCenter = y + Var.RECTANGLE_HEIGHT/2.0;
            double xOut = out.getX();
            double yOut = out.getY();

            double m = (yOut - yCenter)/(xOut - xCenter);

            if (xOut <= xCenter) { // left side
                double tmp = m * (-Var.RECTANGLE_WIDTH/2.0);
                if (-Var.RECTANGLE_HEIGHT/2.0 < tmp && tmp < Var.RECTANGLE_HEIGHT/2.0)
                    return new Point2D(x, tmp + yCenter);
            }

            if (xOut >= xCenter) { // right side
                double tmp = m * Var.RECTANGLE_WIDTH/2.0;
                if (-Var.RECTANGLE_HEIGHT/2.0 < tmp && tmp < Var.RECTANGLE_HEIGHT/2.0)
                    return new Point2D(w, tmp + yCenter);
            }

            if (yOut <= yCenter) { // top side
                double tmp = (-Var.RECTANGLE_HEIGHT/2.0) /m;
                if (-Var.RECTANGLE_WIDTH/2.0 < tmp && tmp < Var.RECTANGLE_WIDTH/2.0)
                    return new Point2D(tmp + xCenter, y);
            }

            if (yOut >= yCenter) { // bottom side
                double tmp = (Var.RECTANGLE_HEIGHT/2.0) /m;
                if (-Var.RECTANGLE_WIDTH/2.0 < tmp && tmp < Var.RECTANGLE_WIDTH/2.0)
                    return new Point2D(tmp + xCenter, h);
            }

            return null;
        }else if(shape instanceof Circle){
            Circle circle = (Circle) shape;
            Point2D coord = localToParent(circle.localToParent(circle.getCenterX(), circle.getCenterY()));

            double x = coord.getX();
            double y = coord.getY();
            double radius = Var.CIRCLE_RADIUS;
            double xOut = out.getX();
            double yOut = out.getY();

            double atan = Math.atan2(yOut - y, xOut - x);
            return new Point2D(x + radius * Math.cos(atan), y + radius * Math.sin(atan));
        }

        return null;
    }

    public void bindAreteView(AreteView areteView){
        layoutXProperty().addListener(areteView);
        layoutYProperty().addListener(areteView);

        translateXProperty().addListener(areteView);
        translateYProperty().addListener(areteView);

        scaleXProperty().addListener(areteView);
        scaleYProperty().addListener(areteView);
    }
    
    public void unbindAreteView(AreteView areteView){
        layoutXProperty().removeListener(areteView);
        layoutYProperty().removeListener(areteView);

        translateXProperty().removeListener(areteView);
        translateYProperty().removeListener(areteView);

        scaleXProperty().removeListener(areteView);
        scaleYProperty().removeListener(areteView);
    }

    public void addEventAreteDrawTool(){
        addEventFilter(MouseEvent.MOUSE_ENTERED, addSommetToAreteDrawTool);
        addEventFilter(MouseEvent.MOUSE_EXITED, rmSommetToArreteDrawTool);
        addEventFilter(MouseEvent.MOUSE_CLICKED, clickedAreteDrawTool);
    }

    public void rmEventAreteDrawTool(){
        removeEventFilter(MouseEvent.MOUSE_ENTERED, addSommetToAreteDrawTool);
        removeEventFilter(MouseEvent.MOUSE_EXITED, rmSommetToArreteDrawTool);
        removeEventFilter(MouseEvent.MOUSE_CLICKED, clickedAreteDrawTool);
    }

    public void initDrag(double x, double y, boolean propage){
        initTranslation = new Point2D(this.getTranslateX(), this.getTranslateY());
        anchor = SommetView.this.localToParent(x, y);

        if(propage)
            Main.toile.initDragOthers(x, y, this);
    }

    public void drag(double x, double y, boolean propage) {
        isDragged = true;
        Point2D p = SommetView.this.localToParent(x, y);
        setTranslateX(initTranslation.getX() - anchor.getX() + p.getX());
        setTranslateY(initTranslation.getY() - anchor.getY() + p.getY());
        isDragged = false;

        if (propage)
            Main.toile.dragOthers(x, y, this);
    }

    public void addBorder(Color color){
        shape.setStroke(color);
        shape.setStrokeWidth(3);
    }

    public void removeBorder(){
        shape.setStrokeWidth(0);
    }

    public void addToToile(){
        Main.toile.getChildren().addAll(this);
    }

    public void removeFromToile(){
        Main.toile.getChildren().removeAll(this);
    }

    public void addToGraph(){
        controller.addToGraph();
    }

    public boolean isInside(Rectangle rectangle){
        return rectangle.contains(getCenter());
    }

    public Shape getSommetShape(){
        return shape;
    }

    public SommetController getController(){
        return controller;
    }

    private void updateText(String n, String i){
        String s = "";
        if(n != null)
            s += n;

        if(i != null && i.length() > 0)
            s += " ("+i+")";

        title.setText(s);
    }

    @Override
    public String toString() {
        if(controller != null)
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
        NAME PROPERTY
     */
    public final String getName(){
        return name.get();
    }

    public final void setName(String name){
        this.name.set(name);
    }

    public StringProperty nameProperty(){
        return name;
    }


    /*
        INFO PROPERTY
     */
    public final String getInfo(){
        return info.get();
    }

    public final void setInto(String m){
        this.info.set(m);
    }

    public StringProperty infoProperty(){
        return info;
    }


    /*
        STATE PROPERTY
     */
    public final int getState(){
        return state.get();
    }

    public final void setState(int i){
        state.setValue(i);
    }

    public final IntegerProperty stateProperty(){
        return state;
    }
}
