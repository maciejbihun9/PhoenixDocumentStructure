package com.volvo.phoenix.document.service;

import com.volvo.phoenix.document.datatype.NotificationState;

/**
 * Holds URL request params. This should be the part of WEB module of Copy Manager but by current design it landed here.
 * 
 */
public interface UrlRequestQueryParamsService {

    public static final String OWNER_EMAILS_NOTIFICATION_STATE = "owneremailnotificationstate";

    NotificationState getOwnerEmailNotificationsState();

    void setOwnerEmailNotificationsState(NotificationState ownerEmailNotificationsState);
}
