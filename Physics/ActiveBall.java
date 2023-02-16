package Physics;

import java.awt.*;
import java.util.Random;

public class ActiveBall {
    private double x; // x-coordinate of the ActiveBall
    private double y; // y-coordinate of the ActiveBall
    private double dx; // x velocity of the ActiveBall
    private double dy; // y velocity of the ActiveBall
    private int w, h; // Width and height of the panel
    private double radius = 25; // Radius of the ActiveBall
    private double mass; // Mass of the ActiveBall
    private Color col = Color.WHITE; // Default color of ActiveBall
    private Random rand; // Random object for randomizing ActiveBall properties
    private double coFric = 0.06, gravity = 9.806; // Coefficient of friction and gravity constant

    public ActiveBall(double x, double y, double dx, double dy, int w, int h) 
    {
        rand = new Random();
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
        this.w = w;
        this.h = h;

        // Randomize the mass of the ActiveBall between (min) and (max)
        int min = 156;
        int max = 170;
        this.mass = rand.nextDouble(max-min) + min;
    }

    public void draw(Graphics g) // Draws the ActiveBall
    {
        updatePosition(); // Updates position of ActiveBall
        g.fillOval((int)this.x, (int)this.y, (int)this.radius*2, (int)this.radius*2); // Draws the ActiveBall at it's given place
    }

    
    public boolean collidesWith(ActiveBall other) // Checks if the ActiveBall collides with another ActiveBall (other)
    {
        double distance = this.getLocation().distance(other.getLocation());
        return distance <= this.radius + other.radius;
    }

    public boolean collidesWithWall() // Checks if the ActiveBall collides with the wall
    {
        return this.x + this.dx <= 0 || this.x + this.dx >= this.w-this.radius*2 || this.y + this.dy <= 0 || this.y + this.dy >= this.h-this.radius*2;
    }

    public void collideWall() // Responds to collision with wall
    {
        if(this.x + this.dx <= 0 || this.x + this.dx >= this.w-this.radius*2)
            this.dx *= -1;
        if(this.y + this.dy <= 0 || this.y + this.dy >= this.h-this.radius*2)
            this.dy *= -1;

        // Randomizes color of ActiveBall
        randColor();
    }
    
    public void updatePosition() // Updates the position of the ActiveBall and account for friction
    {
        double frictionForce = this.gravity * this.coFric; // Friction force
        double frictionVelocityX = (frictionForce / this.mass) * Math.abs(this.dx); // Friction velocity in the x direction
        double frictionVelocityY = (frictionForce / this.mass) * Math.abs(this.dy); // Friction velocity in the y direction

        this.dx += (this.dx != 0)? ((this.dx > 0) ? -frictionVelocityX : frictionVelocityX) : 0; // Updates the x velocity of the ActiveBall
        this.dy += (this.dy != 0)? ((this.dy > 0) ? -frictionVelocityY : frictionVelocityY) : 0; // Updates the y velocity of the ActiveBall
        
        double minSpeed = 0.01; // minimum speed of the ActiveBall (to prevent it from moving forever)
        this.dx = (Math.abs(this.dx) < minSpeed)? 0 : this.dx; // Sets the x velocity to 0 if it is less than the minimum speed
        this.dy = (Math.abs(this.dy) < minSpeed)? 0 : this.dy; // Sets the y velocity to 0 if it is less than the minimum speed

        this.x += this.dx; // Updates the x position of the ActiveBall
        this.y += this.dy; // Updates the y position of the ActiveBall
    }
    
    public void collide(ActiveBall other) // Responds to collision with another ActiveBall (other)
    {
        double distanceX = other.x - this.x; // Distance between the x coordinates of the ActiveBalls
        double distanceY = other.y - this.y; // Distance between the y coordinates of the ActiveBalls

        // Calculate the angle between the ActiveBall's centers
        double angle = Math.atan2(distanceY, distanceX);

        // Calculate the magnitude of each ActiveBall's velocity vector
        double velocity1 = Math.sqrt(Math.pow(this.dx, 2) + Math.pow(this.dy, 2));
        double velocity2 = Math.sqrt(Math.pow(other.dx, 2) + Math.pow(other.dy, 2));

        // Calculate the direction of each ActiveBall's velocity vector
        double direction1 = Math.atan2(this.dy, this.dx);
        double direction2 = Math.atan2(other.dy, other.dx);

        // Calculate the velocity components of each ActiveBall in the direction of the collision
        double velocityX1 = velocity1 * Math.cos(direction1 - angle);
        double velocityY1 = velocity1 * Math.sin(direction1 - angle);
        double velocityX2 = velocity2 * Math.cos(direction2 - angle);
        double velocityY2 = velocity2 * Math.sin(direction2 - angle);

        // Calculate the new velocity components of each ActiveBall after the collision
        double newVelocityX1 = ((velocityX1 * (this.mass - other.mass)) + ((2 * other.mass) * velocityX2)) / (this.mass + other.mass);
        double newVelocityX2 = ((velocityX2 * (other.mass - this.mass)) + ((2 * this.mass) * velocityX1)) / (this.mass + other.mass);
        double newVelocityY1 = velocityY1;
        double newVelocityY2 = velocityY2;

        // Update the velocities of the balls
        this.dx = Math.cos(angle) * newVelocityX1 + Math.cos(angle + Math.PI / 2) * newVelocityY1;
        this.dy = Math.sin(angle) * newVelocityX1 + Math.sin(angle + Math.PI / 2) * newVelocityY1;
        other.dx = Math.cos(angle) * newVelocityX2 + Math.cos(angle + Math.PI / 2) * newVelocityY2;
        other.dy = Math.sin(angle) * newVelocityX2 + Math.sin(angle + Math.PI / 2) * newVelocityY2;

        // Move the balls so they are no longer intersecting
        double overlap = this.radius + other.radius - this.getLocation().distance(other.getLocation());
        this.x -= overlap / 2 * Math.cos(angle);
        this.y -= overlap / 2 * Math.sin(angle);
        other.x += overlap / 2 * Math.cos(angle);
        other.y += overlap / 2 * Math.sin(angle);

        // Randomize the color of the ActiveBall
        randColor();
        other.randColor();
    }

    public Point getLocation() // Returns the location of the ActiveBall as a Point
    {
        return new Point((int)this.x, (int)this.y);
    }

    public void setMass(double mass) // Sets the mass of the ActiveBall
    {
        this.mass = mass;
    }

    public Color getColor() // Returns the color of the ActiveBall
    {
        return this.col;
    }

    public void randColor() // Randomizes the color of the ActiveBall
    {
        this.col = new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255));
    }

}