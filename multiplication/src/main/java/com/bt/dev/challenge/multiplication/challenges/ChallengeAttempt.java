package com.bt.dev.challenge.multiplication.challenges;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class ChallengeAttempt {

    private Long attemptId;
    private Long userId;
    private int factorA;
    private int factorB;
    private boolean correct;
    private int resultAttempt;
}
