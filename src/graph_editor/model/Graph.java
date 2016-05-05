package graph_editor.model;

import graph_editor.util.Var;

import java.util.ArrayList;

public class Graph {

    private static String NAME_CPT = "A";

    private ArrayList<Sommet> sommets;

    private ArrayList<Arete> aretes;

    public Graph(){
        sommets = new ArrayList<Sommet>();
        aretes = new ArrayList<Arete>();

    }

    public void addSommet(Sommet sommet){
        sommets.add(sommet);
    }

    public void removeSommet(Sommet sommet){
        if(sommets.contains(sommet)){
            sommets.remove(sommet);

            for(int i = aretes.size() -1; i >= 0; i--){
                Arete current = aretes.get(i);
                if(current.hasSommet(sommet)){
                    removeArete(current);
                }
            }

            sommet.remove();
        }
    }

    public void addArete(Arete arete){
        aretes.add(arete);
    }

    public void removeArete(Arete arete){
        if(aretes.contains(arete)){
            aretes.remove(arete);

            arete.remove();
        }
    }

    public void clear(){
        for(int i = aretes.size() -1; i >= 0; i--){
            removeArete(aretes.get(i));
        }

        for(int i = sommets.size() -1; i >= 0; i--){
            removeSommet(sommets.get(i));
        }
    }

    public Sommet getSommetByName(String name){
        for(Sommet current : sommets){
            if(current.getName().equals(name))
                return current;
        }
        return null;
    }

    public boolean isNameAvailable(String name){
        for(Sommet current : sommets){
            if(current.getName().equals(name))
                return false;
        }
        return true;
    }

    public String getAvailableName(){
        boolean available = false;

        while(!available){
            available = true;

            for(Sommet current : sommets){
                if(current.getName().equals(NAME_CPT))
                    available = false;
            }

            if(!available){
                if(NAME_CPT.endsWith("Z")) {
                    String tmp = "";
                    for(int i = 0; i < NAME_CPT.length(); i++)
                        tmp += "A";
                    tmp += "A";
                    NAME_CPT = tmp;
                }else{
                    char end = NAME_CPT.charAt(NAME_CPT.length()-1);
                    end++;
                    NAME_CPT = NAME_CPT.substring(0, NAME_CPT.length()-1) + end;
                }
            }
        }

        return NAME_CPT;
    }

    public ArrayList<Sommet> getVisitedSommets(){
        ArrayList<Sommet> tmp = new ArrayList<Sommet>();
        for(Sommet current : sommets){
            if(current.getState() == Var.STATE_VISITED)
                tmp.add(current);
        }
        return tmp;
    }

    public ArrayList<Sommet> getCurrentSommets(){
        ArrayList<Sommet> tmp = new ArrayList<Sommet>();
        for(Sommet current : sommets){
            if(current.getState() == Var.STATE_CURRENT)
                tmp.add(current);
        }
        return tmp;
    }

    public ArrayList<Sommet> getSommets(){
        return sommets;
    }

    public ArrayList<Arete> getAretes(){
        return aretes;
    }
}
