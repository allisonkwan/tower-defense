package game.event;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import game.core.Launch;
import game.types.EnemyTypes;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.scene.input.KeyCode;
import javafx.scene.robot.Robot;
import org.json.simple.JSONObject;

public class  EnemyKilledEvent extends Event {
    public static final EventType<EnemyKilledEvent> ANY
            = new EventType<>(Event.ANY, "ENEMY_KILLED");

    private Entity enemy;

    /**
     * @return killed enemy
     */
    public Entity getEnemy() {
        return enemy;
    }

    private Integer getEntityInt(Entity ent, String field) {
        return ((Long) ((JSONObject) ent.getProperties().getObject("json")).get(field)).intValue();
    }

    public EnemyKilledEvent(Entity enemy) {
        super(ANY);
        this.enemy = enemy;
        if (enemy.getType() == EnemyTypes.BOSS) {
            //WIN GAME
            System.out.println("you win");
            Launch.getEndMenu().setEnd(true);
            Robot r = new Robot();
            r.keyPress(KeyCode.ESCAPE);
            r.keyRelease(KeyCode.ESCAPE);
        }
        FXGL.set("balance", FXGL.geti("balance") + getEntityInt(enemy, "goldVal"));
    }
}
