package game.graphics;


import com.almasb.fxgl.animation.Interpolators;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.audio.Sound;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import game.core.Difficulty;
import javafx.beans.binding.StringBinding;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import org.jetbrains.annotations.NotNull;
import java.util.*;

import static com.almasb.fxgl.dsl.FXGL.*;
import static com.almasb.fxgl.dsl.FXGL.animationBuilder;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getUIFactoryService;
import static javafx.beans.binding.Bindings.*;

public class CustomMainMenu extends FXGLMenu {

    private List<Node> diffButtons = new ArrayList<>();

    private static Map<String, Object> vars = new HashMap<>();
    private int animIndex = 0;

    private UserAction enterCallback;

    private Button active;

    private Boolean clicked = false;

    private Sound ls = FXGL.getAssetLoader().loadSound("ls.wav");
    public CustomMainMenu() {
        super(MenuType.MAIN_MENU);
        var optionsBody = createBody();
        Node start = createStartBody(optionsBody);
        getContentRoot().setPrefHeight(getAppHeight());
        getContentRoot().setPrefWidth(getAppWidth());
        Input input = getInput();
        input.addAction(enterCallback, KeyCode.ENTER);
        switchScreen(start);

    }

    private void switchScreen(Node next) {
        getContentRoot().getChildren().clear();
        getContentRoot().getChildren().add(next);
    }

    protected Node createStartBody(Node next) {
        StackPane p = new StackPane(new Label("Click ENTER to start"));
        p.prefWidthProperty().bind(getContentRoot().prefWidthProperty());
        p.prefHeightProperty().bind(getContentRoot().prefHeightProperty());
        enterCallback = new UserAction("Enter") {
            @Override
            protected void onAction() {
                FXGL.play("mouseClick.wav");
                getAudioPlayer().playSound(ls);
                switchScreen(next);
            }
        };
        return p;
    }

    protected Node createProfileView(String s) {
        return new StackPane();
    }

    private Node createBody() {
        StackPane base = new StackPane();
        base.prefWidthProperty().bind(getContentRoot().prefWidthProperty());
        base.prefHeightProperty().bind(getContentRoot().prefHeightProperty());

        Button start = createNormalButton("Start", 0, 0);
        Button difficulty = createNormalButton("Difficulty", 0, 0);

        Label l = new Label("Enter Name:");
        TextField name = new TextField();
        name.setMaxWidth(200);
        EventHandler<ActionEvent> startHandle = actionEvent -> {
            if (name.getText().trim().length() == 0) {
                getDialogService().showMessageBox("Use a real name!");
            } else {
                FXGL.play("mouseClick.wav");
                getAudioPlayer().stopSound(ls);
                vars.put("Difficulty", Difficulty.valueOf(active.getText().toUpperCase()));
                vars.put("Name", name.getText());
                fireNewGame();
            }
        };
        name.setOnAction(startHandle);
        start.setOnAction(startHandle);
        enterCallback = new UserAction("Enter") {
            @Override
            protected void onAction() {
                startHandle.handle(new ActionEvent());
            }
        };

        Button easy = createNormalButton("Easy", 0, 0);
        easy.setOnMouseClicked(e -> set("Difficulty", Difficulty.EASY));
        Button medium = createNormalButton("Medium", 0, 0);
        medium.setOnMouseClicked(e -> set("Difficulty", Difficulty.MEDIUM));
        Button hard = createNormalButton("Hard", 0, 0);
        hard.setOnMouseClicked(e -> set("Difficulty", Difficulty.HARD));
        active = medium;

        HBox diff = new HBox(10, easy, medium, hard);
        diff.setAlignment(Pos.CENTER);
        diff.setVisible(false);
        difficulty.setOnMouseClicked(e -> {
            FXGL.play("mouseClick.wav");
            if (!clicked) {
                diff.setVisible(true);
                animIndex = 0;
                diffButtons.forEach(btn -> {
                    btn.setOpacity(0);

                    animationBuilder(this)
                            .delay(Duration.seconds(animIndex * 0.1))
                            .interpolator(Interpolators.BACK.EASE_OUT())
                            .translate(btn)
                            .from(new Point2D(-20, 0))
                            .to(new Point2D(0, 0))
                            .buildAndPlay();

                    animationBuilder(this)
                            .delay(Duration.seconds(animIndex * 0.1))
                            .fadeIn(btn)
                            .buildAndPlay();

                    animIndex++;
                });
                clicked = true;
            } else {
                diff.setVisible(false);
                clicked = false;
            }
        });

        for (Node n : diff.getChildren()) {
            diffButtons.add(n);
            Button b = (Button) n;
            if (n.equals(active)) {
                n.setStyle("-fx-background-color: lightblue");
            } else {
                n.setStyle("-fx-background-color: lightgrey");
            }
            n.setOnMouseClicked(e -> {
                FXGL.play("mouseClick.wav");
                active.setStyle("-fx-background-color: lightgrey");
                active = b;
                active.setStyle("-fx-background-color: lightblue");
            });
            n.setOnMouseEntered(e -> n.setStyle("-fx-background-color: lightgreen"));
            n.setOnMouseEntered(e -> {
                if (n.equals(active)) {
                    n.setStyle("-fx-background-color: lightblue");
                } else {
                    n.setStyle("-fx-background-color: lightgrey");
                }
            });
        }
        VBox options = new VBox(l, name, start, difficulty, diff);
        options.setSpacing(10);
        options.setAlignment(Pos.CENTER);
        base.getChildren().addAll(options);
        return base;
    }

