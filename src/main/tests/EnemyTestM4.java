import game.components.EnemyComponent;
import game.core.*;
import javafx.geometry.Point2D;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

public class EnemyTestM4 {
    private List<Point2D> waypoints;
    private Point2D nextWaypoint;
    @Test
    public void waypointExistsTest() {
        waypoints = new ArrayList<>();
        waypoints.addAll(Arrays.asList(
                new Point2D(590, 528),
                new Point2D(590, 70),
                new Point2D(1200, 70)
                )
        );
        int newHealth = EnemyComponent.onUpdateLog(waypoints, 100);
        assertThat(newHealth, is(100));
    }
    @Test
    public void monumentGameOver() {
        waypoints = new ArrayList<>();
        waypoints.addAll(Arrays.asList(

                )
        );
        int newHealth = EnemyComponent.onUpdateLog(waypoints, 20);
        assertThat(newHealth, is(0));
    }
    @Test
    public void monumentHealthDecreaseTwo() {
        waypoints = new ArrayList<>();
        waypoints.addAll(Arrays.asList(

                )
        );
        int newHealth = EnemyComponent.onUpdateLog(waypoints, 51);
        assertThat(newHealth, is(1));
    }

    @Test
    public void healthDecTest() {
        waypoints = new ArrayList<>();
        waypoints.addAll(Arrays.asList(
                        new Point2D(590, 528),
                        new Point2D(590, 70),
                        new Point2D(1200, 70)
                )
        );
        int health = 100;
        int newHealth = EnemyComponent.onUpdateLog(waypoints, health);
        newHealth = EnemyComponent.onUpdateLog(waypoints, newHealth);
        newHealth = EnemyComponent.onUpdateLog(waypoints, newHealth);
        assertThat(newHealth, is(100));
        newHealth = EnemyComponent.onUpdateLog(waypoints, newHealth);
        assertThat(newHealth, is(50));
        newHealth = EnemyComponent.onUpdateLog(waypoints, newHealth);
        assertThat(newHealth, is(0));
    }

    @Test
    public void alreadyZeroHealth() {
        waypoints = new ArrayList<>();
        waypoints.addAll(Arrays.asList(

                )
        );
        int newHealth = EnemyComponent.onUpdateLog(waypoints, 0);
        assertThat(newHealth, is(0));
    }

    @Test
    public void wayPointTest() {
        waypoints = new ArrayList<>();
        waypoints.addAll(Arrays.asList(
                new Point2D(590, 528),
                new Point2D(590, 70),
                new Point2D(1200, 70)
                )
        );
        EnemyComponent.onUpdateLog(waypoints, 100);
        List<Point2D> newWaypoints = new ArrayList<>();
        newWaypoints.addAll(Arrays.asList(
                        new Point2D(590, 70),
                        new Point2D(1200, 70)
                )
        );
        assertThat(waypoints, is(newWaypoints));
    }

    @Test
    public void noWaypointDecHealth() {
        waypoints = new ArrayList<>();
        waypoints.addAll(Arrays.asList(
                        new Point2D(590, 528)
                )
        );
        int newHealth = EnemyComponent.onUpdateLog(waypoints, 100);
        assertThat(newHealth, is(100));
        newHealth = EnemyComponent.onUpdateLog(waypoints, 100);
        assertThat(newHealth, is(50));
    }

    @Test
    public void enemyVelocityTest() {
        waypoints = new ArrayList<>();
        waypoints.addAll(Arrays.asList(
                new Point2D(590, 528),
                new Point2D(590, 70),
                new Point2D(1200, 70)
                )
        );
        Point2D newPos = EnemyComponent.velocityMeth(waypoints, 1, 1, 2, false);
        assertThat(newPos, is(new Point2D(1590, 70)));
    }
    @Test
    public void noMoveTest() {
        waypoints = new ArrayList<>();
        waypoints.addAll(Arrays.asList(
                        new Point2D(590, 528),
                        new Point2D(590, 70),
                        new Point2D(590, 70)
                )
        );
        Point2D newPos = EnemyComponent.velocityMeth(waypoints, 1, 1, 2, false);
        assertThat(newPos, is(new Point2D(590, 70)));
    }

    @Test
    public void nextWaypointTest() {
        waypoints = new ArrayList<>();
        waypoints.addAll(Arrays.asList(
                        new Point2D(590, 528),
                        new Point2D(590, 70),
                        new Point2D(1200, 70)
                )
        );
        Point2D newPos = EnemyComponent.velocityMeth(waypoints, 1, 1, 2, true);
        assertThat(newPos, is(new Point2D(1200, 70)));
    }
}
