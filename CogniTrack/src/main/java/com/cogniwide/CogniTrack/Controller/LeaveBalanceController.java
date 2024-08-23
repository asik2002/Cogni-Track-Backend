package com.cogniwide.CogniTrack.Controller;
import com.cogniwide.CogniTrack.DTO.AddLeaveDto;
import com.cogniwide.CogniTrack.Service.LeaveBalanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/leave-balance")
public class LeaveBalanceController {
    @Autowired
    LeaveBalanceService leaveBalanceService;
    @GetMapping(value = "/get-balance-leave/{employeeId}")
    public Long getBalanceDays(@PathVariable String employeeId, @RequestBody Long leaveTypeId){
       return this.leaveBalanceService.getBalanceDays(employeeId,leaveTypeId);
    }
    @PostMapping(value="/add-leave-balance")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Boolean addLeaveBalance(@RequestBody AddLeaveDto addLeaveDto){
        return this.leaveBalanceService.addLeaveBalance(addLeaveDto);
    }
}
