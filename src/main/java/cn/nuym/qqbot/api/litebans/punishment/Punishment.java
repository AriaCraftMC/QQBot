package cn.nuym.qqbot.api.litebans.punishment;

import java.util.Date;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class Punishment {
    private final long id;
    private final String uuid;
    private final String ip;
    private final String reason;
    private final String executorUUID;
    private final String executorName;
    private String removedByUUID;
    private String removedByName;
    private String removedReason;
    private Date removedDate;
    private final long createdAt;
    private final long expiresAt;
    private final short template;
    private final String serverScope;
    private final String serverOrigin;
    private final boolean silent;
    private final boolean ipBan;
    private final boolean ipBanWildcard;
    private final boolean active;
    private boolean warned;
}
