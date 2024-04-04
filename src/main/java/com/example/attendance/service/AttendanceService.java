package com.example.attendance.service;

import com.example.attendance.exception.DuplicateUserIdException;
import com.example.attendance.model.dto.*;
import com.example.attendance.model.entity.Attendance;
import com.example.attendance.model.entity.Dept;
import com.example.attendance.model.entity.SiteUser;
import com.example.attendance.model.entity.StudentWorkSemester;
import com.example.attendance.model.repository.DeptRepository;
import com.example.attendance.model.repository.StudentWorkSemesterRepository;
import com.example.attendance.model.repository.UserAttendanceRepository;
import com.example.attendance.model.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@RequiredArgsConstructor
@Service
public class AttendanceService {
    private final UserRepository userRepository;
    private final UserAttendanceRepository userAttendanceRepository;
    private final WebSocketService webSocketService;

    private final WebHookService webHookService;

    private final DeptRepository deptRepository;

    public String attendanceCreate(UserAttendanceRequest request) {
        long startTime1 = System.currentTimeMillis();
        SiteUser user = this.userRepository.findByUserId(request.getUserId());

        if (user == null) {
            throw new EntityNotFoundException("해당 userId의 유저를 찾을 수 없습니다.");
        }

        if(!canStartTime(user)){
            throw new RuntimeException("출근한 지 1시간 미만입니다.");
        }

        //int day_attendance_sum = userAttendanceRepository.findBySiteUserAndAttendanceDate(user, LocalDate.now()).size();

        Attendance attendance = new Attendance();
        attendance.setSiteUser(user);
        attendance.setAttendanceTime(LocalDateTime.now());
        attendance.setAttendanceDate(LocalDate.now());
        attendance.setCreateId(null);
        attendance.setCreateTime(null);
        attendance.setStatus(request.getStatus());

        // 새로 추가
        //attendance.setStatus((day_attendance_sum % 2 == 0) ? "1" : "0");

        this.userAttendanceRepository.save(attendance);


        this.webSocketService.sendCurrentAttendanceUsers(user.getDept().getId(),getCurrentAttendanceUsers(user.getDept().getId()));
        long stopTime1 = System.currentTimeMillis();
        System.out.println("attendance duration time :"+(stopTime1 - startTime1));


        long startTime2 = System.currentTimeMillis();
        this.webHookService.sendWebhookMessage(user.getUserName(),request.getStatus());
        long stopTime2 = System.currentTimeMillis();
        System.out.println("attendance duration of hook time :"+(stopTime2 - startTime2));
        return "출퇴근 저장 성공";
    }

    public String requiredAttendanceCreate(UserAttendanceRequest request){
        SiteUser user = this.userRepository.findByUserId(request.getUserId());

        Attendance attendance = new Attendance();
        attendance.setSiteUser(user);
        attendance.setAttendanceTime(request.getAttendanceTime());
        attendance.setAttendanceDate(request.getAttendanceDate());
        attendance.setCreateId(null);
        attendance.setCreateTime(null);
        attendance.setStatus(request.getStatus());

        this.userAttendanceRepository.save(attendance);

        return "출퇴근 저장 성공";
    }

    @Autowired
    public List attendanceGet(){
        List<Attendance> resultList = userAttendanceRepository.findAllWithSiteUser();
        List<AttendanceGetResponse> attendanceGetResponseList = new ArrayList<>();
        for (Attendance attendance : resultList) {
            AttendanceGetResponse dto = new AttendanceGetResponse();
            dto.setAttendanceTime(attendance.getAttendanceTime());
            dto.setAttendanceDate(attendance.getAttendanceDate());
            dto.setUserId(attendance.getSiteUser().getUserId());
            dto.setDeptName(attendance.getSiteUser().getDept().getDeptName());
            dto.setUserName(attendance.getSiteUser().getUserName());
            attendanceGetResponseList.add(dto);
        }
        return attendanceGetResponseList;
    }

