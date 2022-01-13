package game.components;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.ProjectileComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.time.LocalTimer;
import game.graphics.GameUI;
import game.types.EnemyTypes;
import game.core.Launch;
import game.types.TowerDefenceType;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.json.simple.JSONObject;

import static com.almasb.fxgl.dsl.FXGLForKtKt.*;
import static javafx.beans.binding.Bindings.when;

public class TowerComponent extends Component {
    public static final double PROJECTILE_SPEED = 15 * 60;
    private JSONObject towerDescription;
    private TowerDefenceType towerType;
    private LocalTimer shootTimer;
    private TowerCannon tower;

    public TowerComponent(TowerDefenceType type, TowerCannon tower) {
        towerType = type;
//        this.box = box;
        this.tower = tower;
        towerDescription = Launch.getTowerMap().get(towerType);
    }

    public void colorDarker() {
        Color previous = (Color) tower.getBase().getFill();
        tower.getBase().setFill(previous.darker());
    }

    private void setEntityInt(Entity ent, String field, Object val) {
        ((JSONObject) ent.getProperties().getObject("json")).put(field, val);
    }

    private Integer getEntityInt(Entity ent, String field) {
        return ((Long) ((JSONObject) ent.getProperties().getObject("json")).get(field)).intValue();
    }

    @Override
    public void onAdded() {
        shootTimer = FXGL.newLocalTimer();
        shootTimer.capture();
        System.out.println("entity.getPosition() = " + entity.getPosition());
        entity.getViewComponent().addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            GameUI.createUpgradePopUp(this.entity);

        });
    }
    @Override
    public void onUpdate(double tpf) {
        //Need to update line 36 to shoot at general enemy type
        if (shootTimer.elapsed(Duration.seconds(0.5))) {
            FXGL.getGameWorld()
                    .getClosestEntity(entity, e -> (e.isType(EnemyTypes.NECROMANCER)
                            || (e.isType(EnemyTypes.CHORT)
                            || (e.isType(EnemyTypes.WOGOL)
                            || (e.isType(EnemyTypes.BOSS)
                            || (e.isType(EnemyTypes.BIGDEMON)))))))
                    .ifPresent(nearestEnemy -> {
                        shoot(nearestEnemy);
                        shootTimer.capture();
                    });
        }
    }

    public Point2D calcIntercept(Entity source, Entity target) {
        // double a = Math.pow(source.magnitude(), 2) - Math.pow(target.magnitude(), 2);
        // double b = 2 * target.subtract(source).dotProduct(target);
        // double c = -target.subtract(source).magnitude();
        if (!target.getProperties().exists("vector")) {
            return null;
        }
        Point2D targetVector = target.getProperties().getObject("vector");
        Point2D sourcePosition = source.getPosition();
        Point2D targetPosition = target.getPosition();
        Point2D toTarget = targetPosition.subtract(sourcePosition);

        double a = targetVector.dotProduct(targetVector) - PROJECTILE_SPEED * PROJECTILE_SPEED;
        double b = 2 * targetVector.dotProduct(toTarget);
        double c = toTarget.dotProduct(toTarget);
        double p = -b / (2 * a);
        double q = Math.sqrt((b * b) - 4 * a * c) / (2 * a);

        double t1 = p - q;
        double t2 = p + q;
        double t =  Math.min(t1, t2);
        Point2D aimSpot = targetPosition.add(targetVector.multiply(t));
        return aimSpot.subtract(sourcePosition);
    }

    private void shoot(Entity enemy) {
//        Point2D position = getEntity().getPosition();
//        if (calcIntercept(getEntity(), enemy) == null) {
//            return;
//        }
//        Point2D direction = enemy.getPosition().subtract(position);
////        System.out.printf("DISTANCE TO TARGET: %s\n", direction.magnitude());
//        if (direction.magnitude() > (Long) towerDescription.get("range")) {
//            return;
//        }
//        // direction = calcIntercept(getEntity(), enemy);
//        Entity projectile = FXGL.spawn((String) towerDescription.get("projectile"), position);
//        projectile.addComponent(new ProjectileComponent(direction, PROJECTILE_SPEED));
//        projectile.getProperties().setValue("json", towerDescription);
//        Paint previous = box.getFill();
//        box.setFill(Color.GREEN);
//        FXGL.getGameTimer().runOnceAfter(() -> {
//            box.setFill(previous);
//        }, Duration.millis(100));
        tower.shoot(this.entity, enemy);
    }

    /**
     * Update tower property
     * @param field Tower property to modify
     * @param inc Property to add to field
     */
    public void updateField(String field, String inc) {
        int original = ((Number) towerDescription.get(field)).intValue();
        towerDescription.put(field, Long.valueOf(original + ((Number) towerDescription.get(inc)).intValue()));
    }

    public JSONObject getTowerDescription() {
        return towerDescription;
    }

    public static String upgradeT(String tower, int balance, String difficulty) {
        int basicUpgrade = 50;
        int sprayUpgrade = 50;
        int freezeUpgrade = 50;
        if (difficulty == "HARD") {
            basicUpgrade *= 2;
            sprayUpgrade *= 2;
            freezeUpgrade *= 2;
        }
        int upgradeCost = 0;
        if (tower.equals("BASIC")) {
            upgradeCost = basicUpgrade;
        } else if (tower.equals("SPRAY")) {
            upgradeCost = sprayUpgrade;
        } else {
            upgradeCost = freezeUpgrade;
        }
        if (balance - upgradeCost >= 0) {
            return ("" + (balance - upgradeCost));
        } else {
            return "Balance Insufficient!";
        }
    }




}
