package com.cogniwide.CogniTrack.CustomResponse;

import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Data
public class ApiResponse {
    private boolean isSuccess;
    private List<ApiError> errors;
    private Object data;
    private String message;
    private int statusCode;
    public void setData(Object paramData){
        if(paramData instanceof String){
            Map<String, String> dataMap = new HashMap<>();
            dataMap.put("MESSAGE", (String)paramData);
            setData(dataMap);
        }else {
            this.data = paramData;
        }
    }
}
