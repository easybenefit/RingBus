package com.bus.ring;

/**
 * Created by handgunbreak on 16/7/11.
 */
public interface RingBind<T> {

    void bind(T object);

    void unbind(T object);
}
