package vn.iotstar.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import vn.iotstar.entity.OtpEntity;

public interface OtpRepository extends JpaRepository<OtpEntity, Integer>{
	Optional<OtpEntity> findByEmail(String email);
    void deleteByEmail(String email);
}
