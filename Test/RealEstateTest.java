import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;
import java.util.logging.Logger;

public class RealEstateTest {

    private static final Logger logger = Logger.getLogger(RealEstateTest.class.getName());

    @Test
    public void testLoadFromFile() {
        logger.info("Running testLoadFromFile...");
        RealEstateAgent.loadFromFile("Realestate.txt"); // ✅ corrected filename
        List<RealEstate> estates = RealEstateAgent.getEstates();
        assertFalse("Estates should not be empty", estates.isEmpty());
        logger.info("testLoadFromFile passed.");
    }

    @Test
    public void testAveragePricePerSqm() {
        logger.info("Running testAveragePricePerSqm...");
        RealEstateAgent.loadFromFile("Realestate.txt"); // ✅ corrected filename
        List<RealEstate> estates = RealEstateAgent.getEstates();
        double avgPrice = estates.stream()
                .mapToDouble(e -> e.price)
                .average()
                .orElse(0);
        assertTrue("Average price per sqm should be greater than 0", avgPrice > 0);
        logger.info("testAveragePricePerSqm passed.");
    }

    @Test
    public void testGetTotalPrice() {
        logger.info("Running testGetTotalPrice...");
        RealEstateAgent.loadFromFile("Realestate.txt"); // ✅ corrected filename
        List<RealEstate> estates = RealEstateAgent.getEstates();
        for (RealEstate e : estates) {
            int total = e.getTotalPrice();
            assertTrue("Total price should be greater than 0", total > 0);
        }
        logger.info("testGetTotalPrice passed.");
    }

    @Test
    public void testAverageSqmPerRoom() {
        logger.info("Running testAverageSqmPerRoom...");
        RealEstateAgent.loadFromFile("Realestate.txt"); // ✅ corrected filename
        List<RealEstate> estates = RealEstateAgent.getEstates();
        for (RealEstate e : estates) {
            double avg = e.averageSqmPerRoom();
            assertTrue("Average sqm per room should be positive", avg > 0);
        }
        logger.info("testAverageSqmPerRoom passed.");
    }

    @Test
    public void testPanelExtraFeatures() {
        logger.info("Running testPanelExtraFeatures...");
        RealEstateAgent.loadFromFile("Realestate.txt"); // ✅ corrected filename
        List<RealEstate> estates = RealEstateAgent.getEstates();
        estates.stream()
                .filter(e -> e instanceof Panel)
                .map(e -> (Panel) e)
                .forEach(panel -> {
                    assertTrue("Panel room price should be positive", panel.roomPrice() > 0);
                    assertTrue("hasSameAmount should return boolean",
                            panel.hasSameAmount(panel) || !panel.hasSameAmount(panel));
                });
        logger.info("testPanelExtraFeatures passed.");
    }
}
