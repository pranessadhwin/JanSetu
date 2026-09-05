package com.jansetu4.portal.auth;

import com.jansetu4.portal.auth.dto.AuthResponse;
import com.jansetu4.portal.auth.dto.IndustryRegisterRequest;
import com.jansetu4.portal.auth.dto.LoginRequest;
import com.jansetu4.portal.auth.dto.RegisterRequest;
import com.jansetu4.portal.auth.dto.UniversityAdminRegisterRequest;
import com.jansetu4.portal.auth.entity.User;
import com.jansetu4.portal.auth.repository.UserRepository;
import com.jansetu4.portal.common.enums.Role;
import com.jansetu4.portal.common.exceptions.BadRequestException;
import com.jansetu4.portal.common.exceptions.ResourceNotFoundException;
import com.jansetu4.portal.industry.entity.Industry;
import com.jansetu4.portal.industry.repository.IndustryRepository;
import com.jansetu4.portal.university.repository.UniversityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final UniversityRepository universityRepository;
    private final IndustryRepository industryRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Transactional
    public AuthResponse registerCitizen(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email is already registered");
        }

        User user = userRepository.save(User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .role(Role.CITIZEN)
                .approved(true)
                .build());

        return buildAuthResponse(user);
    }

    /**
     * Self-registration for a university admin. The account is created in a
     * pending state and cannot log in until a Super Admin approves it.
     */
    @Transactional
    public void registerUniversityAdmin(UniversityAdminRegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email is already registered");
        }
        if (!universityRepository.existsById(request.getUniversityId())) {
            throw new ResourceNotFoundException("University not found");
        }

        userRepository.save(User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .role(Role.UNIVERSITY_ADMIN)
                .universityId(request.getUniversityId())
                .approved(false)
                .build());
    }

    /**
     * Self-registration for an industry/startup/CSR partner. Creates the
     * Industry organization profile and a pending admin account for it.
     */
    @Transactional
    public void registerIndustry(IndustryRegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email is already registered");
        }

        Industry industry = industryRepository.save(Industry.builder()
                .name(request.getIndustryName())
                .sector(request.getSector())
                .contactEmail(request.getContactEmail())
                .contactPhone(request.getContactPhone())
                .build());

        userRepository.save(User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .role(Role.INDUSTRY)
                .industryId(industry.getId())
                .approved(false)
                .build());
    }

    public AuthResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
        } catch (DisabledException ex) {
            throw new BadRequestException("Your account is pending admin approval. Please try again later.");
        }

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadRequestException("Invalid credentials"));

        return buildAuthResponse(user);
    }

    private AuthResponse buildAuthResponse(User user) {
        return AuthResponse.builder()
                .token(jwtUtil.generateToken(user))
                .userId(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .universityId(user.getUniversityId())
                .industryId(user.getIndustryId())
                .build();
    }
}
