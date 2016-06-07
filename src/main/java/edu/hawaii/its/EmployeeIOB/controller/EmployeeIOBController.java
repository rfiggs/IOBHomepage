package edu.hawaii.its.EmployeeIOB.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.hawaii.its.EmployeeIOB.access.Absence;
import edu.hawaii.its.EmployeeIOB.service.MysqlService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;
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
                            @RequestParam String startTime,
                            @RequestParam String endTime,
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

    @RequestMapping(value = {"/day"}, method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Map<String,List<Absence>> day(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String today = format.format(Calendar.getInstance().getTime());

        return getAbsences(today,today);
    }

    @RequestMapping(value = {"/week"}, method = RequestMethod.GET)
    public @ResponseBody
    List<List<Absence>> week(){
        List<List<Absence>> week = new ArrayList<List<Absence>>();

        return week;
    }

    @RequestMapping(value = {"/month"}, method = RequestMethod.GET)
    public @ResponseBody
    List<List<Absence>> month(){
        List<List<Absence>> month = new ArrayList<List<Absence>>();

        return  month;
    }
    @RequestMapping(value = {"/lookup"}, method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody Map<String,List<Absence>> getAbsences(
            @RequestParam String start,
            @RequestParam String end){
        MysqlService service = new MysqlService();
        Map<String,List<Absence>> map = service.getAbsences(start,end);
        service.close();
        return  map;
    }

}