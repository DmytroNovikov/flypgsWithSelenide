/**
 * Created by Dmytro Novikov on 4/8/2015.
 */

import flypgs.Pages.FlightBookingTab;
import flypgs.Pages.HomePage;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import static com.codeborne.selenide.Selenide.page;

@RunWith(Parameterized.class)
public class PossibleRoutesTest {

    private static final float coverage = 0.001f;
    private String from, to;

    @Parameterized.Parameters
    public static Collection routsData() throws IOException {
        File csvFile = new File("./data/connected_cities_en.csv");
        BufferedReader reader = new BufferedReader(new FileReader(csvFile));
        String line;
        List<Object> data = new ArrayList<Object>();
        while((line=reader.readLine()) != null) {
            String[] lines = line.split(",");
            data.add(new Object[]{lines[0].trim(), lines[1].trim()});
        }
        if(coverage != 1.0f){
            // If the test coverage is not equal to 100% we randomly select the number of input parameters
            // according to the coverage percentage defined in 'coverage' varible.
            List<Object> temp = new ArrayList<Object>();
            int size = data.size();
            int newSize = (int) (size * coverage);
            List<Integer> indexes = new ArrayList<Integer>();
            Random rnd = new Random();
            while(indexes.size() < newSize) {
                int i = rnd.nextInt(size);
                if (!indexes.contains(i)) {
                    indexes.add(i);
                    temp.add(data.get(i));
                }
            }
            data = temp;
        }
        return data;
    }

    public PossibleRoutesTest(String from, String to){
        this.from = from;
        this.to = to;
    }

    @Test
    public void testCase(){
        HomePage homePage = page(HomePage.class);
        homePage.setCurrentLanguage("English");
        FlightBookingTab flightBookingTab = page(FlightBookingTab.class);
        flightBookingTab.setTheRoute(from, to);
    }
}
