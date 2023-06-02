package org.imaginnovate.test.common;

import lombok.Getter;

@Getter
public enum I18Constants {
    EMPLOYEE_NOT_FOUND("employee.not.found");
    String key;
    I18Constants(String key) {
        this.key = key;
    }
}
