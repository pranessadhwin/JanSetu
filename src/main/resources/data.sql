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
