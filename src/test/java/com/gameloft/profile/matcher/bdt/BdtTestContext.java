package com.gameloft.profile.matcher.bdt;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Data
@Component
@Scope("cucumber-glue")
public class BdtTestContext {

    private UUID playerId;
}
