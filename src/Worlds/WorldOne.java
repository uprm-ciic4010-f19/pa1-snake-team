package Worlds;
import java.text.DecimalFormat;
import Game.Entities.Static.Apple;
import Main.Handler;


import java.awt.*;

import java.util.Random;

/**
 * Created by AlexVR on 7/2/2018.
 */



public class WorldOne extends WorldBase{

	int appleX;
	int appley;
    public WorldOne (Handler handler) {
        super(handler);

        //has to be a number bigger than 20 and even
        GridWidthHeightPixelCount = 60;
        GridPixelsize = (800/GridWidthHeightPixelCount);
        playerLocation = new Boolean[GridWidthHeightPixelCount][GridWidthHeightPixelCount];
        appleLocation = new Boolean[GridWidthHeightPixelCount][GridWidthHeightPixelCount];
        badappleLocation = new Boolean[GridWidthHeightPixelCount][GridWidthHeightPixelCount];
    }

    @Override
    public void tick() {
        super.tick();
        player.tick();
        if(!appleOnBoard){
            appleOnBoard=true;
            appleX = new Random().nextInt(handler.getWorld().GridWidthHeightPixelCount-1);
            appley = new Random().nextInt(handler.getWorld().GridWidthHeightPixelCount-1);

            //change coordinates till one is selected in which the player isn't standing
            boolean goodCoordinates=false;
            do{
                if(!handler.getWorld().playerLocation[appleX][appley] && !badappleLocation[appleX][appley]){
                    goodCoordinates=true;
                }
            }while(!goodCoordinates);

            apple = new Apple(handler,appleX,appley);
            appleLocation[appleX][appley]=true;

        }

        if(Game.Entities.Dynamic.Player.stepCounter == 100) { //Spawn bad Apple after # steps. Counter resets after spawn or eating.
        	Game.Entities.Dynamic.Player.stepCounter = 0;
        	spawnBadApple();
        }
    }

	public void spawnBadApple() {
		System.out.println("bad");
		Game.Entities.Dynamic.Player.stepCounter = 0;
		appleLocation[appleX][appley]=false;
		badapple = new Apple(handler,appleX,appley);
		badappleLocation[appleX][appley]=true;
		appleOnBoard = false;
	}
	
	
    @Override
    public void render(Graphics g){
        DecimalFormat df= new DecimalFormat ("#.##");
        super.render(g);
        player.render(g,playerLocation);

        g.drawString("Score: " + df.format(Game.Entities.Dynamic.Player.score),700, handler.getHeight()/64 );
    }

}
