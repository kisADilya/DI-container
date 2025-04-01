package ru.nsu.kisadilya.diContainer.example;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Gearbox {

    private boolean isManual;

    public void shiftUp() {
        System.out.println("Shift up");
    }

    public void shiftDown() {
        System.out.println("Shift down");
    }
}
