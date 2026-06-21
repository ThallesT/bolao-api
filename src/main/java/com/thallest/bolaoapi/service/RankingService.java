package com.thallest.bolaoapi.service;

import com.thallest.bolaoapi.domain.Grupo;
import com.thallest.bolaoapi.domain.MatchStatus;
import com.thallest.bolaoapi.domain.Palpite;
import com.thallest.bolaoapi.domain.Partida;
import com.thallest.bolaoapi.domain.UserEntity;
import com.thallest.bolaoapi.repository.PalpiteRepository;
import com.thallest.bolaoapi.web.dto.RankingEntryResponse;
import com.thallest.bolaoapi.web.dto.RankingResponse;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class RankingService {

    private static final int EXACT_SCORE_POINTS = 3;
    private static final int OUTCOME_POINTS = 1;

    private final GrupoService grupoService;
    private final PalpiteRepository palpiteRepository;

    public RankingService(GrupoService grupoService, PalpiteRepository palpiteRepository) {
        this.grupoService = grupoService;
        this.palpiteRepository = palpiteRepository;
    }

    public RankingResponse getGroupRanking(Long groupId) {
        Grupo grupo = grupoService.getEntity(groupId);
        List<Palpite> guesses = palpiteRepository.findByGroupId(groupId);

        Map<Long, RankingAccumulator> standings = new LinkedHashMap<>();

        for (UserEntity member : grupo.getMembers()) {
            standings.put(member.getId(), new RankingAccumulator(member.getId(), member.getName()));
        }

        for (Palpite guess : guesses) {
            RankingAccumulator accumulator = standings.computeIfAbsent(
                guess.getUser().getId(),
                ignored -> new RankingAccumulator(guess.getUser().getId(), guess.getUser().getName())
            );

            accumulator.totalGuesses++;

            Partida match = guess.getMatch();
            if (match.getStatus() != MatchStatus.FINISHED
                || match.getHomeScore() == null
                || match.getAwayScore() == null) {
                continue;
            }

            accumulator.scoredGuesses++;

            if (isExactScore(guess, match)) {
                accumulator.points += EXACT_SCORE_POINTS;
                accumulator.exactHits++;
                continue;
            }

            if (isCorrectOutcome(guess, match)) {
                accumulator.points += OUTCOME_POINTS;
                accumulator.outcomeHits++;
            }
        }

        List<RankingEntryResponse> ranking = standings.values().stream()
            .sorted(Comparator
                .comparingInt(RankingAccumulator::points).reversed()
                .thenComparingInt(RankingAccumulator::exactHits).reversed()
                .thenComparingInt(RankingAccumulator::outcomeHits).reversed()
                .thenComparing(RankingAccumulator::userName))
            .map(accumulator -> new RankingEntryResponse(
                accumulator.userId(),
                accumulator.userName(),
                accumulator.points(),
                accumulator.exactHits(),
                accumulator.outcomeHits(),
                accumulator.scoredGuesses(),
                accumulator.totalGuesses()
            ))
            .toList();

        return new RankingResponse(
            grupo.getId(),
            grupo.getName(),
            grupo.getChampionship().getId(),
            ranking
        );
    }

    private boolean isExactScore(Palpite guess, Partida match) {
        return guess.getHomeScoreGuess().equals(match.getHomeScore())
            && guess.getAwayScoreGuess().equals(match.getAwayScore());
    }

    private boolean isCorrectOutcome(Palpite guess, Partida match) {
        return Integer.signum(guess.getHomeScoreGuess() - guess.getAwayScoreGuess())
            == Integer.signum(match.getHomeScore() - match.getAwayScore());
    }

    private static final class RankingAccumulator {
        private final Long userId;
        private final String userName;
        private int points;
        private int exactHits;
        private int outcomeHits;
        private int scoredGuesses;
        private int totalGuesses;

        private RankingAccumulator(Long userId, String userName) {
            this.userId = userId;
            this.userName = userName;
        }

        private Long userId() {
            return userId;
        }

        private String userName() {
            return userName;
        }

        private int points() {
            return points;
        }

        private int exactHits() {
            return exactHits;
        }

        private int outcomeHits() {
            return outcomeHits;
        }

        private int scoredGuesses() {
            return scoredGuesses;
        }

        private int totalGuesses() {
            return totalGuesses;
        }
    }
}

