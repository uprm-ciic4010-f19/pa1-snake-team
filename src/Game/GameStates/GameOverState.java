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

      

        uiManager.addObjects(new UIImageButton((handler.getWidth()/2)-100, 500, 200, 64, Images.Restart, () -> {
        	handler.getMouseManager().setUimanager(null);
            handler.getGame().reStart();
            State.setState(handler.getGame().gameState);
            Game.Entities.Dynamic.Player.score = 0;
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
    	DecimalFormat df= new DecimalFormat ("#.#");
    	g.drawImage(Images.GameOver,0,0,handler.getHeight(),handler.getWidth(),Color.BLACK,null);
        Font f= new Font("Times New Roman", Font.PLAIN, 50);
        g.setFont(f);
        g.drawString("Score: " + df.format(Game.Entities.Dynamic.Player.score), 275, handler.getHeight()/2);
 
        uiManager.Render(g);

    }
}
