package org.practice.user.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class UserTest {
    User user;

    @BeforeEach
    public void setUp() {
        user = new User();
    }

    @Test
    public void canUpgradeLevel() {
        user.setLevel(Level.BASIC);
        user.setLogin(50);

        assertThat(user.canUpgradeLevel()).isTrue();

        user.setLogin(49);
        assertThat(user.canUpgradeLevel()).isFalse();

        user.setLevel(Level.SILVER);
        user.setRecommend(30);
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
