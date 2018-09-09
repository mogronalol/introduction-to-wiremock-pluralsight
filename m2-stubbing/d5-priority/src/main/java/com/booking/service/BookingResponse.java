package com.booking.service;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class BookingResponse {
    private final String bookingId;
    private final BookingResponseStatus bookingResponseStatus;

    public BookingResponse(String bookingId, BookingResponseStatus bookingResponseStatus) {
        this.bookingId = bookingId;
        this.bookingResponseStatus = bookingResponseStatus;
    }

    public enum BookingResponseStatus {
        SUCCESS, REJECTED, SUSPECTED_FRAUD
    }

    public String getBookingId() {
        return bookingId;
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
