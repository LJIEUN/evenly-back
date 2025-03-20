INSERT INTO products (name, price, description, image_url, category, stock, status, created_at)
SELECT 'Barro Plate-Set of 2-Ø18-Dark blue',
       35000,
       CONCAT('Designed by Portuguese Rui Pereira, the Barro Plate is part of a tableware collection that brings the warmth and beautiful simplicity of terracotta to the table. Meaning ‘red clay’ in Portuguese, Barro represents the designer’s appreciation of the traditions and uses associated with this ancient material. Barro Plate’s rolled-edge detail combined with its soft touch and proportions create a sense of stability that makes the plate pleasurable to use. Crafted in Portugal from durable terracotta, the Barro Plate is glazed in a variety of high gloss colours that can be matched or mixed in innumerable personalised combinations. The plates come in a set of two.' , CHAR(10), 'Color: Dark blue
 ', CHAR(10),'Size: D18') ,
       'https://www.hay.com/img_20230907111544/globalassets/inriver/integration/service/ac459-a668-ai60-02au_barro-plate-oe18-set-of-2-dark-blue_gb_1220x1220_brandvariant.jpg?w=600',
       'Kitchen',
       0,
       'SOLD_OUT',
       CONVERT_TZ(NOW(), 'UTC', 'Asia/Seoul')
WHERE NOT EXISTS (
    SELECT 1 FROM products WHERE name = 'Barro Plate-Set of 2-Ø18-Dark blue'
);

INSERT INTO products (name, price, description, image_url, category, stock, status, created_at)
SELECT 'Apollo Portable Lamp-White opal glass',
       150000,
       CONCAT('Designed by STUDIO 0405, the Apollo Portable Lamp is a portable and free-standing lamp for indoor and outdoor use that creates space and intimacy wherever it is placed. Hand-made in a single piece of opal glass, its rounded curves and unified silhouette are a contemporary interpretation of the classic lamp form. The light source and rechargeable battery are concealed within the shade, illuminating the upper part to provide a warm, ambient glow. Apollo''s portability and deliberate simplicity of design make it suitable for using in many rooms in a domestic environment, as well as cafés, patios and other outdoor spaces.', CHAR(10), 'Size:', CHAR(10), 'H21.8 x W12.5 x L12.5', CHAR(10), 'Color: White', CHAR(10), 'Material: Glass', CHAR(10),
              'Cord: 100 cm', CHAR(10), 'Bulb: LED G4', CHAR(10), 'Dimmable: Yes', CHAR(10),
              'Switch: Touch step dimmer', CHAR(10), 'IP: 44', CHAR(10), 'Supply: 5V, 2A'),
       'https://www.hay.com/img_20240404083132/globalassets/inriver/integration/service/ae378-b508_apollo-portable-white_gb_1220x1220_brandvariant.jpg?w=600',
       'Lighting',
       10,
       'AVAILABLE',
       CONVERT_TZ(NOW(), 'UTC', 'Asia/Seoul')
WHERE NOT EXISTS (
    SELECT 1 FROM products WHERE name = 'Apollo Portable Lamp-White opal glass'
);