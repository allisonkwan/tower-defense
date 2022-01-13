import game.collision.ProjectileHandler;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;

public class TestM5 {
    //BULLET projectile tests
    @Test
    public void bulletTestBigDemon() {
        String message = ProjectileHandler.collisionTester("BULLET", "BIGDEMON");
        assertThat(message, is("BULLET hit BIGDEMON. Enemy's new health: 240."));
    }
    @Test
    public void bulletTestChort() {
        String message = ProjectileHandler.collisionTester("BULLET", "CHORT");
        assertThat(message, is("BULLET hit CHORT. Enemy's new health: 60."));
    }
    @Test
    public void bulletTestNecromancer() {
        String message = ProjectileHandler.collisionTester("BULLET", "NECROMANCER");
        assertThat(message, is("BULLET hit NECROMANCER. Enemy's new health: 60."));
    }
    @Test
    public void bulletTestWogol() {
        String message = ProjectileHandler.collisionTester("BULLET", "WOGOL");
        assertThat(message, is("BULLET hit WOGOL. Enemy's new health: 30."));
    }

    //ICE projectile tests
    @Test
    public void iceTestBigDemon() {
        String message = ProjectileHandler.collisionTester("ICE", "BIGDEMON");
        assertThat(message, is("ICE hit BIGDEMON. Enemy's speed: 75. Enemy's health: 300."));
    }
    @Test
    public void iceTestChort() {
        String message = ProjectileHandler.collisionTester("ICE", "CHORT");
        assertThat(message, is("ICE hit CHORT. Enemy's speed: 125. Enemy's health: 100."));
    }
    @Test
    public void iceTestNecromancer() {
        String message = ProjectileHandler.collisionTester("ICE", "NECROMANCER");
        assertThat(message, is("ICE hit NECROMANCER. Enemy's speed: 150. Enemy's health: 100."));
    }
    @Test
    public void iceTestWogol() {
        String message = ProjectileHandler.collisionTester("ICE", "WOGOL");
        assertThat(message, is("ICE hit WOGOL. Enemy's speed: 150. Enemy's health: 50."));
    }

    //BOMB projectile tests
    @Test
    public void bombTestBigDemon() {
        String message = ProjectileHandler.collisionTester("BOMB", "BIGDEMON");
        assertThat(message, is("BOMB hit BIGDEMON. 3 bullets spawned!. Enemy's health: 240."));
    }
    @Test
    public void bombTestChort() {
        String message = ProjectileHandler.collisionTester("BOMB", "CHORT");
        assertThat(message, is("BOMB hit CHORT. 3 bullets spawned!. Enemy's health: 60."));
    }
    @Test
    public void bombTestNecromancer() {
        String message = ProjectileHandler.collisionTester("BOMB", "NECROMANCER");
        assertThat(message, is("BOMB hit NECROMANCER. 3 bullets spawned!. Enemy's health: 60."));
    }
    @Test
    public void bombTestWogol() {
        String message = ProjectileHandler.collisionTester("BOMB", "WOGOL");
        assertThat(message, is("BOMB hit CHORT. 3 bullets spawned!. Enemy's health: 60."));
    }
}
