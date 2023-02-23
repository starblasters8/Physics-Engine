package Physics;

import java.awt.*;
import java.util.Random;

public class ActiveBall {
    private double x; // x-coordinate of the ActiveBall
    private double y; // y-coordinate of the ActiveBall
    private double dx; // x velocity of the ActiveBall
    private double dy; // y velocity of the ActiveBall
    private int w, h; // Width and height of the panel
    private double radius; // Radius of the ActiveBall
    private double mass; // Mass of the ActiveBall
    private Color color = Color.white;; // Default color of ActiveBall
    private Random rand; // Random object for randomizing ActiveBall properties
    private double coFric = 0.06, gravity = 9.806; // Coefficient of friction and gravity constant
    private double min = 156, max = 170; // Minimum and maximum mass of the ActiveBall
    private int ID; // ID of the ActiveBall
    private boolean useID; // Decide whether to use the ID's or not

    public ActiveBall(double x, double y, double dx, double dy, int w, int h) 
    {
        rand = new Random();
        radius = 25;
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
        this.w = w;
        this.h = h;
        useID = false;

        // Randomize the mass of the ActiveBall between (min) and (max)
        this.mass = rand.nextDouble(max-min) + min;
    }

    public ActiveBall(double x, double y, int w, int h, int radius, int ID) // Start with no velocity and a set radius and ID
    {
        rand = new Random();
        this.x = x;;
        this.y = y;
        this.dx = 0;
        this.dy = 0;
        this.w = w;
        this.h = h;
        this.radius = radius;
        this.ID = ID;
        useID = true;

        // Randomize the mass of the ActiveBall between (min) and (max)
        this.mass = rand.nextDouble(max-min) + min;
    }

    public void draw(Graphics g) // Draws the ActiveBall
    {
        updatePosition(); // Updates position of ActiveBall
        g.fillOval((int)this.x, (int)this.y, (int)this.radius*2, (int)this.radius*2); // Draws the ActiveBall at its given place

        if(useID) // Draws the ball if it's a pool ball
        {
            g.setColor(Color.WHITE);
            int smallDiameter = (int) (this.radius * 2 * 0.6);
            int smallX = (int) (this.x + (this.radius - smallDiameter / 2));
            int smallY = (int) (this.y + (this.radius - smallDiameter / 2));
            g.fillOval(smallX, smallY, smallDiameter, smallDiameter);

            g.setFont(new Font(g.getFont().getFontName(), Font.BOLD, g.getFont().getSize()));
            g.setColor(Color.BLACK);
            if(ID<10)
                g.drawString(ID+"", (int)(this.x+radius-3), (int)(this.y+radius+5));
            else
                g.drawString(ID+"", (int)(this.x+radius-7), (int)(this.y+radius+5));
        }
    }

    public void setColorFromID()
    {
        switch(ID)
        {
            case 8:
                color = Color.BLACK;
                break;

            default:
                randPoolColor();
                break;
        }
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

        if(!useID) // Randomizes color of ActiveBall
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

        if(!useID)
        {
            // Randomize the color of the ActiveBall
            randColor();
            other.randColor();
        }
    }

    public int getID() // Returns the ID of the ActiveBall
    {
        return this.ID;
    }

    public Point getLocation() // Returns the location of the ActiveBall as a Point
    {
        return new Point((int)this.x, (int)this.y);
    }

    public void randColor() // Randomizes the color of the ActiveBall
    {
        this.color = new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255));
    }

    public void randPoolColor() // Randomizes the color of the ActiveBall so that it's more clear and not black or white
    {
        int r = rand.nextInt(255);
        int g = rand.nextInt(255);
        int b = rand.nextInt(255);
        int whiteThreshhold = 80; // The threshold for the color to be considered to close to white
        int blackThreshold = 80; // The threshold for the color to be considered to close to black
        if(r > 255-whiteThreshhold && g > 255-whiteThreshhold && b > 255-whiteThreshhold) // Check for closeness to white
            randPoolColor();
        if(r < blackThreshold && g < blackThreshold && b < blackThreshold) // Check for closeness to black
            randPoolColor();
        this.color = new Color(r, g, b);
    }

    public void regenMass() // Regenerates the mass of the ActiveBall
    {
        this.mass = rand.nextDouble(max-min) + min;
    }

    public void regenVelocity(double speedDiff) // Regenerates the velocity of the ActiveBall
    {
        this.dx = rand.nextDouble(speedDiff) - speedDiff/2;
		this.dy = rand.nextDouble(speedDiff) - speedDiff/2;
    }

    public void setMassRange(double min, double max, boolean recalculate) // Sets the minimum and maximum mass of the ActiveBall maximum must be lower than minimum. If recalculate is true, the mass of the ActiveBall will be regenerated.
    {
        if(max < min)
        {
            this.min = min;
            this.max = max;
            if(recalculate)
                regenMass();
        }
    }

    // Start of generic getters/setters

    public double getX() // Returns x-position
    {
        return x;
    }

    public void setX(double x) // Overrides the x-position with the value passed into the method
    {
        this.x = x;
    }

    public double getY() // Returns y-position
    {
        return y;
    }

    public void setY(double y) // Overrides the y-position with the value passed into the method
    {
        this.y = y;
    }

    public double getDx() // Returns x-velocity
    {
        return dx;
    }

    public void setDx(double dx) // Overrides the x-velocity with the value passed into the method
    {
        this.dx = dx;
    }

    public double getDy() // Returns y-velocity
    {
        return dy;
    }

    public void setDy(double dy) // Overrides the y-velocity with the value passed into the method
    {
        this.dy = dy;
    }

    public int getW() // Returns width
    {
        return w;
    }

    public void setW(int w) // Overrides the width with the value passed into the method
    {
        this.w = w;
    }

    public int getH() // Returns height
    {
        return h;
    }

    public void setH(int h) // Overrides the height with the value passed into the method
    {
        this.h = h;
    }

    public double getRadius() // Returns radius
    {
        return radius;
    }

    public void setRadius(double radius) // Overrides the radius with the value passed into the method
    {
        this.radius = radius;
    }

    public double getMass() // Returns mass
    {
        return mass;
    }

    public void setMass(double mass) // Overrides the mass with the value passed into the method
    {
        this.mass = mass;
    }

    public Color getColor() // Returns color
    {
        return color;
    }

    public void setColor(Color color) // Overrides the color with the value passed into the method
    {
        this.color = color;
    }

    public double getCoFric() // Returns coefficient of friction
    {
        return coFric;
    }

    public void setCoFric(double coFric) // Overrides the coefficient of friction with the value passed into the method
    {
        this.coFric = coFric;
    }

    public double getGravity() // Returns gravity
    {
        return gravity;
    }

    public void setGravity(double gravity) // Overrides the gravity with the value passed into the method
    {
        this.gravity = gravity;
    } 

    public double min() // Returns the minimum mass
    {
        return this.min;
    }

    public void setMin(double min) // Overrides the minimum mass with the value passed into the method
    {
        this.min = min;
    }

    public double max() // Returns the maximum mass
    {
        return this.max;
    }

    public void setMax(double max) // Overrides the maximum mass with the value passed into the method
    {
        this.max = max;
    }
}