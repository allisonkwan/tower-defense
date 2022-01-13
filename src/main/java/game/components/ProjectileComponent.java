//package game.components;
//
//import com.almasb.fxgl.dsl.FXGL;
//import com.almasb.fxgl.entity.component.Component;
//import com.almasb.fxgl.texture.AnimatedTexture;
//import com.almasb.fxgl.texture.AnimationChannel;
//import game.types.EnemyTypes;
//import game.types.ProjectileType;
//import javafx.util.Duration;
//import org.json.simple.JSONObject;
//import org.json.simple.parser.JSONParser;
//
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.io.Reader;
//
//public class ProjectileComponent extends Component {
//    private AnimatedTexture texture;
//    private AnimationChannel run;
//
//    private JSONObject enemy;
//
//    public ProjectileComponent(ProjectileType type) {
//        InputStream is = getClass().getClassLoader().getResourceAsStream(
//                "assets/config/enemies.json"
//        );
//        Reader read = new InputStreamReader(is);
//        try {
//            JSONObject jsonObj = (JSONObject) new JSONParser().parse(read);
//            System.out.printf("Getting details for: %s\n", type.name());
//            enemy = (JSONObject) jsonObj.get(type.name());
//        } catch (Exception e) {
//            System.out.println("Error message: " + e);
//        }
//        run = new AnimationChannel(FXGL.image((String) enemy.get("sprite")), 4,
//                ((Long) enemy.get("width")).intValue(),
//                ((Long) enemy.get("height")).intValue(),
//                Duration.seconds(0.5), 0, 3);
//        texture = new AnimatedTexture(run);
//    }
//}
