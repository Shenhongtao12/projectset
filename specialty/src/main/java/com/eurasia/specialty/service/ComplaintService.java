package com.eurasia.specialty.service;

import com.eurasia.specialty.entity.Complaint;
import com.eurasia.specialty.repository.ComplaintRepository;
import com.eurasia.specialty.utils.DateUtils;
import com.eurasia.specialty.utils.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Aaron
 * @date 2020/6/8 - 20:46
 **/
@Service
public class ComplaintService {

    @Autowired
    private ComplaintRepository complaintRepository;

    public JsonData save(Complaint complaint) {
        complaint.setCreateTime(DateUtils.dateToString());
        complaint.setStatus(0);
        complaintRepository.save(complaint);
        return JsonData.buildSuccess("投诉成功");
    }

    public Complaint findById(Integer id) {
        return complaintRepository.findById(id).get();
    }

    public JsonData findByUserId(Integer userId) {
        List<Complaint> complaintList = complaintRepository.findByUserId(userId);
        return JsonData.buildSuccess(complaintList,"");
    }

    public JsonData update(Complaint complaint) {
        Complaint byId = findById(complaint.getId());
        byId.setStatus(0);
        byId.setContent(complaint.getContent());
        byId.setEvidenceImages(complaint.getEvidenceImages());
        complaintRepository.save(complaint);
        return JsonData.buildSuccess("更新成功");
    }

    public JsonData delete(Integer id) {
        complaintRepository.deleteById(id);
        return JsonData.buildSuccess("删除成功");
    }
}
