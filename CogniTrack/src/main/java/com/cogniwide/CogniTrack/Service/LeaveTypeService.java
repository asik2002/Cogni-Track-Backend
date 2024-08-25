package com.cogniwide.CogniTrack.Service;

import com.cogniwide.CogniTrack.Model.LeaveType;
import com.cogniwide.CogniTrack.Repository.LeaveTypeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LeaveTypeService {
    @Autowired
    LeaveTypeRepo leaveTypeRepo;
    public Boolean addType(LeaveType leaveType) {
        try{
             this.leaveTypeRepo.save(leaveType);
             return true;
        }
        catch (DataIntegrityViolationException e){
             return false;
        }
    }

    public Boolean delete(Long leaveTypeId) {
          LeaveType leaveType = this.leaveTypeRepo.findByLeaveTypeId(leaveTypeId);
          if(leaveType==null) {
              return false;
          }
          this.leaveTypeRepo.delete(leaveType);
          return true;
    }

    public List<LeaveType> getallType() {
        return this.leaveTypeRepo.findAll();
    }
}
