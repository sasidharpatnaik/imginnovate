package org.imaginnovate.test.services;

import lombok.extern.slf4j.Slf4j;
import org.imaginnovate.test.common.I18Constants;
import org.imaginnovate.test.entities.Employee;
import org.imaginnovate.test.exceptions.DuplicateEmployeeIdException;
import org.imaginnovate.test.exceptions.EmployeeNotFoundException;
import org.imaginnovate.test.model.EmployeeModel;
import org.imaginnovate.test.repos.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;

@Slf4j(topic = "EMPLOYEE_SERVICE")
@Service
public class EmployeeService {
    @Autowired
    EmployeeRepository employeeRepository;
    private final MessageSource messageSource;

    public EmployeeService(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public Employee createEmplyoee(Employee employee) {
        employeeRepository.findByEmployeeId(employee.getEmployeeId())
                .ifPresent(emp -> {
                    throw new DuplicateEmployeeIdException(getLocalMessage(I18Constants.DUPLICATE_EMPLOYEE_ID.getKey(),String.valueOf(employee.getEmployeeId())));
                });
        return employeeRepository.save(employee);
    }

    public EmployeeModel getTaxes(String employeeId) throws ParseException {
        Optional<Employee>  optionalEmployee = employeeRepository.findByEmployeeId(employeeId);
        optionalEmployee.orElseThrow(()-> new EmployeeNotFoundException(getLocalMessage(I18Constants.EMPLOYEE_NOT_FOUND.getKey(),String.valueOf(employeeId))));
        Employee employee = optionalEmployee.get();
        EmployeeModel model = new EmployeeModel();
        if(joinedCurrentCalenderYear(employee.getDoj())){
            model.setEmployeeId(employeeId);
            model.setFirstName(employee.getFirstName());
            model.setLastName(employee.getLastName());
            double joiningMonthSalary = getJoiningMonthSalary(employee.getDoj(), employee.getSalary());
            double monthsSalary = noOfMonthsSalary(employee.getDoj(), employee.getSalary());
            double totalSalary = joiningMonthSalary+monthsSalary;
            model.setYearlySalary(totalSalary);
            double tax = calculateTax(totalSalary);
            model.setTaxAmount(tax);
        }
        return model;
    }

    private double calculateTax(double totalSalary) {
        double tax=0;
        if(totalSalary<=250000) {
            tax=0;
        } else if (totalSalary<=500000) {
            tax = (totalSalary - 250000) * 0.05;
        } else if (totalSalary<=1000000) {
            tax = 12500 + (totalSalary - 500000) * 0.1;
        }else {
            tax = 25000 + (totalSalary - 500000) * 0.2;
        }
        log.info("{}", tax);
        return tax;
    }

    private double noOfMonthsSalary(Date doj, double salary) throws ParseException {
        Date current = new Date();
        String date_string = "31-03-"+(current.getYear()+1900);
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date date = formatter.parse(date_string);
        int noOfMonths = 0;
        log.info("{}",(doj.getYear()+1900));
        log.info("{}",date.getYear());
        if((doj.getYear()+1900)==date.getYear())
            noOfMonths = 3-doj.getMonth();
        else
            noOfMonths = (12-doj.getMonth())+3;
        double monthsSalary = (noOfMonths-1)*salary;
        return monthsSalary;
    }

    private double getJoiningMonthSalary(Date doj, double salary) {
        int noOfDays = getNoOfDays((doj.getMonth()+1), (doj.getYear()+1900));
        double joiningMonthSalaryPerDay = salary/noOfDays;
        int workingDays = noOfDays-doj.getDate();
        workingDays = workingDays==0 ? 1 : workingDays;
        double joiningMonthSalary = workingDays*joiningMonthSalaryPerDay;
        log.info("Joining Month Salary : {}", joiningMonthSalary);
        return joiningMonthSalary;
    }

    private int getNoOfDays(int month, int year) {
        int days=0;
        if(month==1 || month==3 ||month==5 ||month==7||month==8||month==10||month==12) {
            days = 31;
        }else {
            if(month==4||month==6||month==9||month==11) {
                days=30;
            } else {
                if(month==2){
                    if(year%400==0&&year%100==0){
                        days=29;
                    }else{
                        if(year%4==0&&year%100!=0) {
                            days=29;
                        }else {
                            days=28;
                        }
                    }
                }
            }
        }
        return days;
    }

    private boolean joinedCurrentCalenderYear(Date doj) throws ParseException {
        Date current = new Date();
        String date_string = "31-03-"+(current.getYear()+1900);
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date date = formatter.parse(date_string);
        return doj.before(date);
    }


    private String getLocalMessage(String key, String...parms) {
        return messageSource.getMessage(key,parms,
                Locale.ENGLISH);
    }


}
