package com.simicart.core.base.model.entity;

public class BusEntity<T> {
    private String key;
    private T value;

    public BusEntity() {
    }

    public BusEntity(String key, T value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "BusEntity{" +
                "key=" + key +
                ", value=" + value +
                '}';
    }
}