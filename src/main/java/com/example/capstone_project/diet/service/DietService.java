package com.example.capstone_project.diet.service;

import com.example.capstone_project.diet.dto.ReadDietDto;
import io.github.flashvayne.chatgpt.service.ChatgptService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class DietService {

    private final ChatgptService chatgptService;

//     기초 대사량(BMR)을 해리스 배니딕트 방정식을 이용하여 구하고
//     남성 : 66 + (13.7 X 체중 kg) + (5 X 키 cm) - (6.8 X 나이)
//    여성 : 655 + (9.6 X 체중 kg) + (1.7 X 키 cm) - (4.7 X 나이)
//     활동성에 따라 BMR에 MultiNum을 곱해주어 유지 칼로리를 구해줌
//     그리고 사용자가 원하는 상태에 따라 목표 칼로리를 구해준다.
//     그 목표 칼로리를 바탕으로 챗-지피티 API에게 하루 세끼 식단을 짜달라고 한다.
    public ReadDietDto getChatResponse(String gender, Integer age, Float height, Float weight, String activity, String purpose, String vegetarian) {

        float bmr; // 기초대사량
        float mCal; // 유지 칼로리
        float pCal; // 목표 섭취 칼로리

        // 우항은 double형으로 계산되기 때문에 float로 타입 캐스팅
        bmr = (float) ((gender.equals("남자")) ? 66 + (13.7 * weight) + (5 * height) - (6.8 * age) : 655 + (9.6 * weight) + (1.7 * height) - (4.7 * age));

        // activity(활동량)은 거의 없다, 조금 있다, 보통, 꽤 있다, 아주 많다로 나뉨
        mCal = switch (activity) {
            case "거의 없다" -> (float) (bmr * 1.2);
            case "조금 있다" -> (float) (bmr * 1.375);
            case "보통" -> (float) (bmr * 1.55);
            case "꽤 있다" -> (float) (bmr * 1.725);
            case "아주 많다" -> (float) (bmr * 1.9);
            default -> throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "사용자가 선택한 활동량과 일치하는 것이 없음");
        };

        // 감소 목적이면 유지 칼로리에 -20%, 증가 목적이면 유지 칼로리에 +20%
        pCal = switch (purpose) {
            case "감소" -> (float) (0.8 * (mCal * 1.2));
            case "유지" -> (float) (mCal * 1.375);
            case "증가" -> (float) (1.2 * (mCal * 1.55));
            default -> throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "사용자가 선택한 목적과 일치하는 것이 없음");
        };

        if (vegetarian.equals("네")) {
            // 목표 섭취 칼로리를 기준으로 하루 세끼 식단을 짜달라고 할 것임.
            // 탄단지 비율은 가장 이상적인 5 : 3 : 2
            // 아침
            String question1 = "아침에 섭취해야할 칼로리가 " + (pCal * 0.2) + "인데, 이 칼로리에 맞춰서 한국인의 식성에 맞고 조화롭게 건강한 아침 식단을 완전 채식주의자 식단으로 짜줘. 식단의 메뉴 개수는 2개에서 4개 정도로 할게 " +
                    "먼저 음식과 g만 나열하고, 마지막 부분에 총 kcal도 소수점 떼고 적어줘.";

            ReadDietDto readDietDto = new ReadDietDto();
            String morningResponse = chatgptService.sendMessage(question1);
            morningResponse = morningResponse.replaceAll("\n?", "");

            readDietDto.setMorning(morningResponse);

            // 점심
            String question2 = "점심에 섭취해야할 칼로리가 " + (pCal * 0.4) + "인데, 이 칼로리에 맞춰서 한국인의 식성에 맞고 조화롭게 건강한 점심 식단을 완전 채식주의자 식단으로 짜줘. 식단의 메뉴 개수는 2개에서 4개 정도로 할게  " +
                    "먼저 음식과 g만 나열하고, 마지막 부분에 총 kcal도 소수점 떼고 적어줘.";

            String lunchResponse = chatgptService.sendMessage(question2);
            lunchResponse = lunchResponse.replaceAll("\n?", "");

            readDietDto.setLunch(lunchResponse);

            // 저녁
            String question3 = "저녁에 섭취해야할 칼로리가 " + (pCal * 0.4) + "인데, 이 칼로리에 맞춰서 한국인의 식성에 맞고 조화롭게 건강한 저녁 식단을 완전 채식주의자 식단으로 짜줘. 식단의 메뉴 개수는 2개에서 4개 정도로 할게 " +
                    "먼저 음식과 g만 나열하고, 마지막 부분에 총 kcal도 소수점 떼고 적어줘.";

            String dinnerResponse = chatgptService.sendMessage(question3);
            dinnerResponse = dinnerResponse.replaceAll("\n?", "");

            readDietDto.setDinner(dinnerResponse);

            return readDietDto;
        } else {
            // 목표 섭취 칼로리를 기준으로 하루 세끼 식단을 짜달라고 할 것임.
            // 탄단지 비율은 가장 이상적인 5 : 3 : 2
            // 아침
            String question1 = "아침에 섭취해야할 칼로리가 " + (pCal * 0.2) + "인데, 이 칼로리에 맞춰서 한국인의 식성에 맞고 조화롭게 건강한 아침 식단을 짜줘. 식단의 메뉴 개수는 2개에서 4개 정도로 할게 " +
                    "한 끼 식사의 탄수화물:단백질:지방 비율은 5:3:2로 할게. 먼저 음식과 g만 나열하고, 마지막 부분에 총 kcal도 소수점 떼고 적어줘.";

            ReadDietDto readDietDto = new ReadDietDto();
            String morningResponse = chatgptService.sendMessage(question1);
            morningResponse = morningResponse.replaceAll("\n?", "");

            readDietDto.setMorning(morningResponse);

            // 점심
            String question2 = "점심에 섭취해야할 칼로리가 " + (pCal * 0.4) + "인데, 이 칼로리에 맞춰서 한국인의 식성에 맞고 조화롭게 건강한 점심 식단을 짜줘. 식단의 메뉴 개수는 2개에서 4개 정도로 할게  " +
                    "한 끼 식사의 탄수화물:단백질:지방 비율은 5:3:2로 할게. 먼저 음식과 g만 나열하고, 마지막 부분에 총 kcal도 소수점 떼고 적어줘.";

            String lunchResponse = chatgptService.sendMessage(question2);
            lunchResponse = lunchResponse.replaceAll("\n?", "");

            readDietDto.setLunch(lunchResponse);

            // 저녁
            String question3 = "저녁에 섭취해야할 칼로리가 " + (pCal * 0.4) + "인데, 이 칼로리에 맞춰서 한국인의 식성에 맞고 조화롭게 건강한 저녁 식단을 짜줘. 식단의 메뉴 개수는 2개에서 4개 정도로 할게 " +
                    "한 끼 식사의 탄수화물:단백질:지방 비율은 5:3:2로 할게. 먼저 음식과 g만 나열하고, 마지막 부분에 총 kcal도 소수점 떼고 적어줘.";

            String dinnerResponse = chatgptService.sendMessage(question3);
            dinnerResponse = dinnerResponse.replaceAll("\n?", "");

            readDietDto.setDinner(dinnerResponse);

            return readDietDto;
        }
    }

}
