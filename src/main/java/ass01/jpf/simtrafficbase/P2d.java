package ass01.jpf.simtrafficbase;

/**
 *
 * P2d -- modelling a point in a 2D space
 * 
 */
public class P2d {
    double x;
    double y;

    public P2d(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public P2d sum(V2d v){
        return new P2d(x+v.getX(),y+v.getY());
    }

    public V2d sub(P2d v){
        return new V2d(x-v.x,y-v.y);
    }

    public String toString(){
        return "P2d("+x+","+y+")";
    }

    public static double len(P2d p0, P2d p1) {
    	double dx = p0.x - p1.x;
    	double dy = p0.y - p1.y;   	
        return (double)Math.sqrt(dx*dx+dy*dy);

    }

}
