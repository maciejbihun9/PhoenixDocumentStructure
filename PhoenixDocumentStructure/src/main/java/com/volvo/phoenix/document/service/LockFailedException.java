package com.volvo.phoenix.document.service;

/**
 * When it failed to lock source/target folder when perform move/copy
 */
public class LockFailedException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * Who already locked the item
     */
    private String who;
    
    public LockFailedException(){}
    public LockFailedException(String user) {
        this.who = user;
    }

}
