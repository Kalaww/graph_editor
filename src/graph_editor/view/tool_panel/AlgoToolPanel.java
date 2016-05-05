package graph_editor.view.tool_panel;


import graph_editor.Main;
import graph_editor.model.AlgoDijkstra;
import graph_editor.model.AlgoKruskal;
import graph_editor.model.AlgoPrim;
import graph_editor.model.Sommet;
import graph_editor.util.Var;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class AlgoToolPanel extends ToolPanel{

    class Timer extends Thread{

        public static final int AVANT = 1,
            ARRIERE = 2;

        private int sens;
        private boolean alive;
        private long interval;

        public Timer(int sens){
            this.sens = sens;
            alive = true;
        }

        public void kill(){
            alive = false;
            Thread.currentThread().interrupt();
        }

        public void setInterval(long d){
            interval = d;
        }

        @Override
        public void run() {
            while(alive){
                if(sens == AVANT) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            alive = Main.algorithme.nextStep();
                        }
                    });
                }else if(sens == ARRIERE) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            alive = Main.algorithme.previousStep();
                        }
                    });
                }

                if(!alive)
                    break;

                try {
                    Thread.sleep(interval);
                } catch (InterruptedException e) {}
            }
        }
    }

    private HBox paneConfigAlgo;

    private Button buttonAvant;
    private Button buttonRetour;
    private Button buttonAvantAuto;
    private Button buttonStopAuto;
    private Button buttonRetourAuto;
    private Button buttonReset;

    private Slider sliderSpeed;

    private Label labelSliderValue;
    private Label labelStepNumber;

    private ComboBox<String> comboBoxAlgo;
    private ComboBox<Sommet> comboBoxDijkstraStart;
    private ComboBox<Sommet> comboBoxDijkstraEnd;
    private ComboBox<Sommet> comboBoxPrimStart;

    private Pane dijkstraGroup;
    private Pane primGroup;

    private IntegerProperty stepNumber;

    private Timer timer;

    public AlgoToolPanel(){
        super();

        dijkstraGroup = groupDijkstra();
        primGroup = groupPrim();

        stepNumber = new SimpleIntegerProperty();

        paneConfigAlgo = new HBox();

        getChildren().addAll(
                new Label("Algorithmes"),
                groupAlgos(),
                paneConfigAlgo,
                new Separator(),
                new Label("Etape n°"),
                groupStepNumber(),
                new Separator(),
                new Label("Contrôles manuels"),
                groupLecture(),
                new Separator(),
                new Label("Contrôles automatiques"),
                groupAuto()
        );

        sliderSpeed.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                labelSliderValue.setText(String.format("%.1f", newValue));
                if(timer != null)
                    timer.setInterval(newValue.longValue() * 1000);
            }
        });

        comboBoxAlgo.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(oldValue != null){
                    Main.algorithme.unbindStepNumber(stepNumber);
                    Main.algorithme.reset();
                    Main.toile.clearStates();
                    Main.algorithme = null;

                    if(oldValue.equals(Var.ALGO_DIJKSTRA))
                        paneConfigAlgo.getChildren().remove(dijkstraGroup);
                    else if(oldValue.equals(Var.ALGO_PRIM))
                        paneConfigAlgo.getChildren().remove(primGroup);
                }

                if(newValue.equals(Var.ALGO_DIJKSTRA)) {
                    Main.algorithme = new AlgoDijkstra();
                    Main.algorithme.bindStepNumber(stepNumber);

                    if(!Main.algorithme.graphValid()){
                        Main.algorithme.unbindStepNumber(stepNumber);
                        Main.algorithme = null;
                    }else{
                        paneConfigAlgo.getChildren().add(dijkstraGroup);
                    }
                }else if(newValue.equals(Var.ALGO_PRIM)){
                    Main.algorithme = new AlgoPrim();
                    Main.algorithme.bindStepNumber(stepNumber);

                    if(!Main.algorithme.graphValid()){
                        Main.algorithme.unbindStepNumber(stepNumber);
                        Main.algorithme = null;
                    }else{
                        paneConfigAlgo.getChildren().add(primGroup);
                    }
                }else if(newValue.equals(Var.ALGO_KRUSKAL)){
                    Main.algorithme = new AlgoKruskal();
                    Main.algorithme.bindStepNumber(stepNumber);

                    if(!Main.algorithme.graphValid()){
                        Main.algorithme.unbindStepNumber(stepNumber);
                        Main.algorithme = null;
                    }
                }
            }
        });

        comboBoxDijkstraStart.valueProperty().addListener(new ChangeListener<Sommet>() {
            @Override
            public void changed(ObservableValue<? extends Sommet> observable, Sommet oldValue, Sommet newValue) {
                if(newValue != null)
                    ((AlgoDijkstra) Main.algorithme).setSommetDepart(newValue);
            }
        });

        comboBoxDijkstraEnd.valueProperty().addListener(new ChangeListener<Sommet>() {
            @Override
            public void changed(ObservableValue<? extends Sommet> observable, Sommet oldValue, Sommet newValue) {
                if(newValue != null)
                    ((AlgoDijkstra) Main.algorithme).setSommetArrive(newValue);
            }
        });

        comboBoxPrimStart.valueProperty().addListener(new ChangeListener<Sommet>() {
            @Override
            public void changed(ObservableValue<? extends Sommet> observable, Sommet oldValue, Sommet newValue) {
                if(newValue != null)
                    ((AlgoPrim) Main.algorithme).setSommetDepart(newValue);
            }
        });

        buttonAvant.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Main.algorithme.nextStep();
            }
        });

        buttonRetour.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Main.algorithme.previousStep();
            }
        });

        stepNumber.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if(newValue.intValue() > 0){
                    dijkstraGroup.setDisable(true);
                }else if(newValue.intValue() == 0){
                    dijkstraGroup.setDisable(false);
                }

                labelStepNumber.setText(newValue+"");
            }
        });

        buttonReset.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(timer != null){
                    timer.kill();
                    timer = null;
                }
                Main.algorithme.reset();
            }
        });

        buttonAvantAuto.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(timer == null){
                    timer = new Timer(Timer.AVANT);
                    timer.setInterval((long) sliderSpeed.getValue() * 1000);
                    timer.start();
                }else{
                    timer.sens = Timer.AVANT;
                }
            }
        });

        buttonRetourAuto.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(timer == null){
                    timer = new Timer(Timer.ARRIERE);
                    timer.setInterval((long) sliderSpeed.getValue() * 1000);
                    timer.start();
                }else{
                    timer.sens = Timer.ARRIERE;
                }
            }
        });

        buttonStopAuto.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(timer != null){
                    timer.kill();
                    timer = null;
                }
            }
        });

    }

    private Pane groupAlgos(){
        VBox box = new VBox();
        box.setSpacing(10);

        comboBoxAlgo = new ComboBox<String>(FXCollections.observableArrayList(
                Var.ALGO_DIJKSTRA,
                Var.ALGO_PRIM,
                Var.ALGO_KRUSKAL
        ));

        box.getChildren().addAll(
                comboBoxAlgo
        );

        return box;
    }

    private Pane groupLecture(){
        VBox box = new VBox();
        box.setSpacing(10);
        box.setAlignment(Pos.CENTER);

        HBox boxArrow = new HBox();
        boxArrow.setSpacing(10);
        boxArrow.setAlignment(Pos.CENTER);

        buttonAvant = new Button(">");
        buttonRetour = new Button("<");
        buttonReset = new Button("Reset");

        boxArrow.getChildren().addAll(
                buttonRetour,
                buttonAvant
        );

        box.getChildren().addAll(
                boxArrow,
                buttonReset
        );

        return box;
    }

    private Pane groupStepNumber(){
        HBox box = new HBox();
        box.setSpacing(10);
        box.setAlignment(Pos.CENTER);

        labelStepNumber = new Label();

        box.getChildren().addAll(
                labelStepNumber
        );

        return box;
    }

    private Pane groupAuto(){
        VBox box = new VBox();
        box.setSpacing(10);

        HBox boxArrow = new HBox();
        boxArrow.setSpacing(10);
        boxArrow.setAlignment(Pos.CENTER);

        buttonAvantAuto = new Button(">>");
        buttonRetourAuto = new Button("<<");
        buttonStopAuto = new Button("Pause");

        sliderSpeed = new Slider(0.1, 10, 1);

        labelSliderValue = new Label(String.format("%.1f", sliderSpeed.getValue()));

        HBox boxSlider = new HBox();
        boxSlider.getChildren().addAll(
                sliderSpeed,
                labelSliderValue
        );

        boxArrow.getChildren().addAll(
                buttonRetourAuto,
                buttonStopAuto,
                buttonAvantAuto
        );

        box.getChildren().addAll(
                new Label("Vitesse"),
                boxSlider,
                boxArrow
        );

        return box;
    }

    private Pane groupDijkstra(){
        VBox box = new VBox();
        box.setSpacing(10);

        HBox boxStart = new HBox();
        HBox boxEnd = new HBox();

        boxStart.setSpacing(10);
        boxEnd.setSpacing(10);

        comboBoxDijkstraStart = new ComboBox<Sommet>(FXCollections.observableArrayList(Main.graph.getSommets()));
        comboBoxDijkstraEnd = new ComboBox<Sommet>(FXCollections.observableArrayList(Main.graph.getSommets()));

        boxStart.getChildren().addAll(
                new Label("Départ"),
                comboBoxDijkstraStart
        );

        boxEnd.getChildren().addAll(
                new Label("Arrivé"),
                comboBoxDijkstraEnd
        );

        box.getChildren().addAll(
                boxStart,
                boxEnd
        );

        return box;
    }

    private Pane groupPrim(){
        HBox box = new HBox();
        box.setSpacing(10);
        box.setAlignment(Pos.CENTER);

        comboBoxPrimStart = new ComboBox<Sommet>(FXCollections.observableArrayList(Main.graph.getSommets()));

        box.getChildren().addAll(
                new Label("Départ"),
                comboBoxPrimStart
        );

        return box;
    }


    /*
        STEP NUMBER PROPERTY
     */
    public final void setStepNumber(int i){
        stepNumber.setValue(i);
    }

    public final int getStepNumber(){
        return stepNumber.get();
    }

    public final IntegerProperty stepNumBerProperty(){
        return stepNumber;
    }
}