    private Button createNormalButton(String name, double x, double y) {
        Button b = getUIFactoryService().newButton(name);
        b.setPrefHeight(50);
        b.setPrefWidth(200);
        b.setLayoutX(x);
        b.setLayoutY(y);
        return b;
    }

    private StackPane createButton(StringBinding name, Difficulty d) {
        var bg = new Rectangle(200, 50);
        bg.setEffect(new BoxBlur());

        var text = getUIFactoryService().newText(name);
        text.setTranslateX(15);
        text.setFill(Color.BLACK);

        StackPane btn = new StackPane(bg, text);
        btn.setPrefHeight(bg.getHeight());
        btn.setPrefWidth(bg.getWidth());

        bg.fillProperty().bind(when(btn.hoverProperty())
                .then(Color.LIGHTGREEN)
                .otherwise(Color.DARKGRAY)
        );

        btn.setAlignment(Pos.CENTER);

        Rectangle clip = new Rectangle(200, 50);
        clip.translateXProperty().bind(btn.translateXProperty().negate());

        btn.setTranslateX(-200);
        btn.setClip(clip);
        btn.setCache(true);
        btn.setCacheHint(CacheHint.SPEED);

        return btn;
    }

    @Override
    protected Button createActionButton(@NotNull StringBinding name, @NotNull Runnable action) {
        return new Button();
    }

    @Override
    public void onCreate() {

    }

    @NotNull
    @Override
    protected Button createActionButton(@NotNull String s, @NotNull Runnable runnable) {
        return new Button(s);
    }

    @NotNull
    @Override
    protected Node createBackground(double v, double v1) {
        return new StackPane();
    }

    @NotNull
    @Override
    protected Node createTitleView(@NotNull String s) {
        return new StackPane();
    }

    @NotNull
    @Override
    protected Node createVersionView(@NotNull String s) {
        return new StackPane();
    }

    public List<Node> getDiffButtons() {
        return diffButtons;
    }

    public static Map<String, Object> getVars() {
        return vars;
    }

    public static void setVars(String str, Object obj) {
        vars = new HashMap<>();
        vars.put(str, obj);
    }

    public UserAction getEnterCallback() {
        return enterCallback;
    }

    public Button getActive() {
        return active;
    }
}
