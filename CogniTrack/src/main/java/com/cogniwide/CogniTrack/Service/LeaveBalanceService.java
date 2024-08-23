package com.cogniwide.CogniTrack.Service;

import com.cogniwide.CogniTrack.DTO.AddLeaveDto;
import com.cogniwide.CogniTrack.Model.LeaveBalance;
import com.cogniwide.CogniTrack.Repository.LeaveBalanceRepo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class LeaveBalanceService {
    @Autowired
    LeaveBalanceRepo leaveBalanceRepo;
    public Long getBalanceDays(String employeeId, Long leaveTypeId) {
        LeaveBalance leaveBalance= this.leaveBalanceRepo.findByEmployeeIdAndLeaveTypeId(employeeId,leaveTypeId);
        if (leaveBalance==null){
            return 0L;
        }
        return leaveBalance.getBalanceDays();
    }

    public Boolean addLeaveBalance(AddLeaveDto addLeaveDto) {
        try{
            LeaveBalance leaveBalance=new LeaveBalance();
            BeanUtils.copyProperties(addLeaveDto,leaveBalance);
            this.leaveBalanceRepo.save(leaveBalance);
            return true;
        }
        catch (DataIntegrityViolationException e){
            return false;
        }
    }
}
