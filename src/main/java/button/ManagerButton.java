package button;


import models.Model;
import models.Transform;
import org.joml.Vector3f;
import render.Camera;
import render.Shader;
import render.Texture;

public class ManagerButton {
    private Button selected;
    private Button unselected;
    private boolean state;

    public ManagerButton(String select, String unselect, boolean st, float dec) {
        selected = new Button(select,dec);
        unselected = new Button(unselect,dec);
        state = st; //false:unselect true:select
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public boolean isState() {
        return state;
    }

    public void render(Shader shader, Camera camera)
    {
        if (state){
            selected.render(shader, camera);
        }
        else
        {
            unselected.render(shader, camera);
        }
    }
}
