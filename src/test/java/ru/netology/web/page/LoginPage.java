package ru.netology.web.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.Keys;
import ru.netology.web.data.DataHelper;

import static com.codeborne.selenide.Selenide.$;

public class LoginPage {
    private SelenideElement loginField = $("[data-test-id=login] input");
    private SelenideElement passwordField = $("[data-test-id=password] input");
    private SelenideElement loginButton = $("[data-test-id=action-login]");
    private SelenideElement error = $("[data-test-id=error-notification]");
    private SelenideElement errorContent = $("[data-test-id=error-notification] .notification__content");

    public void deletePassword() {
        passwordField.sendKeys(Keys.chord(Keys.CONTROL,"A"));
        passwordField.sendKeys(Keys.chord(Keys.DELETE));
    }

    public VerificationPage validLogin(DataHelper.AuthInfo info) {
        loginField.setValue(info.getLogin());
        passwordField.setValue(info.getPassword());
        loginButton.click();
        return new VerificationPage();
    }

    public void invalidLoginPassword(DataHelper.AuthInfo info) {
        loginField.setValue(info.getLogin());
        passwordField.setValue(info.getPassword());
        loginButton.click();
        error.should(Condition.visible);
        errorContent.shouldHave(Condition.exactText("Ошибка! Неверно указан логин или пароль"));
    }

    public void blockingAfterEntering3IncorrectPasswords(DataHelper.AuthInfo info) {
        loginField.setValue(info.getLogin());
        passwordField.setValue(info.getPassword());
        loginButton.click();
        error.should(Condition.visible);
        errorContent.shouldHave(Condition.exactText("Ошибка! Неверно указан логин или пароль"));
        deletePassword();
        passwordField.setValue(info.getPassword());
        loginButton.click();
        error.should(Condition.visible);
        errorContent.shouldHave(Condition.exactText("Ошибка! Неверно указан логин или пароль"));
        deletePassword();
        passwordField.setValue(info.getPassword());
        loginButton.click();
        error.should(Condition.visible);
        errorContent.shouldHave(Condition.exactText("Система заблокирована"));
    }
}
