package com.example.attendance.domain.department.controller;

import com.example.attendance.domain.legacy.model.dto.DeptGetResponseDto;
import com.example.attendance.domain.legacy.model.dto.DeptCreateRequest;
import com.example.attendance.domain.legacy.model.dto.DeptUpdateRequestDto;
import com.example.attendance.domain.legacy.model.dto.DeptUpdateResponseDto;
import com.example.attendance.domain.department.entity.Dept;
import com.example.attendance.domain.department.service.DeptService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
//@Controller
@RestController
@RequestMapping("/dept")
public class DeptController {

    public final DeptService deptService;

    @PostMapping("/new-dept")
    public ResponseEntity<Object> saveDept(@RequestBody DeptCreateRequest request){
        try{
            Dept createdDept = this.deptService.create(request);
            return new ResponseEntity<>(createdDept, HttpStatus.CREATED);
        } catch (EntityExistsException e) {
            return new ResponseEntity<>("이미 존재하는 부서입니다.", HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/list")
    public ResponseEntity<List<DeptGetResponseDto>> getAllDepartments() {
        List<DeptGetResponseDto> allDepartments = deptService.getAllDepartments();
        return new ResponseEntity<>(allDepartments, HttpStatus.OK);
    }

    @Transactional
    @GetMapping("/{deptId}")
    public ResponseEntity<DeptGetResponseDto> getDepartments(@PathVariable Long deptId) {
        DeptGetResponseDto dept = deptService.getDepartments(deptId);
        return new ResponseEntity<>(dept, HttpStatus.OK);
    }

    @PatchMapping("/{deptId}")
    public ResponseEntity<Object> updateDepartments(@PathVariable Long deptId, @RequestBody DeptUpdateRequestDto request) {
        try {
            DeptUpdateResponseDto updatedDept = deptService.update(deptId, request);
            return new ResponseEntity<>(updatedDept, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>("부서를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
        } catch (EntityExistsException e){
            return new ResponseEntity<>("이미 존재하는 부서입니다.", HttpStatus.CONFLICT);
        }

    }

    @DeleteMapping("/{deptId}")
    public ResponseEntity<Object> deleteDept(@PathVariable Long deptId) {
        try {
            this.deptService.delete(deptId);
            return new ResponseEntity<>("부서가 성공적으로 삭제되었습니다.", HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>("해당 ID로 찾을 수 없는 부서입니다.", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("부서 삭제 중에 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
