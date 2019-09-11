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
	int Power;
	int powerX;
	int powerY;

	public WorldOne (Handler handler) {
		super(handler);

		//has to be a number bigger than 20 and even
		GridWidthHeightPixelCount = 60;
		GridPixelsize = (800/GridWidthHeightPixelCount);
		playerLocation = new Boolean[GridWidthHeightPixelCount][GridWidthHeightPixelCount];
		appleLocation = new Boolean[GridWidthHeightPixelCount][GridWidthHeightPixelCount];
		badappleLocation = new Boolean[GridWidthHeightPixelCount][GridWidthHeightPixelCount];
		slowdownLocation = new Boolean[GridWidthHeightPixelCount][GridWidthHeightPixelCount];
		invincibilityLocation = new Boolean[GridWidthHeightPixelCount][GridWidthHeightPixelCount];
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

		if(Game.Entities.Dynamic.Player.powerCounter == 200) { //Spawn random powerup after # steps. Counter resets after spawning, eating powerup, or despawning.
			spawnPowerUp();
			Game.Entities.Dynamic.Player.powerCounter = 0;
		}

		if(Game.Entities.Dynamic.Player.depowerCounter == 300) { //Despawn powerup after # steps. Counter resets after eating powerup or despawning.
			despawnPowerUp();
			Game.Entities.Dynamic.Player.powerCounter = 0;
			Game.Entities.Dynamic.Player.depowerCounter = 0;
		}
	}

		public void spawnBadApple() { // Replaces good apple with bad apple
			Game.Entities.Dynamic.Player.stepCounter = 0;
			appleLocation[appleX][appley]=false;
			badapple = new Apple(handler,appleX,appley);
			badappleLocation[appleX][appley]=true;
			appleOnBoard = false;
		}

		public void spawnPowerUp() { //Spawn a random power

			Power = new Random().nextInt(2);

			powerX = new Random().nextInt(handler.getWorld().GridWidthHeightPixelCount-1);
			powerY = new Random().nextInt(handler.getWorld().GridWidthHeightPixelCount-1);

			boolean goodCoordinates=false;
			do{
				if(!handler.getWorld().playerLocation[powerX][powerY] && !badappleLocation[powerX][powerY] && !appleLocation[powerX][powerY]){
					goodCoordinates=true;
				}
			}while(!goodCoordinates);


			if (Power==0) {
				slowdown = new Apple(handler,powerX,powerY);
				slowdownLocation[powerX][powerY]=true;
				System.out.println(powerX + " " + powerY);
			}

			if (Power==1) {
				invincibility = new Apple(handler,powerX,powerY);
				invincibilityLocation[powerX][powerY]=true;
				System.out.println(powerX + " " + powerY);
			}
		}

		public void despawnPowerUp() { //Despawn powerup function
			if (Power==0) {
				slowdownLocation[powerX][powerY]=false;
			}

			if (Power==1) {
				invincibilityLocation[powerX][powerY]=false;
				
			}
		}



		@Override
		public void render(Graphics g){

			DecimalFormat df= new DecimalFormat ("#.##");
			super.render(g);
			player.render(g,playerLocation);
			g.setColor(Color.GREEN);
			g.drawString("Score: " + df.format(Game.Entities.Dynamic.Player.score),700, handler.getHeight()/64 );
		}

	}
