package ru.nk.rschir.types.functions;

public interface Func2Args<first, second, R> {
    R apply(first arg1, second arg2);
}
