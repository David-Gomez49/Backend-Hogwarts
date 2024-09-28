INSERT INTO roles ( name,access) VALUES ( 'Student',TRUE), ( 'Parent',TRUE),
 ( 'Teacher',FALSE), ( 'Admin',FALSE);

INSERT INTO users (
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