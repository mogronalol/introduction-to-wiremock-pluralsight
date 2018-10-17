package com.booking.service;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class BookingResponse {
    private final BookingResponseStatus bookingResponseStatus;

    public BookingResponse(BookingResponseStatus bookingResponseStatus) {
        this.bookingResponseStatus = bookingResponseStatus;
    }

    public enum BookingResponseStatus {
        COMPLETE, REJECTED
    }

    public BookingResponseStatus getBookingResponseStatus() {
        return bookingResponseStatus;
    }

    @Override
    public boolean equals(Object obj){
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode(){
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString(){
        return ToStringBuilder.reflectionToString(this);
    }
}
