package Game.Entities.Dynamic;


import Main.Handler;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Random;

import Game.Entities.Static.Apple;
import Game.GameStates.State;

/**
 * Created by AlexVR on 7/2/2018.
 */
public class Player {

	public int lenght;
	public boolean justAte;
	private Handler handler;

	public int xCoord;
	public int yCoord;

	public static int stepCounter;
	public int moveCounter;
	public int speed;


	public String direction;


	public static double score = 0;


	public Player(Handler handler){
		this.handler = handler;
		xCoord = 0;
		yCoord = 0;
		moveCounter = 0;
		stepCounter = 0;
		speed = 10; //The higher the number, the slower the snake
		direction= "Right";
		justAte = false;
		lenght= 1;

	}

	public void tick(){
		moveCounter++;
		if(moveCounter>=speed) {
			checkCollisionAndMove();
			moveCounter=0;
			stepCounter++;
		}
		if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_UP) && !(direction=="Down")){
				direction="Up";
		}

		else if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_DOWN) && !(direction=="Up")){
				direction="Down";
		}

		else if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_LEFT) && !(direction=="Right")){
				direction="Left";
		}

		else if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_RIGHT) && !(direction=="Left")){
				direction="Right";
		}

		if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_N)){ //Adds tail debug
			EatDebug();
		}
		
		if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_X)){ //Remove tail debug
			if(lenght >1) {
			lenght--;
			if (score > 0) {
				score -= Math.sqrt(2*score+1);
			}
			if(score < 0) {
				score = 0;
			}
			handler.getWorld().playerLocation[handler.getWorld().body.getLast().x][handler.getWorld().body.getLast().y] = false;
			handler.getWorld().body.removeLast();
			}
			else {
			State.setState(handler.getGame().gameoverState);
			}
		}

		if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_ESCAPE)){ //Pause Game function
			State.setState(handler.getGame().pauseState);
		}
		

		if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_ADD) || handler.getKeyManager().keyJustPressed(KeyEvent.VK_EQUALS)){ //Speed up snake debug. 
			if(speed > 3) { //3 is good max speed.
				speed--;
			}
		}

		if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_SUBTRACT) || handler.getKeyManager().keyJustPressed(KeyEvent.VK_MINUS)){ // Slow down snake debug.
			if(speed < 10) { // 10 is slow enough.
				speed++;
			}
		}
	
		

	}

	public void checkCollisionAndMove(){
		handler.getWorld().playerLocation[xCoord][yCoord]=false;
		int x = xCoord;
		int y = yCoord;
		switch (direction){
		case "Left":
			if(xCoord==0){
				xCoord=handler.getWorld().GridWidthHeightPixelCount-1;
			}else{
				xCoord--;
			}
			break;
		case "Right":
			if(xCoord==handler.getWorld().GridWidthHeightPixelCount-1){
				xCoord=0;
			}else{
				xCoord++;
			}
			break;
		case "Up":
			if(yCoord==0){
				yCoord=handler.getWorld().GridWidthHeightPixelCount-1;
			}else{
				yCoord--;
			}
			break;
		case "Down":
			if(yCoord==handler.getWorld().GridWidthHeightPixelCount-1){
				yCoord=0;
			}else{
				yCoord++;
			}
			break;
		}
		handler.getWorld().playerLocation[xCoord][yCoord]=true;


		if(handler.getWorld().appleLocation[xCoord][yCoord]){
			Eat();
		}
		
		if(handler.getWorld().badappleLocation[xCoord][yCoord]){ //Bad apple eat function
			badEat();
		}
		
		

		for (int  i = 0; i < lenght-1; i++) { //Check collision with all tails; triggers game over if collided with one tail
			if (handler.getWorld().body.get(i).x == xCoord && handler.getWorld().body.get(i).y == yCoord ) {
				 State.setState(handler.getGame().gameoverState);
			}
		}

		if(!handler.getWorld().body.isEmpty()) {
			handler.getWorld().playerLocation[handler.getWorld().body.getLast().x][handler.getWorld().body.getLast().y] = false;
			handler.getWorld().body.removeLast();
			handler.getWorld().body.addFirst(new Tail(x, y,handler));
		}

	}

	public void render(Graphics g,Boolean[][] playeLocation){
		Random r = new Random();
		for (int i = 0; i < handler.getWorld().GridWidthHeightPixelCount; i++) { 
			for (int j = 0; j < handler.getWorld().GridWidthHeightPixelCount; j++) {
				
				g.setColor(Color.RED); //Coloring Good Apples
				if(playeLocation[i][j]||handler.getWorld().appleLocation[i][j]){
					g.fillRect((i*handler.getWorld().GridPixelsize),
							(j*handler.getWorld().GridPixelsize),
							handler.getWorld().GridPixelsize,
							handler.getWorld().GridPixelsize);

				}

				g.setColor(Color.ORANGE); //Coloring Bad Apples
				if(playeLocation[i][j]||handler.getWorld().badappleLocation[i][j]){
					g.fillRect((i*handler.getWorld().GridPixelsize),
							(j*handler.getWorld().GridPixelsize),
							handler.getWorld().GridPixelsize,
							handler.getWorld().GridPixelsize);

				}
				
				g.setColor(Color.GREEN); //Coloring Player
				if(playeLocation[i][j]){
					g.fillRect((i*handler.getWorld().GridPixelsize),
							(j*handler.getWorld().GridPixelsize),
							handler.getWorld().GridPixelsize,
							handler.getWorld().GridPixelsize);
				}	
			}
		}
	}
	
	

	public void Eat(){
		stepCounter=0;
		if(speed > 3) {
			speed--;
		}
		score += Math.sqrt(2*score+1);
		lenght++;
		Tail tail= null;
		handler.getWorld().appleLocation[xCoord][yCoord]=false;
		handler.getWorld().appleOnBoard=false;
		switch (direction){
		case "Left":
			if( handler.getWorld().body.isEmpty()){
				if(this.xCoord!=handler.getWorld().GridWidthHeightPixelCount-1){
					tail = new Tail(this.xCoord+1,this.yCoord,handler);
				}else{
					if(this.yCoord!=0){
						tail = new Tail(this.xCoord,this.yCoord-1,handler);
					}else{
						tail =new Tail(this.xCoord,this.yCoord+1,handler);
					}
				}
			}else{
				if(handler.getWorld().body.getLast().x!=handler.getWorld().GridWidthHeightPixelCount-1){
					tail=new Tail(handler.getWorld().body.getLast().x+1,this.yCoord,handler);
				}else{
					if(handler.getWorld().body.getLast().y!=0){
						tail=new Tail(handler.getWorld().body.getLast().x,this.yCoord-1,handler);
					}else{
						tail=new Tail(handler.getWorld().body.getLast().x,this.yCoord+1,handler);

					}
				}

			}
			break;
		case "Right":
			if( handler.getWorld().body.isEmpty()){
				if(this.xCoord!=0){
					tail=new Tail(this.xCoord-1,this.yCoord,handler);
				}else{
					if(this.yCoord!=0){
						tail=new Tail(this.xCoord,this.yCoord-1,handler);
					}else{
						tail=new Tail(this.xCoord,this.yCoord+1,handler);
					}
				}
			}else{
				if(handler.getWorld().body.getLast().x!=0){
					tail=(new Tail(handler.getWorld().body.getLast().x-1,this.yCoord,handler));
				}else{
					if(handler.getWorld().body.getLast().y!=0){
						tail=(new Tail(handler.getWorld().body.getLast().x,this.yCoord-1,handler));
					}else{
						tail=(new Tail(handler.getWorld().body.getLast().x,this.yCoord+1,handler));
					}
				}

			}
			break;
		case "Up":
			if( handler.getWorld().body.isEmpty()){
				if(this.yCoord!=handler.getWorld().GridWidthHeightPixelCount-1){
					tail=(new Tail(this.xCoord,this.yCoord+1,handler));
				}else{
					if(this.xCoord!=0){
						tail=(new Tail(this.xCoord-1,this.yCoord,handler));
					}else{
						tail=(new Tail(this.xCoord+1,this.yCoord,handler));
					}
				}
			}else{
				if(handler.getWorld().body.getLast().y!=handler.getWorld().GridWidthHeightPixelCount-1){
					tail=(new Tail(handler.getWorld().body.getLast().x,this.yCoord+1,handler));
				}else{
					if(handler.getWorld().body.getLast().x!=0){
						tail=(new Tail(handler.getWorld().body.getLast().x-1,this.yCoord,handler));
					}else{
						tail=(new Tail(handler.getWorld().body.getLast().x+1,this.yCoord,handler));
					}
				}

			}
			break;
		case "Down":
			if( handler.getWorld().body.isEmpty()){
				if(this.yCoord!=0){
					tail=(new Tail(this.xCoord,this.yCoord-1,handler));
				}else{
					if(this.xCoord!=0){
						tail=(new Tail(this.xCoord-1,this.yCoord,handler));
					}else{
						tail=(new Tail(this.xCoord+1,this.yCoord,handler));
					} System.out.println("Tu biscochito");
				}
			}else{
				if(handler.getWorld().body.getLast().y!=0){
					tail=(new Tail(handler.getWorld().body.getLast().x,this.yCoord-1,handler));
				}else{
					if(handler.getWorld().body.getLast().x!=0){
						tail=(new Tail(handler.getWorld().body.getLast().x-1,this.yCoord,handler));
					}else{
						tail=(new Tail(handler.getWorld().body.getLast().x+1,this.yCoord,handler));
					}
				}

			}
			break;
		}
		handler.getWorld().body.addLast(tail);
		handler.getWorld().playerLocation[tail.x][tail.y] = true;
	}

	public void EatDebug(){
		score += Math.sqrt(2*score+1);
		lenght++;
		Tail tail= null;
		switch (direction){
		case "Left":
			if( handler.getWorld().body.isEmpty()){
				if(this.xCoord!=handler.getWorld().GridWidthHeightPixelCount-1){
					tail = new Tail(this.xCoord+1,this.yCoord,handler);
				}else{
					if(this.yCoord!=0){
						tail = new Tail(this.xCoord,this.yCoord-1,handler);
					}else{
						tail =new Tail(this.xCoord,this.yCoord+1,handler);
					}
				}
			}else{
				if(handler.getWorld().body.getLast().x!=handler.getWorld().GridWidthHeightPixelCount-1){
					tail=new Tail(handler.getWorld().body.getLast().x+1,this.yCoord,handler);
				}else{
					if(handler.getWorld().body.getLast().y!=0){
						tail=new Tail(handler.getWorld().body.getLast().x,this.yCoord-1,handler);
					}else{
						tail=new Tail(handler.getWorld().body.getLast().x,this.yCoord+1,handler);

					}
				}

			}
			break;
		case "Right":
			if( handler.getWorld().body.isEmpty()){
				if(this.xCoord!=0){
					tail=new Tail(this.xCoord-1,this.yCoord,handler);
				}else{
					if(this.yCoord!=0){
						tail=new Tail(this.xCoord,this.yCoord-1,handler);
					}else{
						tail=new Tail(this.xCoord,this.yCoord+1,handler);
					}
				}
			}else{
				if(handler.getWorld().body.getLast().x!=0){
					tail=(new Tail(handler.getWorld().body.getLast().x-1,this.yCoord,handler));
				}else{
					if(handler.getWorld().body.getLast().y!=0){
						tail=(new Tail(handler.getWorld().body.getLast().x,this.yCoord-1,handler));
					}else{
						tail=(new Tail(handler.getWorld().body.getLast().x,this.yCoord+1,handler));
					}
				}

			}
			break;
		case "Up":
			if( handler.getWorld().body.isEmpty()){
				if(this.yCoord!=handler.getWorld().GridWidthHeightPixelCount-1){
					tail=(new Tail(this.xCoord,this.yCoord+1,handler));
				}else{
					if(this.xCoord!=0){
						tail=(new Tail(this.xCoord-1,this.yCoord,handler));
					}else{
						tail=(new Tail(this.xCoord+1,this.yCoord,handler));
					}
				}
			}else{
				if(handler.getWorld().body.getLast().y!=handler.getWorld().GridWidthHeightPixelCount-1){
					tail=(new Tail(handler.getWorld().body.getLast().x,this.yCoord+1,handler));
				}else{
					if(handler.getWorld().body.getLast().x!=0){
						tail=(new Tail(handler.getWorld().body.getLast().x-1,this.yCoord,handler));
					}else{
						tail=(new Tail(handler.getWorld().body.getLast().x+1,this.yCoord,handler));
					}
				}

			}
			break;
		case "Down":
			if( handler.getWorld().body.isEmpty()){
				if(this.yCoord!=0){
					tail=(new Tail(this.xCoord,this.yCoord-1,handler));
				}else{
					if(this.xCoord!=0){
						tail=(new Tail(this.xCoord-1,this.yCoord,handler));
					}else{
						tail=(new Tail(this.xCoord+1,this.yCoord,handler));
					} System.out.println("Tu biscochito");
				}
			}else{
				if(handler.getWorld().body.getLast().y!=0){
					tail=(new Tail(handler.getWorld().body.getLast().x,this.yCoord-1,handler));
				}else{
					if(handler.getWorld().body.getLast().x!=0){
						tail=(new Tail(handler.getWorld().body.getLast().x-1,this.yCoord,handler));
					}else{
						tail=(new Tail(handler.getWorld().body.getLast().x+1,this.yCoord,handler));
					}
				}

			}
			break;
		}
		handler.getWorld().body.addLast(tail);
		handler.getWorld().playerLocation[tail.x][tail.y] = true;
	}
	
	public void badEat() { //Eating a bad apple 
		stepCounter=0;
		handler.getWorld().badappleLocation[xCoord][yCoord]=false;
		if(lenght >1) {
			lenght--;
			if (score > 0) {
				score -= Math.sqrt(2*score+1);
			}
			if(score < 0) {
				score = 0;
			}
			handler.getWorld().playerLocation[handler.getWorld().body.getLast().x][handler.getWorld().body.getLast().y] = false;
			handler.getWorld().body.removeLast();
			}
			else { //Eating a bad apple with no tail ends game
			State.setState(handler.getGame().gameoverState);
			}
	}

	public void kill(){
		lenght = 0;
		for (int i = 0; i < handler.getWorld().GridWidthHeightPixelCount; i++) {
			for (int j = 0; j < handler.getWorld().GridWidthHeightPixelCount; j++) {

				handler.getWorld().playerLocation[i][j]=false;

			}
		}
	}

	public boolean isJustAte() {
		return justAte;
	}

	public void setJustAte(boolean justAte) {
		this.justAte = justAte;
	}
}
