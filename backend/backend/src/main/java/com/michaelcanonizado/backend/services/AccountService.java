package com.michaelcanonizado.backend.services;

import com.michaelcanonizado.backend.annotations.RequirePageantStatus;
import com.michaelcanonizado.backend.contexts.PageantContext;
import com.michaelcanonizado.backend.dtos.account.AccountCreateDTO;
import com.michaelcanonizado.backend.dtos.account.AccountLoginDTO;
import com.michaelcanonizado.backend.dtos.account.AccountSummaryDTO;
import com.michaelcanonizado.backend.exceptions.common.ErrorCode;
import com.michaelcanonizado.backend.exceptions.customs.EntityNotFoundException;
import com.michaelcanonizado.backend.exceptions.customs.PageantAccessDeniedException;
import com.michaelcanonizado.backend.mappers.AccountMapper;
import com.michaelcanonizado.backend.models.*;
import com.michaelcanonizado.backend.repositories.*;
import com.michaelcanonizado.backend.security.AccountPrincipal;
import com.michaelcanonizado.backend.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private JudgeRepository judgeRepository;

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private CriterionRepository criterionRepository;

    @Autowired
    private ScoreRepository scoreRepository;

    @Autowired
    private PageantRepository pageantRepository;

    @Autowired
    private AccountMapper mapper;

    @Autowired
    private PageantContext pageantContext;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public String loginAccount(AccountLoginDTO accountLoginDTO) {
        String username = accountLoginDTO.username();
        String password = accountLoginDTO.password();

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        AccountPrincipal principal = (AccountPrincipal) authentication.getPrincipal();
        Account account = principal.getAccount();
        return jwtService.generateToken(account);
    }

    public AccountSummaryDTO createAdmin(AccountCreateDTO request) {
        String username = request.username();
        String password = request.password();
        String passwordHash = encoder.encode(password);
        Admin admin = new Admin(username, passwordHash);
        return mapper.toSummaryDTO(accountRepository.save(admin));
    }

    @RequirePageantStatus({
            PageantStatus.PREPARATION,
    })
    public AccountSummaryDTO createJudge(AccountCreateDTO request) {
        String username = request.username();
        String password = request.password();
        String passwordHash = encoder.encode(password);

        UUID selectedPageantId = pageantContext.getId();
        Pageant pageant = pageantRepository.findById(selectedPageantId).orElseThrow(() -> {
            return new PageantAccessDeniedException(
                    "Pageant not found! Can't perform operation",
                    ErrorCode.PAGEANT_ACCESS_DENIED
            );
        });

        /* Create judge through account */
        Judge judge = new Judge(username, passwordHash, pageant);
        Account savedAccount = accountRepository.save(judge);

        /* Refetch judge to get a managed entity to
           safely establish relationships */
        Judge savedJudge = judgeRepository.findById(savedAccount.getId()).orElseThrow(() -> {
            return new EntityNotFoundException("Error creating judge!", ErrorCode.ENTITY_NOT_FOUND);
        });

        /* Pre-generate the scores for the new judge */
        List<Candidate> candidates = candidateRepository.findAll();
        List<Criterion> criteria = criterionRepository.findAll();
        List<Score> newScores = new ArrayList<>();
        candidates.forEach(candidate -> {
            criteria.forEach(criterion -> {
                newScores.add(new Score(0, savedJudge, candidate, criterion));
            });
        });
        /* Batch save to minimize insert queries */
        scoreRepository.saveAll(newScores);

        return mapper.toSummaryDTO(savedAccount);
    }
}
