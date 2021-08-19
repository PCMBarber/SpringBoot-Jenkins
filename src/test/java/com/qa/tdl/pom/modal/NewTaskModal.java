package com.qa.tdl.pom.modal;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class NewTaskModal {
	
	private WebDriverWait webDriverWait;
	private WebDriver driver;
	private WebElement targ;

	public NewTaskModal(WebDriver driver) {
		super();
		webDriverWait = new WebDriverWait(driver, 3);
		this.driver = driver;
	}
	
	public void newTask() {
		// new task title input
		targ = webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".show #newTaskTitle")));
		targ.sendKeys("Go shopping");
		// new task save button
		targ = driver.findElement(By.xpath("//*[@id=\"newTaskModal\"]/div/div/div[3]/button[2]"));
		targ.click();
	}
	
	public void closeNewTaskModal() {
		// new task modal close button
		targ = webDriverWait.until(ExpectedConditions.presenceOfElementLocated(
				By.xpath("//*[@id=\"newTaskModal\"]/div/div/div[3]/button[1]")));
		targ.click();
	}

}
