package org.project.heredoggy.domain.postgresql.shelter.shlterAdmin;

import org.project.heredoggy.domain.postgresql.shelter.notice.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShelterAdminRepository extends JpaRepository<Notice, Long> {
}
