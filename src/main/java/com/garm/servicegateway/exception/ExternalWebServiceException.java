package com.garm.servicegateway.exception;


import org.springframework.stereotype.Component;

@Component
public class ExternalWebServiceException {

    public static class AccountDetailFailedException extends BaseException {
        private String messageId;
        private String description;

        public AccountDetailFailedException(String messageId, String description) {
            super("Calling AccountDetail service failed with messageId: " + messageId + " and description: " + description);
            this.messageId = messageId;
            this.description = description;
        }

        public AccountDetailFailedException(String message) {
            super("AccountDetail service is unavailable with message: " + message);
        }

        public AccountDetailFailedException() {
            super("Response is null");
        }

        public String getMessageId() {
            return messageId;
        }

        public String getDescription() {
            return description;
        }
    }

    public static class NullResponseException extends BaseException {

        private final Object response;

        public NullResponseException(Object response) {
            super("Response is null: " + response);
            this.response = response;
        }

        public Object getResponse() {
            return response;
        }
    }

}