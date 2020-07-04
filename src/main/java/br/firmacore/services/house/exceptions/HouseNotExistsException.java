package br.firmacore.services.house.exceptions;

public class HouseNotExistsException extends Exception {
    @Override
    public String getMessage() {
        return "Casa não encontrada";
    }
}
