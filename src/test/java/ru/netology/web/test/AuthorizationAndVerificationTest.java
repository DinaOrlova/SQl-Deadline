package ru.netology.web.test;

import lombok.val;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;

public class AuthorizationAndVerificationTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999/");
    }

    @AfterAll
    static void clearDB() {
        DataHelper.deleteData();
    }

    @Test
    void shouldAuthorizationAndVerification() {
        val loginPage = new LoginPage();
        val authInfo = DataHelper.getAuthInfo();
        val verificationPage = loginPage.validLogin(authInfo);
        val verificationCode = DataHelper.getVerificationCode(authInfo);
        val dashboardPage = verificationPage.validVerify(verificationCode);
        dashboardPage.personalArea();
    }

    @Test
    void shouldNotAuthorizationWithInvalidLogin() {
        val loginPage = new LoginPage();
        val authInfo = DataHelper.getInvalidLoginAuthInfo();
        loginPage.invalidLoginPassword(authInfo);
    }

    @Test
    void shouldNotAuthorizationWithInvalidPassword() {
        val loginPage = new LoginPage();
        val authInfo = DataHelper.getInvalidPasswordAuthInfo();
        loginPage.invalidLoginPassword(authInfo);
    }

    @Test
    void shouldBlockAfterEntering3IncorrectPasswords() {
        val loginPage = new LoginPage();
        val authInfo = DataHelper.getInvalidPasswordAuthInfo();
        loginPage.blockingAfterEntering3IncorrectPasswords(authInfo);
    }

    @Test
    void shouldNotVerifyWithInvalidVerificationCode() {
        val loginPage = new LoginPage();
        val authInfo = DataHelper.getAuthInfo();
        val verificationPage = loginPage.validLogin(authInfo);
        val verificationCode = DataHelper.getInvalidVerificationCode();
        verificationPage.invalidVerify(verificationCode);
    }
}
