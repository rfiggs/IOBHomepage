package edu.hawaii.its.EmployeeIOB.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.hawaii.its.EmployeeIOB.access.Absence;
import edu.hawaii.its.EmployeeIOB.access.Role;
import edu.hawaii.its.EmployeeIOB.access.User;
import edu.hawaii.its.EmployeeIOB.service.MysqlService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import java.util.*;

@Controller
public class EmployeeIOBController {

	@RequestMapping(value = {"/", "/landing"}, method = RequestMethod.GET)
    public String landing(Model model) {
        return "landing";
    }
	
	@RequestMapping(value = {"/day_page"}, method = RequestMethod.GET)
    public String day_page() {
        return "day_page";
    }

	
	@RequestMapping(value = {"/logout"}, method = RequestMethod.GET)
    public String logout(HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "landing";
    }

    @RequestMapping(value = {"/add"}, method = RequestMethod.POST, produces ="application/text")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody String add(@RequestParam String username,
                            @RequestParam long startTime,
                            @RequestParam long endTime,
                            @RequestParam String notes) {
        MysqlService s = new MysqlService();
        String result = s.validateAdd(username,startTime,endTime);
        if(result.equalsIgnoreCase("SUCCESS")){
            s.addAbsence(username,startTime,endTime,notes);

        }
        s.close();
        return result;

    }
    @RequestMapping(value = {"/remove"}, method = RequestMethod.POST)
    public @ResponseBody boolean remove(@RequestParam String absid){
        MysqlService s = new MysqlService();
        s.removeAbsence(absid);
        s.close();
        return true;
    }

    @RequestMapping(value = {"/denied"}, method = RequestMethod.GET)
    public ModelAndView denied(HttpServletRequest request, HttpServletResponse response){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        String message = "No record found.  Please speak with your supervisor.";
        return  new ModelAndView("landing","denied",message);
    }

    @RequestMapping(value = {"/lookup"}, method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody Map<String,List<Absence>> getAbsences(
            @RequestParam long start,
            @RequestParam long end){
        MysqlService service = new MysqlService();
        Map<String,List<Absence>> map = service.getAbsences(start,end);
        service.close();
        return  map;
    }

    @RequestMapping(value = {"/isManager"}, method = RequestMethod.POST)
    public @ResponseBody
    boolean isManager(){
        User user = (User)(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        return (user.hasRole(Role.MANAGER));

    }

}