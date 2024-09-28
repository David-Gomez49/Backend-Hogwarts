INSERT INTO roles (id, name,access) VALUES (1, 'Student',TRUE), (2, 'Parent',TRUE),
 (3, 'Teacher',FALSE), (4, 'Admin',FALSE);

INSERT INTO users (
    id, 
    id_rol, 
    name, 
    lastname, 
    birthday, 
    gender, 
    address, 
    phone, 
    email, 
    document_type, 
    document_number
) VALUES (
    1,                 
    4,                 
    'Santiago',        
    'Trespalacios',           
    '2003-02-09',      
    'Masculino',               
    'Calle 123',       
    '123456789',       
    'santiagot3p@gmail.com', 
    'CC',              
    '1234567890'       
);