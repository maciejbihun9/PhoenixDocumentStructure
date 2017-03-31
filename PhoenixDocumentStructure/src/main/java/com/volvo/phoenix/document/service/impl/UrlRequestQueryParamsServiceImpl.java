package com.volvo.phoenix.document.service.impl;

import org.springframework.stereotype.Service;

import com.volvo.phoenix.document.datatype.NotificationState;
import com.volvo.phoenix.document.service.UrlRequestQueryParamsService;

/**
 * Holds URL request params.
 * 
 */
@Service
public final class UrlRequestQueryParamsServiceImpl implements UrlRequestQueryParamsService {

    private NotificationState ownerEmailNotificationsState = NotificationState.DISABLED ;

    public UrlRequestQueryParamsServiceImpl() {
    }

    @Override
    public NotificationState getOwnerEmailNotificationsState() {
        return ownerEmailNotificationsState;
    }

    @Override
    public void setOwnerEmailNotificationsState(final NotificationState ownerEmailNotificationsState) {
        this.ownerEmailNotificationsState = ownerEmailNotificationsState;
    }
}
