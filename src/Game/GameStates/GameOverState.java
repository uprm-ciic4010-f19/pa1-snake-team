package Game.GameStates;

import Main.Handler;
import Resources.Images;
import UI.UIImageButton;
import UI.UIManager;

import java.awt.*;
import java.text.DecimalFormat;

/**
 * Created by AlexVR on 7/1/2018.
 */
public class GameOverState extends State {

    private int count = 0;
    private UIManager uiManager;

    public GameOverState(Handler handler) {
        super(handler);
        uiManager = new UIManager(handler);
        handler.getMouseManager().setUimanager(uiManager);

      

        uiManager.addObjects(new UIImageButton(56, (223+(64+16))+(64+16), 128, 64, Images.BTitle, () -> {
            handler.getMouseManager().setUimanager(null);
            State.setState(handler.getGame().menuState);
        }));





    }

    @Override
    public void tick() {
        handler.getMouseManager().setUimanager(uiManager);
        uiManager.tick();
        count++;
        if( count>=30){
            count=30;
        }
        if(handler.getKeyManager().pbutt && count>=30){
            count=0;

            State.setState(handler.getGame().gameState);
        }


    }

    @Override
    public void render(Graphics g) {
    	DecimalFormat df= new DecimalFormat ("#.##");
    	g.drawImage(Images.GameOver,0,0,handler.getHeight(),handler.getWidth(),Color.BLACK,null);
        Font f= new Font("Times New Roman", Font.PLAIN, 50);
        g.setFont(f);
        g.drawString("Score: " + df.format(Game.Entities.Dynamic.Player.score), handler.getWidth()/2, handler.getHeight()/2);
 
        uiManager.Render(g);

    }
}
