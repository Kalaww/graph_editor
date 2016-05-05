package graph_editor.util;

import javafx.scene.paint.Color;

public class Var {


    // SELECTION TOOL BAR
    public static final int SELECTION_TOOLBAR_WIDTH = 200;


    // TOILE
    public static final int TOILE_GRID_INTERVAL = 100;


    // STYLE
    public static final String BORDER_BLACK_1 = "-fx-border-style: solid;" +
            "-fx-border-width: 1;" +
            "-fx-border-color: #dcdcdc";


    // SOMMET
    public static final Color SOMMET_DEFAULT_COLOR = Color.SKYBLUE;
    public static final Color SOMMET_COLOR_FOCUS = Color.BLUE;


    // RECTANGLE
    public static final int RECTANGLE_WIDTH = 50;
    public static final int RECTANGLE_HEIGHT = 50;


    // CIRCLE
    public static final int CIRCLE_RADIUS = RECTANGLE_WIDTH / 2;


    // ARROW
    public static final double ARROW_LENGHT = 10.0;


    // ARETE
    public static final double ARETE_WIDTH = 3.0;
    public static final Color ARETE_COLOR_FOCUS = Color.LIGHTGREEN;
    public static final Color ARETE_COLOR_DEFAULT = Color.LIGHTGRAY;

    // OPACITY
    public static final double OPACITY = 0.7;


    // CHOIX ALGORITHMES
    public static final String ALGO_DIJKSTRA = "Dijkstra";
    public static final String ALGO_PRIM = "Prim";
    public static final String ALGO_KRUSKAL = "Kruskal";


    // ALGORITHME
    public static final Color ALGO_COLOR_CURRENT = Color.LIME;
    public static final Color ALGO_COLOR_VISITED = Color.LIMEGREEN;
    public static final Color ALGO_COLOR_END = Color.RED;


    // STATE SOMMET ARETE
    public static final int STATE_NONE = 0;
    public static final int STATE_VISITED = 1;
    public static final int STATE_CURRENT = 2;
    public static final int STATE_END = 3;
    public static final int STATE_FOCUS = 4;


    // FILE ATTRIBUT NOM
    public static final String OBJECT_SOMMET = "sommet";
    public static final String OBJECT_ARETE = "arete";
    public static final String ATTRIBUT_NAME = "nom";
    public static final String ATTRIBUT_POIDS = "poids";
    public static final String ATTRIBUT_POS_X = "posx";
    public static final String ATTRIBUT_POS_Y = "posy";
    public static final String ATTRIBUT_SENS = "sens";
    public static final String ATTRIBUT_SOMMET_1 = "s1";
    public static final String ATTRIBUT_SOMMET_2 = "s2";


    // SEPARATOR
    public static final String SEPARATOR_ATTRIBUT = "!";
    public static final String SEPARATOR_VALEUR = "=";
    public static final String SEPARATOR_OBJECT = "#";
}
