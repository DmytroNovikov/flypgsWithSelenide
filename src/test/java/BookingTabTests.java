import flypgs.Pages.FlightBookingTab;
import flypgs.Pages.HomePage;
import org.junit.Ignore;
import org.junit.Test;

import java.time.LocalDate;

import static com.codeborne.selenide.Selenide.page;
import static com.codeborne.selenide.Selenide.sleep;

/**
 * Created by Dmytro Novikov on 4/7/2015.
 *
 * For using Chrome run with options
 * -Dbrowser=chrome -Dwebdriver.chrome.driver=C:\Users\Dmytro\Documents\QA\Tools\DriversForSelenium\chromedriver.exe
 */
public class BookingTabTests {

    @Test
    public void tc001() {
        HomePage homePage = page(HomePage.class);
        homePage.setCurrentLanguage("English");
        FlightBookingTab flightBookingTab = page(FlightBookingTab.class);
        flightBookingTab.buyOneWayTickets("Lviv", "Amsterdam", LocalDate.now(), 2, 1, 1, true);
    }

    @Test
    public void tc002() {
        HomePage homePage = page(HomePage.class);
        homePage.setCurrentLanguage("English");
        FlightBookingTab flightBookingTab = page(FlightBookingTab.class);
        flightBookingTab.buyRoundTripTickets("Lviv", "Amsterdam", LocalDate.now(), LocalDate.now(), 1, 1, 2, true);
    }

    @Test
    public void tc003() {
        HomePage homePage = page(HomePage.class);
        homePage.setCurrentLanguage("English");
        FlightBookingTab flightBookingTab = page(FlightBookingTab.class);
        flightBookingTab.buyRoundTripTickets("Lviv", "Amsterdam", LocalDate.now(), LocalDate.now(), 0, 0, 0, true);
    }

}
