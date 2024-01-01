package jpabook.jpashop.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateMemberRequest {
    @NotBlank
    private String name;
    @Email
    private String email;
}
