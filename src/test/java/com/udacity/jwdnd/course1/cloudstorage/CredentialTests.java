package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.pages.CredentialPage;
import com.udacity.jwdnd.course1.cloudstorage.pages.LoginPage;
import com.udacity.jwdnd.course1.cloudstorage.pages.ResultPage;
import com.udacity.jwdnd.course1.cloudstorage.pages.SignupPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CredentialTests {
    @LocalServerPort
    private int port;

    private WebDriver webDriver;

    private String url;

    @BeforeAll
    static void beforeAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void beforeEach() {
        this.webDriver = new ChromeDriver();
        this.url = "http://localhost:" + this.port;

        webDriver.get(url + "/signup");
        SignupPage signupPage = new SignupPage(webDriver);
        signupPage.signup("zyt", "zyt", "yz", "yz");

        webDriver.get(url + "/login");
        LoginPage loginPage = new LoginPage(webDriver);
        loginPage.login("yz", "yz");
    }

    @AfterEach
    public void afterEach() {
        if (this.webDriver != null) {
            webDriver.quit();
        }
    }

    @Test
    void testCredentialCreateEditDelete() {

        //create
        webDriver.get(url);
        CredentialPage credentialsHomePage = new CredentialPage(webDriver);
        WebDriverWait wait = new WebDriverWait(webDriver, 1000);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));

        credentialsHomePage.clickOnCredentialsTab();
        wait.until(ExpectedConditions.elementToBeClickable(By.id("credential-add-button")));

        credentialsHomePage.clickOnAddNewCredential();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-save-changes-button")));

        credentialsHomePage.createOrEditCredential("test.com", "test", "test");
        Assertions.assertEquals(url + "/credential", webDriver.getCurrentUrl());

        ResultPage resultPage = new ResultPage(webDriver);
        Assertions.assertTrue(resultPage.isSuccessful());

        resultPage.clickToReturnHomeSuccess();

        webDriver.get(url);
        credentialsHomePage = new CredentialPage(webDriver);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));

        credentialsHomePage.clickOnCredentialsTab();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialTable")));

        String firstCredUrl = credentialsHomePage.getCredentialUrl();
        String firstCredUsername = credentialsHomePage.getCredentialUsername();
        String firstCredPassword = credentialsHomePage.getCredentialPassword();
        Assertions.assertEquals("test.com", firstCredUrl);
        Assertions.assertEquals("test", firstCredUsername);
        Assertions.assertNotEquals("test", firstCredPassword);

        //edit
        credentialsHomePage.clickOnEditCredential();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-password")));

        Assertions.assertEquals("test", credentialsHomePage.getEditCredentialModalPassword());
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-save-changes-button")));

        credentialsHomePage.createOrEditCredential("google.com", "hehe", "hehe");
        resultPage = new ResultPage(webDriver);
        resultPage.clickToReturnHomeSuccess();

        webDriver.get(url);
        credentialsHomePage = new CredentialPage(webDriver);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));

        credentialsHomePage.clickOnCredentialsTab();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialTable")));

        String changedCredUrl = credentialsHomePage.getCredentialUrl();
        String changedCredUsername = credentialsHomePage.getCredentialUsername();
        String changedCredPassword = credentialsHomePage.getCredentialPassword();
        Assertions.assertEquals("google.com", changedCredUrl);
        Assertions.assertEquals("hehe", changedCredUsername);
        Assertions.assertNotEquals("hehe", changedCredPassword);

        //delete
        credentialsHomePage.clickOnDeleteCredential();
        webDriver.get(url);
        credentialsHomePage = new CredentialPage(webDriver);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));

        credentialsHomePage.clickOnCredentialsTab();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialTable")));

        Assertions.assertFalse(webDriver.getPageSource().contains("google.com"));

    }
}
