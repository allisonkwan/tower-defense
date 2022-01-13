package game.components;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.ProjectileComponent;
import com.almasb.fxgl.entity.Entity;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import org.json.simple.JSONObject;
import org.w3c.dom.css.Rect;

import java.awt.*;

public class TowerCannon {
    private Rectangle base;
    private Rectangle cannon;
    private Rectangle muzzle;
    private Point2D orientation;
    private StackPane pane;

    public TowerCannon(Paint color) {
        base = new Rectangle(32, 32, color);
        cannon = new Rectangle(30, 5, Color.WHITE);
        cannon.setTranslateX(15);
        // TODO create muzzle flash?
        pane = new StackPane(base, cannon);
        orientation = new Point2D(1, 0);
    }

    private void rotate(double theta) {
        System.out.printf("Rotating: %f\n", theta);
        Rotate rotate = new Rotate(theta, 0, 0);
        cannon.getTransforms().clear();
        cannon.getTransforms().add(rotate);
    }

    public void shoot(Entity tower, Entity enemy) {
        Point2D position = tower.getPosition();
        Point2D direction = enemy.getPosition().subtract(position);
        JSONObject towerDescription = tower.getComponent(TowerComponent.class).getTowerDescription();
//        System.out.printf("DISTANCE TO TARGET: %s\n", direction.magnitude());
        if (direction.magnitude() > (Long) towerDescription.get("range")) {
            return;
        }
        // direction = calcIntercept(getEntity(), enemy);
        // .angle function returns (0,180) so multiply by y sign
        rotate(new Point2D(1, 0).angle(direction) * (direction.getY() > 0 ? 1.0 : -1.0));
        orientation = direction.normalize().multiply(30); //Get vector of barrel length
        double offsetAngle = 90 - (new Point2D(0, 1).angle(orientation));
        Point2D offset = new Point2D(Math.cos(offsetAngle), Math.sin(offsetAngle)).multiply(5/2);


        // Spawn bullet
        Bounds cannonPos = cannon.localToScene(cannon.getBoundsInLocal());
        Point2D start = orientation.subtract(offset).add(new Point2D(cannonPos.getCenterX(), cannonPos.getCenterY()));
        Entity projectile = FXGL.spawn((String) towerDescription.get("projectile"), start);
//        if (towerDescription.get("projectile").equals("BULLET")) {
            FXGL.play("bulletSound.wav");
//        } else if ((towerDescription.get("projectile").equals("BOMB"))) {
//            FXGL.play("spraySound.wav");
//        }
        projectile.addComponent(new ProjectileComponent(direction, TowerComponent.PROJECTILE_SPEED));
        projectile.getProperties().setValue("json", towerDescription);

        //Toggle base color to green
        Paint previous = base.getFill();
        base.setFill(Color.GREEN);
        FXGL.getGameTimer().runOnceAfter(() -> {
            base.setFill(previous);
        }, Duration.millis(100));
    }
    public Rectangle getBase() {
        return base;
    }

    public StackPane getPane() {
        return pane;
    }
}
