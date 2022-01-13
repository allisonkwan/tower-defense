package game.core;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.OffscreenCleanComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import game.components.*;
import game.types.EnemyTypes;
import game.types.ProjectileType;
import game.types.TowerDefenceType;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import static com.almasb.fxgl.dsl.FXGLForKtKt.*;

public class TowerDefenceFactory implements EntityFactory {

    private Entity spawnGenEnemy(SpawnData data, EnemyTypes type) {
        int size = 16;
        if (type == EnemyTypes.BOSS) {
            size = 32;
        }
        return entityBuilder(data)
                .type(type)
                .bbox(new HitBox(BoundingShape.box(size, size)))
                .with(new CollidableComponent(true))
                .with(new EnemyComponent(type))
                .build();
    }

    @Spawns("BIGDEMON")
    public Entity spawnBigDemon(SpawnData data) {
        return spawnGenEnemy(data, EnemyTypes.BIGDEMON);
    }

    @Spawns("CHORT")
    public Entity spawnChort(SpawnData data) {
        return spawnGenEnemy(data, EnemyTypes.CHORT);
    }

    @Spawns("NECROMANCER")
    public Entity spawnNecromancer(SpawnData data) {
        return spawnGenEnemy(data, EnemyTypes.NECROMANCER);
    }

    @Spawns("WOGOL")
    public Entity spawnWogol(SpawnData data) {
        return spawnGenEnemy(data, EnemyTypes.WOGOL);
    }

    @Spawns("BOSS")
    public Entity spawnBoss(SpawnData data) {
        return spawnGenEnemy(data, EnemyTypes.BOSS);
    }

    private Entity spawnGenTower(SpawnData data, TowerDefenceType type) {
        TowerCannon tower = new TowerCannon(data.get("color"));
        return entityBuilder(data)
                .type(type)
                .viewWithBBox(tower.getPane())
                .with(new CollidableComponent(true))
                .with(new TowerComponent(type, tower))
                .build();
    }

    @Spawns("BASIC")
    public Entity spawnTower(SpawnData data) {
        return spawnGenTower(data, TowerDefenceType.BASIC);
    }

    @Spawns("SPRAY")
    public Entity spawnAOE(SpawnData data) {
        return spawnGenTower(data, TowerDefenceType.SPRAY);
    }

    @Spawns("FREEZE")
    public Entity spawnSlow(SpawnData data) {
        return spawnGenTower(data, TowerDefenceType.FREEZE);
    }


    // This is the clickable tile that players can put towers on
    @Spawns("towerBase")
    public Entity newTowerBase(SpawnData data) {
        return FXGL.entityBuilder(data)
                // need the rectangle to give object a view component that can then be clicked
                .view(new Rectangle(data.<Integer>get("width"),
                        data.<Integer>get("height"),
                        Color.TRANSPARENT))
                .with(new TowerBaseComponent())
                .build();
    }

    @Spawns("menu")
    public Entity newMenu(SpawnData data) {
        return FXGL.entityBuilder(data)
                // need the rectangle to give object a view component that can then be clicked
                .view(new Rectangle(data.<Integer>get("width"),
                        data.<Integer>get("height"),
                        Color.TRANSPARENT))
                .with(new MenuComponent())
                .build();
    }

    @Spawns("monument")
    public Entity newMonument(SpawnData data) {
        return FXGL.entityBuilder(data)
                .view(new Rectangle(data.<Integer>get("width"),
                        data.<Integer>get("height"),
                        Color.TRANSPARENT))
                .build();
    }

    @Spawns("BULLET")
    // Bullet
    public Entity spawnProjectile1(SpawnData data) {
        return entityBuilder(data)
                .type(ProjectileType.BULLET)
                .viewWithBBox(new Rectangle(15, 5, Color.DARKGREY))
                .with(new CollidableComponent(true))
                .with(new OffscreenCleanComponent())
                .build();
    }

    @Spawns("BOMB")
    // Bomb
    public Entity spawnProjectile2(SpawnData data) {
        return entityBuilder(data)
                .type(ProjectileType.BOMB)
                .viewWithBBox(new Circle(15, 5, 5, Color.BLACK))
                .with(new CollidableComponent(true))
                .with(new OffscreenCleanComponent())
                .build();
    }

    @Spawns("ICE")
    //idk
    public Entity spawnProjectile3(SpawnData data) {
        return entityBuilder(data)
                .type(ProjectileType.ICE)
                .viewWithBBox(new Rectangle(15, 5,  Color.BLUE))
                .with(new CollidableComponent(true))
                .with(new OffscreenCleanComponent())
                .build();
    }
}
