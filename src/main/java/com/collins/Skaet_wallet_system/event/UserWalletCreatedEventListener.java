package com.collins.Skaet_wallet_system.event;

import com.collins.Skaet_wallet_system.model.Wallet;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserWalletCreatedEventListener implements ApplicationListener<@NonNull UserWalletCreatedEvent> {



    @Override
    public void onApplicationEvent(@NonNull UserWalletCreatedEvent event) {
        log.info("Event Received: {}",event);
        Wallet wallet = (Wallet) event.getSource();

        String walletCreatedMessage = ("User wallet created" + wallet.getUser() + wallet.getWalletNumber());
        log.info("Wallet Creation SMS Notification Sent: {}", walletCreatedMessage);


    }

    @Override
    public boolean supportsAsyncExecution() {
        return true;
    }
}
