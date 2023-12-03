package ru.spbu.project.services;

import java.util.HashMap;
import org.springframework.stereotype.Service;
import ru.spbu.project.models.Employee;
import ru.spbu.project.models.Leader;
import ru.spbu.project.models.ProductionPractice;
import ru.spbu.project.models.Test;
import ru.spbu.project.models.dto.ProductionPracticeDTO;
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
import java.util.Optional;

import ru.spbu.project.repositories.ProductionPracticeRepository;
import ru.spbu.project.repositories.TestRepository;

@Service
public class TrainingServiceImpl implements TrainingService {

  final LeaderRepository leaderRepository;
  final EmployeeRepository employeeRepository;
  final EmployeeService employeeService;
  final TestRepository testRepository;
  final ProductionPracticeRepository productionPracticeRepository;
  static final int INVITATION_TIME_LIMIT = 3;
  static final int ENTRY_TEST_TIME_LIMIT = 5;
  static final int STUDY_TIME_LIMIT = 30;
  static final int EXAM_TIME_LIMIT = 1;

  public TrainingServiceImpl(LeaderRepository leaderRepository,
      EmployeeRepository employeeRepository, EmployeeService employeeService,
      TestRepository testRepository, ProductionPracticeRepository productionPracticeRepository) {
    this.leaderRepository = leaderRepository;
    this.employeeRepository = employeeRepository;
    this.employeeService = employeeService;
    this.testRepository = testRepository;
    this.productionPracticeRepository = productionPracticeRepository;
  }


  @Override
  public long applyForTraining(TrainingApplicationDTO applicationDTO) {
    Leader leader = leaderRepository.findById(applicationDTO.getLeaderId()).orElseThrow(
            () -> new IllegalArgumentException("There is no leader with id: " + applicationDTO.getLeaderId()));
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
    if (!employee.getStage().equals(Stage.WAITING_APPLICATION_TRAINING)) {
      throw new DifferentStageException(
          "The employee is at a different stage. Current stage: " + employee.getStage());
    }
    checkTime(employee, date, ENTRY_TEST_TIME_LIMIT, employee.getStage());
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

  private void checkActionInPast(LocalDate startDate, LocalDate curDate) throws TimeUpException {
    if (ChronoUnit.DAYS.between(startDate, curDate) < 0) {
      throw new TimeUpException("Action in the past");
    }
  }

  private void checkTime(Employee employee, LocalDate curDate,
                         long timeDif, Stage stage)
          throws TimeUpException, DifferentStageException {
    long days = ChronoUnit.DAYS.between(employee.getStartTime(), curDate);
    if (days < 0) {
      throw new TimeUpException("Action in the past");
    }
    if (days > timeDif) {
      employee.setActive(false);
      switch (stage) {
        case WAITING_APPLICATION_TRAINING -> {
          employee.setStage(Stage.REFUSAL_APPLICATION);
          employeeRepository.save(employee);
          throw new TimeUpException(
              "It is possible to answer application in " + INVITATION_TIME_LIMIT
                  + " days," + " but " + days + " days were passed");
        }
        case PASSES_ENTRANCE_TEST -> {
          employee.setStage(Stage.FAILED_ENTRANCE_TEST);
          employeeRepository.save(employee);
          throw new TimeUpException(
              "It is possible to pass entrance test in " + ENTRY_TEST_TIME_LIMIT
                  + " days," + " but " + days + " days were passed");
        }
        case STUDYING -> {
          employee.setStage(Stage.FAILED_STUDYING);
          employeeRepository.save(employee);
          throw new TimeUpException("It is possible to study in " + STUDY_TIME_LIMIT
              + " days," + " but " + days + " days were passed");
        }
        case EXAM -> {
          employee.setStage(Stage.FAILED_EXAM);
          employeeRepository.save(employee);
          throw new TimeUpException("It is possible to pass exam in " + EXAM_TIME_LIMIT
              + " days," + " but " + days + " days were passed");
        }
        default -> throw new DifferentStageException("There is no such stage!");
      }
    }
  }

  @Override
  public boolean takeModuleTest(Long employeeId, TestDTO moduleTest)
      throws TimeUpException, DifferentStageException, TestTypeException {
    Employee employee = employeeService.findEmployeeByID(employeeId);
    if (!employee.getStage().equals(Stage.STUDYING)) {
      throw new DifferentStageException("Employee can't pass this test");
    }
    if (!isModuleTest(moduleTest.getTestType())) {
      throw new TestTypeException("Test type isn't MODULE_TEST!");
    }
    checkTime(employee, moduleTest.getDate(), STUDY_TIME_LIMIT, employee.getStage());
    Test test = new Test(employee, moduleTest.getTestType(), moduleTest.getScore() / 20,
        moduleTest.getDate());
    testRepository.save(test);
    if (checkProgress(employee)) {
      employee.setStage(Stage.EXPECTS_PRODUCTION_PRACTICE);
      employeeRepository.save(employee);
    }
    return test.getScorePercent() >= 0.8;
  }

  @Override
  public boolean takePracticeTask(Long employeeId, TestDTO practiceTask)
      throws TimeUpException, DifferentStageException, TestTypeException {
    Employee employee = employeeService.findEmployeeByID(employeeId);
    if (!employee.getStage().equals(Stage.STUDYING)) {
      throw new DifferentStageException("Employee can't pass this test");
    }
    if (!isPracticeTaskTest(practiceTask.getTestType())) {
      throw new TestTypeException("Test type isn't PRACTICE_TASK!");
    }
    checkTime(employee, practiceTask.getDate(),
        STUDY_TIME_LIMIT, employee.getStage());
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
      case ENTRY, MODULE_1, MODULE_2, MODULE_3, MODULE_4, MODULE_5, MODULE_6, MODULE_7, MODULE_8 ->
          false;
      case PRACTICE_TASK_1, PRACTICE_TASK_2 -> true;
    };
  }

