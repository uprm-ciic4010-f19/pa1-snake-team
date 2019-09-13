package Game.GameStates;

import Main.Handler;
import Resources.Images;
import UI.UIImageButton;
import UI.UIManager;

import java.awt.*;

/**
 * Created by AlexVR on 7/1/2018.
 */
public class OptionsState extends State {

	private int count = 0;
	private UIManager uiManager;
	private String color;

	public OptionsState(Handler handler) {
		super(handler);
		uiManager = new UIManager(handler);
		handler.getMouseManager().setUimanager(uiManager);



		uiManager.addObjects(new UIImageButton(56, 375, 64, 64, Images.BackArrow, () -> {
			Game.Entities.Dynamic.Player.colorChoice--;
			if(Game.Entities.Dynamic.Player.colorChoice == -1) {
				Game.Entities.Dynamic.Player.colorChoice = 5;
			}
			switchColor();
		}));

		uiManager.addObjects(new UIImageButton(356, 375, 64, 64, Images.FrontArrow, () -> {
			Game.Entities.Dynamic.Player.colorChoice++;
			if(Game.Entities.Dynamic.Player.colorChoice == 6) {
				Game.Entities.Dynamic.Player.colorChoice = 0;
			}
			switchColor();
		}));

		uiManager.addObjects(new UIImageButton( 20,700, 128, 64, Images.Back, () -> {
			handler.getMouseManager().setUimanager(null);
			State.setState(handler.getGame().pauseState);
		}));

		
		switchColor();




	}

	private void switchColor() {
		switch(Game.Entities.Dynamic.Player.colorChoice){
		case 1: 
			Game.Entities.Dynamic.Player.playerColor = Color.BLUE;
			color = "BLUE";
			break;
		case 2: 
			Game.Entities.Dynamic.Player.playerColor = Color.CYAN;
			color = "CYAN";
			break;
		case 3: 
			Game.Entities.Dynamic.Player.playerColor = Color.WHITE;
			color = "WHITE";
			break;
		case 4: 
			Game.Entities.Dynamic.Player.playerColor = Color.YELLOW;
			color = "YELLOW";
			break;
		case 5: 
			Game.Entities.Dynamic.Player.playerColor = Color.RED;
			color = "RED";
			break;
		default: 
			Game.Entities.Dynamic.Player.playerColor = Color.GREEN;
			color = "GREEN";
			break;
		}
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


		g.drawImage(Images.Pause,0,0,handler.getHeight(),handler.getWidth(),null);
		uiManager.Render(g);

		Font f= new Font("Times New Roman", Font.PLAIN, 50);
		g.setFont(f);
		g.setColor(Color.WHITE);
		g.drawString("Snake Color:", handler.getWidth()/8, 325);

		g.setColor(Game.Entities.Dynamic.Player.playerColor);
		g.drawString(color, 135, 425);

	}
}
