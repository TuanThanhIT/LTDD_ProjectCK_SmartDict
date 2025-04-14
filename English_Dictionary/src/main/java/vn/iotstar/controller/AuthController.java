package vn.iotstar.controller;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.iotstar.entity.OtpEntity;
import vn.iotstar.entity.UserEntity;
import vn.iotstar.model.OTPVerificationDTO;
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
        user.setFullname("Tên demo"); // bạn có thể truyền fullname & password từ DTO hoặc lưu tạm
        user.setPassword("mật khẩu mã hóa");

        userRepository.save(user);
        otpRepo.deleteByEmail(dto.getEmail());

        return ResponseEntity.ok("Xác thực thành công. Tài khoản đã được tạo.");
    }
}
