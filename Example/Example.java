package Example;

import Physics.ActiveBall;

import java.awt.*;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.event.*;
import java.util.*;

public class Example extends JPanel
{
	private Timer timer;
	private ActiveBall[] balls;

	private int numBalls = 30; // Number of ActiveBalls to be created
    private double speedDiff = 20; // Half of this is the max speed in either direction of each ActiveBall on creation

	public Example(int w, int h)
	{
		this.setPreferredSize(new Dimension(w,h));
        Random rand = new Random();

		int speedMS = 10; // Speed of the timer in milliseconds
		// Create a timer that will call the repaint method every (speedMS) milliseconds
		timer = new Timer(speedMS, new repaintListener());

		// Create the balls randomly
		balls = new ActiveBall[numBalls];
		for (int i = 0; i < numBalls; i++) 
		{
			double x = rand.nextInt(w/2) + w/4;
			double y = rand.nextInt(h/2) + h/4;
			double dx = rand.nextDouble(speedDiff) - speedDiff/2;
			double dy = rand.nextDouble(speedDiff) - speedDiff/2;
			balls[i] = new ActiveBall(x, y, dx, dy, w, h);
		}

		// Start the timer and set the background to black
		timer.start();
		setBackground(Color.BLACK);
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

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		for(ActiveBall ball : balls) // draws the ActiveBallsalls
		{
			g.setColor(ball.getColor());
			ball.draw(g);
		}

		for (int i = 0; i < balls.length; i++)  // checks for collision between all ActiveBalls and responds appropriately
		{
			ActiveBall b1 = balls[i];
			for (int j = i + 1; j < balls.length; j++) 
			{
				ActiveBall b2 = balls[j];
				if (b1.collidesWith(b2))
					b1.collide(b2);
			}
		}

		for (ActiveBall ball : balls) // checks for collision with wall and all ActiveBalls and responds appropriately
		{
			if(ball.collidesWithWall())
				ball.collideWall();
		}
	}

    //Run me!
    public static void main(String[] args)
	{
		int w = 800; //Width of the window
		int h = 800; //Height of the window
		 
		JFrame frame = new JFrame("Example Of Ball Physics");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(new Example(w,h));
		frame.pack();
		frame.setVisible(true);
	}
}