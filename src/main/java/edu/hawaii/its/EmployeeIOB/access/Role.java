package edu.hawaii.its.EmployeeIOB.access;

public enum Role {
    ANONYMOUS, UH, EMPLOYEE, MANAGER, ADMIN;

    public String longName() {
        return "ROLE_" + name();
    }

    public String toString() {
        return longName();
    }

    public static Role find(String name) {
        for (Role role : Role.values()) {
            if (role.name().equals(name)) {
                return role; // Found it.
            }
        }
        return null;
    }
}
