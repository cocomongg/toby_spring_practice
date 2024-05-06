package org.practice.user.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.practice.user.domain.User.MIN_LOGCOUNT_FOR_SILVER;
import static org.practice.user.domain.User.MIN_RECOMMEND_FOR_GOLD;

public class UserTest {
    User user;

    @BeforeEach
    public void setUp() {
        user = new User();
    }

    @Test
    public void canUpgradeLevel() {
        user.setLevel(Level.BASIC);
        user.setLogin(MIN_LOGCOUNT_FOR_SILVER);

        assertThat(user.canUpgradeLevel()).isTrue();

        user.setLogin(MIN_LOGCOUNT_FOR_SILVER - 1);
        assertThat(user.canUpgradeLevel()).isFalse();

        user.setLevel(Level.SILVER);
        user.setRecommend(MIN_RECOMMEND_FOR_GOLD);
        assertThat(user.canUpgradeLevel()).isTrue();
    }

    @Test
    public void upgradeLevel() {
        Level[] levels = Level.values();
        for(Level level : levels) {
            if(level.getNext() == null) {
                continue;
            }

            user.setLevel(level);
            user.upgradeLevel();
            assertThat(user.getLevel()).isEqualTo(level.getNext());
        }
    }

    @Test
    public void upgradeLevelWithException() {
        Level[] levels = Level.values();
        for(Level level : levels) {
            if(level.getNext() != null) {
                continue;
            }

            user.setLevel(level);
            assertThatThrownBy(() -> user.upgradeLevel()).isInstanceOf(IllegalStateException.class);
        }
    }
}