  public boolean checkProgress(Employee employee) {
    return (passedTest(employee, TestType.MODULE_1) &&
        passedTest(employee, TestType.MODULE_2) &&
        passedTest(employee, TestType.MODULE_3) &&
        passedTest(employee, TestType.MODULE_4) &&
        passedTest(employee, TestType.MODULE_5) &&
        passedTest(employee, TestType.MODULE_6) &&
        passedTest(employee, TestType.MODULE_7) &&
        passedTest(employee, TestType.MODULE_8) &&
        passedTest(employee, TestType.PRACTICE_TASK_1) &&
        passedTest(employee, TestType.PRACTICE_TASK_2));
  }

  private boolean passedTest(Employee employee, TestType testType) {
    List<Test> tests = testRepository.employeeFindTest(employee, testType);
    for (Test test : tests) {
      if (test.getScorePercent() >= 0.8) {
        return true;
      }
    }
    return false;
  }

  @Override
  public HashMap<Stage, Integer> getFromPeriod(LocalDate startTime, LocalDate endTime) {
    List<Employee> employees = employeeRepository.findAll();
    Integer studyingCounter = 0;
    Integer expectsProductionPracticeCounter = 0;
    Integer productionPracticeCounter = 0;
    Integer examCounter = 0;
    Integer failedCounter = 0;
    Integer passedCounter = 0;

    for (Employee employee : employees) {
      if (startTime.isBefore(employee.getStartTime()) && endTime.isAfter(employee.getStartTime())) {
        switch (employee.getStage()) {
          case STUDYING -> studyingCounter++;
          case EXPECTS_PRODUCTION_PRACTICE -> expectsProductionPracticeCounter++;
          case PRODUCTION_PRACTICE -> productionPracticeCounter++;
          case EXAM -> examCounter++;
          case FAILED_EXAM -> failedCounter++;
          case PASSED_EXAM -> passedCounter++;
        }
      }
    }

    HashMap<Stage, Integer> result = new HashMap<>();
    result.put(Stage.STUDYING, studyingCounter);
    result.put(Stage.EXPECTS_PRODUCTION_PRACTICE, expectsProductionPracticeCounter);
    result.put(Stage.PRODUCTION_PRACTICE, productionPracticeCounter);
    result.put(Stage.EXAM, examCounter);
    result.put(Stage.FAILED_EXAM, failedCounter);
    result.put(Stage.PASSED_EXAM, passedCounter);
    return result;
  }

