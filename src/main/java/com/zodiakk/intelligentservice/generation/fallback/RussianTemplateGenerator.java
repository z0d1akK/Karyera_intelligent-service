package com.zodiakk.intelligentservice.generation.fallback;

import com.zodiakk.intelligentservice.career.dto.request.CareerExplanationRequestDto;
import com.zodiakk.intelligentservice.career.dto.request.CareerPathCandidateDto;
import com.zodiakk.intelligentservice.career.dto.request.CareerPathInterpretationRequestDto;
import com.zodiakk.intelligentservice.career.dto.response.CareerExplanationResponseDto;
import com.zodiakk.intelligentservice.career.dto.response.CareerPathDirectionExplanationDto;
import com.zodiakk.intelligentservice.career.dto.response.CareerPathInterpretationResponseDto;
import com.zodiakk.intelligentservice.common.dto.DevelopmentAreaExplanationDto;
import com.zodiakk.intelligentservice.common.dto.SkillGapContextDto;
import com.zodiakk.intelligentservice.common.dto.SkillStrengthDto;
import com.zodiakk.intelligentservice.common.enums.RecommendationType;
import com.zodiakk.intelligentservice.development.dto.request.DevelopmentAreaContextDto;
import com.zodiakk.intelligentservice.development.dto.request.DevelopmentRecommendationRequestDto;
import com.zodiakk.intelligentservice.development.dto.response.DevelopmentRecommendationDto;
import com.zodiakk.intelligentservice.development.dto.response.DevelopmentRecommendationResponseDto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Component
public class RussianTemplateGenerator {

    public static final String MODEL_IDENTIFIER = "template-fallback";

    public CareerExplanationResponseDto explain(CareerExplanationRequestDto request) {
        String position = request.getTarget() == null ? "целевой должности" : name(request.getTarget().getPositionName());
        List<String> strengths = new ArrayList<>();
        if (request.getStrengths() != null) {
            for (SkillStrengthDto strength : request.getStrengths()) {
                strengths.add(name(strength.getSkillName()) + " уже на уровне "
                        + strength.getActualLevel() + " из " + strength.getRequiredLevel()
                        + " — это опора для перехода на " + position + ".");
            }
        }

        List<DevelopmentAreaExplanationDto> areas = new ArrayList<>();
        if (request.getSkillGaps() != null) {
            for (SkillGapContextDto gap : request.getSkillGaps()) {
                int actual = gap.getActualLevel() == null ? 0 : gap.getActualLevel();
                int required = gap.getRequiredLevel() == null ? 0 : gap.getRequiredLevel();
                areas.add(DevelopmentAreaExplanationDto.builder()
                        .skillId(gap.getSkillId())
                        .explanation("Для " + position + " по навыку " + name(gap.getSkillName())
                                + " сейчас " + actual + " из " + required
                                + " (разрыв " + Math.max(required - actual, 0)
                                + "). Следующий шаг — взять рабочую задачу, где этот навык основной, и довести её до результата, который можно показать руководителю.")
                        .build());
            }
        }

        List<String> limitations = new ArrayList<>();
        limitations.add("Результат не является автоматическим решением о повышении.");
        if (!"COMPLETE".equals(request.getDataQualityStatus())) {
            limitations.add("Часть исходных данных была неполной, поэтому объяснение ограничено доступными баллами.");
        }

        return CareerExplanationResponseDto.builder()
                .assessmentId(request.getAssessmentId())
                .summary("Сотрудник соответствует " + position + " на " + percent(request.getScores().getTotal())
                        + ". По навыкам " + percent(request.getScores().getSkill())
                        + ". " + (Boolean.TRUE.equals(request.getMandatoryRequirementsMet())
                        ? "Обязательные требования закрыты, дальше имеет смысл усиливать оставшиеся разрывы."
                        : "Сначала закройте обязательные требования, затем подтягивайте остальные навыки."))
                .strengths(strengths)
                .developmentAreas(areas)
                .limitations(limitations)
                .modelIdentifier(MODEL_IDENTIFIER)
                .build();
    }

