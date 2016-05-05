package graph_editor.model;


import graph_editor.Main;
import graph_editor.util.Var;

import java.util.HashMap;
import java.util.Map;

public class AlgoPrim extends Algorithme{

    private Sommet sommetDepart;

    private Arete currentArete;

    @Override
    public boolean graphValid() {
        setStepNumber(0);
        return true;
    }

    @Override
    public boolean ready() {
        if(sommetDepart == null){
            Main.warningDialog("Algorithme Prim", "Aucun sommet de départ n'a été sélectionné");
            return false;
        }

        for(Arete current : Main.graph.getAretes()){
            if(!current.getSens().equals(Arete.Sens.NONE)){
                Main.warningDialog("Algorithme Kruskal", "Toutes les arêtes doivent être non orientés");
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean nextStep() {
        if(stepNumber.get() == 0){
            if(!ready())
                return false;
        }

        Sommet oldSommet = null;
        Sommet nextSommet = null;
        Arete nextArete = null;
        int min = Integer.MAX_VALUE;

        for(Sommet current : Main.graph.getCurrentSommets()){
            HashMap<Arete, Sommet> nexts = current.connections();
            for(Map.Entry<Arete, Sommet> entry : nexts.entrySet()){
                if(entry.getValue().getState() != Var.STATE_NONE)
                    continue;

                int currentAretePoids = entry.getKey().getPoids();
                if(currentAretePoids < min){
                    oldSommet = current;
                    nextSommet = entry.getValue();
                    nextArete = entry.getKey();
                    min = currentAretePoids;
                }
            }
        }

        if(nextArete == null){
            Main.infoDialog("Algorithme Prim", "L'algorithme est terminé");
            return false;
        }

        swapCurrentArete(nextArete);
        swapCurrentSommet(oldSommet, nextSommet);

        incStepNumber();

        return true;
    }

    @Override
    public boolean previousStep() {
        int stop = getStepNumber();

        if(stop == 0)
            return false;

        reset();
        stop--;
        while(getStepNumber() < stop){
            nextStep();
        }

        return true;
    }

    @Override
    public void reset() {
        setStepNumber(0);
        currentArete = null;
        Main.toile.clearStates();
        sommetDepart.setState(Var.STATE_CURRENT);
        ready();
    }

    private void swapCurrentSommet(Sommet old, Sommet nnew){
        HashMap<Arete, Sommet> tmp = old.connections();
        boolean b = false;
        for(Arete current : tmp.keySet()){
            if(current.getState() == Var.STATE_NONE) {
                b = true;
                break;
            }
        }

        if(!b)
            old.setState(Var.STATE_VISITED);

        tmp = nnew.connections();
        b = false;
        for(Arete current : tmp.keySet()){
            if(current.getState() == Var.STATE_NONE) {
                b = true;
                break;
            }
        }

        if(b)
            nnew.setState(Var.STATE_CURRENT);
        else
            nnew.setState(Var.STATE_VISITED);

        for(Sommet current : Main.graph.getCurrentSommets()){
            b = true;
            HashMap<Arete, Sommet> nexts = current.connections();
            for(Map.Entry<Arete, Sommet> entry : nexts.entrySet()){
                if(entry.getValue().getState() == Var.STATE_NONE){
                    b = false;
                    break;
                }
            }

            if(b)
                current.setState(Var.STATE_VISITED);
        }
    }

    private void swapCurrentArete(Arete nnew){
        if(currentArete != null)
            currentArete.setState(Var.STATE_VISITED);
        currentArete = nnew;
        currentArete.setState(Var.STATE_CURRENT);


    }

    public void setSommetDepart(Sommet sommet){
        if(sommetDepart != null)
            sommetDepart.setState(Var.STATE_NONE);

        sommetDepart = sommet;

        if(sommetDepart != null)
            sommetDepart.setState(Var.STATE_CURRENT);
    }
}
