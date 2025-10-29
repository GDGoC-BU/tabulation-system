package com.michaelcanonizado.backend.services;

import com.michaelcanonizado.backend.annotations.RequirePageantStatus;
import com.michaelcanonizado.backend.contexts.PageantContext;
import com.michaelcanonizado.backend.dtos.account.AccountCreateDTO;
import com.michaelcanonizado.backend.dtos.account.AccountLoginDTO;
import com.michaelcanonizado.backend.dtos.account.AccountSummaryDTO;
import com.michaelcanonizado.backend.dtos.judge.JudgeCreateDTO;
import com.michaelcanonizado.backend.exceptions.common.ErrorCode;
import com.michaelcanonizado.backend.exceptions.customs.*;
import com.michaelcanonizado.backend.mappers.AccountMapper;
import com.michaelcanonizado.backend.mappers.JudgeMapper;
import com.michaelcanonizado.backend.models.*;
import com.michaelcanonizado.backend.repositories.*;
import com.michaelcanonizado.backend.security.AccountPrincipal;
import com.michaelcanonizado.backend.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
    private PhaseRepository phaseRepository;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private JudgeMapper judgeMapper;

    @Autowired
    private PageantContext pageantContext;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public String loginAccount(AccountLoginDTO accountLoginDTO) {
        String username = accountLoginDTO.username();
        String password = accountLoginDTO.password();

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        AccountPrincipal principal = (AccountPrincipal) authentication.getPrincipal();
        Account account = principal.getAccount();

        Map<String, Object> extraClaims = new HashMap<>();

        if (account instanceof Judge judge) {
            Pageant pageant = judge.getPageant();

            /* Might also check if pageant is null, but this
               might need a custom exception. */

            /* Judge can only log in if their assign pageant is ongoing. */
            if (!pageant.getStatus().equals(PageantStatus.ONGOING)) {
                throw new PageantStatusException(
                        "Assigned pageant must be ONGOING to login",
                        ErrorCode.PAGEANT_ACCESS_DENIED
                );
            }

            /* Check if there is an ongoing phase. Else don't let judge login. */
            phaseRepository.findByStatus(PhaseSegmentStatus.ONGOING).orElseThrow(() -> {
                return new PhaseSegmentStatusException(
                        "No phase has started. Please wait for admin to open a phase.",
                        ErrorCode.ACCESS_DENIED
                );
            });

            /* Assigned pageant will be available in the token.
               Use this to fetch the assigned pageant to a judge. */
            extraClaims.put("assigned_pageant_id", pageant.getId());
        }
        return jwtService.generateToken(account, extraClaims);
    }

    public AccountSummaryDTO createAdmin(AccountCreateDTO request) {
        /* Use mapstruct here to encode! */
        String username = request.username();
        String password = request.password();
        String passwordHash = passwordEncoder.encode(password);
        Admin admin = new Admin(username, passwordHash);
        return accountMapper.toSummaryDTO(accountRepository.save(admin));
    }

    @RequirePageantStatus({
            PageantStatus.PREPARATION,
    })
    public AccountSummaryDTO createJudge(JudgeCreateDTO request) {
        /* Password encoding is done by mapper */
        Judge judge = judgeMapper.toEntity(request);

        UUID selectedPageantId = pageantContext.getId();
        Pageant pageant = pageantRepository.findById(selectedPageantId).orElseThrow(() -> {
            return new PageantAccessDeniedException(
                    "Pageant not found! Can't create judge",
                    ErrorCode.PAGEANT_ACCESS_DENIED
            );
        });
        /* Connect pageant */
        judge.setPageant(pageant);

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

        return accountMapper.toSummaryDTO(savedAccount);
    }

    public AccountSummaryDTO getCurrentAccount() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AccountPrincipal accountPrincipal = (AccountPrincipal) authentication.getPrincipal();
        Account currentLoggedInAccount = accountPrincipal.getAccount();
        return accountMapper.toSummaryDTO(currentLoggedInAccount);
    }
}
