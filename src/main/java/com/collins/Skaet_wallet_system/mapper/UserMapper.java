package com.collins.Skaet_wallet_system.mapper;

import com.collins.Skaet_wallet_system.dtos.Request.UserCreationRequest;
import com.collins.Skaet_wallet_system.dtos.Response.UserCreationResponse;
import com.collins.Skaet_wallet_system.model.ApplicationUser;
import com.collins.Skaet_wallet_system.model.Wallet;

import java.time.LocalDateTime;

public class UserMapper {

    public static ApplicationUser mapToUser(UserCreationRequest userRequest){

        ApplicationUser applicationUser = new ApplicationUser();
        applicationUser.setUserName(userRequest.getUserName());
        applicationUser.setEmail(userRequest.getEmail());
        applicationUser.setPassword(userRequest.getPassword());
        applicationUser.setLastLogin(LocalDateTime.now());
        return applicationUser;
    }

    public static UserCreationResponse mapToUserResponse(ApplicationUser applicationUser){
        UserCreationResponse userCreationResponse = new UserCreationResponse();
        userCreationResponse.setUserId(applicationUser.getId());
        userCreationResponse.setUserName(applicationUser.getUserName());
        userCreationResponse.setEmail(applicationUser.getEmail());
        userCreationResponse.setWalletNumber(
                applicationUser.getWallets()
                        .stream()
                        .findFirst()
                        .map(Wallet::getWalletNumber)
                        .orElse("N/A")
        );
        userCreationResponse.setLastLogin(LocalDateTime.now());
        userCreationResponse.setCreatedAt(LocalDateTime.now());
        userCreationResponse.setRoleName(String.valueOf(applicationUser.getRole()));
        userCreationResponse.setUpdatedAt(applicationUser.getUpdatedAt());
        return userCreationResponse;
    }
}
