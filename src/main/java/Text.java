import org.newdawn.slick.TrueTypeFont;
import java.awt.*;
import java.awt.font.FontRenderContext;

public class Text {
    private Font awtFont;
    private TrueTypeFont font;

    public Text(){
        awtFont = new Font("Times New Roman", Font.BOLD, 24);
        //font = new TrueTypeFont(awtFont,false);

    }

    public Text(int size, String police){
        awtFont = new Font(police, Font.BOLD, size);
        font = new TrueTypeFont(awtFont,false);
    }

    public void drawString(int x, int y, String text){
        //font.drawString(x,y,text);
    }
}
