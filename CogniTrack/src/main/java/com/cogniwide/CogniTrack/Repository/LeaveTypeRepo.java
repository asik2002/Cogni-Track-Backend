package com.cogniwide.CogniTrack.Repository;

import com.cogniwide.CogniTrack.Model.LeaveType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeaveTypeRepo extends JpaRepository<LeaveType,Long> {
}
