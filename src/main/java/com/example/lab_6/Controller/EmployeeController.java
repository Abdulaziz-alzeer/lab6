package com.example.lab_6.Controller;


import com.example.lab_6.Api.ApiResponse;
import com.example.lab_6.Model.Employee;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/v1/employee/")
public class EmployeeController {

    ArrayList<Employee>listEmployees=new ArrayList<>();
    ArrayList<Employee>supervisorList=new ArrayList<>();
    ArrayList<Employee>ageList=new ArrayList<>();
    ArrayList<Employee>annualLeaveList=new ArrayList<>();



    @GetMapping("/get")
    public ArrayList<Employee> getEmployees(){
        return listEmployees;
    }
    @PostMapping("/add")
    public ResponseEntity addEmployees(@RequestBody @Valid Employee employee , Errors errors){
        if (errors.hasErrors()){
            String message=errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(new ApiResponse(message));
        }
        listEmployees.add(employee);
        return ResponseEntity.status(200).body(new ApiResponse("employee added successfully"));
    }
    @DeleteMapping("/delete/{index}")
    public ApiResponse deleteEmployee(@PathVariable int index ) {
        if (index >= 0 && index < listEmployees.size()) {
            listEmployees.remove(index);
            return new ApiResponse("employee deleted successfully");
        }return new ApiResponse("enter correct index");
    }

    @PutMapping("/update/{index}")
    public ResponseEntity updateEmployee(@PathVariable int index ,@RequestBody @Valid Employee employee,Errors errors) {
        if (errors.hasErrors()) {
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(new ApiResponse(message));
        } else if (index >= 0 && index < listEmployees.size()) {
            listEmployees.set(index, employee);
            return ResponseEntity.status(200).body(new ApiResponse("employee updated successfully"));
        }
        return ResponseEntity.status(404).body(new ApiResponse("Employee not found"));
    }

    @GetMapping("/search{position}")
    public ResponseEntity searchEmployee(@PathVariable String position){
        supervisorList.clear();
        if (!position.equalsIgnoreCase("supervisor") && !position.equalsIgnoreCase("coordinator")) {
            return ResponseEntity.status(400).body(new ApiResponse("Invalid position, must be 'supervisor' or 'coordinator'"));
        }
        for(Employee employee:listEmployees){
            if (employee.getPosition().equalsIgnoreCase(position)&& position.equalsIgnoreCase("supervisor")){
                supervisorList.add(employee);
            }
        }
        return ResponseEntity.ok(supervisorList);
    }

    @GetMapping("/search/age")
    public ResponseEntity ageEmployee(@RequestPart int minAge ,@RequestPart int maxAge ){
        if(maxAge<minAge||minAge>26){
            return ResponseEntity.status(400).body(new ApiResponse("enter correct age ;minAge must be greater than or equal to 26"));
        }
        ageList.clear();
        for(Employee employee:listEmployees){
            if (employee.getAge()>=minAge&&employee.getAge()<=maxAge){
                ageList.add(employee);
            }
        }
        return ResponseEntity.ok(ageList);
    }

    @PutMapping("/apply/annual-leave/{ID}")
    public ResponseEntity applyForAnnualLeave(@RequestParam String ID){
        for (Employee employee:listEmployees){
            if(employee.getID().equals(ID)){
                if (employee.isOnLeave()){
                    return ResponseEntity.status(200).body(new ApiResponse("employee is already is on leave "));
                }
                if (employee.getAnnualLeave()<1){
                    return ResponseEntity.status(200).body(new ApiResponse("not annual leave remaining"));
                }
                employee.setOnLeave(true);
                employee.setAnnualLeave(employee.getAnnualLeave()-1);
                return ResponseEntity.status(200).body(new ApiResponse("leave applied successfully"));
            }
        }
        return ResponseEntity.status(400).body(new ApiResponse("employee not found "));
    }
    @GetMapping("/no-leave")
    public ResponseEntity getEmployeeWithAnnualLeave(){
        annualLeaveList.clear();
        for(Employee employee:listEmployees){
            if(employee.getAnnualLeave()==0){
                annualLeaveList.add(employee);
            }
        }
        return ResponseEntity.ok(annualLeaveList);
    }
    @PostMapping("/promote/{Id}")
    public ResponseEntity promoteEmployee(@PathVariable String ID ,@RequestParam String requesterID){
        Employee requester=null;
        for (Employee employee:listEmployees){
            if (employee.getID().equals(requesterID)){
                requester=employee;
                break;
            }
        }
        if (requester==null){
            return ResponseEntity.status(400).body(new ApiResponse("requester not found"));
        }if (!requester.getPosition().equals("supervisor")){
            return ResponseEntity.status(400).body(new ApiResponse("only superVisors can promote employees"));
        }
        Employee employeeToPromote = null;
        for (Employee employee : listEmployees) {
            if (employee.getID().equals(ID)) {
                employeeToPromote = employee;
                break;
            }
        }if (employeeToPromote == null) {
            return ResponseEntity.status(404).body(new ApiResponse("Employee not found"));
        }
        if (employeeToPromote.getAge() < 30) {
            return ResponseEntity.badRequest().body(new ApiResponse("Employee must be at least 30 years old"));
        }
        if (employeeToPromote.isOnLeave()) {
            return ResponseEntity.badRequest().body(new ApiResponse("Employee cannot be on leave"));
        } employeeToPromote.setPosition("supervisor");
        return ResponseEntity.ok(new ApiResponse("Employee promoted successfully"));
    }
}
