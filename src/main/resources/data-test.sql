INSERT INTO task_domain(TITLE, COMPLETED, DATE_TIME_SET)
VALUES
('Do laundry', FALSE, '2021-02-05 08:00'),
('Make coffee', FALSE, '2021-01-21 13:00'),
('Take out bins', TRUE, '2020-12-30 19:00'),
('Buy masks', FALSE, '2021-02-01 03:30');

INSERT INTO assignee_domain(NAME)
VALUES
('Jane'),
('Bob'),
('Paul'),
('Sally');

INSERT INTO tasks_assignees(TASK_ID, ASSIGNEE_ID)
VALUES
(1, 1),
(2, 2),
(2, 3),
(3, 2);