package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.services.EncryptionService;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.security.SecureRandom;
import java.util.Base64;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CloudStorageApplicationTests {

  @LocalServerPort private int port;
  private WebDriver driver;
  private final String baseURL = "http://localhost:";
  private WebDriverWait wait;
  private EncryptionService encryptionService;

  @BeforeAll
  static void beforeAll() {
    WebDriverManager.chromedriver().setup();
  }

  @BeforeEach
  public void beforeEach() {
    this.driver = new ChromeDriver();
  }

  @AfterEach
  public void afterEach() {
    if (this.driver != null) {
      driver.quit();
    }
  }

  @Test
  @Order(1)
  public void getHomePageUnauthorized() {
    driver.get("http://localhost:" + this.port + "/home");
    Assertions.assertEquals("Login", driver.getTitle());
  }

  @Test
  @Order(2)
  public void testUserSignupLogin() {
    userSignupLogin();
    Assertions.assertEquals("Home", driver.getTitle());

    HomePage homePage = new HomePage(driver);
    homePage.logout();
    Assertions.assertEquals("Login", driver.getTitle());

    driver.get(baseURL + port + "/Home");
    Assertions.assertEquals("Login", driver.getTitle());
  }

  public void userSignupLogin() {
    String username = "lucaslinard";
    String password = "Jmokom99*";

    driver.get(baseURL + port + "/signup");

    SignupPage signupPage = new SignupPage(driver);
    signupPage.signup("Lucas", "Linard", username, password);

    driver.get(baseURL + port + "/login");

    LoginPage loginPage = new LoginPage(driver);
    loginPage.login(username, password);
  }

  @Test
  @Order(3)
  public void testCreateNote() throws InterruptedException {
    userSignupLogin();
    HomePage homepage = new HomePage(driver);
    int row = 1;
    homepage.recordNewNote("Testing the title", "Testing the description");
    Assertions.assertEquals("Testing the title", homepage.getTitleInList(row));
    Assertions.assertEquals("Testing the description", homepage.getDescription(row));
    homepage.editExistingNote("Testing changes", "Testing changes to the description", row);
    Assertions.assertEquals("Testing changes", homepage.getTitleInList(row));
    Assertions.assertEquals("Testing changes to the description", homepage.getDescription(row));
    homepage.recordNewNote("Second test", "Testing the second description");
    homepage.accessNotesTab();
    row++;
    homepage.deleteExistingNote(row);
    Assertions.assertFalse(homepage.noteExists("Second test"));
  }

  @Test
  @Order(4)
  public void testCredentials() throws InterruptedException {
    userSignupLogin();
    HomePage homepage = new HomePage(driver);
    int row = 1;
    homepage.recordNewCredential("example.com", "lucaslinard", "dummypass");
    Assertions.assertEquals("example.com", homepage.getUrlInList(row));
    Assertions.assertEquals("lucaslinard", homepage.getUsernameInList(row));

    SecureRandom random = new SecureRandom();
    byte[] key = new byte[16];
    String encodedKey = Base64.getEncoder().encodeToString(key);
    EncryptionService encryptionService = new EncryptionService();
    String encodedPassword = encryptionService.encryptValue("dummypass", encodedKey);
    Assertions.assertEquals(encodedPassword, homepage.getPasswordInList(row));
  }

  @Test
  @Order(5)
  public void testExistingCredential() throws InterruptedException {
    userSignupLogin();
    HomePage homepage = new HomePage(driver);
    int row = 1;
    homepage.accessCredentialTab();
    homepage.recordNewCredential("example.com", "lucaslinard", "dummypass");
    homepage.getCredentialForEditing(row);
    Assertions.assertEquals("example.com", homepage.getCredoUrl());
    Assertions.assertEquals("lucaslinard", homepage.getCredoUsername());
    Assertions.assertEquals("dummypass", homepage.getCredoPassword());
    homepage.editExistingCredential("www.google.com", "Schumacher", "Ferrari", row);
    Assertions.assertEquals("www.google.com", homepage.getUrlInList(row));
    Assertions.assertEquals("Schumacher", homepage.getUsernameInList(row));
    SecureRandom random = new SecureRandom();
    byte[] key = new byte[16];
    String encodedKey = Base64.getEncoder().encodeToString(key);
    EncryptionService encryptionService = new EncryptionService();
    String encodedPassword = encryptionService.encryptValue("Ferrari", encodedKey);
    Assertions.assertEquals(encodedPassword, homepage.getPasswordInList(row));
    homepage.recordNewCredential("www.somepage.com", "someuser", "dummypass");
    row++;
    homepage.deleteExistingCredential(row);
    Assertions.assertFalse(homepage.credentialExists("www.somepage.com"));
  }
}
