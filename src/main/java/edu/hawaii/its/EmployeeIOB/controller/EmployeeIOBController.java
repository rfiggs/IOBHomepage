package edu.hawaii.its.EmployeeIOB.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.hawaii.its.EmployeeIOB.access.User;
import edu.hawaii.its.EmployeeIOB.service.LookupService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

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

    @RequestMapping(value = {"/add"}, method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView add(@RequestParam String username,
                            @RequestParam String startTime,
                            @RequestParam String endTime,
                            @RequestParam String notes) {
        String number = LookupService.getUhNumber(username);
        ModelAndView mav;
        if(number.equals("")){
             mav = new ModelAndView("add","result","not in employee table");

        }
        else if(LookupService.validateAdd(number,startTime,endTime)) {
            LookupService.addAbsence(number,startTime,endTime,notes);
             mav = new ModelAndView("add", "result","Success!");

        }
        else{
             mav = new ModelAndView("add", "result","Invalid date range");

        }
        return mav;
    }
}