  public void passingProductionPractice(ProductionPracticeDTO productionPracticeDTO)
          throws TimeUpException, DifferentStageException, IllegalArgumentException {
    Employee employee = employeeService.findEmployeeByID(productionPracticeDTO.getEmployeeId());
    if (!employee.getStage().equals(Stage.EXPECTS_PRODUCTION_PRACTICE)) {
      throw new DifferentStageException("Employee is not waiting for internship");
    }
    checkActionInPast(employee.getStartTime(), productionPracticeDTO.getDate());
    Optional<Leader> optLeader = leaderRepository.findById(productionPracticeDTO.getLeader().getId());
    if (optLeader.isEmpty()) {
      leaderRepository.save(productionPracticeDTO.getLeader());
    }
    ProductionPractice practice = new ProductionPractice(productionPracticeDTO.getLeader(),
            employee, productionPracticeDTO.getProject());
    productionPracticeRepository.save(practice);
    employee.setStage(Stage.PRODUCTION_PRACTICE);
    employeeRepository.save(employee);
  }

  public boolean productionPracticeResult(Long employeeId, Boolean result)
          throws IllegalArgumentException, TimeUpException, DifferentStageException {
    Employee employee = employeeService.findEmployeeByID(employeeId);
    if (!employee.getStage().equals(Stage.PRODUCTION_PRACTICE)) {
      throw new DifferentStageException("Employee is not on internship");
    }
    checkActionInPast(employee.getStartTime(), LocalDate.now());
    ProductionPractice practice = productionPracticeRepository.findProductionPracticeByEmployee(employee).
            orElseThrow(() -> new IllegalArgumentException("Chosen employee is not on internship"));
    practice.setResult(result);
    productionPracticeRepository.save(practice);
    if (!result) {
      employee.setStage(Stage.FAILED_PRODUCTION_PRACTICE);
      employee.setActive(false);
      employeeRepository.save(employee);
    }
    return result;
  }

  public void directionToTakeExam(Long employeeId)
          throws TimeUpException, DifferentStageException, IllegalArgumentException {
    Employee employee = employeeService.findEmployeeByID(employeeId);
    if (!employee.getStage().equals(Stage.PRODUCTION_PRACTICE)) {
      throw new DifferentStageException("Employee is not on internship");
    }
    ProductionPractice practice = productionPracticeRepository.findProductionPracticeByEmployee(employee).
            orElseThrow(() -> new IllegalArgumentException("Chosen employee is not on internship"));
    if (!practice.getResult()) {
      throw new IllegalArgumentException("Employee didn't pass internship");
    }
    LocalDate date = LocalDate.now();
    checkActionInPast(employee.getStartTime(), date);
    employee.setStartTime(date);
    employee.setStage(Stage.EXAM);
    employeeRepository.save(employee);
  }

  public boolean takeExam(Long employeeId, Boolean result)
      throws TimeUpException, DifferentStageException {
    Employee employee = employeeService.findEmployeeByID(employeeId);
    if (!employee.getStage().equals(Stage.EXAM)) {
      throw new DifferentStageException("Employee is not passing exam");
    }
    checkTime(employee, LocalDate.now(), EXAM_TIME_LIMIT, Stage.EXAM);
    employee.setExamResult(result);
    employeeRepository.save(employee);
    if (result) {
      employee.setStage(Stage.PASSED_EXAM);
    }
    return result;
  }
}

