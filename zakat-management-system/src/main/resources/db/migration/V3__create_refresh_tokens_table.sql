CREATE TABLE refresh_tokens (
    token         VARCHAR(512)         NOT NULL PRIMARY KEY,
    issued_at     TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expiry        TIMESTAMP    NOT NULL,
    revoked       BOOLEAN      NOT NULL DEFAULT FALSE,
    user_id       BIGINT       NOT NULL,

    CONSTRAINT fk_refresh_token_user FOREIGN KEY (user_id) REFERENCES users(id)
);
