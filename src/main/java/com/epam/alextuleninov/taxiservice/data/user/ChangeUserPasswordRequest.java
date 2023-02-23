package com.epam.alextuleninov.taxiservice.data.user;

public record ChangeUserPasswordRequest(

        String oldPassword,
        String newPassword,
        String confirmPassword
) {
}
