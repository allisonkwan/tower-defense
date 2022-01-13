package game.graphics;

import com.almasb.fxgl.audio.Sound;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import game.components.TowerComponent;
import game.core.Difficulty;
import game.core.Launch;
import game.types.TowerDefenceType;
import game.core.WaveSpawner;
import javafx.beans.binding.*;
import javafx.beans.property.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;

import static com.almasb.fxgl.dsl.FXGL.getAudioPlayer;
import static com.almasb.fxgl.dsl.FXGLForKtKt.*;
import static javafx.beans.binding.Bindings.when;

public class GameUI {
    private static VBox choiceList;
    private static Pane base;
    private static Node currTowerBase;
    private static TowerDefenceType towerToBuy;
    private static Long towerBeingBoughtCost;
    private static Text descriptionText;

    private static boolean menuVisible = true;
    private static boolean waveSpawned = false;

    private static Button spawnEnemiesBtn;

    private static Sound monsters = FXGL.getAssetLoader().loadSound("monsters.wav");

    public static HBox createUI() {
        // VBox to hold game data
        VBox vbox = new VBox();
        //vbox.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
        int health = FXGL.getip("health").get();
        Rectangle healthbarBackground = new Rectangle(200, 20, Color.GRAY);
        healthbarBackground.setArcWidth(20);
        healthbarBackground.setArcHeight(20);
        Rectangle healthbar = new Rectangle(200, 20, Color.RED);
        // Vary width of health bar based on health, assuming health value is between 0 and 100.
        Difficulty diff = (Difficulty) FXGL.geto("Difficulty");
        DoubleProperty orgHealth;
        if (diff == Difficulty.EASY) {
            orgHealth = new SimpleDoubleProperty(200.0);
        } else if (diff == Difficulty.MEDIUM) {
            orgHealth = new SimpleDoubleProperty(133.0);
        } else {
            orgHealth = new SimpleDoubleProperty(115.0);
        }
        IntegerProperty currHealth = FXGL.getip("health");
        DoubleBinding dp = (DoubleBinding) currHealth.divide(orgHealth).multiply( 200);
        IntegerProperty c = new SimpleIntegerProperty();
        c.bind(Bindings.createIntegerBinding(
                () -> Integer.valueOf((int) Math.round(100 * currHealth.get()/orgHealth.get())),
                currHealth, orgHealth));
        healthbar.widthProperty().bind(dp.asObject());
        healthbar.setArcWidth(20);
        healthbar.setArcHeight(20);
        StackPane healthSection = new StackPane();
        Text healthVal = new Text();
        healthVal.textProperty().bind(c.asString());

        healthSection.getChildren().addAll(healthbarBackground, healthbar, healthVal);
        StackPane.setAlignment(healthbar, Pos.CENTER_LEFT);
        vbox.getChildren().addAll(healthSection);
        vbox.setSpacing(10);

        // VBox to display player info (variables from customMainMenu.java)
        VBox playerInfo = new VBox();
        Text name = new Text("Name: " + FXGL.gets("Name"));
        Text balance = new Text("" + FXGL.getip("balance").get());
        HBox balanceH = new HBox();
        balance.textProperty().bind(FXGL.getip("balance").asString());
        Text balanceT = new Text("Balance: ");
        balanceH.getChildren().addAll(balanceT, balance);

        // StackPane to display tower information upon selection from scrollPane
        base = new Pane();

        //vbox to hold start enemy spawn button
        VBox enemyBtnVbox = new VBox();
        spawnEnemiesBtn = new Button("Start Combat");
        //spawnEnemiesBtn.setOnMouseClicked(e -> spawn("Enemy"));
        if (!waveSpawned) {
            WaveSpawner ws = new WaveSpawner(diff);
            spawnEnemiesBtn.disableProperty().bind(WaveSpawner.activeProperty());
            spawnEnemiesBtn.setOnMouseClicked(e -> {
                try {
                    waveSpawned = true;
                    FXGL.play("mouseClick.wav");
                    FXGL.play("startUp.wav");
                    getAudioPlayer().playSound(monsters);
                    int waveNumber = FXGL.geti("wave");
                    System.out.printf("Wave number: %d\n", waveNumber);
                    ws.waveSpawn(waveNumber);
                    FXGL.set("wave", waveNumber + 1);
                } catch (Exception outOfBounds) {
                    FXGL.getNotificationService().pushNotification("No more waves left!");
                }
            });
        } else {
            spawnEnemiesBtn.disableProperty().bind(new SimpleBooleanProperty(true));
        }
        WaveSpawner ws = new WaveSpawner(diff);
        spawnEnemiesBtn.disableProperty().bind(WaveSpawner.activeProperty());
        spawnEnemiesBtn.setOnMouseClicked(e -> {
            try {
                waveSpawned = true;
                FXGL.play("mouseClick.wav");
                FXGL.play("startUp.wav");
                getAudioPlayer().playSound(monsters);
                int waveNumber = FXGL.geti("wave");
                System.out.printf("Wave number: %d\n", waveNumber);
                ws.waveSpawn(waveNumber);
                FXGL.set("wave", waveNumber + 1);
                waveSpawned = false;
            } catch (Exception outOfBounds) {
                FXGL.getNotificationService().pushNotification("No more waves left!");
            }
        });
        enemyBtnVbox.getChildren().add(spawnEnemiesBtn);

        playerInfo.getChildren().addAll(name, balanceH);
        playerInfo.setSpacing(10);
        vbox.getChildren().addAll(playerInfo);

        // HBox to hold UI for towers and game data such as monument health
        HBox hbox = new HBox();
        hbox.getChildren().addAll(base, vbox, enemyBtnVbox);
        hbox.setSpacing(25);
        hbox.setLayoutX(880);
        hbox.setLayoutY(668);
        hbox.setBackground(new Background(new BackgroundFill(Color.rgb(200, 200, 200, 0.5),
                null, null)));
        hbox.setPrefSize(400, 100);

        return hbox;
    }

