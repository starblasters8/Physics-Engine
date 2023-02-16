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

	private int numBalls = 30; //Number of balls to be created
    private double speedDiff = 20; // half of this is the max speed in either direction of each ball on creation

	public Example(int w, int h)
	{
		this.setPreferredSize(new Dimension(w,h));
        Random rand = new Random();

		//Create a timer that will call the repaint method every 10 milliseconds
		timer = new Timer(10, new repaintListener());

		//Create the balls randomly
		balls = new ActiveBall[numBalls];
		for (int i = 0; i < numBalls; i++) 
		{
			double x = rand.nextInt(w/2) + w/4;
			double y = rand.nextInt(h/2) + h/4;
			double dx = rand.nextDouble(speedDiff) - speedDiff/2;
			double dy = rand.nextDouble(speedDiff) - speedDiff/2;
			balls[i] = new ActiveBall(x, y, dx, dy, w, h);
		}

		//Start the timer and set the background to black
		timer.start();
		setBackground(Color.BLACK);
	}

	private class repaintListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e) 
		{
			//Call the repaint method to redraw the screen
			repaint();
		}
	}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		for (ActiveBall ball : balls) 
		{
			g.setColor(ball.getColor());
			ball.draw(g);
		}

		for (int i = 0; i < balls.length; i++) 
		{
			ActiveBall b1 = balls[i];
			for (int j = i + 1; j < balls.length; j++) 
			{
				ActiveBall b2 = balls[j];
				if (b1.collidesWith(b2))
					b1.collide(b2);
			}
		}

		for (ActiveBall ball : balls)
		{
			if(ball.collidesWithWall())
				ball.collideWall();
			ball.updatePosition();
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