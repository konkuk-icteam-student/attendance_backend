package com.example.attendance.domain.legacy.service;

import com.example.attendance.domain.legacy.model.dto.SemesterGetResponseDto;
import com.example.attendance.domain.legacy.model.dto.WorkSemesterCreateRequest;
import com.example.attendance.domain.legacy.model.entity.StudentWorkSemester;
import com.example.attendance.domain.legacy.model.entity.WorkSemester;
import com.example.attendance.domain.legacy.model.repository.StudentWorkSemesterRepository;
import com.example.attendance.domain.legacy.model.repository.WorkSemesterRepository;
import com.example.attendance.exception.ErrorCode;
import com.example.attendance.exception.NotEqualsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class WorkSemesterService {
    private final WorkSemesterRepository workSemesterRepository;
    private final StudentWorkSemesterRepository studentWorkSemesterRepository;

    public WorkSemester create(WorkSemesterCreateRequest request) {
        /**
         * 예외 처리 다시하기 (임시)
         */
        if (workSemesterRepository.existsByYearAndSemester(request.getYear(), request.getSemester())) {
            throw new NotEqualsException(ErrorCode.SAMPLE_ERROR, "Semester " + request.getYear() + "년도" + request.getSemester() + "학기" + " already exists");
        }

        WorkSemester workSemester = new WorkSemester();
        workSemester.setYear(request.getYear());
        workSemester.setSemester((request.getSemester()));
        workSemester.setCreateId(null);
        workSemester.setCreateTime(null);
        return this.workSemesterRepository.save(workSemester);
    }

    public void deleteSemester(Long semesterId) {
        Optional<WorkSemester> optionalSemester = workSemesterRepository.findById(semesterId);

        if (optionalSemester.isPresent()) {
            WorkSemester semester = optionalSemester.get();

            // Find and delete studentWorkSemester records referencing the current semester
            List<StudentWorkSemester> studentWorkSemesters = studentWorkSemesterRepository.findByWorkSemesterId(semesterId);
            studentWorkSemesterRepository.deleteAll(studentWorkSemesters);

            workSemesterRepository.delete(semester);
        } else {
            throw new EntityNotFoundException("해당 학기를 찾을 수 없습니다.");
        }
    }

    public List getAllWorkSemesters() {
        List<WorkSemester> resultList = workSemesterRepository.findAll();

        List<SemesterGetResponseDto> semesterList = new ArrayList<>();

        for (WorkSemester semester : resultList) {
            SemesterGetResponseDto dto = new SemesterGetResponseDto();
            dto.setId(semester.getId());
            dto.setYear(semester.getYear());
            dto.setSemester(semester.getSemester());

//            List<UserInfo> userList = new ArrayList<>();

//            for(SiteUser siteUser : resultList.get(i++).getSiteUserList())
//            {
//                UserInfo user1 = new UserInfo();
//                user1.setUserId(siteUser.getUserId());
//                user1.setUserName(siteUser.getUserName());
//                user1.setUserPhoneNum(siteUser.getUserPhoneNum());
//                user1.setId(siteUser.getId());
//                userList.add(user1);
//            }
//            dto.setUsers(userList);
            semesterList.add(dto);
        }
        return semesterList;
    }
}
