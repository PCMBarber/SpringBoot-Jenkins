package com.qa.tdl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.qa.tdl.persistence.repos.AssigneeRepo;
import com.qa.tdl.persistence.repos.TaskRepo;
import com.qa.tdl.pom.TdlSitePortal;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

@SpringBootTest
class UserAcceptanceTest {

	private static final String URL = "http://localhost:8080";
	private static WebDriver driver;
	private static ExtentReports report;
	private static ExtentTest test;
	private static TdlSitePortal page;
	
	private TaskRepo taskRepo;
	private AssigneeRepo assigneeRepo;
	
	@Autowired
	UserAcceptanceTest(TaskRepo taskRepo, AssigneeRepo assigneeRepo) {
		this.taskRepo = taskRepo;
		this.assigneeRepo = assigneeRepo;
	}

	@BeforeAll
	static void setup() {
		report = new ExtentReports("target/reports/TdlSiteReport.html", true);
		System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");

		driver = new ChromeDriver();
		driver.manage().window().setSize(new Dimension(1366, 768));
		
		page = PageFactory.initElements(driver, TdlSitePortal.class);
		
		driver.get(URL);
	}
	
	@Test
	void newTaskTest() {
		test = report.startTest("New Task");
		
		String result = page.newTask();
		
		if (result.isEmpty() || !("Go shopping".equals(result))) {
			test.log(LogStatus.FAIL, "Test failed; new task doesn't show on interface");
		} else {
			test.log(LogStatus.PASS, "Test passed; new task shows on interface");
		}

		Assertions.assertThat(result).isEqualTo("Go shopping");
	}
	
	@Test
	void updateTaskTest() throws InterruptedException {
		test = report.startTest("Update Task");
		
		String result = page.updateTask();
		
		if (result.isEmpty() || !("Fix table".equals(result))) {
			test.log(LogStatus.FAIL, "Test failed; edited task doesn't show on interface");
		} else {
			test.log(LogStatus.PASS, "Test passed; edited task shows on interface");
		}

		Assertions.assertThat(result).isEqualTo("Fix table");
	}
	
	@Test
	void readAllTest() {
		test = report.startTest("Read All Task");
		
		int result = page.readAllTask();
		int actual = (int) taskRepo.count();
		
		if (result != actual) {
			test.log(LogStatus.FAIL, "Test failed; incorrect number of tasks being shown");
		} else {
			test.log(LogStatus.PASS, "Test passed; correct number of tasks being shown");
		}
		
		Assertions.assertThat(result).isEqualTo(actual);
	}
	
	@Test
	void deleteTaskTest() {
		test = report.startTest("Delete Task");
		
		int result = page.deleteTask();
		int actual = (int) taskRepo.count();
		
		if (result != actual) {
			test.log(LogStatus.FAIL, "Test failed; incorrect number of tasks visible after deletion");
		} else {
			test.log(LogStatus.PASS, "Test passed; correct number of tasks visible after deletion");
		}
		
		Assertions.assertThat(result).isEqualTo(actual);
	}
	
	@Test
	void markAsCompletedTest() {
		test = report.startTest("Mark as Completed Task");
		
		boolean result = page.markAsCompleted();
		
		if (!result) {
			test.log(LogStatus.FAIL, "Test failed; task not marked as completed as it should be");
		} else {
			test.log(LogStatus.PASS, "Test passed; task marked as completed");
		}
		
		Assertions.assertThat(result).isTrue();
	}
	
	@Test
	void addAssigneeToTaskTest() {
		test = report.startTest("Add Assignee to Task");
		
		boolean result = page.addAssigneeToTask();
		
		if (!result) {
			test.log(LogStatus.FAIL, "Test failed; added assignee not visible on task");
		} else {
			test.log(LogStatus.PASS, "Test passed; added assignee visible on task");
		}
		
		Assertions.assertThat(result).isTrue();
	}
	
	@Test
	void removeAssigneeFromTaskTest() {
		test = report.startTest("Remove Assignee from Task");
		
		boolean result = page.removeAssigneeFromTask();
		
		if (!result) {
			test.log(LogStatus.FAIL, "Test failed; removed assignee still visible on task");
		} else {
			test.log(LogStatus.PASS, "Test passed; removed assignee not visible on task");
		}
		
		Assertions.assertThat(result).isTrue();
	}
	
	@Test
	void newAssigneeTest() {
		test = report.startTest("New Assignee");
		
		boolean result = page.newAssignee();
		
		if (!result) {
			test.log(LogStatus.FAIL, "Test failed; new assignee doesn't show on interface");
		} else {
			test.log(LogStatus.PASS, "Test passed; new assignee shows on interface");
		}

		Assertions.assertThat(result).isTrue();
	}
	
	@Test
	void updateAssigneeTest() {
		test = report.startTest("Update Assignee");
		
		boolean result = page.updateAssignee();
		
		if (!result) {
			test.log(LogStatus.FAIL, "Test failed; updated assignee doesn't show on interface");
		} else {
			test.log(LogStatus.PASS, "Test passed; updated assignee shows on interface");
		}

		Assertions.assertThat(result).isTrue();
	}
	
	@Test
	void readAllAssigneeTest() {
		test = report.startTest("Read All Assignees");
		
		int result = page.readAllAssignee();
		int actual = (int) assigneeRepo.count();
		
		if (result != actual) {
			test.log(LogStatus.FAIL, "Test failed; incorrect number of assignees being shown");
		} else {
			test.log(LogStatus.PASS, "Test passed; correct number of assignees being shown");
		}
		
		Assertions.assertThat(result).isEqualTo(actual);
	}
	
	@Test
	void deleteAssigneeTest() {
		test = report.startTest("Delete Assignee");
		
		boolean result = page.deleteAssignee();
		
		if (result) {
			test.log(LogStatus.FAIL, "Test failed; deleted assignee still visible after deletion");
		} else {
			test.log(LogStatus.PASS, "Test passed; deleted assignee not visible after deletion");
		}
		
		Assertions.assertThat(result).isFalse();
	}

	@AfterEach
	void close() {
		report.endTest(test);
	}

	@AfterAll
	static void tearDown() {
		driver.quit();
		report.flush();
		report.close();
	}
}
