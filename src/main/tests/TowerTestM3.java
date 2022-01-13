import game.core.*;
import game.graphics.CustomMainMenu;
import game.types.TowerDefenceType;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

public class TowerTestM3 {
    private static Map<TowerDefenceType, JSONObject> towerMap = new HashMap<>();

    public void gameCopy() {
        towerMap = Launch.checkDif((Difficulty) CustomMainMenu.getVars().get("Difficulty"));
    }

    //Make sure tower price is correct for difficulty easy
    @Test
    public void towerPriceEasy() {
        CustomMainMenu.setVars("Difficulty", Difficulty.EASY);
        gameCopy();
        assertThat((Integer) towerMap.get(TowerDefenceType.BASIC).get("cost"), is(100));
        assertThat((Integer) towerMap.get(TowerDefenceType.BASIC).get("cost"), is(200));
        assertThat((Integer) towerMap.get(TowerDefenceType.BASIC).get("cost"), is(300));
        // If you run this test at is it doesn't initialize towerList.
        // How do you get all the values initialized without running the game?
    }

    //Make sure tower price is correct for difficulty medium
    @Test
    public void towerPriceMedium() {
        CustomMainMenu.setVars("Difficulty", Difficulty.MEDIUM);
        gameCopy();
        assertThat((Integer) towerMap.get(TowerDefenceType.BASIC).get("cost"), is(200));
        assertThat((Integer) towerMap.get(TowerDefenceType.BASIC).get("cost"), is(400));
        assertThat((Integer) towerMap.get(TowerDefenceType.BASIC).get("cost"), is(600));
    }


    //Make sure tower price is correct for difficulty hard
    @Test
    public void towerPriceHard() {
        CustomMainMenu.setVars("Difficulty", Difficulty.HARD);
        gameCopy();
        assertThat((Integer) towerMap.get(TowerDefenceType.BASIC).get("cost"), is(300));
        assertThat((Integer) towerMap.get(TowerDefenceType.BASIC).get("cost"), is(600));
        assertThat((Integer) towerMap.get(TowerDefenceType.BASIC).get("cost"), is(900));
    }

    //Verifies that 3 towers are in TowerList
    @Test
    public void correctNumberOfTowers() {
        CustomMainMenu.setVars("Difficulty", Difficulty.HARD);
        gameCopy();
        assertThat(towerMap.size() == 3, is(true));
    }

    //Test that Tower tower type property is correct
    @Test
    public void testTowerType() {
        CustomMainMenu.setVars("Difficulty", Difficulty.EASY);
        gameCopy();
        JSONObject td = towerMap.get(TowerDefenceType.BASIC);
        assertThat(TowerDefenceType.valueOf((String) td.get("type")), is(TowerDefenceType.BASIC));
    }

    //Test that Tower tower range property is correct
    @Test
    public void testTowerRange() {
        CustomMainMenu.setVars("Difficulty", Difficulty.EASY);
        gameCopy();
        JSONObject td = towerMap.get(TowerDefenceType.BASIC);
        assertThat((Integer) td.get("range"), is(10));
    }

    //Test that Tower tower fire rate property is correct
    @Test
    public void testTowerFR() {
        CustomMainMenu.setVars("Difficulty", Difficulty.EASY);
        gameCopy();
        JSONObject td = towerMap.get(TowerDefenceType.BASIC);
        assertThat((Integer) td.get("fire_rate"), is(1));
    }

    //Test that AOE tower type property is correct
    @Test
    public void testAOEType() {
        CustomMainMenu.setVars("Difficulty", Difficulty.EASY);
        gameCopy();
        JSONObject td = towerMap.get(TowerDefenceType.SPRAY);
        assertThat(TowerDefenceType.valueOf((String) td.get("type")), is(TowerDefenceType.SPRAY));
    }

    //Test that AOE tower range property is correct
    @Test
    public void testAOERange() {
        CustomMainMenu.setVars("Difficulty", Difficulty.EASY);
        gameCopy();
        JSONObject td = towerMap.get(TowerDefenceType.SPRAY);
        assertThat((Integer) td.get("range"), is(20));
    }

    //Test that AOE tower fire rate property is correct
    @Test
    public void testAOEFr() {
        CustomMainMenu.setVars("Difficulty", Difficulty.EASY);
        gameCopy();
        JSONObject td = towerMap.get(TowerDefenceType.SPRAY);
        assertThat((Integer) td.get("fire_rate"), is(2));
    }

    //Test that Slow tower type property is correct
    @Test
    public void testSlowType() {
        CustomMainMenu.setVars("Difficulty", Difficulty.EASY);
        gameCopy();
        JSONObject td = towerMap.get(TowerDefenceType.FREEZE);
        assertThat(TowerDefenceType.valueOf((String) td.get("type")), is(TowerDefenceType.FREEZE));
    }

    //Test that Slow tower range property is correct
    @Test
    public void testSlowRange() {
        CustomMainMenu.setVars("Difficulty", Difficulty.EASY);
        gameCopy();
        JSONObject td = towerMap.get(TowerDefenceType.FREEZE);
        assertThat((Integer) td.get("range"), is(30));
    }

    //Test that Slow tower fire rate property is correct
    @Test
    public void testSlowFR() {
        CustomMainMenu.setVars("Difficulty", Difficulty.EASY);
        gameCopy();
        JSONObject td = towerMap.get(TowerDefenceType.FREEZE);
        assertThat((Integer) td.get("fire_rate"), is(3));
    }

    //Tests clicking a towerBase that doesn't currently hold a tower
    @Test
    public void testClickTile() {

    }


    //Test buying a tower but balance is less than tower price
    @Test
    public void buyButBalanceInsufficient() {

    }

    //Test buying a tower and balance is sufficient
    @Test
    public void buyBalanceSufficient() {

    }

}
