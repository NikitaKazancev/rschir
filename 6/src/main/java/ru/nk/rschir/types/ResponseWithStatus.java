package ru.nk.rschir.types;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseWithStatus <T> {
    private int status;
    private T data;

    public static <T> ResponseWithStatus<T> empty(int statusCode) {
        return new ResponseWithStatus<>(statusCode, null);
    }

    public static <T> ResponseWithStatus<T> create(int statusCode, T data) {
        return new ResponseWithStatus<>(statusCode, data);
    }

    public static <DataType> Builder<DataType> builder() {
        return new Builder<>();
    }

    public static class Builder <T> {
        private int statusCode;
        private T data;
        public Builder<T> status(int statusCode) {
            this.statusCode = statusCode;
            return this;
        }

        public Builder<T> data(T data) {
            this.data = data;
            return this;
        }

        public ResponseWithStatus<T> build() {
            return new ResponseWithStatus<>(statusCode, data);
        }
    }
}
