package br.com.gabrielnunesds.servicex.exceptios;

public class ObjectNotFoundException  extends RuntimeException {
    public ObjectNotFoundException(String message) {
        super(message);
    }

}