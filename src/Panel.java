public class Panel extends RealEstate implements PanelInterface {
    private int floor;
    private boolean isInsulated;

    public Panel(String city, double price, int sqm, double numberOfRooms, Genre genre, int floor, boolean isInsulated) {
        super(city, price, sqm, numberOfRooms, genre);
        this.floor = floor;
        this.isInsulated = isInsulated;
    }

    @Override
    public int getTotalPrice() {
        double total = super.getTotalPrice();
        if(floor >= 0 && floor <= 2) total *= 1.05;
        else if(floor == 10) total *= 0.95;
        if(isInsulated) total *= 1.05;
        return (int) total;
    }

    @Override
    public boolean hasSameAmount(RealEstate re) { return this.getTotalPrice() == re.getTotalPrice(); }

    @Override
    public int roomPrice() { return (int)(price * sqm / numberOfRooms); }

    @Override
    public String toString() { return "PANEL - floor:" + floor + ", insulated:" + isInsulated + " => " + super.toString(); }
}
