package com.collins.Skaet_wallet_system.event;

import com.collins.Skaet_wallet_system.model.Wallet;
import org.springframework.context.ApplicationEvent;

public class UserWalletCreatedEvent extends ApplicationEvent {
    public UserWalletCreatedEvent(Wallet wallet) {
        super(wallet);
    }
}
