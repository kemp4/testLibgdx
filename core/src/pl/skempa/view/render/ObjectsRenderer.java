package pl.skempa.view.render;

import com.badlogic.gdx.scenes.scene2d.Stage;

import pl.skempa.model.Model;

/**
 * Created by Mymon on 2017-10-08.
 */

public interface ObjectsRenderer {

    void renderObjects(Model model);

    void renderStage(Stage stage);
}
