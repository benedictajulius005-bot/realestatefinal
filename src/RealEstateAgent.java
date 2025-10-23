import java.io.*;
import java.util.*;

public class RealEstateAgent {
    private static TreeSet<RealEstate> estates = new TreeSet<>();

    public static void main(String[] args) {
        loadFromFile("realestates.txt");
        displayResults();
    }


    public static List<RealEstate> getEstates() {
        return new ArrayList<>(estates);
    }

    public static void loadFromFile(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split("#");
                if (data[0].equalsIgnoreCase("PANEL")) {
                    String city = data[1];
                    double price = Double.parseDouble(data[2]);
                    int sqm = Integer.parseInt(data[3]);
                    double rooms = Double.parseDouble(data[4]);
                    RealEstate.Genre genre = RealEstate.Genre.valueOf(data[5]);
                    int floor = Integer.parseInt(data[6]);
                    boolean insulated = data[7].equalsIgnoreCase("yes");

                    estates.add(new Panel(city, price, sqm, rooms, genre, floor, insulated));
                } else {
                    String city = data[1];
                    double price = Double.parseDouble(data[2]);
                    int sqm = Integer.parseInt(data[3]);
                    double rooms = Double.parseDouble(data[4]);
                    RealEstate.Genre genre = RealEstate.Genre.valueOf(data[5]);

                    estates.add(new RealEstate(city, price, sqm, rooms, genre));
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file, loading sample data...");
            estates.add(new RealEstate("Budapest", 250000, 100, 4, RealEstate.Genre.CONDOMINIUM));
        }
    }

    public static void displayResults() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("outputRealEstate.txt"))) {
            double avgSqmPrice = estates.stream().mapToDouble(e -> e.price).average().orElse(0);
            int cheapest = estates.stream().mapToInt(RealEstate::getTotalPrice).min().orElse(0);
            Optional<RealEstate> budapestMostExpensive = estates.stream()
                    .filter(e -> e.city.equalsIgnoreCase("Budapest"))
                    .max(Comparator.comparingInt(RealEstate::getTotalPrice));

            double avgSqmPerRoom = budapestMostExpensive.map(RealEstate::averageSqmPerRoom).orElse(0.0);
            int totalPrice = estates.stream().mapToInt(RealEstate::getTotalPrice).sum();
            double avgTotal = estates.stream().mapToInt(RealEstate::getTotalPrice).average().orElse(0);

            List<RealEstate> cheapCondos = estates.stream()
                    .filter(e -> e.genre == RealEstate.Genre.CONDOMINIUM && e.getTotalPrice() <= avgTotal)
                    .toList();

            writer.println("Average price per sqm: " + avgSqmPrice);
            writer.println("Cheapest property price: " + cheapest);
            writer.println("Avg sqm per room (most expensive Budapest property): " + avgSqmPerRoom);
            writer.println("Total price of all properties: " + totalPrice);
            writer.println("Condominiums below avg price:");
            cheapCondos.forEach(e -> writer.println("  - " + e));

            System.out.println("Average price per sqm: " + avgSqmPrice);
            System.out.println("Cheapest property price: " + cheapest);
            System.out.println("Avg sqm per room (most expensive Budapest property): " + avgSqmPerRoom);
            System.out.println("Total price of all properties: " + totalPrice);
            System.out.println("Condominiums below avg price:");
            cheapCondos.forEach(System.out::println);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
