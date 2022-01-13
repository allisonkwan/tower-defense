package game.collision;

import com.almasb.fxgl.audio.Sound;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.ProjectileComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import game.components.EnemyComponent;
import game.components.TowerComponent;
import game.core.Launch;
import game.core.WaveSpawner;
import game.types.EnemyTypes;
import game.event.EnemyKilledEvent;
import game.types.ProjectileType;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.json.simple.JSONObject;

import java.awt.desktop.SystemEventListener;

import static com.almasb.fxgl.dsl.FXGL.getAudioPlayer;
import static game.components.TowerComponent.PROJECTILE_SPEED;
import static game.types.TowerDefenceType.BASIC;

public class ProjectileHandler extends CollisionHandler {
    private Sound monsters = FXGL.getAssetLoader().loadSound("monsters.wav");
    Image spark = FXGL.image("spark.png");
    public ProjectileHandler(EnemyTypes enemyType, ProjectileType projType) {
        super(projType, enemyType);
    }

    private void setEntityInt(Entity ent, String field, Object val) {
        ((JSONObject) ent.getProperties().getObject("json")).put(field, val);
    }

    private Integer getEntityInt(Entity ent, String field) {
        return ((Long) ((JSONObject) ent.getProperties().getObject("json")).get(field)).intValue();
    }
    protected void onCollisionBegin(Entity projectile, Entity enemy) {
        if (projectile.getProperties().getString("type").equals("BULLET")) {
            //BULLET handling
            System.out.println("Bullet collision detected.");
            //line below seems to subtract enemy damage from enemy health, not projectile damage
            long newHealth = (getEntityInt(enemy, "health") - getEntityInt(projectile, "damage"));
            System.out.println("new health: " + newHealth);
            setEntityInt(enemy, "health", newHealth);
        } else if (projectile.getProperties().getString("type").equals("ICE")) {
            //ICE handling
            System.out.println("Ice collision detected.");
            if (enemy.getComponent(EnemyComponent.class).slow(true)) {
                FXGL.play("otherIceSound.wav");
                enemy.getComponent(EnemyComponent.class).updateField("speed", (long) -100);
                FXGL.getGameTimer().runOnceAfter(() -> {
                    try {
                        enemy.getComponent(EnemyComponent.class).updateField("speed", (long) 100);
                        enemy.getComponent(EnemyComponent.class).slow(false);
                    } catch (IllegalArgumentException e) {
                        System.out.println("already deleted");
                    }

                }, Duration.seconds(3));
            }
        } else {
            //AOE handling
            System.out.println("AOE collision detected.");
            FXGL.play("sprayExplosion.wav");
            Point2D position = enemy.getPosition();
            //Point2D direction = enemy.getPosition().subtract(position);
            double x = 2 * Math.PI * Math.random();
            double y = 2 * Math.PI * Math.random();
            for (int i = 0; i < getEntityInt(projectile, "count"); i++) {
                Point2D direction = new Point2D((Math.cos(x + i * Math.PI / 8)),
                        (Math.sin(y + i * Math.PI / 8)));
                Entity bullet = FXGL.spawn("BULLET", position);
                bullet.setProperty("json", projectile.getProperties().getObject("json"));
                bullet.addComponent(new ProjectileComponent(direction, PROJECTILE_SPEED));
            }
            long newHealth = getEntityInt(enemy, "health") - getEntityInt(enemy, "damage");
            setEntityInt(enemy, "health", newHealth);
        }

        ImageView hit = new ImageView(spark);
        hit.setX(projectile.getX());
        hit.setY(projectile.getY());
        hit.setLayoutX(-80);
        hit.setLayoutY(-80);
        hit.setPreserveRatio(true);
        hit.setScaleX(0.2);
        hit.setScaleY(0.2);
        FXGL.addUINode(hit);
        projectile.removeFromWorld();
        FXGL.getGameTimer().runOnceAfter(() -> {
            FXGL.removeUINode(hit);
            try {
                if (getEntityInt(enemy, "health") <= 0) {
                    enemy.removeFromWorld();
                    int current = Launch.getStats().get(enemy.getType().toString()).intValue();
                    Launch.getStats().get(enemy.getType().toString()).set(current + 1);
                    if (FXGL.getGameWorld().getEntitiesByComponent(EnemyComponent.class).size() == 0) {
                        WaveSpawner.activeProperty().set(false);
                        current = Launch.getStats().get("waves").intValue();
                        Launch.getStats().get("waves").set(current + 1);
                    }
                    FXGL.getEventBus().fireEvent(new EnemyKilledEvent(enemy));
                    getAudioPlayer().stopSound(monsters);
                }
            } catch(IllegalArgumentException e) {
                System.out.println("alr deleted");
            }
        }, Duration.millis(50));

    }

    public static String collisionTester(String projectile, String enemy) {
        String message = "";
        int health;
        int damage;
        int speed;
        if (enemy.equals("BIGDEMON")) {
            health = 300;
            damage = 60;
            speed = 175;
        } else if (enemy.equals("CHORT")) {
            health = 100;
            damage = 40;
            speed = 225;
        } else if (enemy.equals("NECROMANCER")) {
            health = 100;
            damage = 40;
            speed = 250;
        } else {
            health = 50;
            damage = 20;
            speed = 250;
        }
        long newHealth = health;
        if (projectile.compareTo("BULLET") == 0) {
            newHealth = health - damage;
            message += "BULLET hit " + enemy + ". Enemy's new health: ";
        } else if (projectile.compareTo("ICE") == 0) {
            speed -= 100;
            message += "ICE hit " + enemy  + ". Enemy's speed: " + speed + "."
                    + " Enemy's health: ";
        } else {
            int count = 0;
            for (int i = 0; i < 3; i++) {
                count++;
            }
            newHealth = health - damage;
            message += "BOMB hit " + enemy + ". " + count + " bullets spawned!"
                    + ". Enemy's health: ";
        }
        if (health <= 0) {
            newHealth = 0;
            message += "Enemy is dead!";
        }
        message += newHealth + ".";
        return message;
    }
}
