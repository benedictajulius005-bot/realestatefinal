public class RealEstate implements PropertyInterface, Comparable<RealEstate> {
    protected String city;
    protected double price; // price per sqm
    protected int sqm;
    protected double numberOfRooms;
    protected Genre genre;

    public enum Genre { FAMILYHOUSE, CONDOMINIUM, FARM }

    public RealEstate(String city, double price, int sqm, double numberOfRooms, Genre genre) {
        this.city = city;
        this.price = price;
        this.sqm = sqm;
        this.numberOfRooms = numberOfRooms;
        this.genre = genre;
    }

    @Override
    public void makeDiscount(int percentage) { price -= price * percentage / 100.0; }

    @Override
    public int getTotalPrice() {
        double total = price * sqm;
        switch(city.toLowerCase()) {
            case "budapest": total *= 1.30; break;
            case "debrecen": total *= 1.20; break;
            case "nyíregyháza": total *= 1.15; break;
        }
        return (int) total;
    }

    @Override
    public double averageSqmPerRoom() { return sqm / numberOfRooms; }

    @Override
    public String toString() {
        return city + ": " + price + " Ft/m², " + sqm + "m², " + numberOfRooms +
                " rooms, " + genre + ", Total: " + getTotalPrice();
    }

    @Override
    public int compareTo(RealEstate o) { return Integer.compare(this.getTotalPrice(), o.getTotalPrice()); }
}
