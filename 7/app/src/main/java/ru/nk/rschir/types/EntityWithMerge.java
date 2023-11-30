package ru.nk.rschir.types;

public interface EntityWithMerge<T> {
    void merge(T inputEntity);
}
