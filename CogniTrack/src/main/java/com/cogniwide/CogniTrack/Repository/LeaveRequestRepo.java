package com.cogniwide.CogniTrack.Repository;

import com.cogniwide.CogniTrack.Model.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface LeaveRequestRepo extends JpaRepository<LeaveRequest,Long> {
  LeaveRequest findByRequestId(Long requestId);
  @Query(value = "SELECT e FROM LeaveRequest e WHERE e.employeeId = :employeeId AND e.startDate >= :today AND e.status = :status")
  List<LeaveRequest> findRequestsForEmployee(@Param("today") Date today, @Param("employeeId") String employeeId,@Param("status") String status);
  @Query(value = "SELECT e FROM LeaveRequest e WHERE e.approverId = :approverId AND e.startDate >= :today AND e.status = :status")
  List<LeaveRequest> findRequestsForApprover(@Param("today") Date today,@Param("approverId") String approverId, @Param("status") String status);
  @Query(value="SELECT e FROM LeaveRequest e WHERE e.employeeId = :employeeId AND e.startDate< :today AND e.status= :status")
  List<LeaveRequest> getHistory(@Param("employeeId") String employeeId,@Param("today") Date today,@Param("status")String status);
}
