-- Seed data: Jharkhand-based university partners for JanSetu.
-- Existing rows (ids 1-7) are updated in place to preserve any university_assignments
-- created against them; ids 8-17 are additional partner universities.

INSERT INTO universities (id, name, location, contact_email, contact_phone) VALUES
  (1, 'Birsa Agricultural University', 'Ranchi, Jharkhand', 'outreach@bau.ac.in', '+91-9000000001'),
  (2, 'BIT Mesra', 'Ranchi, Jharkhand', 'civic@bitmesra.ac.in', '+91-9000000002'),
  (3, 'NUSRL Ranchi', 'Ranchi, Jharkhand', 'registrar@nusrlranchi.ac.in', '+91-9000000003'),
  (4, 'Jharkhand University of Technology', 'Ranchi, Jharkhand', 'contact@jut.ac.in', '+91-9000000004'),
  (5, 'Sarla Birla University', 'Ranchi, Jharkhand', 'care@sarlabirla.edu.in', '+91-9000000005'),
  (6, 'Central University of Jharkhand', 'Ranchi, Jharkhand', 'dean@cuj.ac.in', '+91-9000000006'),
  (7, 'Nilamber-Pitamber University', 'Palamu, Jharkhand', 'connect@npu.ac.in', '+91-9000000007'),
  (8, 'IIT (ISM) Dhanbad', 'Dhanbad, Jharkhand', 'outreach@iitism.ac.in', '+91-9000000008'),
  (9, 'NIT Jamshedpur', 'Jamshedpur, Jharkhand', 'civic@nitjsr.ac.in', '+91-9000000009'),
  (10, 'XLRI Jamshedpur', 'Jamshedpur, Jharkhand', 'governance@xlri.ac.in', '+91-9000000010'),
  (11, 'Ranchi University', 'Ranchi, Jharkhand', 'info@ranchiuniversity.ac.in', '+91-9000000011'),
  (12, 'Vinoba Bhave University', 'Hazaribagh, Jharkhand', 'info@vbu.ac.in', '+91-9000000012'),
  (13, 'Sido Kanhu Murmu University', 'Dumka, Jharkhand', 'info@skmu.ac.in', '+91-9000000013'),
  (14, 'Kolhan University', 'Chaibasa, Jharkhand', 'info@kolhanuniversity.ac.in', '+91-9000000014'),
  (15, 'Dr. Shyama Prasad Mukherjee University', 'Ranchi, Jharkhand', 'info@dspmu.ac.in', '+91-9000000015'),
  (16, 'Amity University Jharkhand', 'Ranchi, Jharkhand', 'admissions@rnc.amity.edu', '+91-9000000016'),
  (17, 'Usha Martin University', 'Ranchi, Jharkhand', 'info@umu.ac.in', '+91-9000000017')
ON DUPLICATE KEY UPDATE
  name = VALUES(name),
  location = VALUES(location),
  contact_email = VALUES(contact_email),
  contact_phone = VALUES(contact_phone);

DELETE FROM university_disciplines WHERE university_id BETWEEN 1 AND 17;

INSERT INTO university_disciplines (university_id, discipline) VALUES
  (1, 'AGRICULTURE'),
  (1, 'RURAL_LIVELIHOOD'),
  (1, 'WATER'),
  (2, 'URBAN_INFRASTRUCTURE'),
  (2, 'ENVIRONMENT'),
  (3, 'PUBLIC_SERVICE'),
  (3, 'ACCESSIBILITY'),
  (4, 'URBAN_INFRASTRUCTURE'),
  (4, 'SANITATION'),
  (4, 'ENVIRONMENT'),
  (5, 'HEALTHCARE'),
  (5, 'EDUCATION'),
  (6, 'EDUCATION'),
  (6, 'PUBLIC_SERVICE'),
  (7, 'AGRICULTURE'),
  (7, 'RURAL_LIVELIHOOD'),
  (7, 'WATER'),
  (8, 'URBAN_INFRASTRUCTURE'),
  (8, 'ENVIRONMENT'),
  (8, 'WATER'),
  (9, 'URBAN_INFRASTRUCTURE'),
  (9, 'PUBLIC_SERVICE'),
  (10, 'PUBLIC_SERVICE'),
  (11, 'EDUCATION'),
  (11, 'PUBLIC_SERVICE'),
  (12, 'EDUCATION'),
  (12, 'HEALTHCARE'),
  (13, 'RURAL_LIVELIHOOD'),
  (13, 'ACCESSIBILITY'),
  (13, 'EDUCATION'),
  (14, 'EDUCATION'),
  (14, 'RURAL_LIVELIHOOD'),
  (15, 'EDUCATION'),
  (15, 'PUBLIC_SERVICE'),
  (16, 'EDUCATION'),
  (16, 'ACCESSIBILITY'),
  (17, 'URBAN_INFRASTRUCTURE'),
  (17, 'ENVIRONMENT');

