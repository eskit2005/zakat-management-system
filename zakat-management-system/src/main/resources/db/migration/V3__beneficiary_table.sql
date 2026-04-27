DROP TABLE IF EXISTS Beneficiary;

CREATE TABLE Beneficiary (
    id INT PRIMARY KEY,
    reason TEXT,
    dependents INT,
    income DECIMAL(12, 2),
    emergency BOOLEAN DEFAULT FALSE,
    disability BOOLEAN DEFAULT FALSE,
    age INT,
    is_orphan BOOLEAN DEFAULT FALSE,
    has_debt BOOLEAN DEFAULT FALSE,
    unemployed BOOLEAN DEFAULT FALSE,
    illness BOOLEAN DEFAULT FALSE,
    priority_score INT,
    eligible BOOLEAN DEFAULT FALSE,
    reject_reason TEXT,
    checked_at DATETIME,
    total_received_value DECIMAL(12, 2) DEFAULT 0,
    registered DATETIME,
    FOREIGN KEY (id) REFERENCES User(id) ON DELETE CASCADE
);