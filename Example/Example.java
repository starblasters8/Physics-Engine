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
	private Random rand; // Random object used to generate random speeds and positions for the ActiveBalls

	private Point mousePos; // The position of the mouse
	private boolean controls = true; // Whether or not the controls are displayed

	// Constructor
	public Example(int w, int h)
	{
		this.setPreferredSize(new Dimension(w,h));
        rand = new Random();

		int speedMS = 10; // Speed of the timer in milliseconds
		// Create a timer that will call the repaint method every (speedMS) milliseconds
		timer = new Timer(speedMS, new repaintListener());

		// Create the balls randomly
		balls = new ActiveBall[numBalls];
		for (int i = 0; i < numBalls; i++) 
		{
			double x = rand.nextInt(w/2) + w/4;
			double y = rand.nextInt(h/2) + h/4;
			double dx = rand.nextDouble()*speedDiff - speedDiff/2;
			double dy = rand.nextDouble()*speedDiff - speedDiff/2;
			balls[i] = new ActiveBall(x, y, dx, dy, w, h);
		}

		this.addMouseMotionListener(new Mouse());
		this.addKeyListener(new Keys());
		this.setFocusable(true);
		
		// Start the timer and set the background to black
		timer.start();
		setBackground(Color.BLACK);
	}

	public void paintComponent(Graphics g) // Paints the screen
	{
		super.paintComponent(g);

		for(ActiveBall ball : balls) // Draws the ActiveBalls
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

		g.setColor(Color.WHITE);
		g.drawString("Press C to toggle the controls/statistics ", 0, 15);
		if(controls)
		{
			g.drawString("Number of ActiveBalls: " + numBalls, 0, 30);
			g.drawString("Press Q to regenerate the ActiveBalls (at your mouse position)", 0, 50);
			g.drawString("Press W to clear all ActivaBalls from screen", 0, 65);
			g.drawString("Press E to stop all the ActiveBalls", 0, 80);
			g.drawString("Press R to increase the number of ActiveBalls by 10", 0, 95);
			g.drawString("Press T to decrease the number of ActiveBalls by 10", 0, 110);
			g.drawString("Press Y to regenerate all ActiveBalls speeds", 0, 125);
			g.drawString("Press U to double the speed of all the ActiveBalls", 0, 140);
		}

	}

	public void regenBalls() // Regenerates the ActiveBalls with random speeds and positions
	{
		for (int i = 0; i < numBalls; i++) 
		{
			double x = rand.nextInt(this.getWidth()/2) + this.getWidth()/4;
			double y = rand.nextInt(this.getHeight()/2) + this.getHeight()/4;
			double dx = rand.nextDouble()*speedDiff - speedDiff/2;
			double dy = rand.nextDouble()*speedDiff - speedDiff/2;
			balls[i] = new ActiveBall(x, y, dx, dy, this.getWidth(), this.getHeight());
		}
	}

	public void regenAtPoint(int x, int y) // Regenerates the ActiveBalls with random speeds and positions around the point (x,y)
	{
		for (int i = 0; i < numBalls; i++) 
		{
			double dx = rand.nextDouble()*speedDiff - speedDiff/2;
			double dy = rand.nextDouble()*speedDiff - speedDiff/2;
			balls[i] = new ActiveBall(x, y, dx, dy, this.getWidth(), this.getHeight());
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

	private class Mouse implements MouseMotionListener // Listens for mouse movement and saves the position of the mouse
	{
		public void mouseDragged(MouseEvent e) {}

		public void mouseMoved(MouseEvent e) 
		{
			mousePos = e.getPoint();
		}
	}

	private class Keys implements KeyListener // Listens for key presses
	{
		public void keyTyped(KeyEvent e){}

		public void keyPressed(KeyEvent e)
		{
			if(e.getKeyCode() == KeyEvent.VK_Q) // If the Q key is pressed, regenerate the ActiveBalls
			{
				if(mousePos != null)
					regenAtPoint(mousePos.x, mousePos.y);
				else
					regenBalls();
			}

			if(e.getKeyCode() == KeyEvent.VK_E) // If the E key is pressed, stop all the ActiveBalls
			{
				for(ActiveBall ball : balls)
				{
					ball.setDx(0);
					ball.setDy(0);
				}
			}

			if(e.getKeyCode() == KeyEvent.VK_U) // If the U key is pressed, double the speed of all the ActiveBalls
			{
				for(ActiveBall ball : balls)
				{
					ball.setDx(ball.getDx() * 2);
					ball.setDy(ball.getDy() * 2);
				}
			}

			if(e.getKeyCode() == KeyEvent.VK_Y) // If the Y key is pressed, regenerate all the ActiveBalls with random x and y speeds
			{
				for(ActiveBall ball : balls)
					ball.regenVelocity(speedDiff);
			}

			if(e.getKeyCode() == KeyEvent.VK_R) // If the R key is pressed, add 10 more balls to the screen at the mouse position
			{
				if(mousePos != null)
				{
					numBalls += 10;
					ActiveBall[] newBalls = new ActiveBall[numBalls];
					for(int i = 0; i < balls.length; i++)
						newBalls[i] = balls[i];
					for(int i = balls.length; i < newBalls.length; i++)
					{
						double dx = rand.nextDouble()*speedDiff - speedDiff/2;
						double dy = rand.nextDouble()*speedDiff - speedDiff/2;
						newBalls[i] = new ActiveBall(mousePos.x, mousePos.y, dx, dy, getWidth(), getHeight());
					}
					balls = newBalls;
				}
			}

			if(e.getKeyCode() == KeyEvent.VK_T) // If the T key is pressed, remove 10 balls from the screen
			{
				if(numBalls >= 10)
				{
					numBalls -= 10;
					ActiveBall[] newBalls = new ActiveBall[numBalls];
					for(int i = 0; i < newBalls.length; i++)
						newBalls[i] = balls[i];
					balls = newBalls;
				}
			}

			if(e.getKeyCode() == KeyEvent.VK_W) // If the W key is pressed, clear all balls from the screen
			{
				numBalls = 0;
				balls = new ActiveBall[0];
			}

			if(e.getKeyCode() == KeyEvent.VK_C) // If the C key is pressed, toggle the controls
			{
				controls = !controls;
			}
		}

		public void keyReleased(KeyEvent e){}
		
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