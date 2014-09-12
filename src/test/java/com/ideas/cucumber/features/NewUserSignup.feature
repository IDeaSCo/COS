Feature: New User SignUp

	Scenario Outline: New User with single exact matching address and general shift time

		Given: Employee is enrolling for the first time and has entered all details
		When: He views dashboard
		Then: He should see default shift timing of "9:30-6:30" prefilled for the entire month
		