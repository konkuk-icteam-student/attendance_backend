package com.example.attendance.controller;

import com.example.attendance.exception.DuplicateUserIdException;
import com.example.attendance.model.dto.*;
import com.example.attendance.model.entity.SiteUser;
import com.example.attendance.model.entity.WorkSemester;
import com.example.attendance.service.UserSemesterService;
import com.example.attendance.service.UserService;
import com.example.attendance.service.WorkSemesterService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
//@Controller
@RestController
@RequestMapping("/semester")
public class WorkSemesterController {
    public final WorkSemesterService workSemesterService;

    public final UserSemesterService userSemesterService;

    @PostMapping("/new-semester")
    public ResponseEntity<Object> saveSemester(@RequestBody WorkSemesterCreateRequest request){
        try{
            WorkSemester createdSemester = this.workSemesterService.create(request);
            return new ResponseEntity<>(createdSemester, HttpStatus.CREATED);
        } catch (DuplicateUserIdException e) {
            return new ResponseEntity<>("이미 존재하는 학기입니다.", HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/list")
    public ResponseEntity<List<SemesterGetResponseDto>> getAllWorkSemesters() {
        List<SemesterGetResponseDto> workSemesters = workSemesterService.getAllWorkSemesters();
        return new ResponseEntity<>(workSemesters, HttpStatus.OK);
    }

    @DeleteMapping("/{semesterId}")
    public ResponseEntity<Object> deleteSemester(@PathVariable Long semesterId) {
        try {
            workSemesterService.deleteSemester(semesterId);
            return new ResponseEntity<>("학기가 성공적으로 삭제되었습니다.", HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>("해당 학기를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/semester")
    public ResponseEntity<Object> setSemester(@RequestBody UserSetSemesterRequest request){
        try{
            String message = this.userSemesterService.create(request);
            return new ResponseEntity<>(message,HttpStatus.CREATED);
        }catch(EntityNotFoundException e){
            return new ResponseEntity<>("해당 학기가 존재하지 않습니다.", HttpStatus.CONFLICT);
        }
    }
}
