package com.example.attendance.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class StudentWorkSemester {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//mysql의 auto increment
    private Long id;
    @ManyToOne
    @JsonBackReference
    private SiteUser siteUser;

    @ManyToOne
    @JsonBackReference
    private WorkSemester workSemester;
}