    public static void createUIToggleBtn() {
        if (menuVisible) {
            FXGL.getGameScene().clearUINodes();
            menuVisible = false;
        } else {
            FXGL.addUINode(createUI());
            menuVisible = true;
        }
    }

    public static Node createUpgradePopUp(Entity tower) {
        VBox textHolder = new VBox();
        textHolder.setAlignment(Pos.CENTER);
        descriptionText = new Text("Upgrade Info");
        //pulls from JSON
        int upgradeCost = ((Long) tower.getComponent(TowerComponent.class).getTowerDescription().get("upgrade_cost")).intValue();
        int upgradeDamageInc = ((Long) tower.getComponent(TowerComponent.class).getTowerDescription().get("upgrade_damage")).intValue();

        descriptionText.setText("Upgrade basic tower damage: +" + upgradeDamageInc + " damage\nCost: " + upgradeCost);

        descriptionText.setFont(Font.font(20));
        textHolder.getChildren().add(descriptionText);

        HBox buttonRow = new HBox(10);
        VBox stack = new VBox(20, buttonRow, textHolder);
        StackPane b = new StackPane(stack);
        b.setMargin(stack, new Insets(10, 10, 10, 10));
        b.setMaxWidth(getAppWidth() / 4);
        b.setMaxHeight(getAppHeight() / 4);
        b.setBorder(new Border(new BorderStroke(Color.DARKBLUE, BorderStrokeStyle.SOLID,
                new CornerRadii(15), BorderStroke.THICK)));

        StackPane cover = new StackPane(b);
        cover.setPrefWidth(getAppWidth());
        cover.setPrefHeight(getAppHeight());
        cover.setAlignment(Pos.CENTER);
        cover.setStyle("-fx-background-color: rgba(255, 255, 255, 0.9)");


        Button upgrade = getUIFactoryService().newButton("Upgrade");
        upgrade.setMinWidth(200);
        upgrade.setStyle("-fx-background-color: rgba(144, 238, 144, 0.9); "
                + "-fx-text-fill: darkgreen");
        upgrade.styleProperty().bind(when(upgrade.hoverProperty())
                .then("-fx-background-color: darkgreen; -fx-text-fill: white")
                .otherwise("-fx-background-color: lightgreen; -fx-text-fill: darkgreen"));

        upgrade.setOnMouseClicked(e -> {
            FXGL.play("mouseClick.wav");
            if (FXGL.geti("balance") - upgradeCost >= 0) {
                FXGL.set("balance", FXGL.geti("balance") - upgradeCost);
                upgradeTower(tower); //method that does actual upgrading logic
                removeUINode(cover);
            } else {
                FXGL.getNotificationService().pushNotification("Balance Insufficient!");
            }
        });
        Button delete = getUIFactoryService().newButton("Delete");
        delete.styleProperty().bind(when(delete.hoverProperty())
                .then("-fx-background-color: darkred; -fx-text-fill: white")
                .otherwise("-fx-background-color: indianred; -fx-text-fill: darkred"));
        delete.setMinWidth(200);
        delete.setOnMouseClicked(e -> {
            FXGL.play("mouseClick.wav");
            tower.removeFromWorld();
            FXGL.set("balance", FXGL.geti("balance") +
                    ((Integer) tower.getComponent(TowerComponent.class).getTowerDescription().get("invested")));
            removeUINode(cover);
        });


        Button cancel = getUIFactoryService().newButton("Cancel");
        cancel.setOnMouseClicked(e -> {
            removeUINode(cover);
            FXGL.play("mouseClick.wav");});
        cancel.setMinWidth(200);
        buttonRow.getChildren().addAll(delete, upgrade, cancel);
        addUINode(cover);
        return base;
    }

