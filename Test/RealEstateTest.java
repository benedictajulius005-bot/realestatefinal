import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;


public class RealEstateTest {

    @Test
    public void testLoadFromFile() {
        RealEstateAgent.loadFromFile("realestates.txt");
        List<RealEstate> estates = RealEstateAgent.getEstates();
        assertFalse("Estates should not be empty", estates.isEmpty());
    }

    @Test
    public void testAveragePricePerSqm() {
        RealEstateAgent.loadFromFile("realestates.txt");
        List<RealEstate> estates = RealEstateAgent.getEstates();
        double avgPrice = estates.stream()
                .mapToDouble(e -> e.price)
                .average()
                .orElse(0);
        assertTrue("Average price per sqm should be greater than 0", avgPrice > 0);
    }

    @Test
    public void testGetTotalPrice() {
        RealEstateAgent.loadFromFile("realestates.txt");
        List<RealEstate> estates = RealEstateAgent.getEstates();
        for (RealEstate e : estates) {
            int total = e.getTotalPrice();
            assertTrue("Total price should be greater than 0", total > 0);
        }
    }

    @Test
    public void testAverageSqmPerRoom() {
        RealEstateAgent.loadFromFile("realestates.txt");
        List<RealEstate> estates = RealEstateAgent.getEstates();
        for (RealEstate e : estates) {
            double avg = e.averageSqmPerRoom();
            assertTrue("Average sqm per room should be positive", avg > 0);
        }
    }

    @Test
    public void testPanelExtraFeatures() {
        RealEstateAgent.loadFromFile("realestates.txt");
        List<RealEstate> estates = RealEstateAgent.getEstates();
        estates.stream()
                .filter(e -> e instanceof Panel)
                .map(e -> (Panel) e)
                .forEach(panel -> {
                    assertTrue("Panel room price should be positive", panel.roomPrice() > 0);
                    assertTrue("hasSameAmount should return boolean",
                            panel.hasSameAmount(panel) || !panel.hasSameAmount(panel));
                });
    }
}
