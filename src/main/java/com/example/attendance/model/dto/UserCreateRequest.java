package com.example.attendance.model.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class UserCreateRequest {
    @NotEmpty(message="아이디는 필수항목입니다.")
    private String user_id;

    @NotEmpty(message="비밀번호는 필수항목입니다.")
    private String user_pw;

    @NotEmpty(message = "이름은 필수항목입니다.")
    private String user_name;

    @NotEmpty(message = "전화번호는 필수항목 입니다.")
    private String userPhoneNum;

    @NotEmpty(message = "부서는 필수항목입니다.")
    private String dept;

}
