package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.pages.LoginPage;
import com.udacity.jwdnd.course1.cloudstorage.pages.NotePage;
import com.udacity.jwdnd.course1.cloudstorage.pages.ResultPage;
import com.udacity.jwdnd.course1.cloudstorage.pages.SignupPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class NoteTests {
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
    public void testNoteCRUD() {
        //create
        webDriver.get(url);
        NotePage notePage = new NotePage(webDriver);
        WebDriverWait wait = new WebDriverWait(webDriver, 1000);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));

        notePage.clickOnNotesTab();
        wait.until(ExpectedConditions.elementToBeClickable(By.id("note-add-button")));

        notePage.clickOnAddNewNote();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-save-change-button")));

        notePage.createOrEditNote("New Note", "This is a note");
        Assertions.assertEquals(url + "/note", webDriver.getCurrentUrl());

        ResultPage resultPage = new ResultPage(webDriver);
        Assertions.assertTrue(resultPage.isSuccessful());

        resultPage.clickToReturnHomeSuccess();

        webDriver.get(url);
        notePage = new NotePage(webDriver);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));

        notePage.clickOnNotesTab();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("userTable")));

        String firstNoteTitle = notePage.getNoteTitle();
        String firstNoteDesc = notePage.getNoteDescription();
        Assertions.assertEquals("New Note", firstNoteTitle);
        Assertions.assertEquals("This is a note", firstNoteDesc);

        //update
        notePage.clickOnEditNote();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-save-change-button")));

        notePage.createOrEditNote("Changed Note", "This is a changed note");
        resultPage = new ResultPage(webDriver);
        resultPage.clickToReturnHomeSuccess();
        webDriver.get(url);
        notePage = new NotePage(webDriver);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));

        notePage.clickOnNotesTab();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("userTable")));

        String changedNoteTitle = notePage.getNoteTitle();
        String changedNoteDesc = notePage.getNoteDescription();
        Assertions.assertEquals("Changed Note", changedNoteTitle);
        Assertions.assertEquals("This is a changed note", changedNoteDesc);

        //delete
        notePage.clickOnDeleteNote();
        webDriver.get(url);
        notePage = new NotePage(webDriver);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));

        notePage.clickOnNotesTab();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("userTable")));

        Assertions.assertFalse(webDriver.getPageSource().contains("Changed Note"));
    }
}
