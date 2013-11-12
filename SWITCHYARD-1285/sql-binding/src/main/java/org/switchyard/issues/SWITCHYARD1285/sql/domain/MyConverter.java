package org.switchyard.issues.SWITCHYARD1285.sql.domain;

import java.util.Iterator;

import org.apache.camel.Converter;

@Converter
public class MyConverter {
    @SuppressWarnings("rawtypes")
    @Converter
    public static Iterator toIterator(Greet greet) {
        return new GreetIterator(greet);
    }

    @SuppressWarnings("rawtypes")
    @Converter
    public static Iterator toIterator(Greet2 greet2) {
        return new Greet2Iterator(greet2);
    }
}

@SuppressWarnings("rawtypes")
class GreetIterator implements Iterator {
    private int _position = 0;
    private Greet _greet;
    
    public GreetIterator(Greet greet) {
        _greet = greet;
    }
    @Override
    public boolean hasNext() {
        return _position < 2;
    }
    @Override
    public Object next() {
        switch (_position++) {
        case 1: return _greet.getSender();
        case 2: return _greet.getReceiver();
        }
        return null;
    }
    @Override
    public void remove() {
    }
}
@SuppressWarnings("rawtypes")
class Greet2Iterator implements Iterator {
    private int _position = 0;
    private Greet2 _greet;
    
    public Greet2Iterator(Greet2 greet) {
        _greet = greet;
    }
    @Override
    public boolean hasNext() {
        return _position < 2;
    }
    @Override
    public Object next() {
        switch (_position++) {
        case 1: return _greet.getSender();
        case 2: return _greet.getReceiver();
        }
        return null;
    }
    @Override
    public void remove() {
    }
}