    public static void upgradeTower(Entity tower) {
        tower.getComponent(TowerComponent.class).colorDarker();
        tower.getComponent(TowerComponent.class).updateField("damage", "upgrade_damage");
        tower.getComponent(TowerComponent.class).updateField("invested", "upgrade_cost");
    }

    public static Node createBuyPopUp() {
        Pane wrapper = new Pane();
        choiceList = new VBox();
        choiceList.setSpacing(10);
        towerToBuy = null;
        for (TowerDefenceType key : Launch.getTowerMap().keySet()) {
            Button towerBtn = new Button(key.name());
            towerBtn.setMinSize(Button.USE_PREF_SIZE, Button.USE_PREF_SIZE);
            towerBtn.setOnAction(e -> {
                FXGL.play("mouseClick.wav");
                wrapper.getChildren().clear();
                wrapper.getChildren().add(generateTowerInfo(key));
                towerToBuy = key;
                towerBeingBoughtCost = (Long) Launch.getTowerMap().get(key).get("cost");
            });
            choiceList.getChildren().add(towerBtn);
        }
        Region spacer = new Region();
        HBox menu = new HBox(10, wrapper, spacer, choiceList);
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox buttonRow = new HBox(10);
        VBox stack = new VBox(20, menu, buttonRow);
        StackPane b = new StackPane(stack);
        b.setMargin(stack, new Insets(10, 10, 10, 10));
        //b.maxHeightProperty().bind(stack.heightProperty());
        //b.maxWidthProperty().bind(buttonRow.widthProperty());
        b.setMaxWidth(getAppWidth() / 4);
        b.setMaxHeight(getAppHeight() / 4);
        b.setBorder(new Border(new BorderStroke(Color.DARKBLUE, BorderStrokeStyle.SOLID,
                new CornerRadii(15), BorderStroke.THICK)));

        StackPane cover = new StackPane(b);
        cover.setPrefWidth(getAppWidth());
        cover.setPrefHeight(getAppHeight());
        cover.setAlignment(Pos.CENTER);
        cover.setStyle("-fx-background-color: rgba(255, 255, 255, 0.9)");


        Button purchase = getUIFactoryService().newButton("Buy");
        purchase.setStyle("-fx-background-color: rgba(144, 238, 144, 0.9); "
                + "-fx-text-fill: darkgreen");
        purchase.styleProperty().bind(when(purchase.hoverProperty())
                .then("-fx-background-color: darkgreen; -fx-text-fill: white")
                .otherwise("-fx-background-color: lightgreen; -fx-text-fill: darkgreen"));
        purchase.setOnMouseClicked(e -> {
            FXGL.play("mouseClick.wav");
            if (towerToBuy != null && FXGL.geti("balance") - towerBeingBoughtCost >= 0) {
                FXGL.set("balance", FXGL.geti("balance") - towerBeingBoughtCost.intValue());
                Entity spawned = placeTower(towerToBuy);
                spawned.getComponent(TowerComponent.class).getTowerDescription().put("invested", towerBeingBoughtCost.intValue());
                removeUINode(cover);
                FXGL.play("towerPlaced.wav");
            } else if (towerToBuy == null) {
                FXGL.getNotificationService().pushNotification("Select a tower!");
            } else {
                FXGL.getNotificationService().pushNotification("Balance Insufficient!");
            }
        });
        purchase.setMinWidth(200);
//        Button delete = getUIFactoryService().newButton("Delete");
//        delete.styleProperty().bind(when(delete.hoverProperty())
//                .then("-fx-background-color: darkred; -fx-text-fill: white")
//                .otherwise("-fx-background-color: indianred; -fx-text-fill: darkred"));
//        delete.setMinWidth(200);
        HBox towerOps = new HBox(purchase);
        Button cancel = getUIFactoryService().newButton("Cancel");
        cancel.setOnMouseClicked(e -> {
            removeUINode(cover);
            FXGL.play("mouseClick.wav");
        });
        cancel.setMinWidth(200);
        buttonRow.getChildren().addAll(towerOps, cancel);
        addUINode(cover);
        return base;
    }

