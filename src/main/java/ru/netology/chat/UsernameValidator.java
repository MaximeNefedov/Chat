package ru.netology.chat;

public class UsernameValidator {
    /*
    Имя пользователя может состоять из 3–30 знаков и содержать буквы, цифры и символы.
    Имя пользователя может содержать буквы латинского алфавита (a–z), цифры (0–9) и точки (.).
    Запрещено использовать амперсанд (&), знаки равенства (=) и сложения (+), скобки (<>), запятую (,), символ подчеркивания (_), апостроф ('), дефис (-) и несколько точек подряд.
    Имя пользователя может начинаться и заканчиваться любым разрешенным символом, кроме точки (.).
     */
    // '.'
    private static final int DOT_VALUE = 46;

    // [0-9]
    private static final int SMALLEST_VALUE_OF_DIGIT = 48;
    private static final int LARGEST_VALUE_OF_DIGIT = 57;

    // [a-z]
    private static final int SMALLEST_VALUE_OF_CYRILLIC_CHARACTER_LOWER_CASE = 65;
    private static final int LARGEST_VALUE_OF_CYRILLIC_CHARACTER_LOWER_CASE = 90;

    // [A-Z]
    private static final int SMALLEST_VALUE_OF_CYRILLIC_CHARACTER_UPPER_CASE = 97;
    private static final int LARGEST_VALUE_OF_CYRILLIC_CHARACTER_UPPER_CASE = 122;

    public static boolean isValid(String username) {
        final int usernameLength = username.length();
        if (usernameLength < 3 || usernameLength > 30) return false;
        char[] chars = username.toCharArray();
        if (chars[0] == DOT_VALUE || chars[usernameLength - 1] == DOT_VALUE) {
            return false;
        }
        for (int i = 0; i < usernameLength; i++) {
            char currentChar = chars[i];
            if (currentChar >= SMALLEST_VALUE_OF_DIGIT && currentChar <= LARGEST_VALUE_OF_DIGIT) continue;
            if (currentChar >= SMALLEST_VALUE_OF_CYRILLIC_CHARACTER_LOWER_CASE
                    && currentChar <= LARGEST_VALUE_OF_CYRILLIC_CHARACTER_LOWER_CASE) continue;
            if (currentChar >= SMALLEST_VALUE_OF_CYRILLIC_CHARACTER_UPPER_CASE
                    && currentChar <= LARGEST_VALUE_OF_CYRILLIC_CHARACTER_UPPER_CASE) continue;
            if (i > 0 && currentChar == DOT_VALUE) {
                if (chars[i - 1] == DOT_VALUE) {
                    return false;
                }
            } else return false;
        }
        return true;
    }
}
