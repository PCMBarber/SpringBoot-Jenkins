package com.qa.tdl.pom;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.qa.tdl.pom.modal.AddAssigneeModal;
import com.qa.tdl.pom.modal.EditAssigneeModal;
import com.qa.tdl.pom.modal.EditTaskModal;
import com.qa.tdl.pom.modal.NewAssigneeModal;
import com.qa.tdl.pom.modal.NewTaskModal;

public class TdlSitePortal {

	@FindBy(id = "refreshButton")
	private WebElement refreshButton;

	private static WebElement targ;

	private NewTaskModal newTaskModal;
	private NewAssigneeModal newAssigneeModal;
	private EditAssigneeModal editAssigneeModal;
	private EditTaskModal editTaskModal;
	private AddAssigneeModal addAssigneeModal;

	private WebDriverWait webDriverWait;
	private WebDriver driver;

	public TdlSitePortal(WebDriver driver) {
		super();
		newTaskModal = PageFactory.initElements(driver, NewTaskModal.class);
		newAssigneeModal = PageFactory.initElements(driver, NewAssigneeModal.class);
		editAssigneeModal = PageFactory.initElements(driver, EditAssigneeModal.class);
		editTaskModal = PageFactory.initElements(driver, EditTaskModal.class);
		addAssigneeModal = PageFactory.initElements(driver, AddAssigneeModal.class);

		this.driver = driver;
		webDriverWait = new WebDriverWait(driver, 5);
	}

	public String newTask() {
		// new task button
		targ = driver.findElement(By.xpath("/html/body/div/div/div[1]/div/div[1]/button"));
		targ.click();
		this.newTaskModal.newTask();

		// new task title to appear in accordion div
		targ = webDriverWait.until(ExpectedConditions
				.presenceOfElementLocated(By.cssSelector("body[class=\"\"] #flush-heading4 .task-title")));
		String result = targ.getText();

		return result;
	}

