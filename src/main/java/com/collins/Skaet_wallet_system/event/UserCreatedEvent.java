package com.collins.Skaet_wallet_system.event;

import com.collins.Skaet_wallet_system.model.ApplicationUser;
import org.springframework.context.ApplicationEvent;

public class UserCreatedEvent extends ApplicationEvent {
    public UserCreatedEvent(ApplicationUser applicationUser) {
        super(applicationUser);
    }
}
