package Physics;

import java.awt.*;
import java.util.Random;

public class ActiveBall {
    private double x; // x-coordinate of the ball
    private double y; // y-coordinate of the ball
    private double dx; // x velocity of the ball
    private double dy; // y velocity of the ball
    private int w, h; // width and height of the panel
    private double radius = 25; // radius of the ball
    private double mass; // mass of the ball
    private Color col = Color.WHITE; // default color of ball
    private Random rand; // random object for randomizing ball properties
    private double coFric = 0.06, gravity = 9.806; // coefficient of friction and gravity constant

    public ActiveBall(double x, double y, double dx, double dy, int w, int h) 
    {
        rand = new Random();
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
        this.w = w;
        this.h = h;

        this.mass = rand.nextDouble(14) + 156;
    }

    public void draw(Graphics g) 
    {
        updatePosition();
        g.fillOval((int)this.x, (int)this.y, (int)this.radius*2, (int)this.radius*2);
    }

    
    public boolean collidesWith(ActiveBall other) {
        double distance = this.getLocation().distance(other.getLocation());
        return distance <= this.radius + other.radius;
    }

    public boolean collidesWithWall()
    {
        return this.x + this.dx <= 0 || this.x + this.dx >= this.w-this.radius*2 || this.y + this.dy <= 0 || this.y + this.dy >= this.h-this.radius*2;
    }

    public void collideWall()
    {
        if(this.x + this.dx <= 0 || this.x + this.dx >= this.w-this.radius*2)
            this.dx *= -1;
        if(this.y + this.dy <= 0 || this.y + this.dy >= this.h-this.radius*2)
            this.dy *= -1;

        randColor();
    }
    
    public void updatePosition() 
    {
        double frictionForce = this.gravity * this.coFric;
        double frictionVelocityX = (frictionForce / this.mass) * Math.abs(this.dx);
        double frictionVelocityY = (frictionForce / this.mass) * Math.abs(this.dy);

        this.dx += (this.dx != 0)? ((this.dx > 0) ? -frictionVelocityX : frictionVelocityX) : 0;
        this.dy += (this.dy != 0)? ((this.dy > 0) ? -frictionVelocityY : frictionVelocityY) : 0;
        
        double minSpeed = 0.01; // minimum speed of the ball (to prevent it from moving forever)
        this.dx = (Math.abs(this.dx) < minSpeed)? 0 : this.dx;
        this.dy = (Math.abs(this.dy) < minSpeed)? 0 : this.dy;

        this.x += this.dx;
        this.y += this.dy;
    }
    
    public void collide(ActiveBall other) 
    {
        double distanceX = other.x - this.x;
        double distanceY = other.y - this.y;

        // Calculate the angle between the balls' centers
        double angle = Math.atan2(distanceY, distanceX);

        // Calculate the magnitude of each ball's velocity vector
        double velocity1 = Math.sqrt(Math.pow(this.dx, 2) + Math.pow(this.dy, 2));
        double velocity2 = Math.sqrt(Math.pow(other.dx, 2) + Math.pow(other.dy, 2));

        // Calculate the direction of each ball's velocity vector
        double direction1 = Math.atan2(this.dy, this.dx);
        double direction2 = Math.atan2(other.dy, other.dx);

        // Calculate the velocity components of each ball in the direction of the collision
        double velocityX1 = velocity1 * Math.cos(direction1 - angle);
        double velocityY1 = velocity1 * Math.sin(direction1 - angle);
        double velocityX2 = velocity2 * Math.cos(direction2 - angle);
        double velocityY2 = velocity2 * Math.sin(direction2 - angle);

        // Calculate the new velocity components of each ball after the collision
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

        randColor();
        other.randColor();
    }

    public Point getLocation()
    {
        return new Point((int)this.x, (int)this.y);
    }

    public void setMass(double mass)
    {
        this.mass = mass;
    }

    public Color getColor()
    {
        return this.col;
    }

    public void randColor()
    {
        this.col = new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255));
    }

}