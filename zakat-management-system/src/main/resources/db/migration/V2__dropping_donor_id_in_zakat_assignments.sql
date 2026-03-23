ALTER TABLE zakat_assignments
    DROP CONSTRAINT fk_assignments_donation;

ALTER TABLE zakat_assignments
    DROP COLUMN donation_id;