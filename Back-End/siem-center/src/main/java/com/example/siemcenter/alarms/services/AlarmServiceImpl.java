package com.example.siemcenter.alarms.services;

import com.example.siemcenter.alarms.dtos.AlarmFilterDTO;
import com.example.siemcenter.alarms.dtos.AlarmSearchDTO;
import com.example.siemcenter.alarms.models.Alarm;
import com.example.siemcenter.alarms.repositories.AlarmRepository;
import com.example.siemcenter.common.models.FactStatus;
import com.example.siemcenter.rules.services.RuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AlarmServiceImpl implements AlarmService {
    private AlarmRepository alarmRepository;
    private RuleService ruleService;

    @Autowired
    public AlarmServiceImpl(AlarmRepository alarmRepository,
                            RuleService ruleService) {
        this.alarmRepository = alarmRepository;
        this.ruleService = ruleService;
    }

    @Override
    public List<Alarm> findAllAlarms() {
        return alarmRepository.findAll();
    }

    @Override
    public void insertNewAlarm(Alarm alarm) {
        alarmRepository.save(alarm);
    }

    @Override
    public List<Alarm> findAlarmsThatMatchCriteria(AlarmSearchDTO alarmDTO) {
        alarmDTO = formatDTO(alarmDTO);
        List<Alarm> alarmList = fetchAlarms(alarmDTO);

        if(!alarmDTO.getFactStatus().equals("")) {
            AlarmSearchDTO finalDTO = alarmDTO;
            alarmList = alarmList.stream()
                    .filter(alarm -> alarm.getFactStatus() == FactStatus.valueOf(finalDTO.getFactStatus()))
                    .collect(Collectors.toList());
        }

        if(alarmDTO.getStartDate() != null) {
            AlarmSearchDTO finalDTO = alarmDTO;
            alarmList = alarmList.stream()
                    .filter(alarm -> alarm.getTimestamp().toLocalDate().isAfter(finalDTO.getStartDate()))
                    .collect(Collectors.toList());
        }

        if(alarmDTO.getEndDate() != null) {
            AlarmSearchDTO finalDTO = alarmDTO;
            alarmList = alarmList.stream()
                    .filter(alarm -> alarm.getTimestamp().toLocalDate().isBefore(finalDTO.getEndDate()))
                    .collect(Collectors.toList());
        }

        return alarmList;
    }

    @Override
    public List<Alarm> filterAlarms(AlarmFilterDTO dto) {
        dto = formatFilterDTO(dto);
        List<Alarm> alarmList;

        if(dto.isAlarmsPerMachine()) {
            alarmList = alarmRepository.findByIpAddress(dto.getChosenDevice());
        }
        else if (dto.isAlarmsPerSystem()) {
            alarmList = alarmRepository.findByOs(dto.getChosenSystem());
        }
        else {
            alarmList = alarmRepository.findAll();
        }

        if(dto.getStartDate() != null) {
            AlarmFilterDTO filterDTO = dto;
            alarmList = alarmList.stream()
                    .filter(log -> log.getTimestamp().toLocalDate().isAfter(filterDTO.getStartDate()))
                    .collect(Collectors.toList());
        }

        if(dto.getEndDate() != null) {
            AlarmFilterDTO filterDTO = dto;
            alarmList = alarmList.stream()
                    .filter(log -> log.getTimestamp().toLocalDate().isBefore(filterDTO.getEndDate()))
                    .collect(Collectors.toList());
        }

        return alarmList;
    }

    private AlarmFilterDTO formatFilterDTO(AlarmFilterDTO dto) {
        if(dto.getDate().equals("null")) {
            return dto;
        }
        String[] date = dto.getDate().split(",");

        String startString = date[0].substring(1);
        Timestamp startTimestamp = new Timestamp(Long.parseLong(startString));
        LocalDate start = startTimestamp.toLocalDateTime().toLocalDate();

        String endString = date[1].substring(0, date[1].length()-1);
        Timestamp endTimestamp = new Timestamp(Long.parseLong(endString));
        LocalDate end = endTimestamp.toLocalDateTime().toLocalDate();

        dto.setStartDate(start);
        dto.setEndDate(end);

        return dto;
    }

    private AlarmSearchDTO formatDTO(AlarmSearchDTO dto) {
        if(dto.getDate().equals("null")) {
            return dto;
        }
        dto.setMessage(dto.getMessage().trim());
        String[] date = dto.getDate().split(",");

        String startString = date[0].substring(1);
        Timestamp startTimestamp = new Timestamp(Long.parseLong(startString));
        LocalDate start = startTimestamp.toLocalDateTime().toLocalDate();

        String endString = date[1].substring(0, date[1].length()-1);
        Timestamp endTimestamp = new Timestamp(Long.parseLong(endString));
        LocalDate end = endTimestamp.toLocalDateTime().toLocalDate();

        dto.setStartDate(start);
        dto.setEndDate(end);

        return dto;
    }

    private List<Alarm> fetchAlarms(AlarmSearchDTO dto) {
        if(!dto.isRegex() && !dto.getMessage().equals("")) {
            return alarmRepository.findByMessageContains(dto.getMessage());
        }

        if(dto.isRegex() && !dto.getMessage().equals("")) {
            return ruleService.fetchAlarmsByRegex(dto.getMessage());
        }

        return alarmRepository.findAll();
    }
}