    private static HBox generateTowerInfo(TowerDefenceType type) {

        Label towerName = new Label(type.name());
        Label cost = new Label("Cost: " + (Long) Launch.getTowerMap().get(type).get("cost"));
        Label range = new Label("Range: " + (Long) Launch.getTowerMap().get(type).get("range"));
        Label fireRate = new Label("Fire Rate: "
                + (Long) Launch.getTowerMap().get(type).get("fire_rate"));
        Label description = new Label((String) Launch.getTowerMap().get(type).get("description"));
        description.setFont(Font.font("comic sans ms regular", FontPosture.ITALIC, 20));
        description.setWrapText(true);
        HBox.setHgrow(description, Priority.NEVER);
        VBox list = new VBox(2, towerName, cost, range, fireRate);
        HBox info = new HBox(20, list, description);
        list.getChildren().forEach(node -> {
            Label l = (Label) node;
            l.setFont(new Font("comic sans ms regular", 20));
        });
        return info;
    }

    public static void setCurrTowerBase(Node node) {
        currTowerBase = node;
    }

    private static Entity placeTower(TowerDefenceType type) {
        if (type == null) {
            FXGL.getNotificationService().pushNotification("Select a tower to buy.");
            return null;
        }
        Color color = Color.BLACK;
        switch (type) {
        case BASIC:
            color = Color.DARKGREY;
            break;
        case SPRAY:
            color = Color.RED;
            break;
        case FREEZE:
            color = Color.LIGHTBLUE;
            break;
        default:
            throw new IllegalStateException("Unexpected value: " + type);
        }
        Entity spawned = spawn(type.name(), new SpawnData(
                currTowerBase.localToScene(currTowerBase.getBoundsInLocal()).getMinX(),
                        currTowerBase.localToScene(currTowerBase.getBoundsInLocal()).getMinY())
                        .put("color", color)
        );
        towerToBuy = null;
        return spawned;
    }
}
