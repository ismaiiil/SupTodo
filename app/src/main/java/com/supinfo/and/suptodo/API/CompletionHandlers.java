package com.supinfo.and.suptodo.API;

import com.supinfo.and.suptodo.model.TodoResponse;

import java.util.List;

public class CompletionHandlers {
    public interface ShareCompletionHandler{
        void onFinished(Boolean wasShared);
    }

    public interface MyTodoListCompletionHandler {
        void onFinished(List<TodoResponse> todoResponses);
    }
}
