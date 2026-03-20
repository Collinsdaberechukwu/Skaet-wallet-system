package com.collins.Skaet_wallet_system.event;

import com.collins.Skaet_wallet_system.model.ApplicationUser;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserCreatedEventListener implements ApplicationListener<@NonNull UserCreatedEvent> {


    @Override
    public void onApplicationEvent(@NonNull UserCreatedEvent event) {
        log.info("Event Received: {}", event);
        ApplicationUser applicationUser = (ApplicationUser) event.getSource();

        String wellComeMessage ="Welcome onboard to Skaet wallet" + applicationUser.getUserName();
        log.info("SMS Notification Sent: {}", wellComeMessage);

    }

    @Override
    public boolean supportsAsyncExecution() {
        return true;
    }
}
