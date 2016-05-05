package graph_editor.model;


import graph_editor.Main;
import graph_editor.util.Var;

import java.util.*;

public class AlgoKruskal extends Algorithme{

    private Arete currentArete;

    private Set<Set<Sommet>> ensembles;

    private LinkedList<Arete> aretesOrdonnees;

    @Override
    public boolean graphValid() {
        for(Arete current : Main.graph.getAretes()){
            if(!current.getSens().equals(Arete.Sens.NONE)){
                Main.warningDialog("Algorithme Kruskal", "Toutes les arêtes doivent être non orientés");
                return false;
            }
        }

        setStepNumber(0);
        return true;
    }

    @Override
    public boolean ready() {
        ensembles = new HashSet<Set<Sommet>>();

        aretesOrdonnees = new LinkedList<Arete>();
        int min;
        Arete tmp = null;
        for(int i = 0; i < Main.graph.getAretes().size(); i++){
            min  = Integer.MAX_VALUE;
            for(Arete current : Main.graph.getAretes()){
                if(!aretesOrdonnees.contains(current) && current.getPoids() <= min){
                    min = current.getPoids();
                    tmp = current;
                }
            }

            aretesOrdonnees.addLast(tmp);
        }

        for(Sommet current : Main.graph.getSommets()){
            makeSet(current);
        }

        return true;
    }

    @Override
    public boolean nextStep() {
        if(stepNumber.get() == 0){
            if(!ready())
                return false;
        }

        Arete current = null;
        while(!aretesOrdonnees.isEmpty()){
            current = aretesOrdonnees.pollFirst();

            Set<Sommet> set1 = find(current.getSommet1());
            Set<Sommet> set2 = find(current.getSommet2());

            if(set1 == set2){
                current.setState(Var.STATE_END);
            }else{
                if(currentArete != null)
                    currentArete.setState(Var.STATE_VISITED);
                current.setState(Var.STATE_CURRENT);
                current.getSommet1().setState(Var.STATE_VISITED);
                current.getSommet2().setState(Var.STATE_VISITED);
                currentArete = current;

                union(set1, set2);
                break;
            }
        }

        if(aretesOrdonnees.isEmpty()){
            Main.infoDialog("Algorithme Kruskal", "L'algorithme est terminé");
            return false;
        }

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
        ready();
    }

    private Set<Sommet> find(Sommet sommet){
        for(Set<Sommet> set : ensembles){
            if(set.contains(sommet))
                return set;
        }
        return null;
    }

    private void union(Set<Sommet> set1, Set<Sommet> set2){
        ensembles.remove(set1);
        ensembles.remove(set2);

        set1.addAll(set2);

        ensembles.add(set1);
    }

    private void makeSet(Sommet sommet){
        HashSet<Sommet> tmp = new HashSet<Sommet>();
        tmp.add(sommet);

        ensembles.add(tmp);
    }
}
