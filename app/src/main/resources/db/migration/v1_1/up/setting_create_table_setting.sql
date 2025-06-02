CREATE TABLE setting (
  setting_name VARCHAR(100) NOT NULL,
  setting_index INTEGER NOT NULL,
  setting_value VARCHAR(65000) NOT NULL,
  PRIMARY KEY(setting_name, setting_index)
);