	public String updateTask() {
		// task to be edited button (2nd task on accordion)
		targ = webDriverWait
				.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"flush-heading1\"]/button")));
		targ.click();
		// task to be edited expanded (2nd task)
		targ = webDriverWait
				.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#flush-collapse1.show")));
		// edit task button
		targ = driver.findElement(By.xpath("//*[@id=\"flush-collapse1\"]/div/div/button[1]"));
		targ.click();
		this.editTaskModal.editTask();

		// title of edited task
		targ = webDriverWait.until(ExpectedConditions
				.presenceOfElementLocated(By.cssSelector("body[class=\"\"] #flush-heading1 .task-title")));
		String result = targ.getText();

		return result;
	}

	public int readAllTask() {
		webDriverWait.
			until(ExpectedConditions.textToBe(By.cssSelector("#flush-heading0 > button > div > span.task-title"), "Do laundry"));
		
		// accordion items
		List<WebElement> allTaskElements = driver.findElements(By.cssSelector(".accordion-item"));

		int result = allTaskElements.size();

		return result;
	}

	public int deleteTask() {
		// task to be deleted button (2nd task in accordion)
		targ = webDriverWait
				.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"flush-heading1\"]/button")));
		targ.click();
		// task to be deleted expanded (2nd task)
		targ = webDriverWait
				.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#flush-collapse1.show")));
		// delete button
		targ = driver.findElement(By.xpath("//*[@id=\"flush-collapse1\"]/div/div/button[4]"));
		targ.click();

		this.refresh();
		// accordion items
		List<WebElement> allTaskElements = webDriverWait
				.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector(".accordion-item")));

		int result = allTaskElements.size();

		return result;
	}

	public boolean markAsCompleted() {
		// task to be marked as completed (4th task in accordion)
		targ = webDriverWait
				.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"flush-heading3\"]/button")));
		targ.click();
		// task to be marked as completed expanded (4th task)
		targ = webDriverWait
				.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#flush-collapse3.show")));
		// mark as complete button
		targ = driver.findElement(By.xpath("//*[@id=\"flush-collapse3\"]/div/div/button[3]"));
		targ.click();

		// text that should be crossed out
		targ = webDriverWait.until(ExpectedConditions
				.presenceOfElementLocated(By.xpath("//*[@id=\"flush-heading3\"]/button/div/span[1]/span")));

		return targ.getAttribute("class").contains("text-decoration-line-through");
	}

	public boolean addAssigneeToTask() {
		// task to add assignee to (4th task in accordion)
		targ = webDriverWait
				.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"flush-heading3\"]/button")));
		targ.click();
		// task to add assignee to expanded (4th task)
		targ = webDriverWait
				.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#flush-collapse3.show")));
		// add assignee button
		targ = driver.findElement(By.xpath("//*[@id=\"flush-collapse3\"]/div/div/button[2]"));
		targ.click();

		this.addAssigneeModal.addAssigneeToTask();

		// text that should have new assignee
		targ = webDriverWait.until(ExpectedConditions
				.presenceOfElementLocated(By.cssSelector("body[class=\"\"] #flush-heading3 .assigned-to")));

		return targ.getText().contains("Paul");
	}

	public boolean removeAssigneeFromTask() {
		// task to remove assignee from (4th task in accordion)
		targ = webDriverWait
				.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"flush-heading1\"]/button")));
		targ.click();
		// task to remove assignee from expanded (4th task)
		targ = webDriverWait
				.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#flush-collapse1.show")));
		// edit task button
		targ = driver.findElement(By.xpath("//*[@id=\"flush-collapse1\"]/div/div/button[1]"));
		targ.click();

		this.editTaskModal.removeAssigneeFromTask();

		// text that should have new assignee
		targ = webDriverWait.until(ExpectedConditions
				.presenceOfElementLocated(By.cssSelector("body[class=\"\"] #flush-heading1 .assigned-to")));

		return !(targ.getText().contains("Bob"));
	}

	public boolean newAssignee() {
		// new assignee button
		targ = driver.findElement(By.xpath("/html/body/div/div/div[1]/div/div[2]/button"));
		targ.click();
		this.newAssigneeModal.newAssignee();

		// edit assignee button
		targ = webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(
				"body[class=\"\"] > div > div > div.col-lg-4.d-flex.flex-column.justify-content-center.titleColumn > div > div:nth-child(3) > button")));
		targ.click();
		
		// confirm new assignee ("Drake") appears on select
		return this.editAssigneeModal.isAssigneeOnSelect("Drake");
	}
	
	public boolean updateAssignee() {
		// edit assignee button
		targ = driver.findElement(By.cssSelector(
				"body > div > div > div.col-lg-4.d-flex.flex-column.justify-content-center.titleColumn > div > div:nth-child(3) > button"));
		targ.click();
		this.editAssigneeModal.updateAssignee();

		// edit assignee button
		targ = webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(
				"body[class=\"\"] > div > div > div.col-lg-4.d-flex.flex-column.justify-content-center.titleColumn > div > div:nth-child(3) > button")));
		targ.click();
		
		// confirm new assignee ("Lee") appears on select
		return this.editAssigneeModal.isAssigneeOnSelect("Lee");
	}
	
	public int readAllAssignee() {
		// edit assignee button
		targ = driver.findElement(By.cssSelector(
				"body > div > div > div.col-lg-4.d-flex.flex-column.justify-content-center.titleColumn > div > div:nth-child(3) > button"));
		targ.click();
		int result = this.editAssigneeModal.assigneesOnSelectSize();

		return result;
	}
	
	public boolean deleteAssignee() {
		// edit assignee button
		targ = driver.findElement(By.cssSelector(
				"body > div > div > div.col-lg-4.d-flex.flex-column.justify-content-center.titleColumn > div > div:nth-child(3) > button"));
		targ.click();
		this.editAssigneeModal.deleteAssignee();
		
		// edit assignee button
		targ = webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(
				"body[class=\"\"] > div > div > div.col-lg-4.d-flex.flex-column.justify-content-center.titleColumn > div > div:nth-child(3) > button")));
		targ.click();
		
		// confirm deleted assignee ("Sally") appears on select
		return this.editAssigneeModal.isAssigneeOnSelect("Sally"); 
	}

	public void refresh() {
		this.refreshButton.click();
	}
}
