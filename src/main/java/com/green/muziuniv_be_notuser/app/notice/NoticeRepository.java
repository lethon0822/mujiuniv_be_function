package com.green.muziuniv_be_notuser.app.notice;

import com.green.muziuniv_be_notuser.entity.notice.Notice;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
//    @Modifying
//    @Query("UPDATE Notice n SET n.view = n.view + 1 WHERE n.noticeId = :noticeId")
//    void incrementViews(@Param("notice_id") Long noticeId);
}
