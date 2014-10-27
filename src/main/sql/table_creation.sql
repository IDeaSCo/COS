CREATE TABLE employee_info(
	username VARCHAR(10) PRIMARY KEY,
	NAME VARCHAR(50) NOT NULL,
	address VARCHAR(200) NOT NULL,
	latitude DOUBLE NOT NULL,
	longitude DOUBLE NOT NULL,
	mobile VARCHAR(10) NOT NULL
)

CREATE TABLE employee_dashboard (
username VARCHAR(10) NOT NULL,
travel_date DATE NOT NULL,
EVENT ENUM('In-Time', 'Out-Time') NOT NULL,
TIME TIME,
PRIMARY KEY(username, travel_date, EVENT))

DELIMITER $$
DROP PROCEDURE IF EXISTS fillDefaultTiming$$
CREATE PROCEDURE fillDefaultTiming (IN USERNAME VARCHAR(10), IN STARTDATE VARCHAR(12))
BEGIN
                
                SET @endDate = LAST_DAY(DATE_ADD( STARTDATE, INTERVAL 0 MONTH));
                SET  @dt = DATE_ADD(@dt, INTERVAL 1 DAY);
                SELECT @dt;
                SET @dt = STARTDATE;
                SELECT @dt;
                WHILE (@dt <= @endDate) DO
				IF WEEKDAY(@dt)!=5 AND WEEKDAY(@dt)!=6 THEN
                                INSERT INTO employee_dashboard(username, travel_date, EVENT, TIME) VALUES(USERNAME, @dt, 1, '09:30:00');
                                INSERT INTO employee_dashboard(username, travel_date, EVENT, TIME) VALUES(USERNAME, @dt, 2, '18:30:00');                                                                
                                END IF;
                                SET  @dt = DATE_ADD(@dt,INTERVAL 1 DAY);
                END WHILE;
END $$


--CALL fillDefaultTiming('idnsor', '2014-10-01');


SELECT * FROM employee_info;

SELECT * FROM employee_dashboard;

TRUNCATE TABLE employee_info;

TRUNCATE TABLE employee_dashboard;