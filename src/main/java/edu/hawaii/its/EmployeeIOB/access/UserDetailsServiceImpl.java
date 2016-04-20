package edu.hawaii.its.EmployeeIOB.access;

import org.jasig.cas.client.validation.Assertion;
import org.springframework.security.cas.userdetails.AbstractCasAssertionUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service("EmployeeIOBUserDetailsService")
public class UserDetailsServiceImpl
extends AbstractCasAssertionUserDetailsService
{
    @Override
    protected UserDetails loadUserDetails(Assertion assertion)
    {
        RoleHolder roleHolder = new RoleHolder();
        roleHolder.add(Role.UH);
        return new User(assertion.getPrincipal().getName(), "", roleHolder.getAuthorites());
    }

}
