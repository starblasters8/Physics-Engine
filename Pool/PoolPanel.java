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
	private int numBalls = 15; // Number of ActiveBalls to be created
	private int offsetY, offsetX; // Offset of the ActiveBalls
	private int tableW, tableH; // Size of the table

	// Constructor
	public PoolPanel(int w, int h)
	{
		this.setPreferredSize(new Dimension(w,h));

		int speedMS = 10; // Speed of the timer in milliseconds
		// Create a timer that will call the repaint method every (speedMS) milliseconds
		timer = new Timer(speedMS, new repaintListener());
		
		offsetX = 50; // Offset of the ActiveBalls from the left side of the window
		offsetY = 50; // Offset of the ActiveBalls from the top of the window
		
		tableW = w; // Width of the table
		tableH = h; // Height of the table

		balls = new ActiveBall[numBalls]; // Creates an array of ActiveBalls
		for(int i = 0; i < 5; i++) // Creates the first set of ActiveBalls
			balls[i] = new ActiveBall((i*radius+(i*(radius+5))) + offsetX, offsetY, tableW, tableH, radius, (int)((0.5*Math.pow(i, 2))+(-5.5*i)+15)); // Formula: 0.5x^2 - 5.5x + 15
		for(int i = 0; i < 4; i++) // Creates the second set of ActiveBalls
			balls[i+5] = new ActiveBall((i*radius+(i*(radius+5))) + radius + offsetX, (radius*2) + offsetY, tableW, tableH, radius, (int)((0.5*Math.pow(i, 2))+(-5.5*i)+14)); // Formula: 0.5x^2 - 5.5x + 14
		for(int i = 0; i < 3; i++) // Creates the third set of ActiveBalls
			balls[i+9] = new ActiveBall((i*radius+(i*(radius+5))) + (radius*2) + offsetX, (radius*4) + offsetY, tableW, tableH, radius, (int)((0.5*Math.pow(i, 2))+(-5.5*i)+13)); // Formula: 0.5x^2 - 5.5x + 13
		for(int i = 0; i < 2; i++) // Created the fourth et of ActiveBalls
			balls[i+12] = new ActiveBall((i*radius+(i*(radius+5))) + (radius*3) + offsetX, (radius*6) + offsetY, tableW, tableH, radius, (int)((0.5*Math.pow(i, 2))+(-5.5*i)+12)); // Formula: 0.5x^2 - 5.5x + 12
		for(int i = 0; i < 1; i++) // Creates the fifth set of ActiveBalls
			balls[i+14] = new ActiveBall((i*radius+(i*(radius+5))) + (radius*4) + offsetX, (radius*8) + offsetY, tableW, tableH, radius, (int)((0.5*Math.pow(i, 2))+(-5.5*i)+11)); // Formula: 0.5x^2 - 5.5x + 11

		for(ActiveBall ball : balls)
			ball.setColorFromID();

		// Start the timer and set the background to black
		timer.start();
		setBackground(Color.BLACK);
	}

	public void paintComponent(Graphics g) // Paints the screen
	{
		super.paintComponent(g);

		for(ActiveBall ball : balls) // Draws the ActiveBallsalls
		{
			g.setColor(ball.getColor());

			// Sets the width and height of the ActiveBalls to whatever the window is (in case it changes)
			ball.setW(this.getWidth()); 
			ball.setH(this.getHeight());

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

		for (ActiveBall ball : balls) // Checks for collision with wall and all ActiveBalls and responds appropriately
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