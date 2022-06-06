package ru.netology.chat;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UsernameValidatorTest {
    /*
        Имя пользователя может состоять из 3–30 знаков и содержать буквы, цифры и символы.
        Имя пользователя может содержать буквы латинского алфавита (a–z), цифры (0–9) и точки (.).
        Запрещено использовать:
               -амперсанд (&),
               -знаки равенства (=) и сложения (+),
               -скобки (<>),
               -запятую (,),
               -символ подчеркивания (_),
               -апостроф ('),
               -дефис (-) и несколько точек подряд.
        Имя пользователя может начинаться и заканчиваться любым разрешенным символом, кроме точки (.).
    */
    @Test
    public void usernameShouldBeValid() {
        boolean isValid = UsernameValidator.isValid("Max");
        assertTrue(isValid);
    }

    @Test
    public void usernameShouldBeInvalidBecauseIsTooShort() {
        boolean isValid = UsernameValidator.isValid("Ma");
        assertFalse(isValid);
    }

    @Test
    public void usernameShouldBeInvalidBecauseIsTooLong() {
        boolean isValid = UsernameValidator.isValid("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        assertFalse(isValid);
    }

    @Test
    public void usernameShouldBeInvalidBecauseItHasAmpersand() {
        boolean isValid = UsernameValidator.isValid("Ma&x");
        assertFalse(isValid);
    }

    @Test
    public void usernameShouldBeInvalidBecauseItHasApostrophe() {
        boolean isValid = UsernameValidator.isValid("Ma`x");
        assertFalse(isValid);
    }

    @Test
    public void usernameShouldBeInvalidBecauseItHasEqualSign() {
        boolean isValid = UsernameValidator.isValid("=Max");
        assertFalse(isValid);
    }

    @Test
    public void usernameShouldBeInvalidBecauseItHasPlusSign() {
        boolean isValid = UsernameValidator.isValid("+Max");
        assertFalse(isValid);
    }

    @Test
    public void usernameShouldBeInvalidBecauseItHasUnderscoreSign() {
        boolean isValid = UsernameValidator.isValid("Ma_x");
        assertFalse(isValid);
    }

    @Test
    public void usernameShouldBeInvalidBecauseItStartsWithDot() {
        boolean isValid = UsernameValidator.isValid(".Max");
        assertFalse(isValid);
    }

    @Test
    public void usernameShouldBeInvalidBecauseItEndsWithDot() {
        boolean isValid = UsernameValidator.isValid("Max.");
        assertFalse(isValid);
    }

    @Test
    public void usernameShouldBeInvalidBecauseItHasSeveralDotsInRow() {
        boolean isValid = UsernameValidator.isValid("M..ax");
        assertFalse(isValid);
    }

    @Test
    public void usernameShouldBeInvalidBecauseItHasCommaSign() {
        boolean isValid = UsernameValidator.isValid("Ma,x");
        assertFalse(isValid);
    }

    @Test
    public void usernameShouldBeInvalidBecauseItHasHyphenSign() {
        boolean isValid = UsernameValidator.isValid("Max_");
        assertFalse(isValid);
    }

    @Test
    public void usernameShouldBeInvalidBecauseItHasTriangleBrackets() {
        assertAll(
                () -> {
                    boolean isValid = UsernameValidator.isValid("Ma<x");
                    assertFalse(isValid);
                },
                () -> {
                    boolean isValid = UsernameValidator.isValid("Ma>x");
                    assertFalse(isValid);
                }
        );
    }
    @Test
    public void usernameShouldBeInvalidBecauseItHasRoundBrackets() {
        assertAll(
                () -> {
                    boolean isValid = UsernameValidator.isValid("(Max");
                    assertFalse(isValid);
                },
                () -> {
                    boolean isValid = UsernameValidator.isValid("M)ax");
                    assertFalse(isValid);
                }
        );
    }
}