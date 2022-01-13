package game.core;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.almasb.fxgl.audio.Sound;
import com.almasb.fxgl.dsl.FXGL;
import game.collision.ProjectileHandler;
import game.event.EnemyKilledEvent;
import game.graphics.*;
import game.types.EnemyTypes;
import game.types.ProjectileType;
import game.types.TowerDefenceType;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Point2D;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;

import static com.almasb.fxgl.dsl.FXGLForKtKt.*;

public class Launch extends GameApplication {
    private static Map<TowerDefenceType, JSONObject> towerMap = new HashMap<>();
    private static CustomEndMenu endMenu;
    private static Map<String, SimpleIntegerProperty> stats = new HashMap<>();
    private int levelEnemies = 10;
    private List<Point2D> waypoints;
    private List<Point2D> bigDemonWaypoints;
    private List<Point2D> bossWaypoints;

    @Override
    protected void initSettings(GameSettings settings) {

        // This 1280x768 works for the 40x24 tile maps we are using for game levels,
        // Each tile is 32 x 32
        settings.setWidth(1280); // 1280px
        settings.setHeight(768); // 768px
        settings.setIntroEnabled(false);
        settings.setVersion("1.1");
        settings.setMainMenuEnabled(true);
        settings.setGameMenuEnabled(true);
        settings.setTitle("TDG");
        /*
        settings.removeEngineService(NotificationServiceProvider.class);
        settings.addEngineService(CustomNotificationService.class);
        settings.setNotificationViewClass(CustomNotificationView.class);
        */
        for (EnemyTypes type : EnemyTypes.values()) {
            stats.put(type.name(), new SimpleIntegerProperty(0));
        }
        stats.put("waves", new SimpleIntegerProperty(0));
        settings.setSceneFactory(new SceneFactory() {
            @Override
            public FXGLMenu newMainMenu() {
                return new CustomMainMenu();
            }

            @Override
            public FXGLMenu newGameMenu() {
                endMenu = new CustomEndMenu();
                return endMenu;
            }
        });
    }

    public static Map<TowerDefenceType, JSONObject> checkDif(Difficulty diff) {
        InputStream is = Launch.class.getClassLoader()
                .getResourceAsStream("assets/config/towers.json");
        Reader fr = new InputStreamReader(is);
        JSONObject file;
        try {
            file = (JSONObject) new JSONParser().parse(fr);
        } catch (Exception e) {
            getGameController().exit();
            return null;
        }
        JSONObject diffSettings = (JSONObject) file.get(diff.name());
        long diffCost = (long) diffSettings.get("diff_cost");
        System.out.println(diffCost);
        Map<TowerDefenceType, JSONObject> tMap = new HashMap<>();
        for (TowerDefenceType type : TowerDefenceType.values()) {
            JSONObject towerSetting = (JSONObject) file.get(type.name());
            towerSetting.put("cost", (Long) towerSetting.get("cost") * diffCost);
            tMap.put(type, towerSetting);
        }
        return tMap;
    }

    @Override
    protected void initGame() {
        FXGL.getGameWorld().addEntityFactory(new TowerDefenceFactory());
        Launch.getEndMenu().resetEnd();
        FXGL.setLevelFromMap("tdMap.tmx");
        towerMap = checkDif((Difficulty) CustomMainMenu.getVars().get("Difficulty"));
        //FXGL.getEventBus().addEventHandler(EnemyKilledEvent.ANY, this::onEnemyKilled);
        //FXGL.getEventBus().addEventHandler(EnemyReachedGoalEvent.ANY, e -> ());

        waypoints.addAll(Arrays.asList(
                new Point2D(512, 608),
                new Point2D(512, 688),
                new Point2D(1184, 688),
                new Point2D(1184, 80),
                new Point2D(864, 80),
                new Point2D(864, 384),
                new Point2D(208, 384),
                new Point2D(208, 96)
                )
        );
        bigDemonWaypoints.addAll(Arrays.asList(
                new Point2D(505, 595),
                new Point2D(505, 675),
                new Point2D(1177, 675),
                new Point2D(1177, 65),
                new Point2D(857, 65),
                new Point2D(857, 370),
                new Point2D(201, 370),
                new Point2D(201, 96)
                )
        );
        bossWaypoints.addAll(Arrays.asList(
                new Point2D(465, 570),
                new Point2D(465, 640),
                new Point2D(1142, 640),
                new Point2D(1142, 46),
                new Point2D(821, 46),
                new Point2D(820, 340),
                new Point2D(170, 340),
                new Point2D(170, 96)
                )
        );
    }

    @Override
    protected void initPhysics() {
        for (ProjectileType projType : ProjectileType.values()) {
            for (EnemyTypes enemyType : EnemyTypes.values()) {
                FXGL.getPhysicsWorld().addCollisionHandler(
                        new ProjectileHandler(enemyType, projType));
            }
        }

    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        //Default monument health, money, and other settings to be changed upon selecting difficulty
        //transfers vars from CustomMainMenu to vars for GameWorld
        waypoints = new ArrayList<>();
        bigDemonWaypoints = new ArrayList<>();
        bossWaypoints = new ArrayList<>();
        vars.put("monumentHealth", 100);
        vars.put("Name", CustomMainMenu.getVars().get("Name"));
        vars.put("numEnemies", levelEnemies);
        vars.put("wave", 1);
        vars.put("ended", false);
        vars.put("Difficulty", CustomMainMenu.getVars().get("Difficulty"));
        switch ((Difficulty) vars.get("Difficulty")) {
        case EASY:
            vars.put("balance", 3000);
            vars.put("health", 200);
            vars.put("cost", 300);
            break;
        case MEDIUM:
            vars.put("balance", 2000);
            vars.put("health", 133);
            vars.put("cost", 400);
            break;
        case HARD:
            vars.put("balance", 2300);
            vars.put("health", 115);
            vars.put("cost", 400);
            break;
        default:
            System.out.println("Difficulty not chosen");
        }
    }

    @Override
    protected void initUI() {
        addUINode(GameUI.createUI());
    }

    public static Map<TowerDefenceType, JSONObject> getTowerMap() {
        return towerMap;
    }

    public ArrayList<Point2D> getWaypoints() {
        return new ArrayList<>(waypoints);
    }

    public ArrayList<Point2D> getBigDemonWaypoints() {
        return new ArrayList<>(bigDemonWaypoints);
    }

    public ArrayList<Point2D> getBossWaypoints() {
        return new ArrayList<>(bossWaypoints);
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static CustomEndMenu getEndMenu() {
        return endMenu;
    }

    public static Map<String, SimpleIntegerProperty> getStats() {
        return stats;
    }
}
