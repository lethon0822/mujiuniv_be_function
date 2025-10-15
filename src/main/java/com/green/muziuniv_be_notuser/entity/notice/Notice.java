package com.green.muziuniv_be_notuser.entity.notice;



import com.green.muziuniv_be_notuser.entity.UpdatedAt;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Notice extends UpdatedAt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long noticeId;

    @Column(nullable = false, length = 100)
    private String noticeTitle;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String noticeContent;

    @Column(nullable = false)
    private int view;

    @Column(nullable = false)
    private int type;

}
