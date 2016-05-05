package graph_editor.model;


import graph_editor.Main;
import graph_editor.controller.SommetController;
import javafx.beans.property.*;

import java.util.HashMap;


public class Sommet {

    private StringProperty name;

    private StringProperty info;

    private IntegerProperty state;

    private SommetController controller;

    public Sommet(SommetController controller, String name){
        this.controller = controller;
        this.name = new SimpleStringProperty();
        this.info = new SimpleStringProperty();
        this.state = new SimpleIntegerProperty();

        controller.setModel(this);

        if(name == null || name.length() == 0)
            setName(Main.graph.getAvailableName());
        else
            setName(name);
    }

    public Sommet(String name){
        this(new SommetController(), name);
    }

    public void remove(){
        Main.graph.removeSommet(this);
        controller.getView().removeFromToile();
    }

    public HashMap<Arete, Sommet> connections(){
        HashMap<Arete, Sommet> tmp = new HashMap<Arete, Sommet>();
        for(Arete current : Main.graph.getAretes()){
            if(current.hasSommet(this) && (
                    current.getSens().equals(Arete.Sens.UNI) && current.getSommet1() == this
                            || !current.getSens().equals(Arete.Sens.UNI)
            )){
                Sommet next = (current.getSommet1() == this)? current.getSommet2() : current.getSommet1();
                tmp.put(current, next);
            }
        }

        return tmp;
    }

    public void setController(SommetController controller){
        this.controller = controller;
    }

    public SommetController getController(){
        return controller;
    }

    @Override
    public String toString() {
        return "Sommet "+getName();
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
