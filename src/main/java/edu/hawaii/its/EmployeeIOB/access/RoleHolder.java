package edu.hawaii.its.EmployeeIOB.access;

import java.util.LinkedHashSet;
import java.util.Set;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class RoleHolder {

    private Set<GrantedAuthority> authorities = new LinkedHashSet<GrantedAuthority>();

    public void add(Role role) {
        authorities.add(new SimpleGrantedAuthority(role.longName()));
    }

    public Set<GrantedAuthority> getAuthorites() {
        return authorities;
    }
}
