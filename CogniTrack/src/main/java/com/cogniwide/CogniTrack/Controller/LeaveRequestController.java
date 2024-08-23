package com.cogniwide.CogniTrack.Controller;

import com.cogniwide.CogniTrack.DTO.LeaveRequestDto;
import com.cogniwide.CogniTrack.Model.LeaveRequest;
import com.cogniwide.CogniTrack.Service.LeaveRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping
public class LeaveRequestController {
    @Autowired
    LeaveRequestService leaveRequestService;

    @GetMapping(value = "/get-all")
    public List<LeaveRequest> get() {
        return this.leaveRequestService.get();
    }

    @GetMapping(value = "/get-history/{employeeId}")
    public List<LeaveRequest> getHistory(@PathVariable String employeeId, @RequestBody String status) {
        return this.leaveRequestService.getHistory(employeeId, status);
    }

    @GetMapping(value = "/get-request-by-employee/{employeeId}")
    public List<LeaveRequest> getRequestByEmployeeId(@PathVariable String employeeId, @RequestBody String status) {
        return this.leaveRequestService.getRequestsByEmployeeId(employeeId, status);
    }

    @GetMapping(value = "/get-request-by-manager/{approverId}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public List<LeaveRequest> getRequestByApproverId(@PathVariable String approverId, @RequestBody String status) {
        return this.leaveRequestService.getRequestsByApproverId(approverId, status);
    }

    @PostMapping(value = "/request-leave")
    public String leaveRequest(@RequestBody LeaveRequestDto leaveRequestDto) {
        return this.leaveRequestService.requestLeave(leaveRequestDto);
    }

    @PostMapping(value = "/process-leave-request/{reqId}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public void processRequest(@PathVariable Long reqId, @RequestBody LeaveRequestDto leaveRequestDto) {
        if (leaveRequestDto.getStatus().equals("APPROVE")) {
            this.leaveRequestService.approveLeaveRequest(reqId);
        } else if (leaveRequestDto.getStatus().equals("DENY")) {
            this.leaveRequestService.denyLeaveRequest(reqId,leaveRequestDto.getComments());
        }
    }

    @DeleteMapping(value = "/delete-request/{reqId}")
    public String deleteRequest(@PathVariable Long reqId) {
        return this.leaveRequestService.deleteRequest(reqId);
    }

    @PostMapping(value = "/mark-as-read/{requestId}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public void markAsRead(@PathVariable Long requestId) {
        this.leaveRequestService.markAsRead(requestId);
    }
}