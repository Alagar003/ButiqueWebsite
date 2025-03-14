package com.example.Boutique_Final.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EmailConfirmationRequest {

    public @NotBlank(message = "Email is required") @Email(message = "Invalid email format") String getEmail() {
        return email;
    }

    public void setEmail(@NotBlank(message = "Email is required") @Email(message = "Invalid email format") String email) {
        this.email = email;
    }

    public @NotBlank(message = "Confirmation code is required") String getConfirmationCode() {
        return confirmationCode;
    }

    public void setConfirmationCode(@NotBlank(message = "Confirmation code is required") String confirmationCode) {
        this.confirmationCode = confirmationCode;
    }

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Confirmation code is required")
    private String confirmationCode;
}
