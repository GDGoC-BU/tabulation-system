package com.michaelcanonizado.backend.services;

import com.michaelcanonizado.backend.annotations.RequirePageantStatus;
import com.michaelcanonizado.backend.contexts.PageantContext;
import com.michaelcanonizado.backend.dtos.account.AccountCreateDTO;
import com.michaelcanonizado.backend.dtos.account.AccountCredentialDTO;
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
import com.michaelcanonizado.backend.utilities.AccountTypeConstants;
import com.michaelcanonizado.backend.utilities.CacheKeyBuilder;
import com.michaelcanonizado.backend.utilities.CacheNameConstants;
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

    @Autowired
    private  CacheService cacheService;

    @Autowired
    private CacheKeyBuilder cacheKeyBuilder;

    @Transactional
    public String loginAccount(AccountLoginDTO accountLoginDTO) {
        String username = accountLoginDTO.username();
        String password = accountLoginDTO.password();

        /* Verify credentials. If it's wrong, it will throw an exception which is already handled by
           GlobalExceptionHandler. */
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        /* If success, generate the token with their respective claims */
        AccountPrincipal principal = (AccountPrincipal) authentication.getPrincipal();
        AccountCredentialDTO account = principal.getAccount();

        Map<String, Object> extraClaims = new HashMap<>();

        /* Perform special checks on Judge */
        if (account.accountType().equals(AccountTypeConstants.JUDGE)) {
            Judge judge = judgeRepository.findById(account.id()).orElseThrow(() -> {
                return new AuthenticationFailedException(
                        "Couldn't find connected Judge entity to Account.",
                        ErrorCode.AUTHENTICATION_FAILED
                );
            });
            Pageant pageant = judge.getPageant();

            /* Judge can only log in if their assign pageant is ongoing. */
            if (!pageant.getStatus().equals(PageantStatus.ONGOING)) {
                throw new PageantStatusException(
                        "Assigned pageant must be ONGOING to login.",
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
        /* NOTE: The username(cache key) is in the security context, so handle caching programmatically. */
        /* Get account principal from SecurityContextHolder (The caller will be calling this endpoint
        with a jtw token attached. When they reach this method, the account principal has already been set) */
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AccountPrincipal accountPrincipal = (AccountPrincipal) authentication.getPrincipal();
        AccountCredentialDTO currentLoggedInAccount = accountPrincipal.getAccount();

        /* Since AccountSummaryDTO can't be mapped from AccountCredentialsDTO, refetch the
           account and map it to required DTO. */
        String CACHE_NAME = CacheNameConstants.AUTH;
        String CACHE_KEY = cacheKeyBuilder.build("accounts", currentLoggedInAccount.username());

        AccountSummaryDTO responseDTO = cacheService.get(
                CACHE_NAME,
                CACHE_KEY,
                AccountSummaryDTO.class
        );

        if (responseDTO == null) {
            responseDTO = accountMapper.toSummaryDTO(
                    accountRepository.findById(currentLoggedInAccount.id()).orElseThrow(() -> {
                        return new EntityNotFoundException(
                                "Account not found!",
                                ErrorCode.ENTITY_NOT_FOUND
                        );
                    })
            );

            cacheService.put(
                    CACHE_NAME,
                    CACHE_KEY,
                    responseDTO
            );
        }

        return responseDTO;
    }
}
