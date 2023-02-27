package Pool;

import java.awt.*;

public class Hole 
{
    private double x, y;
    private double w, h;
    private double radius;
    public Hole(double x, double y, double w, double h)
    {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.radius = w/2;
    }

    public void drawHole(Graphics g)
    {
        g.setColor(Color.BLACK);
        g.fillOval((int)x, (int)y, (int)w, (int)h);
    }

    public int getX()
    {
        return (int)x;
    }

    public int getY()
    {
        return (int)y;
    }

    public int getRadius()
    {
        return (int)radius;
    }
}
