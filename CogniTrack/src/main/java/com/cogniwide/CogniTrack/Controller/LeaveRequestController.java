package com.cogniwide.CogniTrack.Controller;

import com.cogniwide.CogniTrack.DTO.LeaveRequestDto;
import com.cogniwide.CogniTrack.Model.LeaveRequest;
import com.cogniwide.CogniTrack.Service.LeaveRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/leave-requests")
public class LeaveRequestController {
    @Autowired
    LeaveRequestService leaveRequestService;

    @GetMapping("/all")
    public List<LeaveRequest> get() {
        return this.leaveRequestService.get();
    }

    @GetMapping("/history/{employeeId}")
    public List<LeaveRequest> getHistory(@PathVariable String employeeId, @RequestBody String status) {
        return this.leaveRequestService.getHistory(employeeId, status);
    }

    @GetMapping("/employee/{employeeId}")
    public List<LeaveRequest> getRequestByEmployeeId(@PathVariable String employeeId, @RequestBody String status) {
        return this.leaveRequestService.getRequestsByEmployeeId(employeeId, status);
    }

    @GetMapping("/manager/{approverId}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public List<LeaveRequest> getRequestByApproverId(@PathVariable String approverId, @RequestBody String status) {
        return this.leaveRequestService.getRequestsByApproverId(approverId, status);
    }

    @PostMapping("/request")
    public String leaveRequest(@RequestBody LeaveRequestDto leaveRequestDto) {
        return this.leaveRequestService.requestLeave(leaveRequestDto);
    }

    @PostMapping("/process/{reqId}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public void processRequest(@PathVariable Long reqId, @RequestBody LeaveRequestDto leaveRequestDto) {
        if (leaveRequestDto.getStatus().equals("APPROVE")) {
            this.leaveRequestService.approveLeaveRequest(reqId);
        } else if (leaveRequestDto.getStatus().equals("DENY")) {
            this.leaveRequestService.denyLeaveRequest(reqId,leaveRequestDto.getComments());
        }
    }

    @DeleteMapping("/delete/{reqId}")
    public String deleteRequest(@PathVariable Long reqId) {
        return this.leaveRequestService.deleteRequest(reqId);
    }

    @PostMapping("/mark-as-read/{requestId}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public void markAsRead(@PathVariable Long requestId) {
        this.leaveRequestService.markAsRead(requestId);
    }
}