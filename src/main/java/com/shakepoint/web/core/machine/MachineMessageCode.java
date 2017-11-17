package com.shakepoint.web.core.machine;

public enum MachineMessageCode {
    MACHINE_CREATED(1);

    int value;

    MachineMessageCode(int val) {
        this.value = val;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
