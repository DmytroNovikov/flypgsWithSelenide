package flypgs.Pages;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import jquery.DatePicker;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

/**
 * Created by Dmytro Novikov on 4/7/2015.
 */
public class FlightBookingTab {

    // Language control to get the current language (used by set...Date)
    private static final String currentLanguageControl = "span#lang-selected";

    // From
    private static final String controlFrom = "a#autodep-auto-show-all";
    private static final String valueFrom = "input#autodep";

    // To
    private static final String controlTo = "a#autodest-auto-show-all";
    private static final String valueTo = "input#autodest";

    private static final String citiesList = "li.ui-menu-item a";
    private static final String xpathToItemToWait = "/html/body/ul[%data%]/li[1]/a";

    // Round trip / One-way
    private static final String roundTripRadio =
            "//*[@id='module-online-actions']/div[2]/div[1]/table[1]/tbody/tr[3]/td/p[1]/span/a";
    private static final String oneWayTripRadio =
            "//*[@id='module-online-actions']/div[2]/div[1]/table[1]/tbody/tr[3]/td/p[2]/span/a";

    // Departure Date
    private static final String departureDateControl = "input[id='linked-gidis']";
    private static final String returnDateControl = "input[id='linked-donus']";

    // Adult
    private static final String adultControl =
            "//*[@id='module-online-actions']/div[2]/div[1]/table[2]/tbody/tr[1]/td[1]/div/div/div/div/div";
    private static final String adultsListItems = "//*[@id='pessengerddl-adult']/option[%number%]";

    // Children (2-12)
    public static final String childrenControl =
            "//*[@id='module-online-actions']/div[2]/div[1]/table[2]/tbody/tr[1]/td[2]/div/div/div/div/div";
    private static final String childrenListItems = "//*[@id='pessengerddl-child']/option[%number%]";

    // Infant (0-2)
    private static final String infatControl =
            "//*[@id='module-online-actions']/div[2]/div[1]/table[2]/tbody/tr[1]/td[3]/div/div/div/div/div";
    private static final String infantsListItems = "//*[@id='pessengerddl-baby']/option[%number%]";

    //  Are your travel dates flexible?
    private static final String flexibleCheckbox = "//*[@id='module-online-actions']/div[2]/div[2]/span/a";

    // Continue Button
    private static final String continueButton = "#OnlineTicket_btnOnlineTicketDevam";

    // Error window appeared after Continue Button pressed
    private final By errorCode = By.cssSelector("span.errorCode");
    private final By warning = By.cssSelector("td.warning");
    private final By afterError = By.cssSelector("a[href='Login.jsp']");

    public FlightBookingTab(){
    }

