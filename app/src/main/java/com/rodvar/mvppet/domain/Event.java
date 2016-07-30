package com.rodvar.mvppet.domain;

/**
 * Created by rodrigo on 27/07/16.
 */
public class Event {
    private int id;
    private String phrase;

    @Override
    public String toString() {
        return this.phrase;
    }
}
