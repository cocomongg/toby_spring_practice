package org.practice.user.domain;

public class User {
    public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
    public static final int MIN_RECOMMEND_FOR_GOLD = 30;

    String id;
    String name;
    String password;
    Level level;
    int login;
    int recommend;

    public User() {}

    public User(String id, String name, String password, Level level, int login, int recommend) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.level = level;
        this.login = login;
        this.recommend = recommend;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public int getLogin() {
        return login;
    }

    public void setLogin(int login) {
        this.login = login;
    }

    public int getRecommend() {
        return recommend;
    }

    public void setRecommend(int recommend) {
        this.recommend = recommend;
    }

    public boolean canUpgradeLevel() {
        switch (this.level) {
            case BASIC:
                return this.login >= MIN_LOGCOUNT_FOR_SILVER;
            case SILVER:
                return this.recommend >= MIN_RECOMMEND_FOR_GOLD;
            case GOLD:
                return false;
            default:
                throw new IllegalArgumentException("Unknown level: " + this.level);
        }
    }

    public void upgradeLevel() {
        Level nextLevel = this.level.getNext();
        if(nextLevel == null) {
            throw new IllegalStateException("max level");
        }

        this.level = nextLevel;
    }
}
