package edu.hawaii.its.EmployeeIOB.access;

import edu.hawaii.its.EmployeeIOB.service.MysqlService;
import org.springframework.stereotype.Service;

/**
 * Created by bobbyfiggs on 5/16/16.
 */
@Service("userBuilder")
public class UserBuilder {


    public User make(UhAttributes attributes){
        String username = attributes.getUid();
        String uhnumber = attributes.getUhUuid();
        RoleHolder roleHolder = new RoleHolder();
        roleHolder.add(Role.ANONYMOUS);
        roleHolder.add(Role.UH);
        MysqlService s = new MysqlService();
        String role = s.getRoleFromUhnumber(uhnumber);
        s.close();
        if(role.equalsIgnoreCase("Student")){
            roleHolder.add(Role.EMPLOYEE);
        }
        else if(role.equalsIgnoreCase("Staff")){
            roleHolder.add(Role.EMPLOYEE);
            roleHolder.add(Role.MANAGER);
        }
        /*if(username.equalsIgnoreCase("rfiggs")){
            roleHolder = new RoleHolder();
            roleHolder.add(Role.EMPLOYEE);

        }*/
        User user = new User(username,Long.valueOf(uhnumber),roleHolder.getAuthorites());
        user.setAttributes(attributes);

        return user;
    }
}
