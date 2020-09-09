package com.udacity.jwdnd.course1.cloudstorage.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class CredentialPage {
    @FindBy(id = "nav-credentials-tab")
    private WebElement credentialsTab;

    @FindBy(id = "credential-add-button")
    private WebElement addCredentialButton;

    @FindBy(id = "credential-edit-button")
    private WebElement editCredentialButton;

    @FindBy(id = "credential-delete-button")
    private WebElement deleteCredentialButton;

    @FindBy(id = "credential-save-changes-button")
    private WebElement credentialSaveChangesButton;

    @FindBy(id = "credential-url")
    private WebElement credentialUrl;

    @FindBy(id = "credential-username")
    private WebElement credentialUsername;

    @FindBy(id = "credential-password")
    private WebElement credentialPassword;

    @FindBy(xpath = "//*[@id=\"credentialTable\"]/tbody/tr")
    private WebElement firstCredential;

    public CredentialPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public void clickOnCredentialsTab() {
        this.credentialsTab.click();
    }

    public void clickOnAddNewCredential() {
        this.addCredentialButton.click();
    }

    public void clickOnEditCredential() {
        this.editCredentialButton.click();
    }

    public void clickOnDeleteCredential() {
        this.deleteCredentialButton.click();
    }

    public void createOrEditCredential(String url, String username, String password) {
        this.credentialUrl.clear();
        this.credentialUrl.sendKeys(url);

        this.credentialUsername.clear();
        this.credentialUsername.sendKeys(username);

        this.credentialPassword.clear();
        this.credentialPassword.sendKeys(password);

        this.credentialSaveChangesButton.click();
    }

    public String getEditCredentialModalPassword() {
        return this.credentialPassword.getAttribute("value");
    }

    public String getCredentialUrl() {
        return this.firstCredential.findElement(By.id("displayed-credential-url")).getText();
    }

    public String getCredentialUsername() {
        return this.firstCredential.findElement(By.id("displayed-credential-username")).getText();
    }

    public String getCredentialPassword() {
        return this.firstCredential.findElement(By.id("displayed-credential-password")).getText();
    }
}
