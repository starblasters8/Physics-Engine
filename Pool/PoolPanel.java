package Pool;

import Physics.ActiveBall;

import java.awt.*;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.event.*;

public class PoolPanel extends JPanel
{
	private Timer timer;
	private ActiveBall[] balls;

	private int radius = 15; // Radius of the ActiveBalls
	private int numBalls = 16; // Number of ActiveBalls to be created
	private int offsetY, offsetX; // Offset of the ActiveBalls
	private int tableW, tableH; // Size of the table

	// Constructor
	public PoolPanel(int w, int h)
	{
		this.setPreferredSize(new Dimension(w,h));

		int speedMS = 10; // Speed of the timer in milliseconds
		// Create a timer that will call the repaint method every (speedMS) milliseconds
		timer = new Timer(speedMS, new repaintListener());
		
		offsetX = 250; // Offset of the ActiveBalls from the left side of the window
		offsetY = 50; // Offset of the ActiveBalls from the top of the window
		
		this.tableW = w; // Width of the table
		this.tableH = h; // Height of the table

		balls = new ActiveBall[numBalls]; // Creates an array of ActiveBalls
		for(int i = 0; i < 5; i++) // Creates the first set of ActiveBalls
			balls[i] = new ActiveBall((i*radius+(i*(radius+5)))+offsetX, offsetY, this.tableW, this.tableH, radius, (int)((0.5*Math.pow(i, 2))+(-5.5*i)+15)); // Formula: 0.5x^2 - 5.5x + 15
		for(int i = 0; i < 4; i++) // Creates the second set of ActiveBalls
			balls[i+5] = new ActiveBall((i*radius+(i*(radius+5)))+radius+offsetX, (radius*2)+offsetY, this.tableW, this.tableH, radius, (int)((0.5*Math.pow(i, 2))+(-5.5*i)+14)); // Formula: 0.5x^2 - 5.5x + 14
		for(int i = 0; i < 3; i++) // Creates the third set of ActiveBalls
			balls[i+9] = new ActiveBall((i*radius+(i*(radius+5)))+(radius*2)+offsetX, (radius*4)+offsetY, this.tableW, this.tableH, radius, (int)((0.5*Math.pow(i, 2))+(-5.5*i)+13)); // Formula: 0.5x^2 - 5.5x + 13
		for(int i = 0; i < 2; i++) // Created the fourth et of ActiveBalls
			balls[i+12] = new ActiveBall((i*radius+(i*(radius+5)))+(radius*3)+offsetX, (radius*6)+offsetY, this.tableW, this.tableH, radius, (int)((0.5*Math.pow(i, 2))+(-5.5*i)+12)); // Formula: 0.5x^2 - 5.5x + 12
		for(int i = 0; i < 1; i++) // Creates the fifth set of ActiveBalls
			balls[i+14] = new ActiveBall((i*radius+(i*(radius+5)))+(radius*4)+offsetX, (radius*8)+offsetY, this.tableW, this.tableH, radius, (int)((0.5*Math.pow(i, 2))+(-5.5*i)+11)); // Formula: 0.5x^2 - 5.5x + 11

		balls[15] = new ActiveBall((radius*4) + offsetX, (radius*25)+offsetY, this.tableW, this.tableH, radius, 0); // Creates the cue ball

		for(ActiveBall ball : balls)
			ball.setColorFromID();

		// Adds user control
		this.addKeyListener(new Keys());
		this.setFocusable(true);

		// Start the timer and set the background
		timer.start();
		setBackground(new Color(1, 40, 26));
	}

	public void paintComponent(Graphics g) // Paints the screen
	{
		super.paintComponent(g);

		for(ActiveBall ball : balls) // Draws the ActiveBalls
		{
			g.setColor(ball.getColor());
			ball.draw(g);
		}

		for (int i = 0; i < balls.length; i++)  // Checks for collision between all ActiveBalls and responds appropriately
		{
			ActiveBall b1 = balls[i];
			for (int j = i + 1; j < balls.length; j++) 
			{
				ActiveBall b2 = balls[j];
				if (b1.collidesWith(b2))
					b1.collide(b2);
			}
		}

		for (ActiveBall ball : balls) // Checks for collision with wall on all ActiveBalls and responds appropriately
		{
			if(ball.collidesWithWall())
				ball.collideWall();
		}
	}


	// Called for every tick of the timer (timer)
	private class repaintListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e) 
		{
			// Call the repaint method to redraw the screen
			repaint();
		}
	}

	private class Keys implements KeyListener
	{
		public void keyPressed(KeyEvent e)
		{
			int speed = 1;
			if(e.getKeyCode() == KeyEvent.VK_UP)
				balls[15].setDy(-speed+balls[15].getDy());
			if(e.getKeyCode() == KeyEvent.VK_DOWN)
				balls[15].setDy(speed+balls[15].getDy());
			if(e.getKeyCode() == KeyEvent.VK_LEFT)
				balls[15].setDx(-speed+balls[15].getDx());
			if(e.getKeyCode() == KeyEvent.VK_RIGHT)
				balls[15].setDx(speed+balls[15].getDx());
			if(e.getKeyCode() == KeyEvent.VK_SPACE)
			{
				balls[15].setDx(0);
				balls[15].setDy(0);
			}
		}

		public void keyReleased(KeyEvent e){}
		public void keyTyped(KeyEvent e){}
	}
	
    //Run me!
    public static void main(String[] args)
	{
		int w = 800; //Width of the window
		int h = 800; //Height of the window
		 
		JFrame frame = new JFrame("Pool");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(new PoolPanel(w,h));
		frame.pack();
		frame.setVisible(true);
	}
}