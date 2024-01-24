package com.simple.calc.entity;

/**
 * Класс Pair представляет собой обобщенную пару значений с типами T и U.
 * Поля first и second представляют первое и второе значения в паре соответственно.
 * @param <T> тип первого значения в паре
 * @param <U> тип второго значения в паре
 */
public record Pair<T, U>(T first, U second) {
}