-- Seed toy industry/CSR partners so the Industry dashboard has demo data to browse.
-- Login password for every seeded industry account: Industry@123
INSERT INTO industries (id, name, sector, contact_email, contact_phone) VALUES
  (2, 'GreenTech Industries', 'Clean Energy / Sanitation', 'contact@greentech.example.com', '+91-9000000099'),
  (3, 'Tata Steel Foundation', 'Steel & CSR', 'csr@tatasteel.example.com', '+91-9000000101'),
  (4, 'SAIL Bokaro Steel Plant', 'Steel & Manufacturing', 'outreach@sailbokaro.example.com', '+91-9000000102'),
  (5, 'Coal India Limited (CCL)', 'Mining & Energy', 'csr@coalindia.example.com', '+91-9000000103'),
  (6, 'JREDA', 'Clean & Renewable Energy', 'info@jreda.example.com', '+91-9000000104'),
  (7, 'Cognizant Jharkhand Innovation Hub', 'IT Services & Digital Solutions', 'hub@cognizant.example.com', '+91-9000000105')
ON DUPLICATE KEY UPDATE
  name = VALUES(name),
  sector = VALUES(sector),
  contact_email = VALUES(contact_email),
  contact_phone = VALUES(contact_phone);

-- Pre-approved login accounts, one admin per seeded university (no self-registration).
-- Login password for every seeded university account: University@123
INSERT INTO users (email, name, password_hash, phone, role, approved, university_id) VALUES
  ('bau.admin@example.com', 'Birsa Agricultural University Admin', '$2b$10$AVo/yU/q.y2R1QmJM2/Rh.2H4Y.QjvqHPtkiYj2qvNxKFnVKN8SxG', '+91-9100000001', 'UNIVERSITY_ADMIN', 1, 1),
  ('bitmesra.admin@example.com', 'BIT Mesra Admin', '$2b$10$AVo/yU/q.y2R1QmJM2/Rh.2H4Y.QjvqHPtkiYj2qvNxKFnVKN8SxG', '+91-9100000002', 'UNIVERSITY_ADMIN', 1, 2),
  ('nusrl.admin@example.com', 'NUSRL Ranchi Admin', '$2b$10$AVo/yU/q.y2R1QmJM2/Rh.2H4Y.QjvqHPtkiYj2qvNxKFnVKN8SxG', '+91-9100000003', 'UNIVERSITY_ADMIN', 1, 3),
  ('jut.admin@example.com', 'Jharkhand University of Technology Admin', '$2b$10$AVo/yU/q.y2R1QmJM2/Rh.2H4Y.QjvqHPtkiYj2qvNxKFnVKN8SxG', '+91-9100000004', 'UNIVERSITY_ADMIN', 1, 4),
  ('sarlabirla.admin@example.com', 'Sarla Birla University Admin', '$2b$10$AVo/yU/q.y2R1QmJM2/Rh.2H4Y.QjvqHPtkiYj2qvNxKFnVKN8SxG', '+91-9100000005', 'UNIVERSITY_ADMIN', 1, 5),
  ('cuj.admin@example.com', 'Central University of Jharkhand Admin', '$2b$10$AVo/yU/q.y2R1QmJM2/Rh.2H4Y.QjvqHPtkiYj2qvNxKFnVKN8SxG', '+91-9100000006', 'UNIVERSITY_ADMIN', 1, 6),
  ('npu.admin@example.com', 'Nilamber-Pitamber University Admin', '$2b$10$AVo/yU/q.y2R1QmJM2/Rh.2H4Y.QjvqHPtkiYj2qvNxKFnVKN8SxG', '+91-9100000007', 'UNIVERSITY_ADMIN', 1, 7),
  ('iitism.admin@example.com', 'IIT (ISM) Dhanbad Admin', '$2b$10$AVo/yU/q.y2R1QmJM2/Rh.2H4Y.QjvqHPtkiYj2qvNxKFnVKN8SxG', '+91-9100000008', 'UNIVERSITY_ADMIN', 1, 8),
  ('nitjsr.admin@example.com', 'NIT Jamshedpur Admin', '$2b$10$AVo/yU/q.y2R1QmJM2/Rh.2H4Y.QjvqHPtkiYj2qvNxKFnVKN8SxG', '+91-9100000009', 'UNIVERSITY_ADMIN', 1, 9),
  ('xlri.admin@example.com', 'XLRI Jamshedpur Admin', '$2b$10$AVo/yU/q.y2R1QmJM2/Rh.2H4Y.QjvqHPtkiYj2qvNxKFnVKN8SxG', '+91-9100000010', 'UNIVERSITY_ADMIN', 1, 10),
  ('ranchiuniversity.admin@example.com', 'Ranchi University Admin', '$2b$10$AVo/yU/q.y2R1QmJM2/Rh.2H4Y.QjvqHPtkiYj2qvNxKFnVKN8SxG', '+91-9100000011', 'UNIVERSITY_ADMIN', 1, 11),
  ('vbu.admin@example.com', 'Vinoba Bhave University Admin', '$2b$10$AVo/yU/q.y2R1QmJM2/Rh.2H4Y.QjvqHPtkiYj2qvNxKFnVKN8SxG', '+91-9100000012', 'UNIVERSITY_ADMIN', 1, 12),
  ('skmu.admin@example.com', 'Sido Kanhu Murmu University Admin', '$2b$10$AVo/yU/q.y2R1QmJM2/Rh.2H4Y.QjvqHPtkiYj2qvNxKFnVKN8SxG', '+91-9100000013', 'UNIVERSITY_ADMIN', 1, 13),
  ('kolhan.admin@example.com', 'Kolhan University Admin', '$2b$10$AVo/yU/q.y2R1QmJM2/Rh.2H4Y.QjvqHPtkiYj2qvNxKFnVKN8SxG', '+91-9100000014', 'UNIVERSITY_ADMIN', 1, 14),
  ('dspmu.admin@example.com', 'Dr. Shyama Prasad Mukherjee University Admin', '$2b$10$AVo/yU/q.y2R1QmJM2/Rh.2H4Y.QjvqHPtkiYj2qvNxKFnVKN8SxG', '+91-9100000015', 'UNIVERSITY_ADMIN', 1, 15),
  ('amity.admin@example.com', 'Amity University Jharkhand Admin', '$2b$10$AVo/yU/q.y2R1QmJM2/Rh.2H4Y.QjvqHPtkiYj2qvNxKFnVKN8SxG', '+91-9100000016', 'UNIVERSITY_ADMIN', 1, 16),
  ('umu.admin@example.com', 'Usha Martin University Admin', '$2b$10$AVo/yU/q.y2R1QmJM2/Rh.2H4Y.QjvqHPtkiYj2qvNxKFnVKN8SxG', '+91-9100000017', 'UNIVERSITY_ADMIN', 1, 17)
