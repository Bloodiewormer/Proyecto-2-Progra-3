CREATE TABLE messages (
                          id BIGINT PRIMARY KEY AUTO_INCREMENT,
                          sender_id BIGINT NOT NULL,
                          recipient_id BIGINT NOT NULL,
                          message TEXT NOT NULL,
                          status VARCHAR(20) DEFAULT 'SENT',
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          read_at TIMESTAMP NULL,
                          FOREIGN KEY (sender_id) REFERENCES usuarios(id) ON DELETE CASCADE,
                          FOREIGN KEY (recipient_id) REFERENCES usuarios(id) ON DELETE CASCADE,
                          INDEX idx_sender (sender_id),
                          INDEX idx_recipient (recipient_id),
                          INDEX idx_status (status),
                          INDEX idx_created_at (created_at)
);