CREATE OR REPLACE PROCEDURE find_employees_by_department(
    IN p_department_code VARCHAR(50)
)
LANGUAGE plpgsql
AS $$
BEGIN
    SELECT e.*
    FROM employee e
    JOIN department d ON e.department_id = d.id
    WHERE d.code = p_department_code;
END;
$$;

CREATE OR REPLACE PROCEDURE give_employee_raise(
    IN emp_id BIGINT,
    IN raise_amount DECIMAL(19,2)
)
LANGUAGE plpgsql
AS $$
BEGIN
    UPDATE employee
    SET salary = salary + raise_amount
    WHERE id = emp_id;
END;
$$;