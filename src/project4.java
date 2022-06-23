import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

import java.time.Duration;
import java.util.List;
import java.util.Set;



    public class project4 {




        public static void main(String[] args) throws InterruptedException {


            System.setProperty("webdriver.chrome.driver", "/Users/afaggadimova/Desktop/untitled folder/driver/chromedriver");
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--disable-blink-features=AutomationControlled");

            WebDriver driver = new ChromeDriver(options);
            driver.get("https://www.orbitz.com/");
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(4));
            driver.manage().window().maximize();
            driver.findElement(By.xpath("//button[@aria-label='Going to']")).click();
            driver.findElement(By.id("location-field-destination")).sendKeys("Orlando", Keys.ENTER);
            Thread.sleep(4000);
            driver.findElement(By.id("d1-btn")).click();
            Thread.sleep(4000);
            driver.findElement(By.xpath("(//table[@class='uitk-date-picker-weeks'])[1]//tr//td//button[@data-day='20']")).click();

            driver.findElement(By.xpath("(//table[@class='uitk-date-picker-weeks'])[1]//tr//td//button[@data-day='24']")).click();
            driver.findElement(By.xpath("//button[@data-stid='apply-date-picker']")).click();
            Thread.sleep(4000);
            driver.findElement(By.xpath("//button[@aria-label='1 room, 2 travelers']")).click();
            Thread.sleep(4000);
            driver.findElement(By.xpath("(//button[@class='uitk-layout-flex-item uitk-step-input-touch-target'])[1]")).click();
            driver.findElement(By.xpath("(//button[@class='uitk-layout-flex-item uitk-step-input-touch-target'])[4]")).click();
            Select firstChild = new Select(driver.findElement(By.id("child-age-input-0-0")));
            firstChild.selectByIndex(5);
            driver.findElement(By.xpath("(//button[@class='uitk-layout-flex-item uitk-step-input-touch-target'])[4]")).click();
            Thread.sleep(4000);
            Select secondChild = new Select(driver.findElement(By.id("child-age-input-0-1")));
            secondChild.selectByIndex(9);
            driver.findElement(By.xpath("//button[@data-testid='guests-done-button']")).click();
            driver.findElement(By.xpath("//button[@data-testid='submit-button']")).click();
            Thread.sleep(2000);
            WebElement breakfast = driver.findElement(By.xpath("//input[@id='popularFilter-0-FREE_BREAKFAST']"));

            if (!breakfast.isSelected()) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", breakfast);
            }

            String actual = driver.getPageSource();
            Thread.sleep(4000);
            Assert.assertTrue(actual.contains("Breakfast included"));

            if (breakfast.isSelected()) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", breakfast);

            }
            Thread.sleep(4000);
            ((JavascriptExecutor) driver).executeScript("window.scrollBy(0,500)", "");

            WebElement budget=driver.findElement(By.xpath("//input[@id='price-slider-secondary']"));
            budget.sendKeys(Keys.ARROW_LEFT);
            Thread.sleep(4000);
            String actualPrice= driver.findElement(By.xpath("//button[@id='playback-filter-pill-price-0.0-270.0']")).getText();
            Thread.sleep(4000);
            Assert.assertEquals(actualPrice,"Less than $270");

            ((JavascriptExecutor) driver).executeScript("window.scrollBy(0,3000)", "");
            List<WebElement> hotelList = driver.findElements(By.xpath("//li[contains(@class, 'uitk-spacing-margin-blockstart-three')][@tabindex=\"-1\"]"));
            Thread.sleep(4000);
            ((JavascriptExecutor) driver).executeScript("window.scrollBy(0,3000)", "");
            Assert.assertEquals(hotelList.size(),50);
            Thread.sleep(4000);

            List<WebElement> hotelprice= driver.findElements(By.xpath("//div[contains(text(), \"The price is\")]"));

            for (WebElement price : hotelprice) {
                Assert.assertTrue((Integer.parseInt(price.getText().replaceAll("[$,  The price is]", "")) <= 270));
            }

            ((JavascriptExecutor) driver).executeScript("arguments[0].click();",driver.findElement(By.id("radio-guestRating-45")));
            Thread.sleep(4000);

            List<WebElement> rating = driver.findElements(By.xpath("//span[@class='uitk-text uitk-type-300 uitk-type-bold uitk-text-default-theme']"));

            for (WebElement hotelRatings: rating ) {
                Assert.assertTrue((Double.parseDouble(hotelRatings.getText().substring(0,3))>=4.5));
            }

            hotelList = driver.findElements(By.xpath("//li[contains(@class, 'uitk-spacing-margin-blockstart-three')][@tabindex=\"-1\"]"));

            WebElement lastHotel = hotelList.get(hotelList.size() - 1);
            String lastHotelName = lastHotel.findElement(By.xpath(".//child::*[1]")).getText();
            double lastHotelRating = Double.valueOf(lastHotel.findElement(By.xpath(".//span[contains(text(), 'out of 5')]//preceding-sibling::span")).getText().split("/")[0]);
            lastHotel.click();

            String mainwindowHandle = driver.getWindowHandle();
            Set<String> windowHandles = driver.getWindowHandles();
            Thread.sleep(6000);
            for (String title: windowHandles){
                if(!title.equals(mainwindowHandle)){
                    driver.switchTo().window(title);}
            }

            Assert.assertEquals(lastHotelName, driver.getTitle());

            String currentWindowHotelName = driver.findElement(By.xpath("//div[@data-stid='content-hotel-title']//h1")).getText();
            double currentWindowHotelRating = Double.valueOf(driver.findElement(By.xpath("//div[@data-stid='content-hotel-reviewsummary']//h3")).getText().split("/")[0]);

            Assert.assertEquals(currentWindowHotelName, lastHotelName);
            Assert.assertEquals(currentWindowHotelRating, lastHotelRating);

            driver.close();

            Thread.sleep(6000);
            driver.switchTo().window(mainwindowHandle);

            driver.findElement(By.xpath("//a[@href='/']")).click();
            Thread.sleep(15000);
            WebElement iframe = driver.findElement(By.xpath("//iframe[contains(@id,'vac_iframe_')]"));
            driver.switchTo().frame(iframe );

            driver.findElement(By.xpath("//button[@aria-label='Get help from your Virtual Agent.']")).click();

            Thread.sleep(3000);
            String actualSource = driver.findElement(By.xpath("(//div[@data-test-id='chat-text-message'])[1]")).getText();
            Thread.sleep(6000);
            Assert.assertTrue(actualSource.contains("Hi, I'm your Virtual Agent "));
            Thread.sleep(8000);
            driver.findElement(By.xpath("//button[@id='vac-close-button']")).click();
            Thread.sleep(3000);
            driver.switchTo().defaultContent();
            driver.findElement(By.xpath("//div[@id='gc-custom-header-tool-bar-shop-menu']")).click();
            Thread.sleep(750);
            String [] actualOptions= {"Stays","Flights","Packages","Cars","Cruises","Things to do","Deals","Groups & meetings","Travel Blog"};
            List<WebElement> moreOptions = driver.findElements(By.xpath("(//div[@class='uitk-list'])[1]//a[@role='link']"));

            for(int i=0;i< moreOptions.size();i++){
                Assert.assertEquals(moreOptions.get(i).getText(),actualOptions[i]);
            }
            Thread.sleep(750);
            driver.quit();

        }


    }


