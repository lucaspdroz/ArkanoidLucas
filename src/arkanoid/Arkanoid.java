package arkanoid;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.JOptionPane;

import com.senac.SimpleJava.Console;
import com.senac.SimpleJava.Graphics.Canvas;
import com.senac.SimpleJava.Graphics.Color;
import com.senac.SimpleJava.Graphics.GraphicApplication;
import com.senac.SimpleJava.Graphics.Image;
import com.senac.SimpleJava.Graphics.Point;
import com.senac.SimpleJava.Graphics.Resolution;
import com.senac.SimpleJava.Graphics.Sprite;
import com.senac.SimpleJava.Graphics.events.KeyboardAction;

public class Arkanoid extends GraphicApplication {

	private Bloco blocos[] = new Bloco[78];	
	private Paddle paddle;
	private Ball ball;
	private int deltaY = -1;
	private int deltaX = 1;
	private Image imagem;
	private int score = 0;
	private int stage = 0;
	


	private Color[] coresStage = { 
			new Color(146,145,153),
			new Color(64,54,1),
			new Color(90,79,0),
			new Color(163,142,1),
			new Color(166,145,0),
			new Color(243,207,1) 
	};

	
	@Override
	protected void draw(Canvas canvas) {
		canvas.clear();
		canvas.drawImage(imagem,0,0);		
		drawBlocosStage(canvas);				
		ball.draw(canvas);		
		putTexts(canvas);				
		paddle.draw(canvas);
	}	

	@Override
	protected void setup() {
		this.setFramesPerSecond(60);
		this.setResolution(Resolution.MSX);
		
		try {
			imagem = new Image("images/download.jpg");
		} catch (IOException e) {
			e.printStackTrace(System.err);
		}
		
		ball = new Ball();
		ball.setPosition(130,180);
		
		paddle = new Paddle();
		paddle.setPosition(100,185);
		
		setBlocosStage();
				
		bindKeyPressed("LEFT", new KeyboardAction() {
			@Override			
			public void handleEvent() {
				if(paddle.PosxLimiteMinimo())
					paddle.setPosition(0,185);					
				else
					paddle.move(-5, 0);				
			}
		});
		bindKeyPressed("RIGHT", new KeyboardAction() {
			@Override
			public void handleEvent() {
				if(paddle.PosxLimiteMaximo((getResolution().width-20)))
					paddle.setPosition((getResolution().width-20),185);					
				else
					paddle.move(5, 0);
			}
		});
		bindKeyPressed("SPACE", new KeyboardAction() {
			@Override
			public void handleEvent() {
				for (int i = 0; i < blocos.length; i++) {
					if(blocos[i].isAlive()){
						blocos[i].Die();
						score = score + 100;
					}
				}
			}
		});
	}	

	@Override
	protected void loop() {
		verifyStage();
		if(ball.getLifes() == 0){
			JOptionPane.showMessageDialog(null,
				    "Game Over\n",
				    "Arkanoid",
				    JOptionPane.WARNING_MESSAGE);	
			System.exit(0);
		}
		//Testando os limites do eixo X e Y.
		Point pos = ball.getPosition();
		if (testeLimite(pos.y,0,getResolution().height)) {
			deltaY *= -1;
		}
		if (testeLimite(pos.x,0,getResolution().width)) {
			deltaX *= -1;
		}
		if(ball.getLifes() == 0){
			
		}
		if(ball.isDead(getResolution().height)){
			ball.DieInsect();
			ball.setPosition(130,180);
		}
			
		int bateu = 0;
		for (int i = 0; i < blocos.length; i++) {
			if (blocos[i].bateu(ball)) {
				Console.println("Bateu:" + i);
				bateu++;
				score += 100;
			}
		}
		if(bateu>0)
			deltaY *= -1;
		
		if (paddle.bateu(ball)) {
			Console.println("Bateu no paddle");
			deltaY *= -1;
		}
		
		ball.move(deltaX, deltaY);
		
		redraw();			
		
	}	

	private void drawBlocosStage(Canvas canvas) {
		for (int i = 0; i < blocos.length; i++) {
			blocos[i].draw(canvas);
			blocos[i].draw(canvas);
		}		
	}
	
	private void putTexts(Canvas canvas) {
		canvas.putText(0, 0, 10, "Lifes :");
		canvas.putText(30, 0, 10, ""+ball.getLifes());
		canvas.putText(60, 0, 10, "Stage :");
		canvas.putText(100, 0, 10, ""+(stage+1));
		canvas.putText(150, 0, 10, "Score :");
		canvas.putText(210, 0, 10, ""+score);			
	}
	
	private void forBlocos(Color[] cores) {
		int k = 0;
		int posBlocox = 15;
		int posBlocoy = 0;
		for (int i = 0; i <= 5; i++){
			for (int j = 0; j < 13; j++) {
				if(i == 0){
					blocos[k] = new Bloco(cores[i],2);
				}else{
					blocos[k] = new Bloco(cores[i]);
				}
				blocos[k].setPosition(posBlocoy,posBlocox);				
				k++;
				posBlocoy = posBlocoy + 20;
			}
			posBlocoy = 0;
			posBlocox = posBlocox + 12;
		}
	}
	
	private void setBlocosStage() {
		forBlocos(coresStage);	
	}	
	
	private void verifyStage() {
		for (int i = 0; i < blocos.length; i++) {
			if (blocos[i].isAlive()) {
				return;
			}
		}
		stageAdvance();
	}
	
	private void stageAdvance() {
		stage = stage + 1;
		ball.setPosition(130,180);
		
		if(stage == 1){
			shuffleArray(coresStage);
			try {
				imagem = new Image("images/Backgorund.jpg");
			} catch (IOException e) {
				e.printStackTrace(System.err);
			}
			setBlocosStage();	
		}
		if(stage == 2){
			shuffleArray(coresStage);
			setBlocosStage();
			try {
				imagem = new Image("images/asd.jpg");
			} catch (IOException e) {
				e.printStackTrace(System.err);
			}
		}
		if(stage == 3){
			shuffleArray(coresStage);
			setBlocosStage();
			try {
				imagem = new Image("images/a.jpg");
			} catch (IOException e) {
				e.printStackTrace(System.err);
			}
		}
		
		if(stage == 4){
			shuffleArray(coresStage);
			setBlocosStage();	
		}
		
		if(stage > 0 && stage >= 4){
			JOptionPane.showMessageDialog(null,
				    "You Win the ARKANOID GAME!\n",
				    "Arkanoid",
				    JOptionPane.INFORMATION_MESSAGE);	
			System.exit(0);
		}
		
	}
	
	static void shuffleArray(Color[] ar){
		Random rnd = ThreadLocalRandom.current();
		for (int i = ar.length - 1; i > 0; i--){
		  int index = rnd.nextInt(i + 1);
		      Color a = ar[index];
		      ar[index] = ar[i];
		      ar[i] = a;
		    }
	  }
	
	private boolean testeLimite(double pos, int min, int max) {
		if(pos > max || pos < min) {
			return true;
		} else {
			return false;
		}
	}
	
}