    public String updateAttendance(Long attendanceId, UserAttendanceRequest request) {
        Optional<Attendance> optionalAttendance = userAttendanceRepository.findById(attendanceId);

        if (optionalAttendance.isPresent()) {
            Attendance attendance = optionalAttendance.get();

            // Update fields based on request
            attendance.setAttendanceTime(request.getAttendanceTime());
            attendance.setAttendanceDate(request.getAttendanceDate());
            attendance.setStatus(request.getStatus());

            // Save the updated attendance
            userAttendanceRepository.save(attendance);
            return "근로시간 수정 성공";
        } else {
            throw new EntityNotFoundException("해당 출0 기록을 찾을 수 없습니다.");
        }
    }

    public void deleteAttendance(Long attendanceId) {
        Optional<Attendance> optionalAttendance = userAttendanceRepository.findById(attendanceId);

        if (optionalAttendance.isPresent()) {
            Attendance attendance = optionalAttendance.get();

            // Delete the attendance record
            userAttendanceRepository.delete(attendance);
        } else {
            throw new EntityNotFoundException("해당 출0 기록을 찾을 수 없습니다.");
        }
    }

    public AttendanceMonthResponseDto getUserMonthlyAttendancePairs(String userId, int year, int month) {
        SiteUser user = userRepository.findByUserId(userId);

        if (user == null) {
            throw new EntityNotFoundException("해당 사용자를 찾을 수 없습니다.");
        }

        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.plusMonths(1).minusDays(1);

        List<Attendance> monthlyAttendance = userAttendanceRepository.findBySiteUserAndAttendanceDateBetween(user, startDate, endDate);

        // 1과 0을 시간순으로 정렬하여 묶어주는 메서드 호출(출근:1, 퇴근:0)
        return pairAttendances(monthlyAttendance);
    }

    public AttendanceMonthResponseDto pairAttendances(List<Attendance> attendances) {
        List<AttendancePairDto> resultPairs = new ArrayList<>();
        Duration totalDuration = Duration.ZERO;
        Map<LocalDate, List<Attendance>> arriveMap = new HashMap<>();
        Map<LocalDate, List<Attendance>> leaveMap = new HashMap<>();

        for (Attendance attendance : attendances) {
            if ("1".equals(attendance.getStatus())) {
                arriveMap.computeIfAbsent(attendance.getAttendanceDate(), key -> new ArrayList<>()).add(attendance);
            } else if ("0".equals(attendance.getStatus())) {
                leaveMap.computeIfAbsent(attendance.getAttendanceDate(), key -> new ArrayList<>()).add(attendance);
            }
        }

        for (LocalDate date : arriveMap.keySet()) {
            List<Attendance> arriveList = arriveMap.getOrDefault(date, new ArrayList<>());
            List<Attendance> leaveList = leaveMap.getOrDefault(date, new ArrayList<>());

            arriveList.sort(Comparator.comparing(Attendance::getAttendanceTime));
            leaveList.sort(Comparator.comparing(Attendance::getAttendanceTime));

            int i = 0, j = 0;
            while (i < arriveList.size() || j < leaveList.size()) {
                Attendance arriveAttendance = (i < arriveList.size()) ? arriveList.get(i) : null;
                Attendance leaveAttendance = (j < leaveList.size()) ? leaveList.get(j) : null;

                if (leaveAttendance != null && (arriveAttendance == null || leaveAttendance.getAttendanceTime().isBefore(arriveAttendance.getAttendanceTime()))) {
                    // 퇴근 데이터만 있는 경우 짝을 만듦
                    AttendancePairDto pair = new AttendancePairDto(null, leaveAttendance);
                    resultPairs.add(pair);

                    if (pair.getWorkDuration() != null) {
                        totalDuration = totalDuration.plus(pair.getWorkDuration());
                    }
                    j++;
                } else if (arriveAttendance != null && (leaveAttendance == null || arriveAttendance.getAttendanceTime().isAfter(leaveAttendance.getAttendanceTime()))) {
                    // 출근 데이터만 있는 경우 짝을 만듦
                    AttendancePairDto pair = new AttendancePairDto(arriveAttendance, null);
                    resultPairs.add(pair);

                    if (pair.getWorkDuration() != null) {
                        totalDuration = totalDuration.plus(pair.getWorkDuration());
                    }
                    i++;
                } else {
                    // 출근과 퇴근 데이터가 모두 있는 경우
                    i++;
                    j++;

                    AttendancePairDto pair = new AttendancePairDto(arriveAttendance, leaveAttendance);
                    resultPairs.add(pair);

                    if (pair.getWorkDuration() != null) {
                        totalDuration = totalDuration.plus(pair.getWorkDuration());
                    }
                }
            }
        }

        // 짝이 없는 퇴근 데이터를 고려하여 추가
        for (LocalDate date : leaveMap.keySet()) {
            List<Attendance> leaveList = leaveMap.getOrDefault(date, new ArrayList<>());
            for (Attendance leaveAttendance : leaveList) {
                if (!arriveMap.containsKey(date)) {
                    // 짝이 없는 퇴근 데이터 추가
                    AttendancePairDto pair = new AttendancePairDto(null, leaveAttendance);
                    resultPairs.add(pair);

                    if (pair.getWorkDuration() != null) {
                        totalDuration = totalDuration.plus(pair.getWorkDuration());
                    }
                }
            }
        }

        resultPairs.sort(Comparator
                .comparing((AttendancePairDto pair) -> {
                    Attendance arriveAttendance = pair.getArriveAttendance();
                    if (arriveAttendance != null) {
                        return arriveAttendance.getAttendanceDate();
                    } else {
                        // ArriveAttendance가 NULL인 경우 LeaveAttendance의 날짜를 반환
                        return pair.getLeaveAttendance().getAttendanceDate();
                    }
                })
                .thenComparing(pair -> {
                    Attendance arriveAttendance = pair.getArriveAttendance();
                    if (arriveAttendance != null) {
                        return arriveAttendance.getAttendanceTime();
                    } else {
                        // ArriveAttendance가 NULL인 경우 LeaveAttendance의 시간을 반환
                        return pair.getLeaveAttendance().getAttendanceTime();
                    }
                }));

        AttendanceMonthResponseDto attendanceMonthData = new AttendanceMonthResponseDto();
        attendanceMonthData.setAttendanceDataList(resultPairs);
        attendanceMonthData.setTotalDuration(totalDuration);

        return attendanceMonthData;
    }


