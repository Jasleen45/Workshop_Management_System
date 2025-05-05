package utils;

public class Authentication {
    private static String currentUserRole = null;

    // Role constants
    public static final String ROLE_ADMIN = "Admin";
    public static final String ROLE_MANAGER = "Manager";
    public static final String ROLE_WORKER = "Worker";
    
    // Authenticate user and set their role
    public static boolean authenticate(String username, String password) {
        // Simple hardcoded authentication for demo purposes
        if ("admin".equals(username) && "admin123".equals(password)) {
            currentUserRole = ROLE_ADMIN;
            return true;
        } else if ("manager".equals(username) && "manager123".equals(password)) {
            currentUserRole = ROLE_MANAGER;
            return true;
        } else if ("worker".equals(username) && "worker123".equals(password)) {
            currentUserRole = ROLE_WORKER;
            return true;
        }
        return false;
    }
    
    // Get current user's role
    public static String getCurrentUserRole() {
        return currentUserRole;
    }
    
    // Check if current user has admin privileges
    public static boolean isAdmin() {
        return ROLE_ADMIN.equals(currentUserRole);
    }
    
    // Check if current user has manager privileges
    public static boolean isManager() {
        return ROLE_MANAGER.equals(currentUserRole);
    }
    
    // Check if current user has at least manager privileges (manager or admin)
    public static boolean hasManagerAccess() {
        return isAdmin() || isManager();
    }
    
    // Check if user is logged in
    public static boolean isLoggedIn() {
        return currentUserRole != null;
    }
    
    // Log out current user
    public static void logout() {
        currentUserRole = null;
    }
}
