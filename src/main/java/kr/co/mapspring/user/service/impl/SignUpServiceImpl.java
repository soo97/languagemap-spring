package kr.co.mapspring.user.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.mapspring.global.exception.user.EmailAlreadyExistsException;
import kr.co.mapspring.global.exception.user.PasswordConfirmMismatchException;
import kr.co.mapspring.global.exception.user.PhoneNumberAlreadyExistsException;
import kr.co.mapspring.global.exception.user.PrivacyTermsRequiredException;
import kr.co.mapspring.global.exception.user.ServiceTermsRequiredException;
import kr.co.mapspring.global.exception.user.TermsNotFoundException;
import kr.co.mapspring.user.dto.SignUpDto;
import kr.co.mapspring.user.entity.User;
import kr.co.mapspring.user.repository.UserRepository;
import kr.co.mapspring.user.service.SignUpService;
import kr.co.mapspring.user.terms.entity.Terms;
import kr.co.mapspring.user.terms.entity.UserTermsAgreement;
import kr.co.mapspring.user.terms.enums.TermsType;
import kr.co.mapspring.user.terms.repository.TermsRepository;
import kr.co.mapspring.user.terms.repository.UserTermsAgreementRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SignUpServiceImpl implements SignUpService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final TermsRepository termsRepository;

    private final UserTermsAgreementRepository userTermsAgreementRepository;

    @Override
    @Transactional
    public SignUpDto.ResponseSignUp signUp(SignUpDto.RequestSignUp request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException();
        }
        
        if (userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new PhoneNumberAlreadyExistsException();
        }

        if (!request.getPassword().equals(request.getPasswordConfirm())) {
            throw new PasswordConfirmMismatchException();
        }

        validateRequiredTerms(request);

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        User user = User.create(
                request.getEmail(),
                request.getName(),
                request.getBirthDate(),
                request.getAddress(),
                request.getPhoneNumber(),
                encodedPassword
        );

        User savedUser = userRepository.save(user);

        Terms serviceTerms = getActiveTerms(TermsType.SERVICE);
        Terms privacyTerms = getActiveTerms(TermsType.PRIVACY);
        Terms marketingTerms = getActiveTerms(TermsType.MARKETING);

        saveTermsAgreement(savedUser, serviceTerms, request.getServiceAgree());
        saveTermsAgreement(savedUser, privacyTerms, request.getPrivacyAgree());
        saveTermsAgreement(savedUser, marketingTerms, request.getMarketingAgree());

        return SignUpDto.ResponseSignUp.from(savedUser);
    }

    private void validateRequiredTerms(SignUpDto.RequestSignUp request) {
        if (!Boolean.TRUE.equals(request.getServiceAgree())) {
            throw new ServiceTermsRequiredException();
        }

        if (!Boolean.TRUE.equals(request.getPrivacyAgree())) {
            throw new PrivacyTermsRequiredException();
        }
    }

    private Terms getActiveTerms(TermsType termsType) {
        return termsRepository.findByTermTypeAndActiveTrue(termsType)
                .orElseThrow(TermsNotFoundException::new);
    }

    private void saveTermsAgreement(User user, Terms term, Boolean agreed) {
        UserTermsAgreement agreement = UserTermsAgreement.create(
                user,
                term,
                Boolean.TRUE.equals(agreed)
        );

        userTermsAgreementRepository.save(agreement);
    }
}