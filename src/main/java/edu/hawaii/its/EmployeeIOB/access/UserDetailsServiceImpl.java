package edu.hawaii.its.EmployeeIOB.access;

import org.jasig.cas.client.validation.Assertion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.cas.userdetails.AbstractCasAssertionUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service("EmployeeIOBUserDetailsService")
public class UserDetailsServiceImpl extends AbstractCasAssertionUserDetailsService {

    @Autowired
    private UserBuilder userBuilder;
    @Override
    protected UserDetails loadUserDetails(Assertion assertion) {
        User user = userBuilder.make(new UhAttributes(assertion.getPrincipal().getAttributes()));

        return user;
    }

}
