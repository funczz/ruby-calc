CREATE TABLE problem_element (
  problem_id BIGINT NOT NULL,
  element_index INTEGER NOT NULL,
  element_value VARCHAR(1000) NOT NULL,
  PRIMARY KEY(problem_id, element_index),
  FOREIGN KEY(problem_id) REFERENCES problem(problem_id)
);