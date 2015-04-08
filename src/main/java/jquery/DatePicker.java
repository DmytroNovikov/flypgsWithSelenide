package jquery;


import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import java.time.LocalDate;
import java.util.*;

import static com.codeborne.selenide.Selenide.$;

/**
 * Created by Dmytro Novikov on 4/7/2015.
 *
 * Based on solution by Aravind G
 * http://software-testing-tutorials-automation.blogspot.com/2014/10/how-to-select-date-in-selenium.html
 */
public class DatePicker{

    private static Map<String, List<String>> monthListMap;
    static{
        monthListMap = new HashMap<String, List<String>>();
        monthListMap.put("Türkçe",
                Arrays.asList("Oca", "Şub", "Mar", "Nis", "May", "Haz",
                        "Tem", "Ağu", "Eyl", "Eki", "Kas", "Ara"));
        monthListMap.put("English",
                Arrays.asList("Jan", "Feb", "Mar", "Apr", "May", "Jun",
                        "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"));
        monthListMap.put("Deutsch",
                Arrays.asList("Jan", "Feb", "Mrz", "Apr", "Mai", "Jun",
                        "Jul", "Aug", "Sep", "Okt", "Nov", "Dez"));
        monthListMap.put("Français",
                Arrays.asList("janv.", "févr.", "mars", "avr.", "mai", "juin",
                        "juil.", "août.", "sept.", "oct.", "nov.", "déc."));
        monthListMap.put("Русский",
                Arrays.asList("янв", "фев", "мар", "апр", "май", "июн",
                        "июл", "авг", "сен", "окт", "ноя", "дек"));
        monthListMap.put("Nederlands",
                Arrays.asList("jan", "feb", "mrt", "apr", "mei", "jun",
                        "jul", "aug", "sep", "okt", "nov", "dec"));
        monthListMap.put("Dansk",
                Arrays.asList("jan", "feb", "mar", "apr", "maj", "jun",
                        "jul", "aug", "sep", "okt", "nov", "dec"));
        monthListMap.put("Italiano",
                Arrays.asList("gen", "feb", "mar", "apr", "mag", "giu",
                        "lug", "ago", "set", "ott", "nov", "dic"));
        monthListMap.put("Svenska",
                Arrays.asList("jan", "feb", "mar", "apr", "maj", "jun",
                        "jul", "aug", "sep", "okt", "nov", "dec"));
        monthListMap = Collections.unmodifiableMap(monthListMap);
    }

    public static void setDate(LocalDate date, String language){
        boolean dateNotFound = true;
        String calMonth, calYear, calDay;
        //This loop will be executed continuously till dateNotFound Is true.
        while (dateNotFound){
            //Retrieve current selected month name from date picker popup.
            calMonth = $(".ui-datepicker-month").getText();
            //Retrieve current selected year name from date picker popup.
            calYear = $(".ui-datepicker-year").getText();
            if(date.getYear() > Integer.parseInt(calYear)){
                //Click on next button of date picker.
                $(By.xpath(".//*[@id='ui-datepicker-div']/div/a[2]/span")).click();
            } else if(date.getYear() < Integer.parseInt(calYear)){
                //Click on previous button of date picker.
                $(By.xpath(".//*[@id='ui-datepicker-div']/div/a[1]/span")).click();
            } else if(date.getMonthValue() > getMonthIndex(calMonth, language)){
                //Click on next button of date picker.
                $(By.xpath(".//*[@id='ui-datepicker-div']/div/a[2]/span")).click();
            } else if(date.getMonthValue() < getMonthIndex(calMonth, language)){
                //Click on previous button of date picker.
                $(By.xpath(".//*[@id='ui-datepicker-div']/div/a[1]/span")).click();
            } else {
                //Call selectDate function with date to select and set dateNotFound flag to false.
                SelenideElement datePicker = $("#ui-datepicker-div");
                List<SelenideElement> noOfColumns = datePicker.$$("td");
                String day = String.valueOf(date.getDayOfMonth());
                //Loop will rotate till expected date not found.
                for (SelenideElement cell : noOfColumns){
                    //Select the date from date picker when condition match.
                    if (cell.getText().equals(day)){
                        cell.$(By.linkText(day)).click();
                        break;
                    }
                }
                dateNotFound = false;
            }
        }
    }

    private static int getMonthIndex(String month, String language){
        return (monthListMap.get(language).indexOf(month) + 1);
    }
}