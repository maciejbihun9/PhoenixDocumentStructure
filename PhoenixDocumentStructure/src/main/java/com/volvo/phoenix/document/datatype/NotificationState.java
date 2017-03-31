package com.volvo.phoenix.document.datatype;

public enum NotificationState {
    
    ENABLED("enabled"), DISABLED("disabled");
    
    private final String value;
    
    private NotificationState(String value) {
        this.value = value;
    }

    public static NotificationState getState(final String notificationState) {
        for (final NotificationState state : values()) {
            if (state.value.equalsIgnoreCase(notificationState)) {
                return state;
            }
        }
        return null;
    }
}
