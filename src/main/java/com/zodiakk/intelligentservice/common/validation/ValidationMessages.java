package com.zodiakk.intelligentservice.common.validation;

public final class ValidationMessages {

    private ValidationMessages() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static final String ACTIVE_REQUIRED = "Флаг активности обязателен для заполнения";

    public static final String ASSESSMENT_ID_REQUIRED = "Идентификатор оценки обязателен для заполнения";

    public static final String CANDIDATE_COUNT_MIN = "Количество кандидатов не может быть меньше {value}";

    public static final String CANDIDATE_COUNT_REQUIRED = "Количество кандидатов обязательно для заполнения";

    public static final String CODE_REQUIRED = "Код обязателен для заполнения";

    public static final String CODE_SIZE = "Код должен содержать не более {max} символов";

    public static final String CORRELATION_ID_REQUIRED = "Идентификатор корреляции обязателен для заполнения";

    public static final String CURRENT_GRADE_NAME_SIZE = "Название текущего грейда должно содержать не более {max} символов";

    public static final String CURRENT_LEVEL_MAX = "Текущий уровень не может превышать {value}";

    public static final String CURRENT_LEVEL_MIN = "Текущий уровень не может быть меньше {value}";

    public static final String CURRENT_LEVEL_REQUIRED = "Текущий уровень обязателен для заполнения";

    public static final String CURRENT_POSITION_NAME_SIZE = "Название текущей должности должно содержать не более {max} символов";

    public static final String DATA_QUALITY_STATUS_REQUIRED = "Статус качества данных обязателен для заполнения";

    public static final String DATA_QUALITY_STATUS_SIZE = "Статус качества данных должен содержать не более {max} символов";

    public static final String DEVELOPMENT_AREAS_NOT_EMPTY = "Список «Зоны развития» должен содержать хотя бы один элемент";

    public static final String DIRECTIONS_NOT_EMPTY = "Список «Карьерные направления» должен содержать хотя бы один элемент";

    public static final String ELIGIBILITY_STATUS_REQUIRED = "Статус соответствия обязателен для заполнения";

    public static final String ELIGIBILITY_STATUS_SIZE = "Статус соответствия должен содержать не более {max} символов";

    public static final String EMPLOYEE_ID_REQUIRED = "Идентификатор сотрудника обязателен для заполнения";

    public static final String ERROR_CODE_SIZE = "Код ошибки должен содержать не более {max} символов";

    public static final String EXPERIENCE_MAX = "Опыт не может превышать {value}";

    public static final String EXPERIENCE_MIN = "Опыт не может быть меньше {value}";

    public static final String EXPERIENCE_REQUIRED = "Опыт обязателен для заполнения";

    public static final String GRADE_NAME_SIZE = "Название грейда должно содержать не более {max} символов";

    public static final String IDEMPOTENCY_KEY_SIZE = "Ключ идемпотентности должен содержать не более {max} символов";

    public static final String LANGUAGE_REQUIRED = "Язык обязателен для заполнения";

    public static final String LANGUAGE_SIZE = "Язык должен содержать не более {max} символов";

    public static final String MANDATORY_REQUIREMENTS_MET_REQUIRED = "Флаг выполнения обязательных требований обязателен для заполнения";

    public static final String MAX_OUTPUT_TOKENS_REQUIRED = "Максимальное количество выходных токенов обязательно для заполнения";

    public static final String MAX_RECOMMENDATIONS_MAX = "Максимальное количество рекомендаций не может превышать {value}";

    public static final String MAX_RECOMMENDATIONS_MIN = "Максимальное количество рекомендаций не может быть меньше {value}";

    public static final String MODEL_CONFIG_ID_REQUIRED = "Идентификатор конфигурации модели обязателен для заполнения";

    public static final String MODEL_NAME_REQUIRED = "Название модели обязательно для заполнения";

    public static final String MODEL_NAME_SIZE = "Название модели должно содержать не более {max} символов";

    public static final String OUTPUT_SCHEMA_VERSION_REQUIRED = "Версия схемы вывода обязательна для заполнения";

    public static final String OUTPUT_SCHEMA_VERSION_SIZE = "Версия схемы вывода должна содержать не более {max} символов";

    public static final String PAGE_MAX_SIZE = "Размер страницы не может превышать {value}";

    public static final String PAGE_MIN_SIZE = "Размер страницы должен быть больше нуля";

