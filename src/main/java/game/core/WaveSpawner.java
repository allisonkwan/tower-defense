package game.core;

import com.almasb.fxgl.dsl.FXGL;
import game.types.EnemyTypes;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.util.Duration;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

import static com.almasb.fxgl.dsl.FXGLForKtKt.getGameController;
import static com.almasb.fxgl.dsl.FXGLForKtKt.spawn;

public class WaveSpawner {
    private Difficulty diff;
    private JSONObject settings;
    private static BooleanProperty active;

    /**
     * Read wave settings file and pass in game difficulty
     * @param diff Game mode difficulty
     * @throws Exception File read exception
     */
    public WaveSpawner(Difficulty diff) {
        this.diff = diff;
        InputStream is = getClass().getClassLoader().getResourceAsStream("assets/config/wave.json");
        Reader fr = new InputStreamReader(is);
        try {
            JSONObject file = (JSONObject) new JSONParser().parse(fr);
            settings = (JSONObject) file.get(diff.name());
        } catch (Exception e) {
            getGameController().exit();
        }
        active = new SimpleBooleanProperty(false);
    }

    /**
     * Spawn wave
     * @param n Wave number to spawn
     */
    public void waveSpawn(int n) {
        List<EnemyTypes> queue = new LinkedList<>();
        Double delay = (Double) settings.get("delay");
        JSONObject waveSettings = (JSONObject) ((JSONArray) settings.get("waves")).get(n - 1);
        for (EnemyTypes e : EnemyTypes.values()) {
            if (waveSettings.containsKey(e.name())) {
                for (int i = 0; i < (Long) waveSettings.get(e.name()); i++) {
                    queue.add(e);
                }
            }
        }
        active.set(true);
        FXGL.getGameTimer().runAtInterval(() -> {
            String type = queue.remove(0).name();
            spawn(type);
        }, Duration.seconds(delay), queue.size());
    }

    public static void main(String[] args) {
        WaveSpawner ws = null;
        try {
            ws = new WaveSpawner(Difficulty.EASY);
        } catch (Exception e) {
            System.out.println(e);
        }
        ws.waveSpawn(1);
    }

    public static BooleanProperty activeProperty() {
        return active;
    }
}

