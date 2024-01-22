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
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final DeptRepository deptRepository;

    private final WebClient webClient;//webhook
    @Autowired
    private final UserAttendanceRepository userAttendanceRepository;
    private final DeptService deptService;

    private final StudentWorkSemesterRepository studentWorkSemesterRepository;


    @Autowired
    public UserService(UserRepository userRepository,DeptRepository deptRepository, UserAttendanceRepository userAttendanceRepository, WebClient.Builder webClientBuilder, DeptService deptService, StudentWorkSemesterRepository studentWorkSemesterRepository) {
        this.userRepository = userRepository;
        this.deptRepository = deptRepository;
        this.userAttendanceRepository = userAttendanceRepository;
        this.webClient = webClientBuilder.baseUrl("https://teamroom.nate.com").build();
        this.deptService = deptService;
        this.studentWorkSemesterRepository = studentWorkSemesterRepository;
    }

    public SiteUser create(UserCreateRequest request){
        if (userRepository.existsByUserId(request.getUser_id())) {
            throw new DuplicateUserIdException("User with userId " + request.getUser_id() + " already exists");
        }

        if (!deptRepository.existsByDeptName(request.getDept())) {
            throw new EntityNotFoundException("Department with name " + request.getDept() + " not found");
        }
        SiteUser user = new SiteUser();
        user.setUserId(request.getUser_id());
        user.setUserPw(request.getUser_pw());
        user.setUserName(request.getUser_name());
        user.setUserPhoneNum(request.getUserPhoneNum());
        //부서이름으로 부서데이터 찾아서 부서 id를 user에 저장하기
        Dept dept = this.deptService.getDeptByName(request.getDept());
        user.setDept(dept);
        return this.userRepository.save(user);
    }