    public String buyRoundTripTickets(String from, String to,
                                      LocalDate departureDate, LocalDate returnDate,
                                      int adults, int children, int infants,
                                      boolean flexible){
        setTheRoute(from, to);
        setRoundTrip(true);
        $(By.xpath(roundTripRadio)).shouldHave(cssClass("input-custom-class-radio-active"));
        setDepartureDate(departureDate);
        $(departureDateControl).shouldHave(
                value(departureDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
        setReturnDate(returnDate);
        $(returnDateControl).shouldHave(
                value(returnDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
        setAdultsCount(adults);
        $(By.xpath(adultControl)).shouldHave(text(String.valueOf(adults)));
        setChildrenCount(children);
        $(By.xpath(childrenControl)).shouldHave(text(String.valueOf(children)));
        setInfantsCount(infants);
        $(By.xpath(infatControl)).shouldHave(text(String.valueOf(infants)));
        setFlexible(flexible);
        $(By.xpath(flexibleCheckbox)).shouldHave(cssClass("input-custom-class-checkbox-active"));
        return pushContinueButton();
    }

    public String buyOneWayTickets(String from, String to,
                                   LocalDate departureDate,
                                   int adults, int children, int infants,
                                   boolean flexible){
        setTheRoute(from, to);
        setRoundTrip(false);
        $(By.xpath(roundTripRadio)).shouldNotHave(cssClass("input-custom-class-radio-active"));
        setDepartureDate(departureDate);
        $(departureDateControl).shouldHave(
                value(departureDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
        setAdultsCount(adults);
        $(By.xpath(adultControl)).shouldHave(text(String.valueOf(adults)));
        setChildrenCount(children);
        $(By.xpath(childrenControl)).shouldHave(text(String.valueOf(children)));
        setInfantsCount(infants);
        $(By.xpath(infatControl)).shouldHave(text(String.valueOf(infants)));
        setFlexible(flexible);
        $(By.xpath(flexibleCheckbox)).shouldHave(cssClass("input-custom-class-checkbox-active"));
        return pushContinueButton();
    }

    public void setTheRoute(String from, String to){
        setCityFrom(from);
        $(valueFrom).shouldHave(value(from));
        setToCity(to);
        $(valueTo).shouldHave(value(to));
    }

    public void setCityFrom(String city){
        setCity(true, city);
    }

    public String getFromCity(){
        return $(valueFrom).attr("value");
    }

    public void setToCity(String city){
        setCity(false, city);
    }

    public String getToCity(){
        return $(valueTo).attr("value");
    }

    /**
     * Emulation of city selection by typing it's name
     *
     * Idea: If a correct city name is enetered to the 'From' text area there will be only one item
     *       in the dropdown list. Choose the item by pressing ArrowDown and Enter keys.
     *
     * @param departure
     * @param city
     */
    private void setCity(boolean departure, String city){
        String selector = departure?valueFrom:valueTo;
        SelenideElement inputElement = $(selector);
        if (!departure)
            inputElement.shouldBe(enabled);
        actions()
                .click(inputElement)
                .sendKeys(inputElement, city)
                .build()
                .perform();
        // Wait for single city in dropdown list
        String itemToWait = xpathToItemToWait.replace("%data%", Integer.toString(departure ? 1 : 2));
        $(By.xpath(itemToWait)).shouldHave(text(city));
        actions()
                .sendKeys(Keys.ARROW_DOWN)
                .sendKeys(Keys.ENTER)
                .perform();
    }

    public void setRoundTrip(boolean roundTrip){
        if(roundTrip != getRoundTrip())
            if(roundTrip)
                $(By.xpath(roundTripRadio)).click();
            else
                $(By.xpath(oneWayTripRadio)).click();
    }

    public boolean getRoundTrip(){
        return $(By.xpath(roundTripRadio)).attr("class").equals("input-custom-class-radio-active");
    }

    public void setDepartureDate(LocalDate date){
        if(date.compareTo(LocalDate.now()) >= 0) {
            $(departureDateControl).click();
            DatePicker.setDate(date, $(currentLanguageControl).getText());
        }
    }

    public LocalDate getDepartureDate(){
        return LocalDate.parse($(departureDateControl).attr("value"),
                DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    public void setReturnDate(LocalDate date){
        if(getRoundTrip()){
            if(date.compareTo(getDepartureDate()) >= 0){
                $(returnDateControl).click();
                DatePicker.setDate(date, $(currentLanguageControl).getText());
            }
        }
    }

    public LocalDate getReturnDate(){
        return LocalDate.parse($(returnDateControl).attr("value"),
                DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    public void setAdultsCount(int count){
        setPassengersCount(adultControl, adultsListItems, count);
    }

    public int getAdultsCount(){
        return Integer.parseInt($(By.xpath(adultControl)).getText());
    }

    public void setChildrenCount(int count){
        setPassengersCount(childrenControl, childrenListItems, count);
    }

    public int getChildrenCount(){
        return Integer.parseInt($(By.xpath(childrenControl)).getText());
    }

    public void setInfantsCount(int count){
        setPassengersCount(infatControl, infantsListItems,count);
    }

    public int getInfantsCount(){
        return Integer.parseInt($(By.xpath(infatControl)).getText());
    }

    private void setPassengersCount(String passengerControl, String passengersList , int count){
        $(By.xpath(passengerControl)).click();
        SelenideElement e = $(By.xpath(passengersList.replace("%number%", Integer.toString(count + 1))));
        e.shouldHave(text(Integer.toString(count)));
        e.click();
    }

    public void setFlexible(boolean flexible){
        String tagClass = $(By.xpath(flexibleCheckbox)).attr("class");
        if(flexible && tagClass.equals("input-custom-class-checkbox"))
            $(By.xpath(flexibleCheckbox)).click();
        if(!flexible && tagClass.equals("input-custom-class-checkbox-active"))
            $(By.xpath(flexibleCheckbox)).click();
    }

    public boolean getFlexible(){
        return $(By.xpath(flexibleCheckbox)).attr("class").equals("input-custom-class-checkbox-active");
    }

    public String pushContinueButton(){
        String ok = "OK";
        $(continueButton).click();
/*
        try {
            Alert alert = driver.switchTo().alert();
            ok = alert.getText();
            alert.dismiss();
        } catch (Exception e){
            // Nothing to do. It's not a good idea, but...
        }
        if(ok.equals("OK") && driver.getCurrentUrl().contains("Error")){
            List<WebElement> e = driver.findElements(errorCode);
            ok="";
            if(e.size() > 0) {
                ok = ok.concat(e.get(0).getText());
                ok = ok.concat("\n");
            }
            e = driver.findElements(warning);
            if(e.size() > 0)
                ok = ok.concat(e.get(0).getText());
            driver.findElement(afterError).click();
        }
*/
        return ok;
    }
}
