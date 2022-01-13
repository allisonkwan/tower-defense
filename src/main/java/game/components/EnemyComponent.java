package game.components;
import com.almasb.fxgl.audio.Sound;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import game.types.EnemyTypes;
import javafx.geometry.Point2D;
import game.core.Launch;
import javafx.scene.input.KeyCode;
import javafx.scene.robot.Robot;
import javafx.util.Duration;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import static com.almasb.fxgl.dsl.FXGL.getAudioPlayer;
import static game.types.EnemyTypes.BOSS;

public class EnemyComponent extends Component {

    private List<Point2D> waypoints;
    private Point2D nextWaypoint;
    private double speed;
    private boolean slowed = false;
    private boolean inverseAnim = true;

    private AnimatedTexture texture;
    private AnimationChannel run;

    private JSONObject enemy;
    private EnemyTypes type;


    public EnemyComponent(EnemyTypes type) {
        this.type = type;
        InputStream is = getClass().getClassLoader().getResourceAsStream(
                "assets/config/enemies.json"
        );
        Reader read = new InputStreamReader(is);
        try {
            JSONObject jsonObj = (JSONObject) new JSONParser().parse(read);
            enemy = (JSONObject) jsonObj.get(type.name());
        } catch (Exception e) {
            System.out.println("Error message: " + e);
        }
        if (this.type == EnemyTypes.BOSS) {
            run = new AnimationChannel(FXGL.image((String) enemy.get("sprite")), 9,
                    ((Long) enemy.get("width")).intValue(),
                    ((Long) enemy.get("height")).intValue(),
                    Duration.seconds(1), 0, 8);
            texture = new AnimatedTexture(run);
        } else {
            run = new AnimationChannel(FXGL.image((String) enemy.get("sprite")), 4,
                    ((Long) enemy.get("width")).intValue(),
                    ((Long) enemy.get("height")).intValue(),
                    Duration.seconds(0.5), 0, 3);
            texture = new AnimatedTexture(run);
        }

    }

    @Override
    public void onAdded() {
        // Handle animations
        getEntity().setProperty("json", enemy);
        //getEntity().setProperty("health", ((Long) enemy.get("health")).intValue());
        entity.getViewComponent().addChild(texture);
        texture.loopAnimationChannel(run);

        // Handle waypoints and movement
        if(this.type == EnemyTypes.BOSS) {
            entity.setPosition(0, 560);
            waypoints = ((Launch) FXGL.getApp()).getBossWaypoints();
        } else if (this.type == EnemyTypes.BIGDEMON){
            entity.setPosition(0, 595);
            waypoints = ((Launch) FXGL.getApp()).getBigDemonWaypoints();
        } else {
            entity.setPosition(0, 608);
            waypoints = ((Launch) FXGL.getApp()).getWaypoints();
        }
        nextWaypoint = waypoints.remove(0);
    }

    public void updateField(String field, Long increment) {
        long original = (Long) enemy.get(field);
        enemy.put(field, original + increment);
    }

    public boolean slow(boolean s) {
        if (this.slowed && s) {
            System.out.println("Already slowed");
            return false;
        } else if (s) {
            System.out.println("Slowing");
            this.slowed = true;
            return true;
        } else {
            System.out.println("Speeding");
            this.slowed = false;
            return true;
        }
    }


    @Override
    public void onUpdate(double tpf) {
        speed = tpf * (Long) enemy.get("speed");
        Point2D velocity = nextWaypoint.subtract(entity.getPosition())
                .normalize()
                .multiply(speed);

        getEntity().setProperty("vector", velocity);
        entity.translate(velocity);

        if (nextWaypoint.getX() >= 821 && nextWaypoint.getX() <= 864 && inverseAnim) {
            inverseAnim = false;
            entity.getViewComponent().clearChildren();
            if (entity.getType() == EnemyTypes.BOSS) {
                run = new AnimationChannel(FXGL.image((String) "wormWalkL.png"), 9,
                        90,
                        ((Long) enemy.get("height")).intValue(),
                        Duration.seconds(1), 0, 8);
            } else if (entity.getType() == EnemyTypes.BIGDEMON) {
                run = new AnimationChannel(FXGL.image((String) "bigDemonL.png"), 4,
                        ((Long) enemy.get("width")).intValue(),
                        ((Long) enemy.get("height")).intValue(),
                        Duration.seconds(0.5), 0, 3);
            } else {
                String string = "";
                if (entity.getType() == EnemyTypes.WOGOL) {
                    run = new AnimationChannel(FXGL.image((String) "wogolL.png"), 4,
                            ((Long) enemy.get("width")).intValue(),
                            ((Long) enemy.get("height")).intValue(),
                            Duration.seconds(0.5), 0, 3);
                } else if (entity.getType() == EnemyTypes.NECROMANCER) {
                    run = new AnimationChannel(FXGL.image((String) "necromancerL.png"), 4,
                            ((Long) enemy.get("width")).intValue(),
                            ((Long) enemy.get("height")).intValue(),
                            Duration.seconds(0.5), 0, 3);
                } else {
                    run = new AnimationChannel(FXGL.image((String) "chortL.png"), 4,
                            ((Long) enemy.get("width")).intValue(),
                            ((Long) enemy.get("height")).intValue(),
                            Duration.seconds(0.5), 0, 3);
                }
            }
            texture = new AnimatedTexture(run);
            entity.getViewComponent().addChild(texture);
            texture.loopAnimationChannel(run);
        }
        if (nextWaypoint.distance(entity.getPosition()) < speed) {
            entity.setPosition(nextWaypoint);

            if (!waypoints.isEmpty()) {
                nextWaypoint = waypoints.remove(0);
            } else if (FXGL.geti("health") - ((Long) enemy.get("damage")).intValue() <= 0) {
                entity.removeFromWorld();
                FXGL.play("oof.wav");
                FXGL.set("health", 0);
                FXGL.set("ended", true);
                Launch.getEndMenu().setEnd(false);
                Robot r = new Robot();
                r.keyPress(KeyCode.ESCAPE);
                r.keyRelease(KeyCode.ESCAPE);
            } else {
                FXGL.set("health", FXGL.geti("health") - ((Long) enemy.get("damage")).intValue());
                FXGL.play("oof.wav");
                entity.removeFromWorld();
                FXGL.set("ended", true);
                Launch.getEndMenu().setEnd(false);
            }
        }
    }
    public static int onUpdateLog(List<Point2D> waypoints, int health) {
        if (!waypoints.isEmpty()) {
            waypoints.remove(0);
            return health;
        } else if ((health) - 50 <= 0) {
            return 0;
        } else {
            health = health - 50; // -# is the damage
            return health;
        }
    }

    public static Point2D velocityMeth(
            List<Point2D> waypoints, double tpf, int wp, int owp, boolean nextwp) {
        double speed = tpf * 3000;

        Point2D velocity = waypoints.get(owp).subtract(waypoints.get(wp))
                .normalize()
                .multiply(speed);

        Point2D newPos = new Point2D(
                velocity.getX() * tpf + waypoints.get(wp).getX(),
                velocity.getY() * tpf + waypoints.get(wp).getY()
        );
        if (nextwp && waypoints.get(owp).distance(newPos) < speed) {
            return waypoints.get(owp);
        }
        return newPos;
    }
}
