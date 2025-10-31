import java.io.*;
import java.util.*;
import java.util.logging.*;

/**
 * The RealEstateAgent class handles reading real estate data from a file,
 * storing them in a collection, performing calculations, and writing results to an output file.
 * <p>
 * It also includes logging to record events and exceptions into "realEstateApp.log".
 */
public class RealEstateAgent {

    /** Logger instance for recording info and error messages */
    private static final Logger logger = Logger.getLogger(RealEstateAgent.class.getName());

    /** Collection of real estate properties */
    private static final TreeSet<RealEstate> estates = new TreeSet<>();

    // Static block to configure logging
    static {
        try {
            FileHandler fileHandler = new FileHandler("realEstateApp.log", true);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
            logger.setLevel(Level.INFO);
        } catch (IOException e) {
            System.err.println("Failed to set up logger: " + e.getMessage());
        }
    }

    /**
     * Main method that runs the program.
     * Loads data from a file and displays the calculated results.
     *
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        logger.info("Application started");
        loadFromFile("Realestate.txt"); // ✅ matches your file name exactly
        displayResults();
        logger.info("Application finished successfully");
    }

    /**
     * Returns all loaded real estates as a list.
     *
     * @return List of RealEstate objects
     */
    public static List<RealEstate> getEstates() {
        logger.info("getEstates() called");
        return new ArrayList<>(estates);
    }

    /**
     * Loads real estate data from a text file and adds them to the estates collection.
     * Each line is split by "#" to extract data fields.
     *
     * @param filename the name of the file to load
     */
    public static void loadFromFile(String filename) {
        logger.info("Loading data from file: " + filename);
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
            logger.info("Data successfully loaded from file: " + filename);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error reading file: " + filename, e);
            System.out.println("Error reading file, loading sample data...");
            estates.add(new RealEstate("Budapest", 250000, 100, 4, RealEstate.Genre.CONDOMINIUM));
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error while loading data", e);
        }
    }

    /**
     * Displays calculated real estate statistics in the console
     * and writes them to an output text file.
     * Includes average prices, cheapest property, and condominiums below average price.
     */
    public static void displayResults() {
        logger.info("displayResults() called");
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

            logger.info("Results successfully written to outputRealEstate.txt");

        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error writing to outputRealEstate.txt", e);
        }
    }
}
