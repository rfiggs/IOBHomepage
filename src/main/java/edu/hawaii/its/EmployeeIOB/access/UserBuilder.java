package edu.hawaii.its.EmployeeIOB.access;

import edu.hawaii.its.EmployeeIOB.service.LookupService;

/**
 * Created by bobbyfiggs on 5/16/16.
 */
public class UserBuilder {
    public static User make(UhAttributes attributes){
        String username = attributes.getUid();
        Long uhnumber = Long.valueOf(attributes.getUhUuid());
        RoleHolder roleHolder = new RoleHolder();
        roleHolder.add(Role.ANONYMOUS);
        roleHolder.add(Role.UH);
        if(LookupService.isEmployee(String.valueOf(uhnumber))){
            roleHolder.add(Role.EMPLOYEE);
        }
        User user = new User(username,uhnumber,roleHolder.getAuthorites());
        user.setAttributes(attributes);

        return user;
    }
}
