package com.supinfo.and.suptodo.model;

import java.io.Serializable;

public class TodoResponse implements Serializable {
    private String id;
    private String lastupdate;
    private String usercreator;
    private String userinvited;
    private String todo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLastupdate() {
        return lastupdate;
    }

    public void setLastupdate(String lastupdate) {
        this.lastupdate = lastupdate;
    }

    public String getUsercreator() {
        return usercreator;
    }

    public void setUsercreator(String usercreator) {
        this.usercreator = usercreator;
    }

    public String getUserinvited() {
        return userinvited;
    }

    public void setUserinvited(String userinvited) {
        this.userinvited = userinvited;
    }

    public String getTodo() {
        return todo;
    }

    public void setTodo(String todo) {
        this.todo = todo;
    }
}
