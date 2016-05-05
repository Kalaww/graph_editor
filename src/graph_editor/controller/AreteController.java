package graph_editor.controller;


import graph_editor.Main;
import graph_editor.model.Arete;
import graph_editor.model.Sommet;
import graph_editor.view.shape.AreteView;
import graph_editor.view.shape.SommetView;

public class AreteController {

    private Arete model;

    private AreteView view;

    public AreteController(){

    }

    public void updateModelSommetView1(SommetView sommetView){
        if(model == null)
            return;

        model.setSommet1(sommetView.getController().getModel());
    }

    public void updateModelSommetView2(SommetView sommetView){
        if(model == null)
            return;

        model.setSommet2(sommetView.getController().getModel());
    }

    public void updateModelPoids(int poids){
        if(model == null)
            return;

        model.setPoids(poids);
    }

    public void updateModelSens(Arete.Sens sens){
        if(model == null)
            return;

        model.setSens(sens);
    }

    public void addToGraph(){
        if(model == null)
            setModel(new Arete(this,
                    view.getSommetView1().getController().getModel(),
                    view.getSommetView2().getController().getModel(),
                    view.getPoids(),
                    view.getSens()
            ));
        Main.graph.addArete(model);
    }

    public void addToToile(){
        if(view == null)
            setView(new AreteView(this,
                    model.getSommet1().getController().getView(),
                    model.getSommet2().getController().getView(),
                    model.getPoids(),
                    model.getSens()
            ));
        view.addToToile();
    }

    public void bindBidirectional(){
        model.stateProperty().bindBidirectional(view.stateProperty());
        model.poidsProperty().bindBidirectional(view.poidsProperty());
    }

    public void unbindBidirectional(){
        model.stateProperty().unbindBidirectional(view.stateProperty());
        model.poidsProperty().unbindBidirectional(view.poidsProperty());
    }

    public void remove(){
        unbindBidirectional();
        if(view != null)
            view.removeFromToile();
        if(model != null)
            Main.graph.removeArete(model);
    }

    public void setModel(Arete arete){
        if(model != null && view != null)
            unbindBidirectional();

        model = arete;

        if(model != null && view != null)
            bindBidirectional();
    }

    public Arete getModel(){
        return model;
    }

    public void setView(AreteView areteView){
        if(model != null && view != null)
            unbindBidirectional();

        view = areteView;

        if(model != null && view != null)
            bindBidirectional();
    }

    public AreteView getView(){
        return view;
    }
}
