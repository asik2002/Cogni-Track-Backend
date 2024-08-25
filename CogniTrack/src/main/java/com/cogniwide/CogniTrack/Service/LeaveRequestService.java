package com.cogniwide.CogniTrack.Service;

import com.cogniwide.CogniTrack.CustomResponse.ApiError;
import com.cogniwide.CogniTrack.CustomResponse.ApiResponse;
import com.cogniwide.CogniTrack.DTO.LeaveRequestDto;
import com.cogniwide.CogniTrack.Model.LeaveBalance;
import com.cogniwide.CogniTrack.Model.LeaveRequest;
import com.cogniwide.CogniTrack.Repository.LeaveBalanceRepo;
import com.cogniwide.CogniTrack.Repository.LeaveRequestRepo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.Collections;
import java.util.List;

@Service
public class LeaveRequestService {
     @Autowired
    LeaveRequestRepo leaveRequestRepo;
     @Autowired
    LeaveBalanceRepo leaveBalanceRepo;
    Date today = new Date(System.currentTimeMillis());
     public List<LeaveRequest> getHistory(String employeeId,String status){
         return this.leaveRequestRepo.getHistory(employeeId,today,status);
     }
     public ResponseEntity<ApiResponse> requestLeave(LeaveRequestDto leaveRequestDto) {
         ApiResponse apiResponse= new ApiResponse();
         long numberOfDays=calculateNumberOfDays(leaveRequestDto.getStartDate(),leaveRequestDto.getEndDate());
         LeaveBalance leaveBalance=leaveBalanceRepo.findByEmployeeIdAndLeaveTypeId(
                 leaveRequestDto.getEmployeeId(),leaveRequestDto.getLeaveTypeId()
         );
         if (leaveBalance == null) {
             ApiError error = ApiError.setError("Data not found");
             apiResponse.setSuccess(false);
             apiResponse.setErrors(Collections.singletonList(error));
             apiResponse.setMessage("No leave balance found");
             apiResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
             return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
         }
         if (leaveBalance.getBalanceDays() < numberOfDays) {
             ApiError error = ApiError.setError("Insufficient leave balance");
             apiResponse.setSuccess(false);
             apiResponse.setErrors(Collections.singletonList(error));
             apiResponse.setMessage("Current Balance is "+leaveBalance.getBalanceDays().toString());
             apiResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
             return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
         }
         LeaveRequest leaveRequest=new LeaveRequest();
         leaveRequestDto.setStatus("PENDING");
         leaveRequestDto.setIsRead(false);
         BeanUtils.copyProperties(leaveRequestDto,leaveRequest);
         try {
             this.leaveRequestRepo.save(leaveRequest);
             apiResponse.setSuccess(true);
             apiResponse.setMessage("Leave Request sent successfully");
             apiResponse.setStatusCode(HttpStatus.CREATED.value());
             return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
         }
         catch (DataIntegrityViolationException e){
             ApiError error = ApiError.setError("Data violation");
             apiResponse.setSuccess(false);
             apiResponse.setErrors(Collections.singletonList(error));
             apiResponse.setMessage("Requested Already");
             apiResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
             return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);         }
     }

     public ResponseEntity<ApiResponse> approveLeaveRequest(Long requestId){
         ApiResponse apiResponse =new ApiResponse();
         LeaveRequest leaveRequest = leaveRequestRepo.findByRequestId(requestId);
         if(leaveRequest.getStatus().equals("APPROVED")){
             ApiError error = ApiError.setError("Data violation");
             apiResponse.setSuccess(false);
             apiResponse.setErrors(Collections.singletonList(error));
             apiResponse.setMessage("Already approved");
             apiResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
             return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
         }
         long numberOfDays = calculateNumberOfDays(leaveRequest.getStartDate(), leaveRequest.getEndDate());
         LeaveBalance leaveBalance=leaveBalanceRepo.findByEmployeeIdAndLeaveTypeId(
                 leaveRequest.getEmployeeId(),leaveRequest.getLeaveTypeId()
         );
         if (leaveBalance == null) {
             ApiError error = ApiError.setError("Data not found");
             apiResponse.setSuccess(false);
             apiResponse.setErrors(Collections.singletonList(error));
             apiResponse.setMessage("No leave balance found");
             apiResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
             return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
         }
         if (leaveBalance.getBalanceDays() < numberOfDays) {
             ApiError error = ApiError.setError("Insufficient leave balance");
             apiResponse.setSuccess(false);
             apiResponse.setErrors(Collections.singletonList(error));
             apiResponse.setMessage("Current Balance is "+leaveBalance.getBalanceDays().toString());
             apiResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
             return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
         }
         leaveBalance.setBalanceDays(leaveBalance.getBalanceDays() - numberOfDays);
         leaveBalanceRepo.save(leaveBalance);
         leaveRequest.setStatus("APPROVED");
         leaveRequestRepo.save(leaveRequest);
         apiResponse.setSuccess(true);
         apiResponse.setMessage("Leave Request Approved successfully");
         apiResponse.setStatusCode(HttpStatus.OK.value());
         return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
     }
     public ResponseEntity<ApiResponse> denyLeaveRequest(Long requestId,String comments){
         ApiResponse apiResponse=new ApiResponse();
         LeaveRequest leaveRequest = leaveRequestRepo.findByRequestId(requestId);
         leaveRequest.setComments(comments);
         leaveRequest.setStatus("REJECTED");
         leaveRequestRepo.save(leaveRequest);
         apiResponse.setSuccess(true);
         apiResponse.setMessage("Leave Request Denied");
         apiResponse.setStatusCode(HttpStatus.OK.value());
         return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
     }

    private long calculateNumberOfDays(Date startDate, Date endDate) {
        long diff = endDate.getTime() - startDate.getTime();
        return diff / (1000 * 60 * 60 * 24) + 1; // Add 1 to include the end date
    }

    public List<LeaveRequest> getRequestsByEmployeeId(String employeeId,String status){
        return this.leaveRequestRepo.findRequestsForEmployee(today,employeeId,status);
    }


    public boolean deleteRequest(Long reqId) {
         LeaveRequest leaveRequest=leaveRequestRepo.findByRequestId(reqId);
         if(leaveRequest==null){
             return false;
         }
             leaveRequestRepo.delete(leaveRequest);
             return true;
        }

    public List<LeaveRequest> getRequestsByApproverId(String approverId, String status) {
         return this.leaveRequestRepo.findRequestsForApprover(today,approverId,status);
    }

    public void markAsRead(Long requestId) {
         LeaveRequest leaveRequest=leaveRequestRepo.findByRequestId(requestId);
         if(leaveRequest!=null){
             leaveRequest.setIsRead(true);
             leaveRequestRepo.save(leaveRequest);
         }
    }
}
