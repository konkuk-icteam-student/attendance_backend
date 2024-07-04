package com.example.attendance.domain.attendance.service;

import com.example.attendance.domain.attendance.dto.AttendanceGetResponse;
import com.example.attendance.domain.attendance.dto.AttendanceMonthResponseDto;
import com.example.attendance.domain.attendance.dto.AttendancePairDto;
import com.example.attendance.domain.attendance.dto.AttendanceResponse;
import com.example.attendance.domain.attendance.entity.Attendance;
import com.example.attendance.domain.attendance.entity.AttendanceRepository;
import com.example.attendance.domain.department.entity.Dept;
import com.example.attendance.domain.legacy.model.dto.UserAttendanceRequest;
import com.example.attendance.domain.legacy.model.repository.DeptRepository;
import com.example.attendance.domain.legacy.service.WebHookService;
import com.example.attendance.domain.legacy.service.WebSocketService;
import com.example.attendance.domain.member.dto.MemberInfoResponse;
import com.example.attendance.domain.member.entity.Member;
import com.example.attendance.domain.member.entity.MemberRepository;
import com.example.attendance.exception.ErrorCode;
import com.example.attendance.exception.NotFoundException;
import jakarta.persistence.EntityNotFoundException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class AttendanceService {

    private final MemberRepository memberRepository;
    private final AttendanceRepository attendanceRepository;
    private final WebSocketService webSocketService;

    private final WebHookService webHookService;

    private final DeptRepository deptRepository;

    public String attendanceCreate(UserAttendanceRequest request) {
        long startTime1 = System.currentTimeMillis();
        log.info("학번 : {}", request.getLoginId());
        Member member = memberRepository.findByLoginId(request.getLoginId())
            .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        if (member == null) {
            throw new EntityNotFoundException("해당 userId의 유저를 찾을 수 없습니다.");
        }

        //int day_attendance_sum = userAttendanceRepository.findBySiteUserAndAttendanceDate(user, LocalDate.now()).size();

        Attendance attendance = new Attendance();
        attendance.setMember(member);
        attendance.setAttendanceTime(LocalDateTime.now());
        attendance.setAttendanceDate(LocalDate.now());
        attendance.setCreateId(null);
        attendance.setCreateTime(null);
        attendance.setStatus(request.getStatus());

        // 새로 추가
        //attendance.setStatus((day_attendance_sum % 2 == 0) ? "1" : "0");

        this.attendanceRepository.save(attendance);

        webSocketService.sendCurrentAttendanceUsers(member.getDept().getId(),
            getCurrentAttendanceUsers(member.getDept().getId()));
        long stopTime1 = System.currentTimeMillis();
        System.out.println("attendance duration time :" + (stopTime1 - startTime1));

        long startTime2 = System.currentTimeMillis();
        this.webHookService.sendWebhookMessage(member.getName(), request.getStatus());
        long stopTime2 = System.currentTimeMillis();
        System.out.println("attendance duration of hook time :" + (stopTime2 - startTime2));
        return "출퇴근 저장 성공";
    }

    public String requiredAttendanceCreate(UserAttendanceRequest request) {
        Member member = memberRepository.findByLoginId(request.getLoginId())
            .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        Attendance attendance = new Attendance();
        attendance.setMember(member);
        attendance.setAttendanceTime(request.getAttendanceTime());
        attendance.setAttendanceDate(request.getAttendanceDate());
        attendance.setCreateId(null);
        attendance.setCreateTime(null);
        attendance.setStatus(request.getStatus());

        this.attendanceRepository.save(attendance);

        return "출퇴근 저장 성공";
    }

    public List attendanceGet() {
        List<Attendance> resultList = attendanceRepository.findAllWithMember();
        List<AttendanceGetResponse> attendanceGetResponseList = new ArrayList<>();
        for (Attendance attendance : resultList) {
            AttendanceGetResponse dto = new AttendanceGetResponse();
            dto.setAttendanceTime(attendance.getAttendanceTime());
            dto.setAttendanceDate(attendance.getAttendanceDate());
            dto.setUserId(attendance.getMember().getLoginId());
            dto.setDeptName(attendance.getMember().getDept().getDeptName());
            dto.setUserName(attendance.getMember().getName());
            attendanceGetResponseList.add(dto);
        }
        return attendanceGetResponseList;
    }

    public String updateAttendance(Long attendanceId, UserAttendanceRequest request) {
        Optional<Attendance> optionalAttendance = attendanceRepository.findById(attendanceId);

        if (optionalAttendance.isPresent()) {
            Attendance attendance = optionalAttendance.get();

            // Update fields based on request
            attendance.setAttendanceTime(request.getAttendanceTime());
            attendance.setAttendanceDate(request.getAttendanceDate());
            attendance.setStatus(request.getStatus());

            // Save the updated attendance
            attendanceRepository.save(attendance);
            return "근로시간 수정 성공";
        } else {
            throw new EntityNotFoundException("해당 출0 기록을 찾을 수 없습니다.");
        }
    }

    public void deleteAttendance(Long attendanceId) {
        Optional<Attendance> optionalAttendance = attendanceRepository.findById(attendanceId);

        if (optionalAttendance.isPresent()) {
            Attendance attendance = optionalAttendance.get();

            // Delete the attendance record
            attendanceRepository.delete(attendance);
        } else {
            throw new EntityNotFoundException("해당 출0 기록을 찾을 수 없습니다.");
        }
    }

    public AttendanceMonthResponseDto getUserMonthlyAttendancePairs(String loginId, int year, int month) {
        Member member = memberRepository.findByLoginId(loginId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        if (member == null) {
            throw new EntityNotFoundException("해당 사용자를 찾을 수 없습니다.");
        }

        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.plusMonths(1).minusDays(1);

        List<Attendance> monthlyAttendance = attendanceRepository.findByMemberAndAttendanceDateBetween(member,
            startDate, endDate);

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

                if (leaveAttendance != null && (arriveAttendance == null || leaveAttendance.getAttendanceTime()
                    .isBefore(arriveAttendance.getAttendanceTime()))) {
                    // 퇴근 데이터만 있는 경우 짝을 만듦
                    AttendancePairDto pair = new AttendancePairDto(null, leaveAttendance);
                    resultPairs.add(pair);

                    if (pair.getWorkDuration() != null) {
                        totalDuration = totalDuration.plus(pair.getWorkDuration());
                    }
                    j++;
                } else if (arriveAttendance != null && (leaveAttendance == null || arriveAttendance.getAttendanceTime()
                    .isAfter(leaveAttendance.getAttendanceTime()))) {
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
                AttendanceResponse arriveAttendance = pair.getArriveAttendance();
                if (arriveAttendance != null) {
                    return arriveAttendance.attendanceTime();
                } else {
                    // ArriveAttendance가 NULL인 경우 LeaveAttendance의 날짜를 반환
                    return pair.getLeaveAttendance().attendanceTime();
                }
            })
            .thenComparing(pair -> {
                AttendanceResponse arriveAttendance = pair.getArriveAttendance();
                if (arriveAttendance != null) {
                    return arriveAttendance.attendanceTime();
                } else {
                    // ArriveAttendance가 NULL인 경우 LeaveAttendance의 시간을 반환
                    return pair.getLeaveAttendance().attendanceTime();
                }
            }));

        AttendanceMonthResponseDto attendanceMonthData = new AttendanceMonthResponseDto();
        attendanceMonthData.setAttendanceDataList(resultPairs);
        attendanceMonthData.setTotalDuration(totalDuration);

        return attendanceMonthData;
    }


    public List<MemberInfoResponse> getCurrentAttendanceUsers(Long deptId) {
        Dept department = deptRepository.findById(deptId)
            .orElseThrow(() -> new EntityNotFoundException("해당 부서를 찾을 수 없습니다."));

        // 부서에 속한 유저 중, 현재 출근중인 유저들을 조회
        List<Member> allUsersInDepartment = memberRepository.findByDept(department);

        List<MemberInfoResponse> currentAttendanceUsers = new ArrayList<>();
        for (Member user : allUsersInDepartment) {
            List<Attendance> dayAttendances = attendanceRepository.findByMemberAndAttendanceDate(user, LocalDate.now());
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

    private MemberInfoResponse convertToUserInfo(Member member) {
        return MemberInfoResponse.from(member);
    }


}
