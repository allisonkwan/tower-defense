package game.components;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import game.graphics.GameUI;
import javafx.scene.input.MouseEvent;

public class MenuComponent extends Component {
    @Override
    public void onAdded() {
        // This properly detects a mouse click on the rectangle!
        entity.getViewComponent().addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            FXGL.play("mouseCLick.wav");
            GameUI.createUIToggleBtn();
        });
    }
}
