package com.bs23.keyclock.request;

/**
 * @author Abdur Rahim Nishad
 * @since 2021/11/04
 */
public class PasswordReset {
    private String value;
    private Boolean temporary;

    public Boolean getTemporary() {
        return temporary;
    }

    public void setTemporary(Boolean temporary) {
        this.temporary = temporary;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
