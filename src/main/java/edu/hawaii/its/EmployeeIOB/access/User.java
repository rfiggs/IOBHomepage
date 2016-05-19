package edu.hawaii.its.EmployeeIOB.access;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class User extends org.springframework.security.core.userdetails.User {

	public static final long serialVersionUID = 2L;
    private Long uhuuid;
    private UhAttributes attributes;

    public User(String username, Collection<GrantedAuthority> authorities) {
        super(username, "", authorities);
        setUhuuid(null);
    }

    public User(String username, Long uhuuid, Collection<GrantedAuthority> authorities) {
        super(username, "", authorities);
        setUhuuid(uhuuid);
    }
    
    public String getUid() {
        return getUsername();
    }

    public Long getUhuuid() {
        return uhuuid;
    }

    public void setUhuuid(Long uhuuid) {
        this.uhuuid = uhuuid;
    }

    public String getAttribute(String name) {
        return attributes.getValue(name);
    }
    
    public UhAttributes getAttributes() {
        return attributes;
    }
    
    public void setAttributes(UhAttributes attributes) {
        this.attributes = attributes;
    }
    
    public String getName() {
        return attributes.getValue("cn");
    }
    
    public boolean hasRole(Role role) {
        return getAuthorities().contains(new SimpleGrantedAuthority(role.longName()));
    }

    public String toString() {
        return "User [uid: " + getUid() + ", uhuuid: " + getUhuuid()
                + ", super-class: " + super.toString() + "]";
    }
}
