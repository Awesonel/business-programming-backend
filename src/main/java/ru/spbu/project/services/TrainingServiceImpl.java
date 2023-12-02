package ru.spbu.project.services;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import ru.spbu.project.models.Employee;
import ru.spbu.project.models.Leader;
import ru.spbu.project.models.Test;
import ru.spbu.project.models.dto.TestDTO;
import ru.spbu.project.models.dto.TrainingApplicationDTO;
import ru.spbu.project.models.enums.Stage;
import ru.spbu.project.models.enums.TestType;
import ru.spbu.project.models.exceptions.DifferentStageException;
import ru.spbu.project.models.exceptions.TestTypeException;
import ru.spbu.project.models.exceptions.TimeUpException;
import ru.spbu.project.repositories.EmployeeRepository;
import ru.spbu.project.repositories.LeaderRepository;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import ru.spbu.project.repositories.TestRepository;

@Service
public class TrainingServiceImpl implements TrainingService {

  final LeaderRepository leaderRepository;
  final EmployeeRepository employeeRepository;
  final EmployeeService employeeService;
  final TestRepository testRepository;
  static final int INVITATION_TIME_LIMIT = 3;
  static final int ENTRY_TEST_TIME_LIMIT = 5;
  static final int STUDY_TIME_LIMIT = 30;
  static final int EXAM_TIME_LIMIT = 1;

  public TrainingServiceImpl(LeaderRepository leaderRepository,
      EmployeeRepository employeeRepository, EmployeeService employeeService, TestRepository testRepository) {
    this.leaderRepository = leaderRepository;
    this.employeeRepository = employeeRepository;
    this.employeeService = employeeService;
    this.testRepository = testRepository;
  }


  @Override
  public long applyForTraining(TrainingApplicationDTO applicationDTO) {
    List<Leader> leaders = leaderRepository.findByData(
            applicationDTO.getLeaderSurname(),
            applicationDTO.getLeaderName(),
            applicationDTO.getLeaderPatronymic(),
            applicationDTO.getLeaderJobTitle());
    Leader leader;
    if (leaders.isEmpty()) {
      leader = new Leader(applicationDTO.getLeaderName(), applicationDTO.getLeaderSurname(),
          applicationDTO.getLeaderPatronymic(), applicationDTO.getLeaderJobTitle());
      leaderRepository.save(leader);
    } else {
      leader = leaders.get(0);
    }
    Employee employee = new Employee(applicationDTO.getEmployeeName(),
        applicationDTO.getEmployeeSurname(),
        applicationDTO.getEmployeePatronymic(), applicationDTO.getEmployeeJobTitle(),
        applicationDTO.getProject(), applicationDTO.getTrainingPurpose(), leader);
    employee.setStartTime(applicationDTO.getDate());
    employeeRepository.save(employee);
    return employee.getId();
  }

  @Override
  public void confirmTraining(Long employeeId, LocalDate date)
      throws TimeUpException, DifferentStageException {
    Employee employee = employeeService.findEmployeeByID(employeeId);
    checkTime(employee, date, ENTRY_TEST_TIME_LIMIT, employee.getStage());
    // Проверка стейджа по сути есть уже на чек тайме
    if (!employee.getStage().equals(Stage.WAITING_APPLICATION_TRAINING)) {
      throw new DifferentStageException(
          "The employee is at a different stage. Current stage: " + employee.getStage());
    }
    employee.setStage(Stage.PASSES_ENTRANCE_TEST);
    employee.setStartTime(date);
    employeeRepository.save(employee);
  }

  @Override
  public void refuseTraining(Long employeeID, String reason, LocalDate date)
      throws DifferentStageException, TimeUpException {
    Employee employee = employeeService.findEmployeeByID(employeeID);
    if (!employee.getStage().equals(Stage.WAITING_APPLICATION_TRAINING)) {
      throw new DifferentStageException(
          "The employee is at a different stage. Current stage: " + employee.getStage());
    }
    checkTime(employee, date, ENTRY_TEST_TIME_LIMIT, employee.getStage());
    employee.setActive(false);
    employee.setReasonForRefuseTraining(reason);
    employee.setStage(Stage.REFUSAL_APPLICATION);
    employeeRepository.save(employee);
  }

  @Override
  public boolean takeEntryTest(Long employeeID, TestDTO testDTO)
          throws TimeUpException, DifferentStageException, TestTypeException {
    Employee employee = employeeService.findEmployeeByID(employeeID);
    if (!employee.getStage().equals(Stage.PASSES_ENTRANCE_TEST)) {
      throw new DifferentStageException("Employee can't pass this test");
    }
    if (!testDTO.getTestType().equals(TestType.ENTRY)) {
      throw new TestTypeException("Test type isn't ENTRY!");
    }
    checkTime(employee,  testDTO.getDate(),
            ENTRY_TEST_TIME_LIMIT, employee.getStage());
    Test test = new Test(employee, testDTO.getTestType(), testDTO.getScore() / 20,
            testDTO.getDate());
    if (test.getScorePercent() < 0.8) {
      employee.setStage(Stage.FAILED_ENTRANCE_TEST);
    } else {
      employee.setStage(Stage.STUDYING);
      employee.setStartTime(testDTO.getDate());
    }
    employeeRepository.save(employee);
    testRepository.save(test);
    return test.getScorePercent() >= 0.8;
  }

