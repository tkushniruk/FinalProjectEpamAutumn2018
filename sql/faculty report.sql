SELECT
        `facultyId`,
            `university_admission`.`User`.`first_name`,
            `university_admission`.`User`.`last_name`,
            `university_admission`.`User`.`email`,
            `university_admission`.`Entrant`.`isBlocked`,
            `preliminary_sum`,
            `diploma_sum`,
            `preliminary_sum` + `diploma_sum` AS `total_sum`
    FROM
        (SELECT
        `university_admission`.`Faculty_Entrants`.`Faculty_idFaculty` AS `facultyId`,
            `university_admission`.`Mark`.`Entrant_idEntrant` AS `entrantId`,
            SUM(CASE `exam_type`
                WHEN 'preliminary' THEN `university_admission`.`Mark`.`value`
                ELSE 0
            END) AS `preliminary_sum`,
            SUM(CASE `exam_type`
                WHEN 'diploma' THEN `university_admission`.`Mark`.`value`
                ELSE 0
            END) AS `diploma_sum`
    FROM
        `university_admission`.`Faculty_Entrants`
    INNER JOIN `university_admission`.`Mark` ON `university_admission`.`Faculty_Entrants`.`Entrant_idEntrant` = `university_admission`.`Mark`.`Entrant_idEntrant`
    GROUP BY `entrantId`) AS `entrant_marks_sum`
    INNER JOIN `university_admission`.`Faculty` ON `entrant_marks_sum`.`entrantId` = `university_admission`.`Faculty`.`id`
    INNER JOIN `university_admission`.`Entrant` ON `entrantId` = `university_admission`.`Entrant`.`id`
    INNER JOIN `university_admission`.`User` ON `university_admission`.`Entrant`.`User_idUser` = `university_admission`.`User`.`id`
    WHERE
        `facultyId` = 6
    ORDER BY `isBlocked` ASC , `total_sum` DESC;