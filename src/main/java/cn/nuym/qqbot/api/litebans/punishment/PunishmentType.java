package cn.nuym.qqbot.api.litebans.punishment;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PunishmentType {
    BAN(true),
    MUTE(true),
    KICK(false),
    WARNING(true);

    private final boolean removable;
}