  private void checkTime(Employee employee, LocalDate curDate,
                         long timeDif, Stage stage)
          throws TimeUpException, DifferentStageException {
    long days = ChronoUnit.DAYS.between(curDate, employee.getStartTime());
    if (days > timeDif) {
      employee.setActive(false);
      employeeRepository.save(employee);
      switch (stage) {
        case  WAITING_APPLICATION_TRAINING -> {
          employee.setStage(Stage.REFUSAL_APPLICATION);
          throw new TimeUpException("It is possible to answer application in " + INVITATION_TIME_LIMIT
                  + " days," + " but " + days + " days were passed");
        }
        case PASSES_ENTRANCE_TEST -> {
          employee.setStage(Stage.FAILED_ENTRANCE_TEST);
          throw new TimeUpException("It is possible to pass entrance test in " + ENTRY_TEST_TIME_LIMIT
                  + " days," + " but " + days + " days were passed");
        }
        case STUDYING -> {
          employee.setStage(Stage.FAILED_STUDYING);
          throw new TimeUpException("It is possible to study in " + STUDY_TIME_LIMIT
                  + " days," + " but " + days + " days were passed");
        }
        case EXAM -> {
          employee.setStage(Stage.FAILED_EXAM);
          throw new TimeUpException("It is possible to pass exam in " + EXAM_TIME_LIMIT
                  + " days," + " but " + days + " days were passed");
        }
        default -> {
          throw new DifferentStageException("There is no such stage!");
        }
      }
    }
  }

  private void checkInvitationTime(Employee employee, LocalDate currentDate)
      throws TimeUpException {
    long timePassed = ChronoUnit.DAYS.between(employee.getStartTime(), currentDate);
    if (timePassed > INVITATION_TIME_LIMIT) {
      employee.setStage(Stage.REFUSAL_APPLICATION);
      employee.setActive(false);
      employeeRepository.save(employee);
      throw new TimeUpException("It is possible to answer application in " + INVITATION_TIME_LIMIT
              + " days," + " but " + timePassed + " days were passed");
    }
  }

  @Override
  public boolean takeModuleTest(Long employeeId, TestDTO moduleTest) throws TimeUpException, DifferentStageException, TestTypeException {
    Employee employee = employeeService.findEmployeeByID(employeeId);
    if (!employee.getStage().equals(Stage.STUDYING)) {
      throw new DifferentStageException("Employee can't pass this test");
    }
    if (isModuleTest(moduleTest.getTestType())) {
      throw new TestTypeException("Test type isn't MODULE_TEST!");
    }
    // добавить проверку на время <--------------------------HERE------------------------------
    Test test = new Test(employee, moduleTest.getTestType(), moduleTest.getScore() / 20,
            moduleTest.getDate());
    testRepository.save(test);
    return test.getScorePercent() >= 0.8;
  }

  @Override
  public boolean takePracticeTask(Long employeeId, TestDTO practiceTask) throws TimeUpException, DifferentStageException, TestTypeException {
    Employee employee = employeeService.findEmployeeByID(employeeId);
    if (!employee.getStage().equals(Stage.STUDYING)) {
      throw new DifferentStageException("Employee can't pass this test");
    }
    if (isPracticeTaskTest(practiceTask.getTestType())) {
      throw new TestTypeException("Test type isn't PRACTICE_TASK!");
    }
    // добавить проверку на время <--------------------------HERE------------------------------
    Test test = new Test(employee, practiceTask.getTestType(), practiceTask.getScore() / 20,
            practiceTask.getDate());
    testRepository.save(test);
    if (checkProgress(employee)) {
      employee.setStage(Stage.EXPECTS_PRODUCTION_PRACTICE);
      employeeRepository.save(employee);
    }
    return test.getScorePercent() >= 0.8;
  }

  private boolean isModuleTest(TestType testType) {
    return switch (testType) {
      case ENTRY, PRACTICE_TASK_1, PRACTICE_TASK_2 -> false;
      case MODULE_1, MODULE_2, MODULE_3, MODULE_4, MODULE_5, MODULE_6, MODULE_7, MODULE_8 -> true;
    };
  }

  private boolean isPracticeTaskTest(TestType testType) {
    return switch (testType) {
      case ENTRY, MODULE_1, MODULE_2, MODULE_3, MODULE_4, MODULE_5, MODULE_6, MODULE_7, MODULE_8 -> false;
      case PRACTICE_TASK_1, PRACTICE_TASK_2 -> true;
    };
  }

  private void checkEntryTestTime(Employee employee, LocalDate date) throws TimeUpException {
    long timePassed = ChronoUnit.DAYS.between(employee.getStartTime(), date);
    if (timePassed > ENTRY_TEST_TIME_LIMIT) {
      employee.setStage(Stage.FAILED_ENTRANCE_TEST);
      employee.setActive(false);
      employeeRepository.save(employee);
      throw new TimeUpException("It is possible to pass test in " + ENTRY_TEST_TIME_LIMIT
          + " days," + " but " + timePassed + " days were passed");
    }
  }

  public boolean checkProgress(Employee employee) {
    throw new NotImplementedException("Not implemented");
  }
}

