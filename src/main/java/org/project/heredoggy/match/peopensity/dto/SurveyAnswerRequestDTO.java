package org.project.heredoggy.match.peopensity.dto;

import lombok.*;
import org.project.heredoggy.domain.postgresql.match.propensity.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SurveyAnswerRequestDTO {
    private boolean popularity;
    private boolean firstTimeOwner;
    private boolean noiseTolerance;
    private boolean allergyConcern;
    private DogSize size;
    private Sensitivity hairLossSensitivity;
    private ExerciseTime exerciseTime;
    private boolean kidsInHouse;
    private Sensitivity barkingTolerance;
    private AloneTime aloneTime;
    private HomeType homeType;
    private MedicalBudget medicalBudget;
    private GroomingWillingness groomingWillingness;
    private TrainingEffort trainingEffort;
    private ActivityLevel activityLevel;

}
