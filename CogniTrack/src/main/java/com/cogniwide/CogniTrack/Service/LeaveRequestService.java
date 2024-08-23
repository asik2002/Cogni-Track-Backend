package com.cogniwide.CogniTrack.Service;

import com.cogniwide.CogniTrack.DTO.LeaveRequestDto;
import com.cogniwide.CogniTrack.Model.LeaveBalance;
import com.cogniwide.CogniTrack.Model.LeaveRequest;
import com.cogniwide.CogniTrack.Repository.LeaveBalanceRepo;
import com.cogniwide.CogniTrack.Repository.LeaveRequestRepo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.sql.Date;
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
     public String requestLeave(LeaveRequestDto leaveRequestDto) {
         long numberOfDays=calculateNumberOfDays(leaveRequestDto.getStartDate(),leaveRequestDto.getEndDate());
         LeaveBalance leaveBalance=leaveBalanceRepo.findByEmployeeIdAndLeaveTypeId(
                 leaveRequestDto.getEmployeeId(),leaveRequestDto.getLeaveTypeId()
         );
         if (leaveBalance == null) {
             throw new RuntimeException("Leave balance not found");
         }
         if (leaveBalance.getBalanceDays() < numberOfDays) {
             throw new RuntimeException("Insufficient leave balance");
         }
         LeaveRequest leaveRequest=new LeaveRequest();
         leaveRequestDto.setStatus("PENDING");
         leaveRequestDto.setIsRead(false);
         BeanUtils.copyProperties(leaveRequestDto,leaveRequest);
         try {
             this.leaveRequestRepo.save(leaveRequest);
             return "saved";
         }
         catch (DataIntegrityViolationException e){
             throw new RuntimeException("Requested Already");
         }
     }

     public void approveLeaveRequest(Long requestId){
         LeaveRequest leaveRequest = leaveRequestRepo.findByRequestId(requestId);
         if(leaveRequest.getStatus().equals("APPROVED")){
             throw new RuntimeException("Already Approved");
         }
         long numberOfDays = calculateNumberOfDays(leaveRequest.getStartDate(), leaveRequest.getEndDate());
         LeaveBalance leaveBalance=leaveBalanceRepo.findByEmployeeIdAndLeaveTypeId(
                 leaveRequest.getEmployeeId(),leaveRequest.getLeaveTypeId()
         );
         if (leaveBalance == null) {
             throw new RuntimeException("Leave balance not found");
         }
         if (leaveBalance.getBalanceDays() < numberOfDays) {
             throw new RuntimeException("Insufficient leave balance");
         }
         leaveBalance.setBalanceDays(leaveBalance.getBalanceDays() - numberOfDays);
         leaveBalanceRepo.save(leaveBalance);
         leaveRequest.setStatus("APPROVED");
         leaveRequestRepo.save(leaveRequest);
     }
     public void denyLeaveRequest(Long requestId,String comments){
         LeaveRequest leaveRequest = leaveRequestRepo.findByRequestId(requestId);
         leaveRequest.setComments(comments);
         leaveRequest.setStatus("REJECTED");
         leaveRequestRepo.save(leaveRequest);
     }

    private long calculateNumberOfDays(Date startDate, Date endDate) {
        long diff = endDate.getTime() - startDate.getTime();
        return diff / (1000 * 60 * 60 * 24) + 1; // Add 1 to include the end date
    }

    public List<LeaveRequest> get() {
         return this.leaveRequestRepo.findAll();
    }
    public List<LeaveRequest> getRequestsByEmployeeId(String employeeId,String status){
        return this.leaveRequestRepo.findRequestsForEmployee(today,employeeId,status);
    }


    public String deleteRequest(Long reqId) {
         LeaveRequest leaveRequest=leaveRequestRepo.findByRequestId(reqId);
             leaveRequestRepo.delete(leaveRequest);
             return "deleted";
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