//    public String attendanceCreate(UserAttendanceRequest request){
//        SiteUser user = this.userRepository.findByUserId(request.getUserId());
//
//        int day_attendance_sum = userAttendanceRepository.findBySiteUserAndAttendanceDate(user, LocalDate.now()).size();
//
//        Attendance attendance = new Attendance();
//        attendance.setSiteUser(user);
//        attendance.setAttendanceTime(LocalDateTime.now());
//        attendance.setAttendanceDate(LocalDate.now());
//        attendance.setCreateId(null);
//        attendance.setCreateTime(null);
//
//        //새로 추가
//        attendance.setStatus((day_attendance_sum %2 == 0 ) ? "출근" : "퇴근");
//
//        this.userAttendanceRepository.save(attendance);
//
//        // 호출할 웹훅 URL
//        //String webhookUrl = "/api/webhook/4e71dbbb/8KG8Vhn3dJ9zZG1Y0j9qX0hs";// 팀룸 준형
//        String webhookUrl = "/api/webhook/2573721c/vlWBbePJ6k7kZ9rJMAvYapQe"; //팀룸 동현쌤
//
//        // 웹훅에 전달할 데이터 생성
//        String message = user.getUserName() + "님이 " + ((day_attendance_sum %2 == 0 ) ? "출근" : "퇴근") + "하였습니다.";
//        String payload = "{\"text\": \"" + message + "\"}";
//
//        // WebClient를 사용하여 POST 요청 전송
//        webClient.post()
//                .uri(uriBuilder -> uriBuilder.path(webhookUrl).build())
//                .bodyValue(payload)
//                .retrieve()
//                .bodyToMono(String.class)
//                .block();  // 간단하게 처리하기 위해 blocking 사용, 비동기 처리를 위해 subscribe() 사용
//
//        return (day_attendance_sum %2 == 0 ) ? "출근" : "퇴근";
//    }
//    public String attendanceCreate(UserAttendanceRequest request) {
//        SiteUser user = this.userRepository.findByUserId(request.getUserId());
//
//        if (user == null) {
//            throw new EntityNotFoundException("해당 userId의 유저를 찾을 수 없습니다.");
//        }
//
//        int day_attendance_sum = userAttendanceRepository.findBySiteUserAndAttendanceDate(user, LocalDate.now()).size();
//
//        Attendance attendance = new Attendance();
//        attendance.setSiteUser(user);
//        attendance.setAttendanceTime(LocalDateTime.now());
//        attendance.setAttendanceDate(LocalDate.now());
//        attendance.setCreateId(null);
//        attendance.setCreateTime(null);
//
//        // 새로 추가
//        attendance.setStatus((day_attendance_sum % 2 == 0) ? "출근" : "퇴근");
//
//        this.userAttendanceRepository.save(attendance);
//
//        // 호출할 웹훅 URL
//        // String webhookUrl = "/api/webhook/4e71dbbb/8KG8Vhn3dJ9zZG1Y0j9qX0hs"; // 팀룸 준형
//        String webhookUrl = "/api/webhook/2573721c/vlWBbePJ6k7kZ9rJMAvYapQe"; // 팀룸 동현쌤
//
//        // 웹훅에 전달할 데이터 생성
//        String message = user.getUserName() + "님이 " + ((day_attendance_sum % 2 == 0) ? "출근" : "퇴근") + "하였습니다.";
//        String payload = "{\"text\": \"" + message + "\"}";
//
//        // WebClient를 사용하여 POST 요청 전송
//        webClient.post()
//                .uri(uriBuilder -> uriBuilder.path(webhookUrl).build())
//                .bodyValue(payload)
//                .retrieve()
//                .bodyToMono(String.class)
//                .block();  // 간단하게 처리하기 위해 blocking 사용, 비동기 처리를 위해 subscribe() 사용
//
//        return (day_attendance_sum % 2 == 0) ? "출근" : "퇴근";
//    }
public Attendance attendanceCreate(UserAttendanceRequest request) {
    SiteUser user = this.userRepository.findByUserId(request.getUserId());

    if (user == null) {
        throw new EntityNotFoundException("해당 userId의 유저를 찾을 수 없습니다.");
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
    //attendance.setStatus((day_attendance_sum % 2 == 0) ? "출근" : "퇴근");

    this.userAttendanceRepository.save(attendance);

    // 호출할 웹훅 URL
    // String webhookUrl = "/api/webhook/4e71dbbb/8KG8Vhn3dJ9zZG1Y0j9qX0hs"; // 팀룸 준형
    String webhookUrl = "/api/webhook/2573721c/vlWBbePJ6k7kZ9rJMAvYapQe"; // 팀룸 동현쌤

    // 웹훅에 전달할 데이터 생성
    String message = user.getUserName() + "님이 " + request.getStatus() + "하였습니다.";
    String payload = "{\"text\": \"" + message + "\"}";

    // WebClient를 사용하여 POST 요청 전송
    webClient.post()
            .uri(uriBuilder -> uriBuilder.path(webhookUrl).build())
            .bodyValue(payload)
            .retrieve()
            .bodyToMono(String.class)
            .block();  // 간단하게 처리하기 위해 blocking 사용, 비동기 처리를 위해 subscribe() 사용

    return attendance;
}

    public Attendance requiredAttendanceCreate(UserAttendanceRequest request){
        SiteUser user = this.userRepository.findByUserId(request.getUserId());

        Attendance attendance = new Attendance();
        attendance.setSiteUser(user);
        attendance.setAttendanceTime(request.getAttendanceTime());
        attendance.setAttendanceDate(request.getAttendanceDate());
        attendance.setCreateId(null);
        attendance.setCreateTime(null);
        attendance.setStatus(request.getStatus());

        this.userAttendanceRepository.save(attendance);

        return attendance;
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

    public UserInfo getUserById(Long userId) {
        Optional<SiteUser> optionalUser = userRepository.findById(userId);
        UserInfo user = new UserInfo();

        if (optionalUser.isPresent()) {
            user.setId(optionalUser.get().getId());
            user.setUserId(optionalUser.get().getUserId());
            user.setUserName(optionalUser.get().getUserName());
            user.setUserPhoneNum(optionalUser.get().getUserPhoneNum());
            return user;
        } else {
            throw new EntityNotFoundException("해당 사용자를 찾을 수 없습니다.");
        }
    }

    public UserInfo updateUserById(Long userId, UserUpdateRequestDto request) {
        Optional<SiteUser> optionalUser = userRepository.findById(userId);
        //SiteUser user = new SiteUser();

        if (optionalUser.isPresent()) {
            SiteUser user = optionalUser.get();
            user.setUserId(request.getUserId());
            user.setUserName(request.getUserName());
            user.setUserPhoneNum(request.getUserPhoneNum());

            userRepository.save(user);

            UserInfo updatedUser = new UserInfo();
            updatedUser.setId(user.getId());
            updatedUser.setUserId(user.getUserId());
            updatedUser.setUserName(user.getUserName());
            updatedUser.setUserPhoneNum(user.getUserPhoneNum());

            return updatedUser;
        } else {
            throw new EntityNotFoundException("해당 사용자를 찾을 수 없습니다.");
        }
    }

    public void deleteUserById(Long userId) {
        Optional<SiteUser> optionalUser = userRepository.findById(userId);

        if (optionalUser.isPresent()) {
            SiteUser user = optionalUser.get();

            List<StudentWorkSemester> studentWorkSemesters = studentWorkSemesterRepository.findBySiteUserId(userId);
            studentWorkSemesterRepository.deleteAll(studentWorkSemesters);

            userRepository.delete(user);
        } else {
            throw new EntityNotFoundException("해당 사용자를 찾을 수 없습니다.");
        }
    }

    public AttendanceUpdateDto updateAttendance(AttendanceUpdateDto request) {
        if(request.getArriveAttendance() != null && request.getLeaveAttendance() != null)
        {
            Optional<Attendance> optionalArriveAttendance = userAttendanceRepository.findById(request.getArriveAttendance().getId());
            Optional<Attendance> optionalLeaveAttendance = userAttendanceRepository.findById(request.getLeaveAttendance().getId());

            if (optionalArriveAttendance.isPresent() && optionalLeaveAttendance.isPresent()) {
                Attendance arriveAttendance = optionalArriveAttendance.get();
                Attendance leaveAttendance = optionalLeaveAttendance.get();

                // Update fields based on request
                arriveAttendance.setAttendanceTime(request.getArriveAttendance().getAttendanceTime());
                arriveAttendance.setAttendanceDate(request.getArriveAttendance().getAttendanceDate());
                arriveAttendance.setStatus(request.getArriveAttendance().getStatus());

                leaveAttendance.setAttendanceTime(request.getLeaveAttendance().getAttendanceTime());
                leaveAttendance.setAttendanceDate(request.getLeaveAttendance().getAttendanceDate());
                leaveAttendance.setStatus(request.getLeaveAttendance().getStatus());

                //AttendancePairDto updatedAttendance = new AttendancePairDto(userAttendanceRepository.save(arriveAttendance), userAttendanceRepository.save(leaveAttendance));
                // Save the updated attendance

                AttendanceUpdateDto updatedAttendance = new AttendanceUpdateDto();
                
                userAttendanceRepository.save(arriveAttendance);
                userAttendanceRepository.save(leaveAttendance);

                return updatedAttendance;
            }
            else{
                throw new EntityNotFoundException("해당 출퇴근 기록을 찾을 수 없습니다.");
            }
        } else {
            throw new IllegalArgumentException("요청이 유효하지 않습니다.");
        }
    }

    public void deleteAttendance(Long attendanceId) {
        Optional<Attendance> optionalAttendance = userAttendanceRepository.findById(attendanceId);

        if (optionalAttendance.isPresent()) {
            Attendance attendance = optionalAttendance.get();

            // Delete the attendance record
            userAttendanceRepository.delete(attendance);
        } else {
            throw new EntityNotFoundException("해당 출퇴근 기록을 찾을 수 없습니다.");
        }
    }

//    public List<Attendance> getUserMonthlyAttendance(String userId, int year, int month) {
//        SiteUser user = userRepository.findByUserId(userId);
//
//        if (user == null) {
//            throw new EntityNotFoundException("해당 사용자를 찾을 수 없습니다.");
//        }
//
//        LocalDate startDate = LocalDate.of(year, month, 1);
//        LocalDate endDate = startDate.plusMonths(1).minusDays(1);
//
//        return userAttendanceRepository.findBySiteUserAndAttendanceDateBetween(user, startDate, endDate);
//    }

//    public List<Attendance> getUserMonthlyAttendance(Long userId, int year, int month) {
//        Optional<SiteUser> optionalUser = userRepository.findById(userId);
//
//        if (!optionalUser.isPresent()) {
//            throw new EntityNotFoundException("해당 사용자를 찾을 수 없습니다.");
//        }
//
//        LocalDate startDate = LocalDate.of(year, month, 1);
//        LocalDate endDate = startDate.plusMonths(1).minusDays(1);
//
//        return userAttendanceRepository.findBySiteUserAndAttendanceDateBetween(optionalUser.get(), startDate, endDate);
//    }
public List<AttendancePairDto> getUserMonthlyAttendancePairs(String userId, int year, int month) {
    SiteUser user = userRepository.findByUserId(userId);

    if (user == null) {
        throw new EntityNotFoundException("해당 사용자를 찾을 수 없습니다.");
    }

    LocalDate startDate = LocalDate.of(year, month, 1);
    LocalDate endDate = startDate.plusMonths(1).minusDays(1);

    List<Attendance> monthlyAttendance = userAttendanceRepository.findBySiteUserAndAttendanceDateBetween(user, startDate, endDate);

    // 출근과 퇴근을 시간순으로 정렬하여 묶어주는 메서드 호출
    return pairAttendances(monthlyAttendance);
}

    public List<AttendancePairDto> pairAttendances(List<Attendance> attendances) {
        List<AttendancePairDto> resultPairs = new ArrayList<>();
        System.out.println("--------------------------1------------------------");
        // 출근과 퇴근을 각각 모아놓을 맵
        Map<LocalDate, List<Attendance>> arriveMap = new HashMap<>();
        Map<LocalDate, List<Attendance>> leaveMap = new HashMap<>();

        // 출근과 퇴근을 나누어 맵에 추가
        for (Attendance attendance : attendances) {
            if ("출근".equals(attendance.getStatus())) {
                arriveMap.computeIfAbsent(attendance.getAttendanceDate(), key -> new ArrayList<>()).add(attendance);
            } else if ("퇴근".equals(attendance.getStatus())) {
                leaveMap.computeIfAbsent(attendance.getAttendanceDate(), key -> new ArrayList<>()).add(attendance);
            }
        }

        if(arriveMap.size() >= leaveMap.size())
        {
            // 출근과 퇴근을 쌍으로 묶음
            for (Map.Entry<LocalDate, List<Attendance>> arriveEntry : arriveMap.entrySet()) {
                LocalDate date = arriveEntry.getKey();
                List<Attendance> arriveList = arriveEntry.getValue();
                List<Attendance> leaveList = leaveMap.getOrDefault(date, new ArrayList<>());
                System.out.println("--------------------------5------------------------");
                // 시간순으로 정렬
                arriveList.sort(Comparator.comparing(Attendance::getAttendanceTime));
                leaveList.sort(Comparator.comparing(Attendance::getAttendanceTime));

                int i = 0, j = 0;
                while (i < arriveList.size() || j < leaveList.size()) {
                    Attendance arriveAttendance = (i < arriveList.size()) ? arriveList.get(i) : null;
                    Attendance leaveAttendance = (j < leaveList.size()) ? leaveList.get(j) : null;

                    if (leaveAttendance != null && (arriveAttendance == null || leaveAttendance.getAttendanceTime().isBefore(arriveAttendance.getAttendanceTime()))) {
                        arriveAttendance = null;
                        j++; // 다음 퇴근 데이터로 이동
                    } else if (arriveAttendance != null && (leaveAttendance == null || arriveAttendance.getAttendanceTime().isAfter(leaveAttendance.getAttendanceTime()))) {
                        leaveAttendance = null;
                        i++; // 다음 출근 데이터로 이동
                    } else {
                        // 쌍을 이룰 수 있는 경우
                        i++;
                        j++;
                    }

                    AttendancePairDto pair = new AttendancePairDto(arriveAttendance, leaveAttendance);
                    resultPairs.add(pair);

                }
            }
        }
        else
        {
            // 출근과 퇴근을 쌍으로 묶음
            for (Map.Entry<LocalDate, List<Attendance>> leaveEntry : leaveMap.entrySet()) {
                LocalDate date = leaveEntry.getKey();
                List<Attendance> arriveList = arriveMap.getOrDefault(date, new ArrayList<>());
                List<Attendance> leaveList = leaveEntry.getValue();
                System.out.println("--------------------------5------------------------");
                // 시간순으로 정렬
                arriveList.sort(Comparator.comparing(Attendance::getAttendanceTime));
                leaveList.sort(Comparator.comparing(Attendance::getAttendanceTime));

                int i = 0, j = 0;
                while (i < arriveList.size() || j < leaveList.size()) {
                    Attendance arriveAttendance = (i < arriveList.size()) ? arriveList.get(i) : null;
                    Attendance leaveAttendance = (j < leaveList.size()) ? leaveList.get(j) : null;

                    if (leaveAttendance != null && (arriveAttendance == null || leaveAttendance.getAttendanceTime().isBefore(arriveAttendance.getAttendanceTime()))) {
                        arriveAttendance = null;
                        j++; // 다음 퇴근 데이터로 이동
                    } else if (arriveAttendance != null && (leaveAttendance == null || arriveAttendance.getAttendanceTime().isAfter(leaveAttendance.getAttendanceTime()))) {
                        leaveAttendance = null;
                        i++; // 다음 출근 데이터로 이동
                    } else {
                        // 쌍을 이룰 수 있는 경우
                        i++;
                        j++;
                    }

                    AttendancePairDto pair = new AttendancePairDto(arriveAttendance, leaveAttendance);
                    resultPairs.add(pair);
                }
            }
        }


        return resultPairs;
    }

//
//    private MonthlyAttendanceResponseDto convertToMonthlyAttendanceResponse(Attendance attendance) {
//        MonthlyAttendanceResponseDto response = new MonthlyAttendanceResponseDto();
//        response.setId(attendance.getId());
//        response.setFormattedAttendanceTime(formatAttendanceTime(attendance.getAttendanceTime()));
//        response.setAttendanceDate(attendance.getAttendanceDate().toString());
//        response.setCreateId(attendance.getCreateId());
//        response.setCreateTime(attendance.getCreateTime());
//        response.setStatus(attendance.getStatus());
//        return response;
//    }
//
//    private String formatAttendanceTime(LocalDateTime attendanceTime) {
//        // 원하는 형식에 맞게 포맷을 지정하세요
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
//        return attendanceTime.format(formatter);
//    }

    public List<UserInfo> getCurrentAttendanceUsers(Long deptId) {
        Dept department = deptRepository.findById(deptId)
                .orElseThrow(() -> new EntityNotFoundException("해당 부서를 찾을 수 없습니다."));

        // 부서에 속한 유저 중, 오늘 출퇴근 기록이 홀수인 유저들을 조회
        List<SiteUser> allUsersInDepartment = userRepository.findByDept(department);
        List<UserInfo> currentAttendanceUsers = allUsersInDepartment.stream()
                .filter(user -> {
                    int dayAttendanceSum = userAttendanceRepository.findBySiteUserAndAttendanceDate(user, LocalDate.now()).size();
                    return dayAttendanceSum % 2 != 0; // 홀수인 경우 필터링
                })
                .map(this::convertToUserInfo) // SiteUser를 UserInfo로 변환
                .collect(Collectors.toList());

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
}
