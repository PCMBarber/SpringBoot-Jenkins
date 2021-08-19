package com.qa.tdl.pom.modal;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AddAssigneeModal {

	private WebDriverWait webDriverWait;
	private WebDriver driver;
	private WebElement targ;

	public AddAssigneeModal(WebDriver driver) {
		super();
		webDriverWait = new WebDriverWait(driver, 3);
		this.driver = driver;
	}
	
	public void addAssigneeToTask() {
		// add assignee select
		Select addAssigneeSelect = new Select(
				webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".show #addAssigneeToTaskSelect"))));
		addAssigneeSelect.selectByVisibleText("Paul");
		// add assignee save button
		targ = driver.findElement(By.id("addAssigneeToTaskSubmit"));
		targ.click();
	}
}
