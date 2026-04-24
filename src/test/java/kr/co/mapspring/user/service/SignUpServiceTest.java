package kr.co.mapspring.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import kr.co.mapspring.global.exception.CustomException;
import kr.co.mapspring.user.dto.SignUpDto;
import kr.co.mapspring.user.entity.User;
import kr.co.mapspring.user.enums.UserRole;
import kr.co.mapspring.user.enums.UserStatus;
import kr.co.mapspring.user.repository.UserRepository;
import kr.co.mapspring.user.service.impl.SignUpServiceImpl;
import kr.co.mapspring.user.terms.entity.Terms;
import kr.co.mapspring.user.terms.entity.UserTermsAgreement;
import kr.co.mapspring.user.terms.enums.TermsType;
import kr.co.mapspring.user.terms.repository.TermsRepository;
import kr.co.mapspring.user.terms.repository.UserTermsAgreementRepository;

@ExtendWith(MockitoExtension.class)
class SignUpServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TermsRepository termsRepository;

    @Mock
    private UserTermsAgreementRepository userTermsAgreementRepository;

    @InjectMocks
    private SignUpServiceImpl signUpService;

    @Test
    @DisplayName("중복되지 않은 이메일이면 회원가입에 성공한다")
    void signUpSuccess() {
        // given
        SignUpDto.RequestSignUp request = new SignUpDto.RequestSignUp(
                "홍길동",
                LocalDate.of(2000, 1, 1),
                "서울시 강남구",
                "010-1234-5678",
                "test@naver.com",
                "1234",
                "1234",
                true,
                true,
                false
        );

        given(userRepository.existsByEmail("test@naver.com"))
                .willReturn(false);

        given(passwordEncoder.encode("1234"))
                .willReturn("encodedPassword");

        User savedUser = User.builder()
                .userId(1L)
                .email("test@naver.com")
                .name("홍길동")
                .birthDate(LocalDate.of(2000, 1, 1))
                .address("서울시 강남구")
                .phoneNumber("010-1234-5678")
                .passwordHash("encodedPassword")
                .status(UserStatus.ACTIVE)
                .role(UserRole.USER)
                .build();

        Terms serviceTerms = createTerms(1L, TermsType.SERVICE, true, true);
        Terms privacyTerms = createTerms(2L, TermsType.PRIVACY, true, true);
        Terms marketingTerms = createTerms(3L, TermsType.MARKETING, false, true);

        given(termsRepository.findByTypeAndActiveTrue(TermsType.SERVICE))
                .willReturn(Optional.of(serviceTerms));
        given(termsRepository.findByTypeAndActiveTrue(TermsType.PRIVACY))
                .willReturn(Optional.of(privacyTerms));
        given(termsRepository.findByTypeAndActiveTrue(TermsType.MARKETING))
                .willReturn(Optional.of(marketingTerms));

        given(userRepository.save(any(User.class)))
                .willReturn(savedUser);

        // when
        SignUpDto.ResponseSignUp response = signUpService.signUp(request);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getUserId()).isEqualTo(1L);
        assertThat(response.getEmail()).isEqualTo("test@naver.com");
        assertThat(response.getName()).isEqualTo("홍길동");
    }

    @Test
    @DisplayName("이미 존재하는 이메일이면 예외가 발생한다")
    void signUpFailWhenEmailAlreadyExists() {
        // given
        SignUpDto.RequestSignUp request = new SignUpDto.RequestSignUp(
                "홍길동",
                LocalDate.of(2000, 1, 1),
                "서울시 강남구",
                "010-1234-5678",
                "test@naver.com",
                "1234",
                "1234",
                true,
                true,
                false
        );

        given(userRepository.existsByEmail("test@naver.com"))
                .willReturn(true);

        // when & then
        assertThatThrownBy(() -> signUpService.signUp(request))
                .isInstanceOf(CustomException.class)
                .hasMessage("이미 존재하는 이메일입니다.");
    }

    @Test
    @DisplayName("비밀번호와 비밀번호 확인이 일치하지 않으면 예외가 발생한다")
    void signUpFailWhenPasswordConfirmDoesNotMatch() {
        // given
        SignUpDto.RequestSignUp request = new SignUpDto.RequestSignUp(
                "홍길동",
                LocalDate.of(2000, 1, 1),
                "서울시 강남구",
                "010-1234-5678",
                "test@naver.com",
                "1234",
                "4321",
                true,
                true,
                false
        );

        given(userRepository.existsByEmail("test@naver.com"))
                .willReturn(false);

        // when & then
        assertThatThrownBy(() -> signUpService.signUp(request))
                .isInstanceOf(CustomException.class)
                .hasMessage("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
    }

    @Test
    @DisplayName("회원가입 시 비밀번호는 암호화되어 저장된다")
    void signUpPasswordShouldBeEncodedBeforeSave() {
        // given
        SignUpDto.RequestSignUp request = new SignUpDto.RequestSignUp(
                "홍길동",
                LocalDate.of(2000, 1, 1),
                "서울시 강남구",
                "010-1234-5678",
                "test@naver.com",
                "1234",
                "1234",
                true,
                true,
                false
        );

        given(userRepository.existsByEmail("test@naver.com"))
                .willReturn(false);

        given(passwordEncoder.encode("1234"))
                .willReturn("encodedPassword");

        User savedUser = User.builder()
                .userId(1L)
                .email("test@naver.com")
                .name("홍길동")
                .birthDate(LocalDate.of(2000, 1, 1))
                .address("서울시 강남구")
                .phoneNumber("010-1234-5678")
                .passwordHash("encodedPassword")
                .status(UserStatus.ACTIVE)
                .role(UserRole.USER)
                .build();

        Terms serviceTerms = createTerms(1L, TermsType.SERVICE, true, true);
        Terms privacyTerms = createTerms(2L, TermsType.PRIVACY, true, true);
        Terms marketingTerms = createTerms(3L, TermsType.MARKETING, false, true);

        given(termsRepository.findByTypeAndActiveTrue(TermsType.SERVICE))
                .willReturn(Optional.of(serviceTerms));
        given(termsRepository.findByTypeAndActiveTrue(TermsType.PRIVACY))
                .willReturn(Optional.of(privacyTerms));
        given(termsRepository.findByTypeAndActiveTrue(TermsType.MARKETING))
                .willReturn(Optional.of(marketingTerms));

        given(userRepository.save(any(User.class)))
                .willReturn(savedUser);

        // when
        signUpService.signUp(request);

        // then
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        User capturedUser = userCaptor.getValue();

        assertThat(capturedUser.getPasswordHash()).isEqualTo("encodedPassword");
    }

    @Test
    @DisplayName("회원가입 시 기본 상태는 ACTIVE이고 권한은 USER다")
    void signUpShouldSaveDefaultStatusAndRole() {
        // given
        SignUpDto.RequestSignUp request = new SignUpDto.RequestSignUp(
                "홍길동",
                LocalDate.of(2000, 1, 1),
                "서울시 강남구",
                "010-1234-5678",
                "test@naver.com",
                "1234",
                "1234",
                true,
                true,
                false
        );

        given(userRepository.existsByEmail("test@naver.com"))
                .willReturn(false);

        given(passwordEncoder.encode("1234"))
                .willReturn("encodedPassword");

        User savedUser = User.builder()
                .userId(1L)
                .email("test@naver.com")
                .name("홍길동")
                .birthDate(LocalDate.of(2000, 1, 1))
                .address("서울시 강남구")
                .phoneNumber("010-1234-5678")
                .passwordHash("encodedPassword")
                .status(UserStatus.ACTIVE)
                .role(UserRole.USER)
                .build();

        Terms serviceTerms = createTerms(1L, TermsType.SERVICE, true, true);
        Terms privacyTerms = createTerms(2L, TermsType.PRIVACY, true, true);
        Terms marketingTerms = createTerms(3L, TermsType.MARKETING, false, true);

        given(termsRepository.findByTypeAndActiveTrue(TermsType.SERVICE))
                .willReturn(Optional.of(serviceTerms));
        given(termsRepository.findByTypeAndActiveTrue(TermsType.PRIVACY))
                .willReturn(Optional.of(privacyTerms));
        given(termsRepository.findByTypeAndActiveTrue(TermsType.MARKETING))
                .willReturn(Optional.of(marketingTerms));

        given(userRepository.save(any(User.class)))
                .willReturn(savedUser);

        // when
        signUpService.signUp(request);

        // then
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        User capturedUser = userCaptor.getValue();

        assertThat(capturedUser.getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(capturedUser.getRole()).isEqualTo(UserRole.USER);
    }

    @Test
    @DisplayName("서비스 이용약관에 동의하지 않으면 예외가 발생한다")
    void signUpFailWhenServiceTermsNotAgreed() {
        // given
        SignUpDto.RequestSignUp request = new SignUpDto.RequestSignUp(
                "홍길동",
                LocalDate.of(2000, 1, 1),
                "서울시 강남구",
                "010-1234-5678",
                "test@naver.com",
                "1234",
                "1234",
                false,
                true,
                false
        );

        // when & then
        assertThatThrownBy(() -> signUpService.signUp(request))
                .isInstanceOf(CustomException.class)
                .hasMessage("서비스 이용약관 동의는 필수입니다.");
    }

    @Test
    @DisplayName("개인정보 수집 및 이용에 동의하지 않으면 예외가 발생한다")
    void signUpFailWhenPrivacyTermsNotAgreed() {
        // given
        SignUpDto.RequestSignUp request = new SignUpDto.RequestSignUp(
                "홍길동",
                LocalDate.of(2000, 1, 1),
                "서울시 강남구",
                "010-1234-5678",
                "test@naver.com",
                "1234",
                "1234",
                true,
                false,
                false
        );

        // when & then
        assertThatThrownBy(() -> signUpService.signUp(request))
                .isInstanceOf(CustomException.class)
                .hasMessage("개인정보 수집 및 이용 동의는 필수입니다.");
    }

    @Test
    @DisplayName("마케팅 정보 수신 동의는 선택이므로 false여도 회원가입에 성공한다")
    void signUpSuccessWhenMarketingAgreeIsFalse() {
        // given
        SignUpDto.RequestSignUp request = new SignUpDto.RequestSignUp(
                "홍길동",
                LocalDate.of(2000, 1, 1),
                "서울시 강남구",
                "010-1234-5678",
                "test@naver.com",
                "1234",
                "1234",
                true,
                true,
                false
        );

        given(userRepository.existsByEmail("test@naver.com"))
                .willReturn(false);

        given(passwordEncoder.encode("1234"))
                .willReturn("encodedPassword");

        User savedUser = User.builder()
                .userId(1L)
                .email("test@naver.com")
                .name("홍길동")
                .birthDate(LocalDate.of(2000, 1, 1))
                .address("서울시 강남구")
                .phoneNumber("010-1234-5678")
                .passwordHash("encodedPassword")
                .status(UserStatus.ACTIVE)
                .role(UserRole.USER)
                .build();

        Terms serviceTerms = createTerms(1L, TermsType.SERVICE, true, true);
        Terms privacyTerms = createTerms(2L, TermsType.PRIVACY, true, true);
        Terms marketingTerms = createTerms(3L, TermsType.MARKETING, false, true);

        given(termsRepository.findByTypeAndActiveTrue(TermsType.SERVICE))
                .willReturn(Optional.of(serviceTerms));
        given(termsRepository.findByTypeAndActiveTrue(TermsType.PRIVACY))
                .willReturn(Optional.of(privacyTerms));
        given(termsRepository.findByTypeAndActiveTrue(TermsType.MARKETING))
                .willReturn(Optional.of(marketingTerms));

        given(userRepository.save(any(User.class)))
                .willReturn(savedUser);

        // when
        SignUpDto.ResponseSignUp response = signUpService.signUp(request);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getUserId()).isEqualTo(1L);
        assertThat(response.getEmail()).isEqualTo("test@naver.com");
        assertThat(response.getName()).isEqualTo("홍길동");
    }

    @Test
    @DisplayName("회원가입 성공 시 사용자 약관 동의 이력이 저장된다")
    void signUpShouldSaveUserTermsAgreements() {
        // given
        SignUpDto.RequestSignUp request = new SignUpDto.RequestSignUp(
                "홍길동",
                LocalDate.of(2000, 1, 1),
                "서울시 강남구",
                "010-1234-5678",
                "test@naver.com",
                "1234",
                "1234",
                true,
                true,
                false
        );

        given(userRepository.existsByEmail("test@naver.com"))
                .willReturn(false);

        given(passwordEncoder.encode("1234"))
                .willReturn("encodedPassword");

        User savedUser = User.builder()
                .userId(1L)
                .email("test@naver.com")
                .name("홍길동")
                .birthDate(LocalDate.of(2000, 1, 1))
                .address("서울시 강남구")
                .phoneNumber("010-1234-5678")
                .passwordHash("encodedPassword")
                .status(UserStatus.ACTIVE)
                .role(UserRole.USER)
                .build();

        Terms serviceTerms = createTerms(1L, TermsType.SERVICE, true, true);
        Terms privacyTerms = createTerms(2L, TermsType.PRIVACY, true, true);
        Terms marketingTerms = createTerms(3L, TermsType.MARKETING, false, true);

        given(termsRepository.findByTypeAndActiveTrue(TermsType.SERVICE))
                .willReturn(Optional.of(serviceTerms));
        given(termsRepository.findByTypeAndActiveTrue(TermsType.PRIVACY))
                .willReturn(Optional.of(privacyTerms));
        given(termsRepository.findByTypeAndActiveTrue(TermsType.MARKETING))
                .willReturn(Optional.of(marketingTerms));

        given(userRepository.save(any(User.class)))
                .willReturn(savedUser);

        // when
        signUpService.signUp(request);

        // then
        then(userTermsAgreementRepository).should().save(any(UserTermsAgreement.class));
    }

    // 테스트에서 사용할 약관 엔티티 생성 헬퍼
    private Terms createTerms(Long termId, TermsType type, boolean required, boolean active) {
        return Terms.builder()
                .termId(termId)
                .title(type.name() + " 약관")
                .content(type.name() + " 약관 내용")
                .version("v1.0")
                .type(type)
                .required(required)
                .active(active)
                .build();
    }
}