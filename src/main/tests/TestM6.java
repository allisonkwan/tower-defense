import game.components.TowerComponent;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;
public class TestM6 {
    @Test
    public void easyBasic1() {
        String output = TowerComponent.upgradeT("BASIC", 100, "EASY");
        assertThat(output, is("50"));
    }
    @Test
    public void easyBasic2() {
        String output = TowerComponent.upgradeT("BASIC", 49, "EASY");
        assertThat(output, is("Balance Insufficient!"));
    }
    @Test
    public void mediumBasic1() {
        String output = TowerComponent.upgradeT("BASIC", 100, "MEDIUM");
        assertThat(output, is("50"));
    }
    @Test
    public void mediumBasic2() {
        String output = TowerComponent.upgradeT("BASIC", 49, "MEDIUM");
        assertThat(output, is("Balance Insufficient!"));
    }
    @Test
    public void hardBasic1() {
        String output = TowerComponent.upgradeT("BASIC", 100, "HARD");
        assertThat(output, is("0"));
    }
    @Test
    public void hardBasic2() {
        String output = TowerComponent.upgradeT("BASIC", 99, "HARD");
        assertThat(output, is("Balance Insufficient!"));
    }
    @Test
    public void easySpray1() {
        String output = TowerComponent.upgradeT("SPRAY", 100, "EASY");
        assertThat(output, is("50"));
    }
    @Test
    public void easySpray2() {
        String output = TowerComponent.upgradeT("SPRAY", 49, "EASY");
        assertThat(output, is("Balance Insufficient!"));
    }
    @Test
    public void mediumSpray1() {
        String output = TowerComponent.upgradeT("SPRAY", 100, "MEDIUM");
        assertThat(output, is("50"));
    }
    @Test
    public void mediumSpray2() {
        String output = TowerComponent.upgradeT("SPRAY", 49, "MEDIUM");
        assertThat(output, is("Balance Insufficient!"));
    }
    @Test
    public void hardSpray1() {
        String output = TowerComponent.upgradeT("SPRAY", 100, "HARD");
        assertThat(output, is("0"));
    }
    @Test
    public void hardSpray2() {
        String output = TowerComponent.upgradeT("SPRAY", 99, "HARD");
        assertThat(output, is("Balance Insufficient!"));
    }
    @Test
    public void easyFreeze1() {
        String output = TowerComponent.upgradeT("FREEZE", 100, "EASY");
        assertThat(output, is("50"));
    }
    @Test
    public void easyFreeze2() {
        String output = TowerComponent.upgradeT("FREEZE", 49, "EASY");
        assertThat(output, is("Balance Insufficient!"));
    }
    @Test
    public void mediumFreeze1() {
        String output = TowerComponent.upgradeT("FREEZE", 100, "MEDIUM");
        assertThat(output, is("50"));
    }
    @Test
    public void mediumFreeze2() {
        String output = TowerComponent.upgradeT("FREEZE", 49, "MEDIUM");
        assertThat(output, is("Balance Insufficient!"));
    }
    @Test
    public void hardFreeze1() {
        String output = TowerComponent.upgradeT("FREEZE", 100, "HARD");
        assertThat(output, is("0"));
    }
    @Test
    public void hardFreeze2() {
        String output = TowerComponent.upgradeT("FREEZE", 99, "HARD");
        assertThat(output, is("Balance Insufficient!"));
    }
}
