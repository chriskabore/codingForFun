package com.bt.dev.challenge.multiplication.services;

import com.bt.dev.challenge.multiplication.challenges.Challenge;

public interface ChallengeGeneratorService {
    /**
     *
     * @return a randomly generated Challenge with factors between 11 et 99.
     */
    Challenge generateRandomChallenge();
}
