package com.aptner.v3.attach.repository;

import com.aptner.v3.attach.Attach;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AttachRepository extends JpaRepository<Attach, Long> {

    Optional<Attach> findByUuid(String uuid);

}
