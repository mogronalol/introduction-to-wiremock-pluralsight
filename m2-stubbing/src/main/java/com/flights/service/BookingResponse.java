package com.flights.service;

public class BookingResponse {
    private final String bookingId;
    private final String paymentId;
    private final BookingResponseStatus bookingResponseStatus;

    public BookingResponse(String bookingId, String paymentId, BookingResponseStatus bookingResponseStatus) {
        this.bookingId = bookingId;
        this.paymentId = paymentId;
        this.bookingResponseStatus = bookingResponseStatus;
    }

    public enum BookingResponseStatus {
        SUCCESS
    }
}
