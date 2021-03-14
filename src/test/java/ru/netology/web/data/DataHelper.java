package ru.netology.web.data;

import lombok.Value;
import lombok.val;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.DriverManager;
import java.sql.SQLException;

public class DataHelper {
    private DataHelper() {}

    @Value
    public static class AuthInfo {
        private String login;
        private String password;
    }

    public static AuthInfo getAuthInfo() {
        return new AuthInfo("vasya", "qwerty123");
    }

    public static AuthInfo getOtherAuthInfo(AuthInfo original) {

        return new AuthInfo("petya", "123qwerty");
    }

    public static AuthInfo getInvalidLoginAuthInfo() {
        return new AuthInfo("petya", "qwerty123");
    }

    public static AuthInfo getInvalidPasswordAuthInfo() {
        return new AuthInfo("vasya", "jlgflkja");
    }

    @Value
    public static class VerificationCode {
        private String code;

    }

    public static VerificationCode getVerificationCode(AuthInfo authInfo) {
        String id = "";
        String verificationCode = "";
        val idSQL = "SELECT id FROM users WHERE login='" + authInfo.login + "'";
        val runner = new QueryRunner();
        try (
                val conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/app",
                        "user", "pass"
                );
        ) {
            id = runner.query(conn, idSQL, new ScalarHandler<>());
            val codeSQL = "SELECT code FROM auth_codes WHERE created = (SELECT max(created) FROM auth_codes " +
                    "WHERE user_id='" + id + "')";
            verificationCode = runner.query(conn, codeSQL, new ScalarHandler<>());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return new VerificationCode(verificationCode);
    }

    public static VerificationCode getInvalidVerificationCode() {
        return new VerificationCode("12345");
    }

    public static void deleteData() {
        val deleteAuthCodesSQL = "DELETE FROM auth_codes";
        val deleteCardsSQL = "DELETE FROM cards";
        val deleteUsersSQL = "DELETE FROM users";
        val runner = new QueryRunner();

        try (
                val conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/app", "user", "pass"
                );
        ) {
            runner.update(conn, deleteAuthCodesSQL);
            runner.update(conn, deleteCardsSQL);
            runner.update(conn, deleteUsersSQL);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
