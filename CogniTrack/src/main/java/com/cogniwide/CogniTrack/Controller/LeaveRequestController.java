package com.cogniwide.CogniTrack.Controller;
import com.cogniwide.CogniTrack.CustomResponse.ApiError;
import com.cogniwide.CogniTrack.CustomResponse.ApiResponse;
import com.cogniwide.CogniTrack.DTO.LeaveRequestDto;
import com.cogniwide.CogniTrack.Model.LeaveRequest;
import com.cogniwide.CogniTrack.Service.LeaveRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/leave-requests")
public class LeaveRequestController {
    @Autowired
    LeaveRequestService leaveRequestService;

    @GetMapping("/history/{employeeId}")
    public ResponseEntity<ApiResponse> getHistory(@PathVariable String employeeId, @RequestBody String status) {
        ApiResponse apiResponse=new ApiResponse();

            List<LeaveRequest> history= this.leaveRequestService.getHistory(employeeId, status);
            apiResponse.setSuccess(true);
            apiResponse.setData(history);
            apiResponse.setMessage("Fetched Successfully");
            apiResponse.setStatusCode(HttpStatus.OK.value());
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<ApiResponse> getRequestByEmployeeId(@PathVariable String employeeId, @RequestBody String status) {
        ApiResponse apiResponse=new ApiResponse();
            List<LeaveRequest> requests=this.leaveRequestService.getRequestsByEmployeeId(employeeId, status);
            apiResponse.setSuccess(true);
            apiResponse.setData(requests);
            apiResponse.setMessage("Fetched Successfully");
            apiResponse.setStatusCode(HttpStatus.OK.value());
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/manager/{approverId}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<ApiResponse> getRequestByApproverId(@PathVariable String approverId, @RequestBody String status) {
        ApiResponse apiResponse=new ApiResponse();

        List<LeaveRequest> requests= this.leaveRequestService.getRequestsByApproverId(approverId, status);
            apiResponse.setSuccess(true);
            apiResponse.setData(requests);
            apiResponse.setMessage("Fetched Successfully");
            apiResponse.setStatusCode(HttpStatus.OK.value());
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PostMapping("/request")
    public ResponseEntity<ApiResponse> leaveRequest(@RequestBody LeaveRequestDto leaveRequestDto) {
        return this.leaveRequestService.requestLeave(leaveRequestDto);
    }

    @PostMapping("/process/{reqId}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<ApiResponse> processRequest(@PathVariable Long reqId, @RequestBody LeaveRequestDto leaveRequestDto) {
        if (leaveRequestDto.getStatus().equals("APPROVE")) {
           return this.leaveRequestService.approveLeaveRequest(reqId);
        } else if (leaveRequestDto.getStatus().equals("DENY")) {
           return this.leaveRequestService.denyLeaveRequest(reqId,leaveRequestDto.getComments());
        }
        ApiResponse apiResponse=new ApiResponse();
        ApiError error = ApiError.setError("Data violation");
        apiResponse.setSuccess(false);
        apiResponse.setErrors(Collections.singletonList(error));
        apiResponse.setMessage("Action is missing: Approve or Deny");
        apiResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
    }

    @DeleteMapping("/delete/{reqId}")
    public ResponseEntity<ApiResponse> deleteRequest(@PathVariable Long reqId) {
        ApiResponse apiResponse=new ApiResponse();
        if(this.leaveRequestService.deleteRequest(reqId)){
            apiResponse.setSuccess(true);
            apiResponse.setMessage("Request Deleted");
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

    @PostMapping("/mark-as-read/{requestId}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public void markAsRead(@PathVariable Long requestId) {
        this.leaveRequestService.markAsRead(requestId);
    }
}