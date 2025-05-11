package vn.iotstar.controller;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.transaction.Transactional;
import vn.iotstar.entity.OtpEntity;
import vn.iotstar.entity.UserEntity;
import vn.iotstar.model.ChangePasswordDTO;
import vn.iotstar.model.ForgotPasswwordDTO;
import vn.iotstar.model.LoginResponse;
import vn.iotstar.model.OTPVerificationDTO;
import vn.iotstar.model.UserLoginDTO;
import vn.iotstar.model.UserRegisterDTO;
import vn.iotstar.repository.OtpRepository;
import vn.iotstar.repository.UserRepository;
import vn.iotstar.service.impl.EmailServiceImpl;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	@Autowired
    private UserRepository userRepository;

    @Autowired
    private OtpRepository otpRepo;

    @Autowired
    private EmailServiceImpl emailService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Transactional
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRegisterDTO userDTO) {
        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email đã tồn tại!");
        }

        // Tạo OTP
        String otp = String.format("%06d", new Random().nextInt(999999));
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(5);

        // Lưu OTP vào DB
        otpRepo.deleteByEmail(userDTO.getEmail()); // clear cũ nếu có
        OtpEntity otpEntity = new OtpEntity();
        otpEntity.setEmail(userDTO.getEmail());
        otpEntity.setOtp(otp);
        otpEntity.setExpiryTime(expiryTime);
        otpRepo.save(otpEntity);

        // Gửi mail
        emailService.sendOTPEmail(userDTO.getEmail(), otp);

        return ResponseEntity.ok("Đã gửi OTP đến email!");
    }
    
    @Transactional
    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody OTPVerificationDTO dto) {
        Optional<OtpEntity> otpData = otpRepo.findByEmail(dto.getEmail());

        if (otpData.isEmpty()) return ResponseEntity.badRequest().body("Không tìm thấy OTP!");

        OtpEntity otp = otpData.get();

        if (otp.getExpiryTime().isBefore(LocalDateTime.now()))
            return ResponseEntity.badRequest().body("Mã OTP đã hết hạn!");

        if (!otp.getOtp().equals(dto.getOtp()))
            return ResponseEntity.badRequest().body("Mã OTP không đúng!");

        // Nếu đúng OTP → tạo user
        UserEntity user = new UserEntity();
        user.setEmail(dto.getEmail());
        user.setFullname(dto.getFullname());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        userRepository.save(user);
        otpRepo.deleteByEmail(dto.getEmail());

        return ResponseEntity.ok("Xác thực thành công. Tài khoản đã được tạo.");
    }
    
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody UserLoginDTO loginDTO) {
        Optional<UserEntity> optionalUser = userRepository.findByEmail(loginDTO.getEmail());

        if (optionalUser.isEmpty()) {
            return ResponseEntity.badRequest()
                .body(new LoginResponse(false, "Email không tồn tại!", null));
        }

        UserEntity user = optionalUser.get();

        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            return ResponseEntity.badRequest()
                .body(new LoginResponse(false, "Sai mật khẩu!", null));
        }

        UserLoginDTO data = new UserLoginDTO(user.getFullname(), user.getEmail(), user.getUser_id(), user.getFullname());
        return ResponseEntity.ok(new LoginResponse(true, "Đăng nhập thành công!", data));
    }
    
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswwordDTO request) {
    	String email = request.getEmail().trim();
        Optional<UserEntity> userOpt = userRepository.findByEmail(email.trim());

        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Email không tồn tại trong hệ thống!");
        }

        UserEntity user = userOpt.get();

        // Tạo mật khẩu mới ngẫu nhiên
        String newPassword = generateRandomPassword(8);

        // Mã hóa và cập nhật mật khẩu
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // Gửi email chứa mật khẩu mới
        String subject = "Khôi phục mật khẩu";
        String body = "Chào " + user.getFullname() + ",\n\n"
                + "Mật khẩu mới của bạn là: " + newPassword + "\n"
                + "Vui lòng đăng nhập và đổi lại mật khẩu sau khi đăng nhập.";
        emailService.sendSimpleMessage(user.getEmail(), subject, body);

        return ResponseEntity.ok("Mật khẩu mới đã được gửi đến email của bạn.");
    }

    private String generateRandomPassword(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        Random rnd = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        return sb.toString();
    }
    
    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordDTO request) {
        Optional<UserEntity> userOpt = userRepository.findByEmail(request.getEmail().trim());

        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Email không tồn tại!");
        }

        UserEntity user = userOpt.get();

        // Kiểm tra mật khẩu hiện tại
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            return ResponseEntity.badRequest().body("Mật khẩu hiện tại không đúng!");
        }

        // Mã hóa và cập nhật mật khẩu mới
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        return ResponseEntity.ok("Đổi mật khẩu thành công!");
    }


}
