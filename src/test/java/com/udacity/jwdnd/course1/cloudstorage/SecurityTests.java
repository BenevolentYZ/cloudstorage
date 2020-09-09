package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.pages.LoginPage;
import com.udacity.jwdnd.course1.cloudstorage.pages.NotePage;
import com.udacity.jwdnd.course1.cloudstorage.pages.SignupPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SecurityTests {
    @LocalServerPort
    private int port;
    private WebDriver webDriver;
    private String url;

    @BeforeAll
    public static void beforeAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void beforeEach() {
        this.webDriver = new ChromeDriver();
        this.url = "http://localhost:" + this.port;
    }

    @AfterEach
    public void afterEach() {
        if (this.webDriver != null) {
            webDriver.quit();
        }
    }

    @Test
    public void testUnauthorizedRequests() {
        webDriver.get(url);
        Assertions.assertEquals((url + "/login"), webDriver.getCurrentUrl());
        webDriver.get(url + "/credential");
        Assertions.assertEquals((url + "/login"), webDriver.getCurrentUrl());
        webDriver.get(url + "/note");
        Assertions.assertEquals((url + "/login"), webDriver.getCurrentUrl());
        webDriver.get(url + "/file");
        Assertions.assertEquals((url + "/login"), webDriver.getCurrentUrl());
    }

    @Test
    public void testAuthentication() {
        webDriver.get(url + "/signup");
        SignupPage signupPage = new SignupPage(webDriver);
        signupPage.signup("zyt", "zyt", "yz", "yz");

        webDriver.get(url + "/login");
        LoginPage loginPage = new LoginPage(webDriver);
        loginPage.login("yz", "yz");

        webDriver.get(url + "/");
        NotePage notePage = new NotePage(webDriver);
        notePage.clickOnLogout();


        webDriver.get(url + "/");
        String expectedUrl = url + "/login";
        Assertions.assertEquals(expectedUrl, webDriver.getCurrentUrl());
    }
}
