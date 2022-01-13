package game.components;

import com.almasb.fxgl.entity.component.Component;
import game.graphics.GameUI;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

public class TowerBaseComponent extends Component {

    @Override
    public void onAdded() {
        // This properly detects a mouse click on the rectangle!
        entity.getViewComponent().addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            GameUI.createBuyPopUp();
            updateCurrTowerBase(e.getPickResult().getIntersectedNode());
        });
    }

    private void updateCurrTowerBase(Node node) {
        GameUI.setCurrTowerBase(node);
    }
}
