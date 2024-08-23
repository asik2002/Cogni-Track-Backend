package com.cogniwide.CogniTrack.Repository;

import com.cogniwide.CogniTrack.Model.LeaveBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeaveBalanceRepo extends JpaRepository<LeaveBalance,Long> {
    LeaveBalance findByEmployeeIdAndLeaveTypeId(String employeeId, Long leaveTypeId);
}
