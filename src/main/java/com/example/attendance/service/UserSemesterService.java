package com.example.attendance.service;

import com.example.attendance.model.dto.UserAttendanceRequest;
import com.example.attendance.model.dto.UserSetSemesterRequest;
import com.example.attendance.model.entity.Attendance;
import com.example.attendance.model.entity.SiteUser;
import com.example.attendance.model.entity.StudentWorkSemester;
import com.example.attendance.model.entity.WorkSemester;
import com.example.attendance.model.repository.UserRepository;
import com.example.attendance.model.repository.UserSemesterRepository;
import com.example.attendance.model.repository.WorkSemesterRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserSemesterService {
    private final WorkSemesterRepository workSemesterRepository;
    private final UserRepository userRepository;

    private final UserSemesterRepository userSemesterRepository;

    public String create(UserSetSemesterRequest request){
        WorkSemester workSemester;
        SiteUser user;
        if(!workSemesterRepository.existsByYearAndSemester(request.getYear(),request.getSemester()))
        {
            throw new EntityNotFoundException("semester " + request.getYear() + "."+request.getSemester()+"not found");
        }

        workSemester = workSemesterRepository.findByYearAndSemester(request.getYear(), request.getSemester());
        user = userRepository.findByUserId(request.getUserId());

        StudentWorkSemester studentWorkSemester = new StudentWorkSemester();
        studentWorkSemester.setWorkSemester(workSemester);
        studentWorkSemester.setSiteUser(user);
        userSemesterRepository.save(studentWorkSemester);

        return "학기 설정 완료";
    }


}
