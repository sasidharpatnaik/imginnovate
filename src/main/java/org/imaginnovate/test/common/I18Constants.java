package org.imaginnovate.test.common;

import lombok.Getter;

@Getter
public enum I18Constants {
    EMPLOYEE_NOT_FOUND("employee.not.found"),
    DUPLICATE_EMPLOYEE_ID("duplicate.employee.id");
    String key;
    I18Constants(String key) {
        this.key = key;
    }
}
