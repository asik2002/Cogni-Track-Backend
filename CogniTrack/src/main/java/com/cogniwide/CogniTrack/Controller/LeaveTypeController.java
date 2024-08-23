package com.cogniwide.CogniTrack.Controller;

import com.cogniwide.CogniTrack.Model.LeaveType;
import com.cogniwide.CogniTrack.Service.LeaveTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class LeaveTypeController {
    @Autowired
    LeaveTypeService leaveTypeService;

    @PostMapping(value = "/add-new-leave-type")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Boolean addType(@RequestBody LeaveType leaveType) {
        return this.leaveTypeService.addType(leaveType);
    }

    @DeleteMapping(value = "/delete-type")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Boolean delete(@RequestBody LeaveType leaveType) {
        return this.leaveTypeService.delete(leaveType);
    }

    @GetMapping(value = "/get-all-type")
    public List<LeaveType> getAllType() {
        return this.leaveTypeService.getallType();
    }
}
