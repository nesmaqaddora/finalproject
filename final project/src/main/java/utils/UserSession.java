package utils;

public class UserSession {
    private static String currentUserRole = "";
    private static String currentUsername = "";

    public static void setRole(String role) { currentUserRole = role; }
    public static String getRole() { return currentUserRole; }

    public static void setUsername(String username) { currentUsername = username; }
    public static String getUsername() { return currentUsername; }
}