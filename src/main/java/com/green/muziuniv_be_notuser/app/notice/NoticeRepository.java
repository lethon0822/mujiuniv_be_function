package com.green.muziuniv_be_notuser.app.notice;

import com.green.muziuniv_be_notuser.entity.notice.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

}
