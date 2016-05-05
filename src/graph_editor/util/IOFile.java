package graph_editor.util;


import graph_editor.Main;
import graph_editor.model.Arete;
import graph_editor.model.Graph;
import graph_editor.model.Sommet;
import graph_editor.view.shape.SommetView;

import java.io.*;

public class IOFile {

    private static String sommetToString(Sommet sommet) {
        String m = "";

        SommetView v = sommet.getController().getView();
        String name = (sommet.getName() != null)? sommet.getName() : "";
        String posx = (v.getLayoutX() + v.getTranslateX()) + "";
        String posy = (v.getLayoutY() + v.getTranslateY()) + "";

        m += Var.ATTRIBUT_NAME + Var.SEPARATOR_VALEUR + name;
        m += Var.SEPARATOR_ATTRIBUT;
        m += Var.ATTRIBUT_POS_X + Var.SEPARATOR_VALEUR + posx;
        m += Var.SEPARATOR_ATTRIBUT;
        m += Var.ATTRIBUT_POS_Y + Var.SEPARATOR_VALEUR + posy;

        return m;
    }

    private static String areteToString(Arete arete){
        String m = "";

        String poids = arete.getPoids() +"";
        String s1 = arete.getSommet1().getName();
        String s2 = arete.getSommet2().getName();
        String sens = arete.getSens().toString();

        m += Var.ATTRIBUT_POIDS + Var.SEPARATOR_VALEUR + poids;
        m += Var.SEPARATOR_ATTRIBUT;
        m += Var.ATTRIBUT_SENS + Var.SEPARATOR_VALEUR + sens;
        m += Var.SEPARATOR_ATTRIBUT;
        m += Var.ATTRIBUT_SOMMET_1 + Var.SEPARATOR_VALEUR + s1;
        m += Var.SEPARATOR_ATTRIBUT;
        m += Var.ATTRIBUT_SOMMET_2 + Var.SEPARATOR_VALEUR + s2;

        return m;
    }

    private static String graphToString(Graph graph){
        String m = "";

        for(Sommet current : graph.getSommets()){
            m += Var.OBJECT_SOMMET + Var.SEPARATOR_OBJECT + sommetToString(current) + "\n";
        }

        for(Arete current : graph.getAretes()){
            m += Var.OBJECT_ARETE + Var.SEPARATOR_OBJECT + areteToString(current) + "\n";
        }

        return m;
    }

    public static void saveGraphInFile(Graph graph, String filename){
        try {
            FileWriter fw = new FileWriter(filename);
            fw.write(graphToString(graph));
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void addSommetFromString(String m){
        String[] split = m.split(Var.SEPARATOR_ATTRIBUT);

        String name = null;
        double posx = -1, posy = -1;

        for(String current : split){
            String[] entry = current.split(Var.SEPARATOR_VALEUR);
            if(entry.length != 2)
                continue;
            if(entry[0].equals(Var.ATTRIBUT_NAME))
                name = entry[1];
            else if(entry[0].equals(Var.ATTRIBUT_POS_X))
                posx = Double.parseDouble(entry[1]);
            else if(entry[0].equals(Var.ATTRIBUT_POS_Y))
                posy = Double.parseDouble(entry[1]);
        }

        Sommet sommet = new Sommet(name);
        sommet.getController().addToGraph();
        sommet.getController().addToToile();
        sommet.setName(name);
        sommet.getController().getView().setLayoutX(posx);
        sommet.getController().getView().setLayoutY(posy);
    }

    private static void addAreteFromString(String m){
        String[] split = m.split(Var.SEPARATOR_ATTRIBUT);

        int poids = 0;
        Sommet s1 = null;
        Sommet s2 = null;
        Arete.Sens sens = Arete.Sens.NONE;

        for(String current : split){
            String[] entry = current.split(Var.SEPARATOR_VALEUR);
            if(entry.length != 2)
                continue;
            if(entry[0].equals(Var.ATTRIBUT_POIDS))
                poids = Integer.parseInt(entry[1]);
            else if(entry[0].equals(Var.ATTRIBUT_SENS)){
                if(entry[1].equals(Arete.Sens.NONE.toString()))
                    sens = Arete.Sens.NONE;
                else if(entry[1].equals(Arete.Sens.UNI.toString()))
                    sens = Arete.Sens.UNI;
                else if(entry[1].equals(Arete.Sens.BOTH.toString()))
                    sens = Arete.Sens.BOTH;
            }else if(entry[0].equals(Var.ATTRIBUT_SOMMET_1))
                s1 = Main.graph.getSommetByName(entry[1]);
            else if(entry[0].equals(Var.ATTRIBUT_SOMMET_2))
                s2 = Main.graph.getSommetByName(entry[1]);
        }

        Arete arete = new Arete(s1, s2, poids, sens);
        arete.getController().addToGraph();
        arete.getController().addToToile();
    }

    private static void decodeObject(String m){
        String[] split = m.split(Var.SEPARATOR_OBJECT);

        if(split.length != 2)
            return;

        if(split[0].equals(Var.OBJECT_SOMMET))
            addSommetFromString(split[1]);
        else if(split[0].equals(Var.OBJECT_ARETE))
            addAreteFromString(split[1]);
    }

    public static void readGraphFromFile(String filename){
        Main.graph.clear();

        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));

            String line;
            while((line = br.readLine()) != null){
                decodeObject(line);
            }

            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