    public DevelopmentRecommendationResponseDto recommend(DevelopmentRecommendationRequestDto request) {
        int limit = request.getPreferences() == null || request.getPreferences().getMaxRecommendations() == null
                ? request.getDevelopmentAreas().size()
                : request.getPreferences().getMaxRecommendations();
        boolean mentoring = request.getPreferences() != null && Boolean.TRUE.equals(request.getPreferences().getIncludeMentoring());
        boolean project = request.getPreferences() == null || !Boolean.FALSE.equals(request.getPreferences().getIncludeProjectActivities());

        List<DevelopmentRecommendationDto> recommendations = new ArrayList<>();
        int index = 0;
        for (DevelopmentAreaContextDto area : request.getDevelopmentAreas()) {
            if (recommendations.size() >= limit) {
                break;
            }
            RecommendationType type = mentoring && index % 2 == 1
                    ? RecommendationType.MENTORING
                    : project ? RecommendationType.PROJECT_EXPERIENCE : RecommendationType.SELF_STUDY;
            recommendations.add(DevelopmentRecommendationDto.builder()
                    .skillId(area.getSkillId())
                    .type(type)
                    .title("Подтянуть " + area.getSkillName() + " с " + area.getCurrentLevel() + " до " + area.getTargetLevel())
                    .description("Разрыв по навыку " + area.getSkillName() + " составляет "
                            + Math.max((area.getTargetLevel() == null ? 0 : area.getTargetLevel())
                            - (area.getCurrentLevel() == null ? 0 : area.getCurrentLevel()), 0)
                            + ". В ближайшие недели возьмите задачу, где этот навык главный, разберите рабочий пример и покажите результат руководителю.")
                    .suggestedActivities(List.of(
                            "Выбрать одну боевую задачу по навыку " + area.getSkillName() + " и довести её до ревью.",
                            "Зафиксировать, что изменилось в результате: качество, скорость или самостоятельность."
                    ))
                    .expectedOutcome("Сдвиг по навыку " + area.getSkillName() + " виден на конкретной задаче, а не только в самооценке.")
                    .build());
            index++;
        }

        return DevelopmentRecommendationResponseDto.builder().recommendations(recommendations).build();
    }

    public CareerPathInterpretationResponseDto interpret(CareerPathInterpretationRequestDto request) {
        List<CareerPathCandidateDto> ordered = request.getDirections().stream()
                .sorted(Comparator.comparingInt((CareerPathCandidateDto direction) -> direction.getSkillGaps().size())
                        .thenComparing(CareerPathCandidateDto::getTotalScore, Comparator.reverseOrder()))
                .toList();

        List<CareerPathDirectionExplanationDto> directions = ordered.stream()
                .map(direction -> CareerPathDirectionExplanationDto.builder()
                        .targetPositionId(direction.getTargetPositionId())
                        .targetPositionName(direction.getTargetPositionName())
                        .fitSummary("Рассчитанное соответствие направлению " + direction.getTargetPositionName()
                                + " составляет " + percent(direction.getTotalScore()) + ".")
                        .strengths(direction.getStrengths().stream().map(SkillStrengthDto::getSkillName).toList())
                        .developmentAreas(direction.getSkillGaps().stream().map(SkillGapContextDto::getSkillName).toList())
                        .build())
                .toList();

        CareerPathCandidateDto recommended = ordered.getFirst();
        return CareerPathInterpretationResponseDto.builder()
                .summary("Сравнены только направления, которые уже разрешены карьерным треком.")
                .directions(directions)
                .recommendedDirection(recommended.getTargetPositionName())
                .recommendationRationale("У направления " + recommended.getTargetPositionName()
                        + " меньше выявленных пробелов, чем у остальных переданных вариантов. Срок перехода не прогнозируется.")
                .limitations(List.of("Новые должности не добавлялись. Результат не является решением о переводе."))
                .modelIdentifier(MODEL_IDENTIFIER)
                .build();
    }

    public com.zodiakk.intelligentservice.screening.dto.TalentScreeningExplanationResponseDto explainScreening(
            com.zodiakk.intelligentservice.screening.dto.TalentScreeningExplanationRequestDto request
    ) {
        int count = request.getCandidates() == null ? 0 : request.getCandidates().size();
        String top = count == 0 || request.getCandidates().getFirst().getEmployeeName() == null
                ? "лидера рейтинга"
                : request.getCandidates().getFirst().getEmployeeName();
        return com.zodiakk.intelligentservice.screening.dto.TalentScreeningExplanationResponseDto.builder()
                .summary("По целевой должности " + (request.getTargetPositionName() == null ? "" : request.getTargetPositionName())
                        + " сравнены " + count + " кандидатов. Выше в рейтинге " + top
                        + " по уже рассчитанному баллу. Это не решение о включении в резерв.")
                .highlights(List.of("Рейтинг построен только по рассчитанным оценкам соответствия."))
                .limitations(List.of("Текст не назначает людей в резерв и не обещает сроки."))
                .modelIdentifier(MODEL_IDENTIFIER)
                .status("FALLBACK")
                .build();
    }

    private String name(String skillName) {
        return skillName == null || skillName.isBlank() ? "навыка" : skillName;
    }

    private String percent(BigDecimal score) {
        if (score == null) {
            return "не рассчитано";
        }
        return score.multiply(BigDecimal.valueOf(100)).setScale(1, RoundingMode.HALF_UP) + "%";
    }
}
