package com.example.demo.service;

import com.example.demo.dto.ChartDataDto;
import com.example.demo.entity.AlarmLog;
import com.example.demo.entity.Client;
import com.example.demo.entity.WebCamLog;
import com.example.demo.repository.AlarmLogRepository;
import com.example.demo.repository.WebCamLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ChartDataService {

    @Autowired
    WebCamLogRepository webCamLogRepository;

    @Autowired
    AlarmLogRepository alarmLogRepository;


    public List<ChartDataDto> getChartData(Client clientId) {

//        Long clientId = getCurrentUserId(); // 현재 인증된 사용자의 userId를 얻는 메소드

        // 사용자별 WebCamLog와 AlarmLog를 조회합니다.
        List<WebCamLog> webcamLogs = webCamLogRepository.findByClientId(clientId);
        List<AlarmLog> alarmLogs = alarmLogRepository.findByClientId(clientId);

        // 요일별로 웹캠 실행 시간을 집계합니다.
        Map<DayOfWeek, Long> webcamDurationByDay = webcamLogs.stream()
                .collect(Collectors.groupingBy(
                        log -> log.getStartTime().getDayOfWeek(),
                        Collectors.summingLong(log -> ChronoUnit.MINUTES.between(log.getStartTime(), log.getEndTime()))
                ));

        // 요일별로 알람 발생 횟수를 집계합니다.
        Map<DayOfWeek, Long> alarmCountByDay = alarmLogs.stream()
                .collect(Collectors.groupingBy(
                        log -> log.getTime().getDayOfWeek(), // getTime()이 LocalDateTime을 반환한다고 가정합니다.
                        Collectors.counting()
                ));

        // 집계된 데이터를 ChartDataDTO 리스트로 변환합니다.
        List<ChartDataDto> chartData = new ArrayList<>();
        for (DayOfWeek day : DayOfWeek.values()) {
            long webcamDuration = webcamDurationByDay.getOrDefault(day, 0L);
            long alarmCount = alarmCountByDay.getOrDefault(day, 0L);
            chartData.add(new ChartDataDto(day, webcamDuration, alarmCount));
        }

        return chartData;
    }

    private Long getCurrentUserId() {

//        Object principal = SecurityContextHolder.getContext().getAuthentication().getPricinpal();
//        Long userId;
//
//        if (principal instanceof CustomUserDetails) {
//            userId = ((CustomUserDetails)principal).getUserId(); // CustomUserDetails는 userId를 포함하도록 확장한 클래스
//        } else {
//            throw new IllegalStateException("인증된 사용자 정보를 얻을 수 없습니다.");
//        }

        return null;
    }

}