ON DUPLICATE KEY UPDATE
  name = VALUES(name),
  password_hash = VALUES(password_hash),
  phone = VALUES(phone),
  role = VALUES(role),
  approved = VALUES(approved),
  university_id = VALUES(university_id);

-- Pre-approved login accounts for the seeded toy industry partners.
-- Login password for every seeded industry account: Industry@123
INSERT INTO users (email, name, password_hash, phone, role, approved, industry_id) VALUES
  ('greentech@example.com', 'GreenTech Industries Admin', '$2b$10$V2olv2c8aL0ItOm6wlW1L.wnuIVS8AfQ6EyQ0YKdJIfz1tyMdctHa', '+91-9000000002', 'INDUSTRY', 1, 2),
  ('tatasteel.admin@example.com', 'Tata Steel Foundation Admin', '$2b$10$V2olv2c8aL0ItOm6wlW1L.wnuIVS8AfQ6EyQ0YKdJIfz1tyMdctHa', '+91-9200000001', 'INDUSTRY', 1, 3),
  ('sail.admin@example.com', 'SAIL Bokaro Steel Plant Admin', '$2b$10$V2olv2c8aL0ItOm6wlW1L.wnuIVS8AfQ6EyQ0YKdJIfz1tyMdctHa', '+91-9200000002', 'INDUSTRY', 1, 4),
  ('cil.admin@example.com', 'Coal India Limited Admin', '$2b$10$V2olv2c8aL0ItOm6wlW1L.wnuIVS8AfQ6EyQ0YKdJIfz1tyMdctHa', '+91-9200000003', 'INDUSTRY', 1, 5),
  ('jreda.admin@example.com', 'JREDA Admin', '$2b$10$V2olv2c8aL0ItOm6wlW1L.wnuIVS8AfQ6EyQ0YKdJIfz1tyMdctHa', '+91-9200000004', 'INDUSTRY', 1, 6),
  ('cognizant.admin@example.com', 'Cognizant Jharkhand Innovation Hub Admin', '$2b$10$V2olv2c8aL0ItOm6wlW1L.wnuIVS8AfQ6EyQ0YKdJIfz1tyMdctHa', '+91-9200000005', 'INDUSTRY', 1, 7)
ON DUPLICATE KEY UPDATE
  name = VALUES(name),
  password_hash = VALUES(password_hash),
  phone = VALUES(phone),
  role = VALUES(role),
  approved = VALUES(approved),
  industry_id = VALUES(industry_id);

