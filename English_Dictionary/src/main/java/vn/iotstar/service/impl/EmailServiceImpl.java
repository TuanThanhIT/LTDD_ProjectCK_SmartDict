package vn.iotstar.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import vn.iotstar.service.EmailService;

public class EmailServiceImpl implements EmailService {
	@Autowired
	private JavaMailSender mailSender;

	@Override
	public void sendOTPEmail(String toEmail, String otp) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(toEmail);
		message.setSubject("Mã xác thực OTP");
		message.setText("Mã OTP của bạn là: " + otp + ". Mã có hiệu lực trong 5 phút.");
		mailSender.send(message);
	}
}
