package com.foodch;

import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.stage.Stage;
import javafx.scene.*;
import javafx.stage.WindowEvent;

public class MenuController {
    @FXML
    private ListView OutList;
    @FXML
    private Button startbutton;
    @FXML
    private Spinner<Integer> Preys;
    @FXML
    private VBox WorldParms;
    @FXML
    private Spinner<Integer> WorldX;
    @FXML
    private Spinner<Integer> WorldY;
    @FXML
    private CheckBox splitLimit;


    World w=null;
    static Stage st = null;
    static Boolean run = false;
    double deltaX,deltaY;
    String Ticks,PredPopulation,Food,PredEnergy;
    @FXML
    protected void onHelloButtonClick() {

        if(st==null) {
            run = true;
            StartSimulation();
        }
        else if(!st.isShowing()){
            st.show();
            run=true;
        }
        else{
            run=!run;
        }
        startbutton.setText((run)?"Pause":"Resume");
    }
    private void StartSimulation(){
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 2000, 1);
        Preys.setValueFactory(valueFactory);
        valueFactory.valueProperty().addListener((obs, oldValue, newValue) -> w.foodPerTick =newValue);

        startbutton.setText("Pause");
        Stage stage = new Stage();
        Group group = new Group();
        PerspectiveCamera cam = new PerspectiveCamera(true);

        double width = 800;
        double height = 600;
        stage.setWidth(width);
        stage.setHeight(height);

        int spaceHeight = -1;
        int spaceWidth = -1;
        try {
            spaceWidth = WorldX.getValue();
            spaceHeight = WorldY.getValue();
            if(spaceHeight < 1 || spaceWidth < 1) throw new NullPointerException();
        }
        catch (NullPointerException e){
            spaceWidth = 10000;
            spaceHeight = 10000;
        }
        WorldParms.setVisible(false);

        cam.translateZProperty().set(-20000);
        cam.setFarClip(500000);
        World world = new World(group, cam, spaceHeight, spaceWidth);
        this.w = world;
        this.w.splitLimit = splitLimit.isSelected();


        world.setOnMousePressed(event -> {
            deltaX = event.getSceneX();
            deltaY = event.getSceneY();
        });
        stage.addEventHandler(ScrollEvent.SCROLL, event -> {
            cam.translateZProperty().set(cam.getTranslateZ() - event.getDeltaY() * 20);
        });
        double delta = 200;
        stage.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            switch (event.getCode()) {
                case UP: {
                    cam.setLayoutY(cam.getLayoutY() - delta);
                    break;
                }
                case DOWN: {
                    cam.setLayoutY(cam.getLayoutY() + delta);
                    break;
                }
                case RIGHT: {
                    cam.setLayoutX(cam.getLayoutX() + delta);
                    break;
                }
                case LEFT: {
                    cam.setLayoutX(cam.getLayoutX() - delta);
                    break;
                }
            }
        });
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we)
            {
                startbutton.setText("Open");
            }
        });
        stage.setScene(world);
        stage.setTitle("World");
        stage.show();
        OutList.getItems().add(new String());
        OutList.getItems().add(new String());
        OutList.getItems().add(new String());
        OutList.getItems().add(new String());
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                if(run) {
                    world.tick();
                    double createn = 0;
                    for (Creature creature : world.creatures) {
                        createn += creature.energy;
                    }
                    OutList.getItems().set(0, String.format("Ticks: %d", world.ticks));
                    OutList.getItems().set(1, String.format("Creature population: %d", world.creatures.size()));
                    OutList.getItems().set(2, String.format("Food: %d", world.foods.size()));
                    OutList.getItems().set(3, String.format("Creatures energy: %f", createn));
                }
            }
        }.start();
        st = stage;
    }
}