    public List<UserInfo> getCurrentAttendanceUsers(Long deptId) {
        Dept department = deptRepository.findById(deptId)
                .orElseThrow(() -> new EntityNotFoundException("해당 부서를 찾을 수 없습니다."));

        // 부서에 속한 유저 중, 현재 출근중인 유저들을 조회
        List<SiteUser> allUsersInDepartment = userRepository.findByDept(department);

        List<UserInfo> currentAttendanceUsers = new ArrayList<>();
        for (SiteUser user : allUsersInDepartment) {
            List<Attendance> dayAttendances = userAttendanceRepository.findBySiteUserAndAttendanceDate(user, LocalDate.now());
            dayAttendances.sort(Comparator.comparing(Attendance::getAttendanceTime).reversed()); // 시간 역순으로 정렬

            if (!dayAttendances.isEmpty()) {
                Attendance latestAttendance = dayAttendances.get(0);
                if ("1".equals(latestAttendance.getStatus())) { // 가장 최근 출근 기록의 status가 1이면 현재 출근중
                    currentAttendanceUsers.add(convertToUserInfo(user));
                }
            }
        }

        return currentAttendanceUsers;
    }

    private UserInfo convertToUserInfo(SiteUser user) {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(user.getId());
        userInfo.setUserId(user.getUserId());
        userInfo.setUserName(user.getUserName());
        userInfo.setUserPhoneNum(user.getUserPhoneNum());
        return userInfo;
    }

    private boolean canStartTime(SiteUser user){
        //사용자의 최근 출근 기록 가져오기
        List<Attendance> lastAttendances = userAttendanceRepository.findBySiteUserOrderByAttendanceTimeDesc(user);

        if (!lastAttendances.isEmpty()) {
            Attendance lastAttendance = lastAttendances.get(0);
            LocalDateTime currentTime = LocalDateTime.now();
            LocalDateTime lastAttendanceTime = lastAttendance.getAttendanceTime();

            // 현재 시간과 마지막 출근 시간의 차이 계산
            Duration duration = Duration.between(lastAttendanceTime, currentTime);

            // 1시간이 지났는지 확인
            if (duration.toHours() < 1) {
                return false; // 이미 출근을 찍은 상태
            }
        }
        return true; // 출근 가능한 상태
    }


}
