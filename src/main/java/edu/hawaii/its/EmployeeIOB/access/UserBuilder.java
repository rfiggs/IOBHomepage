package edu.hawaii.its.EmployeeIOB.access;

import edu.hawaii.its.EmployeeIOB.service.LookupService;
import org.springframework.stereotype.Service;

/**
 * Created by bobbyfiggs on 5/16/16.
 */
@Service("userBuilder")
public class UserBuilder {


    public User make(UhAttributes attributes){
        String username = attributes.getUid();
        Long uhnumber = Long.valueOf(attributes.getUhUuid());
        RoleHolder roleHolder = new RoleHolder();
        roleHolder.add(Role.ANONYMOUS);
        roleHolder.add(Role.UH);
        String role = LookupService.getRole(String.valueOf(uhnumber));
        if(role.equalsIgnoreCase("Student")){
            roleHolder.add(Role.EMPLOYEE);
        }
        else if(role.equalsIgnoreCase("Staff")){
            roleHolder.add(Role.EMPLOYEE);
            roleHolder.add(Role.MANAGER);
        }
        User user = new User(username,uhnumber,roleHolder.getAuthorites());
        user.setAttributes(attributes);

        return user;
    }
    public void printsomething(){
        System.out.println("something");
    }
}
