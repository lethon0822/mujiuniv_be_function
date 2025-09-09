package com.green.muziuniv_be_notuser.entity.notice;

import com.green.muziuniv_be_notuser.entity.CreatedAt;
import com.green.muziuniv_be_notuser.entity.UpdatedAt;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "notice")
@EntityListeners(AuditingEntityListener.class)
public class Notice extends UpdatedAt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // AUTO_INCREMENT
    @Column(name = "notice_id")
    private Integer noticeId;

    @Column(name = "notice_title", nullable = false, length = 100)
    private String noticeTitle;


    @Column(name = "notice_content", nullable = false, length = 300)
    private String noticeContent;



}
