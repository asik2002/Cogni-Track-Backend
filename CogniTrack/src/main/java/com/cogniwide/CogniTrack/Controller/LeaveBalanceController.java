package com.cogniwide.CogniTrack.Controller;
import com.cogniwide.CogniTrack.CustomResponse.ApiError;
import com.cogniwide.CogniTrack.CustomResponse.ApiResponse;
import com.cogniwide.CogniTrack.DTO.AddLeaveDto;
import com.cogniwide.CogniTrack.Service.LeaveBalanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/leave-balance")
public class LeaveBalanceController {
    @Autowired
    LeaveBalanceService leaveBalanceService;
    @GetMapping(value = "/get-balance-leave/{employeeId}")
    public ResponseEntity<ApiResponse> getBalanceDays(@PathVariable String employeeId, @RequestBody Long leaveTypeId){
       Long balanceDays=this.leaveBalanceService.getBalanceDays(employeeId,leaveTypeId);
       ApiResponse apiResponse=new ApiResponse();
       apiResponse.setSuccess(true);
       apiResponse.setData(balanceDays);
       apiResponse.setMessage("Fetched Successfully");
       apiResponse.setStatusCode(HttpStatus.OK.value());
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
    @PostMapping(value="/add-leave-balance")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse> addLeaveBalance(@RequestBody AddLeaveDto addLeaveDto){
        ApiResponse response = new ApiResponse();
        if(this.leaveBalanceService.addLeaveBalance(addLeaveDto)){
            response.setSuccess(true);
            response.setMessage("Leave Added");
            response.setStatusCode(HttpStatus.CREATED.value());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }
        else {
            ApiError error = ApiError.setError("Unable to add Leave");
            response.setSuccess(false);
            response.setErrors(Collections.singletonList(error));
            response.setMessage("Check the Details");
            response.setStatusCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