-- Seed 7 statewide Jharkhand government/local bodies that handle routine civic
-- issues directly (no university/industry innovation needed).
INSERT INTO local_bodies (id, name, jurisdiction, contact_email, contact_phone) VALUES
  (1, 'Jharkhand Municipal Administration', 'Statewide (Urban Local Bodies)', 'admin@jharkhandmunicipal.example.com', '+91-9300000001'),
  (2, 'Jharkhand Panchayati Raj Department', 'Statewide (Rural Local Bodies)', 'admin@jhpanchayatiraj.example.com', '+91-9300000002'),
  (3, 'PHED / Jal Nigam Jharkhand', 'Statewide', 'admin@jalnigam.example.com', '+91-9300000003'),
  (4, 'JBVNL (Jharkhand Bijli Vitran Nigam Limited)', 'Statewide', 'admin@jbvnl.example.com', '+91-9300000004'),
  (5, 'Jharkhand Health Department (PHC/CHC Network)', 'Statewide', 'admin@jhhealth.example.com', '+91-9300000005'),
  (6, 'Jharkhand Education Department', 'Statewide', 'admin@jheducation.example.com', '+91-9300000006'),
  (7, 'JSPCB (Jharkhand State Pollution Control Board)', 'Statewide', 'admin@jspcb.example.com', '+91-9300000007')
ON DUPLICATE KEY UPDATE
  name = VALUES(name),
  jurisdiction = VALUES(jurisdiction),
  contact_email = VALUES(contact_email),
  contact_phone = VALUES(contact_phone);

DELETE FROM local_body_disciplines WHERE local_body_id BETWEEN 1 AND 7;

INSERT INTO local_body_disciplines (local_body_id, discipline) VALUES
  (1, 'URBAN_INFRASTRUCTURE'),
  (1, 'SANITATION'),
  (1, 'PUBLIC_SERVICE'),
  (2, 'RURAL_LIVELIHOOD'),
  (2, 'WATER'),
  (2, 'SANITATION'),
  (3, 'WATER'),
  (4, 'URBAN_INFRASTRUCTURE'),
  (5, 'HEALTHCARE'),
  (6, 'EDUCATION'),
  (7, 'ENVIRONMENT');

-- Pre-approved login accounts, one admin per seeded local body (no self-registration).
-- Login password for every seeded local body account: LocalBody@123
INSERT INTO users (email, name, password_hash, phone, role, approved, local_body_id) VALUES
  ('municipal.admin@example.com', 'Jharkhand Municipal Administration Admin', '$2b$10$ToBAIbnVlsCRGhfO23EZY.LV65aXTKAdFnR5GkMM5rffK7jKhSFU.', '+91-9400000001', 'LOCAL_BODY_ADMIN', 1, 1),
  ('panchayat.admin@example.com', 'Jharkhand Panchayati Raj Dept Admin', '$2b$10$ToBAIbnVlsCRGhfO23EZY.LV65aXTKAdFnR5GkMM5rffK7jKhSFU.', '+91-9400000002', 'LOCAL_BODY_ADMIN', 1, 2),
  ('phed.admin@example.com', 'PHED / Jal Nigam Jharkhand Admin', '$2b$10$ToBAIbnVlsCRGhfO23EZY.LV65aXTKAdFnR5GkMM5rffK7jKhSFU.', '+91-9400000003', 'LOCAL_BODY_ADMIN', 1, 3),
  ('jbvnl.admin@example.com', 'JBVNL Admin', '$2b$10$ToBAIbnVlsCRGhfO23EZY.LV65aXTKAdFnR5GkMM5rffK7jKhSFU.', '+91-9400000004', 'LOCAL_BODY_ADMIN', 1, 4),
  ('health.admin@example.com', 'Jharkhand Health Department Admin', '$2b$10$ToBAIbnVlsCRGhfO23EZY.LV65aXTKAdFnR5GkMM5rffK7jKhSFU.', '+91-9400000005', 'LOCAL_BODY_ADMIN', 1, 5),
  ('education.admin@example.com', 'Jharkhand Education Department Admin', '$2b$10$ToBAIbnVlsCRGhfO23EZY.LV65aXTKAdFnR5GkMM5rffK7jKhSFU.', '+91-9400000006', 'LOCAL_BODY_ADMIN', 1, 6),
  ('jspcb.admin@example.com', 'JSPCB Admin', '$2b$10$ToBAIbnVlsCRGhfO23EZY.LV65aXTKAdFnR5GkMM5rffK7jKhSFU.', '+91-9400000007', 'LOCAL_BODY_ADMIN', 1, 7)
ON DUPLICATE KEY UPDATE
  name = VALUES(name),
  password_hash = VALUES(password_hash),
  phone = VALUES(phone),
  role = VALUES(role),
  approved = VALUES(approved),
  local_body_id = VALUES(local_body_id);
