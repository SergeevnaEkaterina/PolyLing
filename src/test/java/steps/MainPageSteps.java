package steps;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.MainPage;
import util.PropertyInitialization;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class MainPageSteps{
    public WebDriver webDriver;

    public static final String URL = PropertyInitialization.getPropertyByName("url");
    protected MainPage mainPage;

    public ChromeDriver getChromeDriver() {
        ChromeOptions chromeOptions = new ChromeOptions();
//        chromeOptions.addArguments("window-size=200x200");

        chromeOptions.addArguments("--remote-allow-origins=*");
        return new ChromeDriver(chromeOptions);
    }

    public EdgeDriver getEdgeDriver() {
        EdgeOptions edgeOptions = new EdgeOptions();
        edgeOptions.addArguments("--start-maximized");
        edgeOptions.addArguments("--remote-allow-origins=*");
        return new EdgeDriver(edgeOptions);
    }

    public FirefoxDriver getFirefoxDriver() {
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.addArguments("--start-maximized");
//        firefoxOptions.addArguments("--remote-allow-origins=*");
        return new FirefoxDriver(firefoxOptions);
    }




    public MainPageSteps() {


    }
    public MainPageSteps initializeBrowser(String browser){
        if(Objects.equals(browser, "edge")) {
            webDriver = getEdgeDriver();
        } else if(Objects.equals(browser, "chrome")) {
            webDriver = getChromeDriver();
        } else if(Objects.equals(browser, "firefox")) {
            webDriver = getFirefoxDriver();
        } else{
            webDriver = getChromeDriver();
        }

        webDriver.manage().window().maximize();
        mainPage = new MainPage(webDriver);
        return this;
    }

    @Step("Input text to analyze")
    @When("User inputs text for foreign analyse with {string} mark into analyse area")
    public void inputForeign(String mark) {
        String fileName;

        fileName = switch (mark) {
            case ("1") -> "ForeignVeryBad";
            case ("2") -> "ForeignBad";
            case ("3") -> "Satisfactory";
            case ("4") -> "Good";
            default -> "Excellent";
        };

        String content;
        try {
            content = Files.readString(Paths.get("src/main/resources/grammarRes/" + fileName + ".txt"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        mainPage.inputTextToAnalysisArea(content);
    }
    @Step("Input text to analyze")
    @When("User inputs text with {string} mark into analyse area")
    public void input(String mark) {
        String fileName;

        fileName = switch (mark) {
            case ("1") -> "VeryBad";
            case ("2") -> "Bad";
            case ("3") -> "Satisfactory";
            case ("4") -> "Good";
            default -> "Excellent";
        };

        String content;
        try {
            content = Files.readString(Paths.get("src/main/resources/grammarRes/" + fileName + ".txt"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        mainPage.inputTextToAnalysisArea(content);
    }

    @Step("Input text to analyze")
    @When("User inputs text {string} into analyse area")
    public void inputPlainText(String text) {

        mainPage.inputTextToAnalysisArea(text);
    }

    @Step("Input link to analyze")
    public void inputLink(String text) {
        mainPage.inputLinkToAnalysisArea(text);
    }


    @Step("Open by url")
    @Given("User opens main page of PolyLing application using {string}")
    public void openByUrl(String browser) {
        MainPageSteps.this.initializeBrowser(browser);
        mainPage.open(URL);
    }

    @When("User minimizes window to mobile mode")
    public void minimizeWindow(){
        webDriver.manage().window().setSize(new Dimension(900,900));

    }

    @Then("Headers in mobile version are")
    public void getInfoHeaders(DataTable headerTexts) throws InterruptedException {
        List<List<String>> dataList = headerTexts.asLists(String.class);
        Thread.sleep(1000);
        for (int i = 0; i < dataList.size(); i++) {
            assertEquals(dataList.get(0).get(i), mainPage.getInformationHeaders().get(i).getText());
        }

    }

    @Step("Run text analyse")
    @Then("User presses analyse button")
    public void runAnalyse() throws InterruptedException {
        mainPage.runTextAnalyse();
    }

    @Then("User presses burger button at right up corner")
    public void getInformation() throws InterruptedException {
        Thread.sleep(2000);
        mainPage.getBurgerButton().click();

    }

    @Step("Get score")
    @Then("The score has attributes {string}")
    public void checkScore(String attributes) {
        assertEquals(mainPage.getScoreWithAttributes(), attributes + "/5");
    }


    @Then("The main header is {string}")
    public void checkHeader(String header) {
        assertEquals(mainPage.getHeader(), header);
    }

    @Then("Char counter shows {string} quantity")
    public void checkCounter(String count) {
        assertEquals(mainPage.getCounter(), count + "/10000");
    }
    @Then("The field for text is available with text {string}")
    public void checkInputField(String text) {
        assertTrue(mainPage.getInputField().isDisplayed());
        assertEquals(mainPage.getInputField().getDomAttribute("placeholder").toLowerCase(Locale.ROOT), text);
    }

    @Then("The link input field is available with text {string}")
    public void checkLinkInputField(String text) {
        assertTrue(mainPage.getLinkInput().get(1).isDisplayed());
        assertEquals(mainPage.getLinkInput().get(1).getDomAttribute("placeholder").toLowerCase(Locale.ROOT), text);
    }

    @Step("Get score")
    @Then("The score of foreign mark has attributes {string}")
    public void checkScoreForeign(String attributes) {
        assertEquals(mainPage.getScoreWithAttributes(), attributes + "/3");
    }

    @Step("Switch native language")
    @When("User switches language to {string} mode")
    public void switchNativeLanguage(String mode) {
        mainPage.switchLanguageNativity(mode);
    }

    @When("Language nativity ratios are available")
    public void getLanguageSwitcher() {
        mainPage.getIsLanguageForeignSwitcher().get(0).isDisplayed();
        mainPage.getIsLanguageForeignSwitcher().get(1).isDisplayed();
    }

    @When("The content to visual switcher is available")
    public void getContentSwitcher() {
        mainPage.getVisualPerceptionSwitcher().isDisplayed();
    }

    @When("The analyse button is available")
    public void getAnalyseButton() {
        mainPage.getRunAnalyseButton().isDisplayed();
    }

    @When("The choose file button is available")
    public void getChooseFileButton() {
        mainPage.getChooseFileButton().isDisplayed();
    }

    @Then("User switches visual perception")
    public void switchVisualPerception() {
        mainPage.getVisualPerceptionSwitcher().click();
        WebElement l = webDriver.findElement(By.cssSelector("div.analysisLink-style__form_group--Yokt3 > span"));
        System.out.println("find by");
        // Javascript executor
        ((JavascriptExecutor)webDriver).executeScript("arguments[0].scrollIntoView(true);", l);
        System.out.println("scrolled");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Step("Check color of score progress bar corresponds to {index} score")
    @Then("The progress bar color is {string}")
    public void checkProgressBarColor(String expectedScoreColor) {
        assertEquals(mainPage.getProgressBarColor(), expectedScoreColor);
    }

    @Step("Switch to visual text perception")
    public void switchPerception() {
        mainPage.switchToVisualOrTextPerception();
    }

    @Then("Switch to doc input")
    public void switchToDocInput() {
        mainPage.switchToDocInput();
    }

    @Then("User closes the page")
    @Step("User closes the page")
    public void closePage() {
        webDriver.close();
//        webDriver.quit();
    }

}
