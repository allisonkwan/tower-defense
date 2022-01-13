package game.graphics;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.dsl.FXGL;
import game.core.Launch;
import game.types.EnemyTypes;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import static com.almasb.fxgl.dsl.FXGLForKtKt.*;
import static javafx.beans.binding.Bindings.when;

public class CustomEndMenu extends FXGLMenu {

//    private BooleanProperty ended;
    private IntegerProperty ended;

    public CustomEndMenu() {
        super(MenuType.GAME_MENU);
        //addUINode(gameOverPopUp());
//        ended = new SimpleBooleanProperty(false);
        ended = new SimpleIntegerProperty(0);
        getContentRoot().getChildren().add(gameOverPopUp());
    }

    private JSONObject getEnemy(EnemyTypes type) {
        InputStream is = getClass().getClassLoader().getResourceAsStream(
                "assets/config/enemies.json"
        );
        Reader read = new InputStreamReader(is);
        try {
            JSONObject jsonObj = (JSONObject) new JSONParser().parse(read);
            return (JSONObject) jsonObj.get(type.name());
        } catch (Exception e) {
            System.out.println("Error message: " + e);
            return null;
        }
    }


    public void setEnd(boolean win) {
        // 0: pause
        // 1: lose
        // 2: win
        ended.set(win ? 2 : 1);
    }

    public void resetEnd() {
        ended.set(0);
    }

    public Node gameOverPopUp() {
        getContentRoot().setMinHeight(getAppHeight());
        getContentRoot().setMinWidth(getAppWidth());
        StackPane base = new StackPane();
        base.getStylesheets().add(getClass().getClassLoader()
                .getResource("assets/config/text.css").toExternalForm());
        base.getStyleClass().addAll("text", "bold");
        base.prefWidthProperty().bind(getContentRoot().minWidthProperty());
        base.prefHeightProperty().bind(getContentRoot().minHeightProperty());
        base.setAlignment(Pos.CENTER);

        VBox buttonCol = new VBox(20);
        Text status = new Text("Game Over.");
        status.textProperty().bind(when(ended.isEqualTo(0)).then("Game Paused").otherwise(when(ended.isEqualTo(1)).then("Game Lost").otherwise("Game Ended")));
        status.setFill(Color.WHITE);
        StackPane message = new StackPane();
        message.setMaxWidth(status.getBoundsInLocal().getWidth() * 3);
        message.setPadding(new Insets(10, 10, 10, 10));
        status.setId("bold");
        message.getChildren().add(status);
        message.backgroundProperty().bind(when(ended.isEqualTo(0)).then(new Background(
                new BackgroundFill(Color.LIGHTBLUE, new CornerRadii(15), Insets.EMPTY)))
                .otherwise(when(ended.isEqualTo(1)).then(new Background(
                        new BackgroundFill(Color.DARKRED, new CornerRadii(15), Insets.EMPTY)))
                        .otherwise(new Background(
                                new BackgroundFill(Color.DARKGREEN, new CornerRadii(15), Insets.EMPTY)))));

        VBox stats = new VBox(10);
        stats.setPadding(new Insets(10, 10, 10, 10));
        stats.setMaxWidth(getAppHeight() / 4);
        stats.setBorder(new Border(new BorderStroke(Color.DARKBLUE, BorderStrokeStyle.SOLID,
                new CornerRadii(15), BorderStroke.THICK)));
        stats.setAlignment(Pos.CENTER);
        Text killed = new Text("Enemies Killed:");
        killed.setId("bold");
        stats.getChildren().add(killed);
        for (EnemyTypes type : EnemyTypes.values()) {
            Image im = FXGL.image((String) getEnemy(type).get("sprite"));
            ImageView icon = new ImageView(im);
            icon.setFitWidth(90);
            icon.setPreserveRatio(true);

            Text t = new Text(String.format("%s: ", type.toString()));
            t.setFill(Color.DIMGREY);
            t.setId("bold");

            Region spacer = new Region();
            spacer.setMinWidth(30);

            Text count = new Text();
            count.setFill(Color.GREY);
            count.textProperty().bind(Launch.getStats().get(type.toString()).asString());

            HBox h = new HBox(icon, t, spacer, count);
            //h.setAlignment(Pos.CENTER);
            HBox.setHgrow(spacer, Priority.ALWAYS);
            stats.getChildren().add(h);
        }
        Text t = new Text("Waves Completed: ");
        t.setId("bold");
        HBox h = new HBox(t);
        h.setAlignment(Pos.CENTER);
        Text count = new Text();
        count.textProperty().bind(Launch.getStats().get("waves").asString());
        h.getChildren().add(count);
        stats.getChildren().add(h);

        Button restartGame = getUIFactoryService().newButton("Restart Game");
        restartGame.setOnMouseClicked(e -> fireExitToMainMenu());
        Button closeGame = getUIFactoryService().newButton("Exit Game");
        closeGame.setOnMouseClicked(e -> {
            fireExit();
        });


        buttonCol.getChildren().addAll(message, stats, restartGame, closeGame);
        buttonCol.setAlignment(Pos.CENTER);
        base.getChildren().addAll(buttonCol);
        base.setStyle("-fx-background-color: rgba(255, 255, 255, 0.9)");
        return base;
    }

    @NotNull
    @Override
    protected Button createActionButton(
            @NotNull StringBinding stringBinding, @NotNull Runnable runnable) {
        return new Button();
    }

    @NotNull
    @Override
    protected Button createActionButton(@NotNull String s, @NotNull Runnable runnable) {
        return new Button();
    }

    @NotNull
    @Override
    protected Node createBackground(double v, double v1) {
        return new StackPane();
    }

    @NotNull
    @Override
    protected Node createProfileView(@NotNull String s) {
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
}
