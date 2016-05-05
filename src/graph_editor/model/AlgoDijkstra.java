package graph_editor.model;


import graph_editor.Main;
import graph_editor.util.Var;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AlgoDijkstra extends Algorithme {

    private Sommet sommetDepart;
    private Sommet sommetArrive;

    private Arete currentArete;

    public AlgoDijkstra(){

    }

    @Override
    public boolean graphValid() {
        ArrayList<Sommet> sommets = Main.graph.getSommets();
        if(sommets.size() < 2){
            Main.warningDialog("Algorithme Dijkstra", "Il faut au moins 2 sommets");
            return false;
        }

        ArrayList<Arete> aretes = Main.graph.getAretes();
        Arete.Sens v = null;
        for(Arete current : aretes){
            if(v == null)
                v = current.getSens();
            else if(!current.getSens().equals(v)){
                Main.warningDialog("Algorithme Dijkstra", "Tous les sommets doivent tous être à sens unique ou non orienté");
                return false;
            }
        }

        setStepNumber(0);
        return true;
    }

    @Override
    public boolean ready() {
        if(sommetDepart == null){
            Main.warningDialog("Algorithme Dijkstra", "Il n'y a aucun sommet de départ");
            return false;
        }

        if(sommetArrive == null){
            Main.warningDialog("Algorithme Dijkstra", "Il n'y a aucun sommet d'arrivé");
            return false;
        }

        if(sommetArrive == sommetDepart){
            Main.warningDialog("Algorithme Dijkstra", "Le sommet de départ et d'arrivé ne peuvent pas être les mêmes");
            return false;
        }

        sommetDepart.setState(Var.STATE_CURRENT);

        sommetArrive.setState(Var.STATE_END);

        return true;
    }

    @Override
    public boolean nextStep() {
        if(stepNumber.get() == 0){
            if(!ready()){
                return false;
            }else{
                for(Sommet current : Main.graph.getSommets())
                    current.setInto("?");

                for(Sommet current : Main.graph.getCurrentSommets())
                    current.setInto("0");
            }
        }else{
            Sommet oldSommet = null;
            Sommet nextSommet = null;
            Arete nextArete = null;
            int min = Integer.MAX_VALUE;

            for(Sommet current : Main.graph.getCurrentSommets()){
                int value;
                if(current.getInfo().equals("?"))
                    value = Integer.MAX_VALUE;
                else
                    value = Integer.parseInt(current.getInfo());

                HashMap<Arete, Sommet> nexts = current.connections();
                for(Map.Entry<Arete, Sommet> entry : nexts.entrySet()){
                    if(entry.getKey().getState() != Var.STATE_NONE)
                        continue;

                    int targetValue;
                    if(entry.getValue().getInfo().equals("?"))
                        targetValue = Integer.MAX_VALUE;
                    else
                        targetValue = Integer.parseInt(entry.getValue().getInfo());

                    int tmpValue = value + entry.getKey().getPoids();
                    if(tmpValue < targetValue && tmpValue < min){
                        oldSommet = current;
                        nextArete = entry.getKey();
                        nextSommet = entry.getValue();
                        min = tmpValue;
                    }
                }
            }

            if(nextArete == null){
                Main.infoDialog("Algorithme Dijkstra", "L'algorithme est terminé");
                return false;
            }

            swapCurrentArete(nextArete);
            swapCurrentSommet(oldSommet, nextSommet);

            nextSommet.setInto(min+"");
        }

        incStepNumber();

        return true;
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

        if(nnew == sommetArrive)
            return;

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
    }

    private void swapCurrentArete(Arete nnew){
        if(currentArete != null)
            currentArete.setState(Var.STATE_VISITED);
        currentArete = nnew;
        currentArete.setState(Var.STATE_CURRENT);


    }

    @Override
    public boolean previousStep() {
        int stop = getStepNumber();

        if(stop == 1)
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
        sommetArrive.setState(Var.STATE_END);
        ready();
    }

    public void setSommetDepart(Sommet sommet){
        if(sommet != null && sommet == sommetArrive){
            Main.warningDialog("Algorithme Dijkstra", "Le sommet est déjà le sommet d'arrivé");
            return;
        }
        
        if(sommetDepart != null)
            sommetDepart.setState(Var.STATE_NONE);
        
        sommetDepart = sommet;
        
        if(sommetDepart != null)
            sommetDepart.setState(Var.STATE_CURRENT);
    }

    public void setSommetArrive(Sommet sommet){
        if(sommet != null && sommet == sommetDepart){
            Main.warningDialog("Algorithme Dijkstra", "Le sommet est déjà le sommet de départ");
            return;
        }

        if(sommetArrive != null)
            sommetArrive.setState(Var.STATE_NONE);

        sommetArrive = sommet;

        if(sommetArrive != null)
            sommetArrive.setState(Var.STATE_END);
    }
}
