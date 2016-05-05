package graph_editor.controller;


import graph_editor.Main;
import graph_editor.model.Sommet;
import graph_editor.view.shape.SommetView;

public class SommetController {

    private Sommet model;

    private SommetView view;

    public SommetController(){

    }

    public void addToGraph(){
        if(model == null)
            setModel(new Sommet(this, view.getName()));

        Main.graph.addSommet(model);
    }

    public void addToToile(){
        if(view == null)
            setView(SommetView.newRectangle(this));
        Main.toile.getChildren().add(view);
    }

    public void bindBidirectional(){
        model.stateProperty().bindBidirectional(view.stateProperty());
        model.nameProperty().bindBidirectional(view.nameProperty());
        model.infoProperty().bindBidirectional(view.infoProperty());
    }

    public void unbindBidirectional(){
        model.stateProperty().unbindBidirectional(view.stateProperty());
        model.nameProperty().unbindBidirectional(view.nameProperty());
        model.infoProperty().unbindBidirectional(view.infoProperty());
    }

    public void remove(){
        unbindBidirectional();
        if(view != null) {
            view.removeFromToile();
        }
        if(model != null)
            Main.graph.removeSommet(model);
    }

    public void setModel(Sommet sommet){
        if(model != null && view != null)
            unbindBidirectional();

        model = sommet;

        if(model != null && view != null)
            bindBidirectional();
    }

    public Sommet getModel(){
        return model;
    }

    public void setView(SommetView sommetView){
        if(model != null && view != null)
            unbindBidirectional();

        view = sommetView;

        if(model != null && view != null)
            bindBidirectional();
    }

    public SommetView getView(){
        return view;
    }
}
