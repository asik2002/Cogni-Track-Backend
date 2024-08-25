package com.cogniwide.CogniTrack.Controller;

import com.cogniwide.CogniTrack.CustomResponse.ApiError;
import com.cogniwide.CogniTrack.CustomResponse.ApiResponse;
import com.cogniwide.CogniTrack.Model.LeaveType;
import com.cogniwide.CogniTrack.Service.LeaveTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/leave-type")
public class LeaveTypeController {
    @Autowired
    LeaveTypeService leaveTypeService;

    @PostMapping(value = "/add-new")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse> addType(@RequestBody LeaveType leaveType) {
        ApiResponse apiResponse=new ApiResponse();
        if(this.leaveTypeService.addType(leaveType)){
            apiResponse.setSuccess(true);
            apiResponse.setMessage("New Leave type created");
            apiResponse.setStatusCode(HttpStatus.CREATED.value());
            return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
        }
        ApiError error = ApiError.setError("Data violation");
        apiResponse.setSuccess(false);
        apiResponse.setErrors(Collections.singletonList(error));
        apiResponse.setMessage("Check the details");
        apiResponse.setStatusCode(HttpStatus.CONFLICT.value());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(apiResponse);
    }

    @DeleteMapping(value = "/delete/{leaveTypeId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long leaveTypeId) {
        ApiResponse apiResponse=new ApiResponse();
        if(this.leaveTypeService.delete(leaveTypeId)){
            apiResponse.setSuccess(true);
            apiResponse.setMessage("Leave type deleted");
            apiResponse.setStatusCode(HttpStatus.OK.value());
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        }
        ApiError error = ApiError.setError("Data not found");
        apiResponse.setSuccess(false);
        apiResponse.setErrors(Collections.singletonList(error));
        apiResponse.setMessage("Check the details");
        apiResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
    }

    @GetMapping(value = "/get-all")
    public ResponseEntity<ApiResponse> getAllType() {
        ApiResponse apiResponse=new ApiResponse();
        List<LeaveType> leaveTypes= this.leaveTypeService.getallType();
        apiResponse.setSuccess(true);
        apiResponse.setData(leaveTypes);
        apiResponse.setMessage("fetched Successfully");
        apiResponse.setStatusCode(HttpStatus.OK.value());
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}