    public static final String PAGE_NUMBER_MIN = "Номер страницы не может быть отрицательным";

    public static final String POSITION_NAME_REQUIRED = "Название должности обязательно для заполнения";

    public static final String POSITION_NAME_SIZE = "Название должности должно содержать не более {max} символов";

    public static final String PRIORITY_SCORE_MAX = "Приоритетный балл не может превышать {value}";

    public static final String PRIORITY_SCORE_MIN = "Приоритетный балл не может быть меньше {value}";

    public static final String PRIORITY_SCORE_REQUIRED = "Приоритетный балл обязателен для заполнения";

    public static final String PROMPT_TEMPLATE_ID_REQUIRED = "Идентификатор шаблона промпта обязателен для заполнения";

    public static final String PROVIDER_REQUIRED = "Провайдер обязателен для заполнения";

    public static final String PROVIDER_SIZE = "Провайдер должен содержать не более {max} символов";

    public static final String RANK_MIN = "Ранг не может быть меньше {value}";

    public static final String RANK_REQUIRED = "Ранг обязателен для заполнения";

    public static final String SCORES_REQUIRED = "Оценки обязательны для заполнения";

    public static final String SCORE_MAX = "Балл не может превышать {value}";

    public static final String SCORE_MIN = "Балл не может быть меньше {value}";

    public static final String SCORE_REQUIRED = "Балл обязателен для заполнения";

    public static final String SCREENING_ID_REQUIRED = "Идентификатор отбора обязателен для заполнения";

    public static final String SKILL_GAPS_REQUIRED = "Дефициты навыков обязательны для заполнения";

    public static final String SKILL_ID_REQUIRED = "Идентификатор навыка обязателен для заполнения";

    public static final String SKILL_MAX = "Навык не может превышать {value}";

    public static final String SKILL_MIN = "Навык не может быть меньше {value}";

    public static final String SKILL_NAME_REQUIRED = "Название навыка обязательно для заполнения";

    public static final String SKILL_NAME_SIZE = "Название навыка должно содержать не более {max} символов";

    public static final String SKILL_REQUIRED = "Навык обязателен для заполнения";

    public static final String STRENGTHS_REQUIRED = "Сильные стороны обязательны для заполнения";

    public static final String SYSTEM_PROMPT_REQUIRED = "Системный промпт обязателен для заполнения";

    public static final String TARGET_GRADE_NAME_SIZE = "Название целевого грейда должно содержать не более {max} символов";

    public static final String TARGET_LEVEL_MAX = "Целевой уровень не может превышать {value}";

    public static final String TARGET_LEVEL_MIN = "Целевой уровень не может быть меньше {value}";

    public static final String TARGET_LEVEL_REQUIRED = "Целевой уровень обязателен для заполнения";

    public static final String TARGET_POSITION_ID_REQUIRED = "Идентификатор целевой должности обязателен для заполнения";

    public static final String TARGET_POSITION_NAME_REQUIRED = "Название целевой должности обязательно для заполнения";

    public static final String TARGET_POSITION_NAME_SIZE = "Название целевой должности должно содержать не более {max} символов";

    public static final String TARGET_REQUIRED = "Целевые данные обязательны для заполнения";

    public static final String TASK_TYPE_REQUIRED = "Тип задачи обязателен для заполнения";

    public static final String TASK_TYPE_SIZE = "Тип задачи должен содержать не более {max} символов";

    public static final String TEMPERATURE_REQUIRED = "Температура обязательна для заполнения";

    public static final String TIMEOUT_SECONDS_REQUIRED = "Таймаут в секундах обязателен для заполнения";

    public static final String TOTAL_MAX = "Итоговая оценка не может превышать {value}";

    public static final String TOTAL_MIN = "Итоговая оценка не может быть меньше {value}";

    public static final String TOTAL_REQUIRED = "Итоговая оценка обязательна для заполнения";

    public static final String TOTAL_SCORE_MAX = "Итоговый балл не может превышать {value}";

    public static final String TOTAL_SCORE_MIN = "Итоговый балл не может быть меньше {value}";

    public static final String TOTAL_SCORE_REQUIRED = "Итоговый балл обязателен для заполнения";

    public static final String USER_PROMPT_TEMPLATE_REQUIRED = "Шаблон пользовательского промпта обязателен для заполнения";

    public static final String VERSION_REQUIRED = "Версия обязательна для заполнения";
}
