package com.lifeshield.api;

import com.fasterxml.jackson.databind.JsonNode;

import com.lifeshield.auth.AppUser;
import com.lifeshield.auth.AppUserRepository;
import com.lifeshield.state.UserState;
import com.lifeshield.state.UserStateRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.HashMap;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/state")
public class StateController {

    private final UserStateRepository stateRepository;
    private final AppUserRepository userRepository;

    private static final Set<String> ALLOWED_KEYS = Set.of(
            "lifeshield_user_profile",
            "lifeshield_daily_log",
            "lifeshield_weekly_history",
            "lifeshield_vaccines",
            "lifeshield_reminders",
            "lifeshield_assessments"
    );

    public StateController(
            UserStateRepository stateRepository,
            AppUserRepository userRepository) {

        this.stateRepository = stateRepository;
        this.userRepository = userRepository;
    }

    // GET ALL DATA FOR CURRENT USER
    @GetMapping
    public ResponseEntity<?> getAllStates(
            HttpServletRequest request) {

        Long userId = getUserId(request);

        if (userId == null) {
            return unauthorized();
        }

        Map<String, JsonNode> result = new HashMap<>();

        for (UserState state :
                stateRepository.findAllByUser_Id(userId)) {

            result.put(
                    state.getStateKey(),
                    state.getPayload()
            );
        }

        return ResponseEntity.ok(result);
    }

    // GET ONE DATA CATEGORY
    @GetMapping("/{key}")
    public ResponseEntity<?> getState(
            @PathVariable String key,
            HttpServletRequest request) {

        Long userId = getUserId(request);

        if (userId == null) {
            return unauthorized();
        }

        if (!ALLOWED_KEYS.contains(key)) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Invalid state key"));
        }

        Optional<UserState> state =
                stateRepository.findByUser_IdAndStateKey(
                        userId, key
                );

        if (state.isEmpty()) {
            return ResponseEntity.ok(
                    Map.of("key", key, "data", Map.of())
            );
        }

        return ResponseEntity.ok(
                Map.of(
                        "key", key,
                        "data", state.get().getPayload()
                )
        );
    }

    // SAVE OR UPDATE ONE DATA CATEGORY
    @PutMapping("/{key}")
    public ResponseEntity<?> saveState(
            @PathVariable String key,
            @RequestBody JsonNode payload,
            HttpServletRequest request) {

        Long userId = getUserId(request);

        if (userId == null) {
            return unauthorized();
        }

        if (!ALLOWED_KEYS.contains(key)) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Invalid state key"));
        }

        if (payload == null || payload.isNull()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Payload cannot be empty"));
        }

        Optional<AppUser> optionalUser =
                userRepository.findById(userId);

        if (optionalUser.isEmpty()) {
            return unauthorized();
        }

        UserState state = stateRepository
                .findByUser_IdAndStateKey(userId, key)
                .orElseGet(UserState::new);

        state.setUser(optionalUser.get());
        state.setStateKey(key);

        // Store JSON directly in PostgreSQL JSONB
        state.setPayload(payload);

        stateRepository.save(state);

        return ResponseEntity.ok(
                Map.of(
                        "message", "State saved successfully",
                        "key", key
                )
        );
    }

    // DELETE ONE DATA CATEGORY
    @DeleteMapping("/{key}")
    public ResponseEntity<?> deleteState(
            @PathVariable String key,
            HttpServletRequest request) {

        Long userId = getUserId(request);

        if (userId == null) {
            return unauthorized();
        }

        if (!ALLOWED_KEYS.contains(key)) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Invalid state key"));
        }

        stateRepository
                .findByUser_IdAndStateKey(userId, key)
                .ifPresent(stateRepository::delete);

        return ResponseEntity.ok(
                Map.of("message", "State deleted successfully")
        );
    }

    // GET CURRENT LOGGED-IN USER ID
    private Long getUserId(HttpServletRequest request) {

        HttpSession session = request.getSession(false);

        if (session == null) {
            return null;
        }

        Object id = session.getAttribute("userId");

        if (id instanceof Number) {
            return ((Number) id).longValue();
        }

        return null;
    }

    // UNAUTHORIZED RESPONSE
    private ResponseEntity<?> unauthorized() {

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(Map.of(
                        "message",
                        "Please login first"
                ));
    }
}