package org.imaginnovate.test.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Employee Id is mandatory")
    @Column(length = 20, nullable = false, unique = true)
    private String employeeId;

    @NotBlank(message = "First name is mandatory")
    @Column(length = 50, nullable = false)
    private String firstName;

    @NotBlank(message = "Last name is mandatory")
    @Column(length = 50, nullable = false)
    private String lastName;

    @NotBlank(message = "Email is mandatory")
    @Column(length = 30, nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date doj;

    @Column(nullable = false)
    private double salary;
}
