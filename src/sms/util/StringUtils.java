package sms.util;

public class StringUtils {

    public static String toTitleCase(String input) {
        if (input == null || input.isEmpty()) return input;
        StringBuilder titleCase = new StringBuilder();
        boolean nextTitleCase = true;

        for (char c : input.toCharArray()) {
            if (Character.isSpaceChar(c)) {
                nextTitleCase = true;
            } else if (nextTitleCase) {
                c = Character.toTitleCase(c);
                nextTitleCase = false;
            } else {
                c = Character.toLowerCase(c);
            }
            titleCase.append(c);
        }
        return titleCase.toString();
    }

    // Optional extras
    public static String getFirstName(String fullName) {
        if (fullName == null || fullName.isEmpty()) return "";
        String[] parts = fullName.split("\\s+");
        return parts[0];
    }

    public static String getEmailDomain(String email) {
        if (email == null || !email.contains("@")) return "";
        return email.substring(email.indexOf("@") + 1);
    }

    public static String concatenateNameEmail(String name, String email) {
        return name + " (" + email + ")";
    }
}