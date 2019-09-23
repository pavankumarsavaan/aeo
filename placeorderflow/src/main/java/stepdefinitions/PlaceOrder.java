package stepdefinitions;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.json.simple.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

import java.util.List;
import java.util.regex.Pattern;

public class PlaceOrder extends Utility {
    WebDriver driver;
    private static String contact_email;

    @Before
    public void beforeScenario() {
        System.setProperty("webdriver.chrome.driver", "/Users/pavankumar/Downloads/chromedriver");
        driver = new ChromeDriver();
    }

    @After
    public void afterScenario() {
        driver.close();
    }

    @Given("^I am on AEO website$")
    public void i_am_on_AEO_website() throws Exception {
        driver.get("https://www.ae.com");
    }

    @When("^I search with \"([^\"]*)\" keyword$")
    public void i_search_with_keyword(String search_keyword) throws Exception {
        driver.findElement(By.className("aeoicon-search_filled")).click();
        driver.findElement(By.name("search")).sendKeys(search_keyword);
        driver.findElement(By.className("search-icon-filled")).click();
    }

    @When("^I click on the first product$")
    public void i_click_on_the_first_product() throws Exception {
        //To click on the first product on the category browse page
        driver.findElement(By.className("product-tile")).click();
    }

    @When("^I select the first available color$")
    public void i_select_the_first_available_color() throws Exception {
        //Selecting the first available color
        List<WebElement> drop_down_values = driver.findElement(By.className("dropdown-menu")).findElements(By.className("ember-view"));
        for (WebElement e : drop_down_values) {
            String size = e.getText();
            if (!size.contains("Out of Stock") && size != "")
                driver.findElement(By.linkText(size)).click();
        }
    }

    @When("^I add the item to my cart$")
    public void i_add_the_item_to_my_cart() throws Exception {
        driver.findElement(By.name("addToBag")).click();
        driver.findElement(By.name("viewBag")).click();
    }

    @When("^I checkout from shopping bag page$")
    public void i_checkout_from_shopping_bag_page() throws Exception {
        driver.findElement(By.className("btn-primary btn-cart-checkout")).click();
    }

    @When("^I enter valid shipping address$")
    public void i_enter_valid_shipping_address() throws Exception {
        //Get the shipping address info from json and enter it on the checkout page
        JSONObject shipping_address = (JSONObject) readJsonFile("user_details.json").get("shipping_address");
        driver.findElement(By.name("firstname")).sendKeys(shipping_address.get("first_name").toString());
        driver.findElement(By.name("lastname")).sendKeys(shipping_address.get("last_name").toString());
        driver.findElement(By.name("streetAddress")).sendKeys(shipping_address.get("street_address").toString());
        driver.findElement(By.name("streetAddress2")).sendKeys(shipping_address.get("address_line2").toString());
        driver.findElement(By.name("city")).sendKeys(shipping_address.get("city").toString());
        Select state_dropdown = new Select(driver.findElement(By.name("states")));
        state_dropdown.selectByValue(shipping_address.get("state").toString());
    }

    @When("^I enter \"([^\"]*)\" credit card details$")
    public void i_enter_credit_card_details(String card_type) throws Exception {
        JSONObject credit_card = null;
        if (card_type == "valid") {
            credit_card = (JSONObject) readJsonFile("user_details.json").get("valid_credit_card");
        } else if (card_type == "invalid") {
            credit_card = (JSONObject) readJsonFile("user_details.json").get("invalid_credit_card");
        } else {
            Assert.fail("Invalid input for card type");
        }
        driver.findElement(By.name("credit-card-number-input")).sendKeys(credit_card.get("credit_card").toString());
        driver.findElement(By.name("expirationDate")).sendKeys(credit_card.get("exp_date").toString());
        driver.findElement(By.name("securityCode")).sendKeys(credit_card.get("scc").toString());
        driver.findElement(By.name("phoneNumber")).sendKeys(credit_card.get("phone").toString());
        contact_email = credit_card.get("contact_email").toString();
        driver.findElement(By.name("email")).sendKeys();
    }

    @When("^I place the order$")
    public void i_place_the_order() throws Exception {
        driver.findElement(By.name("place-order")).click();
    }

    @Then("^I should see that order placed successfully$")
    public void i_should_see_that_order_placed_successfully() throws Exception {
        String confirmation_message = driver.findElement(By.className("qa-confirmation-message")).getText();
        String expected_message = "Your order #\\d{10,} is in the works. We sent an email to " + contact_email + " with your order receipt";
        Assert.assertTrue(Pattern.matches(expected_message, confirmation_message), "Order confirmation message didn't match");
    }

    @Then("^I should see the error message \"([^\"]*)\"$")
    public void i_should_see_the_error_message(String error_message) throws Exception {
        String actual_error_message = driver.findElement(By.className("qa-error-help-block")).getText();
        Assert.assertEquals(error_message, actual_error_message, "Error message didn't match");
    }

    @When("^I enter gift card as the payment type$")
    public void i_enter_gift_card_as_the_payment_type() throws Exception {
        JSONObject gift_card = (JSONObject) readJsonFile("user_details.json").get("valid_credit_card");
        driver.findElement(By.name("giftCardNumber")).sendKeys(gift_card.get("card_number").toString());
        driver.findElement(By.name("giftCardNumber")).sendKeys(gift_card.get("pin").toString());
        driver.findElement(By.name("applyGiftCard")).click();
    }

}
