package com.qa.tdl.pom.modal;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class NewAssigneeModal {

	@FindBy(xpath = "//*[@id=\"newAssigneeModal\"]/div/div/div[3]/button[1]")
	private WebElement newAssigneeModalCloseButton;
	
	private WebDriverWait webDriverWait;
	private WebDriver driver;
	private WebElement targ;
	
	public NewAssigneeModal(WebDriver driver) {
		super();
		webDriverWait = new WebDriverWait(driver, 3);
		this.driver = driver;
	}
	
	public void newAssignee() {
		// new assignee name input
		targ = webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".show #newAssigneeName")));
		targ.sendKeys("Drake");
		// new assignee save button
		targ = driver.findElement(By.xpath("//*[@id=\"newAssigneeModal\"]/div/div/div[3]/button[2]"));
		targ.click();
	}
	
	public void closeNewAssigneeModal() {
		webDriverWait.until(ExpectedConditions.visibilityOf(newAssigneeModalCloseButton));
		this.newAssigneeModalCloseButton.click();
	}
}
