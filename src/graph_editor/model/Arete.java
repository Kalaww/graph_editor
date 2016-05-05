package graph_editor.model;


import graph_editor.Main;
import graph_editor.controller.AreteController;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;


public class Arete{

    private Sommet sommet1, sommet2;

    private IntegerProperty poids;

    public enum Sens{
        NONE, UNI, BOTH
    };

    private Sens sens;

    private AreteController controller;

    private IntegerProperty state;

    public Arete(AreteController controller, Sommet s1, Sommet s2, int poids, Sens sens){
        this.controller = controller;
        sommet1 = s1;
        sommet2 = s2;
        this.poids = new SimpleIntegerProperty();
        this.sens = sens;
        state = new SimpleIntegerProperty();

        setPoids(poids);
        controller.setModel(this);
    }

    public Arete(Sommet s1, Sommet s2, int poids, Sens sens) {
        this(new AreteController(), s1, s2, poids, sens);
    }


    public boolean hasSommet(Sommet sommet){
        return sommet == sommet1 || sommet == sommet2;
    }

    public void remove(){
        Main.graph.removeArete(this);
        controller.getView().removeFromToile();
    }


    public void setSommet1(Sommet s){
        sommet1 = s;
    }

    public Sommet getSommet1(){
        return sommet1;
    }

    public void setSommet2(Sommet s){
        sommet2 = s;
    }

    public Sommet getSommet2(){
        return sommet2;
    }

    public void setSens(Sens s){
        sens = s;
    }

    public Sens getSens(){
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
        String t = "";
        if(sens.equals(Sens.UNI))
            t = " --> ";
        else if(sens.equals(Sens.BOTH))
            t = " <-> ";
        else
            t = " --- ";
        return "Arete "+sommet1.getName()+t+sommet2.getName();
    }


    /*
            POIDS PROPERTY
    */
    public final int getPoids(){
        return poids.get();
    }

    public final void setPoids(int p){
        poids.setValue(p);
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
    }

    public final IntegerProperty stateProperty(){
        return state;
    }
}
