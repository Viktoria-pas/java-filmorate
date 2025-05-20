package ru.yandex.practicum.filmorate.model;

public enum FriendshipStatus {
    PENDING(1),
    CONFIRMED(2);

    private final int id;

    FriendshipStatus(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static FriendshipStatus fromId(int id) {
        for (FriendshipStatus status : values()) {
            if (status.getId() == id) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown status id: " + id);
    }

    public boolean isPending() {
        return this == PENDING;
    }

    public boolean isConfirmed() {
        return this == CONFIRMED;
    }
}

