package com.ace.ai.admin.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ace.ai.admin.datamodel.BatchExamForm;
import com.ace.ai.admin.dtomodel.ExamScheduleDTO;
import com.ace.ai.admin.repository.BatchExamFormRepository;
import com.ace.ai.admin.repository.ExamFormRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ExamScheduleService {
    
    @Autowired
    BatchExamFormRepository batchExamFormRepository;

    @Autowired
    ExamFormRepository examFormRepository;

    public List<ExamScheduleDTO> showExamScheduleTable(Integer batchId) throws ParseException{

        List<ExamScheduleDTO>examScheduleDTOList = new ArrayList<>();
        List<BatchExamForm>batchExamFormList = batchExamFormRepository.findByBatch_Id(batchId);

        for(BatchExamForm batchExamForm:batchExamFormList){

            ExamScheduleDTO examScheduleDTO = new ExamScheduleDTO();
            examScheduleDTO.setId(batchExamForm.getId());
            examScheduleDTO.setExamName(batchExamForm.getExamForm().getName());
            examScheduleDTO.setStartDate(batchExamForm.getStartDate());
            examScheduleDTO.setEndDate(batchExamForm.getEndDate());
            
                       

            examScheduleDTOList.add(examScheduleDTO);
        }

        return examScheduleDTOList;
    }

    public void saveBathExamFrom(BatchExamForm bef){
        batchExamFormRepository.save(bef);
    }

    public BatchExamForm findById(int id){
        return batchExamFormRepository.getById(id);
    }

}