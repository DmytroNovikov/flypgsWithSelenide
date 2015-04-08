package flypgs.Pages;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.Selenide.*;

/**
 * Created by Dmytro Novikov on 4/7/2015.
 */
public class HomePage {
    private static final String pageUrl = "http://www.flypgs.com";
    private static final String pageTitle = "Pegasus Havayollar? - Ucuz U?ak Bileti Demek ?zg?rl?k Demek!";

    private static final String cssCurrentLanguage = "span#lang-selected";
    private static final String cssLanguages = "ul#select-lang a";
    private static final String xpathTargetLanguage="//li/a[text()='%language%']";

    public HomePage(){
        open(pageUrl);
        title().equals(pageTitle);
    }

    public String getCurrentLanguage(){
        return $(cssCurrentLanguage).getText();
    }

    public List<String> getLanguages(){
        List<String> languages = new ArrayList<String>();
        for(SelenideElement e : $$(cssLanguages))
            languages.add(e.getAttribute("innerHTML"));
        return languages;
    }

    public void setCurrentLanguage(String language){
        if(!language.equals(getCurrentLanguage())){
            int n = getLanguages().indexOf(language) + 1;
            if(n > 0){
                $(cssCurrentLanguage).click();
                $(By.xpath(xpathTargetLanguage.replace("%language%", language))).click();
            }
        }
    }
}