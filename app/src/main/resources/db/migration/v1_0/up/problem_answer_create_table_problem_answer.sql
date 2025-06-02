CREATE TABLE problem_answer (
  answer_id BIGINT NOT NULL IDENTITY,
  problem_id BIGINT NOT NULL,
  answer_value VARCHAR(65000) DEFAULT '' NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
  FOREIGN KEY(problem_id) REFERENCES problem(problem_id)
);