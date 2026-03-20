CREATE TABLE users (
                       id         BIGINT AUTO_INCREMENT PRIMARY KEY,
                       name       VARCHAR(100)  NOT NULL,
                       email      VARCHAR(150)  NOT NULL UNIQUE,
                       password   VARCHAR(255)  NOT NULL,
                       role       VARCHAR(20)   NOT NULL CHECK (role IN ('DONOR', 'BENEFICIARY', 'ADMIN')),
                       created_at TIMESTAMP     DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE donations (
                           id         BIGINT AUTO_INCREMENT PRIMARY KEY,
                           donor_id   BIGINT        NOT NULL,
                           amount     DECIMAL(10,2) NOT NULL,
                           donated_at TIMESTAMP     DEFAULT CURRENT_TIMESTAMP,

                           CONSTRAINT fk_donations_donor FOREIGN KEY (donor_id) REFERENCES users(id)
);
CREATE TABLE beneficiaries (
                               id             BIGINT AUTO_INCREMENT PRIMARY KEY,
                               user_id        BIGINT       NOT NULL UNIQUE,
                               full_name      VARCHAR(100) NOT NULL,
                               priority_score INT          NOT NULL DEFAULT 0,
                               is_emergency   BOOLEAN      NOT NULL DEFAULT FALSE,
                               registered_at  TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,

                               CONSTRAINT fk_beneficiaries_user FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE eligibility_checks (
                                    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
                                    beneficiary_id      BIGINT        NOT NULL UNIQUE,
                                    age                 SMALLINT      NOT NULL CHECK(age>0),
                                    monthly_income      DECIMAL(10,2) NOT NULL,
                                    dependents          INT           NOT NULL,
                                    reason              TEXT          NOT NULL,
                                    has_debt            BOOLEAN       NOT NULL,
                                    has_disability      BOOLEAN       NOT NULL,
                                    is_unemployed       BOOLEAN       NOT NULL,
                                    has_chronic_illness BOOLEAN       NOT NULL,
                                    is_orphan           BOOLEAN       NOT NULL,
                                    is_eligible         BOOLEAN       NOT NULL,
                                    rejection_reason    TEXT,
                                    checked_at          TIMESTAMP     DEFAULT CURRENT_TIMESTAMP,

                                    CONSTRAINT fk_eligibility_beneficiary FOREIGN KEY (beneficiary_id) REFERENCES beneficiaries(id)
);

CREATE TABLE zakat_assignments (
                                   id               BIGINT        AUTO_INCREMENT PRIMARY KEY,
                                   beneficiary_id   BIGINT        NOT NULL,
                                   donation_id      BIGINT        NOT NULL,
                                   amount_assigned  DECIMAL(10,2) NOT NULL,
                                   assigned_at      TIMESTAMP     DEFAULT CURRENT_TIMESTAMP,

                                   CONSTRAINT fk_assignments_beneficiary FOREIGN KEY (beneficiary_id) REFERENCES beneficiaries(id),
                                   CONSTRAINT fk_assignments_donation    FOREIGN KEY (donation_id)    REFERENCES donations(id)
);

CREATE TABLE distribution_history (
                                      id                BIGINT        AUTO_INCREMENT PRIMARY KEY,
                                      assignment_id     BIGINT        NOT NULL,
                                      beneficiary_name  VARCHAR(100)  NOT NULL,
                                      amount_received   DECIMAL(10,2) NOT NULL,
                                      distribution_date DATE          NOT NULL,

                                      CONSTRAINT fk_history_assignment FOREIGN KEY (assignment_id) REFERENCES zakat_assignments(id)
);

CREATE TABLE receipts (
                          id             BIGINT        AUTO_INCREMENT PRIMARY KEY,
                          donation_id    BIGINT        NOT NULL,
                          receipt_number VARCHAR(50)   NOT NULL UNIQUE,
                          donor_name     VARCHAR(100)  NOT NULL,
                          amount         DECIMAL(10,2) NOT NULL,
                          issued_at      TIMESTAMP     DEFAULT CURRENT_TIMESTAMP,

                          CONSTRAINT fk_receipts_donation FOREIGN KEY (donation_id) REFERENCES donations(id)
);