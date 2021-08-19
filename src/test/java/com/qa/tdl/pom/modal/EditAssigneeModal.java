package com.qa.tdl.pom.modal;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class EditAssigneeModal {
	
	private WebDriverWait webDriverWait;
	private WebDriver driver;
	private WebElement targ;
	
	public EditAssigneeModal(WebDriver driver) {
		super();
		webDriverWait = new WebDriverWait(driver, 3);
		this.driver = driver;
	}
	
	public void closeEditAssigneeModal() {
		// edit assignee modal close button
		targ = webDriverWait.until(ExpectedConditions.presenceOfElementLocated(
				By.xpath("//*[@id=\"editAssigneeModal\"]/div/div/div[3]/button[1]")));
		targ.click();
	}
	
	public void updateAssignee() {
		// edit assignee select
		Select editAssigneeSelect = new Select(webDriverWait.until(ExpectedConditions.presenceOfElementLocated(
				By.cssSelector(".show #editAssigneeSelect"))));
		editAssigneeSelect.selectByVisibleText("Jane");
		// edit assignee new name input
		targ = driver.findElement(By.id("editAssigneeName"));
		targ.sendKeys("Lee");
		// edit assignee save button
		targ = driver.findElement(By.xpath("//*[@id=\"editAssigneeModal\"]/div/div/div[3]/button[2]"));
		targ.click();
		
	}
	
	public void deleteAssignee() {
		// edit assignee select
		Select editAssigneeSelect = new Select(webDriverWait.until(ExpectedConditions.presenceOfElementLocated(
				By.cssSelector(".show #editAssigneeSelect"))));
		editAssigneeSelect.selectByVisibleText("Sally");
		// delete assignee button
		targ = driver.findElement(By.xpath("//*[@id=\"editAssigneeModal\"]/div/div/div[3]/button[3]"));
		targ.click();
	}
	
	public boolean isAssigneeOnSelect(String name) {
		// edit assignee select
		Select editAssigneeSelect = new Select(webDriverWait.until(ExpectedConditions.presenceOfElementLocated(
				By.cssSelector(".show #editAssigneeSelect"))));
		boolean isPresent = false;
		
		for (WebElement option : editAssigneeSelect.getOptions()) {
			if (name.equals(option.getText())) {
				isPresent = true;
			}
		}
		
		this.closeEditAssigneeModal();
		return isPresent;
	}
	
	public int assigneesOnSelectSize() {
		// edit assignee select
		Select editAssigneeSelect = new Select(webDriverWait.until(ExpectedConditions.presenceOfElementLocated(
				By.cssSelector(".show #editAssigneeSelect"))));
		
		int size = editAssigneeSelect.getOptions().size();
		this.closeEditAssigneeModal();
		
		return size;
	}
}
